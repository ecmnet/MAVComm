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

	//	private static final float MIN_REL_ALTITUDE          = 0.3f;

	private static final int UPDATE_RATE                 = 50;
	private static final int SETPOINT_TIMEOUT_MS         = 5000;

	public static final int MODE_POSITION	 		    = 1;
	public static final int MODE_SPEED	 		        = 2;

	private MSPLogger 				logger				= null;
	private DataModel 				model				= null;
	private IMAVController         	control      		= null;
	private IOffboardListener        listener     		= null;

	private boolean					enabled				= false;
	private int						mode					= 0;
	private Vector4D_F32				target				= null;
	private Vector4D_F32				current				= null;

	private float		acceptance_radius_pos			= 0.2f;
	private float		acceptance_radius_speed			= 0.05f;
	private boolean     already_fired				    = false;
	private boolean     valid_setpoint                   = false;
	private boolean     step_mode                        = false;
	private boolean     step_trigger                     = false;


	public OffboardManager(IMAVController control) {
		this.control        = control;
		this.model          = control.getCurrentModel();
		this.logger         = MSPLogger.getInstance();
		this.target         = new Vector4D_F32();
		this.current        = new Vector4D_F32();

		control.getStatusManager().addListener(StatusManager.TYPE_PX4_STATUS,
				Status.MSP_MODE_OFFBOARD, StatusManager.EDGE_FALLING, (o,n) -> {
					valid_setpoint = false;
				});
	}

	public void start(int m) {
		if(!enabled) {
			mode = m;
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
		already_fired = false;
	}

	public void setTarget(Vector3D_F32 t, float w) {
		target.set(t.x,t.y,t.z,w);
		valid_setpoint = true;
		already_fired = false;
	}

	public void setTarget(Vector4D_F32 t) {
		target.set(t);
		valid_setpoint = true;
		already_fired = false;
	}

	public void setCurrentAsTarget() {
		target.set(model.state.l_x, model.state.l_y, model.state.l_z, model.attitude.y);
		already_fired = false;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void addListener(IOffboardListener listener) {
		this.listener = listener;
	}

	public void removeListener() {
		this.listener = null;
	}

	@Override
	public void run() {

		float delta; long tms; long sleep_tms = 0; long watch_tms = System.currentTimeMillis();

		already_fired = false; valid_setpoint = false;

		logger.writeLocalMsg("[msp] OffboardUpdater started",MAV_SEVERITY.MAV_SEVERITY_DEBUG);
		model.sys.setAutopilotMode(MSP_AUTOCONTROL_ACTION.OFFBOARD_UPDATER, true);

		tms = model.sys.getSynchronizedPX4Time_us();

		//		// Check bottom clearance for minimal altitude above ground. Abort offboard if too low
		//		if(model.hud.ar < MIN_REL_ALTITUDE)
		//			enabled = false;

		while(enabled) {

			if(valid_setpoint && (System.currentTimeMillis()-watch_tms ) > SETPOINT_TIMEOUT_MS  && !step_mode) {
				valid_setpoint = false; mode = MODE_POSITION;
				logger.writeLocalMsg("[msp] Offboard: Setpoint not reached. Loitering.",MAV_SEVERITY.MAV_SEVERITY_WARNING);
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
			}

			sleep_tms = UPDATE_RATE - (model.sys.getSynchronizedPX4Time_us() - tms ) / 1000;
			tms = model.sys.getSynchronizedPX4Time_us();

			if(sleep_tms> 0 && enabled)
				try { Thread.sleep(sleep_tms); 	} catch (InterruptedException e) { }

			//	publishSLAM(model.hud.s,target,current);

			try { Thread.sleep(10); 	} catch (InterruptedException e) { }

		}
		listener = null;
		model.sys.setAutopilotMode(MSP_AUTOCONTROL_ACTION.OFFBOARD_UPDATER, false);
		logger.writeLocalMsg("[msp] OffboardUpdater stopped",MAV_SEVERITY.MAV_SEVERITY_DEBUG);
		publishSLAM(0,target,current);
		already_fired = false; valid_setpoint = false;
		model.sys.autopilot = 0;
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

	private void fireAction(DataModel model,float delta) {
		if(listener!=null && !already_fired) {
			already_fired = true;
			listener.action(model, delta);
		}
	}

	private void publishSLAM(float speed, Vector4D_F32 target, Vector4D_F32 current) {
		msg_msp_micro_slam slam = new msg_msp_micro_slam(2,1);
		if(speed>0.05 && model.sys.isStatus(Status.MSP_MODE_OFFBOARD)) {
			slam.px = target.getX();
			slam.py = target.getY();
			slam.pd = MSP3DUtils.getXYDirection(target, current);
			slam.pv = speed;

			model.debug.v1 = slam.pd;
		}
		control.sendMAVLinkMessage(slam);
	}
}
