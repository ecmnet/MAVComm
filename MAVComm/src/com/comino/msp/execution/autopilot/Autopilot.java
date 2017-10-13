package com.comino.msp.execution.autopilot;

import java.util.Map.Entry;

import org.mavlink.messages.MAV_CMD;
import org.mavlink.messages.MAV_MODE_FLAG;
import org.mavlink.messages.MAV_SEVERITY;
import org.mavlink.messages.MSP_AUTOCONTROL_ACTION;
import org.mavlink.messages.MSP_AUTOCONTROL_MODE;

import com.comino.mav.control.IMAVController;
import com.comino.mav.mavlink.MAV_CUST_MODE;
import com.comino.msp.execution.autopilot.offboard.OffboardManager;
import com.comino.msp.execution.autopilot.tracker.WayPointTracker;
import com.comino.msp.execution.control.StatusManager;
import com.comino.msp.log.MSPLogger;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.LogMessage;
import com.comino.msp.model.segment.Status;
import com.comino.msp.utils.MSP3DUtils;
import com.comino.msp.utils.MSPMathUtils;
import com.comino.vfh.VfhHist;
import com.comino.vfh.vfh2D.HistogramGrid2D;
import com.comino.vfh.vfh2D.PolarHistogram2D;

import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F32;
import georegression.struct.point.Vector4D_F32;

public class Autopilot {

	private static Autopilot        autopilot = null;

	private OffboardManager         offboard = null;
	private DataModel               model    = null;
	private MSPLogger               logger   = null;
	private IMAVController          control  = null;
	private WayPointTracker         tracker  = null;


	public static Autopilot getInstance(IMAVController control) {
		if(autopilot == null)
			autopilot = new Autopilot(control);
		return autopilot;
	}

	public static Autopilot getInstance() {
		return autopilot;
	}

	private Autopilot(IMAVController control) {

		this.offboard = new OffboardManager(control);
		this.tracker  = new WayPointTracker(control);
		this.control  = control;
		this.model    = control.getCurrentModel();
		this.logger   = MSPLogger.getInstance();

		// Auto-Takeoff: Switch to Offboard as soon as takeoff completed
		control.getStatusManager().addListener(StatusManager.TYPE_PX4_STATUS, Status.MSP_MODE_TAKEOFF, StatusManager.EDGE_FALLING, (o,n) -> {
			offboard.setCurrentAsTarget();
			offboard.start(OffboardManager.MODE_POSITION);
			if(!model.sys.isStatus(Status.MSP_RC_ATTACHED)) {
				control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
						MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
						MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_OFFBOARD, 0 );
				control.writeLogMessage(new LogMessage("[msp] Auto-takeoff completed.", MAV_SEVERITY.MAV_SEVERITY_NOTICE));
			}
		});

		control.getStatusManager().addListener(StatusManager.TYPE_PX4_STATUS, Status.MSP_MODE_RTL, StatusManager.EDGE_RISING, (o,n) -> {
			offboard.stop();
		});

