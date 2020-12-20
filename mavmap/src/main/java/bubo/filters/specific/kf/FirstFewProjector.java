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

package bubo.filters.specific.kf;

import bubo.filters.kf.KalmanProjector;
import org.ejml.data.DMatrixRMaj;

/**
 * This projector just reads the first few states
 */
public class FirstFewProjector implements KalmanProjector {

	private DMatrixRMaj H;

	public FirstFewProjector(int readNum, int stateDOF) {
		H = new DMatrixRMaj(readNum, stateDOF);

		for (int i = 0; i < readNum; i++) {
			H.set(i, i, 1.0);
		}
	}

	public int getNumStates() {
		return H.numRows;
	}

	public DMatrixRMaj getProjectionMatrix() {
		return H;
	}
}
