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


package com.comino.mav.comm.highspeedserial;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.locks.LockSupport;

import org.mavlink.MAVLinkReader;
import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.messages.MAV_CMD;
import org.mavlink.messages.MAV_MODE_FLAG;
import org.mavlink.messages.lquac.msg_command_long;
import org.mavlink.messages.lquac.msg_heartbeat;
import org.mavlink.messages.lquac.msg_serial_control;
import org.mavlink.messages.lquac.msg_vision_position_estimate;

import com.comino.mav.comm.IMAVComm;
import com.comino.mav.mavlink.MAVLinkReader2;
import com.comino.mav.mavlink.MAVLinkToModelParser;
import com.comino.msp.main.control.listener.IMAVLinkListener;
import com.comino.msp.main.control.listener.IMAVMessageListener;
import com.comino.msp.main.control.listener.IMSPStatusChangedListener;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.collector.ModelCollectorService;
import com.comino.msp.model.segment.LogMessage;
import com.comino.msp.model.segment.Status;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;


public class MAVHighSpeedSerialComm2 implements IMAVComm, Runnable {

	//	private static final int BAUDRATE  = 57600


	private SerialAMA0 ama0     = null;
	private String	            port;

	private DataModel 		    model = null;


	private MAVLinkToModelParser parser = null;
	private MAVLinkReader2 reader;

	private static IMAVComm com = null;
	MAVLinkMessage msg = null;

	private ByteBuffer rxBuffer = ByteBuffer.allocate(32768);
	private boolean isRunning;

	public static IMAVComm getInstance(DataModel model, int baudrate, boolean isUSB) {
		if(com==null)
			com = new MAVHighSpeedSerialComm2(model, baudrate);
		return com;
	}

	private MAVHighSpeedSerialComm2(DataModel model, int baudrate) {
		this.model = model; int i=0;
		System.out.println("Searching ports... ");
		ama0 = new SerialAMA0();
		port = "AMA0";

		parser = new MAVLinkToModelParser(model, this);
		this.reader = new MAVLinkReader2(3, false);

	}

	/* (non-Javadoc)
	 * @see com.comino.px4.control.serial.IPX4Comm#open()
	 */
	@Override
	public boolean open() {
		isRunning = true;
		ama0.open();
		System.out.println("Serial (HighSpeed2) port opened: "+port);
		model.sys.setStatus(Status.MSP_CONNECTED, true);
		new Thread(this).start();
		return true;
	}

	@Override
	public void run() {
		int len=0;
		while(isRunning) {
			len = ama0.getInputBufferBytesCount();
            if(len>0) {
            	rxBuffer.put(ama0.readBytes(len),0,len);


            	rxBuffer.flip();
				while(rxBuffer.hasRemaining())
					reader.readMavLinkMessageFromBuffer(rxBuffer.get() & 0x00FF);
				rxBuffer.compact();

				while((msg=reader.getNextMessage())!=null)
					try {
						parser.parseMessage(msg);
					} catch (IOException e) {
					}
            }
            LockSupport.parkNanos(1000000);
		}
	}



	/* (non-Javadoc)
	 * @see com.comino.px4.control.serial.IPX4Comm#getModel()
	 */
	@Override
	public DataModel getModel() {
		return model;
	}


	@Override
	public Map<Class<?>,MAVLinkMessage> getMavLinkMessageMap() {
		return parser.getMavLinkMessageMap();
	}

	/* (non-Javadoc)
	 * @see com.comino.px4.control.serial.IPX4Comm#close()
	 */
	@Override
	public void close() {
		isRunning = false;
		ama0.close();

	}



	/* (non-Javadoc)
	 * @see com.comino.px4.control.serial.IPX4Comm#write(org.mavlink.messages.MAVLinkMessage)
	 */
	@Override
	public  void write(MAVLinkMessage msg) throws IOException {
		try {
			ama0.writeBytes(msg.encode());
		} catch (Exception e) { }
	}

	@Override
	public void addMAVLinkListener(IMAVLinkListener listener) {
		parser.addMAVLinkListener(listener);

	}


	@Override
	public boolean isConnected() {
		return true;
	}

	@Override
	public void addStatusChangeListener(IMSPStatusChangedListener listener) {
		parser.addStatusChangeListener(listener);

	}

	@Override
	public void addMAVMessageListener(IMAVMessageListener listener) {
		parser.addMAVMessagekListener(listener);

	}

	@Override
	public void writeMessage(LogMessage m) {


	}

	@Override
	public boolean isSerial() {
		return true;
	}


	@Override
	public int getErrorCount() {
		return reader.getLostPackages();
	}





	public static void main(String[] args) {
		IMAVComm comm = new MAVHighSpeedSerialComm2(new DataModel(), 921600);
		comm.open();


		long time = System.currentTimeMillis();


		try {


//			3


			//	while(System.currentTimeMillis()< (time+30000)) {

			while(true) {


//				msg_command_long cmd = new msg_command_long(255,1);
//				cmd.target_system = 1;
//				cmd.target_component = 1;
//				cmd.command = MAV_CMD.MAV_CMD_DO_SET_MODE;
//				cmd.confirmation = 0;
//
//				cmd.param1 = MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED;
//				cmd.param2 = 2;
//
//
//				try {
//					comm.write(cmd);
//					System.out.println("Execute: "+cmd.toString());
//				} catch (IOException e1) {
//					System.err.println(e1.getMessage());
//				}

				msg_heartbeat msg = 	(msg_heartbeat) comm.getMavLinkMessageMap().get(msg_heartbeat.class);
				if(msg!=null)
					System.out.println(msg.custom_mode);
				//				//		comm.getModel().state.print("NED:");
				//	System.out.println("REM="+comm.model.battery.p+" VOLT="+comm.model.battery.b0+" CURRENT="+comm.model.battery.c0);
				  System.out.println("ANGLEX="+comm.getModel().attitude.p+" ANGLEY="+comm.getModel().attitude.r+" "+comm.getModel().sys.toString());
				Thread.sleep(2000);
			}

			//			colService.stop();
			//			comm.close();
			//
			//			System.out.println(colService.getModelList().size()+" models collected");


			//			for(int i=0;i<colService.getModelList().size();i++) {
			//				DataModel m = colService.getModelList().get(i);
			//				System.out.println(m.attitude.aX);
			//			}


		} catch (Exception e) {
			comm.close();
			e.printStackTrace();

		}




	}


}
