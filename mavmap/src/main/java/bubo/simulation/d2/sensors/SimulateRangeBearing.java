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

package bubo.simulation.d2.sensors;

import bubo.desc.sensors.landmark.RangeBearingMeasurement;
import bubo.desc.sensors.landmark.RangeBearingParam;
import bubo.maps.d2.LandmarkMap2D;
import georegression.struct.point.Point2D_F64;
import georegression.struct.se.Se2_F64;
import georegression.transform.se.SePointOps_F64;
import org.ddogleg.struct.FastQueue;

/**
 * Simulates LRF scans in a simulated line world
 *
 * @author Peter Abeles
 */
public class SimulateRangeBearing {
	RangeBearingParam param;
	FastQueue<RangeBearingMeasurement> measurements =
			new FastQueue<RangeBearingMeasurement>(RangeBearingMeasurement.class,true);

	public SimulateRangeBearing(RangeBearingParam param) {
		this.param = param;
	}

	public FastQueue<RangeBearingMeasurement> getMeasurements() {
		return measurements;
	}

	public RangeBearingParam getParam() {
		return param;
	}

	/**
	 * Given the world model, computes the range measurements
	 * @param sensorToWorld transform from sensor to world frame
	 * @param world Map of the world
	 */
	public void update( Se2_F64 sensorToWorld , LandmarkMap2D world ) {

		measurements.reset();

		Point2D_F64 local = new Point2D_F64();
		for (int i = 0; i < world.getTotal(); i++) {
			// location in world coordinates
			Point2D_F64 landmark = world.getLocation(i);

			// find location in sensor coordinates
			SePointOps_F64.transformReverse(sensorToWorld, landmark, local);

			double range = local.norm();

			if( range <= param.maxRange ) {
				RangeBearingMeasurement m = measurements.grow();

				m.id = i;
				m.range = range;
				m.bearing = Math.atan2(local.y,local.x);
			}
		}
	}
}
