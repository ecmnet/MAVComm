package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_ping;

import com.comino.msp.model.segment.Status;

public class PX4Ping extends MAVLinkPluginBase {


	public PX4Ping() {
		super(msg_ping.class);
	}

	@Override
	public void received(Object o) {

		model.sys.setSensor(Status.MSP_IMU_AVAILABILITY, true);
		model.sys.setSensor(Status.MSP_SYSM_AVAILABILITY, true);

	}
}
