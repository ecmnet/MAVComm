package com.comino.msp.execution.autopilot;

import org.mavlink.messages.MAV_CMD;
import org.mavlink.messages.MAV_MODE_FLAG;
import org.mavlink.messages.MAV_SEVERITY;
import org.mavlink.messages.MSP_AUTOCONTROL_ACTION;
import org.mavlink.messages.MSP_AUTOCONTROL_MODE;
import org.mavlink.messages.lquac.msg_msp_micro_slam;

import com.comino.main.MSPConfig;
import com.comino.mav.control.IMAVController;
import com.comino.mav.mavlink.MAV_CUST_MODE;
import com.comino.msp.execution.control.StatusManager;
import com.comino.msp.execution.offboard.IOffboardExternalControl;
import com.comino.msp.execution.offboard.IOffboardTargetAction;
import com.comino.msp.execution.offboard.OffboardManager;
import com.comino.msp.model.segment.LogMessage;
import com.comino.msp.model.segment.Status;
import com.comino.msp.slam.map2D.filter.impl.DenoiseMapFilter;
import com.comino.msp.slam.vfh.LocalVFH2D;
import com.comino.msp.utils.MSP3DUtils;

import georegression.struct.point.Vector3D_F32;
import georegression.struct.point.Vector4D_F32;

public class LVH2DPilot extends AutoPilotBase {

	private static final int              CYCLE_MS	        = 50;
	private static final float            ROBOT_RADIUS      = 0.25f;

	private static final float OBSTACLE_MINDISTANCE_0MS  	= 0.5f;
	private static final float OBSTACLE_MINDISTANCE_1MS  	= 1.5f;

	private static final float OBSTACLE_FAILDISTANCE     	= OBSTACLE_MINDISTANCE_1MS;
	private static final float OBSTACLE_FAILDISTANCE_2     	= OBSTACLE_MINDISTANCE_1MS / 2f;

	private LocalVFH2D              lvfh      = null;

	private boolean            	isAvoiding    = false;
	private float             	nearestTarget = 0;

	private ILVH2DPilotGetTarget targetListener = null;


	protected LVH2DPilot(IMAVController control, MSPConfig config) {
		super(control,config);

		this.lvfh     = new LocalVFH2D(map,ROBOT_RADIUS, CERTAINITY_THRESHOLD);

		if(mapForget)
			registerMapFilter(new DenoiseMapFilter(800,800));

		// Auto-Takeoff: Switch to Offboard and enable ObstacleAvoidance as soon as takeoff completed
		//
		// TODO: Landing during takeoff switches to offboard mode here => should directly land instead
		//       Update: 		works in SITL, but not on vehicle (timing?)
		control.getStatusManager().addListener(StatusManager.TYPE_PX4_NAVSTATE, Status.NAVIGATION_STATE_AUTO_TAKEOFF, StatusManager.EDGE_FALLING, (o,n) -> {
			if(n.nav_state == Status.NAVIGATION_STATE_AUTO_LAND)
				return;
			offboard.setCurrentSetPointAsTarget();
			offboard.start(OffboardManager.MODE_LOITER);
				control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
						MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
						MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_OFFBOARD, 0 );
				control.writeLogMessage(new LogMessage("[msp] Auto-takeoff completed.", MAV_SEVERITY.MAV_SEVERITY_NOTICE));

		});

		// Stop offboard updater as soon as landed
		control.getStatusManager().addListener(StatusManager.TYPE_PX4_STATUS, Status.MSP_LANDED, StatusManager.EDGE_RISING, (o,n) -> {
			offboard.stop();
		});

