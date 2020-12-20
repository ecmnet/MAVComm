///*
// * Copyright (c) 2013-2014, Peter Abeles. All Rights Reserved.
// *
// * This file is part of Project BUBO.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package bubo.clouds.fit.c2c;
//
//import bubo.clouds.fit.MatchCloudToCloud;
//import bubo.clouds.fit.algs.ClosestPointToModel;
//import bubo.clouds.fit.algs.IterativeClosestPoint;
//import bubo.struct.StoppingCondition;
//import georegression.fitting.MotionTransformPoint;
//import georegression.fitting.se.MotionSe2PointSVD_F64;
//import georegression.fitting.se.MotionSe3PointSVD_F64;
//import georegression.struct.GeoTuple_F64;
//import georegression.struct.InvertibleTransform;
//import georegression.struct.point.Point2D_F64;
//import georegression.struct.point.Point3D_F64;
//import georegression.struct.se.Se2_F64;
//import georegression.struct.se.Se3_F64;
//import org.ddogleg.nn.NearestNeighbor;
//import org.ddogleg.nn.NnData;
//import org.ddogleg.struct.FastQueue;
//
//import java.util.List;
//
///**
// * Abstract implementation of ICP for point cloud fitting using an
// * {@link georegression.struct.InvertibleTransform arbitrary transform}.
// *
// * @see bubo.clouds.fit.algs.IterativeClosestPoint
// *
// * @author Peter Abeles
// */
//public abstract class MatchCloudToCloudIcp<SE extends InvertibleTransform, P extends GeoTuple_F64>
//		implements MatchCloudToCloud<SE, P>
//{
//	// Nearest-neighbor algorithm
//	NearestNeighbor<P> nn;
//
//	// reference to destination list
//	List<P> source;
//	// maximum distance apart two points can be.  Euclidean squared
//	double maxDistanceSq;
//	// storage for NN results
//	NnData<P> storageNN = new NnData<P>();
//
//	// the actual ICP algorithm
//	IterativeClosestPoint<SE,P> icp;
//
//	// storage for source points
//	FastQueue<double[]> pointsDst;
//
//	// number of dimension on the point
//	int dimen;
//
//	// total number of matched points
//	int totalMatched;
//
//	/**
//	 * Configures ICP
//	 *
//	 * @param dimen DOF of each point
//	 * @param motion estimates motion between two sets of associated points
//	 * @param nn Nearest-Neighbor search
//	 * @param maxDistanceSq Maximum distance between two paired points. Euclidean squared
//	 * @param stop Stopping criteria for ICP iterations
//	 */
//	public MatchCloudToCloudIcp(final int dimen, MotionTransformPoint<SE, P> motion,
//								NearestNeighbor<P> nn,
//								double maxDistanceSq, StoppingCondition stop) {
//		this.maxDistanceSq = maxDistanceSq;
//		this.nn = nn;
//		this.dimen = dimen;
//		icp = new IterativeClosestPoint<SE, P>(stop,motion);
//		icp.setModel(new Model());
//
//		pointsDst = new FastQueue<double[]>(double[].class,true ) {
//			@Override
//			protected double[] createInstance() {
//				return new double[dimen];
//			}
//		};
//	}
//
//	@Override
//	public void setSource(List<P> source) {
//		this.source = source;
//	}
//
//	protected abstract void assign( P src , double[] dst );
//
//	@Override
//	public void setDestination(List<P> destination) {
//		// convert the points into a format NN understands
//		pointsDst.reset();
//		for (int i = 0; i < destination.size(); i++) {
//			assign(destination.get(i), pointsDst.grow());
//		}
//
//		nn.init(dimen);
//		nn.setPoints(pointsDst.toList(),destination);
//	}
//
//	@Override
//	public boolean compute() {
//		return icp.process(source);
//	}
//
//	@Override
//	public SE getSourceToDestination() {
//		return icp.getPointsToModel();
//	}
//
//	private class Model implements ClosestPointToModel<P> {
//
//		double srcPt[] = new double[3];
//
//		@Override
//		public P findClosestPoint(P target) {
//			assign(target, srcPt);
//
//			if( nn.findNearest(srcPt, maxDistanceSq, storageNN) ) {
//				return storageNN.data;
//			} else {
//				return null;
//			}
//		}
//	}
//
//	@Override
//	public boolean isModifiedSource() {
//		return true;
//	}
//
//	@Override
//	public int getMatchedSourcePoints() {
//		return icp.getTotalMatched();
//	}
//
//	/**
//	 * Specialized implementation for rigid body 2D points
//	 */
//	public static class SE2 extends MatchCloudToCloudIcp<Se2_F64,Point2D_F64> {
//
//		public SE2(NearestNeighbor<Point2D_F64> nn,double maxDistanceSq, StoppingCondition stop) {
//			super(2, new MotionSe2PointSVD_F64(),nn, maxDistanceSq, stop);
//		}
//
//		@Override
//		protected void assign(Point2D_F64 src, double[] dst) {
//			dst[0] = src.x;
//			dst[1] = src.y;
//		}
//	}
//
//	/**
//	 * Specialized implementation for rigid body 3D points
//	 */
//	public static class SE3 extends MatchCloudToCloudIcp<Se3_F64,Point3D_F64> {
//
//		public SE3(NearestNeighbor<Point3D_F64> nn,double maxDistanceSq, StoppingCondition stop) {
//			super(3, new MotionSe3PointSVD_F64(),nn, maxDistanceSq, stop);
//		}
//
//		@Override
//		protected void assign(Point3D_F64 src, double[] dst) {
//			dst[0] = src.x;
//			dst[1] = src.y;
//			dst[2] = src.z;
//		}
//	}
//}
