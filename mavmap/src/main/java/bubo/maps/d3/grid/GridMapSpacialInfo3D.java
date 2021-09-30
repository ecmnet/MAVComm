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

package bubo.maps.d3.grid;

import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Point3D_I32;
import georegression.struct.se.Se3_F64;
import georegression.transform.se.SePointOps_F64;

/**
 * Description of the 3D grid map's spacial information and its location in the global frame.
 * All cells have a constant size.  The origin of the map is at (0,0,0) grid cell.  The physical
 * space occupied by the grid cell at the origin is from (0,0,0) to (c,c,c), where c is the map's
 * cell size.  There are no negative coordinates along any axis in the map reference frame.
 *
 * For convenience, a coordinate transform is attached to this class which can be used
 * to convert coordinates to a more reasonable reference system.  For example, the canonical
 * could be a robot centered reference frame.
 *
 * @author Peter Abeles
 */
public class GridMapSpacialInfo3D {

	// size of a grid cell in global units
	private double cellSize;

	// transform from the center of the map to world
	private Se3_F64 mapToCanonical = new Se3_F64();

	public GridMapSpacialInfo3D(double cellSize, Se3_F64 mapToCanonical) {
		this.cellSize = cellSize;
		this.mapToCanonical.setTo(mapToCanonical);
	}

	public GridMapSpacialInfo3D() {
	}

	/**
	 * Converts a grid coordinate into a map coordinate using spacial information
	 */
	public void gridToMap( int x , int y , int z , Point3D_F64 map ) {
		map.x = x*cellSize;
		map.y = y*cellSize;
		map.z = z*cellSize;
	}

	/**
	 * Converts a map coordinate into a grid coordinate using spacial information
	 */
	public void mapToGrid( double x , double y , double z , Point3D_I32 map ) {
		// assumes (x,y,z) > 0.  Otherwise it won't floor it, but round to zero instead
		map.x = (int)(x/cellSize);
		map.y = (int)(y/cellSize);
		map.z = (int)(z/cellSize);
	}

	/**
	 * Convert from global coordinates into map cell coordinates.
	 */
	public void canonicalToMap(Point3D_F64 canonical, Point3D_F64 map) {
		SePointOps_F64.transformReverse(mapToCanonical,canonical,map);
	}

	/**
	 * Convert from map cell coordinates into global coordinates
	 */
	public void mapToCanonical(Point3D_F64 map, Point3D_F64 global) {
		SePointOps_F64.transform(mapToCanonical, map, global);
	}

	public double getCellSize() {
		return cellSize;
	}

	public Se3_F64 getMapToCanonical() {
		return mapToCanonical;
	}

	public void setCellSize(double cellSize) {
		this.cellSize = cellSize;
	}

	public void setMapToCanonical(Se3_F64 mapToCanonical) {
		this.mapToCanonical = mapToCanonical;
	}
}
