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

import java.util.Arrays;

import com.comino.msp.model.segment.generic.Segment;

public class Servo extends Segment {

	private static final long serialVersionUID = 6155451845240484694L;

	public float servo1 = 0;
	public float servo2 = 0;
	public float servo3 = 0;
	public float servo4 = 0;
	public float servo5 = 0;
	public float servo6 = 0;
	public float servo7 = 0;
	public float servo8 = 0;

	public void set(Servo a) {
		this.servo1 = a.servo1;
		this.servo2 = a.servo2;
		this.servo3 = a.servo3;
		this.servo4 = a.servo4;
		this.servo5 = a.servo5;
		this.servo6 = a.servo6;
		this.servo7 = a.servo7;
		this.servo8 = a.servo8;
	}

	public Servo clone() {
		Servo at = new Servo();
		at.set(this);
		return at;
	}

	//--------------------------------------------------------------------------------------------------------


	public void clear() {
		servo1 = 0;
		servo2 = 0;
		servo3 = 0;
		servo4 = 0;
		servo5 = 0;
		servo6 = 0;
		servo7 = 0;
		servo8 = 0;
	}

}
