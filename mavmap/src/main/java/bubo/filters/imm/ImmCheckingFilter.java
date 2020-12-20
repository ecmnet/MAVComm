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

import bubo.filters.MultivariateGaussianDM;
import bubo.filters.ekf.EkfPredictor;
import bubo.filters.ekf.ExtendedKalmanFilter;
import org.ejml.UtilEjml;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CovarianceOps_DDRM;
import org.ejml.dense.row.MatrixFeatures_DDRM;

/**
 * This is a wrapper on top of the IMM filter which provides several additional checks
 * that ensure everything is working inside the filter.  All the model probabilites must add
 * up to one, state transition matrix must pass a sanity check, and the state and covariance matrices
 * need to be valid.
 * <p/>
 * In a developmental environment where a small performance hit is not essential this
 * might be preferable to the orignal filter.
 */
public class ImmCheckingFilter<Control> extends InteractingMultipleModelFilter<Control> {

	public ImmCheckingFilter(ExtendedKalmanFilter<Control> filter,
							 EkfPredictor<Control>[] propagators,
							 DMatrixRMaj interaction) {
		super(filter, propagators, interaction);

		isValidMarkovMatrix(interaction);
	}

	/**
	 * Makes sure this is a valid Markov state transition matrix.  Checks
	 * to see if the rows add up to one.
	 */
	protected static boolean isValidMarkovMatrix(DMatrixRMaj pi) {
		// make sure the Markov matrix is correctly constructed
		for (int i = 0; i < pi.numRows; i++) {
			// a row should add up to one.
			// pi(i,j) is the probability of model i transitioning to model j
			double total = 0.0;
			for (int j = 0; j < pi.numCols; j++) {
				total += pi.get(i, j);
			}
			if (Math.abs(total - 1.0) > UtilEjml.EPS)
				return false;
		}

		return true;
	}

	/**
	 * Makes sure the model probabilities add up to one and that the state/covariance matrices
	 * don't have invalid values inside of them
	 */
	protected static void sanityCheckFilter(ImmHypothesis[] hypotheses) {

		double totalProb = 0.0;

		for (ImmHypothesis m : hypotheses) {
			totalProb += m.getProbability();

			MultivariateGaussianDM s = m.getState();

			if (MatrixFeatures_DDRM.hasUncountable(s.getMean())) {
				throw new SanityCheck("Bad model state");
			}

			if (!CovarianceOps_DDRM.isValidFast(s.getCovariance())) {
				throw new SanityCheck("Bad model covariance");
			}
		}

		if (Math.abs(totalProb - 1.0) > UtilEjml.EPS) {
			throw new SanityCheck("The model probabilities don't add up to one.");
		}
	}

	@Override
	public void predict(ImmState state , Control control , double elapsedTime ) {
		super.predict(state,control,elapsedTime);
		sanityCheckFilter(state.hypotheses);
	}

	@Override
	public void update(ImmState state, MultivariateGaussianDM meas) {
		super.update(state, meas);
		sanityCheckFilter(state.hypotheses);
	}

	/**
	 * An exception to indicate that a sanity check failed in the IMM.
	 */
	public static class SanityCheck extends RuntimeException {
		public SanityCheck(String message) {
			super(message);
		}
	}

}
