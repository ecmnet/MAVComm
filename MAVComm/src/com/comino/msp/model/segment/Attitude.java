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

public class Attitude extends Segment {

	private static final long serialVersionUID = -5331001446960382012L;

	public float r        	 = Float.NaN;	// roll in rad
	public float p           = Float.NaN;	// pitch in rad
	public float y           = Float.NaN;	// yaw in rad

	public float q1        	 = Float.NaN;	// Quaternion Q1
	public float q2        	 = Float.NaN;	// Quaternion Q2
	public float q3        	 = Float.NaN;	// Quaternion Q3
	public float q4        	 = Float.NaN;	// Quaternion Q4

	public float rr        	 = Float.NaN;	// rollrate in rad/s
	public float pr          = Float.NaN;	// pitchrate in rad/s
	public float yr          = Float.NaN;	// yawrate in rad/s

	public float sr        	 = Float.NaN;	// setpoint roll in rad
	public float sp          = Float.NaN;	// setpoint pitch in rad
	public float sy          = Float.NaN;	// setpoint yaw in rad
	public float st          = Float.NaN;

	public float srr          = Float.NaN;	// setpoint rollrate in rad/s
	public float spr          = Float.NaN;	// setpoint pitchrate in rad/s
	public float syr          = Float.NaN;	// setpoint yawrate in rad/s


	public void set(Attitude a) {

		r 	= a.r;
		p   = a.p;
		y   = a.y;
		q1  = a.q1;
		q2  = a.q2;
		q3  = a.q3;
		q4  = a.q4;
		rr 	= a.rr;
		pr  = a.pr;
		yr  = a.yr;
		sr 	= a.sr;
		sp  = a.sp;
		sy  = a.sy;
		st  = a.st;
		srr = a.srr;
		spr = a.spr;
		syr = a.syr;
	}

	public Attitude clone() {
		Attitude a = new Attitude();
		a.set(this);
		return a;
	}

	public void clear() {
		r 	= Float.NaN;
		p   = Float.NaN;
		y   = Float.NaN;
		q1  = Float.NaN;
		q2  = Float.NaN;
		q3  = Float.NaN;
		q4  = Float.NaN;
		rr 	= Float.NaN;
		pr  = Float.NaN;
		yr  = Float.NaN;
		sr 	= Float.NaN;
		sp  = Float.NaN;
		sy  = Float.NaN;
		st  = Float.NaN;
		srr = Float.NaN;
		spr = Float.NaN;
		syr = Float.NaN;
	}

}
