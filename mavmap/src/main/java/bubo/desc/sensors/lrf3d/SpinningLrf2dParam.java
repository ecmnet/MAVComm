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

package bubo.desc.sensors.lrf3d;

import bubo.desc.sensors.lrf2d.Lrf2dParam;
import georegression.struct.se.Se3_F64;

import java.io.Serializable;

/**
 * <p>
 * Description of a spinning 2D LRF.  This description is composed of the 2D description,
 * {@link bubo.desc.sensors.lrf2d.Lrf2dParam}, and a radius of rotation.
 * </p>
 *
 * <p>
 * There are 3 reference frames.  LRF is the sensor's reference frame. Arm is the reference frame whose origin
 * is rotated about the base. The base reference frame is the reference frame which contains the rotary joint.
 * In the LRF reference frame the 2D points are converted into 3D points by the following transform
 * p3 = ( -p2.y , 0 , p2.x ).  The transform from LRF to arm will nominally be identity, but physical implementations
 * can cause it to not be.  The transform from arm to base is a translation of (radius , 0 , 0).
 * </p>
 *
 * <p>
 * EXAMPLE: If the LRF to arm transform is identity, then when the LRF is at a rotation of angle 0 it lies at the
 * (radius,0,0) coordinate and at an angle of 90 degrees it lies at (0,radius,0).
 * </p>
 *
 * @author Peter Abeles
 */
public class SpinningLrf2dParam implements Serializable {
	/**
	 * Description of the 2D LRF
	 */
	public Lrf2dParam param2d;

	/**
	 * Transform from LRF to arm.  The origin of arm lies at (radius,0,0) when the rotation angle is 0.
	 */
	public Se3_F64 lrfToArm = new Se3_F64();

	/**
	 * Radius of the circle the LRF spins around
	 */
	public double radius;

	public SpinningLrf2dParam(Lrf2dParam param2d, Se3_F64 lrfToArm, double radius) {
		this.param2d = param2d;
		this.lrfToArm = lrfToArm;
		this.radius = radius;
	}

	public SpinningLrf2dParam(Lrf2dParam param2d, double radius) {
		this.param2d = param2d;
		this.radius = radius;
	}

	public SpinningLrf2dParam() {
	}

	public Lrf2dParam getParam2d() {
		return param2d;
	}

	public void setParam2d(Lrf2dParam param2d) {
		this.param2d = param2d;
	}

	public Se3_F64 getLrfToArm() {
		return lrfToArm;
	}

	public void setLrfToArm(Se3_F64 lrfToArm) {
		this.lrfToArm = lrfToArm;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}
}
