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
import org.ejml.UtilEjml;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;

/**
 * The estimated state of an IMM filter
 */
public class ImmState {

	ImmHypothesis hypotheses[];

	MultivariateGaussianDM stateMOG;

	private DMatrixRMaj d;
	private DMatrixRMaj outer;

	public ImmState(InteractingMultipleModelFilter<?> filter) {
		EkfPredictor[] models = filter.getModels();

		hypotheses = new ImmHypothesis[models.length];
		for (int i = 0; i < models.length; i++) {
			hypotheses[i] = new ImmHypothesis(models[i]);
		}

		int dimen = models[0].getSystemSize();

		// predeclare matrices that will be used later on
		d = new DMatrixRMaj(dimen, 1);
		outer = new DMatrixRMaj(dimen, dimen);

		stateMOG = new MultivariateGaussianDM(dimen);
	}

	protected ImmState() {
	}

	/**
	 * Returns the information on a specific internal hypothesis for the IMM
	 */
	public ImmHypothesis getHypothesis(int index) {
		return hypotheses[index];
	}

	/**
	 * Returns the model specific estimated state.
	 */
	public MultivariateGaussianDM getModelState(int modelIndex) {
		return hypotheses[modelIndex].getState();
	}

	/**
	 * Sets the state of the filter to the specified values.  Each model will have the
	 * specified state and the coresponding probability.
	 */
	public void setState(MultivariateGaussianDM state, double[] probs) {
		double total = 0.0;
		for (int i = 0; i < hypotheses.length; i++) {
			hypotheses[i].setStateValue(state, probs[i]);
			total += probs[i];
		}

		// perform a quick check to see if the preconditions of probability are met
		if (Math.abs(total - 1.0) > UtilEjml.EPS) {
			throw new IllegalArgumentException("The total probability does not add up to 1.  Instead it is " + total);
		}
	}

	/**
	 * Computes a mixture of Gaussians (MOG) from the models.
	 */
	public MultivariateGaussianDM computeMOG() {
		DMatrixRMaj x_ret = stateMOG.getMean();
		DMatrixRMaj P_ret = stateMOG.getCovariance();

		x_ret.zero();
		P_ret.zero();

//        System.out.println("compute MOG");
		// compute the mean
		for (ImmHypothesis h : hypotheses) {
//            System.out.println("  model prob = "+h.getProbability());
			CommonOps_DDRM.add(x_ret, h.getProbability(), h.getState().getMean(), x_ret);
		}

		// compute the covariance
		for (ImmHypothesis h : hypotheses) {
			d.set(h.getState().getMean());
			CommonOps_DDRM.add(d, -1, x_ret, d);

			CommonOps_DDRM.multTransB(d, d, outer);
			CommonOps_DDRM.add(outer, h.getState().getCovariance(), outer);

			CommonOps_DDRM.add(P_ret, h.getProbability(), outer, P_ret);
		}

		return stateMOG;
	}

	/**
	 * Returns the previously computed MOG state
	 */
	public MultivariateGaussianDM _getStateMOG() {
		return stateMOG;
	}
}
