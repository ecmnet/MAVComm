package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.MAV_MODE_FLAG;
import org.mavlink.messages.MAV_MODE_FLAG_DECODE_POSITION;
import org.mavlink.messages.MAV_STATE;
import org.mavlink.messages.lquac.msg_heartbeat;

import com.comino.mav.mavlink.MAV_CUST_MODE;
import com.comino.msp.model.segment.Status;

public class PX4HeartBeatPlugin extends MAVLinkPluginBase {

	public PX4HeartBeatPlugin() {
		super(msg_heartbeat.class);
	}

	@Override
	public void received(Object o) {
		msg_heartbeat hb = (msg_heartbeat) o;

		model.sys.px4_status = hb.system_status;
		model.sys.nav_state  = 0;

		model.sys.setStatus(Status.MSP_ARMED,
				(hb.base_mode & MAV_MODE_FLAG_DECODE_POSITION.MAV_MODE_FLAG_DECODE_POSITION_SAFETY) > 0);

		model.sys.setStatus(Status.MSP_READY, (hb.system_status & MAV_STATE.MAV_STATE_STANDBY) > 0);
		model.sys.setStatus(Status.MSP_ARMED, (hb.base_mode & MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED) != 0);

		model.sys.setStatus(Status.MSP_CONNECTED, true);

		model.sys.setStatus(Status.MSP_MODE_ALTITUDE,
				MAV_CUST_MODE.is(hb.custom_mode, MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_ALTCTL));
		if(model.sys.isStatus(Status.MSP_MODE_ALTITUDE))
			model.sys.nav_state = Status.NAVIGATION_STATE_ALTCTL;


		model.sys.setStatus(Status.MSP_MODE_POSITION,
				MAV_CUST_MODE.is(hb.custom_mode, MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_POSCTL));
		if(model.sys.isStatus(Status.MSP_MODE_POSITION))
			model.sys.nav_state = Status.NAVIGATION_STATE_POSCTL;

		model.sys.setStatus(Status.MSP_MODE_OFFBOARD,
				MAV_CUST_MODE.is(hb.custom_mode, MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_OFFBOARD));
		if(model.sys.isStatus(Status.MSP_MODE_OFFBOARD))
			model.sys.nav_state = Status.NAVIGATION_STATE_OFFBOARD;

		model.sys.setStatus(Status.MSP_MODE_STABILIZED,
				MAV_CUST_MODE.is(hb.custom_mode, MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_STABILIZED));

		model.sys.setStatus(Status.MSP_MODE_LOITER, MAV_CUST_MODE.is(hb.custom_mode,
				MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_AUTO, MAV_CUST_MODE.PX4_CUSTOM_SUB_MODE_AUTO_LOITER));
		if(model.sys.isStatus(Status.MSP_MODE_LOITER))
			model.sys.nav_state = Status.NAVIGATION_STATE_AUTO_LOITER;

		model.sys.setStatus(Status.MSP_MODE_MISSION, MAV_CUST_MODE.is(hb.custom_mode,
				MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_AUTO, MAV_CUST_MODE.PX4_CUSTOM_SUB_MODE_AUTO_MISSION));
		if(model.sys.isStatus(Status.MSP_MODE_MISSION))
			model.sys.nav_state = Status.NAVIGATION_STATE_AUTO_MISSION;

		model.sys.setStatus(Status.MSP_MODE_LANDING, MAV_CUST_MODE.is(hb.custom_mode,
				MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_AUTO, MAV_CUST_MODE.PX4_CUSTOM_SUB_MODE_AUTO_LAND));
		if(model.sys.isStatus(Status.MSP_MODE_LANDING))
			model.sys.nav_state = Status.NAVIGATION_STATE_AUTO_LAND;

		model.sys.setStatus(Status.MSP_MODE_RTL, MAV_CUST_MODE.is(hb.custom_mode,
				MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_AUTO, MAV_CUST_MODE.PX4_CUSTOM_SUB_MODE_AUTO_RTL));

		model.sys.setStatus(Status.MSP_MODE_TAKEOFF, MAV_CUST_MODE.is(hb.custom_mode,
				MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_AUTO, MAV_CUST_MODE.PX4_CUSTOM_SUB_MODE_AUTO_TAKEOFF));
		if(model.sys.isStatus(Status.MSP_MODE_TAKEOFF))
			model.sys.nav_state = Status.NAVIGATION_STATE_AUTO_TAKEOFF;

		//System.err.println(Long.toBinaryString(hb.custom_mode));

	}
}
