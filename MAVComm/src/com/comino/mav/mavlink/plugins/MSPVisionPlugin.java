package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_msp_vision;

import com.comino.msp.model.segment.Status;

public class MSPVisionPlugin extends MAVLinkPluginBase {

	public MSPVisionPlugin() {
		super(msg_msp_vision.class);
	}

	@Override
	public void received(Object o) {

		msg_msp_vision mocap = (msg_msp_vision) o;
		model.vision.vx = mocap.vx;
		model.vision.vy = mocap.vy;
		model.vision.vz = mocap.vz;

		model.vision.cov_px = mocap.cov_px;
		model.vision.cov_py = mocap.cov_py;
		model.vision.cov_pz = mocap.cov_pz;

		model.vision.cov_vx = mocap.cov_vx;
		model.vision.cov_vy = mocap.cov_vy;
		model.vision.cov_vz = mocap.cov_vz;

		// model.vision.x = mocap.x;
		// model.vision.y = mocap.y;
		// model.vision.z = mocap.z;
		//
		// model.vision.h= mocap.h;
		// model.vision.p= mocap.p;
		// model.vision.r= mocap.r;

		model.vision.qual = mocap.quality;
		model.vision.errors = (int) mocap.errors;

		model.vision.flags = (int) mocap.flags;
		model.vision.fps = mocap.fps;
		if (model.vision.errors < 5 && ( mocap.vx !=0 || mocap.vy!=0)) {
			model.vision.tms = model.sys.getSynchronizedPX4Time_us();
			model.sys.setSensor(Status.MSP_OPCV_AVAILABILITY, true);
		}
	}
}
