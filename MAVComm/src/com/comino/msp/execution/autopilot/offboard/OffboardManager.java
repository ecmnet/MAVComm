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

package com.comino.msp.execution.autopilot.offboard;

import org.mavlink.messages.MAV_CMD;
import org.mavlink.messages.MAV_FRAME;
import org.mavlink.messages.MAV_MODE_FLAG;
import org.mavlink.messages.MAV_SEVERITY;
import org.mavlink.messages.MSP_AUTOCONTROL_ACTION;
import org.mavlink.messages.lquac.msg_msp_micro_slam;
import org.mavlink.messages.lquac.msg_set_position_target_local_ned;

import com.comino.main.MSPConfig;
import com.comino.mav.control.IMAVController;
import com.comino.mav.mavlink.MAV_CUST_MODE;
import com.comino.msp.execution.control.StatusManager;
import com.comino.msp.log.MSPLogger;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.Status;
import com.comino.msp.utils.MSP3DUtils;
import com.comino.msp.utils.MSPMathUtils;

import georegression.struct.point.Vector3D_F32;
import georegression.struct.point.Vector4D_F32;

public class OffboardManager implements Runnable {

	private static final float MAX_SPEED					= 1.0f;		// Default Max speed: 1 m/s
	private static final int   RC_DEADBAND             		= 20;		// RC XY deadband for safety check

	//	private static final float MIN_REL_ALTITUDE          = 0.3f;

	private static final int UPDATE_RATE                 	= 50;
	private static final int SETPOINT_TIMEOUT_MS         	= 15000;

	private static final float PI2							= 2f*(float)Math.PI;

	public static final int MODE_LOITER	 		   			= 0;
	public static final int MODE_POSITION	 		   		= 1;
	public static final int MODE_SPEED	 		        	= 2;
	public static final int MODE_SPEED_POSITION	 	    	= 3;
	public static final int MODE_LAND_LOCAL		 	    	= 4;
	public static final int MODE_START_LOCAL 	    		= 5;

	private MSPLogger 				logger					= null;
	private DataModel 				model					= null;
	private IMAVController         	control      			= null;
	private IOffboardTargetAction    action_listener     	= null;		// CB target reached
	private IOffboardExternalControl ext_control_listener   = null;		// CB external angle+speed control in MODE_SPEED_POSITION

	private boolean					enabled					= false;
	private int						mode					= 0;
	private Vector4D_F32			target					= null;
	private Vector4D_F32			current					= null;
	private Vector3D_F32			current_speed			= null;

	private float	 	acceptance_radius_pos				= 0.2f;
	private float	   	acceptance_radius_speed				= 0.05f;
	private float       acceptance_yaw                      = 0.02f;
	private float       max_speed							= MAX_SPEED;
	private float		break_radius					    = MAX_SPEED;
	private boolean    	already_fired			    		= false;
	private boolean    	valid_setpoint                   	= false;
	private boolean    	new_setpoint                   	 	= false;
	//	private boolean     step_mode                        	= false;
	//	private boolean    	step_trigger                    	= false;


	public OffboardManager(IMAVController control) {
		this.control        = control;
		this.model          = control.getCurrentModel();
		this.logger         = MSPLogger.getInstance();
		this.target         = new Vector4D_F32();
		this.current        = new Vector4D_F32();

		this.current_speed  = new Vector3D_F32();

		MSPConfig config		= MSPConfig.getInstance();

		max_speed = config.getFloatProperty("AP2D_MAX_SPEED", String.valueOf(MAX_SPEED));
		System.out.println("Offboard: speed constraints: "+max_speed+" m/s ");
		break_radius = max_speed*2;
		System.out.println("Offboard: break-radius: "+break_radius+" m");

		control.getStatusManager().addListener(StatusManager.TYPE_PX4_NAVSTATE,
				Status.NAVIGATION_STATE_OFFBOARD, StatusManager.EDGE_FALLING, (o,n) -> {
					valid_setpoint = false;
				});
	}

	public void start(int m) {
		mode = m;
		if(!enabled) {
			enabled = true;
			Thread worker = new Thread(this);
			worker.setPriority(Thread.MIN_PRIORITY);
			worker.setName("OffboardManager");
			worker.start();
		}
	}

