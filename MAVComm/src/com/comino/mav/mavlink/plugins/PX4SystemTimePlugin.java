package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_system_time;

public class PX4SystemTimePlugin extends MAVLinkPluginBase {

	public PX4SystemTimePlugin() {
		super(msg_system_time.class);
	}

	@Override
	public void received(Object o) {
		msg_system_time time = (msg_system_time) o;
		model.sys.t_boot_ms = time.time_boot_ms;
	}
}
