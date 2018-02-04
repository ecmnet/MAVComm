package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_manual_control;

import com.comino.msp.model.segment.Status;

public class PX4ManualControlPlugin extends MAVLinkPluginBase {

	public PX4ManualControlPlugin() {
		super(msg_manual_control.class);
	}

	@Override
	public void received(Object o) {
		model.sys.setStatus(Status.MSP_JOY_ATTACHED, true);
	}
}
