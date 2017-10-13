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


package com.comino.msp.execution.commander;

import java.io.IOException;

import org.mavlink.messages.MAV_SEVERITY;
import org.mavlink.messages.MSP_AUTOCONTROL_ACTION;
import org.mavlink.messages.MSP_AUTOCONTROL_MODE;
import org.mavlink.messages.MSP_CMD;
import org.mavlink.messages.MSP_COMPONENT_CTRL;
import org.mavlink.messages.lquac.msg_msp_command;

import com.comino.mav.control.IMAVController;
import com.comino.mav.control.IMAVMSPController;
import com.comino.msp.execution.autopilot.Autopilot;
import com.comino.msp.execution.autopilot.offboard.OffboardManager;
import com.comino.msp.execution.control.StatusManager;
import com.comino.msp.execution.control.listener.IMAVLinkListener;
import com.comino.msp.log.MSPLogger;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.LogMessage;
import com.comino.msp.model.segment.Status;
import com.comino.vfh.vfh2D.HistogramGrid2D;

import georegression.struct.point.Point3D_F64;

public class MSPCommander {

	private IMAVMSPController        control = null;
	private Autopilot              autopilot = null;
	private DataModel                  model = null;
	private HistogramGrid2D             vfh  = null;

	public MSPCommander(IMAVMSPController control, HistogramGrid2D vfh) {

		this.vfh = vfh;

		this.control = control;
		this.model   = control.getCurrentModel();

		registerCommands();

		System.out.println("Commander initialized");

		autopilot = Autopilot.getInstance(control);
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
				case MSP_CMD.MSP_CMD_AUTOMODE:
					setAutopilotMode((int)(cmd.param2),cmd.param3,(int)(cmd.param1)==MSP_COMPONENT_CTRL.ENABLE);
					break;
				}
			}
		});
	}

	private void setAutopilotMode(int mode, float param, boolean enable) {
		model.sys.setAutopilotMode(mode, enable);
		switch(mode) {
		case MSP_AUTOCONTROL_MODE.ABORT:
			autopilot.abort();
			break;
		case MSP_AUTOCONTROL_ACTION.CIRCLE_MODE:
			if(param == 0) param = 0.75f;
			if(enable)
				setCircleObstacleForSITL() ;
			autopilot.enableCircleMode(enable, param);
			break;
		case MSP_AUTOCONTROL_ACTION.WAYPOINT_MODE:
				autopilot.return_along_path(enable);
			break;
		case MSP_AUTOCONTROL_ACTION.AUTO_MISSION:

			break;
		case MSP_AUTOCONTROL_ACTION.DEBUG_MODE1:
			if(enable)
				setXObstacleForSITL();
			autopilot.enableDebugMode1(enable, 0.1f);

		break;
		case MSP_AUTOCONTROL_ACTION.DEBUG_MODE2:
			if(enable)
				setYObstacleForSITL();
			autopilot.enableDebugMode2(enable, 0.1f);

		break;
		case MSP_AUTOCONTROL_ACTION.OFFBOARD_UPDATER:
				autopilot.offboardPosHold(enable);
			break;
		}
	}


	private void restartCompanion(msg_msp_command cmd) {
		MSPLogger.getInstance().writeLocalMsg("Companion rebooted",
				MAV_SEVERITY.MAV_SEVERITY_CRITICAL);
		if(model.sys.isStatus(Status.MSP_LANDED))
			executeConsoleCommand("reboot");
	}


	private void setOffboardPosition(msg_msp_command cmd) {
		autopilot.setTarget(cmd.param1, cmd.param2, cmd.param3, cmd.param4);
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

	// SITL testing

	private void setCircleObstacleForSITL() {
		if(vfh==null)
			return;
		vfh.reset(model);
		Point3D_F64   pos          = new Point3D_F64();
		System.err.println("SITL -> set example obstacle map");
		pos.x = 0.5 + model.state.l_x;
		pos.y = 0.4 + model.state.l_y;
		pos.z = 1.0 + model.state.l_z;
		vfh.gridUpdate(pos);
		pos.y = 0.45 + model.state.l_y;
		vfh.gridUpdate(pos);
		pos.y = 0.50 + model.state.l_y;
		vfh.gridUpdate(pos);
		pos.y = 0.55 + model.state.l_y;
		vfh.gridUpdate(pos);
		pos.y = 0.60 + model.state.l_y;
		vfh.gridUpdate(pos);
		pos.y = 0.65 + model.state.l_y;
		vfh.gridUpdate(pos);
		pos.y = 0.70 + model.state.l_y;
		vfh.gridUpdate(pos);
	}

	private void setXObstacleForSITL() {
		if(vfh==null)
			return;
		vfh.reset(model);
		Point3D_F64   pos = new Point3D_F64();
		System.err.println("SITL -> set example obstacle map");
		pos.y = -0.10 + model.state.l_y;
		pos.x = 1 + model.state.l_x;
		pos.z = 1.0 + model.state.l_z;
		vfh.gridUpdate(pos);
		pos.y = -0.05 + model.state.l_y;
		vfh.gridUpdate(pos);
		pos.y = 0 + model.state.l_y;
		vfh.gridUpdate(pos);
		pos.y = 0.05 + model.state.l_y;
		vfh.gridUpdate(pos);
		pos.y = 0.10 + model.state.l_y;
		vfh.gridUpdate(pos);
	}

	private void setYObstacleForSITL() {
		if(vfh==null)
			return;
		vfh.reset(model);
		Point3D_F64   pos = new Point3D_F64();
		System.err.println("SITL -> set example obstacle map");
		pos.x = -0.10 + model.state.l_x;
		pos.y = 1 + model.state.l_y;
		pos.z = 1.0 + model.state.l_z;
		vfh.gridUpdate(pos);
		pos.x = -0.05 + model.state.l_x;
		vfh.gridUpdate(pos);
		pos.x = 0 + model.state.l_x;
		vfh.gridUpdate(pos);
		pos.x = 0.05 + model.state.l_x;
		vfh.gridUpdate(pos);
		pos.x = 0.10 + model.state.l_x;
		vfh.gridUpdate(pos);
	}

}
