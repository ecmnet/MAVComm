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
				if(model.state.l_z > -5.0f)
					model.state.l_z = model.state.l_z - 0.001f - (float)Math.random()*0.001f;
				model.state.l_x = (float)(Math.sin(count/100f))*0.2f;
				model.state.l_y = (float)(Math.cos(count/100f))*0.2f;
				if(model.battery.b0 > 11f)
					model.battery.b0 = model.battery.b0 - (float)Math.random()*0.0001f - 0.0001f;

			} else {
				count = 0;
				model.state.l_z = 0;
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
