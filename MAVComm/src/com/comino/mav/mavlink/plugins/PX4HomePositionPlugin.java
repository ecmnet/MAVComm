package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_home_position;

public class PX4HomePositionPlugin extends MAVLinkPluginBase {

	public PX4HomePositionPlugin() {
		super(msg_home_position.class);
	}

	@Override
	public void received(Object o) {

		msg_home_position ref = (msg_home_position) o;

		model.home_state.l_x = ref.x;
		model.home_state.l_y = ref.y;
		model.home_state.l_z = ref.z;

		model.home_state.g_lat = ref.latitude  / 1e7;
		model.home_state.g_lon = ref.longitude / 1e7;
		model.home_state.g_alt = (int) ((ref.altitude + 500) / 1000f);


	}
}
