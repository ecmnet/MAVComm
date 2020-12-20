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

import bubo.filters.kf.KalmanPredictor;
import org.ejml.data.DMatrixRMaj;

/**
 * Checks to see if the delta time has changed, if so it recomputes everything, otherwise it
 * uses the previous value.
 */
public abstract class CacheKalmanPredictor<Control> implements KalmanPredictor<Control> {

	protected DMatrixRMaj tran;
	protected DMatrixRMaj control;
	protected DMatrixRMaj plant;
	private double prevDeltaTime = -1;

	public CacheKalmanPredictor(int stateDimen, int controlDimen) {
		tran = new DMatrixRMaj(stateDimen, stateDimen);
		plant = new DMatrixRMaj(stateDimen, stateDimen);
		control = new DMatrixRMaj(stateDimen, controlDimen);
	}

	public CacheKalmanPredictor(int stateDimen) {
		tran = new DMatrixRMaj(stateDimen, stateDimen);
		plant = new DMatrixRMaj(stateDimen, stateDimen);
	}

	@Override
	public void compute(Control control, double elapsedTime) {
		if (this.prevDeltaTime != elapsedTime) {
			_compute(elapsedTime);
			prevDeltaTime = elapsedTime;
		}
	}

	/**
	 * If the delta time is different this is called and the matrices should be recomputed
	 */
	protected abstract void _compute(double deltaTime);

	public DMatrixRMaj getStateTransition() {
		return tran;
	}

	public DMatrixRMaj getControlTransition() {
		return control;
	}

	public DMatrixRMaj getPlantNoise() {
		return plant;
	}

	public int getNumStates() {
		return tran.getNumCols();
	}

	@Override
	public int getNumControl() {
		if (control == null)
			return 0;
		else
			return control.getNumCols();
	}
}
