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

public class Vibration extends Segment {

	private static final long serialVersionUID = -1177166201561030663L;

	public float 	vibx = 0;
	public float 	viby = 0;
	public float 	vibz = 0;
	public float    cli0 = 0;
	public float    cli1 = 0;
	public float    cli2 = 0;


	public void set(Vibration a) {
		vibx		= a.vibx;
		viby		= a.viby;
		vibz		= a.vibz;
		cli0        = a.cli0;
		cli1        = a.cli1;
		cli2        = a.cli2;

	}

	public Vibration clone() {
		Vibration at = new Vibration();
		at.vibx		= vibx;
		at.viby		= viby;
		at.vibz		= vibz;
		at.cli0     = cli0;
		at.cli1     = cli1;
		at.cli2     = cli2;

		return at;
	}

	//--------------------------------------------------------------------------------------------------------


	public void clear() {
		vibx = 0;
		viby = 0;
		vibz = 0;
		cli0 = 0;
		cli1 = 0;
		cli2 = 0;
	}

}
