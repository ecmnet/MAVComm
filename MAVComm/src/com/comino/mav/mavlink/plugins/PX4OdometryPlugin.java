package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_utm_global_position;

public class PX4OdometryPlugin extends MAVLinkPluginBase {

	public PX4OdometryPlugin() {
		super(msg_utm_global_position.class);
	}

	@Override
	public void received(Object o) {

		msg_utm_global_position utem = (msg_utm_global_position) o;


	}
}
