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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.messages.lquac.msg_command_long;
import org.mavlink.messages.lquac.msg_msp_command;

import com.comino.mav.comm.IMAVComm;
import com.comino.mav.comm.highspeedserial.MAVHighSpeedSerialComm;
import com.comino.mav.control.IMAVController;
import com.comino.mav.mavlink.proxy.MAVUdpProxy;
import com.comino.msp.main.control.listener.IMAVLinkListener;
import com.comino.msp.main.control.listener.IMAVMessageListener;
import com.comino.msp.main.control.listener.IMSPModeChangedListener;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.collector.ModelCollectorService;
import com.comino.msp.model.segment.Message;
import com.comino.msp.model.segment.Status;

/*
 * Direct high speed Proxy controller onboard companions connected with high speed
 * serial driver (currently RPi only)
 */

public class MAVProxyController implements IMAVController {

	protected static IMAVController controller = null;
	protected IMAVComm comm = null;

	protected HashMap<Class<?>,IMAVLinkListener> listeners = null;

	protected   DataModel model = null;
	protected   MAVUdpProxy proxy = null;

	private     boolean  isRunning = false;

	public static IMAVController getInstance() {
		return controller;
	}


	public MAVProxyController() {
		controller = this;
		model = new DataModel();
		listeners = new HashMap<Class<?>,IMAVLinkListener>();

		System.out.println("Proxy Controller loaded");


		comm = MAVHighSpeedSerialComm.getInstance(model);
		proxy = new MAVUdpProxy();
		comm.addMAVLinkListener(proxy);
	}

	@Override
	public boolean sendMAVLinkCmd(int command, float...params) {

		if(!controller.getCurrentModel().sys.isStatus(Status.MSP_CONNECTED)) {
			System.out.println("Command rejected. No connection.");
			return false;
		}

		msg_command_long cmd = new msg_command_long(255,1);
		cmd.target_system = 1;
		cmd.target_component = 1;
		cmd.command = command;
		cmd.confirmation = 0;

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

		try {
			comm.write(cmd);
			System.out.println("Sent to PX4: "+cmd.toString());
			return true;
		} catch (IOException e1) {
			System.out.println("Command rejected. "+e1.getMessage());
			return false;
		}
	}

	@Override
	public boolean sendMSPLinkCmd(int command, float...params) {
		System.out.println("Command rejected: Proxy cannot send command to itself...");
		return false;
	}

	public void registerListener(Class<?> clazz, IMAVLinkListener listener) {
		listeners.put(clazz, listener);
	}

	public boolean isConnected() {
		return proxy.isConnected();
	}

	@Override
	public boolean connect() {
		return proxy.open();
	}


	public boolean start() {
		isRunning = true;
		new Thread(new MAVLinkProxyWorker()).start();
		comm.open();
		return true;
	}


	public boolean stop() {
		isRunning = false;
		comm.close();
		return false;
	}

	@Override
	public boolean close() {
		comm.close();
		return true;
	}


	@Override
	public DataModel getCurrentModel() {
		return comm.getModel();
	}


	@Override
	public List<Message> getMessageList() {
		return comm.getMessageList();
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

			while (isRunning) {
				try {
					Thread.sleep(5);

					MAVLinkMessage msg = proxy.getInputStream().read();

					if(msg==null)
						continue;

					IMAVLinkListener listener = listeners.get(msg.getClass());
					if(listener!=null)
						listener.received(msg);
					else
					    comm.write(msg);

				} catch (Exception e) {

				}

			}
		}
	}


	public static void main(String[] args) {


		MAVProxyController control = new MAVProxyController();


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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while(true) {
			try {
				Thread.sleep(100);
				if(!control.isConnected())
					control.connect();

				// Example to send MAVLinkMessages from MSP
//		           msg_msp_status sta = new msg_msp_status();
//		           sta.load = 50;
//		           control.proxy.write(sta);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}




	@Override
	public void addModeChangeListener(IMSPModeChangedListener listener) {
		comm.addModeChangeListener(listener);

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

}
