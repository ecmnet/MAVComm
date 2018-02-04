package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_global_position_int;

import com.comino.msp.model.segment.Status;

public class PX4GlobalPositionPlugin extends MAVLinkPluginBase {

	public PX4GlobalPositionPlugin() {
		super(msg_global_position_int.class);
	}

	@Override
	public void received(Object o) {

		msg_global_position_int pos = (msg_global_position_int) o;
		model.state.g_lat = pos.lat / 1.0e7;
		model.state.g_lon = pos.lon / 1.0e7;
		model.state.g_alt = (pos.alt / 1000);
		model.gps.heading = (short) (pos.hdg / 1000f);
		model.gps.altitude = (short) (pos.alt / 1000);
		model.state.g_vx = pos.vx / 100f;
		model.state.g_vy = pos.vy / 100f;
		model.state.g_vz = pos.vz / 100f;

		if(pos.lat!=0 && pos.lon!=0) {
			model.state.gpos_tms = model.sys.getSynchronizedPX4Time_us();
			model.sys.setStatus(Status.MSP_GPOS_VALID, true);
		}

	}
}
