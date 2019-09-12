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

import georegression.struct.point.Vector4D_F32;

public class OffboardManager implements Runnable {

	private static final int UPDATE_RATE                 			= 100;					  // offboard update rate in ms

	private static final float MAX_SPEED					        = 1.00f;          	      // Default Max speed in m/s
	private static final float MAX_YAW_SPEED                		= MSPMathUtils.toRad(30); // Max YawSpeed rad/s
	private static final float MAX_TURN_SPEED               		= 0.2f;   	              // Max speed that allow turning before start in m/s
	private static final float MAX_ACCELERATION		                = 0.3f;                   // Max acceleration in m/s2
	private static final float BREAKING_MINDISTANCE_1MS  	        = 1.5f;

	private static final int   RC_DEADBAND             				= 20;				      // RC XY deadband for safety check
	private static final int   RC_LAND_CHANNEL						= 8;                      // RC channel 8 landing
	private static final int   RC_LAND_THRESHOLD            		= 1600;		              // RC channel 8 landing threshold


	private static final int SETPOINT_TIMEOUT_MS         			= 15000;

	private static final float YAW_PV								= 0.05f;                  // P factor for yaw speed control
	private static final float YAW_P								= 0.25f;                  // P factor for yaw position control
	private static final float Z_PV						    		= 0.05f;                  // P factor for Z speed control

	private static final float YAW_ACCEPT                	    	= MSPMathUtils.toRad(2);  // Acceptance yaw deviation

	// Offboard modes

	public static final int MODE_LOITER	 		   					= 0;
	public static final int MODE_LOCAL_SPEED                		= 1;
	public static final int MODE_SPEED_POSITION	 	    			= 2;
	public static final int MODE_POSITION	 		            	= 3;

	//

	private MSPLogger 				 logger							= null;
	private DataModel 				 model							= null;
	private IMAVController         	 control      			       	= null;

	private IOffboardTargetAction        action_listener     	    = null;		// CB target reached
	private IOffboardExternalControl     ext_control_listener       = null;		// CB external angle+speed control in MODE_SPEED_POSITION
	private IOffboardExternalConstraints ext_constraints_listener   = null;		// CB Constrains in MODE_SPEED_POSITION

	private boolean					enabled					  		= false;
	private int						mode					  		= 0;		// Offboard mode

	private final Vector4D_F32		target					  		= new Vector4D_F32();	 // target state (coordinates/speeds) incl. yaw
	private final Vector4D_F32		current					  		= new Vector4D_F32();	 // current state incl. yaw
	private final Vector4D_F32      start                     		= new Vector4D_F32();    // state, when setpoint was set incl. yaw
	private final Vector4D_F32		cmd			  	            	= new Vector4D_F32();    // vehicle command state (coordinates/speeds)

	private final msg_set_position_target_local_ned pos_cmd   		= new msg_set_position_target_local_ned(1,2);
	private final msg_set_position_target_local_ned speed_cmd 		= new msg_set_position_target_local_ned(1,2);


	private float	 	acceptance_radius_pos						= 0.2f;
	private float       max_speed									= MAX_SPEED;
	private boolean    	already_fired			    				= false;
	private boolean    	valid_setpoint                   			= false;
	private boolean    	new_setpoint                   	 			= false;

	private long        setpoint_tms                        		= 0;
	private long	    last_update_tms                             = 0;

