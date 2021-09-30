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
import bubo.filters.UtilMultivariateGaussian;
import bubo.filters.ekf.EkfPredictor;
import bubo.filters.ekf.ExtendedKalmanFilter;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;

/**
 * The following is a straight forward implementation of the Interacting Multiple Model (IMM) filter.
 * Pay close attention to the list of assumptions and restrictions below.  While very similar to the
 * original algorithm, the restrictions it imposes are excessive.
 * <p/>
 * A continuous discrete implementation of the IMM filter using a second order extended-Kalman
 * filter (EKF) internally.  Each of the models is assumed to have a state vector of the same type.
 * This means that each element in the state vector has the same meaning and the same number of
 * elements in each vector.  This makes the merging process much easier to implement.
 */
public class InteractingMultipleModelFilter<Control> {

	// the filter which is used to update the state of the internal models
	private ExtendedKalmanFilter<Control> filter;
	// creates the interaction matrix based upon the sensors measurement sojourn time
	private DMatrixRMaj markov;

	// an array of the internal models
	private EkfPredictor<Control> models[];

	private DMatrixRMaj c;

	// The DOF of the state for each one of the internal models.
	// note the all the models' state will have the same dimension/DOF.
	private int dimen;
	private DMatrixRMaj d;
	private DMatrixRMaj outer;

	/**
	 * Constructor used to create a new IMM filter.
	 *
	 * @param filter     The filter which will be used to process the internal models
	 * @param predictors The predictor each internal model will use
	 * @param markov     Used to compute the interaction matrix at each time step (reference is saved)
	 */
	public InteractingMultipleModelFilter(ExtendedKalmanFilter<Control> filter,
										  EkfPredictor<Control> predictors[],
										  DMatrixRMaj markov) {

		dimen = predictors[0].getSystemSize();
		final int N = predictors.length;

		// create one model for each dynamics model provided
		models = new EkfPredictor[N];
		System.arraycopy(predictors, 0, models, 0, N);

		this.markov = markov;
		this.filter = filter;

		c = new DMatrixRMaj(N, N);
		d = new DMatrixRMaj(dimen, 1);
		outer = new DMatrixRMaj(dimen, dimen);
	}

	protected InteractingMultipleModelFilter() {
	}

	/**
	 * Returns a reference to the filter that (after having its model swapped) will
	 * process the measurements and update the state estimates.
	 */
	public ExtendedKalmanFilter getFilter() {
		return filter;
	}

	/**
	 * Returns the DOF in the state vector
	 */
	public int getStateDOF() {
		return dimen;
	}

	/**
	 * Returns the number of models the filter internally uses.
	 */
	public int getNumModels() {
		return models.length;
	}

	/**
	 * Predict the state of the system deltaTime in the future.
	 * <p/>
	 * Before the state are propagated in the future the filter mixes
	 * the model estimates together.
	 */
	public void predict(ImmState state , Control control , double elapsedTime ) {
		ImmHypothesis hypotheses[] = state.hypotheses;

		// mix the states together
		mixing(state);

		// predict the state of each hypothesis and set the mixed state
		// to be the true state
		for (int i = 0; i < hypotheses.length; i++) {
			ImmHypothesis h = hypotheses[i];
			EkfPredictor<Control> model = models[i];
			filter.setPredictor(model);
			filter.predict(h.getMix(),control,elapsedTime);
			h.swapMix();
		}
	}

	/**
	 * Updates the state of each model with the measurement as well as the model probabilities
	 */
	public void update(ImmState state, MultivariateGaussianDM meas) {
		ImmHypothesis hypotheses[] = state.hypotheses;

//        System.out.println("IMM Update");
		double total = 0.0;

//        System.out.println("  measurement = "+meas.getMean().get(0,0));

		for (ImmHypothesis h : hypotheses) {
			// update it with the measurements
			filter.update(h.getState(), meas);

			// compute the likelihood
			DMatrixRMaj y = filter.getInnovation();
			DMatrixRMaj S = filter.getInnovationCov();
			DMatrixRMaj S_inv = filter.getInnovationCovInverse();

			double likelihood = UtilMultivariateGaussian.likelihoodP(y, S, S_inv);

			// If the probability goes to zero exactly then it can never recover and is essentially dead
			// Instead this lets it have a value which is almost zero
			double prob = Math.max(h.getProbability() * likelihood, Double.MIN_VALUE);
			total += prob;

//            System.out.printf("  pred pos = %8.2f like = %8.2f prob = %8.2f\n",predPos,likelihood,prob);

			h.setProbability(prob);
		}

		// now normalize so that its a probability
		for (ImmHypothesis h : hypotheses) {
			h.setProbability(h.getProbability() / total);
//            System.out.println("normalized prob = "+h.getProbability());
		}
	}

	/**
	 * Performs the mixing step in the IMM
	 */
	protected void mixing(ImmState state) {
		computeMixedProbability(state);
		computeMixedMean(state);
		computeMixedCovariance(state);
	}

	protected void computeMixedProbability(ImmState state) {
		ImmHypothesis hypotheses[] = state.hypotheses;

		for (int i = 0; i < hypotheses.length; i++) {
			double p = 0.0;

			ImmHypothesis m = hypotheses[i];

			for (int j = 0; j < hypotheses.length; j++) {
				p += markov.get(j, i) * hypotheses[j].getProbability();
			}
			m.setMixedProb(p);

			for (int j = 0; j < hypotheses.length; j++) {
				double c_element = markov.get(j, i) * hypotheses[j].getProbability() / m.getMixedProb();
				c.set(i, j, c_element);
			}
		}
	}

	protected void computeMixedMean(ImmState state) {
		ImmHypothesis hypotheses[] = state.hypotheses;

		for (int i = 0; i < hypotheses.length; i++) {
			DMatrixRMaj mix = hypotheses[i].getMix().getMean();
			mix.zero();

			for (int j = 0; j < hypotheses.length; j++) {
				DMatrixRMaj x_j = hypotheses[j].getState().getMean();

				CommonOps_DDRM.add(mix, c.get(i, j), x_j, mix);
			}
		}
	}

	protected void computeMixedCovariance(ImmState state) {
		ImmHypothesis hypotheses[] = state.hypotheses;

		for (int i = 0; i < hypotheses.length; i++) {
			ImmHypothesis m = hypotheses[i];
			DMatrixRMaj mixMean = m.getMix().getMean();
			DMatrixRMaj mixCov = m.getMix().getCovariance();
			mixCov.zero();

			for (int j = 0; j < hypotheses.length; j++) {
				ImmHypothesis m_j = hypotheses[j];
				d.setTo(mixMean);
				CommonOps_DDRM.add(d, -1, m_j.getMix().getMean(), d);
				CommonOps_DDRM.multTransB(d, d, outer);

				CommonOps_DDRM.add(outer, m_j.getState().getCovariance(), outer);

				CommonOps_DDRM.add(mixCov, c.get(i, j), outer, mixCov);
			}
		}
	}

	protected EkfPredictor<Control>[] getModels() {
		return models;
	}
}
