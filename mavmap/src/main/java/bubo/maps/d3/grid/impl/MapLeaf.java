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

/**
 * Internal storage used by {@link bubo.maps.d3.grid.impl.OctreeGridMap_F64} for saving the value of a map
 * cell.
 *
 * @author Peter Abeles
 */
public class MapLeaf {
	public double   probability;
	public long     tms;

	public MapLeaf(double probability, long tms) {
		this.probability = probability;
		this.tms = tms;
	}

	public MapLeaf() {
		
	}
}
