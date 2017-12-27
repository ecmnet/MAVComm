/****************************************************************************
 *
 *   Copyright (c) 2017,2018 Eike Mansfeld ecm@gmx.de. All rights reserved.
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

import com.comino.main.MSPConfig;
import com.comino.mav.control.IMAVMSPController;
import com.comino.msp.execution.autopilot.Autopilot2D;
import com.comino.msp.execution.control.listener.IMAVLinkListener;
import com.comino.msp.log.MSPLogger;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.Status;
import com.comino.msp.slam.map.ILocalMap;
import com.comino.msp.utils.MSPMathUtils;

import georegression.struct.point.Vector3D_F32;

public class MSPCommander {

	private IMAVMSPController        control 	= null;
	private Autopilot2D              autopilot 	= null;
	private DataModel                  model 	= null;
	private ILocalMap                	map  	= null;

	public MSPCommander(IMAVMSPController control, MSPConfig config) {

		this.control = control;
		this.model   = control.getCurrentModel();

		registerCommands();

		System.out.println("Commander initialized");

		autopilot = Autopilot2D.getInstance(control,config);
		this.map = autopilot.getMap2D();
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
				case MSP_CMD.MSP_CMD_MICROSLAM:
					switch((int)cmd.param1) {
					case MSP_COMPONENT_CTRL.RESET:
						autopilot.reset(); break;
					}
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
		case MSP_AUTOCONTROL_ACTION.RTL:
			if(enable)
			   autopilot.returnToLand(3000);
			break;
		case MSP_AUTOCONTROL_ACTION.WAYPOINT_MODE:
			autopilot.return_along_path(enable);
			break;
		case MSP_AUTOCONTROL_ACTION.SAVE_MAP2D:
			autopilot.saveMap2D();
			break;
		case MSP_AUTOCONTROL_ACTION.AUTO_MISSION:

			break;
		case MSP_AUTOCONTROL_ACTION.DEBUG_MODE1:
			if(enable)
				setXObstacleForSITL();
			autopilot.enableDebugMode1(enable, 0.1f);
			break;
		case MSP_AUTOCONTROL_ACTION.DEBUG_MODE2:
			setYObstacleForSITL();
			break;
		case MSP_AUTOCONTROL_ACTION.OFFBOARD_UPDATER:
			autopilot.offboardPosHold(enable);
			break;
		case MSP_AUTOCONTROL_ACTION.STEP:
			autopilot.executeStep();
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
		//autopilot.setTarget(cmd.param1, cmd.param2, cmd.param3, cmd.param4);
		autopilot.reset(false);
		autopilot.moveto(cmd.param1, cmd.param2, cmd.param3, cmd.param4);
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
		if(map==null)
			return;
		map.reset();
		Vector3D_F32   pos          = new Vector3D_F32();
		System.err.println("SITL -> set example obstacle map");
		pos.x = 0.5f + model.state.l_x;
		pos.y = 0.4f + model.state.l_y;
		pos.z = 1.0f + model.state.l_z;
		map.update(pos);
		pos.y = 0.45f + model.state.l_y;
		map.update(pos);
		pos.y = 0.50f + model.state.l_y;
		map.update(pos);
		pos.y = 0.55f + model.state.l_y;
		map.update(pos);
		pos.y = 0.60f + model.state.l_y;
		map.update(pos);
		pos.y = 0.65f + model.state.l_y;
		map.update(pos);
		pos.y = 0.70f + model.state.l_y;
		map.update(pos);
	}

	private void setXObstacleForSITL() {
		if(map==null)
			return;
		map.reset();
		Vector3D_F32   pos          = new Vector3D_F32();
		System.err.println("SITL -> set example obstacle map");

		pos.y = 0.5f + model.state.l_y;
		pos.z =  model.state.l_z;
		for(int i = 0; i < 40;i++) {
			pos.x = -1.25f + i *0.05f + model.state.l_x;
			map.update(pos);
		}

		pos.y = 1.75f + model.state.l_y;
		pos.z =  model.state.l_z;
		for(int i = 0; i < 30;i++) {
			pos.x = -1.25f + i *0.05f + model.state.l_x;
			map.update(pos);
		}

		for(int i = 0; i < 30;i++) {
			pos.x = 1.25f + i *0.05f + model.state.l_x;
			map.update(pos);
		}

		pos.x = 2.0f + model.state.l_x;
		for(int i = 0; i < 25;i++) {
			pos.y = -1 + i *0.05f + model.state.l_y;
			map.update(pos);
		}



	}

	private void setYObstacleForSITL() {
		float x,y;
		if(map==null)
			return;
		map.reset();
		Vector3D_F32   pos          = new Vector3D_F32();
		System.err.println("SITL -> set example obstacle map");
		pos.z = 1.0f + model.state.l_z;

		pos.y = 2f + model.state.l_y;
		pos.x = -0.15f + model.state.l_x;
		map.update(pos);
		pos.x = -0.10f + model.state.l_x;
		map.update(pos);
		pos.x = -0.05f + model.state.l_x;
		map.update(pos);
		pos.x =  0.00f + model.state.l_x;
		map.update(pos);
		pos.x = 0.05f + model.state.l_x;
		map.update(pos);
		pos.x = 0.10f + model.state.l_x;
		map.update(pos);
		pos.x = 0.15f + model.state.l_x;
		map.update(pos);

		float dotx, doty ; float[] r = new float[2];

		for(int j=0; j< 30; j++) {

			dotx = (float)((Math.random()*10-5f));
			doty = (float)((Math.random()*10-5f));

			MSPMathUtils.rotateRad(r, dotx, doty, (float)Math.random() * 6.28f);

			for(int i=0; i< 40; i++) {

				x =  (float)Math.random()*.8f - 0.4f + r[0];
				y =  (float)Math.random()*.8f - 0.4f + r[1];

				pos.x = x;
				pos.y = y ;
				map.update(pos);

			}
		}


	}

}
