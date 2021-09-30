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

import bubo.construct.ConstructOctreeLeaf_I32;
import bubo.construct.OctreeOps;
import bubo.construct.Octree_I32;
import bubo.maps.d3.grid.CellProbability_F64;
import bubo.maps.d3.grid.OccupancyGrid3D_F64;
import georegression.metric.Intersection3D_I32;
import georegression.struct.point.Point3D_I32;
import georegression.struct.shapes.Box3D_I32;

import org.ddogleg.struct.DogArray;
import org.ddogleg.struct.FastArray;


import java.util.Iterator;
import java.util.List;

/**
 * Creates a 3D map using an Octree.  This is a sparse data structure which is in most situations much more
 * efficient at storing 3D maps than a raw 3D array.  The price paid is that reading and writing to the
 * octree is more expensive.
 *
 * Internally, the probability is stored in {@link MapLeaf} in the {@link bubo.construct.Octree_I32}'s user
 * data parameter.  If a graph node doesn't exist or has no user data assigned to it then it is assumed to
 * have the value of the 'defaultValue' parameter.
 *
 * @see ConstructOctreeLeaf_I32
 *
 * @author Peter Abeles
 */
public class OctreeGridMap_F64 implements OccupancyGrid3D_F64 {

	// value of cells with no information
	double defaultValue = 0.5;

	// constructs and maintains the octree
	ConstructOctreeLeaf_I32 construct;

	// storage for map info which is placed in each leaf
	DogArray<MapLeaf> info = new DogArray<MapLeaf>(MapLeaf::new);

	// used to temporarily store a point's value when looking things up
	Point3D_I32 temp = new Point3D_I32();

	// describes the area which the map is contained inside of
	Box3D_I32 region;

	/**
	 * Creates a new map based on the users request.  The actual map size is adjusted to ensure
	 * that the leaf cells are the specified size
	 *
	 * @param lengthX Number of map cells along x-axis
	 * @param lengthY Number of map cells along y-axis
	 * @param lengthZ Number of map cells along z-axis
	 */
	public OctreeGridMap_F64( int lengthX , int lengthY , int lengthZ ) {
		this.region = new Box3D_I32(0,0,0,lengthX,lengthY,lengthZ);

		construct = new ConstructOctreeLeaf_I32();
		construct.initialize(region);
	}

	@Override
	public void set(int x, int y, int z, double value) {
		temp.setTo(x,y,z);

		Octree_I32 leaf = construct.addLeaf(temp);
		if(leaf == null)
			return;
		
		MapLeaf info;
		if( leaf.userData == null ) {
			info = this.info.grow();
			leaf.userData = info;
		} else {
			info = (MapLeaf)leaf.userData;
		}
		info.probability = value;
		info.tms = System.currentTimeMillis();
	}

	@Override
	public double get(int x, int y, int z) {
		temp.setTo(x,y,z);
		Octree_I32 node = construct.getTree().findDeepest(temp);
		if( node == null || node.userData == null )
			return defaultValue;
		else
			return ((MapLeaf)node.userData).probability;
	}

	@Override
	public boolean isValid(double value) {
		return value >= 0 && value <= 1;
	}

	@Override
	public double getDefaultValue() {
		return defaultValue;
	}

	@Override
	public void setDefaultValue(double value) {
		defaultValue = value;
	}

	@Override
	public Iterator<CellProbability_F64> iteratorKnown() {
		return new OctIterator();
	}

	@Override
	public Iterator<CellProbability_F64> iteratorKnown(long tms) {
		return new OctIterator(tms);
	}

	@Override
	public Iterator<CellProbability_F64> iteratorKnown(Comparable<Integer> zfilter) {
		return new OctIterator(zfilter);
	}

	@Override
	public OccupancyGrid3D_F64 copy() {
		OccupancyGrid3D_F64 ret = new OctreeGridMap_F64(getSizeX(),getSizeY(),getSizeZ());

		Iterator<CellProbability_F64> iter = iteratorKnown();
		while( iter.hasNext() ) {
			CellProbability_F64 p = iter.next();
			ret.set(p.x,p.y,p.z,p.probability);
		}

		return ret;
	}

	@Override
	public void clear() {
		construct.reset();
		info.reset();
	}

	@Override
	public boolean isInBounds(int x, int y, int z) {
		temp.setTo(x,y,z);
		return Intersection3D_I32.contains(region,temp);
	}

	@Override
	public boolean isDefault(int x, int y, int z) {
		temp.setTo(x,y,z);
		Octree_I32 node = construct.getTree().findDeepest(temp);
		if( node != null && node.isLeaf() && node.isSmallest() ) {
			MapLeaf info = node.getUserData();
			return info.probability != defaultValue;
		}
		return false;
	}

	@Override
	public int getSizeX() {
		return region.getLengthX();
	}

	@Override
	public int getSizeY() {
		return region.getLengthY();
	}

	@Override
	public int getSizeZ() {
		return region.getLengthZ();
	}

	public ConstructOctreeLeaf_I32 getConstruct() {
		return construct;
	}

	public int size() {
		return info.size();
	//	return construct.getAllNodes().size();
	}

	/**
	 * Returns all grid cells which have been assigned values as {@link bubo.construct.Octree_I32} nodes.
	 * @return List of all occupied cells
	 */
	public List<Octree_I32> getGridCells() {
		return OctreeOps.findLeafsWithData(construct.getAllNodes().toList(), null);
	}

	/**
	 * Iterator which will go through all the map cells.  This is defined as nodes in the graph which are
	 * the smallest size possible and have been assigned a probability
	 */
	private class OctIterator implements Iterator<CellProbability_F64> {

		DogArray<Octree_I32> nodes = construct.getAllNodes();
		long tms;
		int index;
		Comparable<Integer> zfilter = null;

		Octree_I32 next;
		CellProbability_F64 storage = new CellProbability_F64();
		
		public OctIterator() {
			this.tms = 0;
			searchNext();
		}

		public OctIterator(long tms) {
			this.tms = tms;
			searchNext();
		}

		public OctIterator(Comparable<Integer> zfilter) {
			this.zfilter = zfilter;
			searchNext();
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public CellProbability_F64 next() {
			Octree_I32 prev = next;
			searchNext();
			MapLeaf info = prev.getUserData();
			storage.setTo( prev.space.p0 );
			storage.probability = info.probability;
			storage.tms         = info.tms;

			return storage;
		}

		protected void searchNext() {
			next = null;
			while( index < nodes.size() ) {
				Octree_I32 o = nodes.get(index++);
				if( o.isSmallest()) {
					MapLeaf info = o.getUserData();
					if(zfilter==null) {
						if (info != null && info.probability != 0.5f && info.tms > tms) {
							next = o;
							break;
						}
					} else {
						if (info != null && info.probability > 0.5f && info.tms > tms && zfilter.compareTo(o.space.p0.z) == 0) {
							next = o;
							break;
						}
					}
				}
			}
		}

		@Override
		public void remove() {
			throw new RuntimeException("Remove is not supported");
		}
	}
}
