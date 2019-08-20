/****************************************************************************
 *
 *   Copyright (c) 2017-2019 Eike Mansfeld ecm@gmx.de. All rights reserved.
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


package com.comino.msp.execution.commander;

import java.io.IOException;

import org.mavlink.messages.MAV_SEVERITY;
import org.mavlink.messages.MSP_AUTOCONTROL_ACTION;
import org.mavlink.messages.MSP_AUTOCONTROL_MODE;
import org.mavlink.messages.MSP_CMD;
import org.mavlink.messages.MSP_COMPONENT_CTRL;
import org.mavlink.messages.lquac.msg_hil_gps;
import org.mavlink.messages.lquac.msg_msp_command;

import com.comino.main.MSPConfig;
import com.comino.mav.control.IMAVMSPController;
import com.comino.msp.execution.autopilot.AutoPilotBase;
import com.comino.msp.execution.autopilot.CollisonPreventionPilot;
import com.comino.msp.execution.autopilot.LVH2DPilot;
import com.comino.msp.execution.control.listener.IMAVLinkListener;
import com.comino.msp.log.MSPLogger;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.Status;
import com.comino.msp.slam.map2D.ILocalMap;
import com.comino.msp.utils.ExecutorService;
import com.comino.msp.utils.MSPMathUtils;

import georegression.struct.point.Vector3D_F32;

@SuppressWarnings("unused")
public class MSPCommander {

	private IMAVMSPController        control 	= null;
	private AutoPilotBase           autopilot 	= null;
	private DataModel                  model 	= null;
	private ILocalMap                	map  	= null;

	public MSPCommander(IMAVMSPController control, MSPConfig config) {

		this.control = control;
		this.model   = control.getCurrentModel();

		registerCommands();

		System.out.println("Commander initialized");

//		autopilot = AutoPilotBase.getInstance(CollisonPreventionPilot.class,control,config);
		autopilot = AutoPilotBase.getInstance(LVH2DPilot.class,control,config);

		this.map = autopilot.getMap2D();
	}

	public void setGlobalOrigin(double lat, double lon, double altitude) {

		final long MAX_GPOS_SET_MS = 20000;

		if(model.sys.isStatus(Status.MSP_GPOS_VALID) || lat == 0.0 || lon == 0.0 ||
				model.sys.isSensorAvailable(Status.MSP_GPS_AVAILABILITY))
			return;

		ExecutorService.submit(() -> {

			long tms = System.currentTimeMillis();


			msg_hil_gps gps = new msg_hil_gps(1,1);
			gps.lat = (long)(lat * 1e7);
			gps.lon = (long)(lon * 1e7);
			gps.alt = (int)(altitude * 1000);
			gps.satellites_visible = 10;
			gps.eph = 30;
			gps.epv = 30;
			gps.fix_type = 4;
			gps.cog = 0;

			while(!model.sys.isStatus(Status.MSP_GPOS_VALID)
					&& (System.currentTimeMillis() - tms) < MAX_GPOS_SET_MS) {
				control.sendMAVLinkMessage(gps);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) { }
			}
			gps.eph = 0;
			gps.epv = 0;
			gps.satellites_visible = 0;
			gps.fix_type = 0;
			control.sendMAVLinkMessage(gps);

			MSPLogger.getInstance().writeLocalMsg("[msp] Reference position set from MAVGCL",
					MAV_SEVERITY.MAV_SEVERITY_INFO);

		}, ExecutorService.LOW );

	}

	private void registerCommands() {

		// register MSP commands here

		control.registerListener(msg_msp_command.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_msp_command cmd = (msg_msp_command)o;
				switch(cmd.command) {
				case MSP_CMD.MSP_CMD_RESTART:
					restartCompanion(cmd);
					break;
				case MSP_CMD.MSP_CMD_OFFBOARD_SETLOCALPOS:
					setOffboardPosition(cmd);
					break;
				case MSP_CMD.MSP_CMD_SET_HOMEPOS:
					setGlobalOrigin(cmd.param1 / 1e7f, cmd.param2 / 1e7f, cmd.param3 / 1e3f );
					break;
				case MSP_CMD.MSP_CMD_AUTOMODE:
					autopilot.setMode((int)(cmd.param2),cmd.param3,(int)(cmd.param1)==MSP_COMPONENT_CTRL.ENABLE);
					break;
				case MSP_CMD.MSP_CMD_OFFBOARD_SETLOCALVEL:
					autopilot.setCurrentLocalSpeed((int)(cmd.param1)==MSP_COMPONENT_CTRL.ENABLE,cmd.param2, cmd.param3, cmd.param4, cmd.param5);
					break;
				case MSP_CMD.MSP_CMD_MICROSLAM:
					switch((int)cmd.param1) {
					case MSP_COMPONENT_CTRL.RESET:
						autopilot.resetMap(); break;
					}
					break;
				}
			}
		});
	}


	private void restartCompanion(msg_msp_command cmd) {
		MSPLogger.getInstance().writeLocalMsg("Companion rebooted",
				MAV_SEVERITY.MAV_SEVERITY_CRITICAL);
		if(model.sys.isStatus(Status.MSP_LANDED))
			executeConsoleCommand("reboot");
	}


	private void setOffboardPosition(msg_msp_command cmd) {
		if(cmd.param3 == 0 || cmd.param3 == Float.NaN)
		  autopilot.moveto(cmd.param1, cmd.param2, model.state.l_z, Float.NaN);
		else
		  autopilot.moveto(cmd.param1, cmd.param2, cmd.param3, Float.NaN);
	}




	// -----------------------------------------------------------------------------------------------helper

	private void executeConsoleCommand(String command) {
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			MSPLogger.getInstance().writeLocalMsg("LINUX command '"+command+"' failed: "+e.getMessage(),
					MAV_SEVERITY.MAV_SEVERITY_CRITICAL);
		}
	}



}
