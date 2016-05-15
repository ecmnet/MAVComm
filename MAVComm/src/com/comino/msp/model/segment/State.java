/****************************************************************************
 *
 *   Copyright (c) 2016 Eike Mansfeld ecm@gmx.de. All rights reserved.
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

public class State extends Segment {


	private static final long serialVersionUID = 5910084328085663916L;

	public static final int 	STATE_Z_AVAILABLE  = 0;
	public static final int 	STATE_H_AVAILABLE  = 1;
	public static final int		STATE_XY_AVAILABLE = 2;

	// flags

	private int		flags=0;		// flags

	// positioning actual

	public float	x=0;			//  x-position in m (Roll)
	public float    y=0;			//  y-position in m (Pitch)
	public float    z=0;			//  z-position in m (Altitude)
	public float    h=0;			// heading in radiant

	public float	hx=0;			// home x-position in m (Roll)
	public float    hy=0;			// home y-position in m (Pitch)
	public float    hz=0;			// home z-position in m (Altitude)

	public float    vx=0;			// gative x speed in m/s
	public float 	vy=0;			// gative y speed in m/s
	public float	vz=0;			// gative z speed in m/s
	public float	vh=0;			// gative heading speed in radiant/s

	public float    lat=0;
	public float    lon=0;

	public float	g_x		= 0;
	public float    g_y 	= 0;
	public float    g_z		= 0;
	public float    g_vx	= 0;
	public float    g_vy	= 0;
	public float    g_vz	= 0;


	// helpers

	public void  setFlag(int box, boolean val) {
		if(val)
			flags = (short) (flags | (1<<box));
		else
			flags = (short) (flags & ~(1<<box));
	}

	public boolean isStateValid(int ...box) {
		for(int b : box)
			if((flags & (1<<b))==0)
				return false;
		return true;
	}



	public State clone() {
		State t = new State();
		t.flags = flags;
		t.x		= x;
		t.y		= y;
		t.z		= z;
		t.hx	= hx;
		t.hy	= hy;
		t.hz	= hz;
		t.h		= h;
		t.vx	= vx;
		t.vy	= vy;
		t.vz	= vz;
		t.vh	= vh;

		t.lat   = lat;
		t.lon   = lon;
		t.g_x	= g_x;
		t.g_y   = g_y;
		t.g_z	= g_z;
		t.g_vx	= g_vx;
		t.g_vy	= g_vy;
		t.g_vz	= g_vz;


		return t;
	}

	public void set(State t) {
		flags = t.flags;
		x		= t.x;
		y		= t.y;
		z		= t.z;
		hx		= t.hx;
		hy		= t.hy;
		hz		= t.hz;
		h		= t.h;
		vx		= t.vx;
		vy		= t.vy;
		vz		= t.vz;
		vh		= t.vh;

		lat     = t.lat;
		lon     = t.lon;
		g_x		= t.g_x;
		g_y 	= t.g_y;
		g_z		= t.g_z;
		g_vx	= t.g_vx;
		g_vy	= t.g_vy;
		g_vz	= t.g_vz;
	}

	public void clear() {
		flags 	= 0;
		x		= 0;
		y		= 0;
		z		= 0;
		hx		= 0;
		hy		= 0;
		hz		= 0;
		h		= 0;
		vx		= 0;
		vy		= 0;
		vz		= 0;
		vh		= 0;
		lat     = 0;
		lon     = 0;
		g_x		= 0;
		g_y 	= 0;
		g_z		= 0;
		g_vx	= 0;
		g_vy	= 0;
		g_vz	= 0;
	}

	public void print(String header) {
		System.out.printf("%s State: x= %3.2f y=%3.2f z=%3.2f h=%3.2f - vx= %3.2f vy=%3.2f vz=%3.2f vh=%3.2f \n",
				header,x,y,z,h,vx,vy,vz,vh);
	}

}