	public void start(int m, int delay_ms) {
		try {
			Thread.sleep(delay_ms);
		} catch (InterruptedException e) {	}
		start(m);
	}

	public void stop() {
		enabled = false;
	}

	public void setTarget(Vector3D_F32 t) {
		mode = MODE_LOITER;
		target.set(t.x,t.y,t.z,Float.NaN);
		target.w = MSP3DUtils.getXYDirection(target, current);
		valid_setpoint = true;
		new_setpoint = true;
		already_fired = false;
	}

	public void setTarget(Vector3D_F32 t, float w) {
		mode = MODE_LOITER;
		target.set(t.x,t.y,t.z,w);
		valid_setpoint = true;
		new_setpoint = true;
		already_fired = false;
	}

	public void setTarget(Vector4D_F32 t) {
		mode = MODE_LOITER;
		target.set(t);
		valid_setpoint = true;
		new_setpoint = true;
		already_fired = false;
	}

	public Vector4D_F32 getCurrentTarget() {
		return target;
	}

	public void setCurrentAsTarget() {
		mode = MODE_LOITER;
		target.set(model.state.l_x, model.state.l_y, model.state.l_z, model.attitude.y);
		already_fired = false;
		new_setpoint = true;
		valid_setpoint = true;
	}

	public void setCurrentSetPointAsTarget() {
		mode = MODE_LOITER;
		target.set(model.target_state.l_x, model.target_state.l_y, model.target_state.l_z, model.attitude.sy);
		already_fired = false;
		new_setpoint = true;
		valid_setpoint = true;
	}

