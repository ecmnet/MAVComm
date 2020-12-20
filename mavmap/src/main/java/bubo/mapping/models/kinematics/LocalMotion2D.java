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

import georegression.struct.se.Se2_F64;

/**
 * Description of a motion in which a local tranlation is done followed by a point rotation.
 *
 * <pre>
 * x' = x + lx*cos(theta) - ly*sin(theta)
 * x' = x + ly*cos(theta) + lx*sin(theta)
 * theta' = theta + ltheta;
 * </pre>
 * @author Peter Abeles
 */
public class LocalMotion2D {
	public double x;
	public double y;
	public double theta;

	public LocalMotion2D(double x, double y, double theta) {
		this.x = x;
		this.y = y;
		this.theta = theta;
	}

	public LocalMotion2D() {
	}

	public void set(double x, double y, double theta) {
		this.x = x;
		this.y = y;
		this.theta = theta;
	}


	/**
	 * Adds motion to the provided pose
	 * @param pose
	 */
	public void addTo(Se2_F64 pose) {
		pose.T.x = pose.T.x + x*pose.c - y*pose.s;
		pose.T.y = pose.T.y + x*pose.s + y*pose.c;
		pose.setYaw( pose.getYaw() + theta );
	}

	public void setFrom( Se2_F64 srcToWorld , Se2_F64 dstToWorld ) {

		double dx = dstToWorld.getX() - srcToWorld.getX();
		double dy = dstToWorld.getY() - srcToWorld.getY();

		x =  dx*srcToWorld.c + dy*srcToWorld.s;
		y = -dx*srcToWorld.s + dy*srcToWorld.c;

		theta = dstToWorld.getYaw()-srcToWorld.getYaw();
	}
}
