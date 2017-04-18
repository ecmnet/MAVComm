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
import java.util.Map;

import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.messages.MAV_CMD;
import org.mavlink.messages.MAV_MODE_FLAG;
import org.mavlink.messages.lquac.msg_command_long;
import org.mavlink.messages.lquac.msg_heartbeat;

import com.comino.mav.comm.IMAVComm;
import com.comino.mav.mavlink.MAVLinkToModelParser;
import com.comino.msp.main.control.listener.IMAVLinkListener;
import com.comino.msp.main.control.listener.IMAVMessageListener;
import com.comino.msp.main.control.listener.IMSPStatusChangedListener;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.collector.ModelCollectorService;
import com.comino.msp.model.segment.LogMessage;
import com.comino.msp.model.segment.Status;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;


public class MAVSerialComm_depr implements IMAVComm {


	private SerialPort 			serialPort;
	private String	        port;

	private DataModel 			model = null;

	private MAVLinkToModelParser parser = null;

	private static IMAVComm com = null;
	private int    baudrate = 921600;


	public static IMAVComm getInstance(DataModel model, int baudrate) {
		if(com==null)
			com = new MAVSerialComm_depr(model, baudrate);
		return com;
	}

	private MAVSerialComm_depr(DataModel model, int baudrate) {
		this.baudrate = baudrate;
		this.model = model; int i=0;
		System.out.println("Searching ports... ");
		String[] list = SerialPortList.getPortNames();

		if(list.length>0) {
			for(i=0;i<list.length;i++) {
				if(list[i].contains("tty.SLAB") || list[i].contains("tty.usb") || list[i].contains("AMA0")) {
					break;
				}
			}

			port = list[i];
		}
		else
			port ="/dev/tty.SLAB_USBtoUART";

		serialPort = new SerialPort(port);
		parser = new MAVLinkToModelParser(model, this);
//		parser.start(new SerialPortChannel(serialPort));

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
		System.out.println("Serial port opened: "+port);
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
	public void write(MAVLinkMessage msg) throws IOException {
		try {
			serialPort.writeBytes(msg.encode());
		} catch (SerialPortException e) {
			throw new IOException(e.getMessage());
		}
	}

	@Override
	public void addMAVLinkListener(IMAVLinkListener listener) {
		parser.addMAVLinkListener(listener);

	}


	public static void main(String[] args) {
		IMAVComm comm = new MAVSerialComm_depr(new DataModel(),921600);
		comm.open();


		long time = System.currentTimeMillis();


		try {


			ModelCollectorService colService = new ModelCollectorService(comm.getModel());
			colService.start();


		//	while(System.currentTimeMillis()< (time+30000)) {

			while(true) {


				msg_command_long cmd = new msg_command_long(255,1);
				cmd.target_system = 1;
				cmd.target_component = 1;
				cmd.command = MAV_CMD.MAV_CMD_DO_SET_MODE;
				cmd.confirmation = 0;

				cmd.param1 = MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED;
				cmd.param2 = 2;


				try {
					comm.write(cmd);
					System.out.println("Execute: "+cmd.toString());
				} catch (IOException e1) {
				    System.err.println(e1.getMessage());
				}

				msg_heartbeat msg = 	(msg_heartbeat) comm.getMavLinkMessageMap().get(msg_heartbeat.class);
				if(msg!=null)
			      System.out.println(msg.custom_mode);
//				//		comm.getModel().state.print("NED:");
				//	System.out.println("REM="+comm.model.battery.p+" VOLT="+comm.model.battery.b0+" CURRENT="+comm.model.battery.c0);
				//   System.out.println("ANGLEX="+comm.model.attitude.aX+" ANGLEY="+comm.model.attitude.aY+" "+comm.model.sys.toString());
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
		return 0;
	}

}
