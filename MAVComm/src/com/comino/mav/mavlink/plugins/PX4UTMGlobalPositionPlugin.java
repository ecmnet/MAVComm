package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_odometry;

public class PX4UTMGlobalPositionPlugin extends MAVLinkPluginBase {

	public PX4UTMGlobalPositionPlugin() {
		super(msg_odometry.class);
	}

	@Override
	public void received(Object o) {

		msg_odometry odom = (msg_odometry) o;
//		model.vision.x = mocap.x;
//		model.vision.y = mocap.y;
//		model.vision.z = mocap.z;
//		model.vision.h = MSPMathUtils.fromRad(mocap.yaw);
//		model.vision.p = mocap.pitch;
//		model.vision.r = mocap.roll;

	}
}
