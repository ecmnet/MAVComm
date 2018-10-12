package com.comino.msp.utils.px4;

import java.io.UnsupportedEncodingException;

import org.mavlink.messages.SERIAL_CONTROL_DEV;
import org.mavlink.messages.SERIAL_CONTROL_FLAG;
import org.mavlink.messages.lquac.msg_play_tune;
import org.mavlink.messages.lquac.msg_serial_control;

import com.comino.mav.control.IMAVController;

public class DefaultTunes {

	public static final String NOTIFY_POSITIVE = "MFT200e8a8a";
	public static final String NOTIFY_NEGATIVE = "MFT200e8c8e8c8e8c8";

	public static void play(IMAVController control, String tune_string) {
		// TODO: Does not work with PX4 1.8
//		msg_play_tune tune = new msg_play_tune(1,2);
//		tune.target_system=0;
//		tune.target_component=0;
//		tune.setTune(tune_string);
//		control.sendMAVLinkMessage(tune);
		writeToShell(control, tune_string);
	}

	private static void writeToShell(IMAVController control, String s) {
		String tune = "tune_control play -m "+s+"\n";
		msg_serial_control msg = new msg_serial_control(1,1);
		try {
			byte[] bytes = tune.getBytes("US-ASCII");
			for(int i =0;i<bytes.length && i<70;i++)
				msg.data[i] = bytes[i];
			msg.count = bytes.length;
			msg.device = SERIAL_CONTROL_DEV.SERIAL_CONTROL_DEV_SHELL;
			msg.flags  = SERIAL_CONTROL_FLAG.SERIAL_CONTROL_FLAG_RESPOND;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		control.sendMAVLinkMessage(msg);
		System.out.println("Played: "+s);
	}

}
