/****************************************************************************
 *
 *   Copyright (c) 2017,2018 Eike Mansfeld ecm@gmx.de. All rights reserved.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.mavlink.messages.IMAVLinkMessageID;
import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.messages.MAV_CMD;
import org.mavlink.messages.lquac.msg_command_long;
import org.mavlink.messages.lquac.msg_statustext;

import com.comino.mav.comm.IMAVComm;
import com.comino.mav.comm.proxy.MAVUdpProxyNIO;
import com.comino.mav.comm.serial.MAVSerialComm;
import com.comino.mav.comm.udp.MAVUdpCommNIO;
import com.comino.mav.control.IMAVCmdAcknowledge;
import com.comino.mav.control.IMAVController;
import com.comino.mav.control.IMAVMSPController;
import com.comino.msp.execution.control.StatusManager;
import com.comino.msp.execution.control.listener.IMAVLinkListener;
import com.comino.msp.execution.control.listener.IMAVMessageListener;
import com.comino.msp.execution.control.listener.IMSPStatusChangedListener;
import com.comino.msp.log.MSPLogger;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.LogMessage;
import com.comino.msp.model.segment.Status;
import com.comino.msp.utils.ExecutorService;

public class MAVProxyController implements IMAVMSPController, Runnable {

	protected String peerAddress = null;

	protected static IMAVMSPController controller = null;

	protected IMAVComm comm = null;


	protected   DataModel model = null;
	protected   MAVUdpProxyNIO proxy = null;

	private static final int BAUDRATE_9   = 921600;
	private static final int BAUDRATE_15  = 1500000;

	private StatusManager 				status_manager 	= null;
	private List<IMAVMessageListener> 	messageListener = null;

	private ScheduledFuture<?> future = null;

	private int mode;

	public static IMAVController getInstance() {
		return controller;
	}


	public MAVProxyController(int mode) {
		this.mode = mode;
		controller = this;
		model = new DataModel();
		status_manager = new StatusManager(model);
		messageListener = new ArrayList<IMAVMessageListener>();

		model.sys.setSensor(Status.MSP_MSP_AVAILABILITY, true);
		model.sys.setStatus(Status.MSP_SITL, mode == MAVController.MODE_NORMAL);
		model.sys.setStatus(Status.MSP_PROXY, true);

		switch(mode) {
		case MAVController.MODE_NORMAL:
			comm = MAVSerialComm.getInstance(model, BAUDRATE_15, false);
			comm.open();
			try { Thread.sleep(500); } catch (InterruptedException e) { }

			proxy = new MAVUdpProxyNIO("172.168.178.2",14550,"172.168.178.1",14555,comm);
			peerAddress = "172.168.178.2";
			System.out.println("Proxy Controller loaded: "+peerAddress);
			model.sys.setStatus(Status.MSP_SITL,false);
			break;

		case MAVController.MODE_SITL:
			comm = MAVUdpCommNIO.getInstance(model, "127.0.0.1",14580, 14540);
			proxy = new MAVUdpProxyNIO("127.0.0.1",14650,"0.0.0.0",14656,comm);
			peerAddress = "127.0.0.1";
			System.out.println("Proxy Controller (SITL mode) loaded");
			model.sys.setStatus(Status.MSP_SITL,true);
			break;
		case MAVController.MODE_USB:
			comm = MAVSerialComm.getInstance(model, BAUDRATE_9, false);
			comm.open();
			try { Thread.sleep(500); } catch (InterruptedException e) { }
			proxy = new MAVUdpProxyNIO("127.0.0.1",14650,"0.0.0.0",14656,comm);
			peerAddress = "127.0.0.1";
			System.out.println("Proxy Controller (serial mode) loaded: "+peerAddress);
			model.sys.setStatus(Status.MSP_SITL,false);
			break;
		}

		model.sys.setStatus(Status.MSP_ACTIVE, true);

		comm.addMAVLinkListener(proxy);

	}

	@Override
	public boolean sendMAVLinkMessage(MAVLinkMessage msg) {

		try {
			if(msg.sysId==2) {
				if(proxy.isConnected())
					proxy.write(msg);
			}
			else {
				if(comm.isConnected())
					comm.write(msg);
			}
			return true;
		} catch (Exception e1) {
		       MSPLogger.getInstance().writeLocalMsg("Command rejected: "+msg);
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
	public boolean sendMAVLinkCmd(int command, IMAVCmdAcknowledge ack, float...params) {
		comm.setCmdAcknowledgeListener(ack);
		return sendMAVLinkCmd(command, params);
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
		model.sys.setStatus(Status.MSP_ACTIVE, comm.isConnected());
		if(mode == MAVController.MODE_NORMAL)
			return proxy.isConnected() && comm.isConnected();
		return proxy.isConnected();
	}

	@Override
	public boolean connect() {
		comm.open(); proxy.open();
		if(comm.isConnected()) {
			sendMAVLinkCmd(MAV_CMD.MAV_CMD_REQUEST_AUTOPILOT_CAPABILITIES, 1);
		}
		future = ExecutorService.get().scheduleAtFixedRate(this, 1, 1, TimeUnit.SECONDS);
		return true;
	}

	@Override
	public boolean close() {
		proxy.close(); comm.close();
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
		return mode == MAVController.MODE_SITL;
	}

	@Override
	public void addStatusChangeListener(IMSPStatusChangedListener listener) {
		status_manager.addListener(listener);

	}

	@Override
	public void addMAVLinkListener(IMAVLinkListener listener) {

	}

	@Override
	public void addMAVMessageListener(IMAVMessageListener listener) {
		messageListener.add(listener);
	}

	@Override
	public void writeLogMessage(LogMessage m) {
		msg_statustext msg = new msg_statustext();
		msg.setText(m.text);
		msg.componentId = 1;
		msg.severity =m.severity;
		proxy.write(msg);
		if (messageListener != null) {
			for (IMAVMessageListener msglistener : messageListener)
				msglistener.messageReceived(m);
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
	public String enableFileLogging(boolean enable, String directory) {
		return null;
	}

	@Override
	public StatusManager getStatusManager() {
		return status_manager;
	}


	@Override
	public boolean start() {

		status_manager.start();

		if(isSimulation()) {
			System.out.println("Setup MAVLink streams for simulation mode");
			sendMAVLinkCmd(MAV_CMD.MAV_CMD_SET_MESSAGE_INTERVAL, IMAVLinkMessageID.MAVLINK_MSG_ID_HIGHRES_IMU,50000);
			sendMAVLinkCmd(MAV_CMD.MAV_CMD_SET_MESSAGE_INTERVAL, IMAVLinkMessageID.MAVLINK_MSG_ID_VISION_POSITION_ESTIMATE,50000);
			sendMAVLinkCmd(MAV_CMD.MAV_CMD_SET_MESSAGE_INTERVAL, IMAVLinkMessageID.MAVLINK_MSG_ID_ATTITUDE_TARGET,20000);
		}
		return true;
	}


	@Override
	public void run() {
		if(!proxy.isConnected())  {
			proxy.open();
		}
		if(!comm.isConnected()) {
			comm.open();
		}
	}

}
