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

package com.comino.msp.main;

import com.comino.mav.control.IMAVController;
import com.comino.mav.control.impl.MAVUdpController;
import com.comino.msp.log.MSPLogger;

public class MAVCommTest implements Runnable {

	private IMAVController control = null;
	MSPConfig	           config  = null;

	public MAVCommTest(String[] args) {

		String peerAddress = null;

		if(args.length> 0) {
			peerAddress  = args[0];
		}


		if(args.length>0)
			control = new MAVUdpController("172.168.178.1",14555,14550, false);
		else
		  System.exit(-1);

		if(!control.isConnected())
			control.connect();

		MSPLogger.getInstance(control);

		new Thread(this).start();

	}

	public static void main(String[] args) {
		new MAVCommTest(args);

	}

	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(1000);
				if(control.isConnected())
				  System.out.println(control.getCurrentModel().hud.ag);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}


		}

	}

}
