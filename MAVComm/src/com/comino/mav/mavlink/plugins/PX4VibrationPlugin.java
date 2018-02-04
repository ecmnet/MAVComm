package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_vibration;

public class PX4VibrationPlugin extends MAVLinkPluginBase {

	public PX4VibrationPlugin() {
		super(msg_vibration.class);
	}

	@Override
	public void received(Object o) {

		msg_vibration vib = (msg_vibration) o;
		model.vibration.vibx = vib.vibration_x;
		model.vibration.viby = vib.vibration_y;
		model.vibration.vibz = vib.vibration_z;
		model.vibration.cli0 = vib.clipping_0;
		model.vibration.cli1 = vib.clipping_1;
		model.vibration.cli2 = vib.clipping_2;
		model.vibration.tms = vib.time_usec;

	}
}
