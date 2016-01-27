package com.comino.mav.mavlink;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

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
	public MAVLinkMessage read() throws IOException {
		

		int n = 1;
		while (true) {
			try {
			//	LockSupport.parkNanos(3000000);
				
				rxBuffer.get(buf, 0, n);
//				for(int i=0;i<n;i++)
//				System.out.print(buf[i]);
//				System.out.println();
				return reader.getNextMessage(buf, n);
				

			} catch (BufferUnderflowException e) {
				// Try to refill buffer
				rxBuffer.compact();
				try {         
					
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
