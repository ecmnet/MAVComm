/*
 * Copyright (c) 2016 by E.Mansfeld
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.comino.mav.control.impl;

import com.comino.mav.comm.udp.MAVUdpCommNIO;
import com.comino.mav.control.IMAVController;


public class MAVUdpController extends MAVController implements IMAVController, Runnable {


	private boolean connect;

	public MAVUdpController(String peerAddress, int peerPort, int bindPort, boolean isSITL) {
		super();
		this.isSITL = isSITL;
		this.peerAddress = peerAddress;
		System.out.println("UDP Controller loaded ("+peerAddress+":"+peerPort+")");
		comm = MAVUdpCommNIO.getInstance(model, peerAddress,peerPort, bindPort);

	}

	@Override
	public boolean connect() {
		this.connect = true;
		new Thread(this).start();
		return connect;
	}

	@Override
	public boolean close() {
		this.connect = true;
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
				comm.open();
			}
		}

		collector.stop();
		comm.close();


	}


}
