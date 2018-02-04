package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_vision_position_estimate;

import com.comino.msp.utils.MSPMathUtils;

public class PX4VisionPositionEstimatePlugin extends MAVLinkPluginBase {

	public PX4VisionPositionEstimatePlugin() {
		super(msg_vision_position_estimate.class);
	}

	@Override
	public void received(Object o) {

		msg_vision_position_estimate mocap = (msg_vision_position_estimate) o;
		model.vision.x = mocap.x;
		model.vision.y = mocap.y;
		model.vision.z = mocap.z;
		model.vision.h = MSPMathUtils.fromRad(mocap.yaw);
		model.vision.p = mocap.pitch;
		model.vision.r = mocap.roll;

	}
}
