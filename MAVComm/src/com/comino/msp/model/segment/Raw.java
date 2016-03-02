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

public class Raw extends Segment {

	private static final long serialVersionUID = -1123989934184248219L;

	public float di     = 0;		// LIDAR distance
	public float fX     = 0;		// Flow integrated X
	public float fY     = 0;		// Flow integrated Y
	public int   fq		= 0;
	public float fd		= 0;		// Flow distance



	public void set(Raw a) {
		fX    	= a.fX;
		fY    	= a.fY;
		di   	= a.di;
		fq      = a.fq;
		fd      = a.fd;

	}

	public Raw clone() {
		Raw at = new Raw();
		at.fX 		= fX;
		at.fY 		= fY;
		at.di		= di;
		at.fq		= fq;
		at.fd       = fd;

		return at;
	}

	//--------------------------------------------------------------------------------------------------------


	public void clear() {
		fX    	= 0;
		fY    	= 0;
		di   	= 0;
		fq      = 0;
		fd      = 0;
	}

}
