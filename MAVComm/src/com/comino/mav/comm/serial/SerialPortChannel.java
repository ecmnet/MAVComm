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

package com.comino.mav.comm.serial;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

import jssc.SerialPort;
import jssc.SerialPortException;


public class SerialPortChannel implements ByteChannel {

	private final SerialPort serialPort;

    public SerialPortChannel(SerialPort serialPort) {
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