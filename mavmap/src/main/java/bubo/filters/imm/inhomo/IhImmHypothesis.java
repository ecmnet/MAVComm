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

import bubo.filters.MultivariateGaussianDM;
import bubo.filters.ekf.ExtendedKalmanFilter;

/**
 * Adds a projector to the ImmHypothesis.  This is needed since the state vectors can be
 * of different types.
 */
public class IhImmHypothesis {
	private MultivariateGaussianDM state;
	private double probability;
	// probability computed durring mixing
	private double mixProb;
	// temporary state used durring mixing
	private MultivariateGaussianDM mix;

	public IhImmHypothesis(ExtendedKalmanFilter filter) {
		int stateDOF = filter.getStateDOF();

		mix = new MultivariateGaussianDM(stateDOF);
		state = new MultivariateGaussianDM(stateDOF);
		probability = Double.NaN;
		mixProb = Double.NaN;
	}


	public MultivariateGaussianDM getState() {
		return state;
	}

	public MultivariateGaussianDM getMix() {
		return mix;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double val) {
		this.probability = val;
	}

	public double getMixedProb() {
		return mixProb;
	}

	public void setMixedProb(double val) {
		this.mixProb = val;
	}

	/**
	 * Swaps the mixed and regular states.  This avoid an unnecisary copy.
	 */
	public void swapMix() {
		MultivariateGaussianDM temp = state;
		state = mix;
		mix = temp;

		probability = mixProb;
	}

	public void setStateValue(MultivariateGaussianDM state, double prob) {
		this.state.set(state);
		this.probability = prob;
	}
}