	public OffboardManager(IMAVController control) {
		this.control        = control;
		this.model          = control.getCurrentModel();
		this.logger         = MSPLogger.getInstance();

		MSPConfig config	= MSPConfig.getInstance();

		max_speed = config.getFloatProperty("AP2D_MAX_SPEED", String.valueOf(MAX_SPEED));
		System.out.println("Autopilot: maximum speed: "+max_speed+" m/s ");

		acceptance_radius_pos = config.getFloatProperty("AP2D_ACCEPT_RAD", String.valueOf(acceptance_radius_pos));
		System.out.println("Autopilot: acceptance radius: "+acceptance_radius_pos+" m");

		control.getStatusManager().addListener(StatusManager.TYPE_PX4_NAVSTATE,
				Status.NAVIGATION_STATE_OFFBOARD, StatusManager.EDGE_FALLING, (n) -> {
					valid_setpoint = false;
				});

		control.getStatusManager().addListener(Status.MSP_ARMED, (n) -> {
			model.slam.clear();
			model.slam.tms = model.sys.getSynchronizedPX4Time_us();

			// set to manual mode when armed/disarmed
			control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
					MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
					MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_MANUAL, 0 );

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


	public void stop() {
		enabled = false;
	}


	public void setTarget(Vector4D_F32 t) {
		target.set(t);
		valid_setpoint = true;
		new_setpoint = true;
		already_fired = false;
		setpoint_tms = System.currentTimeMillis();
	}

	public void setTarget(Vector4D_F32 t, int mode) {
		this.mode = mode;
		setTarget(t);
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

	public void registerExternalConstraintsListener(IOffboardExternalConstraints constraints) {
		this.ext_constraints_listener = constraints;
	}

	public void removeExternalControlListener() {
		// consider current setpoint as new to set the start position at
		this.new_setpoint = true;
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

		long watch_tms = System.currentTimeMillis();

		float delta_sec  = 0;
		float speed_incr = 0.1f;

		Polar3D_F32 path = new Polar3D_F32(); // planned direct path
		Polar3D_F32 spd  = new Polar3D_F32(); // current speed
		Polar3D_F32 ctl  = new Polar3D_F32(); // speed control

		already_fired = false; if(!new_setpoint) valid_setpoint = false;

		logger.writeLocalMsg("[msp] OffboardUpdater started",MAV_SEVERITY.MAV_SEVERITY_DEBUG);
		model.sys.setAutopilotMode(MSP_AUTOCONTROL_ACTION.OFFBOARD_UPDATER, true);

		last_update_tms = System.currentTimeMillis();

		while(enabled) {

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

			delta_sec = (System.currentTimeMillis() - last_update_tms ) / 1000.0f;

			last_update_tms = System.currentTimeMillis();

			switch(mode) {

			case MODE_LOITER:

				if(!valid_setpoint)
					setCurrentAsTarget();
				sendPositionControlToVehice(target, MAV_FRAME.MAV_FRAME_LOCAL_NED);
				watch_tms = System.currentTimeMillis();
				toModel(0,target,current);
				break;

			case MODE_LOCAL_SPEED:

				if(!valid_setpoint) {
					watch_tms = System.currentTimeMillis();
					setCurrentAsTarget();
				}

				spd.set(model.state.l_vx, model.state.l_vy, model.state.l_vz );
				ctl.set(target.x, target.y, target.z);
				path.angle_xy = ctl.angle_xy;

				// check external constraints
				if(ext_constraints_listener!=null)
					ext_constraints_listener.get(delta_sec, spd, path, ctl);

				ctl.get(cmd);

				// manual yaw control
				cmd.w = target.w;


				if(cmd.z==0) {

					//  simple P controller for altitude
					cmd.z = (model.target_state.l_z - current.z) / (UPDATE_RATE / 1000f) * Z_PV;
				}


				sendSpeedControlToVehice(cmd, MAV_FRAME.MAV_FRAME_LOCAL_NED);

				if((System.currentTimeMillis()- setpoint_tms) > 1000)
					valid_setpoint = false;
				else
					watch_tms = System.currentTimeMillis();
				break;

			case MODE_SPEED_POSITION:


				// a new setpoint was provided
				if(new_setpoint) {
					new_setpoint = false; ctl.clear();
					start.set(model.state.l_x, model.state.l_y, model.state.l_z,model.attitude.y);
					path.set(target, current);
				}

				watch_tms = System.currentTimeMillis();

				spd.set(model.state.l_vx, model.state.l_vy, model.state.l_vz );

				if(valid_setpoint)
					path.set(target, current);
				else
					path.clear();

				// target reached?
				if(path.value < acceptance_radius_pos && valid_setpoint ) {

					if(Float.isNaN(target.w)) {
						path.clear();
						ctl.clear();
						fireAction(model, path.value);
						mode = MODE_LOITER;
					} else
						// If target yaw is given, use mode position to turn
						mode = MODE_POSITION;
					continue;
				}


				// external speed control via control callback ?
				if(ext_control_listener!=null) {

					ext_control_listener.determine(delta_sec, spd, path, ctl);
				}
				else {

					// get angles from remaining path planned
					ctl.angle_xy =  path.angle_xy;
					ctl.angle_xz =  path.angle_xz;

					speed_incr = MAX_ACCELERATION * delta_sec;

					if(path.value < TrajMathLib.getAvoidanceDistance(spd.value, 0 , BREAKING_MINDISTANCE_1MS))  {
						speed_incr = - 2 * MAX_ACCELERATION * delta_sec;
					}

					// check external constraints
					if(ext_constraints_listener!=null)
						ext_constraints_listener.get(delta_sec, spd, path, ctl);

//					System.out.println(ctl.value);


					ctl.value = Math.min(ctl.value + speed_incr, MAX_SPEED);
					if(ctl.value < 0.1f) ctl.value = 0.1f;


				}

				// TODO: acceleration contraints

				// if vehicle is not moving and turn angle > 180° => turn before moving
				if( Math.abs(MSPMathUtils.normAngle(ctl.angle_xy - current.w)) > Math.PI/2 && ctl.value < MAX_TURN_SPEED)
					ctl.value = 0;


				//  simple P controller for yaw;
				cmd.w = MSPMathUtils.normAngle(path.angle_xy - current.w) / (UPDATE_RATE / 1000f) * YAW_PV;

				// get Cartesian speeds from polar
				ctl.get(cmd);

				sendSpeedControlToVehice(cmd, MAV_FRAME.MAV_FRAME_LOCAL_NED);

				toModel(ctl.value,target,current);

				break;

			case MODE_POSITION:

				if(!valid_setpoint)
					setCurrentAsTarget();

				path.set(target, current);

				if(path.value < acceptance_radius_pos && valid_setpoint && Math.abs(MSPMathUtils.normAngle(target.w - current.w)) < YAW_ACCEPT) {
					path.clear();
					ctl.clear();
					fireAction(model, path.value);
					mode = MODE_LOITER;
					continue;
				}

				cmd.set(target.x,target.y,target.z,current.w + MSPMathUtils.normAngle(target.w - current.w) * YAW_P);

				sendPositionControlToVehice(cmd, MAV_FRAME.MAV_FRAME_LOCAL_NED);
				toModel(0,target,current);

				break;

			}
			try { Thread.sleep(UPDATE_RATE); 	} catch (InterruptedException e) { }
		}

		action_listener = null; ext_control_listener = null; model.sys.autopilot = 0;
		logger.writeLocalMsg("[msp] OffboardUpdater stopped",MAV_SEVERITY.MAV_SEVERITY_DEBUG);
		already_fired = false; valid_setpoint = false;
	}


	private void sendPositionControlToVehice(Vector4D_F32 target, int frame) {

		pos_cmd.target_component = 1;
		pos_cmd.target_system    = 1;
		pos_cmd.type_mask        = 0b000101111111000;

		pos_cmd.x   = target.x;
		pos_cmd.y   = target.y;
		pos_cmd.z   = target.z;
		pos_cmd.yaw = Float.isNaN(target.w)? model.attitude.y : MSPMathUtils.normAngle(target.w);

		if(target.x==Float.MAX_VALUE) pos_cmd.type_mask = pos_cmd.type_mask | 0b000000000000001;
		if(target.y==Float.MAX_VALUE) pos_cmd.type_mask = pos_cmd.type_mask | 0b000000000000010;
		if(target.z==Float.MAX_VALUE) pos_cmd.type_mask = pos_cmd.type_mask | 0b000000000000100;
		if(target.w==Float.MAX_VALUE) pos_cmd.type_mask = pos_cmd.type_mask | 0b000010000000000;

		pos_cmd.coordinate_frame = frame;

		if(!control.sendMAVLinkMessage(pos_cmd))
			enabled = false;
	}

	private void sendSpeedControlToVehice(Vector4D_F32 target, int frame) {

		speed_cmd.target_component = 1;
		speed_cmd.target_system    = 1;
		speed_cmd.type_mask        = 0b000011111000111;


		if(target.x==Float.MAX_VALUE) speed_cmd.type_mask = speed_cmd.type_mask | 0b000000000001000;
		if(target.y==Float.MAX_VALUE) speed_cmd.type_mask = speed_cmd.type_mask | 0b000000000010000;
		if(target.z==Float.MAX_VALUE) speed_cmd.type_mask = speed_cmd.type_mask | 0b000000000100000;
		if(target.w==Float.MAX_VALUE) speed_cmd.type_mask = speed_cmd.type_mask | 0b000100000000000;

		// safety constraints
		checkAbsoluteSpeeds(target);

		speed_cmd.vx       = target.x;
		speed_cmd.vy       = target.y;
		speed_cmd.vz       = target.z;
		speed_cmd.yaw_rate = target.w;

		speed_cmd.coordinate_frame = frame;

		if(!control.sendMAVLinkMessage(speed_cmd))
			enabled = false;
	}

	private void checkAbsoluteSpeeds(Vector4D_F32 s) {

		if(s.x >  max_speed )  s.x =  max_speed;
		if(s.y >  max_speed )  s.y =  max_speed;
		if(s.z >  max_speed )  s.z =  max_speed;
		if(s.x < -max_speed )  s.x = -max_speed;
		if(s.y < -max_speed )  s.y = -max_speed;
		if(s.z < -max_speed )  s.z = -max_speed;

		if(s.w >   MAX_YAW_SPEED )  s.w =   MAX_YAW_SPEED;
		if(s.w <  -MAX_YAW_SPEED )  s.w =  -MAX_YAW_SPEED;
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
