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

package com.comino.mav.comm.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.List;
import java.util.Map;

import org.mavlink.messages.MAVLinkMessage;

import com.comino.mav.comm.IMAVComm;
import com.comino.mav.mavlink.IMAVLinkMsgListener;
import com.comino.mav.mavlink.MAVLinkToModelParser;
import com.comino.msp.main.control.listener.IMSPModeChangedListener;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.collector.ModelCollectorService;
import com.comino.msp.model.segment.Message;
import com.comino.msp.utils.ExecutorService;


public class MAVUdpComm implements IMAVComm {


	private String 				    address;
	private DataModel 				model = null;

	private SocketAddress 			bindPort = null;
	private SocketAddress 			peerPort;
	private DatagramChannel 		channel = null;

	private MAVLinkToModelParser	parser = null;

	private boolean					isConnected = false;

	private static MAVUdpComm com = null;

	public static MAVUdpComm getInstance(DataModel model, String address) {
		if(com==null)
			com = new MAVUdpComm(model, address);
		return com;
	}

	private MAVUdpComm(DataModel model, String address) {
		this.model = model;
		this.address = address;
		this.parser = new MAVLinkToModelParser(model,this);


	}

	public boolean open() {

		if(channel!=null && channel.isConnected()) {
			isConnected = true;
			return true;
		}

			try {

				if(address!=null && address.startsWith("172")) {

					peerPort = new InetSocketAddress("172.168.178.1", 14556);
					bindPort = new InetSocketAddress("172.168.178.2", 14550);

				} else {
					address = "localhost";
					peerPort = new InetSocketAddress("localhost", 14556);
					bindPort = new InetSocketAddress("localhost", 14550);
				}

				channel = DatagramChannel.open();
				channel.socket().bind(bindPort);
				channel.configureBlocking(false);
				channel.connect(peerPort);

				parser.start(channel);
				isConnected = true;
				System.out.println("UDP connected to "+address);
				return true;
			} catch(Exception e) {
				System.out.println("Cannot connect to Port: "+e.getMessage()+" "+address);
				close();
				isConnected = false;
			}

		return false;
	}


	public List<Message> getMessageList() {

		return parser.getMessageList();
	}

	@Override
	public Map<Class<?>,MAVLinkMessage> getMavLinkMessageMap() {
		if(parser!=null)
			return parser.getMavLinkMessageMap();
		return null;
	}

	public void write(MAVLinkMessage msg) throws IOException {
		ByteBuffer buf = ByteBuffer.wrap(msg.encode());
		channel.write(buf);
	}

	@Override
	public void registerProxyListener(IMAVLinkMsgListener listener) {
		parser.registerProxyListener(listener);

	}


	public boolean isConnected() {
		return isConnected;
	}

	public DataModel getModel() {
		return model;
	}

	public void close() {
		if(parser!=null)
			parser.stop();
		try {
			if (channel != null) {
				channel.close();
			}
		} catch(IOException e) {

		}
	}






	public static void main(String[] args) {
	//	MAVUdpComm comm = new MAVUdpComm(new DataModel(), "172,168,178,1");
		MAVUdpComm comm = new MAVUdpComm(new DataModel(), "127.0.0.1");
		comm.open();



		long time = System.currentTimeMillis();


		try {


			ModelCollectorService colService = new ModelCollectorService(comm.getModel());
			colService.start();

			System.out.println("Started");

			while(System.currentTimeMillis()< (time+60000)) {

				//	comm.model.state.print("NED:");
				//				System.out.println("REM="+comm.model.battery.p+" VOLT="+comm.model.battery.b0+" CURRENT="+comm.model.battery.c0);
				System.out.println("ANGLEX="+comm.model.attitude.aX+" ANGLEY="+comm.model.attitude.aY+" "+comm.model.sys.toString());
				Thread.sleep(1000);


			}


			for(Message m : comm.getMessageList()) {
				System.out.println(m.severity+": "+m.msg);
			}

			colService.stop();
			comm.close();

			ExecutorService.shutdown();

			System.out.println(colService.getModelList().size()+" models collected");


			//			for(int i=0;i<colService.getModelList().size();i++) {
			//				DataModel m = colService.getModelList().get(i);
			//				System.out.println(m.attitude.aX);
			//			}


		} catch (Exception e) {
			comm.close();
			// TODO Auto-generated catch block
			e.printStackTrace();

		}




	}

	@Override
	public void addModeChangeListener(IMSPModeChangedListener listener) {
		parser.addModeChangeListener(listener);

	}

}
