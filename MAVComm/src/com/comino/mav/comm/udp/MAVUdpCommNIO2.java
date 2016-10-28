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


package com.comino.mav.comm.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.LockSupport;

import org.mavlink.MAVLinkReader;
import org.mavlink.MAVLinkReader2;
import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.messages.lquac.msg_heartbeat;
import org.mavlink.messages.lquac.msg_system_time;

import com.comino.mav.comm.IMAVComm;
import com.comino.mav.mavlink.MAVLinkToModelParser;
import com.comino.msp.main.control.listener.IMAVLinkListener;
import com.comino.msp.main.control.listener.IMAVMessageListener;
import com.comino.msp.main.control.listener.IMSPStatusChangedListener;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.collector.ModelCollectorService;
import com.comino.msp.model.segment.LogMessage;
import com.comino.msp.model.segment.Status;
import com.comino.msp.utils.ExecutorService;


public class MAVUdpCommNIO2 implements IMAVComm, Runnable {


	private DataModel 				model = null;

	private SocketAddress 			bindPort = null;
	private SocketAddress 			peerPort = null;
	private DatagramChannel 		channel = null;

	private MAVLinkToModelParser	parser = null;

	private boolean					isConnected = false;

	private MAVLinkReader reader;

	private Selector selector;

	private static MAVUdpCommNIO2 com = null;

	public static MAVUdpCommNIO2 getInstance(DataModel model, String peerAddress, int peerPort, int bindPort) {
		if(com==null)
			com = new MAVUdpCommNIO2(model, peerAddress, peerPort, bindPort);
		return com;
	}

	private MAVUdpCommNIO2(DataModel model, String peerAddress, int pPort, int bPort) {
		this.model = model;
		this.parser = new MAVLinkToModelParser(model,this);
		this.peerPort = new InetSocketAddress(peerAddress,pPort);
		this.bindPort = new InetSocketAddress(bPort);
		this.reader = new MAVLinkReader(2);

		System.out.println("Vehicle (NIO2): BindPort="+bPort+" PeerPort="+pPort);

	}

	public boolean open() {

		if(channel!=null && channel.isConnected()) {
			isConnected = true;
			return true;
		}

		try {
			isConnected = true;
			System.out.println("Try to open UDP channel....");
			channel = DatagramChannel.open();
			channel.bind(bindPort);
			channel.socket().setTrafficClass(0x10);
			channel.connect(peerPort);

			channel.configureBlocking(false);

			selector = Selector.open();

			LockSupport.parkNanos(10000000);

			new Thread(this).start();


		} catch(Exception e) {
			model.sys.setStatus(Status.MSP_CONNECTED,false);
			close();
			isConnected = false;
		}

		return false;
	}

	@Override
	public void run() {
		ByteBuffer rxBuffer = ByteBuffer.allocate(1024);
		SelectionKey key = null;
		try {
			channel.register(selector, SelectionKey.OP_READ);

			msg_heartbeat hb = new msg_heartbeat(255,1);
			hb.isValid = true;
			write(hb);

			MAVLinkMessage msg = null;

			while(isConnected) {

				if(selector.select(500)==0)
					throw new IOException("Timeout");

				Iterator<?> selectedKeys = selector.selectedKeys().iterator();

				while (selectedKeys.hasNext()) {
					key = (SelectionKey) selectedKeys.next();
					selectedKeys.remove();
					if (!key.isValid()) {
						continue;
					}

					if (key.isReadable()) {
						if(channel.isConnected() && channel.receive(rxBuffer)!=null) {
							msg = reader.getNextMessage(rxBuffer.array(), rxBuffer.position());
							rxBuffer.clear();
							if(msg!=null) {
								parser.parseMessage(msg);
							}
						}
					}
				}
			}

		} catch(Exception e) {
			rxBuffer.clear();
			model.sys.setStatus(Status.MSP_CONNECTED,false);
			close();
			isConnected = false;
		}

	}


	@Override
	public Map<Class<?>,MAVLinkMessage> getMavLinkMessageMap() {
		if(parser!=null)
			return parser.getMavLinkMessageMap();
		return null;
	}

	public void write(MAVLinkMessage msg) throws IOException {
		if(msg!=null && channel!=null && channel.isOpen())
			channel.write(ByteBuffer.wrap(msg.encode()));
	}

	@Override
	public void addMAVLinkListener(IMAVLinkListener listener) {
		parser.addMAVLinkListener(listener);

	}

	@Override
	public void addStatusChangeListener(IMSPStatusChangedListener listener) {
		parser.addStatusChangeListener(listener);

	}

	@Override
	public void addMAVMessageListener(IMAVMessageListener listener) {
		parser.addMAVMessagekListener(listener);

	}

	@Override
	public boolean isSerial() {
		return false;
	}

	public boolean isConnected() {
		return parser.isConnected();
	}

	public DataModel getModel() {
		return model;
	}

	public void close() {
		try {
			selector.close();
			if (channel != null) {
				channel.disconnect();
				channel.close();
			}
		} catch(Exception e) {

		}
		//	LockSupport.parkNanos(1000000000);
	}



	public static void main(String[] args) {
		MAVUdpCommNIO2 comm = new MAVUdpCommNIO2(new DataModel(), "127.0.0.1", 14556, 14550);
		//	MAVUdpComm comm = new MAVUdpComm(new DataModel(), "192.168.4.1", 14555,"0.0.0.0",14550);

		comm.open();



		long time = System.currentTimeMillis();


		try {


			ModelCollectorService colService = new ModelCollectorService(comm.getModel());
			colService.start();

			System.out.println("Started");

			while(System.currentTimeMillis()< (time+60000)) {

				//					comm.model.state.print("NED:");
				//								System.out.println("REM="+comm.model.battery.p+" VOLT="+comm.model.battery.b0+" CURRENT="+comm.model.battery.c0);

				if(comm.isConnected)
					System.out.println("ANGLEX="+comm.model.hud.aX+" ANGLEY="+comm.model.hud.aY+" "+comm.model.sys.toString());

				Thread.sleep(1000);


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
			e.printStackTrace();

		}


	}

	@Override
	public void writeMessage(LogMessage m) {
		parser.writeMessage(m);

	}

}
