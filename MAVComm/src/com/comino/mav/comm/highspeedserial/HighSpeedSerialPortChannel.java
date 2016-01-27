package com.comino.mav.comm.highspeedserial;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;


public class HighSpeedSerialPortChannel implements ByteChannel {
    
	private final SerialAMA0 serialPort;

    public HighSpeedSerialPortChannel(SerialAMA0 serialPort) {
        this.serialPort = serialPort;
    }

    @Override
    public int read(ByteBuffer buffer) throws IOException {
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
    }

    @Override
    public int write(ByteBuffer buffer) throws IOException {
            byte[] b = new byte[buffer.remaining()];
            buffer.get(b);
            serialPort.writeBytes(b);
            return b.length;

    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public void close() throws IOException {
       
            serialPort.close();
       
    }
}