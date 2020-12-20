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

package bubo.desc;

import java.util.List;

/**
 * A description of the robot and its current state.  Components are typically physically separate components that
 * are attached to the robot.  For example the wheels
 *
 * @author Peter Abeles
 */
public class RobotDescription {

	private List<RobotComponent> components;
	private RobotComponent self;

	/**
	 * Components that compose the robot.
	 *
	 * @return
	 */
	public List<RobotComponent> getComponents() {
		return components;
	}

	/**
	 * This is the component which is the center of the robot's coordinate frame.  Its state
	 * represents the robot's state in the global frame.  It also contains information which are
	 * not part of any component but also describe the robot.
	 *
	 * @return Description of the robot in the global frame and a description of itself.
	 */
	public RobotComponent getSelf() {
		return self;
	}
}
