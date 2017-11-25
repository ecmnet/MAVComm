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

package com.comino.msp.execution.autopilot.offboard;

import org.mavlink.messages.MAV_FRAME;
import org.mavlink.messages.MAV_SEVERITY;
import org.mavlink.messages.MSP_AUTOCONTROL_ACTION;
import org.mavlink.messages.lquac.msg_msp_micro_slam;
import org.mavlink.messages.lquac.msg_set_position_target_local_ned;

import com.comino.mav.control.IMAVController;
import com.comino.msp.execution.control.StatusManager;
import com.comino.msp.log.MSPLogger;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.Status;
import com.comino.msp.utils.MSP3DUtils;

import georegression.struct.point.Vector3D_F32;
import georegression.struct.point.Vector4D_F32;

public class OffboardManager implements Runnable {

	private static final float MAX_SPEED					= 0.8f;

	//	private static final float MIN_REL_ALTITUDE          = 0.3f;

	private static final int UPDATE_RATE                 = 50;
	private static final int SETPOINT_TIMEOUT_MS         = 15000;

	public static final int MODE_POSITION	 		    = 1;
	public static final int MODE_SPEED	 		        = 2;
	public static final int MODE_SPEED_POSITION	 	    = 3;

	private MSPLogger 				logger					= null;
	private DataModel 				model					= null;
	private IMAVController         	control      			= null;
	private IOffboardTargetAction    action_listener     		= null;		// CB target reached
	private IOffboardExternalControl ext_control_listener   	= null;		// CB external angle+speed control in MODE_SPEED_POSITION

	private boolean					enabled					= false;
	private int						mode						= 0;
	private Vector4D_F32				target					= null;
	private Vector4D_F32				current					= null;
	private Vector3D_F32				current_speed			= null;

	private float		acceptance_radius_pos				= 0.2f;
	private float		acceptance_radius_speed				= 0.05f;
	private boolean     already_fired				    		= false;
	private boolean     valid_setpoint                   		= false;
	private boolean     new_setpoint                    	 = false;
	private boolean     step_mode                        = false;
	private boolean     step_trigger                     = false;


	public OffboardManager(IMAVController control) {
		this.control        = control;
		this.model          = control.getCurrentModel();
		this.logger         = MSPLogger.getInstance();
		this.target         = new Vector4D_F32();
		this.current        = new Vector4D_F32();

		this.current_speed  = new Vector3D_F32();

		control.getStatusManager().addListener(StatusManager.TYPE_PX4_STATUS,
				Status.MSP_MODE_OFFBOARD, StatusManager.EDGE_FALLING, (o,n) -> {
					valid_setpoint = false;
				});
	}

	public void start(int m) {
		mode = m;
		if(!enabled) {
			enabled = true;
			new Thread(this).start();
		}
	}

	public void stop() {
		enabled = false;
	}

	public void setStepMode(boolean step_mode) {
		this.step_mode = step_mode;
	}

	public void triggerStep() {
		this.step_trigger = true;
	}

	public void setTarget(Vector3D_F32 t) {
		target.set(t.x,t.y,t.z,0);
		target.w = MSP3DUtils.getXYDirection(target, current);
		valid_setpoint = true;
		new_setpoint = true;
		already_fired = false;
	}

	public void setTarget(Vector3D_F32 t, float w) {
		target.set(t.x,t.y,t.z,w);
		valid_setpoint = true;
		new_setpoint = true;
		already_fired = false;
	}

	public void setTarget(Vector4D_F32 t) {
		target.set(t);
		valid_setpoint = true;
		new_setpoint = true;
		already_fired = false;
	}

	public Vector4D_F32 getCurrentTarget() {
		return target;
	}

	public void setCurrentAsTarget() {
		target.set(model.state.l_x, model.state.l_y, model.state.l_z, model.attitude.y);
		already_fired = false;
		new_setpoint = true;
		valid_setpoint = true;
	}

