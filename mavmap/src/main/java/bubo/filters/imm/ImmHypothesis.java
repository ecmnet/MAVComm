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

/**
 * Describes the current state of a filter associated with a model in the IMM
 */
public class ImmHypothesis {
	public MultivariateGaussianDM state;
	// probability computed durring mixing
	public double probability;
	// temporary state used durring mixing
	public double mixProb;

	private MultivariateGaussianDM mix;

	public ImmHypothesis(EkfPredictor<?> model) {
		state = new MultivariateGaussianDM(model.getSystemSize());
		mix = new MultivariateGaussianDM(model.getSystemSize());
		probability = Double.NaN;
		mixProb = Double.NaN;
	}

	public MultivariateGaussianDM getMix() {
		return mix;
	}

	public MultivariateGaussianDM getState() {
		return state;
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
