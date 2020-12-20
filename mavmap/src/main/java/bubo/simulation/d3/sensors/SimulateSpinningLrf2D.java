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

import bubo.desc.sensors.lrf3d.SpinningLrf2dMeasurement;
import bubo.desc.sensors.lrf3d.SpinningLrf2dParam;
import bubo.desc.sensors.lrf3d.SpinningLrf2dScanToPoints;
import bubo.maps.d3.triangles.Triangle3dMap;
import georegression.metric.UtilAngle;
import georegression.struct.se.Se3_F64;
import georegression.transform.se.InterpolateLinearSe3_F64;

/**
 * Simulates LRF scans in a simulated triangle world.
 *
 * The location of the first scan relative to the sensor coordinate frame is specified by baseToSensor.  The
 * sensor then rotates around it's +z axis as it collects data.
 *
 * @author Peter Abeles
 */
public class SimulateSpinningLrf2D {
	SpinningLrf2dMeasurement measurement;
	SpinningLrf2dParam param;
	Se3_F64 baseToSensor = new Se3_F64();

	private InterpolateLinearSe3_F64 interp = new InterpolateLinearSe3_F64();
	Se3_F64 sensorToWorldPrev = new Se3_F64();
	Se3_F64 sensorToWorld0 = new Se3_F64();

	boolean first = true;
	SpinningLrf2dScanToPoints spinningTransform;
	RaytraceSpinningLrf2D raytrace;

	double time;

	// How long it takes to do a full rotation
	public double rotationPeriod;
	// How quickly it performs a single sweep of the LRF
	public double sweepTime;

	public SimulateSpinningLrf2D(SpinningLrf2dParam param, double rotationPeriod, double sweepTime ) {
		this.param = param;
		this.rotationPeriod = rotationPeriod;
		this.sweepTime = sweepTime;
		measurement = new SpinningLrf2dMeasurement(param.getParam2d().getNumberOfScans());
		spinningTransform = new SpinningLrf2dScanToPoints(param);
	}

	public SpinningLrf2dParam getParam() {
		return param;
	}

	public SpinningLrf2dMeasurement getMeasurement() {
		return measurement;
	}

	public void reset() {
		first = true;
	}

	/**
	 * Given the world model, computes the range measurements
	 *
	 * @param sensorToWorld transform from sensor's base to world frame.  Sensor's base is assumed to be static in
	 *                      reference to the world during the scan.
	 * @param world Map of the world
	 */
	public void update( Se3_F64 sensorToWorld , Triangle3dMap world , double updatePeriod ) {
		if( first ) {
			first = false;
			sensorToWorldPrev.set(sensorToWorld);
			time = 0;
		} else {

			// if the sweep time is less than the update period perform the sweep at the start of the period
			if (sweepTime <= updatePeriod) {
				interp.setTransforms(sensorToWorldPrev, sensorToWorld);
				interp.interpolate( (updatePeriod-sweepTime)/updatePeriod, sensorToWorld0);
			} else {
				// otherwise it should break the sweep across multiple updates.  Instead let's just throw an
				// exception
				throw new RuntimeException("Sweep time is too short");
			}

			// given how long it has been spinning compute it's current rotation angle
			time += updatePeriod;
			measurement.angle0 = UtilAngle.bound(Math.PI*2.0*((time-sweepTime)/rotationPeriod));
			// see how far it rotates during a single sweep
			measurement.angle1 = UtilAngle.bound(Math.PI*2.0*(time/rotationPeriod));

			// simulate location
			raytrace.setMap(world);
			raytrace.process(sensorToWorld0,sensorToWorld,measurement);
		}

		sensorToWorldPrev.set(sensorToWorld);
	}
}
