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

				try {
					rxBuffer.compact();
					LockSupport.parkNanos(200000);
					n = channel.read(rxBuffer);
				} catch (IOException ioe) {
					rxBuffer.flip();
					LockSupport.parkNanos(500000);
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
