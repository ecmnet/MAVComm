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

/**
 * A 3D occupancy grid stores the probability of a cube region being occupied by an obstacle or not. It is a
 * popular map format because of its simplicity.  The values contained in the map are always normalized.
 * For a floating point implementation it is normalized to be between 0 and 1 inclusive.  Integer based maps
 * typically are between 0 and the maximum unsigned value of the int.
 *
 * @author Peter Abeles
 */
public interface OccupancyGrid3D {

	/**
	 * Discards all map information and sets all cells to unknown.
	 */
	public void clear();

	/**
	 * Checks to see if the specified point is in bounds.
	 *
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param z z-coordinate
	 * @return true if the coordinate belongs to a valid cell in the map.
	 */
	public boolean isInBounds(int x, int y, int z);

	/**
	 * Checks to see if the specified location has the default value or not
	 *
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param z z-coordinate
	 * @return true if information is available on the cell.
	 */
	public boolean isDefault(int x, int y, int z);

	/**
	 * Length along x-axis.  All valid x-coordinates are between 0 and getSizeX()-1
	 *
	 * @return Length along x-axis.
	 */
	public int getSizeX();

	/**
	 * Length along y-axis.  All valid y-coordinates are between 0 and getSizeY()-1
	 *
	 * @return Length along y-axis.
	 */
	public int getSizeY();

	/**
	 * Length along z-axis.  All valid z-coordinates are between 0 and getSizeZ()-1
	 *
	 * @return Length along z-axis.
	 */
	public int getSizeZ();

	/**
	 * Creates a copy of this map
	 * @return copy of the map
	 */
	public OccupancyGrid3D copy();
}
