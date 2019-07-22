package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.MAV_LANDED_STATE;
import org.mavlink.messages.lquac.msg_extended_sys_state;

import com.comino.msp.model.segment.Status;

public class PX4ExtendedSysStatusPlugin extends MAVLinkPluginBase {

	public PX4ExtendedSysStatusPlugin() {
		super(msg_extended_sys_state.class);
	}

	@Override
	public void received(Object o) {

		msg_extended_sys_state sys = (msg_extended_sys_state) o;

		switch(sys.landed_state) {
		case MAV_LANDED_STATE.MAV_LANDED_STATE_ON_GROUND:
			  model.sys.setStatus(Status.MSP_LANDED,true);
			break;
		case MAV_LANDED_STATE.MAV_LANDED_STATE_IN_AIR:
			model.sys.setStatus(Status.MSP_LANDED,false);
			break;
		case MAV_LANDED_STATE.MAV_LANDED_STATE_TAKEOFF:
		//	model.sys.nav_state = Status.NAVIGATION_STATE_AUTO_TAKEOFF;
			model.sys.setStatus(Status.MSP_LANDED,false);
			break;
		case MAV_LANDED_STATE.MAV_LANDED_STATE_LANDING:
		//	model.sys.nav_state = Status.NAVIGATION_STATE_AUTO_LAND;
			model.sys.setStatus(Status.MSP_LANDED,false);
			break;

		}

	}
}
