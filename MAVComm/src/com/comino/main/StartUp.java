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


package com.comino.main;

import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;

import org.mavlink.messages.MAV_SEVERITY;
import org.mavlink.messages.MSP_CMD;
import org.mavlink.messages.MSP_COMPONENT_CTRL;
import org.mavlink.messages.lquac.msg_msp_command;
import org.mavlink.messages.lquac.msg_msp_micro_grid;
import org.mavlink.messages.lquac.msg_msp_status;
import org.mavlink.messages.lquac.msg_timesync;

import com.comino.mav.control.IMAVMSPController;
import com.comino.mav.control.impl.MAVController;
import com.comino.mav.control.impl.MAVProxyController;
import com.comino.msp.execution.autopilot.Autopilot2D;
import com.comino.msp.execution.commander.MSPCommander;
import com.comino.msp.execution.control.listener.IMAVLinkListener;
import com.comino.msp.log.MSPLogger;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.LogMessage;
import georegression.struct.point.Point3D_F64;

public class StartUp implements Runnable {

	private IMAVMSPController    control = null;
	private MSPConfig	          config = null;

	private MSPCommander commander = null;
	private OperatingSystemMXBean osBean = null;;
	private MemoryMXBean  mxBean = null;
	private DataModel      model = null;

	public StartUp(String[] args) {

		config  = MSPConfig.getInstance(System.getProperty("user.home"),"msp.properties");
		System.out.println("MSPService version "+config.getVersion()+" ("+config.getBasePath()+")");

		if(args.length>0)
			control = new MAVProxyController(MAVController.MODE_SITL);
		else
			control = new MAVProxyController(MAVController.MODE_USB);

		model = control.getCurrentModel();

		MSPLogger.getInstance(control);

		commander = new MSPCommander(control,config);

		osBean = java.lang.management.ManagementFactory.getOperatingSystemMXBean();
		mxBean = java.lang.management.ManagementFactory.getMemoryMXBean();

		// Start services if required


		control.start();
		MSPLogger.getInstance().writeLocalMsg("MAVProxy "+config.getVersion()+" loaded");
		Thread worker = new Thread(this);
		worker.start();

		control.registerListener(msg_msp_command.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_msp_command cmd = (msg_msp_command)o;
				switch(cmd.command) {
				case MSP_CMD.MSP_TRANSFER_MICROSLAM:
					model.grid.invalidateTransfer();
					control.writeLogMessage(new LogMessage("[sitl] map transfer request",
							MAV_SEVERITY.MAV_SEVERITY_NOTICE));
					break;
				}
			}
		});
		Autopilot2D.getInstance().reset(true);
	}

	public static void main(String[] args) {
		new StartUp(args);
	}

    boolean isAvoiding = true;

	@Override
	public void run() {
		long tms = System.currentTimeMillis();

		msg_msp_micro_grid grid = new msg_msp_micro_grid(2,1);
		msg_msp_status msg = new msg_msp_status(2,1);

		while(true) {
			try {
				Thread.sleep(200);
				if(!control.isConnected()) {
					control.close();
					control.connect();
				}


				grid.resolution = 0;
				grid.extension  = 0;
				grid.cx  = model.grid.getIndicatorX();
				grid.cy  = model.grid.getIndicatorY();
				grid.tms  = model.sys.getSynchronizedPX4Time_us();
				grid.count = model.grid.count;
				if(model.grid.toArray(grid.data)) {
					control.sendMAVLinkMessage(grid);
				}


				msg.load = (int)(osBean.getSystemLoadAverage()*100f/osBean.getAvailableProcessors());
				msg.autopilot_mode =control.getCurrentModel().sys.autopilot;
				msg.memory = (int)(mxBean.getHeapMemoryUsage().getUsed() * 100 /mxBean.getHeapMemoryUsage().getMax());
				msg.com_error = control.getErrorCount();
				msg.uptime_ms = System.currentTimeMillis() - tms;
				msg.status = control.getCurrentModel().sys.getStatus();
				msg.setVersion(config.getVersion());
				msg.setArch(osBean.getArch());
				msg.cpu_temp = 27;
				msg.unix_time_us = control.getCurrentModel().sys.getSynchronizedPX4Time_us();
				msg.wifi_quality = 100;
				control.sendMAVLinkMessage(msg);

				msg_timesync sync_s = new msg_timesync(255,1);
				sync_s.tc1 = 0;
				sync_s.ts1 = System.currentTimeMillis()*1000000L;
				control.sendMAVLinkMessage(sync_s);


			} catch (Exception e) {
				e.printStackTrace();
				control.close();
			}
		}

	}

}
