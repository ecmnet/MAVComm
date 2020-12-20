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

package bubo.mapping.localization.d2.landmark;

import bubo.desc.sensors.landmark.RangeBearingMeasurement;
import bubo.filters.MultivariateGaussianDM;
import bubo.maps.d2.LandmarkMap2D;

import java.util.List;

/**
 * Extremely simplistic robot pose estimation from multiple range-bearing landmark observations.
 *
 * @author Peter Abeles
 */
public class BatchRangeBearingPose {
	LandmarkMap2D map;

	List<RangeBearingMeasurement> observations;

	/**
	 * Adds an observation.  Returns true if it has enough information to provide an initial estimate
	 */
	public boolean addObservation( RangeBearingMeasurement obs ) {
		return true;
	}

	public MultivariateGaussianDM getState() {
		return null;
	}
}
