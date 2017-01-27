/****************************************************************************
 *
 *   Copyright (c) 2017 Eike Mansfeld ecm@gmx.de. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 ****************************************************************************/

package com.comino.msp.log;

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
