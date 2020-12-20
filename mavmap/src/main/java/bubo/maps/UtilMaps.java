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

package bubo.maps;

import bubo.maps.d3.grid.OccupancyGrid3D;

/**
 * @author Peter Abeles
 */
public class UtilMaps {
	/**
	 * Checks to see if the maps are the same shape.
	 * @param a map
	 * @param b map
	 * @return true if they are the same shape or false if not
	 */
	public static boolean sameShape( OccupancyGrid3D a , OccupancyGrid3D b ) {
		return a.getSizeX() == b.getSizeX() && a.getSizeY() == b.getSizeY() & a.getSizeZ() == b.getSizeZ();
	}
}
