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

import georegression.struct.point.Point3D_I32;

/**
 * Provides the location and probability of a cell in a 3D occupancy grid.
 *
 * @author Peter Abeles
 */
public class CellProbability_F64 extends Point3D_I32 {
	public double probability;
	public long   tms;

	public CellProbability_F64(CellProbability_F64 p) {
		super(p);
		this.probability = p.probability;
		this.tms = 0;
	}

	public CellProbability_F64() {
		super();
	}

	public double getProbability() {
		return probability;
	}
	
	public long getTms() {
		return tms;
	}
}
