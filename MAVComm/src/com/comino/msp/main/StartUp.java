package com.comino.msp.main;

import com.comino.mav.control.impl.MAVProxyController;

public class StartUp {
	
	MAVProxyController control = null;
	MSPConfig	          config  = null;
	
	public StartUp() {
		
		config  = MSPConfig.getInstance("msp.properties");
		System.out.println("MSPControlService version "+config.getVersion());
		control = new MAVProxyController();
	
		
		// TODO 1.0: Start services if required
		
		// TODO 1.0: register MSP commands here
		
		control.start();

		try {
			Thread.sleep(5000);
			if(!control.isConnected())
				control.connect();
		} catch (InterruptedException e) {
		}

		while(true) {
			try {
				Thread.sleep(100);
				if(!control.isConnected())
					control.connect();

			} catch (InterruptedException e) {
				
			}
		}
	}
	
	

	public static void main(String[] args) {
		new StartUp();

	}

}
