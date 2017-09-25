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
import org.mavlink.messages.MSP_AUTOCONTROL_MODE;
import org.mavlink.messages.MSP_CMD;
import org.mavlink.messages.MSP_COMPONENT_CTRL;
import org.mavlink.messages.lquac.msg_msp_command;

import com.comino.mav.control.IMAVMSPController;
import com.comino.msp.execution.control.StatusManager;
import com.comino.msp.execution.control.listener.IMAVLinkListener;
import com.comino.msp.execution.flightcontrol.FlightGestureControl;
import com.comino.msp.execution.offboard.OffboardManager;
import com.comino.msp.log.MSPLogger;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.Status;

public class MSPCommander {

	private IMAVMSPController        control = null;
	private OffboardManager offboard = null;
	private FlightGestureControl    gestures = null;
	private DataModel                  model = null;

	public MSPCommander(IMAVMSPController control) {

		this.control = control;
		this.model   = control.getCurrentModel();

		registerCommands();

		System.out.println("Commander initialized");

		offboard = OffboardManager.getInstance(control);

		gestures = new FlightGestureControl(model, offboard);
	}

	public  OffboardManager getOffBoardUpdater() {
		return offboard;
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
			offboard.abort();
			break;
		  case MSP_AUTOCONTROL_MODE.CIRCLE_MODE:
			if(param == 0) param = 0.75f;
			gestures.enableCircleMode(enable, param);
			break;

		  case MSP_AUTOCONTROL_MODE.WAYPOINT_MODE:
			if(enable)
				gestures.waypoint_example(10.5f);
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
		gestures.setTargetAndExecute(cmd.param1, cmd.param2, cmd.param3, cmd.param4);
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
