package com.comino.msp.log;

import org.mavlink.messages.MAV_SEVERITY;

import com.comino.mav.control.IMAVController;

public class MSPLogger {

	// TOOD: register proxy, and send messages if proxy is registered

	private static MSPLogger log = null;
	private IMAVController control = null;
	private boolean debug_msg_enabled = false;


	public static MSPLogger getInstance(IMAVController control) {
		if(log==null) {
			log = new MSPLogger(control);
		}
		return log;
	}

	public static MSPLogger getInstance() {
		return log;
	}

	private MSPLogger(IMAVController control2) {
		this.control = control2;
	}

	public void enableDebugMessages(boolean enabled) {
		this.debug_msg_enabled = enabled;
	}

	public void writeLocalMsg(String msg) {
		writeLocalMsg(msg,MAV_SEVERITY.MAV_SEVERITY_INFO);
	}

	public void writeLocalMsg(String msg, int severity) {
		control.writeMessage(msg, severity);
	}

	public void writeLocalDebugMsg(String msg) {
		if(debug_msg_enabled)
		   control.writeMessage(msg, MAV_SEVERITY.MAV_SEVERITY_DEBUG);
	}

}
