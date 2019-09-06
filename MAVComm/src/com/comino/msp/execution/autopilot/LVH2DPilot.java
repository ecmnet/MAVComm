package com.comino.msp.execution.autopilot;

import org.mavlink.messages.MAV_CMD;
import org.mavlink.messages.MAV_MODE_FLAG;
import org.mavlink.messages.MAV_SEVERITY;
import org.mavlink.messages.MSP_AUTOCONTROL_ACTION;
import org.mavlink.messages.MSP_AUTOCONTROL_MODE;
import org.mavlink.messages.lquac.msg_msp_micro_slam;

import com.comino.libs.TrajMathLib;
import com.comino.main.MSPConfig;
import com.comino.mav.control.IMAVController;
import com.comino.mav.mavlink.MAV_CUST_MODE;
import com.comino.msp.execution.offboard.IOffboardTargetAction;
import com.comino.msp.execution.offboard.OffboardManager;
import com.comino.msp.model.segment.Status;
import com.comino.msp.slam.map2D.filter.impl.DenoiseMapFilter;
import com.comino.msp.slam.vfh.LocalVFH2D;
import com.comino.msp.utils.MSP3DUtils;
import com.comino.msp.utils.struct.Polar3D_F32;

import georegression.struct.point.Vector3D_F32;
import georegression.struct.point.Vector4D_F32;

public class LVH2DPilot extends AutoPilotBase {

	private static final int              CYCLE_MS	        = 50;
	private static final float            ROBOT_RADIUS      = 0.25f;

	private static final float OBSTACLE_MINDISTANCE_0MS  	= 0.3f;
	private static final float OBSTACLE_MINDISTANCE_1MS  	= 1.0f;

	private static final float OBSTACLE_FAILDISTANCE     	= OBSTACLE_MINDISTANCE_1MS;
	private static final float OBSTACLE_FAILDISTANCE_2     	= OBSTACLE_MINDISTANCE_1MS / 2f;

	private LocalVFH2D              lvfh      = null;

	private boolean            	isAvoiding    = false;

	private ILVH2DPilotGetTarget targetListener = null;

	private final Vector4D_F32 projected  = new Vector4D_F32();
	private Polar3D_F32         obstacle  = new Polar3D_F32();


	protected LVH2DPilot(IMAVController control, MSPConfig config) {
		super(control,config);

		this.lvfh     = new LocalVFH2D(map,ROBOT_RADIUS, CERTAINITY_THRESHOLD);

		start();
	}

	public void run() {

		Vector3D_F32 current = new Vector3D_F32(); boolean tooClose = false; float min_distance;
		msg_msp_micro_slam slam = new msg_msp_micro_slam(2,1);

		while(isRunning) {

			try { Thread.sleep(CYCLE_MS); } catch(Exception s) { }

			//System.out.println(model.sys.getSensorString());

			current.set(model.state.l_x, model.state.l_y,model.state.l_z);
			lvfh.update_histogram(current);

			map.nearestObstacle(obstacle);
			if(obstacle.value < OBSTACLE_FAILDISTANCE_2  && !tooClose ) {
				logger.writeLocalMsg("[msp] Collision warning.",MAV_SEVERITY.MAV_SEVERITY_WARNING);
				tooClose = true;
			}

			if(obstacle.value > OBSTACLE_FAILDISTANCE+ROBOT_RADIUS) {
				tooClose = false;
			}


			min_distance = TrajMathLib.getAvoidanceDistance(model.state.getXYSpeed(),OBSTACLE_MINDISTANCE_0MS,OBSTACLE_MINDISTANCE_1MS );

			if(obstacle.value < min_distance && !isAvoiding) {
				isAvoiding = true;
				if(model.sys.isAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_AVOIDANCE))
					obstacleAvoidance();
				if(model.sys.isAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_STOP))
					stop_at_position();
			}

