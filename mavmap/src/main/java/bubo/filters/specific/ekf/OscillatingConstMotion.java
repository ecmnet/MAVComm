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

package bubo.filters.specific.ekf;


import bubo.filters.ekf.EkfPredictor;
import org.ejml.data.DMatrixRMaj;

import static java.lang.Math.*;

/**
 * This is a non-linear motion problem.  The center of the system moves at a constant motion,
 * but the ent point oscillates with a constant peak and frequency.
 * <p/>
 * This is primarily used for testing non-linear filters.  The nice thing about this system
 * is that there is an analytical solution.
 * <p/>
 * x(t) = x_0 + vt + cos(t + phi)
 * <p/>
 * <p/>
 * f(x,T) = [ x + vT            ]
 * [ v                 ]
 * [ cos( acos(x3) + T ]
 */
public class OscillatingConstMotion implements EkfPredictor {

	DMatrixRMaj x;
	DMatrixRMaj F;
	DMatrixRMaj Q;
	double T;

	public OscillatingConstMotion(double T) {
		x = new DMatrixRMaj(3, 1);
		F = new DMatrixRMaj(3, 3);
		Q = new DMatrixRMaj(3, 3);
		this.T = T;
	}

	public OscillatingConstMotion() {
		x = new DMatrixRMaj(3, 1);
		F = new DMatrixRMaj(3, 3);
		Q = new DMatrixRMaj(3, 3);

		T = Double.NaN;
	}

	@Override
	public void predict(DMatrixRMaj mean, Object o, double T) {
		double x1 = mean.get(0, 0);
		double x2 = mean.get(1, 0);
		double x3 = mean.get(2, 0);

		double a = cos(acos(x3) + T);

		this.x.set(0, 0, x1 + x2 * T);
		this.x.set(1, 0, x2);
		this.x.set(2, 0, a);

		a = sin(acos(x3) + T) / sqrt(1 - x3 * x3);

		F.set(0, 0, 1.0);
		F.set(0, 1, T);
		F.set(1, 1, 1.0);
		F.set(2, 2, a);

		// no plant noise on velocity
		Q.set(0, 0, 2.0 * T);
		Q.set(2, 2, 0.1 * T);
	}

	@Override
	public DMatrixRMaj getJacobianF() {
		return F;
	}

	@Override
	public DMatrixRMaj getPlantNoise() {
		return Q;
	}

	@Override
	public DMatrixRMaj getPredictedState() {
		return x;
	}

	@Override
	public int getSystemSize() {
		return 3;
	}
}
