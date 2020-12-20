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
 * Interface for sending controls to simulation and cheating by getting truth/setting the pose
 *
 * @author Peter Abeles
 */
public interface ControlListener2D {
	/**
	 * Sends a control request for the robot to travel at the specified speed
	 * @param velocity translation velocity
	 * @param anglularVelocity angular velocity
	 */
	public void sendControl( double velocity , double anglularVelocity );

	/**
	 * Way to cheat and make the robot magically teleport to the specified location
	 * @param robotToWorld The new robot pose
	 */
	public void _setPose(Se2_F64 robotToWorld);

	/**
	 * Way to cheat and get the robot's true location in the world
	 * @return true pose
	 */
	public Se2_F64 _truthRobotToWorld();
}
