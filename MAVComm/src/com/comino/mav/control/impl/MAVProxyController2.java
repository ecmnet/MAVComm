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


package com.comino.mav.control.impl;

import java.io.IOException;
import java.util.Map;

import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.messages.MAV_CMD;
import org.mavlink.messages.lquac.msg_command_long;
import org.mavlink.messages.lquac.msg_statustext;

import com.comino.mav.comm.IMAVComm;
import com.comino.mav.comm.highspeedserial.MAVHighSpeedSerialComm;
import com.comino.mav.comm.highspeedserial.MAVHighSpeedSerialComm2;
import com.comino.mav.comm.serial.MAVSerialComm3;
import com.comino.mav.comm.serial.MAVSerialComm4;
import com.comino.mav.comm.udp.MAVUdpCommNIO3;
import com.comino.mav.control.IMAVController;
import com.comino.mav.control.IMAVMSPController;
import com.comino.mav.mavlink.proxy.MAVUdpProxyNIO3;
import com.comino.msp.log.MSPLogger;
import com.comino.msp.main.control.listener.IMAVLinkListener;
import com.comino.msp.main.control.listener.IMAVMessageListener;
import com.comino.msp.main.control.listener.IMSPStatusChangedListener;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.collector.ModelCollectorService;
import com.comino.msp.model.segment.LogMessage;
import com.comino.msp.model.segment.Status;

/*
 * Direct high speed Proxy controller onboard companions connected with high speed
 * serial driver (currently RPi only)
 */

public class MAVProxyController2 implements IMAVMSPController {

	protected String peerAddress = null;

	protected static IMAVMSPController controller = null;

	protected IMAVComm comm = null;


	protected   DataModel model = null;
	protected   MAVUdpProxyNIO3 proxy = null;

	private static final int BAUDRATE  = 921600;

	public static IMAVController getInstance() {
		return controller;
	}


	public MAVProxyController2(boolean sitl) {
		controller = this;
		model = new DataModel();

		if(sitl) {
			comm = MAVUdpCommNIO3.getInstance(model, "127.0.0.1",14556, 14550);
			proxy = new MAVUdpProxyNIO3("127.0.0.1",14650,"0.0.0.0",14656,comm);
			peerAddress = "127.0.0.1";
			System.out.println("Proxy Controller loaded (SITL) ");
		}
		else {

			if(java.lang.management.ManagementFactory.getOperatingSystemMXBean().getArch().contains("64"))
			    comm = MAVSerialComm4.getInstance(model, BAUDRATE, false);
			else
				comm = MAVSerialComm4.getInstance(model, BAUDRATE, false);
			proxy = new MAVUdpProxyNIO3("172.168.178.2",14550,"172.168.178.1",14555,comm);
			peerAddress = "172.168.178.2";

//			proxy = new MAVUdpProxyNIO3("192.168.178.20",14550,"192.168.178.22",14555,comm);
//			peerAddress = "192.168.178.20";

			System.out.println("Proxy Controller loaded: "+peerAddress);

		}
		comm.addMAVLinkListener(proxy);


	}

	@Override
	public boolean sendMAVLinkMessage(MAVLinkMessage msg) {

		try {
			if(msg.sysId==2) {
				proxy.write(msg);
			} else {
				if(controller.getCurrentModel().sys.isStatus(Status.MSP_CONNECTED)) {
					comm.write(msg);
				} else {
					System.out.println("Command rejected. No connection.");
					return false;
				}

			}
			return true;
		} catch (Exception e1) {
			MSPLogger.getInstance().writeLocalMsg("Command rejected. "+e1.getClass().getSimpleName()+":"+e1.getMessage());
			return false;
		}

	}

	@Override
	public boolean sendMAVLinkCmd(int command, float...params) {

		msg_command_long cmd = new msg_command_long(255,1);
		cmd.target_system = 1;
		cmd.target_component = 1;
		cmd.command = command;
		cmd.confirmation = 1;

		for(int i=0; i<params.length;i++) {
			switch(i) {
			case 0: cmd.param1 = params[0]; break;
			case 1: cmd.param2 = params[1]; break;
			case 2: cmd.param3 = params[2]; break;
			case 3: cmd.param4 = params[3]; break;
			case 4: cmd.param5 = params[4]; break;
			case 5: cmd.param6 = params[5]; break;
			case 6: cmd.param7 = params[6]; break;

			}
		}

		return sendMAVLinkMessage(cmd);
	}


	@Override
	public boolean sendMSPLinkCmd(int command, float...params) {
		MSPLogger.getInstance().writeLocalMsg("Command rejected: Proxy cannot send command to itself...");
		return false;
	}

	public void registerListener(Class<?> clazz, IMAVLinkListener listener) {
		proxy.registerListener(clazz, listener);
	}



	public boolean isConnected() {
		return proxy.isConnected() && comm.isConnected();
	}

	@Override
	public boolean connect() {
		proxy.close();proxy.open(); comm.open();
		if(comm.isConnected())
			sendMAVLinkCmd(MAV_CMD.MAV_CMD_REQUEST_AUTOPILOT_CAPABILITIES, 1);
		return true;
	}

	@Override
	public boolean close() {
		proxy.close();
		return true;
	}


	@Override
	public DataModel getCurrentModel() {
		return comm.getModel();
	}


	@Override
	public Map<Class<?>,MAVLinkMessage> getMavLinkMessageMap() {
		return comm.getMavLinkMessageMap();
	}


	@Override
	public boolean isSimulation() {
		return true;
	}

	@Override
	public void addStatusChangeListener(IMSPStatusChangedListener listener) {
		comm.addStatusChangeListener(listener);

	}

	@Override
	public void addMAVLinkListener(IMAVLinkListener listener) {

	}

	@Override
	public void addMAVMessageListener(IMAVMessageListener listener) {

	}


	@Override
	public ModelCollectorService getCollector() {
		return null;
	}


	@Override
	public void writeLogMessage(LogMessage m) {
		msg_statustext msg = new msg_statustext();
		msg.setText(m.msg);
		msg.componentId = 1;
		msg.severity =m.severity;
		try {
			proxy.write(msg);
		} catch (IOException e) {
		}
	}


	@Override
	public String getConnectedAddress() {
		return peerAddress;
	}


	@Override
	public int getErrorCount() {
		return comm.getErrorCount();
	}


	@Override
	public void enableFileLogging(boolean enable, String directory) {

	}


	@Override
	public boolean start() {

		return false;
	}

}
