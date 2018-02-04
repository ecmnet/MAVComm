package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_attitude;

import com.comino.msp.model.segment.Status;

public class PX4AttitudePlugin extends MAVLinkPluginBase {


	public PX4AttitudePlugin() {
		super(msg_attitude.class);
	}

	@Override
	public void received(Object o) {

		msg_attitude att = (msg_attitude) o;

		model.attitude.r = att.roll;
		model.attitude.p = att.pitch;
		model.attitude.y = att.yaw;
		model.state.h    = model.hud.h;

		model.attitude.rr = att.rollspeed;
		model.attitude.pr = att.pitchspeed;
		model.attitude.yr = att.yawspeed;

		model.attitude.tms = model.sys.getSynchronizedPX4Time_us();

		model.hud.aX = att.roll;
		model.hud.aY = att.pitch;

		model.sys.setSensor(Status.MSP_IMU_AVAILABILITY, true);

	}
}
