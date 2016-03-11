package com.comino.msp.log;

import com.comino.mav.control.IMAVController;

public class MSPLogger {

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
		control.writeMessage("GCL: "+msg);
	}

	public void writeLocalDebugMsg(String msg) {
		if(debug_msg_enabled)
		   control.writeMessage("DBG: "+msg);
	}

}
