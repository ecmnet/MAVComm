package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_optical_flow_rad;

import com.comino.msp.model.segment.Status;

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

		if(flow.quality > 0) {
		  model.sys.setSensor(Status.MSP_PIX4FLOW_AVAILABILITY, true);
		  model.raw.tms = model.sys.getSynchronizedPX4Time_us();
		}

	}
}
