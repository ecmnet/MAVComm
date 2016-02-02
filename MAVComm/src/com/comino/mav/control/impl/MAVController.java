package com.comino.mav.control.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.messages.lquac.msg_command_long;
import org.mavlink.messages.lquac.msg_msp_command;

import com.comino.mav.comm.IMAVComm;
import com.comino.mav.comm.udp.MAVUdpComm;
import com.comino.mav.control.IMAVController;
import com.comino.msp.main.control.listener.IMSPModeChangedListener;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.collector.ModelCollectorService;
import com.comino.msp.model.segment.Message;
import com.comino.msp.model.segment.Status;


public class MAVController implements IMAVController {

	protected static IMAVController controller = null;
	protected IMAVComm comm = null;

	protected ModelCollectorService collector = null;

	protected   DataModel model = null;

	public static IMAVController getInstance() {
		return controller;
	}


	public MAVController() {
		controller = this;
		model = new DataModel();
		collector = new ModelCollectorService(model);
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
			System.out.println("Execute: "+cmd.toString());
			return true;
		} catch (IOException e1) {
			System.out.println("Command rejected. "+e1.getMessage());
			return false;
		}
	}

    public boolean sendMSPLinkCmd(int command, float...params) {

		if(!controller.getCurrentModel().sys.isStatus(Status.MSP_CONNECTED)) {
			System.out.println("Command rejected. No connection.");
			return false;
		}

		msg_msp_command cmd = new msg_msp_command(255,1);
		cmd.command = command;

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
			if(!(comm instanceof MAVUdpComm))
				throw new IOException("MSP Commands only via UDP to proxy allowed");

			comm.write(cmd);
			System.out.println("Execute: "+cmd.toString());
			return true;
		} catch (IOException e1) {
			System.out.println("Command rejected: "+e1.getMessage());
			return false;
		}
	}

	@Override
	public boolean connect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean start() {
		return collector.start();

	}

	@Override
	public boolean stop() {
		return collector.stop();
	}


	@Override
	public List<DataModel> getModelList() {
		return collector.getModelList();
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


	@Override
	public boolean isConnected() {
		return true;
	}

	@Override
	public boolean isCollecting() {
		return collector.isCollecting();
	}


	@Override
	public boolean close() {
		comm.close();
		return true;
	}


	@Override
	public void addModeChangeListener(IMSPModeChangedListener listener) {
		comm.addModeChangeListener(listener);

	}




}
