/*
 * Copyright (c) 2013-2014, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Project BUBO.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bubo.simulation.d3;

import bubo.desc.sensors.lrf3d.SpinningLrf2dParam;
import bubo.simulation.d3.sensors.SimulateSpinningLrf2D;
import georegression.struct.se.Se3_F64;

/**
 * Simulated robot in 3D
 *
 * @author Peter Abeles
 */
public class Simulation3D implements ControlListener3D {

	RobotInterface3D user;
	Robot3D robot;

	SimulateSpinningLrf2D sensor;
	Se3_F64 spinningToRobot;

	// frequency for how often each item is updated, in seconds
	double periodControl;
	double periodOdometry;
	double periodLidar;
	double periodSimulation;

	// simulation time in seconds
	double time;

	// time that the following was last updated, in seconds
	double lastControl;
	double lastOdometry;
	double lastLidar;

	// transforms
	Se3_F64 sensorToWorld = new Se3_F64();
	Se3_F64 newToOld = new Se3_F64();
	Se3_F64 temp = new Se3_F64();

	public Simulation3D(RobotInterface3D user, Robot3D robot) {
		this.user = user;
		this.robot = robot;
	}

	public void setSensor( SimulateSpinningLrf2D sensor ,Se3_F64 spinningToRobot) {
		this.sensor = sensor;
		this.spinningToRobot = spinningToRobot;
	}

	public void setPeriods( double simulation , double control , double odometry , double lidar ) {
		this.periodControl = control;
		this.periodOdometry = odometry;
		this.periodLidar = lidar;
		this.periodSimulation = simulation;
	}

	public void setLocation( Se3_F64 robotToWorld ) {
		robot.robotToWorld.set(robotToWorld);
	}

	/**
	 * Sets up the simulation to run
	 */
	public void initialize() {
		user.setControlListener(this);

		SpinningLrf2dParam paramLrf = sensor == null ? null : sensor.getParam();

		user.setIntrinsic(robot.sensorToRobot, spinningToRobot, paramLrf );
		this.time = 0;
		this.lastOdometry = this.lastLidar = this.lastControl = 0;
	}

	/**
	 * Performs a single simulation step
	 */
	public void doStep() {
//		moveRobot(periodSimulation);
//		handleCollisions();
//
//		time += periodSimulation;
//		long timeStamp = (long)(time*1000);
//		if( time >= lastOdometry+periodOdometry ) {
//			lastOdometry = time;
//			user.odometry(timeStamp, robot.robotToWorld);
//		}
//		if( time >= lastLidar+periodLidar ) {
//			lastLidar = time;
//			robot.sensorToRobot.concat(robot.robotToWorld, sensorToWorld);
//
//			if( sensorLrf != null ) {
//				sensorLrf.update(sensorToWorld, walls);
//				user.ladar(timeStamp, sensorLrf.getMeasurement());
//			}
//
//			if( sensorLandmarks != null ) {
//				sensorLandmarks.update(sensorToWorld,landmarks);
//				FastQueue<RangeBearingMeasurement> measurements = sensorLandmarks.getMeasurements();
//				for (int i = 0; i < measurements.size; i++) {
//					user.rangeBearing(timeStamp,measurements.get(i));
//				}
//			}
//		}
//		if( time >= lastControl+periodControl ) {
//			lastControl = time;
//			user.doControl(timeStamp);
//		}
	}

	@Override
	public void _setPose(Se3_F64 robotToWorld) {

	}

	@Override
	public Se3_F64 _truthRobotToWorld() {
		return null;
	}
}
