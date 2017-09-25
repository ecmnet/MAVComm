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
import com.comino.msp.utils.MSP3DUtils;

import georegression.struct.se.Se3_F32;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class OffboardManager implements Runnable {

	private static OffboardManager offboard_manager = null;

	public static final int MODE_SINGLE_TARGET 		= 1; // switches to POSHOLD after target reached
	public static final int MODE_MULTI_TARGET 		= 2; // does not switch off offboard
	public static final int MODE_MULTI_NOCHECK 		= 3; // sends new target each 100ms
	public static final int MODE_MULTI_LIST    		= 4; // works list of targets
	public static final int MODE_MULTI_SPEED    		= 5; // sends new speed each 100ms
	public static final int MODE_MULTI_SPEED_LIST   	= 6; // works list of targets


	private float acceptance_radius = 0.1f;

	private IMAVController              control        = null;
	private BooleanProperty             enableProperty = null;
	private IOffboardListener             listener     = null;

	private final APSetPoint currentPos       = new APSetPoint();
	private final APSetPoint nextTarget       = new APSetPoint();

	private final LinkedList<APSetPoint>  worklist  = new LinkedList<APSetPoint>();

	private MSPLogger logger;
	private DataModel model;

	private int mode = 0;

	public static OffboardManager getInstance(IMAVController control) {
		if(offboard_manager == null)
			offboard_manager = new OffboardManager(control);
		return offboard_manager;
	}


	private OffboardManager(IMAVController control) {
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
		System.out.println("OffboardManager instantiated");
	}

	public void abort() {
		this.listener = null;
		enableProperty.set(false);
		logger.writeLocalMsg("[msp] Autopilot terminated manually",MAV_SEVERITY.MAV_SEVERITY_WARNING);
	}

	public void addListener(IOffboardListener listener) {
		this.listener = listener;
	}

	public void setNextTarget(APSetPoint nextTarget, int mode) {
		this.nextTarget.set(nextTarget);
		this.mode = mode;
		this.enableProperty.set(true);
	}

	public void setNextTarget(APSetPoint nextTarget) {
		setNextTarget(nextTarget,MODE_SINGLE_TARGET);
	}

	public void executeList() {
		if(enableProperty.get()) {
			logger.writeLocalMsg("[msp] Offboard rejected. Already running",MAV_SEVERITY.MAV_SEVERITY_NOTICE);
			return;
		}

		this.nextTarget.set(worklist.pop());
		this.mode = MODE_MULTI_LIST;
		this.enableProperty.set(true);
	}


	public void addToPositionList(APSetPoint target) {
		if(!target.position.isIdentical(0, 0, 0))
			worklist.add(target);
	}


	@Override
	public void run() {

		float distance;  long tms = 0;

		if(!enableProperty.get())
			return;

		logger.writeLocalMsg("[msp] Offboard started",MAV_SEVERITY.MAV_SEVERITY_NOTICE);

		while(enableProperty.get() && checkStickInputs()) {

			currentPos.set(model);

			try { Thread.sleep(50); 	} catch (InterruptedException e) { }


			switch(mode) {
			case MODE_SINGLE_TARGET:
				distance = nextTarget.position.distance(currentPos.position);
				sendPositionControlToVehice(nextTarget,currentPos);
				if(distance < acceptance_radius) {
					fireAction(distance,IOffboardListener.TYPE_NEXT_TARGET_REACHED);
					enableProperty.set(false);
				}
				break;
			case MODE_MULTI_TARGET:
				distance = nextTarget.position.distance(currentPos.position);
				sendPositionControlToVehice(nextTarget,currentPos);
				if(distance < acceptance_radius)
					fireAction(distance,IOffboardListener.TYPE_NEXT_TARGET_REACHED);
				break;
			case MODE_MULTI_NOCHECK:
				distance = nextTarget.position.distance(currentPos.position);
				sendPositionControlToVehice(nextTarget,currentPos);
				if((System.currentTimeMillis() - tms) > 100)
					fireAction(distance,IOffboardListener.TYPE_CONTINUOUS);
				break;
			case MODE_MULTI_LIST:
				distance = nextTarget.position.distance(currentPos.position);
				sendPositionControlToVehice(nextTarget,currentPos);
				if(distance < acceptance_radius) {
					if(!worklist.isEmpty()) {
						nextTarget.set(worklist.pop());
					}
					else {
						fireAction(distance,IOffboardListener.TYPE_LIST_COMPLETED);
						enableProperty.set(false);
					}
				}
				break;
			case MODE_MULTI_SPEED:
				sendSpeedControlToVehice(nextTarget);
				if((System.currentTimeMillis() - tms) > 100)
					fireAction(0,IOffboardListener.TYPE_CONTINUOUS);
				break;

			case MODE_MULTI_SPEED_LIST:


				break;
			}

			if((System.currentTimeMillis() - tms)>100) {
				// Don't publish for speed modes
				if(mode < MODE_MULTI_SPEED)
					publishSLAM(nextTarget);
				tms = System.currentTimeMillis();
			}
		}

		worklist.clear();

		model.sys.setStatus(Status.MSP_OFFBOARD_UPDATER_STARTED, false);
		model.sys.autopilot = 0;

		logger.writeLocalMsg("[msp] Offboard stopped",MAV_SEVERITY.MAV_SEVERITY_NOTICE);

		control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
				MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
				MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_POSCTL, 0 );

		publishSLAM(null);
	}

	public int getWorkListSize() {
		return worklist.size();
	}

	private void sendPositionControlToVehice( APSetPoint target, APSetPoint current) {

		msg_set_position_target_local_ned cmd = new msg_set_position_target_local_ned(1,2);
		cmd.target_component = 1;
		cmd.target_system = 1;
		cmd.type_mask = 0b000101111111000;

		// TODO: better: set Mask accordingly
		if(target.position.x!=Float.NaN) cmd.x = target.position.x; else cmd.x = current.position.x;
		if(target.position.y!=Float.NaN) cmd.y = target.position.y; else cmd.y = current.position.y;
		if(target.position.z!=Float.NaN) cmd.z = target.position.z; else cmd.z = current.position.z;

		//	cmd.yaw = MSPConvertUtils.getDirectionFromTargetXY(model,target);
		cmd.coordinate_frame = MAV_FRAME.MAV_FRAME_LOCAL_NED;

		if(!control.sendMAVLinkMessage(cmd))
			enableProperty.set(false);
	}

	private void sendSpeedControlToVehice( APSetPoint target) {

		msg_set_position_target_local_ned cmd = new msg_set_position_target_local_ned(1,2);
		cmd.target_component = 1;
		cmd.target_system = 1;
		cmd.type_mask = 0b000101111000111;

		// TODO: better: set Mask accordingly
		if(target.speed.x!=Float.NaN) cmd.x = target.speed.x; else cmd.x = 0;
		if(target.speed.y!=Float.NaN) cmd.y = target.speed.y; else cmd.y = 0;
		if(target.speed.z!=Float.NaN) cmd.z = target.speed.z; else cmd.z = 0;

		cmd.coordinate_frame = MAV_FRAME.MAV_FRAME_LOCAL_NED;

		if(!control.sendMAVLinkMessage(cmd))
			enableProperty.set(false);
	}

	private boolean checkStickInputs() {
		if(control.isSimulation())
			return true;

		// TODO: Check stick inputs for safety: Moved sticks lead to POSHOLD mode
		return true;
	}

	private void fireAction(float distance,int action_type) {
		if(listener!=null)
			listener.action(currentPos, distance, action_type);
	}

	private void publishSLAM(APSetPoint target) {
		msg_msp_micro_slam slam = new msg_msp_micro_slam(2,1);
		if(target!=null) {
			slam.px = target.position.getX();
			slam.py = target.position.getY();
			slam.pd = MSP3DUtils.getDirectionFromTargetXY(model, nextTarget);
			slam.wpcount = worklist.size();
			slam.pv = target.getSpeed();
		}
		control.sendMAVLinkMessage(slam);
	}


}
