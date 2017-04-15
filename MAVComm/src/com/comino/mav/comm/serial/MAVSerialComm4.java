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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

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


public class MAVSerialComm4 implements IMAVComm {

	//	private static final int BAUDRATE  = 57600


	private SerialPort 			serialPort;
	private String	            port;

	private DataModel 		    model = null;


	private MAVLinkToModelParser parser = null;
	private MAVLinkReader2 reader;

	private static IMAVComm com = null;
	MAVLinkMessage msg = null;

	private ByteBuffer rxBuffer = ByteBuffer.allocate(32768);

	private int baudrate = 921600;

	public static IMAVComm getInstance(DataModel model, int baudrate, boolean isUSB) {
		if(com==null)
			com = new MAVSerialComm4(model, baudrate);
		return com;
	}

	private MAVSerialComm4(DataModel model, int baudrate) {
		this.model = model; int i=0;
		this.baudrate = baudrate;
		System.out.println("Searching ports... ");
		String[] list = SerialPortList.getPortNames();


		if(list.length>0) {
			for(i=0;i<list.length;i++) {
				if(list[i].contains("tty.SLAB") || list[i].contains("tty.usb") || list[i].contains("AMA0") || list[i].contains("S1")) {
					break;
				}
			}

			port = list[i];
		}
		else
			port ="/dev/tty.SLAB_USBtoUART";

		serialPort = new SerialPort(port);
		parser = new MAVLinkToModelParser(model, this);
		this.reader = new MAVLinkReader2(3, false);

	}

	/* (non-Javadoc)
	 * @see com.comino.px4.control.serial.IPX4Comm#open()
	 */
	@Override
	public boolean open() {

		while(!open(port ,baudrate,8,1,0)) {
			try {
				if(serialPort.isOpened()) {
					try {
						serialPort.closePort();
					} catch (SerialPortException e) {

					}
				}
				Thread.sleep(1000);
			} catch (Exception e) {	}
		}
		System.out.println("Serial port "+this.getClass().getSimpleName()+" opened: "+port);
		return true;
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
		try {
			serialPort.closePort();
		} catch (SerialPortException e) {

		}
	}

	private boolean open(String portName, int baudRate, int dataBits, int stopBits, int parity) {

		if(serialPort.isOpened())
			return true;

		try {
			serialPort.openPort();
			serialPort.setParams(baudRate, dataBits, stopBits, parity);

			int eventMask = SerialPort.MASK_RXCHAR;
			try {
				serialPort.addEventListener((serialEvent) -> {

					try {
						switch (serialEvent.getEventType()) {
						case SerialPortEvent.RXCHAR:

							rxBuffer.put(serialPort.readBytes());

							rxBuffer.flip();
							while(rxBuffer.hasRemaining())
								reader.readMavLinkMessageFromBuffer(rxBuffer.get() & 0x00FF);
							rxBuffer.compact();

							while((msg=reader.getNextMessage())!=null)
								parser.parseMessage(msg);

							break;

						}
					} catch (Exception e) {
						System.err.println(e.getMessage());
					}
				}, eventMask);
			} catch (SerialPortException e) {
				e.printStackTrace();
			}

		} catch (Exception e2) {
			try {
				serialPort.closePort();
			} catch (SerialPortException e) {
				//e.printStackTrace();
			}
			//System.err.println(e2.getMessage());
			return false;
		}

		System.out.println("Connected to "+serialPort.getPortName());
		model.sys.setStatus(Status.MSP_CONNECTED, true);
		return true;

	}

	/* (non-Javadoc)
	 * @see com.comino.px4.control.serial.IPX4Comm#write(org.mavlink.messages.MAVLinkMessage)
	 */
	@Override
	public  void write(MAVLinkMessage msg) throws IOException {
		try {
			serialPort.writeBytes(msg.encode());
		} catch (Exception e) { }
	}

	@Override
	public void addMAVLinkListener(IMAVLinkListener listener) {
		parser.addMAVLinkListener(listener);

	}


	@Override
	public boolean isConnected() {
		return (serialPort != null && serialPort.isOpened());
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
		IMAVComm comm = new MAVSerialComm4(new DataModel(),921600);
		comm.open();


		long time = System.currentTimeMillis();


		try {


			ModelCollectorService colService = new ModelCollectorService(comm.getModel());
			colService.start();


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

				comm.getMavLinkMessageMap().forEach((a,b) -> {
					System.out.println(b);
				});

				msg_heartbeat msg = 	(msg_heartbeat) comm.getMavLinkMessageMap().get(msg_heartbeat.class);
				if(msg!=null)
					System.out.println(msg.custom_mode);
				//				//		comm.getModel().state.print("NED:");
				System.out.println("REM="+comm.getModel().battery.p+" VOLT="+comm.getModel().battery.b0+" CURRENT="+comm.getModel().battery.c0);
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
