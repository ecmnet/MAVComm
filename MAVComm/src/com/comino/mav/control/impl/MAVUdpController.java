package com.comino.mav.control.impl;

import com.comino.mav.comm.udp.MAVUdpComm;
import com.comino.mav.control.IMAVController;

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
		return comm.isConnected();
	}
	
	

}
