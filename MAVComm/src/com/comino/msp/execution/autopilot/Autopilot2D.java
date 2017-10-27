package com.comino.msp.execution.autopilot;

import java.util.Map.Entry;

import org.mavlink.messages.MAV_CMD;
import org.mavlink.messages.MAV_MODE_FLAG;
import org.mavlink.messages.MAV_SEVERITY;
import org.mavlink.messages.MSP_AUTOCONTROL_MODE;
import org.mavlink.messages.lquac.msg_msp_micro_grid;
import org.mavlink.messages.lquac.msg_msp_micro_slam;

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
import com.comino.vfh.vfh2D.HistogramGrid2D;
import com.comino.vfh.vfh2D.PolarHistogram2D;

import georegression.struct.point.Vector3D_F32;
import georegression.struct.point.Vector4D_F32;

public class Autopilot2D implements Runnable {

	private static final float HIS_WINDOWSIZE       = 3f;

	private static final float POH_DENSITY_B        = 0.24f;
	private static final float POH_DENSITY_A        = 10f;
	private static final float POH_THRESHOLD        = 1f;
	private static final int   POH_ALPHA            = 1;
	private static final int   POH_SMAX             = 50;
	private static final int   POH_SMOOTHING        = 10;

	private static final float OBSTACLE_MINDISTANCE  = 1.25f;
	private static final float OBSTACLE_FAILDISTANCE = 0.3f;
	private static final float OBSTACLE_SPEEDFACTOR  = 0.3f;

	private static Autopilot2D      autopilot = null;

	private OffboardManager         offboard = null;
	private DataModel               model    = null;
	private MSPLogger               logger   = null;
	private IMAVController          control  = null;
	private WayPointTracker         tracker  = null;

	private HistogramGrid2D  		vfh 		= null;
	private PolarHistogram2D 		poh 		= null;

	private IAutoPilotGetTarget targetListener = null;

	private boolean              isAvoiding  = false;


	public static Autopilot2D getInstance(IMAVController control) {
		if(autopilot == null)
			autopilot = new Autopilot2D(control);
		return autopilot;
	}

	public static Autopilot2D getInstance() {
		return autopilot;
	}

