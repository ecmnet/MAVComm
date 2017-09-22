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

package com.comino.msp.execution.offboard;

import java.util.LinkedList;

import org.mavlink.messages.MAV_CMD;
import org.mavlink.messages.MAV_FRAME;
import org.mavlink.messages.MAV_MODE_FLAG;
import org.mavlink.messages.MAV_SEVERITY;
import org.mavlink.messages.MSP_AUTOCONTROL_MODE;
import org.mavlink.messages.lquac.msg_msp_micro_slam;
import org.mavlink.messages.lquac.msg_set_position_target_local_ned;

import com.comino.mav.control.IMAVController;
import com.comino.mav.mavlink.MAV_CUST_MODE;
import com.comino.msp.execution.IOffboardListener;
import com.comino.msp.log.MSPLogger;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.Status;
import com.comino.msp.utils.MSPConvertUtils;

import georegression.struct.se.Se3_F32;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class OffboardPositionUpdater implements Runnable {

	public static final int MODE_SINGLE_TARGET 	= 1; // switches to POSHOLD after target reached
	public static final int MODE_MULTI_TARGET 	= 2; // does not switch off offboard
	public static final int MODE_MULTI_NOCHECK 	= 3; // sends new target each 100ms
	public static final int MODE_MULTI_LIST    	= 4; // works list of targets


	private float acceptance_radius = 0.1f;

	private IMAVController              control        = null;
	private BooleanProperty             enableProperty = null;
	private IOffboardListener             listener     = null;

	private final Se3_F32 currentPos          = new Se3_F32();
	private final Se3_F32 nextTargetPos       = new Se3_F32();

	private final LinkedList<Se3_F32>  worklist  = new LinkedList<Se3_F32>();

	private MSPLogger logger;
	private DataModel model;

	private int mode = 0;


	public OffboardPositionUpdater(IMAVController control) {
		this.control        = control;
		this.model          = control.getCurrentModel();
		this.enableProperty = new SimpleBooleanProperty();
		this.logger         = MSPLogger.getInstance();

		control.getStatusManager().addListener(Status.MSP_LANDED,(o,n) -> {
			if(n.isStatus(Status.MSP_LANDED))
				enableProperty.set(false);
		});

		enableProperty.addListener((e,o,n) -> {

			if(n.booleanValue()) {

				new Thread(this).start();

				try {
					Thread.sleep(50);
				} catch (InterruptedException k) { }
				control.getCurrentModel().sys.setStatus(Status.MSP_OFFBOARD_UPDATER_STARTED, true);

				if(!control.getCurrentModel().sys.isStatus(Status.MSP_MODE_OFFBOARD))
					control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
							MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
							MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_OFFBOARD, 0 );
			}
		});
	}

	public BooleanProperty enableProperty() {
		return enableProperty;
	}

	public void addListener(IOffboardListener listener) {
		this.listener = listener;
	}

	public void setNextTarget(Se3_F32 nextTarget, int mode) {
		if(!nextTarget.T.isIdentical(0, 0, 0)) {
			this.nextTargetPos.set(nextTarget);
			this.mode = mode;
			this.enableProperty.set(true);
		}
	}

	public void setNextTarget(Se3_F32 nextTarget) {
		setNextTarget(nextTarget,MODE_SINGLE_TARGET);
	}

	public void executeList() {
		if(enableProperty.get()) {
			logger.writeLocalMsg("[msp] Offboard rejected. Already running",MAV_SEVERITY.MAV_SEVERITY_NOTICE);
			return;
		}

		this.nextTargetPos.set(worklist.pop());
		this.mode = MODE_MULTI_LIST;
		this.enableProperty.set(true);
	}


	public void addToList(Se3_F32 target) {
		if(!target.T.isIdentical(0, 0, 0))
			worklist.add(target);
	}


	@Override
	public void run() {

		float distance;  long tms = 0;

		if(!enableProperty.get())
			return;

		logger.writeLocalMsg("[msp] Offboard started",MAV_SEVERITY.MAV_SEVERITY_NOTICE);

		while(enableProperty.get()) {

			MSPConvertUtils.convertModelToSe3_F32(model, currentPos);

			distance = nextTargetPos.getT().distance(currentPos.T);

			sendPositionControlToVehice(nextTargetPos,currentPos);

			try { Thread.sleep(50); 	} catch (InterruptedException e) { }


			switch(mode) {
			case MODE_SINGLE_TARGET:
				if(distance < acceptance_radius) {
					fireAction(distance,IOffboardListener.TYPE_NEXT_TARGET_REACHED);
					enableProperty.set(false);
				}
				break;
			case MODE_MULTI_TARGET:
				if(distance < acceptance_radius)
					fireAction(distance,IOffboardListener.TYPE_NEXT_TARGET_REACHED);
				break;
			case MODE_MULTI_NOCHECK:
				if((System.currentTimeMillis() - tms) > 100)
					fireAction(distance,IOffboardListener.TYPE_CONTINUOUS);
				break;
			case MODE_MULTI_LIST:
				if(distance < acceptance_radius) {
					if(!worklist.isEmpty())
						nextTargetPos.set(worklist.pop());
					else {
						fireAction(distance,IOffboardListener.TYPE_LIST_COMPLETED);
						enableProperty.set(false);
					}
				}
				break;
			}

			if((System.currentTimeMillis() - tms)>100) {
				publishSLAM(model.hud.s,nextTargetPos);
				tms = System.currentTimeMillis();
			}
		}

		worklist.clear();

		model.sys.setStatus(Status.MSP_OFFBOARD_UPDATER_STARTED, false);
		model.sys.setAutopilotMode(MSP_AUTOCONTROL_MODE.CIRCLE_MODE, false);
		model.sys.setAutopilotMode(MSP_AUTOCONTROL_MODE.WAYPOINT_MODE, false);

		logger.writeLocalMsg("[msp] Offboard stopped",MAV_SEVERITY.MAV_SEVERITY_NOTICE);

		control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
				MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
				MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_POSCTL, 0 );

		publishSLAM(0,null);
	}

	private void sendPositionControlToVehice( Se3_F32 target, Se3_F32 current) {

		msg_set_position_target_local_ned cmd = new msg_set_position_target_local_ned(1,2);
		cmd.target_component = 1;
		cmd.target_system = 1;
		//	cmd.type_mask = 0b000101111111000;
		cmd.type_mask = 0b000101111000000;

		// TODO: better: set Mask accordingly
		if(nextTargetPos.getX()!=Float.NaN) cmd.x = target.getX(); else cmd.x = current.getX();
		if(nextTargetPos.getY()!=Float.NaN) cmd.y = target.getY(); else cmd.y = current.getY();
		if(nextTargetPos.getZ()!=Float.NaN) cmd.z = target.getZ(); else cmd.z = current.getZ();

	//	cmd.yaw = MSPConvertUtils.getDirectionFromTargetXY(model,target);
		cmd.coordinate_frame = MAV_FRAME.MAV_FRAME_LOCAL_NED;

		if(!control.sendMAVLinkMessage(cmd))
			enableProperty.set(false);
	}

	private void fireAction(float distance,int action_type) {
		if(listener!=null)
			listener.action(currentPos, distance, action_type);
	}

	private void publishSLAM(float speed, Se3_F32 target) {
		msg_msp_micro_slam slam = new msg_msp_micro_slam(2,1);
		if(target!=null) {
			slam.px = target.getX();
			slam.py = target.getY();
			slam.pd = MSPConvertUtils.getDirectionFromTargetXY(model, nextTargetPos);
			slam.wpcount = worklist.size();
		}
		slam.pv = speed;
		control.sendMAVLinkMessage(slam);
	}


}
