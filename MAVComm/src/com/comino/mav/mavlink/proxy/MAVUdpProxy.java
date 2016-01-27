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
		open();
	}

	public boolean open() {

		if(channel!=null && channel.isConnected())
			return true;

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
			System.out.println("MAVProxy connected");
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			close();
			isConnected = false;
			return false;
		}
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

			buffer.put(msg.encode());
			buffer.flip();
			channel.write(buffer);
			buffer.compact();


		} catch (IOException e) {
			buffer.clear();
			
		}
	}


	@Override
	public void received(Object o) {
		write((MAVLinkMessage) o);
	}









}
