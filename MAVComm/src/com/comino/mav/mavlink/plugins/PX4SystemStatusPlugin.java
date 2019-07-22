package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_sys_status;

public class PX4SystemStatusPlugin extends MAVLinkPluginBase {

	public PX4SystemStatusPlugin() {
		super(msg_sys_status.class);
	}

	@Override
	public void received(Object o) {

		msg_sys_status sys = (msg_sys_status) o;
		if(sys.battery_remaining != -1) {
			model.battery.p = (short) sys.battery_remaining;
			model.battery.b0 = sys.voltage_battery / 1000f;
			model.battery.c0 = sys.current_battery / 100f;
			model.battery.tms = System.currentTimeMillis() * 1000;
		}

		model.sys.error1 = sys.errors_count1;
		if(sys.load > 0)
			model.sys.load_p = sys.load / 10;
		model.sys.drops_p = sys.drop_rate_comm / 10000f;


	}
}
