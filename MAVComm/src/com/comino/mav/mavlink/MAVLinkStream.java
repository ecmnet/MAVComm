/****************************************************************************
 *
 *   Copyright (c) 2017 Eike Mansfeld ecm@gmx.de. All rights reserved.
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


package com.comino.mav.mavlink;

import java.io.EOFException;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.util.concurrent.locks.LockSupport;

import org.mavlink.messages.MAVLinkMessage;


public class MAVLinkStream {

	private final ByteChannel channel;

	private ByteBuffer rxBuffer = ByteBuffer.allocate(2048);

	static byte[] buf = new byte[2048];

	private MAVLinkReader reader;
	private int protocol;


	public MAVLinkStream(ByteChannel channel) {
		this.channel = channel;
		this.rxBuffer.flip();
		this.reader = new MAVLinkReader(0);
	}

	public int getProtocol() {
		return protocol;
	}

	public int getAvailableBytes() {
		return rxBuffer.remaining();
	}


	/**
	 * Read message.
	 *
	 * @return MAVLink message or null if no more messages available at the moment
	 * @throws java.io.IOException on IO error
	 */
	public MAVLinkMessage read() throws IOException, EOFException {
		MAVLinkMessage msg=null;
		int n = 1;
		while (true) {
			try {

				rxBuffer.get(buf, 0, n);
				msg = reader.getNextMessage(buf, n);
				return msg;

			} catch (BufferUnderflowException e) {
				// Try to refill buffer
				try {
				    LockSupport.parkNanos(500000);
					rxBuffer.compact();
					n = channel.read(rxBuffer);
					rxBuffer.flip();

				} catch (Exception ioe) {
					throw new IOException(ioe.getMessage());
				}
				//rxBuffer.flip();
			}
		}
	}


}
