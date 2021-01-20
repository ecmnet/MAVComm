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

import java.util.Iterator;

/**
 * Interface for 64bit floating point 3D occupancy grid.  An occupancy grid stores the probability of a square region
 * being occupied by an obstacle or not.  A value of 1 means 100% and 0 means 0%, 50% is unknown or equal probability.
 *
 * The default value assigned to cells, which have not be explicitly assigned a value, is, by default, 0.5.  0.5 was
 * selected because it means it is not known if it is occupied or not.  The user can override this behavior by
 * calling {@link #setDefaultValue}.
 *
 * @author Peter Abeles
 */
public interface OccupancyGrid3D_F64 extends OccupancyGrid3D {
	/**
	 * Sets the specified cell to 'value'.
	 *
	 * @param x     x-coordinate of the cell.
	 * @param y     y-coordinate of the cell.
	 * @param value The cell's new value.
	 */
	public void set(int x, int y, int z, double value);

	/**
	 * Gets the value of the cell at the specified coordinate.
	 *
	 * @param x x-coordinate of the cell.
	 * @param y y-coordinate of the cell.
	 * @return The cell's value.
	 */
	public double get(int x, int y, int z);
	

	/**
	 * Checks to see if the provided value is within the valid range.
	 *
	 * @param value the value being tested
	 * @return if it is valid or not
	 */
	public boolean isValid(double value);

	/**
	 * Returns the value of grid cells which have not been explicitly assigned a value.
	 *
	 * @return value of unknown grid cells
	 */
	public double getDefaultValue();

	/**
	 * <p>
	 * Sets the default value used in the map.
	 * </p>
	 *
	 * <p>
	 * WARNING: How this affects the current map is undefined.  Depending on the data structure
	 * it might have an immediate effect or none at all.  To put the map back into a known state invoke
	 * {@link #clear()} which sets all elements in the grid to the default.
	 * </p>
	 *
	 * @param value The new value of unknown.
	 */
	public void setDefaultValue(double value);

	/**
	 * Returns an iterator, which will iterate through all maps cells which are not assigned a value of unknown.
	 * @return Iterator for map cells
	 */
	public Iterator<CellProbability_F64> iteratorKnown();
	
	/**
	 * Returns an iterator, which will iterate through all maps cells which are not assigned a value of unknown and
	 * are added since the timestamp tms
	 * @return Iterator for map cells
	 */
	public Iterator<CellProbability_F64> iteratorKnown(long tms);
	
	
	/**
	 * Returns an iterator, which will iterate through all maps cells which are not assigned a value of unknown and
	 * fullfil the filter criteria for the z axis
	 * @return Iterator for map cells
	 */
	public Iterator<CellProbability_F64> iteratorKnown(Comparable<Integer> zfilter);


	/**
	 * Creates a copy of this map
	 * @return copy of the map
	 */
	public OccupancyGrid3D_F64 copy();
	
	public int size();
}
