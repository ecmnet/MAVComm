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

import bubo.filters.MultivariateGaussianDM;
import bubo.filters.abst.KalmanFilterInterface;
import org.ejml.data.DMatrixRMaj;

import static org.ejml.dense.row.CommonOps_DDRM.*;

/**
 * <p>
 * Standard implementation of a Kalman filter.<br>
 * <br>
 * x<sub>k</sub> = F<sub>k</sub> x<sub>k-1</sub> + w<sub>k</sub><br>
 * z<sub>k</sub> = H<sub>k</sub> x<sub>k</sub> + v<sub>k</sub> <br>
 * <br>
 * w<sub>k</sub> ~ N(0,Q<sub>k</sub>)<br>
 * v<sub>k</sub> ~ N(0,R<sub>k</sub>)<br>
 * </p>
 * <p/>
 * <p>
 * To keep this class simple and readable only the essential computations are included.
 * Additional metrics that are needed for specific problems should be included else where.
 * <br>
 * Assumptions:
 * - The probagation time is variable
 * *not really needed, see predict()
 * - The DOF of the measurements is constant
 * * could be removed by moving matrices to the projector and making projectors specific to this algoritihm
 * </p>
 */
public class KalmanFilter<Control> extends DKFCommon implements KalmanFilterInterface<Control> {
	// describes how the state changes as a function of time
	private KalmanPredictor<Control> predictor;

	// the state to meas matrix
	private KalmanProjector projector;

	private DMatrixRMaj controlInput;

	public KalmanFilter(KalmanPredictor<Control> predictor, KalmanProjector projector) {
		super(predictor.getNumStates(), projector.getNumStates());

		this.predictor = predictor;
		this.projector = projector;
	}

	public void setControlInputRef(DMatrixRMaj controlInput) {
		this.controlInput = controlInput;
	}

	public DMatrixRMaj getControlInput() {
		return controlInput;
	}

	public void setControlInput(DMatrixRMaj controlInput) {
		this.controlInput = new DMatrixRMaj(controlInput);
	}

	/**
	 * This will predict the state of the system forward in time by the specified time step.
	 * <p/>
	 * The results of the prediction are stored in the 'state' variable.
	 * <p/>
	 * A Kalman filter does have an optional known control input.  This control rarely changes
	 * and by not having it as an input parameter this class can be swapped with an EKF.  To change
	 * the control input see the setControlInput() function.
	 *
	 * @param state The current state and covariance estimate
	 */
	@Override
	public void predict(MultivariateGaussianDM state, Control control, double elapsedTime) {

		predictor.compute(control, elapsedTime);

		DMatrixRMaj F = predictor.getStateTransition();
		DMatrixRMaj Q = predictor.getPlantNoise();

		DMatrixRMaj G = predictor.getControlTransition();

		DMatrixRMaj x = state.getMean();
		DMatrixRMaj P = state.getCovariance();

		// predict the state
		mult(F, x, a);
		x.set(a);
		// note: this could be made slightly more efficient at the cost of NASTY bugs by swapping
		// the references for x and a

		// handle the control, if there is one
		if (G != null) {
			mult(G, controlInput, a);
			addEquals(x, a);
		}

		_predictCovariance(F, Q, P);
	}

	/**
	 * Just adds the change caused by the control to the specified state.  This
	 * assumes that predictor.compute() has already been called with the appropriate
	 * deltaTime.
	 */
	public void addControl(MultivariateGaussianDM state) {
		DMatrixRMaj G = predictor.getControlTransition();
		DMatrixRMaj x = state.getMean();

		mult(G, controlInput, a);
		addEquals(x, a);
	}

	@Override
	public void update(MultivariateGaussianDM state, MultivariateGaussianDM meas) {
		DMatrixRMaj H = projector.getProjectionMatrix();

		DMatrixRMaj x = state.getMean();
		DMatrixRMaj P = state.getCovariance();

		DMatrixRMaj z = meas.getMean();
		DMatrixRMaj R = meas.getCovariance();

		// compute the residual
		mult(H, x, y);
		subtract(z, y, y);

		_updateCovariance(H, x, P, R);
	}

	public KalmanProjector getProjector() {
		return projector;
	}

	public KalmanPredictor getPredictor() {
		return predictor;
	}
}
