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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.mavlink.messages.MAV_SEVERITY;

import com.comino.mav.control.IMAVController;
import com.comino.msp.model.segment.LogMessage;
import com.comino.msp.utils.ExecutorService;

public class MSPLogger implements Runnable {

	// TOOD: register proxy, and send messages if proxy is registered

	private static MSPLogger log      = null;
	private IMAVController control    = null;
	private boolean debug_msg_enabled = false;
	private boolean file_log_enabled  = false;

	private String           filename;
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private PrintStream ps_log;


	public static MSPLogger getInstance(IMAVController control, String directory_name)  {
		if(log==null) {
			log = new MSPLogger(control, directory_name);
		}
		return log;
	}

	public static MSPLogger getInstance(IMAVController control) {
		return getInstance(control,null);
	}

	public static MSPLogger getInstance() {
		return log;
	}

	private MSPLogger(IMAVController control2, String directory_name) {
		this.control = control2;
		if(directory_name!=null) {
			file_log_enabled = true;
			File file = new File(directory_name);
			if(!file.exists() || !file.isDirectory()){
				boolean wasDirectoryMade = file.mkdirs();
				if(wasDirectoryMade)System.out.println("Directory "+directory_name+" created");
				else {
					file_log_enabled = false;
					System.out.println("No logging to file: Could not create directory "+directory_name);
					return;
				}
			}
			// create file, if it does not exist
			SimpleDateFormat sdfFile = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
			this.filename = directory_name +"/msplog_"+sdfFile.format(new Date());
			System.out.println("Logging to: "+filename);

			try {
				FileOutputStream fos_log  = new FileOutputStream(filename + ".log");
				ps_log = new PrintStream(fos_log);
			} catch (FileNotFoundException e) {
				System.out.println("No logging to file: Error creating log file.");
				file_log_enabled = false;
				return;
			}

			ExecutorService.get().schedule(this, 10, TimeUnit.SECONDS);
		} else
			file_log_enabled = false;
	}

	public void enableDebugMessages(boolean enabled) {
		this.debug_msg_enabled = enabled;
	}

	public void writeLocalMsg(String msg) {
		writeLocalMsg(msg,MAV_SEVERITY.MAV_SEVERITY_INFO);
		if(file_log_enabled) {
			writeLogToFile(msg);
		}
	}

	public void writeLocalMsg(String msg, int severity) {
		LogMessage m = new LogMessage();
		m.msg = msg; m.severity = severity;
		control.writeLogMessage(m);
		if(file_log_enabled) {
			writeLogToFile(m.toString());
		}
	}

	public void writeLocalDebugMsg(String msg) {
		if(debug_msg_enabled) {
			LogMessage m = new LogMessage();
			m.msg = msg; m.severity = MAV_SEVERITY.MAV_SEVERITY_DEBUG;
			control.writeLogMessage(m);
			if(file_log_enabled) {
				writeLogToFile(m.toString());
			}
		}
	}

	private void writeLogToFile(String msg) {
		ps_log.println(sdf1.format(new Date())+": "+msg);
	}

	@Override
	public void run() {
		if(file_log_enabled) {
			ps_log.flush();
			ExecutorService.get().schedule(this, 1, TimeUnit.SECONDS);
		}
	}
}