		start();
	}

	public void run() {

		Vector3D_F32 current = new Vector3D_F32(); boolean tooClose = false; float min_distance;
		msg_msp_micro_slam slam = new msg_msp_micro_slam(2,1);

		while(isRunning) {

			try { Thread.sleep(CYCLE_MS); } catch(Exception s) { }

			//System.out.println(model.sys.getSensorString());

			current.set(model.state.l_x, model.state.l_y,model.state.l_z);
			lvfh.update_histogram(current, model.hud.s);

			nearestTarget = map.nearestDistance(model.state.l_x, model.state.l_y);
			if(nearestTarget < OBSTACLE_FAILDISTANCE_2  && !tooClose ) {
				logger.writeLocalMsg("[msp] Collision warning.",MAV_SEVERITY.MAV_SEVERITY_CRITICAL);
				tooClose = true;
			}

			if(nearestTarget > OBSTACLE_FAILDISTANCE+ROBOT_RADIUS) {
				tooClose = false;
			}

			min_distance = getAvoidanceDistance(model.hud.s);

			if(nearestTarget < min_distance && !isAvoiding) {
				isAvoiding = true;
				if(model.sys.isAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_AVOIDANCE))
					obstacleAvoidance();
				if(model.sys.isAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_STOP))
					stop_at_position();
			}

			if(nearestTarget > (min_distance + ROBOT_RADIUS) && isAvoiding) {
				offboard.removeExternalControlListener();
				if(model.sys.isAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_AVOIDANCE))
					isAvoiding = false;
			}

			// Publish SLAM data to GC
			slam.px = model.slam.px;
			slam.py = model.slam.py;
			slam.pz = model.slam.pz;
			slam.pd = model.slam.pd;
			slam.pv = model.slam.pv;
			slam.md = model.slam.di;


			if(nearestTarget < OBSTACLE_FAILDISTANCE) {

				slam.ox = (float)map.getNearestObstaclePosition().x;
				slam.oy = (float)map.getNearestObstaclePosition().y;
				slam.oz = (float)map.getNearestObstaclePosition().z;
				if(control.isSimulation())
					model.slam.dm = nearestTarget;

			} else {

				slam.ox = 0; slam.oy = 0; slam.oz = 0;
				if(control.isSimulation())
					model.slam.dm = Float.NaN;
			}

			slam.dm = model.slam.dm;
			slam.tms = model.sys.getSynchronizedPX4Time_us();
			control.sendMAVLinkMessage(slam);

			if(mapForget && mapFilter != null)
				map.applyMapFilter(mapFilter);


		}
	}

	public void registerTargetListener(ILVH2DPilotGetTarget cb) {
		isAvoiding = false;
		this.targetListener = cb;
	}

	public void setTarget(float x, float y, float z, float yaw) {
		isAvoiding = false;
		Vector4D_F32 target = new Vector4D_F32(x,y,z,yaw);
		offboard.setTarget(target);
		offboard.start(OffboardManager.MODE_POSITION);
	}

	private void clearAutopilotActions() {
		isAvoiding = false;
		//		targetListener = null;
		model.sys.autopilot &= 0b11000000000000000111111111111111;
		offboard.removeActionListener();
		control.sendMAVLinkMessage(new msg_msp_micro_slam(2,1));
	}

	public void offboardPosHold(boolean enable) {
		if(enable) {
			offboard.setCurrentAsTarget();
			offboard.start(OffboardManager.MODE_POSITION);
			if(!model.sys.isStatus(Status.MSP_LANDED) && !model.sys.isStatus(Status.MSP_RC_ATTACHED)) {
				control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
						MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
						MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_OFFBOARD, 0 );
			}
		} else {
			if(model.sys.nav_state==Status.NAVIGATION_STATE_OFFBOARD) {
				model.sys.autopilot = 0;
				control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
						MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
						MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_POSCTL, 0 );
			}
			offboard.stop();
		}
	}



	@Override
	public void setMode(int mode, float param, boolean enable) {
		switch(mode) {
		case MSP_AUTOCONTROL_MODE.ABORT:
			abort();
			break;
		case MSP_AUTOCONTROL_ACTION.RTL:
			if(enable)
				returnToLand(3000);
			break;
//		case MSP_AUTOCONTROL_ACTION.WAYPOINT_MODE:
//			return_along_path(enable);
//			break;
		case MSP_AUTOCONTROL_ACTION.SAVE_MAP2D:
			saveMap2D();
			break;
		case MSP_AUTOCONTROL_ACTION.LOAD_MAP2D:
			loadMap2D();
			break;
		case MSP_AUTOCONTROL_ACTION.AUTO_MISSION:
			break;
		case MSP_AUTOCONTROL_ACTION.DEBUG_MODE1:
			setXObstacleForSITL();
			//	autopilot.applyMapFilter();
			break;
		case MSP_AUTOCONTROL_ACTION.DEBUG_MODE2:
			//    autopilot.square(3);
			setYObstacleForSITL();
			//  autopilot.landLocal();
			break;
		case MSP_AUTOCONTROL_ACTION.OFFBOARD_UPDATER:
			offboardPosHold(enable);
			break;
//		case MSP_AUTOCONTROL_ACTION.APPLY_MAP_FILTER:
//			applyMapFilter();
//			break;
		}
		super.setMode(mode, param, enable);
	}

	public void abort() {
		clearAutopilotActions();
		model.sys.autopilot &= 0b11000000000000000000000000000001;
		if(model.sys.isStatus(Status.MSP_RC_ATTACHED)) {
			control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
					MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
					MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_POSCTL, 0 );
			offboard.stop();
		} else {
			offboard.setCurrentAsTarget();
			offboard.start(OffboardManager.MODE_POSITION);
		}
	}

	public void returnToLand(int delay_ms) {

		final Vector4D_F32 target = new Vector4D_F32(0,0,model.state.l_z,0);
		logger.writeLocalMsg("[msp] Autopilot: Return to land.",MAV_SEVERITY.MAV_SEVERITY_INFO);

		model.sys.setAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_AVOIDANCE, true);
		isAvoiding = false;

		offboard.registerActionListener((m,d) -> {
			offboard.finalize();
			logger.writeLocalMsg("[msp] Autopilot: Home reached.",MAV_SEVERITY.MAV_SEVERITY_INFO);
			control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_NAV_LAND, 5, 0, 0, 0.05f );
			control.sendMAVLinkMessage(new msg_msp_micro_slam(2,1));
		});

		offboard.setTarget(target);
		offboard.start(OffboardManager.MODE_SPEED_POSITION);
	}

	public void stop_at_position() {
		offboard.setCurrentAsTarget();
		offboard.start(OffboardManager.MODE_POSITION);
	}

	public void execute(Vector4D_F32 target,IOffboardTargetAction action) {
		this.registerTargetListener((n)->{ n.set(target); });
		offboard.registerActionListener(action);
		offboard.setTarget(target);
		offboard.start(OffboardManager.MODE_SPEED_POSITION);
	}

	public void moveto(float x, float y, float z, float yaw) {
        final Vector4D_F32 target = new Vector4D_F32(x,y,z,yaw);
		if(flowCheck && !model.sys.isSensorAvailable(Status.MSP_PIX4FLOW_AVAILABILITY)) {
			logger.writeLocalMsg("[msp] Autopilot: Aborting. No Flow available.",MAV_SEVERITY.MAV_SEVERITY_WARNING);
			return;
		}

		execute(target, (m,d) -> {
			offboard.finalize();
			logger.writeLocalMsg("[msp] Autopilot: Target reached.",MAV_SEVERITY.MAV_SEVERITY_INFO);
			control.sendMAVLinkMessage(new msg_msp_micro_slam(2,1));
		});
	}


	public void obstacleAvoidance() {


		final Vector4D_F32 current    = new Vector4D_F32();
		final Vector4D_F32 projected  = new Vector4D_F32();

		float[] ctl = new float[2];

		final msg_msp_micro_slam slam = new msg_msp_micro_slam(2,1);

		current.set(model.state.l_x,model.state.l_y,model.state.l_z,Float.NaN);

		lvfh.init(model.hud.s);

		// Determine projected position via CB
		if(targetListener!=null) {
			targetListener.getTarget(projected);
		} else
			return;

		offboard.setTarget(projected);

		// determine velocity setpoint via callback
		offboard.registerExternalControlListener((speed, target_dir, distance) -> {

			current.set(model.state.l_x,model.state.l_y,model.state.l_z,Float.NaN);

			if(!model.sys.isAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_AVOIDANCE)) {
				offboard.setTarget(current);
			}

			try {
				lvfh.select(MSP3DUtils.angleXY(projected, current)+(float)Math.PI, speed, distance*1000f);
				ctl[IOffboardExternalControl.ANGLE] = (float)(2* Math.PI) - lvfh.getSelectedDirection() - (float)Math.PI/2f;
				ctl[IOffboardExternalControl.SPEED] = lvfh.getSelectedSpeed();

			} catch(Exception e) {
				logger.writeLocalMsg("Obstacle Avoidance: No path found", MAV_SEVERITY.MAV_SEVERITY_WARNING);
				offboard.setTarget(current);
			}
			return ctl;
		});

		offboard.start(OffboardManager.MODE_SPEED_POSITION);
		if(!model.sys.isStatus(Status.MSP_LANDED)) {
			control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
					MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
					MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_OFFBOARD, 0 );
		}
		logger.writeLocalMsg("[msp] ObstacleAvoidance executed",MAV_SEVERITY.MAV_SEVERITY_WARNING);
	}

	private float getAvoidanceDistance( float speed ) {
		float val = OBSTACLE_MINDISTANCE_0MS + (speed*( OBSTACLE_MINDISTANCE_1MS-OBSTACLE_MINDISTANCE_0MS ));
		if ( val < 0 )
			val = 0;
		return val;
	}


}
