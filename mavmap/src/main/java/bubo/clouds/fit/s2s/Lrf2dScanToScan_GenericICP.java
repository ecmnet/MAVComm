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
import bubo.clouds.fit.algs.IterativeClosestPoint;
import bubo.clouds.fit.algs.PointModel;
import bubo.desc.sensors.lrf2d.Lrf2dParam;
import bubo.desc.sensors.lrf2d.Lrf2dPrecomputedTrig;
import bubo.struct.StoppingCondition;
import georegression.fitting.se.MotionSe2PointSVD_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.se.Se2_F64;
import georegression.transform.se.SePointOps_F64;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Basic implementation of scan matching that uses a generic version of {@link bubo.clouds.fit.algs.IterativeClosestPoint}.
 * Not the fast or most accurate but easy to understand/implement.
 * </p>
 *
 * @author Peter Abeles
 */
public class Lrf2dScanToScan_GenericICP implements Lrf2dScanToScan {
	// description of the LRF being used
	private Lrf2dParam param;
	// speeds up calculations
	private Lrf2dPrecomputedTrig lrf2pt;

	// the previous LRF scan which the motion is being computed relative to
	private List<Point2D_F64> reference = new ArrayList<Point2D_F64>();
	// most recent LRF scan
	private List<Point2D_F64> match = new ArrayList<Point2D_F64>();
	// a working copy of the LRF scan since it is modified
	private List<Point2D_F64> working = new ArrayList<Point2D_F64>();

	// saved points which can be used.  Faster than constantly creating/destroying memory
	private List<Point2D_F64> savedPts = new ArrayList<Point2D_F64>();

	// most recently estimated motion
	private Se2_F64 foundMotion = new Se2_F64();

	private IterativeClosestPoint<Se2_F64, Point2D_F64> icp = new IterativeClosestPoint<Se2_F64, Point2D_F64>(
			new StoppingCondition(20, 0.0001), new MotionSe2PointSVD_F64());

	// maximum assumed distance that the robot can move between scans
	private double maxPointDistance = 0.2;

	public Lrf2dScanToScan_GenericICP() {
		icp.setModel(new PointModel<Point2D_F64>(reference, maxPointDistance));
	}

	@Override
	public void setSensorParam(Lrf2dParam param) {
		this.param = param;
		this.lrf2pt = new Lrf2dPrecomputedTrig(param);

		// precompute all the points that might be needed
		for (int i = 0; i < param.getNumberOfScans(); i++) {
			savedPts.add(new Point2D_F64());
			savedPts.add(new Point2D_F64());
			savedPts.add(new Point2D_F64());
		}
	}

	@Override
	public Se2_F64 getSourceToDestination() {
		return foundMotion;
	}

	@Override
	public void setDestination(double[] scan) {
		computePoints(scan, reference);
	}

	@Override
	public void setSource(double[] scan) {
		computePoints(scan, match);
	}

	/**
	 * Computes the 2D coordinate of each LRF scan.
	 *
	 * @param scan   range measurements
	 * @param points 2D coordinates.  Old points contained in the list are recycled.
	 */
	private void computePoints(double[] scan, List<Point2D_F64> points) {
		final int N = param.getNumberOfScans();

		if (scan.length < N)
			throw new IllegalArgumentException("Scan does not match LRF description");

		// recycle old points
		savedPts.addAll(points);
		points.clear();

		// find the coordinates and use available points
		for (int i = 0; i < N; i++) {
			double r = scan[i];

			if (param.isValidRange(r)) {
				Point2D_F64 p = savedPts.remove(savedPts.size() - 1);
				points.add(p);
				lrf2pt.computeEndPoint(i, scan[i], p);
			}
		}
	}

	/**
	 * Swaps the two point lists and tells ICP about the change
	 */
	@Override
	public void assignSourceToDestination() {
		List<Point2D_F64> temp = reference;
		reference = match;
		match = temp;

		icp.setModel(new PointModel<Point2D_F64>(reference, maxPointDistance));
	}

	/**
	 * Creates a copy of the second scan so that it can be modified.
	 */
	void createWorkingCopy() {
		savedPts.addAll(working);
		working.clear();

		for (Point2D_F64 p : match) {
			Point2D_F64 w = savedPts.remove(savedPts.size() - 1);
			w.x = p.x;
			w.y = p.y;
			working.add(w);
		}
	}

	@Override
	public boolean process(Se2_F64 hintSrcToDst) {

		createWorkingCopy();

		if (hintSrcToDst != null) {
			// use the hint to bring the two sets of odometry closer
			SePointOps_F64.transform(hintSrcToDst, working);
		}

		// the motion is found from the current scan to the previous scan this is because in the robot's frame the
		// transform is reversed Example, if heading forward towards a wall the wall will be closer to the robot in
		// the LRF scan, Thus the transform from the first to second would be in the opposite direction of the robot's
		// motion.
		icp.process(working);

		// save the found motion
		if (hintSrcToDst != null)
			hintSrcToDst.concat(icp.getPointsToModel(), foundMotion);
		else
			foundMotion.set(icp.getPointsToModel());

		return true;
	}


	@Override
	public double getError() {
		return icp.getFoundError();
	}

	@Override
	public int totalScansMatched() {
		return icp.getTotalMatched();
	}
}
