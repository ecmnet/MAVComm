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

package bubo.clouds.fit.s2s;

import bubo.clouds.fit.Lrf2dScanToScan;
import bubo.desc.sensors.lrf2d.Lrf2dParam;
import bubo.desc.sensors.lrf2d.Lrf2dPrecomputedTrig;
import bubo.struct.StoppingCondition;
import georegression.fitting.se.MotionSe2PointSVD_F64;
import georegression.metric.UtilAngle;
import georegression.struct.point.Point2D_F64;
import georegression.struct.se.Se2_F64;
import georegression.transform.se.SePointOps_F64;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * Base class for scan to scan matching that only considers scans that have an index close to each other.
 * </p>
 *
 * @author Peter Abeles
 */
public abstract class LocalScanToScanMatching implements Lrf2dScanToScan {

	// description of the sensor
	protected Lrf2dParam param;
	// decides when to stop iterating
	protected StoppingCondition stop;
	// various bits of information related to each scan
	protected ScanInfo first;
	protected ScanInfo second;
	// computed angles after transform has been applied
	protected double ang[];
	protected double scan[];
	// list of associated points
	protected List<Point2D_F64> fromPts = new ArrayList<Point2D_F64>();
	protected List<Point2D_F64> toPts = new ArrayList<Point2D_F64>();
	// speeds up calculations
	private Lrf2dPrecomputedTrig lrf2pt;
	// the found total motion
	private Se2_F64 motion = new Se2_F64();
	// the final error
	private double foundError;

	// given associated points computes rigid body motion
	private MotionSe2PointSVD_F64 motionAlg = new MotionSe2PointSVD_F64();

	// Number of indexes away it will search in the reference scan for a correspondence
	private int searchNeighborhood;

	// the maximum separation between two points for them to be connected
	private double maxSeparationSq;

	public LocalScanToScanMatching(StoppingCondition stop, int searchNeighborhood, double maxSeparation) {
		this.searchNeighborhood = searchNeighborhood;
		this.maxSeparationSq = maxSeparation * maxSeparation;
		this.stop = stop;
	}

	@Override
	public void setSensorParam(Lrf2dParam param) {
		this.param = param;
		this.lrf2pt = new Lrf2dPrecomputedTrig(param);

		ang = new double[param.getNumberOfScans()];
		first = new ScanInfo(param.getNumberOfScans());
		second = new ScanInfo(param.getNumberOfScans());
		scan = new double[param.getNumberOfScans()];
	}

	@Override
	public Se2_F64 getSourceToDestination() {
		return motion;
	}

	@Override
	public void setDestination(double[] scan) {
		System.arraycopy(scan, 0, first.scan, 0, param.getNumberOfScans());
	}

	@Override
	public void setSource(double[] scan) {
		System.arraycopy(scan, 0, second.scan, 0, param.getNumberOfScans());
	}

	public void computeScan(double scan[], Point2D_F64 pts[]) {
		final int N = param.getNumberOfScans();
		final double maxRange = param.getMaxRange();
		for (int i = 0; i < N; i++) {
			double r = scan[i];

			if (r <= maxRange) {
				lrf2pt.computeEndPoint(i, r, pts[i]);
			}
		}
	}

	@Override
	public void assignSourceToDestination() {
		ScanInfo temp = first;
		first = second;
		second = temp;
	}

	@Override
	public boolean process(Se2_F64 hintSrcToDst) {
//        System.out.println("-----------------------------------------------------");
		computeScan(second.scan, second.pts);
		computeScan(first.scan, first.pts);

		if (hintSrcToDst != null) {
			transform(hintSrcToDst, second);
			motion.set(hintSrcToDst);
		} else
			motion.set(0, 0, 0);

		setVisibleByRange(first);

		stop.reset();
		while (true) {
//            System.out.println();
			// compute the angle of each point in the current view
			computePointAngles(second);

//            UtilDouble.print(first.scan,"%5.2f");
//            UtilDouble.print(scan,"%5.2f");
//            UtilDouble.print(ang,"%5.2f");

			// angle based visibility test
			checkVisibleByDeltaAngle(second);

			// find the motion which minimizes the error between the two scans
			Se2_F64 foundMotion = estimateMotion();

			// apply the transform to the points in the scan being matched
			transform(foundMotion, second);
			foundError = computeMeanSquaredError();

			// increment
			motion = motion.concat(foundMotion, null);

			if (stop.isFinished(foundError))
				break;
		}

		return true;
	}

	protected abstract Se2_F64 estimateMotion();

	private void setVisibleByRange(ScanInfo info) {
		final int N = param.getNumberOfScans();
		final double maxRange = param.getMaxRange();
		for (int i = 0; i < N; i++) {
			first.vis[i] = info.scan[i] <= maxRange;
		}
	}

