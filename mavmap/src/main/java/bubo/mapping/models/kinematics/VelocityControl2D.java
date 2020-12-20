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

package bubo.mapping.models.kinematics;

/**
 * @author Peter Abeles
 */
public class VelocityControl2D {
	// control input: Translational Velocity
	public double translationalVel;
	// control input: Angular Velocity
	public double angularVel;

	public VelocityControl2D(double translationalVel, double angularVel) {
		this.translationalVel = translationalVel;
		this.angularVel = angularVel;
	}

	public VelocityControl2D() {
	}

	public void set(double translationalVel, double angularVel) {
		this.translationalVel = translationalVel;
		this.angularVel = angularVel;
	}

	public double getTranslationalVel() {
		return translationalVel;
	}

	public void setTranslationalVel(double translationalVel) {
		this.translationalVel = translationalVel;
	}

	public double getAngularVel() {
		return angularVel;
	}

	public void setAngularVel(double angularVel) {
		this.angularVel = angularVel;
	}
}
