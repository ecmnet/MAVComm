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

package bubo.simulation.d2;

import bubo.desc.sensors.landmark.RangeBearingMeasurement;
import bubo.desc.sensors.landmark.RangeBearingParam;
import bubo.desc.sensors.lrf2d.Lrf2dParam;
import bubo.maps.d2.LandmarkMap2D;
import bubo.maps.d2.lines.LineSegmentMap;
import bubo.simulation.d2.sensors.SimulateLrf2D;
import bubo.simulation.d2.sensors.SimulateRangeBearing;
import georegression.metric.ClosestPoint2D_F64;
import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Vector2D_F64;
import georegression.struct.se.Se2_F64;
import org.ddogleg.struct.FastQueue;

/**
 * Simulation of a robot in 2D with a LIDAR sensor on it.  Primary intended to act as a simple way to generator
 * sensor observations.  Does not intend to accurately model physics, collisions, or anything remotely advanced.
 *
 * @author Peter Abeles
 */
public class Simulation2D implements ControlListener2D {

	// simulation and control models
	RobotInterface2D user;
	LineSegmentMap walls;
	LandmarkMap2D landmarks;
	CircularRobot2D robot;
	SimulateLrf2D sensorLrf;
	SimulateRangeBearing sensorLandmarks;

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
	Se2_F64 sensorToWorld = new Se2_F64();
	Se2_F64 newToOld = new Se2_F64();
	Se2_F64 temp = new Se2_F64();

	public Simulation2D(RobotInterface2D user,
						CircularRobot2D robot ) {
		this.user = user;
		this.robot = robot;

	}

	public void setWalls(LineSegmentMap walls) {
		this.walls = walls;
	}

	public void setLaserRangeFinder( Lrf2dParam sensorLrfParam ) {
		sensorLrf = new SimulateLrf2D(sensorLrfParam);
	}

	public void setLandmarks(LandmarkMap2D landmarks, RangeBearingParam param ) {
		this.landmarks = landmarks;
		this.sensorLandmarks = new SimulateRangeBearing(param);
	}

	public void setPeriods( double simulation , double control , double odometry , double lidar ) {
		this.periodControl = control;
		this.periodOdometry = odometry;
		this.periodLidar = lidar;
		this.periodSimulation = simulation;
	}

	public void setLocation( double x , double y , double theta ) {
		robot.robotToWorld.set(x,y,theta);
	}

	/**
	 * Sets up the simulation to run
	 */
	public void initialize() {
		user.setControlListener(this);

		Lrf2dParam paramLrf = sensorLrf == null ? null : sensorLrf.getParam();
		RangeBearingParam paramRb = sensorLandmarks == null ? null : sensorLandmarks.getParam();

		user.setIntrinsic(robot.sensorToRobot, paramLrf , paramRb );
		this.time = 0;
		this.lastOdometry = this.lastLidar = this.lastControl = 0;
	}

	/**
	 * Performs a single simulation step
	 */
	public void doStep() {
		moveRobot(periodSimulation);
		handleCollisions();

		time += periodSimulation;
		long timeStamp = (long)(time*1000);
		if( time >= lastOdometry+periodOdometry ) {
			lastOdometry = time;
			user.odometry(timeStamp, robot.robotToWorld);
		}
		if( time >= lastLidar+periodLidar ) {
			lastLidar = time;
			robot.sensorToRobot.concat(robot.robotToWorld, sensorToWorld);

			if( sensorLrf != null ) {
				sensorLrf.update(sensorToWorld, walls);
				user.ladar(timeStamp, sensorLrf.getMeasurement());
			}

			if( sensorLandmarks != null ) {
				sensorLandmarks.update(sensorToWorld,landmarks);
				FastQueue<RangeBearingMeasurement> measurements = sensorLandmarks.getMeasurements();
				for (int i = 0; i < measurements.size; i++) {
					user.rangeBearing(timeStamp,measurements.get(i));
				}
			}
		}
		if( time >= lastControl+periodControl ) {
			lastControl = time;
			user.doControl(timeStamp);
		}
	}

	/**
	 * Moves the robot based on its velocity
	 * @param T integration time
	 */
	protected void moveRobot( double T ) {

		// This motion model is referred to as the "velocity" model in Probabilistic Robotics
		if( robot.angularVelocity == 0.0 ) {
			newToOld.set(robot.velocity*T,0,0);
		} else {
			// absolute value of v/w is the radius
			double r = robot.velocity/robot.angularVelocity;

			// derived using the center of the circle it's moving along and location
			// on the circle
			double dtheta = robot.angularVelocity*T;
			double dx = r*Math.sin(dtheta);
			double dy = r*(1-Math.cos(dtheta));

			newToOld.set(dx,dy,dtheta);
		}

		// add it to the previous pose and update
		newToOld.concat(robot.robotToWorld,temp);
		robot.robotToWorld.set(temp);
	}

	/**
	 * Sees if the robot is hitting any line.  If it does the robot is moved away from the line a little bit away
	 * from the wall.
	 */
	protected void handleCollisions() {
		if( walls == null )
			return;

		Vector2D_F64 T = robot.robotToWorld.getTranslation();
		Point2D_F64 p = new Point2D_F64(T.x,T.y);
		Point2D_F64 c = new Point2D_F64();
		int numCycles;
		for( numCycles = 0; numCycles < 10; numCycles++ ) {
			boolean collision = false;
			for (int i = 0; i < walls.lines.size(); i++) {
				LineSegment2D_F64 line = walls.lines.get(i);

				ClosestPoint2D_F64.closestPoint(line,p,c);
				double d = p.distance2(c);
				if (d < robot.radius*robot.radius ) {
					// Collision!  Move the robot away from the wall
					collision = true;
					double slopeX = p.x - c.x;
					double slopeY = p.y - c.y;

					d = Math.sqrt(d);
					// move it a little bit extra so that it isn't a glancing blow
					p.x += slopeX*(d*1.0001)/d;
					p.y += slopeY*(d*1.0001)/d;
				}
			}
			if( !collision )
				break;
		}
		if( numCycles == 10 ) {
			throw new RuntimeException("Oh crap.  Cant move without hitting something");
		}
		robot.robotToWorld.T.set(p.x,p.y);
	}

	@Override
	public void sendControl(double velocity, double anglularVelocity) {
		robot.angularVelocity = anglularVelocity;
		robot.velocity = velocity;
	}

	@Override
	public void _setPose(Se2_F64 robotToWorld) {
		robot.getRobotToWorld().set(robotToWorld);
	}

	@Override
	public Se2_F64 _truthRobotToWorld() {
		return robot.robotToWorld;
	}

	public Se2_F64 getSensorToRobot() {
		return robot.sensorToRobot;
	}

	public double getTime() {
		return time;
	}

	public CircularRobot2D getRobot() {
		return robot;
	}

	public SimulateLrf2D getSimulatedLadar() {
		return sensorLrf;
	}

	public SimulateRangeBearing getSensorLandmarks() {
		return sensorLandmarks;
	}

	public double getPeriodControl() {
		return periodControl;
	}

	public double getPeriodOdometry() {
		return periodOdometry;
	}

	public double getPeriodLidar() {
		return periodLidar;
	}

	public double getPeriodSimulation() {
		return periodSimulation;
	}
}
