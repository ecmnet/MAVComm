package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_attitude_target;

import com.comino.msp.model.segment.Status;
import com.comino.msp.utils.MSPMathUtils;

public class PX4AttitudeTargetPlugin extends MAVLinkPluginBase {

	private final float[] sp = new float[3];

	public PX4AttitudeTargetPlugin() {
		super(msg_attitude_target.class);
	}

	@Override
	public void received(Object o) {

		msg_attitude_target att = (msg_attitude_target) o;

		MSPMathUtils.eulerAnglesByQuaternion(sp, att.q);

		model.attitude.sr = sp[0];
		model.attitude.sp = sp[1];
		model.attitude.sy = sp[2];

		if(model.sys.isStatus(Status.MSP_ARMED))
		    model.attitude.st = att.thrust;
		else
			model.attitude.st = 0;

		model.attitude.srr = att.body_roll_rate;
		model.attitude.spr = att.body_pitch_rate;
		model.attitude.syr = att.body_yaw_rate;
	}
}
