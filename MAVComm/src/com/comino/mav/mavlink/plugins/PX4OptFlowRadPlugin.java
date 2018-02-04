package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_optical_flow_rad;

public class PX4OptFlowRadPlugin extends MAVLinkPluginBase {

	public PX4OptFlowRadPlugin() {
		super(msg_optical_flow_rad.class);
	}

	@Override
	public void received(Object o) {

		msg_optical_flow_rad flow = (msg_optical_flow_rad) o;
		model.raw.fX = flow.integrated_x;
		model.raw.fY = flow.integrated_y;
		model.raw.fq = flow.quality;
		model.raw.fgX = flow.integrated_xgyro;
		model.raw.fgY = flow.integrated_ygyro;
		model.raw.fgZ = flow.integrated_zgyro;
		model.raw.fd = flow.distance;

		model.raw.tms = flow.time_usec;

	}
}
