/****************************************************************************
 *
 *   Copyright (c) 2017,2019 Eike Mansfeld ecm@gmx.de. All rights reserved.
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

import org.mavlink.messages.MAV_CMD;
import org.mavlink.messages.MAV_FRAME;
import org.mavlink.messages.MAV_MODE_FLAG;
import org.mavlink.messages.MAV_SEVERITY;
import org.mavlink.messages.MSP_AUTOCONTROL_ACTION;
import org.mavlink.messages.lquac.msg_msp_micro_slam;
import org.mavlink.messages.lquac.msg_set_position_target_local_ned;

import com.comino.libs.TrajMathLib;
import com.comino.main.MSPConfig;
import com.comino.mav.control.IMAVController;
import com.comino.mav.mavlink.MAV_CUST_MODE;
import com.comino.msp.execution.control.StatusManager;
import com.comino.msp.log.MSPLogger;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.Status;
import com.comino.msp.utils.MSP3DUtils;
import com.comino.msp.utils.MSPMathUtils;
import com.comino.msp.utils.struct.Polar3D_F32;

import georegression.struct.point.Vector3D_F32;
import georegression.struct.point.Vector4D_F32;

public class OffboardManager implements Runnable {

	private static final float MAX_SPEED					= 1.00f;          	// Default Max speed in m/s
	private static final float MAX_TURN_SPEED               = 0.2f;   	        // Max speed that allow turning before start

	private static final int   RC_DEADBAND             		= 20;				// RC XY deadband for safety check
	private static final int   RC_LAND_CHANNEL				= 8;                // RC channel 8 landing
	private static final int   RC_LAND_THRESHOLD            = 1600;		        // RC channel 8 landing threshold

	private static final int UPDATE_RATE                 	= 50;
	private static final int SETPOINT_TIMEOUT_MS         	= 15000;

	public static final int MODE_LOITER	 		   			= 0;
	public static final int MODE_POSITION	 		   		= 1;
	public static final int MODE_SPEED	 		        	= 2;
	public static final int MODE_SPEED_POSITION	 	    	= 3;
	public static final int MODE_LAND_LOCAL		 	    	= 4;
	public static final int MODE_START_LOCAL 	    		= 5;

	private MSPLogger 				 logger					= null;
	private DataModel 				 model					= null;
	private IMAVController         	 control      			= null;
	private IOffboardTargetAction    action_listener     	= null;		// CB target reached
	private IOffboardExternalControl ext_control_listener   = null;		// CB external angle+speed control in MODE_SPEED_POSITION

	private boolean					enabled					= false;
	private int						mode					= 0;
	private final Vector4D_F32		target					= new Vector4D_F32();
	private final Vector4D_F32		current					= new Vector4D_F32();
	private final Vector4D_F32      start                   = new Vector4D_F32();
	private final Vector3D_F32		control_speed			= new Vector3D_F32();


	private float	 	acceptance_radius_pos				= 0.2f;
	private float       acceptance_yaw                      = 0.02f;
	private float       max_speed							= MAX_SPEED;
	private float		break_radius					    = MAX_SPEED;
	private boolean    	already_fired			    		= false;
	private boolean    	valid_setpoint                   	= false;
	private boolean    	new_setpoint                   	 	= false;

	private long        setpoint_tms                        = 0;

	public OffboardManager(IMAVController control) {
		this.control        = control;
		this.model          = control.getCurrentModel();
		this.logger         = MSPLogger.getInstance();

		MSPConfig config		= MSPConfig.getInstance();

		max_speed = config.getFloatProperty("AP2D_MAX_SPEED", String.valueOf(MAX_SPEED));
		System.out.println("Offboard: speed constraints: "+max_speed+" m/s ");
		System.out.println("Offboard: break-radius: "+break_radius+" m");

		control.getStatusManager().addListener(StatusManager.TYPE_PX4_NAVSTATE,
				Status.NAVIGATION_STATE_OFFBOARD, StatusManager.EDGE_FALLING, (o,n) -> {
					valid_setpoint = false;
				});
		control.getStatusManager().addListener(Status.MSP_ARMED, (o,n) -> {
			model.slam.clear();
			model.slam.tms = model.sys.getSynchronizedPX4Time_us();

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
			try { Thread.sleep(5); } catch (InterruptedException e) { }
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
		//	mode = MODE_LOITER;
		target.set(t.x,t.y,t.z,Float.NaN);
		target.w = MSP3DUtils.getXYDirection(target, current);
		valid_setpoint = true;
		new_setpoint = true;
		already_fired = false;
		setpoint_tms = System.currentTimeMillis();
	}

	public void setTarget(Vector3D_F32 t, float w) {
		//	mode = MODE_LOITER;
		target.set(t.x,t.y,t.z,w);
		valid_setpoint = true;
		new_setpoint = true;
		already_fired = false;
		setpoint_tms = System.currentTimeMillis();
	}

	public void setTarget(Vector4D_F32 t) {
		//	mode = MODE_LOITER;
		target.set(t);
		valid_setpoint = true;
		new_setpoint = true;
		already_fired = false;
		setpoint_tms = System.currentTimeMillis();
	}

	public void setTarget(Vector4D_F32 t, int mode) {
		this.mode = mode;
		target.set(t);
		valid_setpoint = true;
		new_setpoint = true;
		already_fired = false;
		setpoint_tms = System.currentTimeMillis();
	}

	public void setTarget(float x, float y, float z, float yaw, int mode) {
		this.mode = mode;
		target.set(x,y,z,yaw);
		valid_setpoint = true;
		new_setpoint = true;
		already_fired = false;
		setpoint_tms = System.currentTimeMillis();
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
		setpoint_tms = System.currentTimeMillis();
	}

	public void setCurrentSetPointAsTarget() {
		mode = MODE_LOITER;
		target.set(model.target_state.l_x, model.target_state.l_y, model.target_state.l_z, model.attitude.sy);
		already_fired = false;
		new_setpoint = true;
		valid_setpoint = true;
		setpoint_tms = System.currentTimeMillis();
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

		float delta, delta_w; long tms, sleep_tms = 0;
		long watch_tms = System.currentTimeMillis();

		Polar3D_F32 path = new Polar3D_F32(); // planned direct path
		Polar3D_F32 ctl  = new Polar3D_F32(); // speed control
		Polar3D_F32 way  = new Polar3D_F32(); // path already done

		already_fired = false; if(!new_setpoint) valid_setpoint = false;

		logger.writeLocalMsg("[msp] OffboardUpdater started",MAV_SEVERITY.MAV_SEVERITY_INFO);
		model.sys.setAutopilotMode(MSP_AUTOCONTROL_ACTION.OFFBOARD_UPDATER, true);

		tms = System.currentTimeMillis();

		while(enabled) {

			sleep_tms = UPDATE_RATE - (System.currentTimeMillis() - tms );
			tms = System.currentTimeMillis();

			if(sleep_tms> 0 && enabled && sleep_tms < UPDATE_RATE)
				try { Thread.sleep(sleep_tms); 	} catch (InterruptedException e) { }

			if(model.sys.isStatus(Status.MSP_RC_ATTACHED) && !safety_check()) {
				enabled = false;
				continue;
			}

			current.set(model.state.l_x, model.state.l_y, model.state.l_z,model.attitude.y);

			if(valid_setpoint && (System.currentTimeMillis()-watch_tms ) > SETPOINT_TIMEOUT_MS ) {
				valid_setpoint = false; mode = MODE_LOITER;

				if(model.sys.nav_state == Status.NAVIGATION_STATE_OFFBOARD)
					logger.writeLocalMsg("[msp] Offboard: Setpoint not reached. Loitering.",MAV_SEVERITY.MAV_SEVERITY_WARNING);
			}

			// a new setpoint was provided
			if(new_setpoint) {
				new_setpoint = false; ctl.clear(); way.clear();

				start.set(model.state.l_x, model.state.l_y, model.state.l_z,model.attitude.y);

				path.set(target, current);

				break_radius = path.value / 2f;

			}

			switch(mode) {

			case MODE_LOITER:
				if(!valid_setpoint)
					setCurrentAsTarget();
				sendPositionControlToVehice(target);
				watch_tms = System.currentTimeMillis();
				toModel(control_speed.norm(),target,current);
				break;

			case MODE_POSITION:
				if(!valid_setpoint) {
					watch_tms = System.currentTimeMillis();
					setCurrentAsTarget();
				}

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
				sendSpeedControlToVehice(target,MAV_FRAME.MAV_FRAME_BODY_NED);

				if((System.currentTimeMillis()- setpoint_tms) > 1000)
					valid_setpoint = false;
				else
					watch_tms = System.currentTimeMillis();
				break;

			case MODE_SPEED_POSITION:

				// TODO:
				// - Switch to Loiter mode only if no new setpoint after certain timeout or empty queue
				// - Setpoint-Queue
				//   Calculate yaw limit depending on the estimated flight time to next WP
				// - yaw speed control to be added
				// - add distance to WP target to KeyFigures/Datamodel
				// - eventually PID controller for Z
				// - Control setpoint changes during flight (speed limit)

				// see:
				// https://github.com/PX4/Firmware/commit/4eb9c7d812c8ac78a6609057466135f47798e8c8

				watch_tms = System.currentTimeMillis();

				way.set(current,start);

				if(valid_setpoint)
					path.set(target, current);
				else
					path.clear();


				if(path.value < acceptance_radius_pos && valid_setpoint ) {
					ctl.clear();
					fireAction(model, path.value);
					mode = MODE_LOITER;
					continue;
				}

				if(ext_control_listener!=null) {

					ext_control_listener.determine(model.state.getXYSpeed(), MSP3DUtils.getXYDirection(target, current), path.value, ctl);
					ctl.angle_xz =  path.angle_xz;
				}
				else {

					// get angles from direct path planned
					ctl.angle_xy =  path.angle_xy;
					ctl.angle_xz =  path.angle_xz;

					// determine speed depending on the distance
					if(way.value < break_radius)
						ctl.value = TrajMathLib.computeSpeedFromDistance(5f, 1f, max_speed, way.value);
					else
						ctl.value = TrajMathLib.computeSpeedFromDistance(1.5f, 1f, max_speed, path.value);

				}

				// if vehicle is not moving and turn angle > 180° => turn before moveing
				if(Math.abs(ctl.angle_xy - current.w) > Math.PI && model.state.getXYSpeed() < MAX_TURN_SPEED)
					ctl.value = 0;

				// Collision detection 2nd level: Absolute speed constraint if collision risk detected
				// Experimental: Risk is if min_distance of detector < 1.5m; at 0.3m set speed to 0
				// Should be only in derection of flight (+/-75 degree)
				// Put that into constraint speed
				//				if(!Float.isNaN(model.slam.dm) && model.slam.dm < 1.5f && model.slam.dm > 0) {
				//					logger.writeLocalMsg("[msp] Offboard: Collision risk. Speed reduced.",MAV_SEVERITY.MAV_SEVERITY_INFO);
				//					ctl[IOffboardExternalControl.SPEED] = ctl[IOffboardExternalControl.SPEED] * (model.slam.dm - 0.3f);
				//					if(ctl[IOffboardExternalControl.SPEED]<0)
				//						ctl[IOffboardExternalControl.SPEED] = 0;
				//				}

				// get kartesian from polar coordinates
				ctl.get(control_speed);
				constraint_speed(control_speed);

				sendSpeedControlToVehice(control_speed,ctl.angle_xy);

				//	System.out.println(ctl+ "/ "+path);
				toModel(control_speed.norm(),target,current);

				break;

			}
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

		cmd.x   = target.x;
		cmd.y   = target.y;
		cmd.z   = target.z;
		cmd.yaw = Float.isNaN(target.w)? model.attitude.y : MSPMathUtils.normAngle(target.w);

		if(target.x==Float.MAX_VALUE) cmd.type_mask = cmd.type_mask | 0b000000000000001;
		if(target.y==Float.MAX_VALUE) cmd.type_mask = cmd.type_mask | 0b000000000000010;
		if(target.z==Float.MAX_VALUE) cmd.type_mask = cmd.type_mask | 0b000000000000100;
		if(target.w==Float.MAX_VALUE) cmd.type_mask = cmd.type_mask | 0b000010000000000;

		cmd.coordinate_frame = MAV_FRAME.MAV_FRAME_LOCAL_NED;

		if(!control.sendMAVLinkMessage(cmd))
			enabled = false;
	}

	private void sendSpeedControlToVehice(Vector4D_F32 target, int frame) {

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

		cmd.coordinate_frame = frame;

		if(!control.sendMAVLinkMessage(cmd))
			enabled = false;
	}

	private void sendSpeedControlToVehice(Vector3D_F32 target, float yaw) {

		msg_set_position_target_local_ned cmd = new msg_set_position_target_local_ned(1,2);
		cmd.target_component = 1;
		cmd.target_system    = 1;
		cmd.type_mask        = 0b00000111000111;

		cmd.vx       = target.x;
		cmd.vy       = target.y;
		cmd.vz       = target.z;
		cmd.yaw      = MSPMathUtils.normAngle(yaw);

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

		if(mode!=MODE_LOITER) {
			model.slam.px = target.getX();
			model.slam.py = target.getY();
			model.slam.pz = target.getZ();
			model.slam.pd = MSP3DUtils.getXYDirection(target, current);
			model.slam.pv = speed;
			model.slam.di = MSP3DUtils.distance2D(target,current);
		} else
			model.slam.clear();

		model.slam.tms = model.sys.getSynchronizedPX4Time_us();
	}

	private boolean safety_check() {

		if(Math.abs(model.rc.s1 -1500) > RC_DEADBAND || Math.abs(model.rc.s2 -1500) > RC_DEADBAND) {
			logger.writeLocalMsg("[msp] OffboardUpdater stopped: RC",MAV_SEVERITY.MAV_SEVERITY_INFO);
			control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
					MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
					MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_POSCTL, 0 );
			return false;
		}

		// Safety: Channel 8 triggers landing mode of PX4
		if(model.rc.get(RC_LAND_CHANNEL) > RC_LAND_THRESHOLD) {
			logger.writeLocalMsg("[msp] OffboardUpdater stopped: Landing",MAV_SEVERITY.MAV_SEVERITY_INFO);
			control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_NAV_LAND, 0, 2, 0.05f );
			return false;
		}
		return true;
	}

}