	public void finalize() {
		target.w = 0;
		mode = MODE_POSITION;
		this.action_listener      = null;
		this.ext_control_listener = null;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void registerExternalControlListener(IOffboardExternalControl control_listener) {
		this.ext_control_listener = control_listener;
	}

	public void removeExternalControlListener() {
		this.ext_control_listener = null;
	}

	public void registerActionListener(IOffboardTargetAction listener) {
		this.action_listener = listener;
	}

	public void removeActionListener() {
		this.action_listener = null;
	}

	public IOffboardTargetAction getActionListener() {
		return action_listener;
	}

	@Override
	public void run() {

		float delta, delta_sec;  long tms, sleep_tms = 0; float[] ctl = new float[2];
		long watch_tms = System.currentTimeMillis();

		already_fired = false; valid_setpoint = false;

		logger.writeLocalMsg("[msp] OffboardUpdater started",MAV_SEVERITY.MAV_SEVERITY_DEBUG);
		model.sys.setAutopilotMode(MSP_AUTOCONTROL_ACTION.OFFBOARD_UPDATER, true);

		tms = model.sys.getSynchronizedPX4Time_us();

		//		// Check bottom clearance for minimal altitude above ground. Abort offboard if too low
		//		if(model.hud.ar < MIN_REL_ALTITUDE)
		//			enabled = false;

		while(enabled) {

			// TODO: Security checks: XY Sticks and altitude

			delta_sec = UPDATE_RATE / 1000.0f;

			if(valid_setpoint && (System.currentTimeMillis()-watch_tms ) > SETPOINT_TIMEOUT_MS  && !step_mode) {
				valid_setpoint = false; mode = MODE_POSITION;
				logger.writeLocalMsg("[msp] Offboard: Setpoint not reached. Loitering.",MAV_SEVERITY.MAV_SEVERITY_WARNING);
			}

			if(new_setpoint) {
				new_setpoint = false;
				ctl[IOffboardExternalControl.ANGLE] = 0;
				ctl[IOffboardExternalControl.SPEED] = 0;
			}

			switch(mode) {

			case MODE_POSITION:
				if(!valid_setpoint) {
					watch_tms = System.currentTimeMillis();
					setCurrentAsTarget();
				}
				current.set(model.state.l_x, model.state.l_y, model.state.l_z,model.attitude.y);
				sendPositionControlToVehice(target);

				if(step_mode && !step_trigger)
					continue;

				delta = MSP3DUtils.distance3D(target,current);
				if(delta < acceptance_radius_pos && valid_setpoint) {
					fireAction(model, delta);
					watch_tms = System.currentTimeMillis();
					step_trigger = false;
				}
				break;

			case MODE_SPEED:
				if(!valid_setpoint) {
					watch_tms = System.currentTimeMillis();
					target.set(0,0,0,0);
				}
				current.set(model.state.l_vx, model.state.l_vy, model.state.l_vz,model.attitude.yr);
				sendSpeedControlToVehice(target);

				if(step_mode && !step_trigger)
					continue;

				delta = MSP3DUtils.distance3D(target,current);
				if(delta < acceptance_radius_speed && valid_setpoint) {
					fireAction(model, delta);
					watch_tms = System.currentTimeMillis();
					step_trigger = false;
				}
				break;

			case MODE_SPEED_POSITION:

				current.set(model.state.l_x, model.state.l_y, model.state.l_z,model.attitude.y);
				watch_tms = System.currentTimeMillis();

				if(!valid_setpoint) {
					watch_tms = System.currentTimeMillis();
					current_speed.set(0,0,0);
				}

				delta = MSP3DUtils.distance2D(target,current);

				if(delta < acceptance_radius_pos && valid_setpoint) {
					watch_tms = System.currentTimeMillis();

					ctl[IOffboardExternalControl.ANGLE] = 0;
					ctl[IOffboardExternalControl.SPEED] = 0;

					fireAction(model, delta);
					step_trigger = false;
					mode = MODE_POSITION;
				}

				if(ext_control_listener!=null) {
					ctl = ext_control_listener.determine(model.hud.s, MSP3DUtils.getXYDirection(target, current), delta);
				}
				else {
					ctl[IOffboardExternalControl.ANGLE] = (float)(2*Math.PI)- MSP3DUtils.getXYDirection(target, current)+(float)Math.PI/2;
					if(delta > 1.0) {
						ctl[IOffboardExternalControl.SPEED] += 0.1*delta_sec;
						if(ctl[IOffboardExternalControl.SPEED] > MAX_SPEED) ctl[IOffboardExternalControl.SPEED] = MAX_SPEED;
					}
					else {
						ctl[IOffboardExternalControl.SPEED] -= 0.3*delta_sec;
						if(ctl[IOffboardExternalControl.SPEED] < 0.2) ctl[IOffboardExternalControl.SPEED] = 0.2f;
					}
				}

				current_speed.set((float)Math.sin(ctl[IOffboardExternalControl.ANGLE]), (float)Math.cos(ctl[IOffboardExternalControl.ANGLE]), 0) ;
				current_speed.scale(ctl[IOffboardExternalControl.SPEED]);

				constraint_speed(current_speed);

				sendSpeedControlToVehice(current_speed,(float)(2*Math.PI)-ctl[IOffboardExternalControl.ANGLE]+(float)Math.PI/2f);

				break;
			}


			sleep_tms = UPDATE_RATE - (model.sys.getSynchronizedPX4Time_us() - tms ) / 1000;
			tms = model.sys.getSynchronizedPX4Time_us();

			if(sleep_tms> 0 && enabled)
				try { Thread.sleep(sleep_tms); 	} catch (InterruptedException e) { }

			try { Thread.sleep(10); 	} catch (InterruptedException e) { }

		}
		action_listener = null;
		model.sys.setAutopilotMode(MSP_AUTOCONTROL_ACTION.OFFBOARD_UPDATER, false);
		logger.writeLocalMsg("[msp] OffboardUpdater stopped",MAV_SEVERITY.MAV_SEVERITY_DEBUG);
		publishSLAM(0,target,current);
		already_fired = false; valid_setpoint = false;
		model.sys.autopilot = 0;
	}

	private void constraint_speed(Vector3D_F32 s) {
		if(s.x >  MAX_SPEED )  s.x =  MAX_SPEED;
		if(s.y >  MAX_SPEED )  s.y =  MAX_SPEED;
		if(s.x < -MAX_SPEED )  s.x = -MAX_SPEED;
		if(s.y < -MAX_SPEED )  s.y = -MAX_SPEED;
	}

	private void sendPositionControlToVehice(Vector4D_F32 target) {

		msg_set_position_target_local_ned cmd = new msg_set_position_target_local_ned(1,2);
		cmd.target_component = 1;
		cmd.target_system    = 1;
		cmd.type_mask        = 0b000101111111000;

		cmd.x   = target.x;
		cmd.y   = target.y;
		cmd.z   = target.z;
		cmd.yaw = target.w;

		if(target.x==Float.MAX_VALUE) cmd.type_mask = cmd.type_mask | 0b000000000000001;
		if(target.y==Float.MAX_VALUE) cmd.type_mask = cmd.type_mask | 0b000000000000010;
		if(target.z==Float.MAX_VALUE) cmd.type_mask = cmd.type_mask | 0b000000000000100;
		if(target.w==Float.MAX_VALUE) cmd.type_mask = cmd.type_mask | 0b000010000000000;

		cmd.coordinate_frame = MAV_FRAME.MAV_FRAME_LOCAL_NED;

		if(!control.sendMAVLinkMessage(cmd))
			enabled = false;
	}

	private void sendSpeedControlToVehice(Vector4D_F32 target) {

		msg_set_position_target_local_ned cmd = new msg_set_position_target_local_ned(1,2);
		cmd.target_component = 1;
		cmd.target_system    = 1;
		cmd.type_mask        = 0b000011111000111;

		cmd.vx       = target.x;
		cmd.vy       = target.y;
		cmd.vz       = target.z;
		cmd.yaw_rate = target.w;

		if(target.x==Float.MAX_VALUE) cmd.type_mask = cmd.type_mask | 0b000000000001000;
		if(target.y==Float.MAX_VALUE) cmd.type_mask = cmd.type_mask | 0b000000000010000;
		if(target.z==Float.MAX_VALUE) cmd.type_mask = cmd.type_mask | 0b000000000100000;
		if(target.w==Float.MAX_VALUE) cmd.type_mask = cmd.type_mask | 0b000100000000000;

		cmd.coordinate_frame = MAV_FRAME.MAV_FRAME_LOCAL_NED;

		if(!control.sendMAVLinkMessage(cmd))
			enabled = false;
	}

	private void sendSpeedControlToVehice(Vector3D_F32 target, float yaw) {

		msg_set_position_target_local_ned cmd = new msg_set_position_target_local_ned(1,2);
		cmd.target_component = 1;
		cmd.target_system    = 1;
		cmd.type_mask        = 0b000101111000111;

		cmd.vx       = target.x;
		cmd.vy       = target.y;
		cmd.vz       = target.z;
		cmd.yaw      = yaw;

		if(target.x==Float.MAX_VALUE) cmd.type_mask = cmd.type_mask | 0b000000000001000;
		if(target.y==Float.MAX_VALUE) cmd.type_mask = cmd.type_mask | 0b000000000010000;
		if(target.z==Float.MAX_VALUE) cmd.type_mask = cmd.type_mask | 0b000000000100000;

		cmd.coordinate_frame = MAV_FRAME.MAV_FRAME_LOCAL_NED;

		if(!control.sendMAVLinkMessage(cmd))
			enabled = false;
	}

	private void fireAction(DataModel model,float delta) {
		if(action_listener!=null && !already_fired) {
			already_fired = true;
			action_listener.action(model, delta);
		}
	}

	private void publishSLAM(float speed, Vector4D_F32 target, Vector4D_F32 current) {
		msg_msp_micro_slam slam = new msg_msp_micro_slam(2,1);
		if(speed>0.05 && model.sys.isStatus(Status.MSP_MODE_OFFBOARD)) {
			slam.px = target.getX();
			slam.py = target.getY();
			slam.pd = MSP3DUtils.getXYDirection(target, current);
			slam.pv = speed;

			model.debug.x = slam.pd;
		}
		control.sendMAVLinkMessage(slam);
	}
}
