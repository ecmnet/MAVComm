package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_msp_micro_slam;

public class MspMicroSlamPlugin extends MAVLinkPluginBase {

	public MspMicroSlamPlugin() {
		super(msg_msp_micro_slam.class);
	}

	@Override
	public void received(Object o) {

		msg_msp_micro_slam slam = (msg_msp_micro_slam) o;
		model.slam.pd = slam.pd;
		model.slam.pp = slam.pp;
		model.slam.pv = slam.pv;
		model.slam.px = slam.px;
		model.slam.py = slam.py;
		model.slam.pz = slam.pz;
		model.slam.di = slam.md;
		model.slam.ox = slam.ox;
		model.slam.oy = slam.oy;
		model.slam.oz = slam.oz;
		model.grid.tms = model.sys.getSynchronizedPX4Time_us();

	}
}
