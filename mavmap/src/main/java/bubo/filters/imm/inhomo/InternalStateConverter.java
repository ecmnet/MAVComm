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

package bubo.filters.imm.inhomo;

import org.ejml.data.DMatrixRMaj;

/**
 * Used to convert between one internal state to another internal state for perposes of merging
 * hypotheses.
 * <p/>
 * For now there must be a way to convert between the formats that results in an equivalent
 * state vector.  A more generalized approach is possible where only some of the state vector
 * elements are merged.  This is more complex algorithmically, so its not being done now.
 */
public interface InternalStateConverter {
	/**
	 * Information from the 'from' state vector is being merged into the 'target' state vector.
	 * <p/>
	 * The output state vector format is the same as the 'target' state vector.
	 * <p/>
	 * If no change is needed the same state vector can be returned from what was provided to it.
	 */
	public DMatrixRMaj convertMergeFrom(boolean isMean, DMatrixRMaj fromState,
										   int fromID, int targetID);

	/**
	 * Converts the specified input type into the same format used for output.
	 * <p/>
	 * If no change is needed the same state vector can be returned from what was provided to it.
	 */
	public DMatrixRMaj convertOutput(boolean isMean, DMatrixRMaj fromtate, int fromID);

	/**
	 * Returns the dimension of the output state vector
	 */
	int getOutputDimen();
}
