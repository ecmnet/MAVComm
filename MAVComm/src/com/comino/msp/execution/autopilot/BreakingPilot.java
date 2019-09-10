package com.comino.msp.execution.autopilot;

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

import org.mavlink.messages.MAV_SEVERITY;
import org.mavlink.messages.MSP_AUTOCONTROL_MODE;
import org.mavlink.messages.lquac.msg_msp_micro_slam;

import com.comino.libs.TrajMathLib;
import com.comino.main.MSPConfig;
import com.comino.mav.control.IMAVController;
import com.comino.msp.execution.offboard.OffboardManager;
import com.comino.msp.utils.MSPMathUtils;
import com.comino.msp.utils.struct.Polar3D_F32;

public class BreakingPilot extends AutoPilotBase {

	private static final int              CYCLE_MS	        = 50;
	private static final float            ROBOT_RADIUS      = 0.25f;

	private static final float OBSTACLE_MINDISTANCE_0MS  	= 0.5f;
	private static final float OBSTACLE_MINDISTANCE_1MS  	= 1.5f;
	private static final float MIN_BREAKING_SPEED           = 0.2f;
	private static final float BREAKING_ACCELERATION       	= 3f;

	private boolean             tooClose      = false;
	private boolean             isStopped     = false;

	private float				breakingAcceleration = 0;

	final private Polar3D_F32   obstacle      = new Polar3D_F32();
	final private Polar3D_F32   plannedPath   = new Polar3D_F32();


	protected BreakingPilot(IMAVController control, MSPConfig config) {
		super(control,config);

		obstacle.value = Float.POSITIVE_INFINITY;

		// calculate speed constraints considering the distance to obstacles
		offboard.registerExternalConstraintsListener((delta_sec, speed, path, ctl) -> {

			plannedPath.set(path);

			if(model.sys.isAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_STOP)) {

				if(Float.isInfinite(obstacle.value) || ctl.value < MIN_BREAKING_SPEED ) {
					return;
				}

//				if( ctl.value > ctl.value * ( obstacle.value - OBSTACLE_MINDISTANCE_0MS ) / OBSTACLE_MINDISTANCE_1MS + MIN_BREAKING_SPEED) {
//					ctl.value = ctl.value - BREAKING_ACCELERATION * delta_sec;
//				}

				breakingAcceleration = BREAKING_ACCELERATION * ( obstacle.value - OBSTACLE_MINDISTANCE_0MS ) / OBSTACLE_MINDISTANCE_1MS;


				if(obstacle.value < TrajMathLib.getAvoidanceDistance(ctl.value, OBSTACLE_MINDISTANCE_0MS, OBSTACLE_MINDISTANCE_1MS))
				   ctl.value = Math.min(ctl.value, ctl.value - breakingAcceleration * delta_sec);


				if(ctl.value < MIN_BREAKING_SPEED || ( tooClose && isStopped)) ctl.value = MIN_BREAKING_SPEED;

			}
		});

		start();
	}

	public void run() {

		msg_msp_micro_slam slam = new msg_msp_micro_slam(2,1);
		float relAngle = 0; final float maxAngle = (float)Math.PI/2;

		while(isRunning) {

			try { Thread.sleep(CYCLE_MS); } catch(Exception s) { }

			map.processWindow(model.state.l_x, model.state.l_y);
	        map.nearestObstacle(obstacle);

			relAngle = MSPMathUtils.normAngleDiff(obstacle.angle_xy, plannedPath.angle_xy);

//			System.out.println("PATH: "+MSPMathUtils.fromRad(plannedPath.angle_xy)+"°  OBSTACLE:"+MSPMathUtils.fromRad(obstacle.angle_xy)+"° Difference: "+
//					MSPMathUtils.fromRad(relAngle)+"°  rel.Distance: "+obstacle.value);

			if(obstacle.value < OBSTACLE_MINDISTANCE_1MS  && !tooClose && relAngle < maxAngle) {
				tooClose = true;
				logger.writeLocalMsg("[msp] Collision warning. Breaking.",MAV_SEVERITY.MAV_SEVERITY_WARNING);
			}


			if(obstacle.value < OBSTACLE_MINDISTANCE_0MS && !isStopped && relAngle < maxAngle) {
				if(model.sys.isAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_STOP))
					stop_at_position();
				isStopped = true;
			}


			if(obstacle.value > OBSTACLE_MINDISTANCE_1MS+ROBOT_RADIUS || relAngle > maxAngle) {
				tooClose = false;
				isStopped = false;
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
		super.takeoffCompleted();
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
