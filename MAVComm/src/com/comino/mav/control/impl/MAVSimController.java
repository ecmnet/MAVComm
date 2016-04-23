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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.messages.MAV_SEVERITY;

import com.comino.mav.control.IMAVController;
import com.comino.mav.control.IMAVMSPController;
import com.comino.msp.main.control.listener.IMAVLinkListener;
import com.comino.msp.main.control.listener.IMAVMessageListener;
import com.comino.msp.main.control.listener.IMSPModeChangedListener;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.collector.ModelCollectorService;
import com.comino.msp.model.segment.LogMessage;
import com.comino.msp.model.segment.Status;
import com.comino.msp.utils.ExecutorService;

public class MAVSimController extends MAVController implements IMAVController {

	DataModel model = null;
	ArrayList<LogMessage>					msgList;
	private List<IMAVMessageListener> msgListener        = null;
	private ArrayList<IMSPModeChangedListener> modeListener;



	public MAVSimController() {
		System.out.println("Simulation Controller loaded");
		model = new DataModel();
		collector = new ModelCollectorService(model);
		msgList = new ArrayList<LogMessage>();
		msgListener = new ArrayList<IMAVMessageListener>();
		modeListener = new ArrayList<IMSPModeChangedListener>();




		ExecutorService.get().scheduleAtFixedRate(new Simulation(), 2000, 50, TimeUnit.MILLISECONDS);
	}

	@Override
	public boolean connect() {
		return true;
	}

	@Override
	public List<LogMessage> getMessageList() {
		return msgList;
	}



	@Override
	public DataModel getCurrentModel() {
		return model;
	}

	//	@Override
	//	public List<DataModel> getModelList() {
	//		return collector.getModelList();
	//	}

	@Override
	public boolean close() {
		collector.stop();
		return true;
	}

	@Override
	public void addMAVMessageListener(IMAVMessageListener listener) {
		msgListener.add(listener);
	}

	@Override
	public boolean sendMAVLinkMessage(MAVLinkMessage msg) {

		if(!controller.getCurrentModel().sys.isStatus(Status.MSP_CONNECTED)) {
			System.out.println("Command rejected. No connection.");
			return false;
		}
		return true;

	}

	@Override
	public void addModeChangeListener(IMSPModeChangedListener listener) {
			this.modeListener.add(listener);

	}

	@Override
	public boolean sendMAVLinkCmd(int command, float...params) {


		return true;
	}

	public boolean sendMSPLinkCmd(int command, float...params) {

		if(!controller.getCurrentModel().sys.isStatus(Status.MSP_CONNECTED)) {
			System.out.println("Command rejected. No connection.");
			return false;
		}

		return true;
	}


	private class Simulation implements Runnable {


		long count = 0; int msg_count = 0;

		@Override
		public void run() {
			model.battery.p = (short)(95+Math.random()*2f);

			if(getCollector().isCollecting()) {
				count++;
				if(model.state.z > -5.0f)
					model.state.z = model.state.z - 0.001f - (float)Math.random()*0.001f;
				model.state.x = (float)(Math.sin(count/100f))*0.2f;
				model.state.y = (float)(Math.cos(count/100f))*0.2f;
				if(model.battery.b0 > 11f)
					model.battery.b0 = model.battery.b0 - (float)Math.random()*0.0001f - 0.0001f;

			} else {
				count = 0;
				model.state.z = 0;
				model.battery.b0 = 12.4f;
			}



			model.raw.di = (float)Math.random()*0.5f+1;
			model.imu.accx = (float)Math.random()*0.5f-0.25f;
			model.imu.accy = (float)Math.random()*0.5f-0.25f;
			model.imu.accz = (float)Math.random()*0.5f-9.81f;

			model.imu.gyrox = (float)Math.random()*0.5f-0.25f;
			model.imu.gyroy = (float)Math.random()*0.5f-0.25f;
			model.imu.gyroz = (float)Math.random()*0.5f-0.25f;

			model.sys.setStatus(Status.MSP_LANDED, true);

			model.gps.latitude = 47.37174;
			model.gps.longitude = 8.54226;
			model.gps.numsat = 8;

			model.attitude.ag = (float)Math.random()*10f+500f;

			model.imu.abs_pressure = 1013 +  (float)Math.random()*10f;

			if(model.sys.isStatus(Status.MSP_CONNECTED)) {
				for(IMSPModeChangedListener listener : modeListener) {
					listener.update(model.sys, model.sys);
				}
			}

			model.sys.setStatus(Status.MSP_CONNECTED, true);
			model.sys.setStatus(Status.MSP_ARMED, true);
			model.sys.setStatus(Status.MSP_READY, true);

			model.sys.setSensor(Status.MSP_IMU_AVAILABILITY, true);
			model.sys.setSensor(Status.MSP_LIDAR_AVAILABILITY, true);
			model.sys.setSensor(Status.MSP_PIX4FLOW_AVAILABILITY, true);
			model.sys.setSensor(Status.MSP_GPS_AVAILABILITY, true);

			if(msgList.size()>msg_count) {
				msg_count = msgList.size();
				if(msgListener!=null) {
					for(IMAVMessageListener msglistener : msgListener)
						msglistener.messageReceived(msgList, msgList.get(msg_count-1));
				}
			}


		}

	}


}
