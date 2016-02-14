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

import java.util.concurrent.TimeUnit;

import com.comino.mav.comm.udp.MAVUdpCommNIO;
import com.comino.mav.control.IMAVController;
import com.comino.msp.model.segment.Status;
import com.comino.msp.utils.ExecutorService;

/*
 * UDP connection to simulation or MAVProxies
 */

public class MAVUdpController extends MAVController implements IMAVController {

	private boolean isSimulation = false;

	public MAVUdpController(String peerAddress, int peerPort, String bindAddress, int bindPort) {
		super();

		System.out.println("UDP Controller loaded");
		comm = MAVUdpCommNIO.getInstance(model, peerAddress,peerPort,bindAddress,bindPort);

		ExecutorService.get().scheduleAtFixedRate(new ConnectionWatch(), 0, 2, TimeUnit.SECONDS);
	}

	@Override
	public boolean connect() {
        return comm.open();
	}

	@Override
	public boolean isSimulation() {
		return isSimulation;
	}

	@Override
	public boolean isConnected() {
		return comm.isConnected() && model.sys.isStatus(Status.MSP_CONNECTED);
	}



	private class ConnectionWatch implements Runnable {

		@Override
		public void run() {
		     if(isConnected())
		    	 return;
		     comm.close(); comm.open();


		}

	}


}
