package com.comino.msp.execution.autopilot;

import java.util.List;

import org.mavlink.messages.MAV_CMD;
import org.mavlink.messages.MAV_MODE_FLAG;
import org.mavlink.messages.MAV_SEVERITY;
import org.mavlink.messages.MSP_AUTOCONTROL_MODE;

import com.comino.mav.control.IMAVController;
import com.comino.mav.mavlink.MAV_CUST_MODE;
import com.comino.msp.execution.autopilot.offboard.OffboardManager;
import com.comino.msp.execution.autopilot.old.APSetPoint;
import com.comino.msp.execution.control.StatusManager;
import com.comino.msp.log.MSPLogger;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.LogMessage;
import com.comino.msp.model.segment.Status;
import com.comino.msp.utils.MSP3DUtils;

import georegression.geometry.ConvertRotation3D_F32;
import georegression.struct.point.Vector3D_F32;
import georegression.struct.se.Se3_F32;

public class Autopilot {

	private static Autopilot        autopilot = null;

	private OffboardManager         offboard = null;
	private DataModel               model    = null;
	private MSPLogger               logger   = null;
	private IMAVController          control  = null;


	public static Autopilot getInstance(IMAVController control) {
		if(autopilot == null)
			autopilot = new Autopilot(control);
		return autopilot;
	}

	private Autopilot(IMAVController control) {
		this.offboard = new OffboardManager(control);
		this.control  = control;
		this.model    = control.getCurrentModel();
		this.logger   = MSPLogger.getInstance();

		// Auto-Takeoff: Switch to Offboard as soon as takeoff completed
		control.getStatusManager().addListener(StatusManager.TYPE_PX4_STATUS, Status.MSP_MODE_TAKEOFF, StatusManager.EDGE_FALLING, (o,n) -> {
			if(!model.sys.isStatus(Status.MSP_RC_ATTACHED) && !model.sys.isStatus(Status.MSP_JOY_ATTACHED)) {
				offboard.setTarget(model);
				offboard.start(OffboardManager.MODE_POSITION);
				control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
						MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
						MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_OFFBOARD, 0 );
				control.writeLogMessage(new LogMessage("[msp] Auto-takeoff completed.", MAV_SEVERITY.MAV_SEVERITY_NOTICE));
			}
		});

		control.getStatusManager().addListener(StatusManager.TYPE_PX4_STATUS, Status.MSP_LANDED, StatusManager.EDGE_RISING, (o,n) -> {
			offboard.stop();
		});
	}

	public void setTarget(float x, float y, float z, float yaw) {
		Vector3D_F32 target = new Vector3D_F32(x,y,z);
		offboard.setTarget(target);
	}

	public void abort() {
		model.sys.autopilot = 0;
		control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
				MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
				MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_POSCTL, 0 );
		offboard.stop();
	}

	public void jumpback(float distance) {
		//		APSetPoint target = new APSetPoint(model);
		//		target.position.set(target.position.x-distance*(float)Math.cos(model.attitude.y), target.position.y-distance*(float)Math.sin(model.attitude.y), target.position.z);
		//		offboard.setTarget(target.position);
	}

	// Circle mode
	private Vector3D_F32 circleCenter = new Vector3D_F32();
	private Vector3D_F32 circleTarget = new Vector3D_F32();
	private float inc;
	private Vector3D_F32 circleDelta = new Vector3D_F32();

	public void enableCircleMode(boolean enable, float radius) {
		if(enable) {
			inc = model.attitude.y;
			circleCenter.set(model.state.l_x,model.state.l_y,model.state.l_z);
			circleTarget.set(circleCenter);
			circleDelta.set((float)Math.sin(inc), (float)Math.cos(inc), 0);
			circleTarget.plusIP(circleDelta);

			offboard.addListener((m,d) -> {
				inc = inc+0.2f;
				circleTarget.set(circleCenter);
				circleDelta.set((float)Math.sin(inc)*radius, (float)Math.cos(inc)*radius, 0);
				circleTarget.plusIP(circleDelta);
				offboard.setTarget(circleTarget);
			});
			offboard.setTarget(circleTarget);
			offboard.start(OffboardManager.MODE_POSITION);
			logger.writeLocalMsg("[msp] Circlemode activated",MAV_SEVERITY.MAV_SEVERITY_INFO);
		}
		else {
			offboard.addListener((m,d) -> {
				logger.writeLocalMsg("[msp] Circlemode stopped",MAV_SEVERITY.MAV_SEVERITY_INFO);
			});
			offboard.setTarget(circleCenter);
		}
	}

	public void waypoint_example(float length) {

		offboard.setTarget(model); offboard.start(OffboardManager.MODE_POSITION);
		control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
				MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
				MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_OFFBOARD, 0 );

		////		APSetPoint start  = new APSetPoint(APSetPoint.TYPE_TAKEOFF);
		////		offboard.addToList(start);
		//
		//		APSetPoint target  = new APSetPoint(model);
		//		target.position.z = -8;
		//	//	for(int z=0;z<5;z++) {
		//		target.position.x = target.position.x+length;
		//		offboard.addAllToList(APSetPointUtils.interpolateCombinedLinear(30,2000,new APSetPoint(model),target));
		//		target.position.y = target.position.y+length;
		//		offboard.addAllToList(APSetPointUtils.interpolateCombinedLinear(30,2000,offboard.getLastAdded(),target));
		//		target.position.x = target.position.x-length;
		//		offboard.addAllToList(APSetPointUtils.interpolateCombinedLinear(30,2000,offboard.getLastAdded(),target));
		//		target.position.y = target.position.y-length;
		//		offboard.addAllToList(APSetPointUtils.interpolateCombinedLinear(30,2000,offboard.getLastAdded(),target));
		//	//	}
		//		APSetPoint land  = new APSetPoint(APSetPoint.TYPE_LAND);
		//		offboard.addToList(land);
		//
		//		offboard.executeList();

	}

}
