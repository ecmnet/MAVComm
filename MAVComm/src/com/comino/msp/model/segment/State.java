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

public class State extends Segment {


	private static final long serialVersionUID = 5910084328085663916L;

	public static final int 	STATE_Z_AVAILABLE  = 0;
	public static final int 	STATE_H_AVAILABLE  = 1;
	public static final int		STATE_XY_AVAILABLE = 2;

	// flags

	private int		flags=0;		// flags

	// positioning actual

	public float		l_x=Float.NaN;		//  x-position in m (Roll)
	public float    l_y=Float.NaN;		//  y-position in m (Pitch)
	public float    l_z=Float.NaN;		//  z-position in m (Altitude)

	public float    h=Float.NaN;		// heading in radiant

	public float    l_vx=Float.NaN;		//  x speed in m/s
	public float 	l_vy=Float.NaN;		//  y speed in m/s
	public float		l_vz=Float.NaN;		//  z speed in m/s

	public float		vh=Float.NaN;		//  heading speed in radiant/s
	public float		v=Float.NaN;		//  ground speed in m/s

	public float    l_ax=Float.NaN;		//  x acceleration in m/s^2
	public float 	l_ay=Float.NaN;		//  y acceleration in m/s^2
	public float		l_az=Float.NaN;		//  z acceleration in m/s^2

	public float		ah=Float.NaN;		// gative heading speed in radiant/s^2

	public double   g_lon	= Double.NaN;
	public double   g_lat 	= Double.NaN;
	public float    g_alt	= Float.NaN;
	public float    g_vx	= Float.NaN;
	public float    g_vy	= Float.NaN;
	public float    g_vz	= Float.NaN;

	public int      c_frame = 0;


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
		t.l_x		= l_x;
		t.l_y		= l_y;
		t.l_z		= l_z;
		t.h		    = h;
		t.v         = v;

		t.l_vx	= l_vx;
		t.l_vy	= l_vy;
		t.l_vz	= l_vz;
		t.vh	= vh;

		t.l_ax	= l_ax;
		t.l_ay	= l_ay;
		t.l_az	= l_az;
		t.ah	= ah;

		t.g_lon	= g_lon;
		t.g_lat = g_lat;
		t.g_alt	= g_alt;
		t.g_vx	= g_vx;
		t.g_vy	= g_vy;
		t.g_vz	= g_vz;

		t.c_frame = c_frame;


		return t;
	}

	public void set(State t) {
		flags = t.flags;
		l_x		= t.l_x;
		l_y		= t.l_y;
		l_z		= t.l_z;
		h		= t.h;
		v       = t.v;

		l_vx	= t.l_vx;
		l_vy	= t.l_vy;
		l_vz	= t.l_vz;
		vh		= t.vh;

		l_ax	= t.l_ax;
		l_ay	= t.l_ay;
		l_az	= t.l_az;
		ah		= t.ah;

		g_lon	= t.g_lon;
		g_lat 	= t.g_lat;
		g_alt	= t.g_alt;
		g_vx	= t.g_vx;
		g_vy	= t.g_vy;
		g_vz	= t.g_vz;

		c_frame = t.c_frame;
	}

	public void clear() {
		flags 	= 0;
		l_x		= 0;
		l_y		= 0;
		l_z		= 0;
		h		= 0;
		l_vx	= 0;
		l_vy	= 0;
		l_vz	= 0;
		vh		= 0;
		l_ax	= 0;
		l_ay	= 0;
		l_az	= 0;
		ah		= 0;
		g_lon	= 0;
		g_lat 	= 0;
		g_alt	= 0;
		g_vx	= 0;
		g_vy	= 0;
		g_vz	= 0;

		v       = 0;

		c_frame = 0;
	}

	public void print(String header) {
		System.out.printf("%s State: x= %3.2f y=%3.2f z=%3.2f h=%3.2f - vx= %3.2f vy=%3.2f vz=%3.2f vh=%3.2f \n",
				header,l_x,l_y,l_z,h);
	}

}
