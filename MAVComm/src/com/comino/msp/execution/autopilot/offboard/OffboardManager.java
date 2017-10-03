package com.comino.msp.execution.autopilot.offboard;

import org.mavlink.messages.MAV_FRAME;
import org.mavlink.messages.MAV_SEVERITY;
import org.mavlink.messages.lquac.msg_set_position_target_local_ned;

import com.comino.mav.control.IMAVController;
import com.comino.msp.log.MSPLogger;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.Status;

import georegression.struct.point.Vector3D_F32;

public class OffboardManager implements Runnable {

	public static final int MODE_POSITION	 		    = 1;
	public static final int MODE_SPEED	 		        = 2;

	private MSPLogger 				logger				= null;
	private DataModel 				model				= null;
	private IMAVController         	control      		= null;
	private IOffboardListener        listener     		= null;

	private boolean					enabled				= false;
	private int						mode					= 0;
	private Vector3D_F32				target				= null;
	private Vector3D_F32				current				= null;

	private float		acceptance_radios_pos			= 0.2f;
	private float		acceptance_radios_speed			= 0.05f;
	private boolean     already_fired				    = false;


	public OffboardManager(IMAVController control) {
		this.control        = control;
		this.model          = control.getCurrentModel();
		this.logger         = MSPLogger.getInstance();
		this.target         = new Vector3D_F32();
		this.current        = new Vector3D_F32();
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

	public void setTarget(Vector3D_F32 t) {
		target.set(t);
		already_fired = false;
	}

	public void setTarget(DataModel model) {
		target.set(model.state.l_x, model.state.l_y, model.state.l_z);
		already_fired = false;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void addListener(IOffboardListener listener) {
		this.listener = listener;
	}

	@Override
	public void run() {

		float delta;

		already_fired = false;

		logger.writeLocalMsg("[msp] Offboard started",MAV_SEVERITY.MAV_SEVERITY_DEBUG);
		model.sys.setStatus(Status.MSP_OFFBOARD_UPDATER_STARTED, true);

		while(enabled) {
			switch(mode) {
			case MODE_POSITION:
				current.set(model.state.l_x, model.state.l_y, model.state.l_z);
				sendPositionControlToVehice(target,current);
				delta = target.distance(current);
				if(delta < acceptance_radios_pos) {
					fireAction(model, delta);
				}
				break;
			case MODE_SPEED:
				current.set(model.state.l_vx, model.state.l_vy, model.state.l_vz);
				sendSpeedControlToVehice(target);
				delta = target.distance(current);
				if(delta < acceptance_radios_speed) {
					fireAction(model, delta);
				}
				break;
			}
			try { Thread.sleep(50); 	} catch (InterruptedException e) { }
		}
		listener = null;
		model.sys.setStatus(Status.MSP_OFFBOARD_UPDATER_STARTED, false);
		logger.writeLocalMsg("[msp] Offboard stopped",MAV_SEVERITY.MAV_SEVERITY_DEBUG);
		already_fired = false;
	}

	private void sendPositionControlToVehice(Vector3D_F32 target, Vector3D_F32 current) {

		msg_set_position_target_local_ned cmd = new msg_set_position_target_local_ned(1,2);
		cmd.target_component = 1;
		cmd.target_system = 1;
		cmd.type_mask = 0b000101111111000;
		//		cmd.type_mask = 0b000101111000000;

		// TODO: better: set Mask accordingly
		if(target.x!=Float.NaN) cmd.x = target.x; else cmd.x = current.x;
		if(target.y!=Float.NaN) cmd.y = target.y; else cmd.y = current.y;
		if(target.z!=Float.NaN) cmd.z = target.z; else cmd.z = current.z;

		//	cmd.yaw = MSPConvertUtils.getDirectionFromTargetXY(model,target);
		cmd.coordinate_frame = MAV_FRAME.MAV_FRAME_LOCAL_NED;

		if(!control.sendMAVLinkMessage(cmd))
			enabled = false;
	}

	private void sendSpeedControlToVehice(Vector3D_F32 target) {

		msg_set_position_target_local_ned cmd = new msg_set_position_target_local_ned(1,2);
		cmd.target_component = 1;
		cmd.target_system = 1;
		cmd.type_mask = 0b000101111000111;

		// TODO: better: set Mask accordingly
		if(target.x!=Float.NaN) cmd.vx = target.x; else cmd.vx = 0;
		if(target.y!=Float.NaN) cmd.vy = target.y; else cmd.vy = 0;
		if(target.z!=Float.NaN) cmd.vz = target.z; else cmd.vz = 0;

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

}
