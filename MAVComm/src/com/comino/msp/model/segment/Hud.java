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

public class Hud extends Segment {

	private static final long serialVersionUID = -1123989934184248219L;

	public float aX     = Float.NaN;	// angleX
	public float aY     = Float.NaN;	// angleY

	public float h      = Float.NaN;	// heading (compass)
	public float al     = Float.NaN;	// altitude above ground
	public float ag     = Float.NaN;	// est.altitude above sealevel
	public float at     = Float.NaN;    // altitde terrain
	public float ar     = Float.NaN;    // altitde relative
	public float bc     = Float.NaN;    // bottom clearance
	public float ap     = Float.NaN;    // pressure altitude

	public float s    	= Float.NaN;	// ground speed
	public float vs     = Float.NaN;    // vertical speed
	public float as     = Float.NaN;    // airspeed
	public float th     = Float.NaN;	// throttle (0..1)


	public void set(Hud a) {
		aX    	= a.aX;
		aY    	= a.aY;
		h   	= a.h;
		al      = a.al;
		ag      = a.ag;
		s   	= a.s;
		at      = a.at;
		vs      = a.vs;
		ar      = a.ar;
		bc      = a.bc;
		as      = a.as;
		ap      = a.ap;
		th      = a.th;

	}

	public Hud clone() {
		Hud a = new Hud();
		a.set(this);
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
		vs      = 0;
		ar      = 0;
		bc      = 0;
		as      = 0;
		ap      = 0;
		th      = 0;
	}

}