		// Stop offboard updater as soon as landed
		control.getStatusManager().addListener(StatusManager.TYPE_PX4_STATUS, Status.MSP_LANDED, StatusManager.EDGE_RISING, (o,n) -> {
			offboard.stop();
		});
	}
	public void setTarget(float x, float y, float z, float yaw) {
		Vector3D_F32 target = new Vector3D_F32(x,y,z);
		offboard.setTarget(target);
		offboard.start(OffboardManager.MODE_POSITION);
	}

	public void clearAutopilotActions() {
		model.sys.autopilot &= 0b01000000000000001111111111111111;
	}

	public void offboardPosHold(boolean enable) {
		if(enable) {
			offboard.setCurrentAsTarget();
			offboard.start(OffboardManager.MODE_POSITION);
			if(!model.sys.isStatus(Status.MSP_RC_ATTACHED) && !model.sys.isStatus(Status.MSP_LANDED)) {
				control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
						MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
						MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_OFFBOARD, 0 );
			}
		} else {
			if(model.sys.isStatus(Status.MSP_MODE_OFFBOARD)) {
				model.sys.autopilot = 0;
				control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
						MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
						MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_POSCTL, 0 );
			}
			offboard.stop();
		}
	}

	public void abort() {
		if(model.sys.isStatus(Status.MSP_RC_ATTACHED)) {
			control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
					MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
					MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_POSCTL, 0 );
			offboard.stop();
		}
	}

	public void jumpback(float distance) {
		if(!tracker.freeze())
			return;

		final Vector4D_F32 current = MSP3DUtils.getCurrentVector4D(model);

		logger.writeLocalMsg("[msp] JumpBack executed",MAV_SEVERITY.MAV_SEVERITY_WARNING);
		offboard.start(OffboardManager.MODE_POSITION);
		control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
				MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
				MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_OFFBOARD, 0 );

		offboard.setTarget(tracker.pollLastFreezedWaypoint().getValue());

		offboard.addListener((m,d) -> {
			Entry<Long, Vector4D_F32> e = tracker.pollLastFreezedWaypoint();
			if(e!=null && MSP3DUtils.distance3D(e.getValue(), current) < distance)
				offboard.setTarget(e.getValue());
			else {
				abort();
				tracker.unfreeze();
			}
		});
	}

	public void obstacleAvoidance(HistogramGrid2D his, PolarHistogram2D poh, float speed) {

		final Vector3D_F32 current = new Vector3D_F32();
		final Vector3D_F32 delta   = new Vector3D_F32();
		final Vector3D_F32 target  = new Vector3D_F32();

		current.set(model.state.l_x,model.state.l_y,model.state.l_z);
		target.set(current);

		float angle = poh.selectValley(0);
		System.err.println(MSPMathUtils.fromRad(angle));
		delta.set((float)Math.sin(angle)*speed, (float)Math.cos(angle)*speed, 0);
		target.plusIP(delta);

		offboard.setTarget(target);
		offboard.addListener((m,d) -> {
			if(his.nearestDistance(model.state.l_x, model.state.l_x, 0) < 0.3f) {
				float a = poh.selectValley(1);
				System.err.println(MSPMathUtils.fromRad(a));
				delta.set((float)Math.sin(a)*speed, (float)Math.cos(a)*speed, 0);
				target.plusIP(delta);
				offboard.setTarget(target);
			} else {
				logger.writeLocalMsg("[msp] ObstacleAvoidance finalized. Resume track.",MAV_SEVERITY.MAV_SEVERITY_INFO);

			}
		});

		offboard.start(OffboardManager.MODE_POSITION);
		control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
				MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
				MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_OFFBOARD, 0 );
		logger.writeLocalMsg("[msp] ObstacleAvoidance executed",MAV_SEVERITY.MAV_SEVERITY_WARNING);
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
				offboard.setTarget(circleTarget,0);
			});
			offboard.setTarget(circleTarget);
			offboard.start(OffboardManager.MODE_POSITION);
			logger.writeLocalMsg("[msp] Circlemode activated",MAV_SEVERITY.MAV_SEVERITY_INFO);
		}
		else {
			offboard.setTarget(circleCenter);
			offboard.addListener((m,d) -> {
				logger.writeLocalMsg("[msp] Circlemode stopped",MAV_SEVERITY.MAV_SEVERITY_INFO);
			});
		}
	}

	public void enableDebugMode1(boolean enable, float delta) {
		if(enable) {
			circleCenter.set(model.state.l_x,model.state.l_y,model.state.l_z);
			circleTarget.set(circleCenter);
			circleDelta.set(delta,0,0);
			circleTarget.plusIP(circleDelta);

			offboard.addListener((m,d) -> {
				circleTarget.plusIP(circleDelta);
				offboard.setTarget(circleTarget);
			});
			offboard.setTarget(circleTarget);
			offboard.start(OffboardManager.MODE_POSITION);
			logger.writeLocalMsg("[msp] DebugMode1 activated",MAV_SEVERITY.MAV_SEVERITY_INFO);
		}
		else {
			offboard.addListener((m,d) -> {
				logger.writeLocalMsg("[msp] DebugMode1 stopped",MAV_SEVERITY.MAV_SEVERITY_INFO);
			});
		}
	}

	public void enableDebugMode2(boolean enable, float delta) {
		if(enable) {
			circleCenter.set(model.state.l_x,model.state.l_y,model.state.l_z);
			circleTarget.set(circleCenter);
			circleDelta.set(0,delta,0);
			circleTarget.plusIP(circleDelta);

			offboard.addListener((m,d) -> {
				circleTarget.plusIP(circleDelta);
				offboard.setTarget(circleTarget);
			});
			offboard.setTarget(circleTarget);
			offboard.start(OffboardManager.MODE_POSITION);
			logger.writeLocalMsg("[msp] DebugMode2 activated",MAV_SEVERITY.MAV_SEVERITY_INFO);
		}
		else {
			offboard.addListener((m,d) -> {
				logger.writeLocalMsg("[msp] DebugMod2 stopped",MAV_SEVERITY.MAV_SEVERITY_INFO);
			});
		}
	}

	public void return_along_path(boolean enable) {
		if(!tracker.freeze())
			return;
		logger.writeLocalMsg("[msp] Return along the path",MAV_SEVERITY.MAV_SEVERITY_INFO);
		offboard.setTarget(tracker.pollLastFreezedWaypoint().getValue());
		offboard.start(OffboardManager.MODE_POSITION);
		offboard.addListener((m,d) -> {
			Entry<Long, Vector4D_F32> e = tracker.pollLastFreezedWaypoint();
			if(e!=null && e.getValue().z < -0.3f)
				offboard.setTarget(e.getValue());
			else {
				tracker.unfreeze();
				logger.writeLocalMsg("[msp] Return finalized",MAV_SEVERITY.MAV_SEVERITY_INFO);
				clearAutopilotActions() ;
			}
		});
	}


}
