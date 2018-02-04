package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_local_position_ned;

import com.comino.msp.model.segment.Status;

public class PX4LocalPositionPlugin extends MAVLinkPluginBase {

	public PX4LocalPositionPlugin() {
		super(msg_local_position_ned.class);
	}

	@Override
	public void received(Object o) {

		msg_local_position_ned ned = (msg_local_position_ned) o;

		model.state.l_x = ned.x;
		model.state.l_y = ned.y;
		model.state.l_z = ned.z;

		model.state.l_vx = ned.vx;
		model.state.l_vy = ned.vy;
		model.state.l_vz = ned.vz;

		model.state.v = (float) Math.sqrt(ned.vx * ned.vx + ned.vy * ned.vy);

		if((ned.x!=0 || ned.y!=0) && Float.isFinite(ned.x) && Float.isFinite(ned.y)) {
			model.state.tms = model.sys.getSynchronizedPX4Time_us();
			model.sys.setStatus(Status.MSP_LPOS_VALID, true);
		}

	}
}
