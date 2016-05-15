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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.messages.lquac.msg_heartbeat;
import org.mavlink.messages.lquac.msg_system_time;

import com.comino.mav.comm.IMAVComm;
import com.comino.mav.mavlink.MAVLinkToModelParser;
import com.comino.msp.main.control.listener.IMAVLinkListener;
import com.comino.msp.main.control.listener.IMAVMessageListener;
import com.comino.msp.main.control.listener.IMSPModeChangedListener;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.collector.ModelCollectorService;
import com.comino.msp.model.segment.LogMessage;
import com.comino.msp.model.segment.Status;
import com.comino.msp.utils.ExecutorService;


public class MAVUdpCommNIO2 implements IMAVComm {

	private DataModel model = null;
	private ByteChannel channel = null;
	private MAVLinkToModelParser parser = null;
	private boolean isConnected = false;
	private static MAVUdpCommNIO2 com = null;

	private AtomicReference<DatagramSocket> socketRef = new AtomicReference<>();
	private int serverPort;
	private int hostPort;
	private InetAddress hostAdd;
	private DatagramPacket sendPacket;
	private DatagramPacket receivePacket;

	public static MAVUdpCommNIO2 getInstance(DataModel model, String peerAddress, int peerPort, String bindAddress, int bindPort) {
		if (com == null) {
			com = new MAVUdpCommNIO2(model, bindPort);
		}
		return com;
	}

	private MAVUdpCommNIO2(DataModel model, int port) {
		this.model = model;
		this.parser = new MAVLinkToModelParser(model, this);
		this.serverPort = port;
	}

	public boolean open() {

		if (channel != null && channel.isOpen()) {
			isConnected = true;
			return true;
		}

		try {
			final DatagramSocket socket = new DatagramSocket(null);
			socket.setBroadcast(true);
			socket.setReuseAddress(true);
			socket.bind(new InetSocketAddress(serverPort));
			socketRef.set(socket);

			System.out.println("Port used: "+socket.getPort());

			channel = new ByteChannel() {

				@Override
				public int read(ByteBuffer readData) throws IOException {
					final DatagramSocket socket = socketRef.get();
					if (socket == null) {
						return 0;
					}

					if (receivePacket == null)
						receivePacket = new DatagramPacket(readData.array(), readData.array().length);
					else
						receivePacket.setData(readData.array());

					System.out.println("Socket: 1");
					socket.receive(receivePacket);
					System.out.println("Socket: 2");
					hostAdd = receivePacket.getAddress();
					hostPort = receivePacket.getPort();
					return receivePacket.getLength();
				}

				@Override
				public boolean isOpen() {
					return socket.isConnected();
				}

				@Override
				public void close() throws IOException {
					final DatagramSocket socket = socketRef.get();
					if (socket != null) {
						if (socket.isConnected())
							socket.disconnect();
						socket.close();
					}
				}

				@Override
				public int write(ByteBuffer buffer) throws IOException {
					final DatagramSocket socket = socketRef.get();
					if (socket == null)
						return 0;

					try {
						if (hostAdd != null) { // We can't send to our sister until they
							// have connected to us
							if (sendPacket == null)
								sendPacket = new DatagramPacket(buffer.array(), buffer.array().length, hostAdd, hostPort);
							else {
								sendPacket.setData(buffer.array(), 0, buffer.array().length);
								sendPacket.setAddress(hostAdd);
								sendPacket.setPort(hostPort);
							}
							socket.send(sendPacket);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					return 0;
				}

			};

			msg_heartbeat msg = new msg_heartbeat(255, 0);
			msg.isValid = true;
			write(msg);

			msg_system_time time = new msg_system_time(255, 0);
			time.time_unix_usec = System.currentTimeMillis() / 1000L;
			time.isValid = true;
			write(time);

			parser.start(channel);
			isConnected = true;
			System.out.println("UDP connected...");
			return true;
		} catch (Exception e) {
			System.out.println("Cannot connect to Port: " +e.getMessage()+" " + serverPort);
			close();
			isConnected = false;
		}

		return false;
	}

	public List<LogMessage> getMessageList() {
		return parser.getMessageList();
	}

	@Override
	public Map<Class<?>, MAVLinkMessage> getMavLinkMessageMap() {
		if (parser != null)
			return parser.getMavLinkMessageMap();
		return null;
	}

	public void write(MAVLinkMessage msg) throws IOException {
		ByteBuffer buf = ByteBuffer.wrap(msg.encode());
		channel.write(buf);
	}

	@Override
	public void addMAVLinkListener(IMAVLinkListener listener) {
		parser.addMAVLinkListener(listener);
	}

	@Override
	public void addModeChangeListener(IMSPModeChangedListener listener) {
		parser.addModeChangeListener(listener);
	}

	@Override
	public void addMAVMessageListener(IMAVMessageListener listener) {
		parser.addMAVMessagekListener(listener);

	}

	public boolean isConnected() {
		return isConnected;
	}

	public DataModel getModel() {
		return model;
	}

	public void close() {
		if (parser != null)
			parser.stop();
		try {
			if (channel != null) {
				channel.close();
			}
		} catch (IOException e) {

		}
		LockSupport.parkNanos(1000000000);
	}

	public static void main(String[] args) {
		MAVUdpCommNIO2 comm = new MAVUdpCommNIO2(new DataModel(), 14556);


		try {

			while(!comm.isConnected) {
				comm.open();
				Thread.sleep(500);

			}

			System.out.println("connected");

			long time = System.currentTimeMillis();

			ModelCollectorService colService = new ModelCollectorService(comm.getModel());
			colService.start();

			System.out.println("Started");

			while (System.currentTimeMillis() < (time + 60000)) {

				// comm.model.state.print("NED:");
				// System.out.println("REM="+comm.model.battery.p+" VOLT="+comm.model.battery.b0+" CURRENT="+comm.model.battery.c0);

				if (comm.model.sys.isStatus(Status.MSP_CONNECTED))
					System.out.println("ANGLEX=" + comm.model.attitude.aX + " ANGLEY=" + comm.model.attitude.aY + " " + comm.model.sys.toString());
				else
					System.out.println("not connected");

				Thread.sleep(1000);

			}

			for (LogMessage m : comm.getMessageList()) {
				System.out.println(m.severity + ": " + m.msg);
			}

			colService.stop();
			comm.close();

			ExecutorService.shutdown();

			System.out.println(colService.getModelList().size() + " models collected");

			// for(int i=0;i<colService.getModelList().size();i++) {
			// DataModel m = colService.getModelList().get(i);
			// System.out.println(m.attitude.aX);
			// }
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
