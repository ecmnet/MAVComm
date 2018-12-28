package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_rc_channels;

import com.comino.msp.model.segment.Status;


public class PX4RCChannelstPlugin extends MAVLinkPluginBase {

	public PX4RCChannelstPlugin() {
		super(msg_rc_channels.class);
	}

	@Override
	public void received(Object o) {

		msg_rc_channels rc = (msg_rc_channels) o;
		model.rc.rssi = (short) (rc.rssi);

		if(!model.sys.isStatus(Status.MSP_SITL))
			model.sys.setStatus(Status.MSP_RC_ATTACHED, (model.rc.rssi > 0));

		model.rc.s0  = rc.chan1_raw < 65534 ? (short) rc.chan1_raw : 1500;
		model.rc.s1  = rc.chan2_raw < 65534 ? (short) rc.chan2_raw : 1500;
		model.rc.s2  = rc.chan3_raw < 65534 ? (short) rc.chan3_raw : 1500;
		model.rc.s3  = rc.chan4_raw < 65534 ? (short) rc.chan4_raw : 1500;
		model.rc.s4  = rc.chan5_raw < 65534 ? (short) rc.chan5_raw : 1500;
		model.rc.s5  = rc.chan6_raw < 65534 ? (short) rc.chan6_raw : 1500;
		model.rc.s6  = rc.chan7_raw < 65534 ? (short) rc.chan7_raw : 1500;
		model.rc.s7  = rc.chan8_raw < 65534 ? (short) rc.chan8_raw : 1500;
		model.rc.tms = model.sys.getSynchronizedPX4Time_us();

	}
}
