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
//package bubo.clouds;
//
//import bubo.clouds.fit.MatchCloudToCloud;
//import bubo.clouds.fit.c2c.MatchCloudToCloudIcp;
//import bubo.struct.StoppingCondition;
//import georegression.struct.point.Point2D_F64;
//import georegression.struct.point.Point3D_F64;
//import georegression.struct.se.Se2_F64;
//import georegression.struct.se.Se3_F64;
//import org.ddogleg.nn.FactoryNearestNeighbor;
//import org.ddogleg.nn.NearestNeighbor;
//
///**
// * @author Peter Abeles
// */
//public class FactoryFitting {
//
//	/**
//	 * 2D {@link georegression.struct.se.Se2_F64 rigid-body} {@link bubo.clouds.fit.algs.IterativeClosestPoint}
//	 * based {@link MatchCloudToCloud}.
//	 *
//	 * @param maxDistance Maximum Euclidean distance two points will be matched up.
//	 * @param stop Iteration stopping criteria
//	 * @return ICP based cloud matching
//	 */
//	public static MatchCloudToCloud<Se2_F64,Point2D_F64> cloudIcp2D( double maxDistance, StoppingCondition stop)  {
//		NearestNeighbor<Point2D_F64> nn = FactoryNearestNeighbor.kdtree();
//		return new MatchCloudToCloudIcp.SE2(nn,maxDistance*maxDistance,stop);
//	}
//
//	/**
//	 * 3D {@link georegression.struct.se.Se3_F64 rigid-body} {@link bubo.clouds.fit.algs.IterativeClosestPoint}
//	 * based {@link MatchCloudToCloud}.
//	 *
//	 * @param maxDistance Maximum Euclidean distance two points will be matched up.
//	 * @param stop Iteration stopping criteria
//	 * @return ICP based cloud matching
//	 */
//	public static MatchCloudToCloud<Se3_F64,Point3D_F64> cloudIcp3D( double maxDistance, StoppingCondition stop)  {
//		NearestNeighbor<Point3D_F64> nn = FactoryNearestNeighbor.kdtree();
//		return new MatchCloudToCloudIcp.SE3(nn,maxDistance*maxDistance,stop);
//	}
//}
