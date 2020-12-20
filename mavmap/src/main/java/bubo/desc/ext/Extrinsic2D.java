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

package bubo.desc.ext;

import bubo.desc.ExtrinsicParameters;
import bubo.desc.RobotComponent;
import georegression.struct.se.Se2_F64;

/**
 * Extrinsic parameters in a 2D world model are described using special euclidean 2D transforms.  These
 * are composed of a rotation then a translation.
 *
 * @author Peter Abeles
 */
public class Extrinsic2D implements ExtrinsicParameters {

	// the component that this transform is in reference to
	private RobotComponent ref;

	// coordinate system transform
	// from the local to the parent reference frame
	private Se2_F64 tranToParent = new Se2_F64();

	// is this transform fixed
	private boolean fixed;

	public Extrinsic2D(RobotComponent ref, boolean fixed) {
		this.ref = ref;
		this.fixed = fixed;
	}

	public void setTransformToParent(double x, double y, double yaw) {
		tranToParent.set(x, y, yaw);
	}

	/**
	 * <p>
	 * Returns the transform from the local coordinate system to the reference
	 * frame.
	 * </p>
	 *
	 * @return Coordinate system transform.
	 */
	public Se2_F64 getTransformToParent() {
		return tranToParent;
	}

	/**
	 * If the this transform ever changes or not
	 */
	public boolean isFixed() {
		return fixed;
	}

	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	@Override
	public RobotComponent getReference() {
		return ref;
	}

	@Override
	public boolean isConstant() {
		return fixed;
	}
}
