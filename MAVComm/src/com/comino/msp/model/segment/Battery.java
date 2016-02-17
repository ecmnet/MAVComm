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

public class Battery extends Segment {

	private static final long serialVersionUID = -4051617731611229443L;

	public float  b0              = 0; // Voltage
	public float  c0			  = 0; // Current
	public float  a0			  = 0; // Accumulated consumption
	public short  p               = 0;


	public void set(Battery m) {
		 a0 = m.a0;
		 b0 = m.b0;
		 c0 = m.c0;
		 p  = m.p;
	}

	public Battery clone() {
		Battery s = new Battery();
		s.b0	= b0;
		s.c0    = c0;
		s.p     = p;
		s.a0    = a0;
		return s;
	}

	public void clear() {
		b0    = 0;
		c0    = 0;
		p     = 0;
		a0    = 0;
	}


}