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

package com.comino.main;

import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.messages.MAV_CMD;
import org.mavlink.messages.lquac.msg_serial_control;

import com.comino.mav.control.IMAVController;
import com.comino.mav.control.impl.MAVUdpController;
import com.comino.msp.execution.control.listener.IMAVLinkListener;
import com.comino.msp.log.MSPLogger;
import com.comino.msp.utils.px4.DefaultTunes;

public class MAVCommTest implements IMAVLinkListener, Runnable {

	private IMAVController control = null;
	MSPConfig	           config  = null;

	public MAVCommTest(String[] args) {

		String peerAddress = null;

		if(args.length> 0) {
			peerAddress  = args[0];
		}


		//		if(args.length>0)
		//			control = new MAVUdpController("172.168.178.1",14555,14550, false);
		//		control = new MAVUdpController("192.168.178.42",14555,14550, false);
		control = new MAVUdpController("127.0.0.1",14580,14540, true);
		//		else
		//		  System.exit(-1);

		while(!control.isConnected()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {

			}
			control.connect();
			control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_LOGGING_STOP);
		}

		control.addMAVLinkListener(this);

		MSPLogger.getInstance(control);

		new Thread(this).start();

	}

	public static void main(String[] args) {
		new MAVCommTest(args);

	}

	public void run() {
		while(true) {
			try {
				if(!control.isConnected()) {
					Thread.sleep(2000);
					control.connect();
					System.out.println();
					continue;
				}
				//   	  control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_REQUEST_AUTOPILOT_CAPABILITIES, 1);
				DefaultTunes.play(control,DefaultTunes.NOTIFY_POSITIVE);

				Thread.sleep(1000);
			} catch (InterruptedException e) {

			}

		}
	}


	@Override
	public void received(Object o) {
		MAVLinkMessage m = (MAVLinkMessage)o;
//		if(m instanceof msg_serial_control)
			System.out.println(m);
		//		if(o instanceof msg_autopilot_version)
		//	    System.out.println(m.messageType+"-"+m.componentId+" "+control.getErrorCount()+":"+o);

	}

}
