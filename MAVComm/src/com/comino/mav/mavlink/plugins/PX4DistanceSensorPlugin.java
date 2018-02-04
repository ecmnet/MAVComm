package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_distance_sensor;

import com.comino.msp.model.segment.Status;

public class PX4DistanceSensorPlugin extends MAVLinkPluginBase {

	public PX4DistanceSensorPlugin() {
		super(msg_distance_sensor.class);
	}

	@Override
	public void received(Object o) {

		msg_distance_sensor lidar = (msg_distance_sensor) o;
		model.raw.di = lidar.current_distance / 100f;
		model.raw.dicov = lidar.covariance / 100f;
		model.sys.setSensor(Status.MSP_LIDAR_AVAILABILITY, true);

	}
}
