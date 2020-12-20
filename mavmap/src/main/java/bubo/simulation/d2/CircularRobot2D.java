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

import georegression.struct.se.Se2_F64;

/**
 * @author Peter Abeles
 */
public class CircularRobot2D {
	public Se2_F64 robotToWorld = new Se2_F64();
	public Se2_F64 sensorToRobot = new Se2_F64();
	public double radius;
	public double velocity;
	public double angularVelocity;

	public CircularRobot2D(double radius) {
		this.radius = radius;
	}

	public CircularRobot2D() {
	}

	public void set( CircularRobot2D orig ) {
		robotToWorld.set( orig.robotToWorld );
		sensorToRobot.set( orig.sensorToRobot );
		radius = orig.radius;
		velocity = orig.velocity;
		angularVelocity = orig.angularVelocity;
	}

	public Se2_F64 getRobotToWorld() {
		return robotToWorld;
	}

	public Se2_F64 getSensorToRobot() {
		return sensorToRobot;
	}

	public double getRadius() {
		return radius;
	}

	public double getVelocity() {
		return velocity;
	}

	public double getAngularVelocity() {
		return angularVelocity;
	}
}
