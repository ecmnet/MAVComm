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
import com.comino.msp.utils.MSPMathUtils;
import com.comino.msp.utils.struct.Polar3D_F32;

import georegression.struct.point.Vector3D_F32;
import georegression.struct.point.Vector4D_F32;

public class BreakingPilot extends AutoPilotBase {

	private static final int              CYCLE_MS	        = 50;
	private static final float            ROBOT_RADIUS      = 0.25f;

	private static final float OBSTACLE_MINDISTANCE_0MS  	= 0.5f;
	private static final float OBSTACLE_MINDISTANCE_1MS  	= 1.5f;

	private float 				speedStep     = 0;
	private boolean             tooClose      = false;
	private boolean             isStopped     = false;

	final private Polar3D_F32   obstacle      = new Polar3D_F32();
	final private Polar3D_F32   plannedPath   = new Polar3D_F32();


	protected BreakingPilot(IMAVController control, MSPConfig config) {
		super(control,config);

		obstacle.value = Float.POSITIVE_INFINITY;

		// calculate speed constraints considering the distance to obstacles
		offboard.registerExternalConstraintsListener((tms, current, way, path, ctl) -> {

			plannedPath.set(path);

			if(model.sys.isAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_STOP)) {
				if(Float.isInfinite(obstacle.value)) {
					speedStep = 0;
					return;
				}

				if(speedStep == 0) {
					speedStep = 1.4f / ( obstacle.value );
				}

				ctl.value = Math.min(ctl.value,  speedStep * ( obstacle.value ) );
				if(ctl.value < 0.3) ctl.value = 0.3f;

			}
		});

		start();
	}

	public void run() {

		msg_msp_micro_slam slam = new msg_msp_micro_slam(2,1);
		float relAngle = 0; final float pi4 = (float)Math.PI/4;

		while(isRunning) {

			try { Thread.sleep(CYCLE_MS); } catch(Exception s) { }

			map.processWindow(model.state.l_x, model.state.l_y);
	        map.nearestObstacle(obstacle);

			relAngle = MSPMathUtils.normAngleDiff(obstacle.angle_xy, plannedPath.angle_xy);

//			System.out.println("PATH: "+MSPMathUtils.fromRad(plannedPath.angle_xy)+"°  OBSTACLE:"+MSPMathUtils.fromRad(obstacle.angle_xy)+"° Difference: "+
//					MSPMathUtils.fromRad(relAngle)+"°  rel.Distance: "+obstacle.value);

			if(obstacle.value < OBSTACLE_MINDISTANCE_1MS  && !tooClose && relAngle < pi4) {
				tooClose = true;
				logger.writeLocalMsg("[msp] Collision warning. Breaking.",MAV_SEVERITY.MAV_SEVERITY_WARNING);
			}


			if(obstacle.value < OBSTACLE_MINDISTANCE_0MS && !isStopped && relAngle < pi4) {
				if(model.sys.isAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_STOP))
					stop_at_position();
				isStopped = true;
			}


			if(obstacle.value > OBSTACLE_MINDISTANCE_1MS+ROBOT_RADIUS || relAngle > pi4) {
				tooClose = false;
			}


			// Publish SLAM data to GC
			slam.px = model.slam.px;
			slam.py = model.slam.py;
			slam.pz = model.slam.pz;
			slam.pd = model.slam.pd;
			slam.pv = model.slam.pv;
			slam.md = model.slam.di;


			if(tooClose) {

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

	protected void takeoffCompleted() {
		model.sys.setAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_STOP,true);
	}




	@Override
	public void moveto(float x, float y, float z, float yaw) {
		isStopped = false;
		super.moveto(x, y, z, yaw);
	}

	public void stop_at_position() {
		offboard.setCurrentAsTarget();
		offboard.start(OffboardManager.MODE_LOITER);
		logger.writeLocalMsg("[msp] Autopilot: Emergency breaking",MAV_SEVERITY.MAV_SEVERITY_EMERGENCY);
	}





}
