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

public class LogMessage extends Segment {

	private static final long serialVersionUID = 3345013931542810501L;


	public String     msg = null;
	public int   severity = 0;

	public LogMessage() {
		this.tms = System.currentTimeMillis();
	}

	public LogMessage(String msg, int severity) {
		this.msg = msg;
		this.severity = severity;
		this.tms = System.currentTimeMillis();
	}

	public LogMessage(String msg, int severity, long tms) {
		this.msg = msg;
		this.severity = severity;
		this.tms = tms;
	}

	public LogMessage clone() {
		return new LogMessage(this.msg,this.severity, this.tms);
	}

	public void set(LogMessage m) {
		this.msg = m.msg;
		this.severity = m.severity;
		this.tms = m.tms;
	}

	public boolean isEqual(LogMessage m) {
		if(m.msg==null)
			return false;
		return m.msg.contains(this.msg) || (m.tms - this.tms) > 500 ;
	}

	public void clear() {
		this.msg = null;
		this.severity = 0;
	}

	public String toString() {
		return "["+severity+"] "+msg;
	}

}
