package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_position_target_local_ned;

import com.comino.msp.utils.MSPMathUtils;

public class PX4PositionTargetLocalPlugin extends MAVLinkPluginBase {

	public PX4PositionTargetLocalPlugin() {
		super(msg_position_target_local_ned.class);
	}

	@Override
	public void received(Object o) {

		msg_position_target_local_ned ned = (msg_position_target_local_ned) o;

		model.target_state.l_x = ned.x;
		model.target_state.l_y = ned.y;
		model.target_state.l_z = ned.z;

		model.target_state.h = MSPMathUtils.fromRad(ned.yaw);

		model.target_state.l_vx = ned.vx;
		model.target_state.l_vy = ned.vy;
		model.target_state.l_vz = ned.vz;

		model.target_state.vh = MSPMathUtils.fromRad(ned.yaw_rate);

		model.target_state.l_ax = ned.afx;
		model.target_state.l_ay = ned.afy;
		model.target_state.l_az = ned.afz;

		model.target_state.c_frame = ned.coordinate_frame;

		model.target_state.tms = model.sys.getSynchronizedPX4Time_us();

	}
}
