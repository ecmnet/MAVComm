package com.comino.msp.execution.flightcontrol;

import java.util.List;

import org.mavlink.messages.MAV_SEVERITY;
import org.mavlink.messages.MSP_AUTOCONTROL_MODE;

import com.comino.msp.execution.IOffboardListener;
import com.comino.msp.execution.offboard.APSetPoint;
import com.comino.msp.execution.offboard.APSetPointUtils;
import com.comino.msp.execution.offboard.OffboardManager;
import com.comino.msp.log.MSPLogger;
import com.comino.msp.model.DataModel;
import com.comino.msp.utils.MSP3DUtils;

import georegression.geometry.ConvertRotation3D_F32;
import georegression.struct.point.Vector3D_F32;
import georegression.struct.se.Se3_F32;

public class FlightGestureControl {

	private OffboardManager offboard = null;
	private DataModel               model    = null;
	private MSPLogger               logger   = null;

	// Circle mode
	private APSetPoint circleCenter = new APSetPoint();
	private APSetPoint circleTarget = new APSetPoint();
	private float inc;
	private Vector3D_F32 circleDelta = new Vector3D_F32();


	public FlightGestureControl(DataModel model, OffboardManager offboard) {
		this.offboard = offboard;
		this.model    = model;
		this.logger   = MSPLogger.getInstance();
	}

	public void setTargetAndExecute(float x, float y, float z, float yaw) {
		APSetPoint target = new APSetPoint(x,y,z,yaw);
		offboard.setNextTarget(target);
	}

	public void jumpback(float distance) {
		APSetPoint target = new APSetPoint(model);
		target.position.set(target.position.x-distance*(float)Math.cos(model.attitude.y), target.position.y-distance*(float)Math.sin(model.attitude.y), target.position.z);
		offboard.setNextTarget(target);
	}

	public void enableCircleMode(boolean enable, float radius) {
		if(enable) {
			inc = model.attitude.y;
			circleCenter.set(model);
            circleTarget.set(circleCenter);
            circleDelta.set((float)Math.sin(inc), (float)Math.cos(inc), 0);
            circleTarget.position.plusIP(circleDelta);

			offboard.addListener((APSetPoint p,float d, int t) -> {
			    inc = inc+0.1f;
			    circleTarget.set(circleCenter);
	            circleDelta.set((float)Math.sin(inc)*radius, (float)Math.cos(inc)*radius, 0);
	            circleTarget.position.plusIP(circleDelta);
	            offboard.setNextTarget(circleTarget, OffboardManager.MODE_MULTI_NOCHECK);
			});
			offboard.setNextTarget(circleTarget, OffboardManager.MODE_MULTI_NOCHECK);
			logger.writeLocalMsg("[msp] Circlemode activated",MAV_SEVERITY.MAV_SEVERITY_INFO);
		}
		else {
			offboard.addListener((APSetPoint p,float d, int t) -> {
				logger.writeLocalMsg("[msp] Circlemode stopped",MAV_SEVERITY.MAV_SEVERITY_INFO);
			});
			offboard.setNextTarget(circleCenter);
		}
	}

	public void waypoint_example(float length) {
		APSetPoint target  = new APSetPoint(model);
		APSetPoint current = new APSetPoint(model);
		target.position.x = target.position.x+length;

        List<APSetPoint> list = APSetPointUtils.interpolatePositionLinear(20,current,target);
        for(APSetPoint p : list) {
		   offboard.addToPositionList(p);
        }



		target.position.y = target.position.y+length;
		offboard.addToPositionList(target.copy());
		target.position.x = target.position.x-length;
		offboard.addToPositionList(target.copy());
		target.position.y = target.position.y-length;
		offboard.addToPositionList(target.copy());
		System.err.println("Size: "+offboard.getWorkListSize());
		offboard.executeList();

	}

}
