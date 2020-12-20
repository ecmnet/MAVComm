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

package bubo.mapping.build.ladar2d;

import bubo.clouds.fit.Lrf2dScanToScan;
import bubo.clouds.fit.s2s.Lrf2dScanToScan_LocalICP;
import bubo.clouds.motion.Lrf2dMotionRollingKeyFrame;
import bubo.desc.sensors.lrf2d.Lrf2dParam;
import bubo.desc.sensors.lrf2d.Lrf2dPrecomputedTrig;
import bubo.log.streams.PositionRangeArrayData;
import bubo.maps.d2.grid.GridMapSpacialInfo;
import bubo.maps.d2.grid.OccupancyGrid2D_F32;
import bubo.struct.StoppingCondition;
import georegression.struct.point.Point2D_F64;
import georegression.struct.se.Se2_F64;
import georegression.transform.se.SePointOps_F64;

/**
 * @author Peter Abeles
 */
// todo improve weighting mechanism.  use probability theory there too?
public class LadarMapBayesUpdate extends LineGridGenericUpdate {


	Lrf2dPrecomputedTrig trig;
	Lrf2dParam param;
	OccupancyGrid2D_F32 map;
	GridMapSpacialInfo mapSpacial;
	LineRangeProbability probability = new LineRangeGaussian(0.2);

//	Lrf2dScanToScan scanMatching = new Lrf2dScanToScan_GenericICP();
	Lrf2dScanToScan scanMatching = new Lrf2dScanToScan_LocalICP(new StoppingCondition(20, 0.0001), 200, 0.20);
//    Lrf2dScanToScan scanMatching = new Lrf2dScanToScan_IDC(new StoppingCondition(20,0.0001),
//            UtilAngle.degreeToRadian(20),0.20,0.1);

	Lrf2dMotionRollingKeyFrame motion;

	float sensorWeight = 0.05f;
	float mapWeight = 0.99f;

	Se2_F64 estimatedCurrToWorld = new Se2_F64();

	public void init(Lrf2dParam param,
					 OccupancyGrid2D_F32 map,
					 GridMapSpacialInfo mapSpacial) {
		this.param = param;
		trig = new Lrf2dPrecomputedTrig(param);
		this.map = map;
		this.mapSpacial = mapSpacial;

		if (scanMatching != null) {
			scanMatching.setSensorParam(param);
			motion = new Lrf2dMotionRollingKeyFrame(scanMatching,10);
			motion.init(param);
		}
	}

	public Se2_F64 getPosition() {
		return estimatedCurrToWorld;
	}

	public void process(PositionRangeArrayData ranges) {

		final int N = param.getNumberOfScans();
		double r[] = ranges.getRange();

		// todo clean up variable naming for reference frames

		if( scanMatching != null ) {
			motion.process(ranges.getScanToWorld(),ranges.getRange());
			estimatedCurrToWorld.set(motion.getSensorToWorld());
		} else {
			estimatedCurrToWorld.set(ranges.getScanToWorld());
		}

		double cellSize = mapSpacial.getCellSize();

		// todo this is where coordinates need to be concat
		double x0 = (estimatedCurrToWorld.getX() - mapSpacial.getBl().getX()) / cellSize;
		double y0 = (estimatedCurrToWorld.getY() - mapSpacial.getBl().getY()) / cellSize;

//        System.out.println("Location "+ranges.getScanToWorld().getTranslation());

		Point2D_F64 temp = new Point2D_F64();
		for (int i = 0; i < N; i++) {

			probability.setRangeMeasurement(r[i]);
			// update the map the distance of the measurement plus the extension for
			// the distribution
			double dist = r[i] + probability.lineExtension();

			if (!param.isValidRange(dist)) {
				continue;
			}

			// compute ladar hit in sensor coordinates
			trig.computeEndPoint(i, dist);

			// convert to map coordinates
			SePointOps_F64.transform(estimatedCurrToWorld, trig.x, trig.y, temp);

			double x1 = (temp.getX() - mapSpacial.getBl().getX()) / cellSize;
			double y1 = (temp.getY() - mapSpacial.getBl().getY()) / cellSize;

//            System.out.println("( "+x0+" , "+y0+" ) ( "+x1+" , "+y1+" )");
			// todo bounds check the line to avoid pointless search
			update(x0, y0, x1, y1, dist);
		}

	}

	/**
	 * Updates a cell on the occupancy grid using a bayesian process.  The map observation
	 * and sensor observation are weighted, preventing it from getting stuck at 0 and 1.
	 *
	 * @param x x-axis coordinate of cell on map
	 * @param y y-axis coordinate of cell on map
	 * @param p Probability of their being a wall according to a sensor observation.
	 */
	public void bayesUpdate(final int x, final int y, float p) {
		float m = map.get(x, y);

		// todo is this decay bayesian?
		p = (p - 0.5f) * sensorWeight + 0.5f;
		m = (m - 0.5f) * mapWeight + 0.5f;

		m = p * m / (p * m + (1.0f - p) * (1.0f - m));

		if (m < 0 || m > 1)
			throw new RuntimeException("Egads " + m);


		map.set(x, y, m);
	}

	@Override
	protected void update(int x, int y, double dist) {
		if (map.isInBounds(x, y)) {
			float p = probability.computeProbability(dist);

			bayesUpdate(x, y, p);
		}
	}


}
