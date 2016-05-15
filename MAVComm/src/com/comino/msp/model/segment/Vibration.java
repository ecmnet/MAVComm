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

public class Vibration extends Segment {

	private static final long serialVersionUID = -1177166201561030663L;

	public float 	vibx = 0;
	public float 	viby = 0;
	public float 	vibz = 0;
	public float    cli0 = 0;
	public float    cli1 = 0;
	public float    cli2 = 0;


	public void set(Vibration a) {
		vibx		= a.vibx;
		viby		= a.viby;
		vibz		= a.vibz;
		cli0        = a.cli0;
		cli1        = a.cli1;
		cli2        = a.cli2;

	}

	public Vibration clone() {
		Vibration at = new Vibration();
		at.vibx		= vibx;
		at.viby		= viby;
		at.vibz		= vibz;
		at.cli0     = cli0;
		at.cli1     = cli1;
		at.cli2     = cli2;

		return at;
	}

	//--------------------------------------------------------------------------------------------------------


	public void clear() {
		vibx = 0;
		viby = 0;
		vibz = 0;
		cli0 = 0;
		cli1 = 0;
		cli2 = 0;
	}

}
