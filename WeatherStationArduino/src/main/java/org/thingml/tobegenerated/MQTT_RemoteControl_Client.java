package org.thingml.tobegenerated;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.thingml.generated.WeatherStation;
import org.thingml.generated.api.IWeatherStation_guiClient;

import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bmori on 11.09.2014.
 */
public class MQTT_RemoteControl_Client implements IWeatherStation_guiClient {

    MqttAsyncClient mqtt;
    WeatherStation weatherStation;
    IMqttToken token;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public MQTT_RemoteControl_Client(WeatherStation ws) {
        weatherStation = ws;
        try {
        mqtt = new MqttAsyncClient("tcp://localhost:1883", "WeatherStation", new MemoryPersistence());
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        System.out.println("Connecting to broker");
        token = mqtt.connect(connOpts);
        System.out.println("Connected");
                mqtt.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable e) {
                        System.err.println("MQTT connection lost. " + e.getLocalizedMessage());
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                        if (topic.equals("HEADS/WeatherStation/changeDisplay")) {
                            System.out.println("changeDisplay received on MQTT topic");
                           weatherStation.changeDisplay_via_gui();
                        }
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                    }
                });
            token.waitForCompletion();
            registerOnChangeDisplay();
        } catch (Exception e) {
            System.err.println("Cannot connect to MQTT Server. " + e.getLocalizedMessage());
        }
    }

    public void stop() {
        try {
            mqtt.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void registerOnChangeDisplay() {
        try {
            mqtt.subscribe("HEADS/WeatherStation/changeDisplay", 2);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private byte[] formatJSON(String deviceId, String type, String value) {
        final Date date = new Date();
        final StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("\"deviceId\":\"" + deviceId + "\",");
        builder.append("\"observationTime\":\"" + dateFormat.format(date) + "\",");
        builder.append("\"observations\":[");
        builder.append("{\"" + type + "\":\"" + value + "\"}");
        builder.append("]}");
        System.out.println(builder.toString());
        return builder.toString().getBytes();
    }

    @Override
    public void temperature_from_gui(short RemoteMonitoringMsgs_temperature_temp__var) {
        try {
            ByteBuffer bb = ByteBuffer.allocate(2);
            bb.putShort(RemoteMonitoringMsgs_temperature_temp__var);
            System.out.println("Publishing temperature: " + RemoteMonitoringMsgs_temperature_temp__var);
            MqttMessage message = new MqttMessage(bb.array());
            message.setQos(2);
            mqtt.publish("HEADS/WeatherStation/temperature", message);
            System.out.println("Message published:\n" + formatJSON("WeatherStation", "temperature", String.valueOf(RemoteMonitoringMsgs_temperature_temp__var)));
        } catch (Exception e) {
            System.err.println("Cannot publish on MQTT topic. " + e.getLocalizedMessage());
        }

    }

    @Override
    public void light_from_gui(short RemoteMonitoringMsgs_light_light__var) {
        try {
            ByteBuffer bb = ByteBuffer.allocate(2);
            bb.putShort(RemoteMonitoringMsgs_light_light__var);
            System.out.println("Publishing light: " + RemoteMonitoringMsgs_light_light__var);
            MqttMessage message = new MqttMessage(bb.array());
            message.setQos(2);
            mqtt.publish("HEADS/WeatherStation/light", message);
            System.out.println("Message published");
            System.out.println("Message published:\n" + formatJSON("WeatherStation", "temperature", String.valueOf(RemoteMonitoringMsgs_light_light__var)));
        } catch (Exception e) {
            System.err.println("Cannot publish on MQTT topic. " + e.getLocalizedMessage());
        }
    }

    public void sendChangeDisplay() {//just a test, should be removed later on
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 50; i++) {
                    try {
                        System.out.println("Publishing changeDisplay");
                        MqttMessage message = new MqttMessage();
                        message.setQos(2);
                        mqtt.publish("HEADS/WeatherStation/changeDisplay", message);
                        System.out.println("Message published");
                        Thread.currentThread().sleep(1000);
                    } catch (Exception e) {
                        System.err.println("Cannot publish on MQTT topic. " + e.getLocalizedMessage());
                    }
                }
            }
        }).start();
    }
}