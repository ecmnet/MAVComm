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


package com.comino.mav.control.impl;

import com.comino.mav.comm.udp.MAVUdpCommNIO;
import com.comino.mav.comm.udp.MAVUdpCommNIO2;
import com.comino.mav.control.IMAVController;


public class MAVUdpController extends MAVController implements IMAVController, Runnable {


	private boolean connect;

	public MAVUdpController(String peerAddress, int peerPort, int bindPort, boolean isSITL) {
		super();
		this.isSITL = isSITL;
		this.peerAddress = peerAddress;
		System.out.println("UDP Controller loaded ("+peerAddress+":"+peerPort+")");
		comm = MAVUdpCommNIO2.getInstance(model, peerAddress,peerPort, bindPort);

	}

	@Override
	public boolean connect() {
		this.connect = true;
		new Thread(this).start();
		return connect;
	}

	@Override
	public boolean close() {
		this.connect = false;
		return true;
	}


	@Override
	public boolean isConnected() {
		return comm.isConnected();
	}

	@Override
	public void run() {
		while(connect) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) { }

			if(!comm.isConnected()) {
				comm.close();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) { }
				comm.open()	;
			}
		}
		collector.stop();
		comm.close();
	}


}
