package com.comino.mav.mavlink;

import java.io.EOFException;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.util.concurrent.locks.LockSupport;

import org.mavlink.IMAVLinkMessage;
import org.mavlink.MAVLinkReader;
import org.mavlink.messages.MAVLinkMessage;


public class MAVLinkStream {

	private final ByteChannel channel;


	private ByteBuffer rxBuffer = ByteBuffer.allocate(8192);

	byte[] buf = new byte[500];

	private MAVLinkReader reader;


	public MAVLinkStream(ByteChannel channel) {
		this.channel = channel;
		this.rxBuffer.flip();
		this.reader = new MAVLinkReader(IMAVLinkMessage.MAVPROT_PACKET_START_V10);
	}


	/**
	 * Read message.
	 *
	 * @return MAVLink message or null if no more messages available at the moment
	 * @throws java.io.IOException on IO error
	 */
	public MAVLinkMessage read() throws IOException, EOFException {


		int n = 1;
		while (true) {
			try {
				rxBuffer.get(buf, 0, n);
				return reader.getNextMessage(buf, n);
			} catch (BufferUnderflowException e) {
				// Try to refill buffer
				rxBuffer.compact();
				try {
					  LockSupport.parkNanos(3000000);
					  n = channel.read(rxBuffer);
				} catch (IOException ioe) {
					rxBuffer.flip();
					throw ioe;
				}
				rxBuffer.flip();
				if (n <= 0) {
//                     System.out.println("end");
					//return null;
				}
			}
		}
	}


}
