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

package bubo.filters.ekf;

import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;

/**
 * Utility functions related to EkfPropagator
 *
 * @author Peter Abeles
 */
public class UtilEkfPropagator {

	/**
	 * Computes the jacobian numerically.
	 *
	 * @param initX The point the jacobian is computed around.
	 * @param prop  the proagator
	 * @param T     the time step that the state is propagated forward
	 * @param delta How much the state changes should be adjusted
	 * @return
	 */
	public static DMatrixRMaj numericalJacobian(DMatrixRMaj initX, EkfPredictor<?> prop, double T, double delta) {
		int N = prop.getSystemSize();

		DMatrixRMaj a = new DMatrixRMaj(N, 1);
		DMatrixRMaj b = new DMatrixRMaj(initX);
		DMatrixRMaj F = new DMatrixRMaj(N, N);

		prop.predict(initX, null, T);
		a.set(prop.getPredictedState());

		for (int i = 0; i < N; i++) {
			b.set(i, 0, initX.get(i, 0) + delta);

			prop.predict(b, null, T);
			b.set(prop.getPredictedState());
			CommonOps_DDRM.add(b, -1, a, b);


			for (int j = 0; j < N; j++) {
				F.set(j, i, b.get(j, 0) / delta);
			}
			b.set(initX);
		}

		return F;
	}
}
