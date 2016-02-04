/*
 * Copyright (c) 2016 by E.Mansfeld
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.comino.msp.model.segment;

import com.comino.msp.model.segment.generic.Segment;

public class Telemetry extends Segment {

	private static final long serialVersionUID = -5513070249760162722L;

	public float  rssi            = 0;


	public void set(Telemetry m) {
		rssi = m.rssi;
	}

	public Telemetry clone() {
		Telemetry s = new Telemetry();
		s.rssi	= rssi;
		return s;
	}

	public void clear() {
		rssi    = 0;

	}


}