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

import bubo.maps.d2.grid.OccupancyGrid2D_I;

/**
 * Dense array floating point implementation of  OccupancyGrid2D_F32.
 *
 * @author Peter Abeles
 */
public class ArrayGrid2D_I8 extends ArrayGrid2DBase implements OccupancyGrid2D_I {

	public static int MAX = 255;
	public static byte UNKNOWN = (byte) 127;

	// grid map in a row major format
	private byte data[];

	public ArrayGrid2D_I8(int width, int height) {
		super(width, height);

		data = new byte[width * height];
	}

	@Override
	public void clear() {
		for (int i = 0; i < data.length; i++) {
			data[i] = UNKNOWN;
		}
	}

	@Override
	public void set(int x, int y, int value) {
		data[y * width + x] = (byte) value;
	}

	@Override
	public int get(int x, int y) {
		return data[y * width + x];
	}

	@Override
	public boolean isKnown(int x, int y) {
		return data[y * width + x] != UNKNOWN;
	}

	@Override
	public int getMaxValue() {
		return MAX;
	}

	@Override
	public int getUnknown() {
		return UNKNOWN & 0xFF;
	}

	@Override
	public boolean isValid(int value) {
		return value >= 0 && value <= 256;
	}

	@Override
	public OccupancyGrid2D_I copy() {
		ArrayGrid2D_I8 ret = new ArrayGrid2D_I8(width, height);

		System.arraycopy(data, 0, ret.data, 0, data.length);

		return ret;
	}
}