	private Autopilot2D(IMAVController control) {

		this.offboard = new OffboardManager(control);
		this.tracker  = new WayPointTracker(control);
		this.control  = control;
		this.model    = control.getCurrentModel();
		this.logger   = MSPLogger.getInstance();

		this.vfh      = new HistogramGrid2D(model.grid.getExtension(),HIS_WINDOWSIZE, model.grid.getResolution());
		this.poh      = new PolarHistogram2D(POH_ALPHA,POH_THRESHOLD,POH_DENSITY_A,POH_DENSITY_B, model.grid.getResolution());

		// Auto-Takeoff: Switch to Offboard and enable ObstacleAvoidance as soon as takeoff completed
		control.getStatusManager().addListener(StatusManager.TYPE_PX4_STATUS, Status.MSP_MODE_TAKEOFF, StatusManager.EDGE_FALLING, (o,n) -> {
			offboard.setCurrentAsTarget();
			offboard.start(OffboardManager.MODE_POSITION);
			if(!model.sys.isStatus(Status.MSP_RC_ATTACHED)) {
				model.sys.setAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_AVOIDANCE, true);
				control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
						MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
						MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_OFFBOARD, 0 );
				control.writeLogMessage(new LogMessage("[msp] Auto-takeoff completed.", MAV_SEVERITY.MAV_SEVERITY_NOTICE));
			}
		});

		control.getStatusManager().addListener(StatusManager.TYPE_MSP_AUTOPILOT, MSP_AUTOCONTROL_MODE.STEP_MODE,(o,n) -> {
			offboard.setStepMode(n.isAutopilotMode(MSP_AUTOCONTROL_MODE.STEP_MODE));
		});

		control.getStatusManager().addListener(StatusManager.TYPE_PX4_STATUS, Status.MSP_MODE_RTL, StatusManager.EDGE_RISING, (o,n) -> {
			offboard.stop();
		});

		// Stop offboard updater as soon as landed
		control.getStatusManager().addListener(StatusManager.TYPE_PX4_STATUS, Status.MSP_LANDED, StatusManager.EDGE_RISING, (o,n) -> {
			offboard.stop();
		});

		new Thread(this).start();

	}

	@Override
	public void run() {

		float nearestTarget = 0;

		msg_msp_micro_grid grid = new msg_msp_micro_grid(2,1);

		while(true) {
			try { Thread.sleep(50); } catch(Exception s) { }

			vfh.transferGridToModel(model, 4, false);
			poh.histUpdate(vfh.getMovingWindow(model.state.l_x, model.state.l_y));
			poh.histSmooth(POH_SMOOTHING);

			nearestTarget = vfh.nearestDistance(model.state.l_y, model.state.l_x);
			//			if(nearestTarget < OBSTACLE_FAILDISTANCE) {
			//				logger.writeLocalMsg("[msp] Obstacle too close.",MAV_SEVERITY.MAV_SEVERITY_CRITICAL);
			//			}

			if(nearestTarget < OBSTACLE_MINDISTANCE) {
				if(!isAvoiding) {
					isAvoiding = true;
					if(model.sys.isAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_AVOIDANCE))
						obstacleAvoidance(vfh, poh,OBSTACLE_SPEEDFACTOR);
					if(model.sys.isAutopilotMode(MSP_AUTOCONTROL_MODE.JUMPBACK))
						jumpback(0.3f);
				}
			} else {
				isAvoiding = false;
			}

			grid.resolution = 0;
			grid.extension  = 0;
			grid.cx  = model.grid.getIndicatorX();
			grid.cy  = model.grid.getIndicatorY();
			grid.tms  = System.nanoTime() / 1000;
			grid.count = model.grid.count;
			if(model.grid.toArray(grid.data)) {
				control.sendMAVLinkMessage(grid);
			}
		}
	}

	public void registerTargetListener(IAutoPilotGetTarget cb) {
		this.targetListener = cb;
	}

	public HistogramGrid2D getMap2D() {
		return vfh;
	}

	public void reset(boolean grid) {
		clearAutopilotActions();
		if(grid)
			vfh.reset(model);
	}

	public void setTarget(float x, float y, float z, float yaw) {
		Vector3D_F32 target = new Vector3D_F32(x,y,z);
		offboard.setTarget(target);
		offboard.start(OffboardManager.MODE_POSITION);
	}

	private void clearAutopilotActions() {
		isAvoiding = false;
		targetListener = null;
		offboard.removeListener();
		model.sys.autopilot &= 0b11000000000000000111111111111111;
		msg_msp_micro_slam slam = new msg_msp_micro_slam(2,1);
		control.sendMAVLinkMessage(slam);
	}

	public void executeStep() {
		offboard.triggerStep();
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
		clearAutopilotActions();
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

		float angle=0;  float projected_distance = 2.5f;

		final Vector3D_F32 current    = new Vector3D_F32();
		final Vector3D_F32 delta      = new Vector3D_F32();
		final Vector3D_F32 target     = new Vector3D_F32();
		final Vector3D_F32 projected  = new Vector3D_F32();

		current.set(model.state.l_x,model.state.l_y,model.state.l_z);
		target.set(current);

		// Determine projected position via CB
		if(targetListener!=null) {
			targetListener.getTarget(projected);
		} else {
			// If no target by CB available => use last direction projection
			projected.set(current);
			angle = MSP3DUtils.angleXY(current, MSP3DUtils.convertTo3D(tracker.pollLastWaypoint().getValue()))+(float)Math.PI;

			delta.set((float)Math.sin(2*Math.PI-angle-(float)Math.PI/2f)*projected_distance,
					(float)Math.cos(2*Math.PI-angle-(float)Math.PI/2f)*projected_distance, 0);
			projected.plusIP(delta);
		}

		try {
			angle = poh.getDirection(MSP3DUtils.angleXY(projected, current)+(float)Math.PI,POH_SMAX);
		} catch(Exception e) {
			offboard.setTarget(current);
			logger.writeLocalMsg("Obstacle Avoidance: No path found", MAV_SEVERITY.MAV_SEVERITY_CRITICAL);
			return;
		}
		delta.set((float)Math.sin(2*Math.PI-angle-(float)Math.PI/2f)*speed, (float)Math.cos(2*Math.PI-angle-(float)Math.PI/2f)*speed, 0);
		target.plusIP(delta);

		offboard.setTarget(target);
		offboard.addListener((m,d) -> {
			float a  =  0;
			current.set(model.state.l_x,model.state.l_y,model.state.l_z);
			try {
				a  = poh.getDirection(MSP3DUtils.angleXY(projected, current)+(float)Math.PI, POH_SMAX);
			} catch(Exception e) {
				offboard.setTarget(current);
				return;
			}
			float nd = his.nearestDistance(model.state.l_y, model.state.l_x);
			//			System.out.print(nd+": "); poh.print(poh.hist_smoothed,(int)MSPMathUtils.fromRad(a));
			float spd = speed * nd;

			if(MSP3DUtils.distance3D(projected,current) > 0.3f ) {

				//				if(nd < OBSTACLE_MINDISTANCE ) {
				delta.set((float)Math.sin(2*Math.PI-a-(float)Math.PI/2f)*spd, (float)Math.cos(2*Math.PI-a-(float)Math.PI/2f)*spd, 0);
				target.plusIP(delta);
				offboard.setTarget(target);
				//				} else {
				//					// should continue with previous mode execution
				//					target.plusIP(delta);
				//					offboard.setTarget(target);
				//				}
			} else {
				logger.writeLocalMsg("[msp] ObstacleAvoidance finalized. Projected target reached.",MAV_SEVERITY.MAV_SEVERITY_INFO);
				spd = 0;
			}
			msg_msp_micro_slam slam = new msg_msp_micro_slam(2,1);
			slam.px = projected.getY();
			slam.py = projected.getX();
			slam.md = nd;
			slam.pd = MSP3DUtils.angleXY(projected, current);
			slam.pv = spd*15;
			control.sendMAVLinkMessage(slam);
		});

		offboard.start(OffboardManager.MODE_POSITION);
		control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
				MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
				MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_OFFBOARD, 0 );
		logger.writeLocalMsg("[msp] ObstacleAvoidance executed",MAV_SEVERITY.MAV_SEVERITY_WARNING);
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

	public void moveto(float x, float y, float z, float yaw) {
		final Vector3D_F32 target     = new Vector3D_F32(x,y,model.state.l_z);
		final Vector3D_F32 delta      = new Vector3D_F32();
		final Vector3D_F32 projected  = new Vector3D_F32();

		autopilot.registerTargetListener((n)->{
			n.set(target);
		});

		projected.set(model.state.l_x,model.state.l_y,model.state.l_z);
		float angle = MSP3DUtils.angleXY(target,projected);

		delta.set((float)Math.sin(Math.PI-angle-(float)Math.PI/2f)*0.2f,
				(float)Math.cos(Math.PI-angle-(float)Math.PI/2f)*0.2f, 0);

		projected.plusIP(delta);

		offboard.setTarget(projected);
		offboard.start(OffboardManager.MODE_POSITION);
		offboard.addListener((m,d) -> {
			projected.set(model.state.l_x,model.state.l_y,model.state.l_z);
			if(MSP3DUtils.distance3D(target,projected) >0.3f && model.sys.isAutopilotMode(MSP_AUTOCONTROL_MODE.INTERACTIVE)) {
				projected.plusIP(delta);
				offboard.setTarget(projected);
			} else {

			}
		});
	}


	///************ Experimental modes

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

}
