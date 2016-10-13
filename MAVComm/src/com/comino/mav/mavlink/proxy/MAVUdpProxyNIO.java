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
import java.util.LinkedList;

import org.mavlink.messages.MAVLinkMessage;

import com.comino.mav.mavlink.MAVLinkStream;
import com.comino.msp.main.control.listener.IMAVLinkListener;


public class MAVUdpProxyNIO implements IMAVLinkListener  {

	private SocketAddress 			bindPort = null;
	private SocketAddress 			peerPort;
	private DatagramChannel 		channel = null;

	private ByteBuffer 				buffer = null;
	private MAVLinkStream			in	   = null;

	private LinkedList<MAVLinkMessage> queue = null;


	private boolean 			isConnected = false;


	//	public MAVUdpProxy() {
	//		this("172.168.178.2",14550,"172.168.178.1",14555);
	//	}


	public MAVUdpProxyNIO(String peerAddress, int pPort, String bindAddress, int bPort) {
		buffer = ByteBuffer.allocate(16384);
		peerPort = new InetSocketAddress(peerAddress, pPort);
		bindPort = new InetSocketAddress(bindAddress, bPort);

		this.queue = new LinkedList<MAVLinkMessage>();


		System.out.println("Proxy: BindPort="+bPort+" PeerPort="+pPort);

	}

	public boolean open() {

		if(channel!=null && channel.isConnected()) {
			isConnected = true;
			return true;
		}
		while(!isConnected) {
			try {

				isConnected = true;
				buffer.clear(); queue.clear();
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
				in = new MAVLinkStream(channel);
				return true;
			} catch(Exception e) {
				System.err.println(e.getMessage());
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
			if (channel != null && channel.isOpen()) {
				channel.close();
			}
		} catch(IOException e) {

		}
	}

	public MAVLinkStream getInputStream() {
		return in;
	}

	//	public synchronized  void write(MAVLinkMessage msg) {
	//		queue.add(msg);
	//	}


	public synchronized  void write(MAVLinkMessage msg) {
		try {

			if(isConnected) {

				if(!channel.isConnected())
					return;

				if(msg!=null) {
					buffer.put(msg.encode());
					buffer.flip();
					channel.write(buffer);
					buffer.compact();
				}
			}


		} catch (Exception e) {
		//	try { Thread.sleep(150); } catch(Exception k) { }
			buffer.clear();
			close();
			isConnected = false;
		}
	}


	@Override
	public void received(Object o) {
		write((MAVLinkMessage) o);
	}

}
