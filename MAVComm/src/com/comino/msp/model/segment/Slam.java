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


public class Slam extends Segment {

	private static final long serialVersionUID = -353494527253663585L;

	public float    px;		// planned path x
	public float    py;		// planned path y
	public float    pz;		// planned path y
	public float    pd;		// planned direction XY
	public float    pp;		// planned direction YZ
	public float    pv;		// planned speed
	public float    dw;     // distance to next waypoint
	public float    dm;     // minimal distance to obstacle
	public float    di;     // distance to target
	public float    ox;		// nearest obstacle x
	public float    oy;		// nearest obstacle y
	public float    oz;		// nearest obstacle z

	public Slam() {

	}

	public void set(Slam a) {
		pv = a.pv;
		pd = a.pd;
		pp = a.pp;
		px = a.px;
		py = a.py;
		pz = a.pz;
		di = a.di;
		dw = a.dw;
		dm = a.dm;
		ox = a.ox;
		oy = a.oy;
		oz = a.oz;
	}

	public Slam clone() {
		Slam at = new Slam();
		at.set(this);
		return at;
	}

	public void clear() {
		pv = Float.NaN;
		pd = Float.NaN;
		pp = Float.NaN;
		px = Float.NaN;
		py = Float.NaN;
		pz = Float.NaN;
		di = Float.NaN;
		dw = Float.NaN;
		dm = Float.NaN;
		ox = Float.NaN;
		oy = Float.NaN;
		oz = Float.NaN;
	}

}
