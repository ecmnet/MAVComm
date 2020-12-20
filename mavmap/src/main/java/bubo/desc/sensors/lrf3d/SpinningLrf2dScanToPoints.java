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

import bubo.desc.sensors.lrf2d.Lrf2dPrecomputedTrig;
import georegression.geometry.ConvertRotation3D_F64;
import georegression.struct.EulerType;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.se.Se3_F64;
import georegression.transform.se.InterpolateLinearSe3_F64;
import georegression.transform.se.SePointOps_F64;
import org.ddogleg.struct.FastQueue;

/**
 * Code for converting a LRF scan into 3D points in the spinning sensor's base reference frame.  For
 * a description of all the coordinate systems see {@link SpinningLrf2dParam}.
 *
 * @author Peter Abeles
 */
public class SpinningLrf2dScanToPoints {

	// specification of the spinning LRF
	private SpinningLrf2dParam param;
	// fast computation of 2D location of a scan point
	private Lrf2dPrecomputedTrig trig;

	// transforms which take in account the sensor's rotation
	private Se3_F64 lrfToBase = new Se3_F64();
	private Se3_F64 lrf0ToWorld0 = new Se3_F64();
	private Se3_F64 lrf1ToWorld1 = new Se3_F64();

	private Se3_F64 baseRtoBase = new Se3_F64(); // rotated base to canonical base
	private Se3_F64 armToBaseR = new Se3_F64();  // arm to rotated base
	private Se3_F64 armToBase = new Se3_F64();   // arm to canonical base
	private Se3_F64 lrfToWorld = new Se3_F64();   // LRF to canonical base

	// interpolate between lrf0 and lrf1
	private InterpolateLinearSe3_F64 interp = new InterpolateLinearSe3_F64();

	// storage for 2D point
	private Point2D_F64 point2D = new Point2D_F64();
	// Storage for 3D points
	private FastQueue<Point3D_F64> points = new FastQueue<Point3D_F64>(Point3D_F64.class,true);

	// dummy matrix
	private Se3_F64 identity = new Se3_F64();

	public SpinningLrf2dScanToPoints(SpinningLrf2dParam param) {
		this.param = param;
		trig = new Lrf2dPrecomputedTrig(param.param2d);
		points.growArray(param.param2d.getNumberOfScans());
		armToBaseR.getT().set(param.radius,0,0);
	}

	/**
	 * Computes the coordinate of all valid scans in the measurement.
	 * @param meas Measurement
	 * @param baseToWorld0 (Optional) transform from sensor base to world at start of scan.  If null identity is used.
	 * @param baseToWorld1 (Optional) transform from sensor base to world at end of scan.  If null identity is used.
	 */
	public void process( SpinningLrf2dMeasurement meas , Se3_F64 baseToWorld0 , Se3_F64 baseToWorld1 ) {

		if( baseToWorld0 == null )
			baseToWorld0 = identity;
		if( baseToWorld1 == null )
			baseToWorld1 = identity;

		// The LRF is spinning.  Compute the transform when the first scan was collected and when it ended
		ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ,0,0,meas.angle0, baseRtoBase.R);
		armToBaseR.concat(baseRtoBase, armToBase);
		param.lrfToArm.concat(armToBase, lrfToBase);
		lrfToBase.concat(baseToWorld0,lrf0ToWorld0);
		ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ,0, 0, meas.angle1, baseRtoBase.R);
		armToBaseR.concat(baseRtoBase, armToBase);
		param.lrfToArm.concat(armToBase, lrfToBase);
		lrfToBase.concat(baseToWorld1,lrf1ToWorld1);

		// set up interpolation
		interp.setTransforms(lrf0ToWorld0, lrf1ToWorld1);

		// compute the location of each measurement and convert to sensor frame
		points.reset();
		for (int i = 0; i < meas.numMeas; i++) {
			double r = meas.meas[i];
			if( param.param2d.isValidRange(r)) {
				Point3D_F64 p3 = points.grow();
				trig.computeEndPoint(i, meas.meas[i], point2D);

				// compute point location in LRF reference frame
				p3.set(-point2D.y, 0, point2D.x);

				// now put it into the sensor's base
				interp.interpolate( i/(double)(meas.numMeas-1), lrfToWorld);

				SePointOps_F64.transform(lrfToWorld,p3,p3);
			}
		}
	}

	public FastQueue<Point3D_F64> getPoints() {
		return points;
	}
}
