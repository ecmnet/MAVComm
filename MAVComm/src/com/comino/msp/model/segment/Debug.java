/****************************************************************************
 *
 *   Copyright (c) 2017 Eike Mansfeld ecm@gmx.de. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 ****************************************************************************/


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
