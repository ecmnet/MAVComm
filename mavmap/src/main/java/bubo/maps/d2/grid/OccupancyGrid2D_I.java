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

package bubo.maps.d2.grid;

/**
 * Interface for an integer point occupancy grid.  An occupancy grid stores the probability of a square region
 * being occupied by an obstacle or not.  A value of MAX is 100% chance of there being an obstacle while 0 indicates
 * a probability of 0%.  Both byte,short, and int based maps all use this interface.  Java locally converts variables
 * into ints so there is little reason to return anything other than ints.  It also makes it easier for all variables
 * to be "unsigned".
 *
 * @author Peter Abeles
 */
public interface OccupancyGrid2D_I extends OccupancyGrid2D {

	/**
	 * Sets the specified cell to 'value'.
	 *
	 * @param x     x-coordinate of the cell.
	 * @param y     y-coordinate of the cell.
	 * @param value The cell's new value.
	 */
	public void set(int x, int y, int value);

	/**
	 * Gets the value of the cell at the specified coordinate.
	 *
	 * @param x x-coordinate of the cell.
	 * @param y y-coordinate of the cell.
	 * @return The cell's value.
	 */
	public int get(int x, int y);

	/**
	 * The largest value a cell can have.
	 *
	 * @return Largest value.
	 */
	public int getMaxValue();

	/**
	 * The value which means there is equal probability of it being occupied/unoccupied
	 *
	 * @return value of unknown
	 */
	public int getUnknown();

	/**
	 * Checks to see if the provided value is within the valid range.
	 *
	 * @param value the value being tested
	 * @return if it is valid or not
	 */
	public boolean isValid(int value);

	/**
	 * Creates an exact copy of 'this' map.
	 *
	 * @return A copy of 'this' map
	 */
	public OccupancyGrid2D_I copy();

}
