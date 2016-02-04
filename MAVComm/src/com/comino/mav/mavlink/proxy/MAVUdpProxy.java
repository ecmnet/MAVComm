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

package com.comino.mav.mavlink.proxy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import org.mavlink.messages.MAVLinkMessage;

import com.comino.mav.mavlink.IMAVLinkMsgListener;
import com.comino.mav.mavlink.MAVLinkStream;


public class MAVUdpProxy implements IMAVLinkMsgListener  {

	private SocketAddress 			bindPort = null;
	private SocketAddress 			peerPort;
	private DatagramChannel 		channel = null;

	private ByteBuffer 				buffer = null;
	private MAVLinkStream			in	   = null;


	private boolean 			isConnected = false;

	private String					peer  = null;
	private String					bind  = null;

	public MAVUdpProxy() {
		this("172.168.178.1","172.168.178.2");
	}


	public MAVUdpProxy(String bind, String peer) {
		buffer = ByteBuffer.allocate(8192);
		this.bind = bind;
		this.peer = peer;
	}

	public boolean open() {

		if(channel!=null && channel.isConnected()) {
			isConnected = true;
			return true;
		}
		while(!isConnected) {
			try {
				isConnected = true;
				System.out.println("Connect to UDP channel");
				peerPort = new InetSocketAddress(peer, 14550);
				bindPort = new InetSocketAddress(bind, 14556);
				try {
					channel = DatagramChannel.open();
					channel.socket().bind(bindPort);

					channel.configureBlocking(false);
				} catch (IOException e) {
					e.printStackTrace();
				}
				channel.connect(peerPort);
				in = new MAVLinkStream(channel);
				System.out.println("MAVProxy connected to "+peer);
				return true;
			} catch(Exception e) {
				System.out.println(e.getMessage());
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
			if (channel != null) {
				channel.close();
			}
		} catch(IOException e) {

		}
	}

	public MAVLinkStream getInputStream() {
		return in;
	}


	public void write(MAVLinkMessage msg) {
		try {

			if(isConnected) {

				if(!channel.isConnected())
					throw new IOException("Channel not bound");

				buffer.put(msg.encode());
				buffer.flip();
				channel.write(buffer);
				buffer.compact();
			}


		} catch (IOException e) {
			try { Thread.sleep(1000); } catch(Exception k) { }
			System.out.println(e.getMessage());
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
