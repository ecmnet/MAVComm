package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_estimator_status;

public class PX4EstimatorStatusPlugin extends MAVLinkPluginBase {

	public PX4EstimatorStatusPlugin() {
		super(msg_estimator_status.class);
	}

	@Override
	public void received(Object o) {

		msg_estimator_status est = (msg_estimator_status) o;
		model.est.haglRatio = est.hagl_ratio;
		model.est.magRatio = est.mag_ratio;
		model.est.horizRatio = est.pos_horiz_ratio;
		model.est.vertRatio = est.pos_vert_ratio;
		model.est.posHorizAccuracy = est.pos_horiz_accuracy;
		model.est.posVertAccuracy = est.pos_vert_accuracy;
		model.est.flags = est.flags;
		model.est.tasRatio = est.tas_ratio;
		model.est.velRatio = est.vel_ratio;

		model.est.tms = est.time_usec;

	}
}
