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

package com.comino.msp.main.offboard;

import org.mavlink.messages.MAV_CMD;
import org.mavlink.messages.MAV_FRAME;
import org.mavlink.messages.MAV_MODE_FLAG;
import org.mavlink.messages.MAV_SEVERITY;
import org.mavlink.messages.lquac.msg_set_position_target_local_ned;

import com.comino.mav.control.IMAVController;
import com.comino.mav.mavlink.MAV_CUST_MODE;
import com.comino.msp.log.MSPLogger;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.Status;
import com.comino.msp.utils.MSPMathUtils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class OffboardPositionUpdater implements Runnable {

	private static final float RADIUS = 0.1f;

	private IMAVController control = null;
	private BooleanProperty enableProperty = new SimpleBooleanProperty();

	private float z_pos = -1.0f;
	private float x_pos = 0f;
	private float y_pos = 0f;

	private float yaw = 0f;

	private MSPLogger logger;
	private DataModel model;

	private boolean target_set = false;

	public OffboardPositionUpdater(IMAVController control) {
		this.control = control;
		this.model   = control.getCurrentModel();
		this.logger  = MSPLogger.getInstance();

		enableProperty.addListener((e,o,n) -> {

			this.x_pos = model.state.l_x;
			this.y_pos = model.state.l_y;
			this.z_pos = model.state.l_z;
			this.yaw   = model.state.h;

			target_set = false;

			new Thread(this).start();

			try {
				Thread.sleep(50);
			} catch (InterruptedException k) { }
			control.getCurrentModel().sys.setStatus(Status.MSP_OFFBOARD_UPDATER_STARTED, true);

			if(!control.getCurrentModel().sys.isStatus(Status.MSP_MODE_OFFBOARD))
				control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
						MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
						MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_OFFBOARD, 0 );
		});
	}

	public BooleanProperty enableProperty() {
		return enableProperty;
	}

	public void setNEDZ(float z) {
		this.z_pos = z;
		System.out.printf("Offboard Set: X: %2.1f Y: %2.1f Z: %2.1f\n",x_pos, y_pos,z_pos);
		target_set = true;
	}

	public void setNEDX(float x) {
		this.x_pos = x;
		System.out.printf("Offboard Set: X: %2.1f Y: %2.1f Z: %2.1f\n",x_pos, y_pos,z_pos);
		target_set = true;
	}

	public void setNEDY(float y) {
		this.y_pos = y;
		System.out.printf("Offboard Set: X: %2.1f Y: %2.1f Z: %2.1f\n",x_pos, y_pos,z_pos);
		target_set = true;
	}

	public void setYaw(float yaw_deg) {
		this.yaw = yaw_deg;
		System.out.printf("Offboard Set: YAW: %2f°\n",yaw);
		target_set = true;
	}


	@Override
	public void run() {

		float distance;

		if(!enableProperty.get())
			return;

		logger.writeLocalMsg("[msp] Offboard control",MAV_SEVERITY.MAV_SEVERITY_NOTICE);

		while(enableProperty.get()) {

			msg_set_position_target_local_ned cmd = new msg_set_position_target_local_ned(1,2);
			cmd.target_component = 1;
			cmd.target_system = 1;
			cmd.type_mask = 0b000101111111000;
			cmd.x =  x_pos;
			cmd.y =  y_pos;
			cmd.z =  z_pos;
			cmd.yaw = (float)(MSPMathUtils.toRad(yaw));
			cmd.coordinate_frame = MAV_FRAME.MAV_FRAME_LOCAL_NED;

			if(!control.sendMAVLinkMessage(cmd))
				enableProperty.set(false);

			try {
				Thread.sleep(250);
			} catch (InterruptedException e) { }

			distance = MSPMathUtils.getLocalDistance(model.state.l_x, model.state.l_y,model.state.l_z, x_pos, y_pos, z_pos);
			if(distance<RADIUS && target_set) {
				System.out.println(distance+"m");
				logger.writeLocalMsg("[msp] Offboard: Target reached",MAV_SEVERITY.MAV_SEVERITY_NOTICE);
				enableProperty.set(false);
			}
		}
		control.getCurrentModel().sys.setStatus(Status.MSP_OFFBOARD_UPDATER_STARTED, false);


		control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
				MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
				MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_POSCTL, 0 );
	}

}
