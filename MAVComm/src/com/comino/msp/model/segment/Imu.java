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

public class Imu extends Segment {

	private static final long serialVersionUID = -7271311099121759585L;

	public float accx = 0;
	public float accy = 0;
	public float accz = 0;

	public float gyrox = 0;
	public float gyroy = 0;
	public float gyroz = 0;

	public float magx = 0;
	public float magy = 0;
	public float magz = 0;

	public float abs_pressure = 0;


	public void set(Imu a) {
		accx = a.accx;
		accy = a.accy;
		accz = a.accz;

		gyrox = a.gyrox;
		gyroy = a.gyroy;
		gyroz = a.gyroz;

		magx = a.magx;
		magy = a.magy;
		magz = a.magz;

		abs_pressure = a.abs_pressure;

	}

	public Imu clone() {
		Imu a = new Imu();
		a.accx = accx;
		a.accy = accy;
		a.accz = accz;

		a.gyrox = gyrox;
		a.gyroy = gyroy;
		a.gyroz = gyroz;

		a.magx = magx;
		a.magy = magy;
		a.magz = magz;

		a.abs_pressure = abs_pressure;


		return a;
	}


	public void clear() {
		accx = 0;
		accy = 0;
		accz = 0;

		gyrox = 0;
		gyroy = 0;
		gyroz = 0;

		magx = 0;
		magy = 0;
		magz = 0;

		abs_pressure = 0;

	}



}
