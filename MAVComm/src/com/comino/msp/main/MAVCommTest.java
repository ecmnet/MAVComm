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
			control = new MAVUdpController(peerAddress,14555,14550, true);
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
				Thread.sleep(100);
				System.out.println(control.getCurrentModel().rc.rssi);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
