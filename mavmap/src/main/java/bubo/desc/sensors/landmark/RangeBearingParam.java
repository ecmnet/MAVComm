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

package bubo.desc.sensors.landmark;

/**
 * Parameters which describe a range-bearing landmark sensor
 *
 * @author Peter Abeles
 */
public class RangeBearingParam {
	public double maxRange;
	public double rangeSigma;
	public double bearingSigma;

	public RangeBearingParam(double maxRange, double rangeSigma, double bearingSigma) {
		this.maxRange = maxRange;
		this.rangeSigma = rangeSigma;
		this.bearingSigma = bearingSigma;
	}

	public RangeBearingParam(double maxRange) {
		this.maxRange = maxRange;
	}

	public RangeBearingParam() {
	}
}
