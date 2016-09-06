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

public class EstStatus extends Segment {

	private static final long serialVersionUID = -8204904128449328395L;

	public float haglRatio  = 0;
	public float magRatio   = 0;
	public float tasRatio   = 0;
	public float velRatio   = 0;
	public float horizRatio = 0;
	public float vertRatio  = 0;

	public float posHorizAccuracy = 0;
	public float posVertAccurracy = 0;

	public int flags = 0;

	public void set(EstStatus a) {
		this.haglRatio   = a.haglRatio;
		this.magRatio    = a.magRatio;
		this.tasRatio    = a.tasRatio;
		this.velRatio    = a.velRatio;
		this.horizRatio  = a.horizRatio;
		this.vertRatio   = a.vertRatio;

		this.posHorizAccuracy = a.posHorizAccuracy;
		this.posVertAccurracy = a.posVertAccurracy;

		this.flags = a.flags;

	}

	public EstStatus clone() {
		EstStatus at = new EstStatus();
		at.set(this);
		return at;
	}

	//--------------------------------------------------------------------------------------------------------


	public void clear() {
		haglRatio = 0;
		tasRatio  = 0;
		magRatio  = 0;
		vertRatio = 0;
		horizRatio = 0;
		velRatio = 0;

		posHorizAccuracy = 0;
		posVertAccurracy = 0;
		flags = 0;
	}

}
