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

package bubo.maps.d2.grid.impl;

import bubo.maps.d2.grid.OccupancyGrid2D_F32;
import bubo.maps.d2.grid.OccupancyGrid2D_I;

/**
 * Takes an integer occupancy grid map and converts its input and output into a floating point map.
 *
 * @author Peter Abeles
 */
public class WrapOccupancy2D_I_to_F32 implements OccupancyGrid2D_F32 {

	private OccupancyGrid2D_I map;

	public WrapOccupancy2D_I_to_F32(OccupancyGrid2D_I map) {
		this.map = map;
	}

	@Override
	public void set(int x, int y, float value) {
		map.set(x, y, floatToInt(value));
	}

	@Override
	public float get(int x, int y) {
		return intToFloat(map.get(x, y));
	}

	@Override
	public boolean isValid(float value) {
		return value >= 0f && value <= 1.0f;
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public boolean isInBounds(int x, int y) {
		return map.isInBounds(x, y);
	}

	@Override
	public boolean isKnown(int x, int y) {
		return map.isKnown(x, y);
	}

	@Override
	public int getWidth() {
		return map.getWidth();
	}

	@Override
	public int getHeight() {
		return map.getHeight();
	}

	/**
	 * Converts a normalized floating point number that is between 0 and 1 inclusive and converts it into
	 * an integer value normalized for the integer map.
	 *
	 * @param value float between 0 and 1 inclusize
	 * @return equivalent int value
	 */
	public int floatToInt(float value) {
		return (int) (map.getMaxValue() * value);
	}

	public float intToFloat(int value) {
		return (float) value / (float) map.getMaxValue();
	}
}
