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

import georegression.struct.se.Se3_F64;

/**
 * @author Peter Abeles
 */
public class Robot3D {
	Se3_F64 robotToWorld = new Se3_F64();
	Se3_F64 sensorToRobot = new Se3_F64();

	public Se3_F64 getRobotToWorld() {
		return robotToWorld;
	}

	public Se3_F64 getSensorToRobot() {
		return sensorToRobot;
	}

	public void setTo( Robot3D robot ) {
		this.robotToWorld.set(robot.getRobotToWorld());
		this.sensorToRobot.set(robot.getSensorToRobot());
	}
}
