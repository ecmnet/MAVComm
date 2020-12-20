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

package bubo.clouds.filter;

import bubo.construct.ConstructOctreeLeaf_I32;
import bubo.construct.Octree_I32;
import georegression.geometry.UtilPoint3D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Point3D_I32;
import georegression.struct.se.Se3_F64;
import georegression.struct.shapes.Box3D_F64;
import georegression.struct.shapes.Box3D_I32;
import georegression.transform.se.SePointOps_F64;
import org.ddogleg.struct.FastQueue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * TODO comment
 *
 *
 * @author Peter Abeles
 */
public class UniformDensityCloudOctree {

	Random rand;

	// How many points it wants in a region
	int desiredCount;
	// size of the region
	double desiredWidth;

	ConstructOctreeLeaf_I32 octree = new ConstructOctreeLeaf_I32();

	Box3D_F64 bounds = new Box3D_F64();

	Se3_F64 worldToOctree = new Se3_F64();
	FastQueue<Point3D_I32> gridPoints = new FastQueue<Point3D_I32>(Point3D_I32.class,true);

	List<Point3D_F64> shuffleList = new ArrayList<Point3D_F64>();

	Point3D_F64 local = new Point3D_F64();

	public UniformDensityCloudOctree(int desiredCount, double desiredWidth , long randSeed ) {
		this.desiredCount = desiredCount;
		this.desiredWidth = desiredWidth;
		this.rand = new Random(randSeed);
	}

	public void process( List<Point3D_F64> input , List<Point3D_F64> output ) {
		UtilPoint3D_F64.boundingBox(input,bounds);

		worldToOctree.getT().set( -bounds.p0.x , -bounds.p0.y , -bounds.p0.z );
		int sizeX = (int)Math.ceil(bounds.getLengthX()/desiredWidth);
		int sizeY = (int)Math.ceil(bounds.getLengthY()/desiredWidth);
		int sizeZ = (int)Math.ceil(bounds.getLengthZ()/desiredWidth);

		octree.initialize(new Box3D_I32(0,0,0,sizeX,sizeY,sizeZ));

		gridPoints.reset();

		for (int i = 0; i < input.size(); i++) {
			Point3D_F64 p = input.get(i);
			SePointOps_F64.transform(worldToOctree, p, local);
			Point3D_I32 g = gridPoints.grow();
			g.set((int)(local.x/desiredWidth),(int)(local.y/desiredWidth),(int)(local.z/desiredWidth));
			octree.addPoint(g,p);
		}

		FastQueue<Octree_I32> nodes = octree.getAllNodes();

		// Go through each node and only grab up to N points
		for (int i = 0; i < nodes.size(); i++) {
			Octree_I32 o = nodes.get(i);
			if( o.isSmallest() ) {
				int N = o.points.size();

				if( N <= desiredCount ) {
					for (int j = 0; j < o.points.size; j++) {
						Point3D_F64 p = o.points.get(j).getUserData();
						output.add(p);
					}
				} else {
					shuffleList.clear();
					for (int j = 0; j < N; j++) {
						shuffleList.add((Point3D_F64)o.points.get(j).getUserData());
					}
					Collections.shuffle(shuffleList,rand);
					for (int j = 0; j < desiredCount; j++) {
						output.add(shuffleList.get(j));
					}
				}
			}
		}
	}
}
