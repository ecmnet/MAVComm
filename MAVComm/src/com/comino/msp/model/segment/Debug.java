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

public class Debug extends Segment {

	private static final long serialVersionUID = -1026591471182262641L;

	// positioning actual


	public float	x=0;			//  x-position in m (Roll)
	public float    y=0;			//  y-position in m (Pitch)
	public float    z=0;			//  z-position in m (Altitude)
	public float    h=0;			// heading in radiant

	public float    vx=0;			// gative x speed in m/s
	public float 	vy=0;			// gative y speed in m/s
	public float	vz=0;			// gative z speed in m/s
	public float	vh=0;			// gative heading speed in radiant/s



	public Debug clone() {
		Debug t = new Debug();
		t.x		= x;
		t.y		= y;
		t.z		= z;
		t.h		= h;
		t.vx	= vx;
		t.vy	= vy;
		t.vz	= vz;
		t.vh	= vh;

		return t;
	}

	public void set(Debug t) {

		x		= t.x;
		y		= t.y;
		z		= t.z;
		h		= t.h;
		vx		= t.vx;
		vy		= t.vy;
		vz		= t.vz;
		vh		= t.vh;

	}

	public void clear() {

		x		= 0;
		y		= 0;
		z		= 0;
		h		= 0;
		vx		= 0;
		vy		= 0;
		vz		= 0;
		vh		= 0;
	}

	public void print(String header) {
		System.out.printf("%s State: x= %3.2f y=%3.2f z=%3.2f h=%3.2f - vx= %3.2f vy=%3.2f vz=%3.2f vh=%3.2f \n",
				header,x,y,z,h,vx,vy,vz,vh);
	}

}
