package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_debug_vect;

public class PX4DebugVectorPlugin extends MAVLinkPluginBase {

	public PX4DebugVectorPlugin() {
		super(msg_debug_vect.class);
	}

	@Override
	public void received(Object o) {

		msg_debug_vect vec = (msg_debug_vect) o;
		model.debug.tms = vec.time_usec;
		model.debug.x = vec.x;
		model.debug.y = vec.y;
		model.debug.z = vec.z;

	}
}
