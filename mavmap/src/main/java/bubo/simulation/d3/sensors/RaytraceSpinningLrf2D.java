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

package bubo.simulation.d3.sensors;

import bubo.desc.sensors.lrf2d.Lrf2dPrecomputedTrig;
import bubo.desc.sensors.lrf3d.SpinningLrf2dMeasurement;
import bubo.desc.sensors.lrf3d.SpinningLrf2dParam;
import bubo.maps.d3.triangles.Triangle3dMap;
import georegression.geometry.ConvertRotation3D_F64;
import georegression.geometry.GeometryMath_F64;
import georegression.metric.Intersection3D_F64;
import georegression.struct.EulerType;
import georegression.struct.line.LineParametric3D_F64;
import georegression.struct.line.LineSegment3D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector2D_F64;
import georegression.struct.se.Se3_F64;
import georegression.struct.shapes.Triangle3D_F64;
import georegression.transform.se.InterpolateLinearSe3_F64;
import georegression.transform.se.SePointOps_F64;

/**
 * Simulates a spinning 2D LRF in a Triangle3dMap world.
 *
 * @author Peter Abeles
 */
public class RaytraceSpinningLrf2D {

	// world map
	private Triangle3dMap map;

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

	// direction in 2D
	Vector2D_F64 v = new Vector2D_F64();
	// laser ray
	LineParametric3D_F64 line = new LineParametric3D_F64();
	LineSegment3D_F64 segment = new LineSegment3D_F64();

	// dummy matrix
	private Se3_F64 identity = new Se3_F64();

	public RaytraceSpinningLrf2D( SpinningLrf2dParam param ) {
		this.param = param;
		trig = new Lrf2dPrecomputedTrig(param.param2d);
		armToBaseR.getT().set(param.radius,0,0);
	}

	/**
	 * Computes the coordinate of all valid scans in the measurement.
	 * @param baseToWorld0 (Optional) transform from sensor base to world at start of scan.  If null identity is used.
	 * @param baseToWorld1 (Optional) transform from sensor base to world at end of scan.  If null identity is used.
	 * @param meas (output) simulated measurements in the world
	 */
	public void process(Se3_F64 baseToWorld0, Se3_F64 baseToWorld1, SpinningLrf2dMeasurement meas) {

		if( baseToWorld0 == null )
			baseToWorld0 = identity;
		if( baseToWorld1 == null )
			baseToWorld1 = identity;

		// The LRF is spinning.  Compute the transform when the first scan was collected and when it ended
		ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ,0, 0, meas.angle0, baseRtoBase.R);
		armToBaseR.concat(baseRtoBase, armToBase);
		param.lrfToArm.concat(armToBase, lrfToBase);
		lrfToBase.concat(baseToWorld0,lrf0ToWorld0);
		ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ,0, 0, meas.angle1, baseRtoBase.R);
		armToBaseR.concat(baseRtoBase, armToBase);
		param.lrfToArm.concat(armToBase, lrfToBase);
		lrfToBase.concat(baseToWorld1,lrf1ToWorld1);

		// set up interpolation
		interp.setTransforms(lrf0ToWorld0, lrf1ToWorld1);

		for (int i = 0; i < meas.numMeas; i++) {
			// compute ray in sensor reference frame
			trig.computeDirection(i, v);
			line.p.set(0, 0, 0);
			line.slope.set(-v.y,0,v.x);

			// transform to world
			interp.interpolate( i/(double)(meas.numMeas-1), lrfToWorld);
			SePointOps_F64.transform(lrfToWorld, line.p, line.p);
			GeometryMath_F64.mult(lrfToWorld.getR(),line.slope,line.slope);

			// see if it hits anything in the world
			meas.meas[i] = findIntersection(line);
		}
	}

	/**
	 * Find the intersection (if any) of the line with objects in the world
	 */
	protected double findIntersection( LineParametric3D_F64 line ) {

		double R = param.getParam2d().getMaxRange();

		segment.a.set(line.p);
		segment.b.x = line.p.x + R*line.slope.x;
		segment.b.y = line.p.y + R*line.slope.y;
		segment.b.z = line.p.z + R*line.slope.z;

		Point3D_F64 hit = new Point3D_F64();

		double best = Double.MAX_VALUE;

		for(Triangle3D_F64 t : map.triangles ) {
			int result = Intersection3D_F64.intersect(t,segment,hit);

			if( result == 1 ) {
				double d = line.p.distance(hit);
				if( d < best ) {
					best = d;
				}
			}
		}

		return best;
	}

	public void setMap(Triangle3dMap map) {
		this.map = map;
	}
}