			if(obstacle.value > (min_distance + ROBOT_RADIUS) && isAvoiding) {
				if(model.sys.isAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_AVOIDANCE)) {
					offboard.removeExternalControlListener();
					isAvoiding = false;
					logger.writeLocalMsg("[msp] Avoidance cleared.",MAV_SEVERITY.MAV_SEVERITY_INFO);
				}
			}

			// Publish SLAM data to GC
			slam.px = model.slam.px;
			slam.py = model.slam.py;
			slam.pz = model.slam.pz;
			slam.pd = model.slam.pd;
			slam.pv = model.slam.pv;
			slam.md = model.slam.di;


			if(obstacle.value < OBSTACLE_FAILDISTANCE) {

				slam.ox = obstacle.getX()+model.state.l_x;
				slam.oy = obstacle.getY()+model.state.l_y;
				slam.oz = obstacle.getZ()+model.state.l_z;
				if(control.isSimulation())
					model.slam.dm = obstacle.value;

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
		super.setTarget(x,y,z,yaw);
		isAvoiding = false;
	}

//	@Override
//	public void setMode(boolean enable, int mode, float param) {
//		switch(mode) {
//		case MSP_AUTOCONTROL_ACTION.RTL:
//			if(enable)
//				returnToLand();
//			break;
////		case MSP_AUTOCONTROL_ACTION.WAYPOINT_MODE:
////			return_along_path(enable);
////			break;
//		}
//		super.setMode(enable, mode, param);
//	}

//	public void returnToLand(int delay_ms) {
//
//		final Vector4D_F32 target = new Vector4D_F32(0,0,model.state.l_z,0);
//		logger.writeLocalMsg("[msp] Autopilot: Return to land.",MAV_SEVERITY.MAV_SEVERITY_INFO);
//
//		model.sys.setAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_AVOIDANCE, true);
//		isAvoiding = false;
//
//		offboard.registerActionListener((m,d) -> {
//			logger.writeLocalMsg("[msp] Autopilot: Home reached.",MAV_SEVERITY.MAV_SEVERITY_INFO);
//			control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_NAV_LAND, 5, 0, 0, 0.05f );
//		    offboard.finalize();
//		});
//
//		offboard.setTarget(target);
//		offboard.start(OffboardManager.MODE_SPEED_POSITION);
//	}

	public void stop_at_position() {
		offboard.setCurrentAsTarget();
		offboard.start(OffboardManager.MODE_LOITER);
		logger.writeLocalMsg("[msp] Autopilot: Emergency breaking",MAV_SEVERITY.MAV_SEVERITY_WARNING);
	}

	public void moveto(float x, float y, float z, float yaw) {
		final Vector4D_F32 target = new Vector4D_F32(x,y,z,yaw);
		this.registerTargetListener((n)->{ n.set(target); });
		super.moveto(x, y, z, yaw);
	}


	public void obstacleAvoidance() {


	//	System.out.println("NT="+nearestTarget);

		lvfh.init(model.state.getXYSpeed());

		// Determine projected position via CB
		if(targetListener!=null) {
			targetListener.getTarget(projected);
		} else
			return;

		offboard.setTarget(projected);

		// determine velocity setpoint via callback
		offboard.registerExternalControlListener((tms, current, path, ctl) -> {

			if(!model.sys.isAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_AVOIDANCE)) {
				offboard.setCurrentAsTarget();
			}

			try {
				lvfh.select(path.angle_xy+(float)Math.PI, current.value , path.value * 1000f);
				ctl.angle_xy =  lvfh.getSelectedDirection()-(float)Math.PI;
				ctl.value = lvfh.getSelectedSpeed();

			} catch(Exception e) {
				logger.writeLocalMsg("Obstacle Avoidance: No path found", MAV_SEVERITY.MAV_SEVERITY_WARNING);
				offboard.setCurrentAsTarget();
			}
		});

		offboard.start(OffboardManager.MODE_SPEED_POSITION);
		if(!model.sys.isStatus(Status.MSP_LANDED)) {
			control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
					MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
					MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_OFFBOARD, 0 );
		}
		logger.writeLocalMsg("[msp] ObstacleAvoidance executed",MAV_SEVERITY.MAV_SEVERITY_WARNING);
	}


}
