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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.LockSupport;

import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.messages.MAV_CMD;
import org.mavlink.messages.lquac.msg_command_long;
import org.mavlink.messages.lquac.msg_msp_command;
import org.mavlink.messages.lquac.msg_statustext;
import org.mavlink.messages.lquac.msg_system_time;
import org.mavlink.messages.lquac.msg_timesync;

import com.comino.mav.comm.IMAVComm;
import com.comino.mav.comm.highspeedserial.MAVHighSpeedSerialComm;
import com.comino.mav.comm.serial.MAVSerialComm;
import com.comino.mav.comm.serial.MAVSerialComm2;
import com.comino.mav.comm.udp.MAVUdpCommNIO;
import com.comino.mav.comm.udp.MAVUdpCommNIO2;
import com.comino.mav.control.IMAVController;
import com.comino.mav.control.IMAVMSPController;
import com.comino.mav.mavlink.proxy.MAVUdpProxyNIO;
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

	protected HashMap<Class<?>,IMAVLinkListener> listeners = null;

	protected   DataModel model = null;
	protected   MAVUdpProxyNIO proxy = null;

	protected  int commError=0;

	private     boolean  isRunning = false;

	public static IMAVController getInstance() {
		return controller;
	}


	public MAVProxyController2(boolean sitl) {
		controller = this;
		model = new DataModel();
		listeners = new HashMap<Class<?>,IMAVLinkListener>();

		if(sitl) {
			comm = MAVUdpCommNIO2.getInstance(model, "127.0.0.1",14556, 14550);
			proxy = new MAVUdpProxyNIO("127.0.0.1",14650,"0.0.0.0",14656);
			peerAddress = "127.0.0.1";
			System.out.println("Proxy Controller loaded (SITL) ");
		}
		else {

			if(java.lang.management.ManagementFactory.getOperatingSystemMXBean().getArch().contains("64"))
			    comm = MAVSerialComm2.getInstance(model);
			else
				comm = MAVHighSpeedSerialComm.getInstance(model);
			comm.open();
			proxy = new MAVUdpProxyNIO("172.168.178.2",14550,"172.168.178.1",14555);
			peerAddress = "172.168.178.2";
			System.out.println("Proxy Controller loaded ");

		}
		comm.addMAVLinkListener(proxy);
	}

	@Override
	public boolean sendMAVLinkMessage(MAVLinkMessage msg) {


		try {
			if(msg.componentId==2) {
				proxy.write(msg);
			} else {
				if(controller.getCurrentModel().sys.isStatus(Status.MSP_CONNECTED)) {
					comm.write(msg);
					MSPLogger.getInstance().writeLocalDebugMsg("Execute: "+msg.toString());
				} else {
					System.out.println("Command rejected. No connection.");
					return false;
				}

			}
			return true;
		} catch (IOException e1) {
			commError++;
			MSPLogger.getInstance().writeLocalMsg("Command rejected. "+e1.getMessage());
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
		listeners.put(clazz, listener);
	}

	public boolean isConnected() {
		return proxy.isConnected() && comm.isConnected();
	}

	@Override
	public boolean connect() {
		commError=0;
		sendMAVLinkCmd(MAV_CMD.MAV_CMD_REQUEST_AUTOPILOT_CAPABILITIES, 1);
		return proxy.open();
	}


	public boolean start() {
		commError=0;
		isRunning = true;
		Thread worker = new Thread(new MAVLinkProxyWorker());
		worker.start();
		return true;
	}


	public boolean stop() {
		isRunning = false;
		return false;
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


	private class MAVLinkProxyWorker implements Runnable {

		public void run() {

			System.out.println("Starting proxy thread..");
			MAVLinkMessage msg = null;

			while (isRunning) {
				try {
					Thread.yield();

					if(proxy.getInputStream()!=null)
					    msg = proxy.getInputStream().read();

					if(msg!=null) {

						IMAVLinkListener listener = listeners.get(msg.getClass());
						if(listener!=null)
							listener.received(msg);
						else
							comm.write(msg);
					}

				} catch (Exception e) {
					commError++;
				}
			}
		}
	}


	public static void main(String[] args) {


		MAVProxyController2 control = new MAVProxyController2(true);


		// Example to execute MSP MAVLinkMessages via sendMSPLinkCommand(..)
		control.registerListener(msg_msp_command.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_msp_command hud = (msg_msp_command)o;
				System.out.println("MSP Command "+hud.command+" executed");
			}
		});



		control.start();

		try {
			Thread.sleep(5000);
			if(!control.isConnected())
				control.connect();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		while(true) {
			try {
				Thread.sleep(500);
				if(!control.isConnected()) {
					control.connect();


				// Example to send MAVLinkMessages from MSP
				//		           msg_msp_status sta = new msg_msp_status();
				//		           sta.load = 50;
				//		           control.proxy.write(sta);

				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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
		proxy.write(msg);

	}


	@Override
	public String getConnectedAddress() {
		return peerAddress;
	}


	@Override
	public int getErrorCount() {
		return commError;
	}

}
