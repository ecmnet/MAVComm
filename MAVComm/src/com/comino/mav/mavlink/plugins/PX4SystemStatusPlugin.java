package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.MAV_SYS_STATUS_SENSOR;
import org.mavlink.messages.lquac.msg_sys_status;

import com.comino.msp.model.segment.Status;

public class PX4SystemStatusPlugin extends MAVLinkPluginBase {

	public PX4SystemStatusPlugin() {
		super(msg_sys_status.class);
	}

	@Override
	public void received(Object o) {

		msg_sys_status sys = (msg_sys_status) o;
		model.battery.p = (short) sys.battery_remaining;
		model.battery.b0 = sys.voltage_battery / 1000f;
		model.battery.c0 = sys.current_battery / 100f;
		model.battery.tms = System.currentTimeMillis() * 1000;

		model.sys.error1 = sys.errors_count1;
		model.sys.load_p = sys.load / 10;
		model.sys.drops_p = sys.drop_rate_comm / 10000f;

		// Sensor availability

		model.sys.setSensor(Status.MSP_PIX4FLOW_AVAILABILITY, (sys.onboard_control_sensors_enabled
				& MAV_SYS_STATUS_SENSOR.MAV_SYS_STATUS_SENSOR_OPTICAL_FLOW) > 0);

	}
}