	/**
	 * Computes the angle of each scan and flags visible based on measured range
	 */
	private void computePointAngles(ScanInfo info) {
		final int N = param.getNumberOfScans();
		final double maxRange = param.getMaxRange();
		for (int i = 0; i < N; i++) {
			double r = info.scan[i];
			Point2D_F64 p = info.pts[i];

			if (r <= maxRange) {
				ang[i] = Math.atan2(p.y, p.x);
				scan[i] = Math.sqrt(p.y * p.y + p.x * p.x); // todo push outside
				info.vis[i] = true;
			} else {
				scan[i] = maxRange;
				info.vis[i] = false;
			}
		}
	}

	/**
	 * Sees if the order of observations is as expected
	 */
	private void checkVisibleByDeltaAngle(ScanInfo info) {
		final int N = param.getNumberOfScans();
		boolean increasing = param.getAngleIncrement() > 0;

		for (int i = 1; i < N; i++) {
			if (!info.vis[i])
				continue;

			double deltaAng = UtilAngle.minus(ang[i], ang[i - 1]);
			if (increasing) {
				if (deltaAng < 0)
					info.vis[i] = false;
			} else {
				if (deltaAng > 0)
					info.vis[i] = false;
			}
		}
	}

	/**
	 * Computes motion by associating points using the provided distance function then
	 * computing the Se2_F64 transform.
	 *
	 * @param funcDist distance function
	 * @return found motion
	 */
	protected Se2_F64 computeMotion(Distance funcDist) {
		associatePoints(funcDist);
//        filterAmbiguousAssociations();

		motionAlg.process(fromPts, toPts);

		return motionAlg.getTransformSrcToDst();
	}

	/**
	 * Associates LRF scans by minimizing the provided distance frunction
	 *
	 * @param funcDist used to measure the distance between two scans.
	 */
	protected void associatePoints(Distance funcDist) {

		fromPts.clear();
		toPts.clear();

		final int N = param.getNumberOfScans();
		for (int i = 0; i < N; i++) {
			if (!second.vis[i])
				continue;

			int min = i - searchNeighborhood;
			int max = i + searchNeighborhood;
			if (min < 0) min = 0;
			if (max > N) max = N;

			int bestIndex = -1;
			double bestDistance = Double.MAX_VALUE;

			funcDist.setReference(i);

			for (int j = min; j < max; j++) {
				if (!first.vis[j] || j == i)
					continue;

				double dist = funcDist.dist(j);

				if (dist < bestDistance) {
					bestDistance = dist;
					bestIndex = j;
				}
			}

			if (bestIndex != -1 && bestDistance < maxSeparationSq) {
				// todo Interpolate between the two sets
				fromPts.add(second.pts[i]);
				toPts.add(first.pts[bestIndex]);
			}
		}
	}

	/**
	 * If more than one point was associated with the same point then remove all of those
	 * associations
	 */
	private void filterAmbiguousAssociations() {
		for (int i = 0; i < toPts.size(); ) {
			Point2D_F64 t = toPts.get(i);

			boolean ambiguous = false;
			for (int j = i + 1; j < toPts.size(); ) {
				if (t == toPts.get(j)) {
					ambiguous = true;
					toPts.remove(j);
					fromPts.remove(j);
				} else {
					j++;
				}
			}

			if (!ambiguous)
				i++;

		}
	}

	private void transform(Se2_F64 m, ScanInfo scan) {
		for (Point2D_F64 p : scan.pts) {
			SePointOps_F64.transform(m, p, p);
		}
	}

	private double computeMeanSquaredError() {
		double error = 0;

		for (int i = 0; i < fromPts.size(); i++) {
			Point2D_F64 f = fromPts.get(i);
			Point2D_F64 t = toPts.get(i);

			error += f.distance2(t);
		}

		return error / fromPts.size();
	}

	@Override
	public double getError() {
		return foundError;
	}

	public int getSearchNeighborhood() {
		return searchNeighborhood;
	}

	public void setSearchNeighborhood(int searchNeighborhood) {
		this.searchNeighborhood = searchNeighborhood;
	}

	public static interface Distance {
		/**
		 * The scan in the first set of observations that distance is being computed in reference to
		 *
		 * @param indexA
		 */
		public void setReference(int indexA);

		/**
		 * Distance from reference to the specified index
		 *
		 * @param indexB
		 * @return
		 */
		public double dist(int indexB);
	}

	/**
	 * Information on a LRF scan
	 */
	public static class ScanInfo {
		// location of points in 2D cartesian space
		public Point2D_F64 pts[];
		// if the points are "visible"
		public boolean vis[];
		// raw range measurements
		public double scan[];

		public ScanInfo(int N) {
			pts = new Point2D_F64[N];
			vis = new boolean[N];
			scan = new double[N];

			for (int i = 0; i < N; i++) {
				pts[i] = new Point2D_F64();
			}
		}
	}
}
