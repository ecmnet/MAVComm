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

public class Raw extends Segment {

	private static final long serialVersionUID = -1123989934184248219L;

	public float di     = Float.NaN;		// LIDAR distance
	public float fX     = Float.NaN;		// Flow integrated X
	public float fY     = Float.NaN;		// Flow integrated Y
	public float fq		= Float.NaN;
	public float fd		= Float.NaN;		// Flow distance
	public float dicov	= Float.NaN;		// LIDAR covariance
	public float fgX    = Float.NaN;	    // Flow Gyro
	public float fgY    = Float.NaN;
	public float fgZ    = Float.NaN;



	public void set(Raw a) {
		fX    	= a.fX;
		fY    	= a.fY;
		di   	= a.di;
		fq      = a.fq;
		fd      = a.fd;
		fgX     = a.fgX;
		fgY     = a.fgY;
		fgZ     = a.fgZ;
		dicov   = a.dicov;

	}

	public Raw clone() {
		Raw at = new Raw();
		at.fX 		= fX;
		at.fY 		= fY;
		at.di		= di;
		at.fq		= fq;
		at.fd       = fd;
		at.fgX      = fgX;
		at.fgY      = fgY;
		at.fgZ      = fgZ;
		at.dicov    = dicov;

		return at;
	}

	//--------------------------------------------------------------------------------------------------------


	public void clear() {
		fX    	= 0;
		fY    	= 0;
		di   	= 0;
		fq      = 0;
		fd      = 0;
		fgX     = 0;
		fgY     = 0;
		fgZ     = 0;
		dicov   = 0;
	}

}
