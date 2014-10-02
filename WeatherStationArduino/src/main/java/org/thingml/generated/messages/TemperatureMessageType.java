/**
 * File generated by the ThingML IDE
 * /!\Do not edit this file/!\
 * In case of a bug in the generated code,
 * please submit an issue on our GitHub
 **/

package org.thingml.generated.messages;

import org.thingml.java.Port;
import org.thingml.java.ext.Event;
import org.thingml.java.ext.EventType;

public class TemperatureMessageType extends EventType {
	public TemperatureMessageType() {
		name = "temperature";
	}

	public Event instantiate(final Port port, final short temp) {
		return new TemperatureMessage(this, port, temp);
	}
	public class TemperatureMessage extends Event
			implements
				java.io.Serializable {

		public final short temp;
		@Override
		public String toString() {
			return "Temperature " + "short: " + temp;
		}

		protected TemperatureMessage(EventType type, Port port, final short temp) {
			super(type, port);
			this.temp = temp;
		}
	}

}
