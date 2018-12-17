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
		switch(lidar.type) {
		case 1:
			model.sys.setSensor(Status.MSP_SONAR_AVAILABILITY, true);
			model.sys.setSensor(Status.MSP_LIDAR_AVAILABILITY, false);
			break;
		default:
			model.sys.setSensor(Status.MSP_SONAR_AVAILABILITY, false);
			model.sys.setSensor(Status.MSP_LIDAR_AVAILABILITY, true);
		}

	}
}
