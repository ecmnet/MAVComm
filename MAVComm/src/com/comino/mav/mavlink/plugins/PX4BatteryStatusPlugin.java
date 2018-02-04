package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_battery_status;

public class PX4BatteryStatusPlugin extends MAVLinkPluginBase {

	public PX4BatteryStatusPlugin() {
		super(msg_battery_status.class);
	}

	@Override
	public void received(Object o) {
		msg_battery_status bat = (msg_battery_status) o;
		if (bat.current_consumed > 0)
			model.battery.a0 = bat.current_consumed;
	}
}
