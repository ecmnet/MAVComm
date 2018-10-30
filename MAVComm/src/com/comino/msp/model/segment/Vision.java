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

public class Vision extends Segment {

	private static final long serialVersionUID = 270248566309263309L;

	public static final int MOCAP_VALID = 1;
	public static final int MOCAP_COLLISION_WARNING = 2;

	public float x = Float.NaN;
	public float y = Float.NaN;
	public float z = Float.NaN;

	public float cov_px = Float.NaN;
	public float cov_py = Float.NaN;
	public float cov_pz = Float.NaN;

	public float vx = Float.NaN;
	public float vy = Float.NaN;
	public float vz = Float.NaN;

	public float cov_vx = Float.NaN;
	public float cov_vy = Float.NaN;
	public float cov_vz = Float.NaN;

	public float h    = Float.NaN;
	public float p    = Float.NaN;
	public float r    = Float.NaN;

	public int   flags = 0;
	public float fps   = Float.NaN;
	public float qual  = Float.NaN;

	public int   errors = 0;


	public void set(Vision a) {
		x  = a.x;
		y  = a.y;
		z  = a.z;

		cov_px  = a.cov_px;
		cov_py  = a.cov_py;
		cov_pz  = a.cov_pz;

		vx = a.vx;
		vy = a.vy;
		vz = a.vz;

		cov_vx  = a.cov_vx;
		cov_vy  = a.cov_vy;
		cov_vz  = a.cov_vz;

		errors = a.errors;

		h = a.h;
		p = a.p;
		r = a.r;
		qual = a.qual;

		flags = a.flags;
		fps   = a.fps;
	}

	public Vision clone() {
		Vision a = new Vision();

		a.x  = x;
		a.y  = y;
		a.z  = z;

		a.cov_px  = cov_px;
		a.cov_py  = cov_py;
		a.cov_pz  = cov_pz;

		a.vx = vx;
		a.vy = vy;
		a.vz = vz;

		a.cov_vx  = cov_vx;
		a.cov_vy  = cov_vy;
		a.cov_vz  = cov_vz;

		a.errors = errors;

		a.h = h;
		a.p = p;
		a.r = r;
		a.qual = qual;

		a.flags = flags;
		a.fps = fps;
		return a;
	}


	public void clear() {
		x  = Float.NaN;
		y  = Float.NaN;
		z  = Float.NaN;

		cov_px  = Float.NaN;
		cov_py  = Float.NaN;
		cov_pz  = Float.NaN;

		vx = Float.NaN;
		vy = Float.NaN;
		vz = Float.NaN;

		cov_vx  = Float.NaN;
		cov_vy  = Float.NaN;
		cov_vz  = Float.NaN;

		h = Float.NaN;
		p = Float.NaN;
		r = Float.NaN;

		qual=Float.NaN;

		flags = 0;
		fps = Float.NaN;
	}

	public void  setStatus(int box, boolean val) {
		if(val)
			flags = (int) (flags | (1<<box));
		else
			flags = (int) (flags & ~(1<<box));
	}

	public boolean isStatus(int ...box) {
		for(int b : box)
		  if((flags & (1<<b))==0)
            return false;
		return true;
	}



}
