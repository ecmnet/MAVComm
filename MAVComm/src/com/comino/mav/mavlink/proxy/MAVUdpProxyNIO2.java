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


package com.comino.mav.mavlink.proxy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.Iterator;

import org.mavlink.MAVLinkReader;
import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.messages.MAV_CMD;
import org.mavlink.messages.lquac.msg_command_long;
import org.mavlink.messages.lquac.msg_heartbeat;

import com.comino.mav.comm.IMAVComm;
import com.comino.msp.main.control.listener.IMAVLinkListener;


public class MAVUdpProxyNIO2 implements IMAVLinkListener, Runnable {

	private SocketAddress 			bindPort = null;
	private SocketAddress 			peerPort;
	private DatagramChannel 		channel = null;

	private HashMap<Class<?>,IMAVLinkListener> listeners = null;

	private MAVLinkReader reader;

	private Selector selector;

	private IMAVComm comm;

	private boolean 			isConnected = false;


	//	public MAVUdpProxy() {
	//		this("172.168.178.2",14550,"172.168.178.1",14555);
	//	}


	public MAVUdpProxyNIO2(String peerAddress, int pPort, String bindAddress, int bPort, IMAVComm comm) {

		peerPort = new InetSocketAddress(peerAddress, pPort);
		bindPort = new InetSocketAddress(bindAddress, bPort);
		reader = new MAVLinkReader(1);

		this.comm = comm;

		listeners = new HashMap<Class<?>,IMAVLinkListener>();

		System.out.println("Proxy (NIO2): BindPort="+bPort+" PeerPort="+pPort);

	}

	public boolean open() {

		if(channel!=null && channel.isConnected()) {
			isConnected = true;
			return true;
		}
		while(!isConnected) {
			try {

				isConnected = true;
				//			System.out.println("Connect to UDP channel");
				try {
					channel = DatagramChannel.open();
					channel.socket().bind(bindPort);
					channel.socket().setTrafficClass(0x10);
					channel.configureBlocking(false);

				} catch (IOException e) {
					continue;
				}
				channel.connect(peerPort);
				selector = Selector.open();

				new Thread(this).start();

				return true;
			} catch(Exception e) {
				close();
				isConnected = false;

			}
		}
		return false;
	}

	public boolean isConnected() {
		return isConnected;
	}


	public void close() {
		isConnected = false;

		try {
			selector.close();
			if (channel != null) {
				channel.disconnect();
				channel.close();
			}
		} catch(Exception e) {

		}
	}

	public void registerListener(Class<?> clazz, IMAVLinkListener listener) {
		listeners.put(clazz, listener);
	}

	@Override
	public void run() {

		ByteBuffer rxBuffer = ByteBuffer.allocate(4096);

		SelectionKey key = null;
		MAVLinkMessage msg = null;

		try {
			channel.register(selector, SelectionKey.OP_READ );

			if(comm.isConnected()) {
				msg_heartbeat hb = new msg_heartbeat(255,1);
				hb.isValid = true;
				comm.write(hb);
			}


			while(isConnected) {

				if(selector.select()==0)
					continue;

				Iterator<?> selectedKeys = selector.selectedKeys().iterator();

				while (selectedKeys.hasNext()) {
					key = (SelectionKey) selectedKeys.next();
					selectedKeys.remove();

					if (!key.isValid()) {
						continue;
					}

					if (key.isReadable()) {
						try {
							if(channel.isConnected() && channel.receive(rxBuffer)!=null) {
								msg = reader.getNextMessage(rxBuffer.array(), rxBuffer.position());
								rxBuffer.clear();
								if(msg!=null) {
									IMAVLinkListener listener = listeners.get(msg.getClass());
									if(listener!=null)
										listener.received(msg);
									else {
										if(comm.isConnected()) {
											comm.write(msg);
										}
									}
								}
							}
						} catch(Exception io) { }
					}
				}
			}
		} catch(Exception e) {
			close();
			isConnected = false;
		}
	}

	public  void write(MAVLinkMessage msg) throws IOException {
		if(msg!=null && channel!=null && channel.isConnected()) {
			channel.write(ByteBuffer.wrap(msg.encode()));
		}

	}


	@Override
	public void received(Object o) {
		try {
			write((MAVLinkMessage) o);
		} catch (IOException e) {

		}
	}

}
