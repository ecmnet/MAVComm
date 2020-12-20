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

package bubo.filters.hinf;

import bubo.filters.MultivariateGaussianDM;
import bubo.filters.abst.KalmanFilterInterface;
import bubo.filters.kf.KalmanPredictor;
import bubo.filters.kf.KalmanProjector;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;

/**
 * An implementation of the discrete-time H Infinity filter.  This filter is designed to
 * be more robust than the Kalman filter and make less assumptions.  If everything is known
 * it tends to perform worse than a kalman filter.  If there is modeling errors or unknown
 * parameters it tends to perform better.
 * <p/>
 * "Optimal State Estimation" by Dan Simon. page 353
 * <p/>
 * This implementation is designed to be easily dropped into problem a Kalman filter
 * would be dropped into.  A more generic implementation would allow more of the
 * user specified variables to be user specified.  Essentially this makes stronger
 * assumptions about the system.
 * <p/>
 * System equations:
 * <p/>
 * x(k+1) = F(k)w(k) + w(k)
 * y(k) = H(k)x(k) + v(k)
 * z(k) = L(k)x(k)
 * <p/>
 * where w(k) and v(k) are noise terms.  Note the type of noise and other parameters are not
 * specified for w(k) and v(k) as they are in a Kalman filter.
 * <p/>
 * ------------------------------------------------
 */
public class DiscreteHInfinityFilter<Control> implements KalmanFilterInterface<Control> {
	// describes how the state changes as a function of time
	private KalmanPredictor predictor;

	// the state to meas matrix
	private KalmanProjector projector;

	/**
	 * \bar{s} = L^T S L
	 */
	private DMatrixRMaj S_bar;

	/**
	 * A user defined performance bound
	 */
	private double theta;

	private DMatrixRMaj I_z;
	private DMatrixRMaj I_x;
	private DMatrixRMaj R_inv;
	private DMatrixRMaj temp0_m_m;
	private DMatrixRMaj D;
	private DMatrixRMaj temp0_m_n;
	private DMatrixRMaj K;
	private DMatrixRMaj temp0_m_1;
	private DMatrixRMaj innovation;

	private DMatrixRMaj control;

	/**
	 * L is a user defined full rank matrix.  If we want to estimate the full state (like in a kalman
	 * filter) it is set to the identity matrix.
	 * <p/>
	 * S is a symmetric positive definte matrix (n by n) n = DOF of meas
	 * it is used to weight the importance of measurement parameters
	 * if an element in the measurement is more important then the elements
	 * corresponding to it should be larger
	 * <p/>
	 * theta is a user defined performance bound
	 * <p/>
	 * Both L and S are assumed to be constant and should not be changed.
	 *
	 * @param predictor describes how the state evolves as a function of time.  Saved internally.
	 * @param projector describes how measurements are taken of the system. Saved internally.
	 * @param control   An optional control input.  Can be set to null if there is none
	 * @param S         measurement estimation importance matrics.  Not saved internally.
	 * @param L         State estimation  matrix.  Not saved internally.
	 * @param theta     performance bound.
	 */
	public DiscreteHInfinityFilter(KalmanPredictor predictor, KalmanProjector projector,
								   DMatrixRMaj control,
								   DMatrixRMaj S, DMatrixRMaj L, double theta) {
		this.predictor = predictor;
		this.projector = projector;
		this.theta = theta;

		if (control != null) {
			this.control = control.copy();
		}

		int m = predictor.getNumStates();
		int n = projector.getNumStates();

		I_z = CommonOps_DDRM.identity(n);
		I_x = CommonOps_DDRM.identity(m);
		R_inv = new DMatrixRMaj(n, n);
		temp0_m_m = new DMatrixRMaj(m, m);
		D = new DMatrixRMaj(m, m);
		temp0_m_n = new DMatrixRMaj(m, n);
		K = new DMatrixRMaj(m, n);
		temp0_m_1 = new DMatrixRMaj(m, 1);
		innovation = new DMatrixRMaj(n, 1);

		S_bar = new DMatrixRMaj(m, m);
		CommonOps_DDRM.multAddTransA(L, S, temp0_m_m);
		CommonOps_DDRM.mult(temp0_m_m, L, S_bar);
	}

	@Override
	public void predict(MultivariateGaussianDM state, Object o, double elapsedTime) {

		DMatrixRMaj x = state.getMean();
		DMatrixRMaj P = state.getCovariance();

		DMatrixRMaj F = predictor.getStateTransition();
		DMatrixRMaj Q = predictor.getPlantNoise();

		CommonOps_DDRM.mult(F, x, temp0_m_1);
		x.set(temp0_m_1);

		if (control != null) {
			DMatrixRMaj G = predictor.getControlTransition();

			CommonOps_DDRM.mult(G, control, temp0_m_1);
			CommonOps_DDRM.add(x, temp0_m_1, x);
		}

		CommonOps_DDRM.mult(F, P, temp0_m_m);
		CommonOps_DDRM.multTransB(temp0_m_m, F, P);
		CommonOps_DDRM.add(P, Q, P);
	}

	@Override
	public void update(MultivariateGaussianDM state,
					   MultivariateGaussianDM meas) {
		DMatrixRMaj x = state.getMean();
		DMatrixRMaj P = state.getCovariance();
		DMatrixRMaj H = projector.getProjectionMatrix();
		DMatrixRMaj R = meas.getCovariance();
		DMatrixRMaj y = meas.getMean();

		// compute D
		CommonOps_DDRM.mult(-theta, S_bar, P, D);

		CommonOps_DDRM.invert(R, R_inv);

		CommonOps_DDRM.multTransA(H, R_inv, temp0_m_n);
		CommonOps_DDRM.mult(temp0_m_n, H, temp0_m_m);
		CommonOps_DDRM.mult(temp0_m_m, P, D);

		for (int i = 0; i < D.getNumCols(); i++)
			D.set(i, i, D.get(i, i) + 1);

		CommonOps_DDRM.invert(D, temp0_m_m);
		DMatrixRMaj PD_inv = D;
		CommonOps_DDRM.mult(P, temp0_m_m, PD_inv);

		// compute K
		CommonOps_DDRM.multTransB(PD_inv, H, temp0_m_n);
		CommonOps_DDRM.mult(temp0_m_n, R_inv, K);

		// innovation
		innovation.set(y);
		CommonOps_DDRM.multAdd(-1, H, x, innovation);

		// update the state
		CommonOps_DDRM.mult(K, innovation, temp0_m_1);
		CommonOps_DDRM.add(x, temp0_m_1, x);

		// update the covariance
		P.set(PD_inv);
	}

	public KalmanProjector getProjector() {
		return projector;
	}

	public KalmanPredictor getPredictor() {
		return predictor;
	}

	public DMatrixRMaj getInnovation() {
		return innovation;
	}

	public int getStateDOF() {
		return predictor.getNumStates();
	}
}
