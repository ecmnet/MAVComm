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

package bubo.maps.d3.grid.impl;

import bubo.construct.Octree_I32;
import bubo.maps.UtilMaps;
import georegression.struct.point.Point3D_I32;
import georegression.struct.shapes.Box3D_I32;
import org.ddogleg.struct.DogArray;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Class for applying 3D kernels to {@link bubo.maps.d3.grid.impl.OctreeGridMap_F64} which
 * takes advantage of the maps sparse data structure.  Any {@link bubo.maps.d3.grid.impl.Kernel3D_F64}
 * can be used.
 * </p>
 *
 * <p>
 * It is assumed that most of the neighbor nodes surrounding a 'known' node in the input have
 * a value of 'defaultValue'.  This type of sparse structure is taken advantage of by having each node with
 * a known value push its information on to its neighbors.  This is a re-ordering of the usual convolution
 * computation.
 * </p>
 * <p>
 * NOTE: If a cell is along the map's border cells outside the map will be treated as having a value
 * of 'defaultValue'.  A better way would be to reweigh it based on the cells which are inside the image.
 * </p>
 * @author Peter Abeles
 */
public class BlurOctreeGridMap_F64 {

	// internal work space
	protected List<Octree_I32> neighbors = new ArrayList<Octree_I32>();
	protected List<Octree_I32> ignore = new ArrayList<Octree_I32>();
	protected Box3D_I32 box = new Box3D_I32();

	// stores information on the computed sum for convolution
	protected DogArray<SumData> convData = new DogArray<SumData>(SumData::new);

	/**
	 * Applies the kernel to the map while taking advantage of its sparsity.
	 *
	 * @param input (Input) Input map.  Not modified.
	 * @param kernel (Input) Blur kernel. Must sum to one.  Not modified.
	 * @param blurred (Output) Storage for blurred map.  blurred.clear() is called.
	 */
	public void apply( OctreeGridMap_F64 input , Kernel3D_F64 kernel , OctreeGridMap_F64 blurred ) {
		if( !UtilMaps.sameShape(input, blurred))
			throw new IllegalArgumentException("Maps must be the same shape!");

		blurred.clear();
		convData.reset();

		// Construct the blurred map and do a sparse convolution
		createBlurredCells(input, kernel, blurred);

		// use previously computed convolution information to compute the value of each
		// cell in the blurred image
		computeProbability(blurred);
	}

	/**
	 * Creates grid map cells in the blurred image around the coordinates of map cells in the input map which
	 * have been assigned a value other than 'defaultValue'
	 */
	protected void createBlurredCells(OctreeGridMap_F64 input, Kernel3D_F64 kernel, OctreeGridMap_F64 blurred) {
		DogArray<Octree_I32> list = input.getConstruct().getAllNodes();

		int radius = kernel.radius;
		for (int i = 0; i < list.size; i++) {
			Octree_I32 o = list.get(i);
			if (o.userData == null || !o.isLeaf() || !o.isSmallest())
				continue;

			// set the box around it
			box.p0.setTo(o.space.p0);
			box.p0.x -= radius;
			box.p0.y -= radius;
			box.p0.z -= radius;

			box.p1.setTo(o.space.p0);
			box.p1.x += radius + 1;
			box.p1.y += radius + 1;
			box.p1.z += radius + 1;

			// create grid cells in blurred image around the location of 'o'
			blurred.getConstruct().addLeafsIntersect(box, neighbors, ignore);

			applyToNeighbors(kernel, o);
		}
	}

	/**
	 * Apply the probability to all the neighbors of 'o' in the blurred image.  The weight given
	 * to each neighbor is the weight from the kernel centered around it.  This is more efficient
	 * that picking a point and convolving around it since only a few points actually have
	 * values which aren't 'defaultValue'.
	 */
	protected void applyToNeighbors(Kernel3D_F64 kernel, Octree_I32 o) {

		int radius = kernel.getRadius();
		int width = kernel.getWidth()-1;
		Point3D_I32 p = o.getLocation();
		double probability = ((MapLeaf)o.getUserData()).probability;
		for (int j = 0; j < neighbors.size(); j++) {
			Octree_I32 neighbor = neighbors.get(j);
			Point3D_I32 n = neighbor.getLocation();

			// sample the value from the kernel when it is centered around the neighbor
			double weight = kernel.get(
					width-(p.x-n.x+radius),
					width-(p.y-n.y+radius),
					width-(p.z-n.z+radius));

			// apply the blue
			SumData data = neighbor.getUserData();
			if( data == null ) {
				neighbor.userData = data = convData.grow();
				data.reset();
			}
			data.total += weight*probability;
			data.weight += weight;
		}
	}

	/**
	 * Now that all off the non-unknown nodes have applied their probabilities to their neighbors,
	 * compute the probability for each non-unknown cell in the blurred image.  This takes in
	 * account that all cells which did not contribute to the convolution will have a value of 'defaultValue'
	 */
	protected static void computeProbability(OctreeGridMap_F64 blurred) {
		DogArray<Octree_I32> list = blurred.getConstruct().getAllNodes();
		for (int i = 0; i < list.size; i++) {
			Octree_I32 o = list.get(i);
			if ( o.userData == null || !o.isLeaf() || !o.isSmallest())
				continue;

			SumData blur = o.getUserData();

			if( blur.weight > 1.0 )
				throw new RuntimeException("BUG!!  Weight should be <= 1");

			// take in account all the unknown cells surrounding it
			// NOTE: This will effectively treat cells outside the map as having a value of 'defaultValue'
			// the kernel is normalized so that it's sum will be one
			MapLeaf data = blurred.info.grow();
			data.probability = blur.total + blurred.getDefaultValue()*(1.0-blur.weight);
			// replace the  blur data with the usual data structure
			o.userData = data;
		}
	}
}
