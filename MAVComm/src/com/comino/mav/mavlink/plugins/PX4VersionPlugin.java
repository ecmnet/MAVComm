package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_autopilot_version;

public class PX4VersionPlugin extends MAVLinkPluginBase {

	public PX4VersionPlugin() {
		super(msg_autopilot_version.class);
	}

	@Override
	public void received(Object o) {

		msg_autopilot_version version = (msg_autopilot_version) o;
		model.sys.version = String.format("%d.%d.%d", (version.flight_sw_version >> (8 * 3)) & 0xFF,
				(version.flight_sw_version >> (8 * 2)) & 0xFF, (version.flight_sw_version >> (8 * 1)) & 0xFF);
		//	System.out.println("Version: " + model.sys.version);
	}
}
