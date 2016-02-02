package com.comino.mav.control.impl;

import java.util.concurrent.TimeUnit;

import com.comino.mav.comm.udp.MAVUdpComm;
import com.comino.mav.control.IMAVController;
import com.comino.msp.model.segment.Status;
import com.comino.msp.utils.ExecutorService;

/*
 * UDP connection to simulation or MAVProxies
 */

public class MAVUdpController extends MAVController implements IMAVController {
	
	private boolean isSimulation = false;
	
	public MAVUdpController(String address) {
		super();
	
		System.out.println("UDP Controller loaded");
		comm = MAVUdpComm.getInstance(model, address);
		
		if(address.startsWith("localhost") || address.startsWith("127"))
			isSimulation = true;
		
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
