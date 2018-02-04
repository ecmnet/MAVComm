package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_altitude;

public class PX4AltitudePlugin extends MAVLinkPluginBase {

	public PX4AltitudePlugin() {
		super(msg_altitude.class);
	}

	@Override
	public void received(Object o) {

		msg_altitude alt = (msg_altitude) o;
		model.hud.al = alt.altitude_local;
		model.hud.ag = alt.altitude_amsl;
		model.hud.at = alt.altitude_terrain;
		model.hud.ar = alt.altitude_relative;
		model.hud.bc = alt.bottom_clearance;
		model.hud.tms = model.sys.getSynchronizedPX4Time_us();

	}
}
