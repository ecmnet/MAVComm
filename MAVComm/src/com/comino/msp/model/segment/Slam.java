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
	public float    pd;		// planned direction
	public float    pv;		// planned speed

	public Slam() {

	}

	public void set(Slam a) {
		pv = a.pv;
		pd = a.pd;
		px = a.px;
		py = a.py;
	}

	public void setPath(float d_rad, float speed) {
		pv = speed;
		pd = d_rad;
		calcPathPoint(1);
	}

	public void calcPathPoint(float f) {
		px = (float)Math.cos(pd)*f*pv;
		py = (float)Math.sin(pd)*f*pv;
	}

	public Slam clone() {
		Slam at = new Slam();
		at.pv = pv;
		at.pd = pd;
		at.px = px;
		at.py = py;
		return at;
	}

	public void clear() {
		pv = 0;
		pd = 0;
		px = 0;
		py = 0;
	}

}
