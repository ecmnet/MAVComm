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
import java.util.concurrent.TimeUnit;

import org.mavlink.messages.MAVLinkMessage;

import com.comino.mav.control.IMAVController;
import com.comino.msp.execution.control.listener.IMAVMessageListener;
import com.comino.msp.execution.control.listener.IMSPStatusChangedListener;
import com.comino.msp.log.MSPLogger;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.collector.ModelCollectorService;
import com.comino.msp.model.segment.LogMessage;
import com.comino.msp.model.segment.Status;
import com.comino.msp.utils.ExecutorService;
import com.comino.msp.utils.MSPMathUtils;

public class MAVSimController extends MAVController implements IMAVController {

	DataModel model = null;
	ArrayList<LogMessage>					msgList;
	private List<IMAVMessageListener> msgListener        = null;
	private ArrayList<IMSPStatusChangedListener> modeListener;

	public MAVSimController() {
		model = new DataModel();
		collector = new ModelCollectorService(model);
		msgList = new ArrayList<LogMessage>();
		msgListener = new ArrayList<IMAVMessageListener>();
		modeListener = new ArrayList<IMSPStatusChangedListener>();

		ExecutorService.get().scheduleAtFixedRate(new Simulation(), 2000, 50, TimeUnit.MILLISECONDS);
	}

	@Override
	public boolean connect() {
		MSPLogger.getInstance().writeLocalMsg("Simulation Controller loaded");
		return true;
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
	public void addStatusChangeListener(IMSPStatusChangedListener listener) {
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

		Status old = new Status();

		long count = 0; int msg_count = 0;

		@Override
		public void run() {
			model.battery.b0 = 12.75f - count/500f;

			model.base.latitude = 48;
			model.base.longitude = 11;
			model.base.altitude = 520;
			model.base.numsat = 9;

			model.home_state.g_lat = 42.406314f;
			model.home_state.g_lon = 8.730709;

			model.state.g_lat = 42.406314f;
			model.state.g_lon = 8.730709;

			count++;
//			if(model.state.l_z > -5.0f)
//				model.state.l_z = model.state.l_z - 0.001f - (float)Math.random()*0.001f;
			model.state.l_z = -(float)(Math.sin(count/200f))*2f;
			model.state.l_x = (float)(Math.sin(count/100f))*1f;
			model.state.l_y = (float)(Math.cos(count/100f))*1f;

			model.vision.x = model.state.l_x + (float)Math.random()*0.2f-0.1f;
			model.vision.y = model.state.l_y + (float)Math.random()*0.2f-0.1f;
			model.vision.z = model.state.l_z + (float)Math.random()*0.2f-0.1f;

			model.raw.di = (float)Math.random()*0.5f+1;
			model.attitude.r = 0;
			model.attitude.p = (float)(Math.PI/4);
			model.attitude.y = (float)(count/100f % 2*Math.PI);
			model.imu.accx = (float)Math.random()*0.5f-0.25f;
			model.imu.accy = (float)Math.random()*0.5f-0.25f;
			model.imu.accz = (float)Math.random()*0.5f-9.81f;

			model.imu.gyrox = (float)Math.random()*0.5f-0.25f;
			model.imu.gyroy = (float)Math.random()*0.5f-0.25f;
			model.imu.gyroz = (float)Math.random()*0.5f-0.25f;

			model.sys.setStatus(Status.MSP_LANDED, true);

			model.gps.latitude = 47.37174f;
			model.gps.longitude = 8.54226f;
			model.gps.numsat = 8;

			model.slam.pd = MSPMathUtils.toRad(count % 360);
			model.slam.pv = 1 + (float)Math.random()*0.3f;
			model.slam.px = model.state.l_x+(float)Math.cos(model.slam.pd);
			model.slam.py = model.state.l_y+(float)Math.sin(model.slam.pd);

			for(int i=0;i<10;i++)
			  model.grid.setBlock((float)Math.random()*20f-10,(float)Math.random()*20f-10, Math.random()>0.5);

			model.grid.setIndicator(model.state.l_x, model.state.l_y);

			model.hud.ag = (float)Math.random()*10f+500f;

			model.imu.abs_pressure = 1013 +  (float)Math.random()*10f;

			model.sys.setStatus(Status.MSP_CONNECTED, true);
			model.sys.setStatus(Status.MSP_ARMED, true);
			model.sys.setStatus(Status.MSP_READY, true);

			model.sys.setSensor(Status.MSP_IMU_AVAILABILITY, true);
			model.sys.setSensor(Status.MSP_LIDAR_AVAILABILITY, true);
			model.sys.setSensor(Status.MSP_PIX4FLOW_AVAILABILITY, true);
			model.sys.setSensor(Status.MSP_GPS_AVAILABILITY, true);

			if(!old.isEqual(model.sys)) {
				for(IMSPStatusChangedListener listener : modeListener) {
					listener.update(old, model.sys);
				}
			}

			old.set(model.sys);

			if(msgList.size()>msg_count) {
				msg_count = msgList.size();
				if(msgListener!=null) {
					for(IMAVMessageListener msglistener : msgListener)
						msglistener.messageReceived(msgList.get(msg_count-1));
				}
			}


		}

	}


}