	public void finalize() {
		mode = MODE_LOITER;
		this.action_listener      = null;
		this.ext_control_listener = null;
		toModel(0,target,current);
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

		float delta, delta_sec, delta_w;  long tms, sleep_tms = 0; float[] ctl = new float[2];
		long watch_tms = System.currentTimeMillis();

		already_fired = false; if(!new_setpoint) valid_setpoint = false;

		logger.writeLocalMsg("[msp] OffboardUpdater started",MAV_SEVERITY.MAV_SEVERITY_DEBUG);
		model.sys.setAutopilotMode(MSP_AUTOCONTROL_ACTION.OFFBOARD_UPDATER, true);

		tms = System.currentTimeMillis();

		//		// Check bottom clearance for minimal altitude above ground. Abort offboard if too low
		//		if(model.hud.ar < MIN_REL_ALTITUDE)
		//			enabled = false;

		while(enabled) {

			sleep_tms = UPDATE_RATE - (System.currentTimeMillis() - tms );
			tms = System.currentTimeMillis();

			if(sleep_tms> 0 && enabled && sleep_tms < UPDATE_RATE)
				try { Thread.sleep(sleep_tms); 	} catch (InterruptedException e) { }

			if(model.sys.isStatus(Status.MSP_RC_ATTACHED)) {
				// Safety check: if RC attached, check XY sticks and fallback to POSHOLD if moved
				if(Math.abs(model.rc.s1 -1500) > RC_DEADBAND || Math.abs(model.rc.s2 -1500) > RC_DEADBAND) {
					logger.writeLocalMsg("[msp] OffboardUpdater stopped: RC",MAV_SEVERITY.MAV_SEVERITY_DEBUG);
					control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
							MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
							MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_POSCTL, 0 );
					enabled = false;
				}
			}

			delta_sec = UPDATE_RATE / 1000.0f;

			if(valid_setpoint && (System.currentTimeMillis()-watch_tms ) > SETPOINT_TIMEOUT_MS ) {
				valid_setpoint = false; mode = MODE_POSITION;

				if(model.sys.nav_state == Status.NAVIGATION_STATE_OFFBOARD)
					logger.writeLocalMsg("[msp] Offboard: Setpoint not reached. Loitering.",MAV_SEVERITY.MAV_SEVERITY_WARNING);
			}

			if(new_setpoint) {
				new_setpoint = false;
				ctl[IOffboardExternalControl.ANGLE] = 0;
				ctl[IOffboardExternalControl.SPEED] = 0;
				toModel(0,target,current);
			}

			switch(mode) {

			case MODE_LOITER:
				if(!valid_setpoint)
					setCurrentAsTarget();
				sendPositionControlToVehice(target);
				watch_tms = System.currentTimeMillis();
				break;

			case MODE_POSITION:
				if(!valid_setpoint) {
					watch_tms = System.currentTimeMillis();
					setCurrentAsTarget();
				}
				current.set(model.state.l_x, model.state.l_y, model.state.l_z,model.attitude.y);

				sendPositionControlToVehice(target);


				delta = MSP3DUtils.distance2D(target,current);
				delta_w = Float.isNaN(target.w) ? 0 : Math.abs(target.w - current.w);

				if(delta < acceptance_radius_pos && valid_setpoint && delta_w < acceptance_yaw) {
					fireAction(model, delta);
					watch_tms = System.currentTimeMillis();
					continue;
				}
				break;

			case MODE_SPEED:
				if(!valid_setpoint) {
					watch_tms = System.currentTimeMillis();
					target.set(0,0,0,0);
				}
				current.set(model.state.l_vx, model.state.l_vy, model.state.l_vz,model.attitude.yr);
				sendSpeedControlToVehice(target);

				delta = MSP3DUtils.distance3D(target,current);
				if(delta < acceptance_radius_speed && valid_setpoint) {
					fireAction(model, delta);
					watch_tms = System.currentTimeMillis();
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


				if(delta < acceptance_radius_pos && valid_setpoint ) {
					watch_tms = System.currentTimeMillis();

					ctl[IOffboardExternalControl.ANGLE] = 0;
					ctl[IOffboardExternalControl.SPEED] = 0;

					mode = MODE_POSITION;
					continue;
				}

				if(ext_control_listener!=null) {
					ctl = ext_control_listener.determine(model.hud.s, MSP3DUtils.getXYDirection(target, current), delta);
				}
				else {
					ctl[IOffboardExternalControl.ANGLE] = (float)(2*Math.PI)- MSP3DUtils.getXYDirection(target, current)+(float)Math.PI/2;
					// speed control: reduce speed if nearer than 1s of max_speed
					if(delta > break_radius) {
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

				toModel(current_speed.norm(),target,current);

				break;

			}

			try { Thread.sleep(10); 	} catch (InterruptedException e) { }

		}
		action_listener = null; ext_control_listener = null;
		model.sys.setAutopilotMode(MSP_AUTOCONTROL_ACTION.OFFBOARD_UPDATER, false);
		logger.writeLocalMsg("[msp] OffboardUpdater stopped",MAV_SEVERITY.MAV_SEVERITY_DEBUG);
		already_fired = false; valid_setpoint = false;
		model.sys.autopilot = 0;
	}

	private void constraint_speed(Vector3D_F32 s) {
		if(s.x >  max_speed )  s.x =  max_speed;
		if(s.y >  max_speed )  s.y =  max_speed;
		if(s.z >  max_speed )  s.z =  max_speed;
		if(s.x < -max_speed )  s.x = -max_speed;
		if(s.y < -max_speed )  s.y = -max_speed;
		if(s.z < -max_speed )  s.z = -max_speed;
	}

	private void sendPositionControlToVehice(Vector4D_F32 target) {

		msg_set_position_target_local_ned cmd = new msg_set_position_target_local_ned(1,2);
		cmd.target_component = 1;
		cmd.target_system    = 1;
		cmd.type_mask        = 0b000101111111000;

		if(target.w >  Math.PI) target.w = target.w - PI2;
		if(target.w < -Math.PI) target.w = target.w + PI2;

		cmd.x   = target.x;
		cmd.y   = target.y;
		cmd.z   = target.z;
		cmd.yaw = Float.isNaN(target.w)? model.attitude.y : target.w;

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

		if(yaw >  Math.PI) yaw = yaw - PI2;
		if(yaw < -Math.PI) yaw = yaw + PI2;

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

	private void toModel(float speed, Vector4D_F32 target, Vector4D_F32 current) {

		if(speed > 0.05) {
			model.slam.px = target.getX();
			model.slam.py = target.getY();
			model.slam.pz = target.getZ();
			model.slam.pd = MSP3DUtils.getXYDirection(target, current);
			model.slam.pv = speed;
			model.slam.di = MSP3DUtils.distance2D(target,current);
		} else
			model.slam.clear();

	}
}
