package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_vfr_hud;


public class PX4VfrHUDPlugin extends MAVLinkPluginBase {

	public PX4VfrHUDPlugin() {
		super(msg_vfr_hud.class);
	}

	@Override
	public void received(Object o) {

		msg_vfr_hud hud = (msg_vfr_hud) o;
		model.hud.s = hud.groundspeed;
		model.hud.vs = hud.climb;
		model.hud.h  = hud.heading;
		model.hud.as = hud.airspeed;

	}
}
