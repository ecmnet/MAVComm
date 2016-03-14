package com.comino.msp.log;

import java.util.ArrayList;
import java.util.List;

import org.mavlink.messages.MAV_SEVERITY;

import com.comino.mav.control.IMAVController;
import com.comino.msp.model.segment.LogMessage;

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
		LogMessage m = new LogMessage();
		m.msg = msg; m.severity = severity;
		control.writeLogMessage(m);
	}

	public void writeLocalDebugMsg(String msg) {
		if(debug_msg_enabled) {
			LogMessage m = new LogMessage();
			m.msg = msg; m.severity = MAV_SEVERITY.MAV_SEVERITY_DEBUG;
			control.writeLogMessage(m);
		}
	}

}
