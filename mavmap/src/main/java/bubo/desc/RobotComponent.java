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

import java.util.ArrayList;
import java.util.List;

/**
 * A device (sensor, manipulator, ... etc) that is attached to a robot.
 *
 * @author Peter Abeles
 */
public class RobotComponent {

	protected String name;
	protected ExtrinsicParameters extrinsic;
	protected IntrinsicParameters intrinsic;
	protected ComponentIO inputOutput;

	protected List<RobotComponent> childComponents = new ArrayList<RobotComponent>();

	public RobotComponent(String name, ExtrinsicParameters extrinsic, IntrinsicParameters intrinsic, ComponentIO inputOutput) {
		this.name = name;
		this.extrinsic = extrinsic;
		this.intrinsic = intrinsic;
		this.inputOutput = inputOutput;
	}

	public void addChild(RobotComponent child) {
		childComponents.add(child);
	}

	public String getName() {
		return name;
	}

	public ExtrinsicParameters getExtrinsic() {
		return extrinsic;
	}

	public void setExtrinsic(ExtrinsicParameters extrinsic) {
		this.extrinsic = extrinsic;
	}

	public IntrinsicParameters getIntrinsic() {
		return intrinsic;
	}

	public void setIntrinsic(IntrinsicParameters intrinsic) {
		this.intrinsic = intrinsic;
	}

	public ComponentIO getInputOutput() {
		return inputOutput;
	}

	/**
	 * List of components which are part of this component but make more sense to be treated
	 * as a separate component.
	 *
	 * @return List of child components.
	 */
	public List<RobotComponent> getChildComponents() {
		return childComponents;
	}
}
