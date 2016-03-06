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

public class Attitude extends Segment {

	private static final long serialVersionUID = -1123989934184248219L;

	public float aX     = 0;	// angleX
	public float aY     = 0;	// angleY

	public float h      = 0;	// heading (compass)
	public float al     = 0;	// altitude above ground
	public float ag     = 0;	// est.altitude above sealevel
	public float at     = 0;    // altitde terrain

	public float s    	= 0;	// ground speed


	public void set(Attitude a) {
		aX    	= a.aX;
		aY    	= a.aY;
		h   	= a.h;
		al      = a.al;
		ag      = a.ag;
		s   	= a.s;
		at      = a.at;

	}

	public Attitude clone() {
		Attitude a = new Attitude();
		a.aX 		= aX;
		a.aY 		= aY;
		a.h			= h;
		a.al       	= al;
		a.ag       	= ag;
		a.s			= s;
		a.at       	= at;
		return a;
	}

	//--------------------------------------------------------------------------------------------------------


	public void clear() {
		aX    	= 0;
		aY    	= 0;
		h   	= 0;
		al 		= 0;
		ag      = 0;
		s   	= 0;
		at      = 0;
	}

}
