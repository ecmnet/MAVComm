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

import bubo.desc.sensors.lrf2d.Lrf2dMeasurement;
import bubo.desc.sensors.lrf2d.Lrf2dParam;
import bubo.desc.sensors.lrf2d.Lrf2dPrecomputedTrig;
import bubo.maps.d2.lines.LineSegmentMap;
import georegression.metric.Intersection2D_F64;
import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Vector2D_F64;
import georegression.struct.se.Se2_F64;
import georegression.transform.se.SePointOps_F64;

/**
 * Simulates LRF scans in a simulated line world
 *
 * @author Peter Abeles
 */
public class SimulateLrf2D {
	Lrf2dParam param;
	Lrf2dMeasurement measurement;
	Lrf2dPrecomputedTrig trig;

	public SimulateLrf2D(Lrf2dParam param) {
		this.param = param;
		this.trig = new Lrf2dPrecomputedTrig(param);
		this.measurement = new Lrf2dMeasurement(param.getNumberOfScans());
	}

	public Lrf2dMeasurement getMeasurement() {
		return measurement;
	}

	public Lrf2dParam getParam() {
		return param;
	}

	/**
	 * Given the world model, computes the range measurements
	 * @param sensorToWorld transform from sensor to world frame
	 * @param world Map of the world
	 */
	public void update( Se2_F64 sensorToWorld , LineSegmentMap world ) {

		Vector2D_F64 T = sensorToWorld.getTranslation();

		LineSegment2D_F64 laserLine = new LineSegment2D_F64();
		laserLine.a.set(T.x,T.y);

		Point2D_F64 hit = new Point2D_F64();

		for (int i = 0; i < param.getNumberOfScans(); i++) {
			// create the laser line
			trig.computeEndPoint(i,param.getMaxRange(),laserLine.b);

			// go from sensor to world frame
			SePointOps_F64.transform(sensorToWorld,laserLine.b,laserLine.b);

			// intersect with all the lines in the world and see if there are any collisions
			// if there is a collision save the distance of the closest
			double best = param.getMaxRange()*param.getMaxRange();
			for (int j = 0; j < world.lines.size(); j++) {
				LineSegment2D_F64 worldLine = world.lines.get(j);

				if( null != Intersection2D_F64.intersection(laserLine,worldLine,hit) ) {
					double d = laserLine.a.distance2(hit);
					if( d < best ) {
						best = d;
					}
				}
			}

			// save the results
			measurement.meas[i] = Math.sqrt(best);
		}

	}
}
