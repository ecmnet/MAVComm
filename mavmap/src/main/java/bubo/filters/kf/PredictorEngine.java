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

package bubo.filters.kf;

import org.ejml.data.DMatrixRMaj;

import static org.ejml.dense.row.CommonOps_DDRM.mult;
import static org.ejml.dense.row.CommonOps_DDRM.multAdd;

/**
 * This is useful if a predictor is being used to propagate a state forward in time, but
 * a kalman filter isn't needed.
 */
public class PredictorEngine {

	private DMatrixRMaj x;
	private DMatrixRMaj u;
	private KalmanPredictor pred;
	private DMatrixRMaj temp;

	public PredictorEngine(DMatrixRMaj x_init, KalmanPredictor pred) {
		this.temp = new DMatrixRMaj(pred.getNumStates(), 1);

		this.x = x_init.copy();
		this.pred = pred;
	}

	public PredictorEngine(DMatrixRMaj x_init,
						   DMatrixRMaj u,
						   KalmanPredictor pred) {
		this.temp = new DMatrixRMaj(pred.getNumStates(), 1);
		this.x = x_init.copy();
		this.u = u.copy();
		this.pred = pred;
	}

	public void predict() {

		DMatrixRMaj F = pred.getStateTransition();

		mult(F, x, temp);

		if (u != null) {
			DMatrixRMaj G = pred.getControlTransition();
			multAdd(G, u, temp);
		}

		// swap the references around
		DMatrixRMaj c = temp;
		temp = x;
		x = c;
	}

	public DMatrixRMaj getState() {
		return x;
	}

	public DMatrixRMaj getControl() {
		return u;
	}
}
