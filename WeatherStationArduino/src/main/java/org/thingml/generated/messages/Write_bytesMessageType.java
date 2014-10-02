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

public class Write_bytesMessageType extends EventType {
	public Write_bytesMessageType() {
		name = "write_bytes";
	}

	public Event instantiate(final Port port, final byte[] b) {
		return new Write_bytesMessage(this, port, b);
	}
	public class Write_bytesMessage extends Event
			implements
				java.io.Serializable {

		public final byte[] b;
		@Override
		public String toString() {
			return "Write_bytes " + "byte[]: " + b;
		}

		protected Write_bytesMessage(EventType type, Port port, final byte[] b) {
			super(type, port);
			this.b = b;
		}
	}

}
