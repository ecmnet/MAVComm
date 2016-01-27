package com.comino.mav.control.impl;

import com.comino.mav.comm.serial.MAVSerialComm;
import com.comino.mav.control.IMAVController;

/*
 * Direct serial controller up to 115200 baud for telem1 connections e.g. Radio
 */

public class MAVSerialController extends MAVController implements IMAVController {
	
	
	public MAVSerialController() {
		super();
		System.out.println("Serial Controller loaded");
		comm = MAVSerialComm.getInstance(model);
		
	}

	@Override
	public boolean connect() {
		return comm.open();
	}
	
	@Override
	public boolean isSimulation() {
		return false;
	}
	
	@Override
	public boolean isConnected() {
		return isConnected;
	}

}
