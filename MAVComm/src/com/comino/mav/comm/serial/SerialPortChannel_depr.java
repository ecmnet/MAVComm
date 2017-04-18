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


package com.comino.mav.comm.serial;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

import jssc.SerialPort;
import jssc.SerialPortException;


public class SerialPortChannel_depr implements ByteChannel {

	private final SerialPort serialPort;

    public SerialPortChannel_depr(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    @Override
    public int read(ByteBuffer buffer) throws IOException {
        try {
            int available = serialPort.getInputBufferBytesCount();
            if (available <= 0) {
                return 0;
            }
            byte[] b = serialPort.readBytes(Math.min(available, buffer.remaining()));
            if (b != null) {
                buffer.put(b);
                return b.length;
            } else {
                return 0;
            }
        } catch (SerialPortException e) {
        	System.err.println(e.getMessage());
            throw new IOException(e);
        }
    }

    @Override
    public int write(ByteBuffer buffer) throws IOException {
        try {
            byte[] b = new byte[buffer.remaining()];
            buffer.get(b);
            return serialPort.writeBytes(b) ? b.length : 0;
        } catch (SerialPortException e) {
        	System.err.println(e.getMessage());
            throw new IOException(e);
        }
    }

    @Override
    public boolean isOpen() {
        return serialPort.isOpened();
    }

    @Override
    public void close() throws IOException {
        try {
            serialPort.closePort();
        } catch (SerialPortException e) {
            throw new IOException(e);
        }
    }
}