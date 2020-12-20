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

package bubo.filters.imm;

import org.ejml.data.DMatrixRMaj;

/**
 * Computes an interaction matrix for the IMM filter.  The values in the interaction matrix depend on
 * the time since the last observation.
 */
public interface InteractionMatrixInterface {
	/**
	 * Computes the interaction matrix.
	 *
	 * @param deltaTime The time since the previous observation.
	 */
	public DMatrixRMaj computeMatrix(double deltaTime);
}
