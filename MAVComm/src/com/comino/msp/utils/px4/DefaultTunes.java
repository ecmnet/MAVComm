package com.comino.msp.utils.px4;

import org.mavlink.messages.lquac.msg_play_tune;

import com.comino.mav.control.IMAVController;

public class DefaultTunes {

	public static final String NOTIFY_POSITIVE = "MFT200e8a8a";
	public static final String NOTIFY_NEGATIVE = "MFT200e8c8e8c8e8c8";

	public static void play(IMAVController control, String tune_string) {
		msg_play_tune tune = new msg_play_tune(1,2);
		tune.target_system=1;
		tune.target_component=1;
		tune.setTune(tune_string);
		control.sendMAVLinkMessage(tune);
	}

}
