package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_odometry;

public class PX4OdometryPlugin extends MAVLinkPluginBase {

	public PX4OdometryPlugin() {
		super(msg_odometry.class);
	}

	@Override
	public void received(Object o) {

		msg_odometry odom = (msg_odometry) o;


	}
}
