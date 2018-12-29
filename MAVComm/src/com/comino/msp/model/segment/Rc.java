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

public class Rc extends Segment {

	private static final long serialVersionUID = -1177166201561030663L;

	public float  rssi =  Float.NaN;

	public float 	s0 = Float.NaN;
	public float 	s1 = Float.NaN;
	public float 	s2 = Float.NaN;
	public float 	s3 = Float.NaN;
	public float 	s4 = Float.NaN;
	public float 	s5 = Float.NaN;
	public float 	s6 = Float.NaN;
	public float 	s7 = Float.NaN;

	public void set(Rc a) {
		s0		= a.s0;
		s1		= a.s1;
		s2		= a.s2;
		s3		= a.s3;
		s4		= a.s4;
		s5		= a.s5;
		s6		= a.s6;
		s7		= a.s7;
		rssi    = a.rssi;
	}

	public Rc clone() {
		Rc at = new Rc();
		at.s0 		= s0;
		at.s1 		= s1;
		at.s2 		= s2;
		at.s3 		= s3;
		at.s4 		= s4;
		at.s5 		= s5;
		at.s6 		= s6;
		at.s7 		= s7;
		at.rssi     = rssi;

		return at;
	}

	public float get(int channel) {
		switch(channel) {
		case 1:
			return s0;
		case 2:
			return s1;
		case 3:
			return s2;
		case 4:
			return s3;
		case 5:
			return s4;
		case 6:
			return s5;
		case 7:
			return s6;
		case 8:
			return s7;
		default:
			return Float.NaN;
		}
	}

	//--------------------------------------------------------------------------------------------------------


	public void clear() {
		s0   = Float.NaN;
		s1   = Float.NaN;
		s2   = Float.NaN;
		s3   = Float.NaN;
		s4   = Float.NaN;
		s5   = Float.NaN;
		s6   = Float.NaN;
		s7   = Float.NaN;
		rssi = Float.NaN;

	}

}
