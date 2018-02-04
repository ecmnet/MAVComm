package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_attitude_quaternion;

public class PX4AttitudeQuaternionPlugin extends MAVLinkPluginBase {


	public PX4AttitudeQuaternionPlugin() {
		super(msg_attitude_quaternion.class);
	}

	@Override
	public void received(Object o) {

		msg_attitude_quaternion att = (msg_attitude_quaternion) o;

		model.attitude.q1 = att.q1;
		model.attitude.q2 = att.q2;
		model.attitude.q3 = att.q3;
		model.attitude.q4 = att.q4;


	}
}
