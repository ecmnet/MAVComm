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

package com.comino.msp.main;

import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.messages.MAV_CMD;
import org.mavlink.messages.lquac.msg_system_time;
import org.mavlink.messages.lquac.msg_timesync;

import com.comino.mav.control.IMAVController;
import com.comino.mav.control.impl.MAVUdpController;
import com.comino.msp.log.MSPLogger;
import com.comino.msp.main.control.listener.IMAVLinkListener;

public class MAVTimeSyncTest implements Runnable, IMAVLinkListener {

	private IMAVController control = null;
	MSPConfig	           config  = null;

	public MAVTimeSyncTest(String[] args) {


	//		control = new MAVUdpController("172.168.178.1",14555,14550, false);
		     control = new MAVUdpController("127.0.0.1",14557,14540, true);
    //     	control.enableFileLogging(true, null);
         	control.addMAVLinkListener(this);

		while(!control.isConnected()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			control.connect();
			control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_LOGGING_STOP);
		}


		MSPLogger.getInstance(control);

		new Thread(this).start();

	}

	public static void main(String[] args) {
		new MAVTimeSyncTest(args);

	}

	@Override
	public void run() {

		msg_system_time time = new msg_system_time(255,1);
		time.time_unix_usec = System.currentTimeMillis() * 1000;
		time.time_boot_ms = 0;
		control.sendMAVLinkMessage(time);

		msg_timesync sync_s = new msg_timesync(255,1);
		sync_s.tc1 = 0;
		sync_s.ts1 = System.currentTimeMillis() * 1000000;
		control.sendMAVLinkMessage(sync_s);

		while(true) {
			try {
				Thread.sleep(1000);



//				if(control.isConnected())
//				  System.out.println(control.getCurrentModel().hud.ag);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}


		}

	}

	@Override
	public void received(Object o) {

//		if(o instanceof msg_system_time) {
//			msg_system_time stime = (msg_system_time)o;
//			long dt = stime.time_unix_usec -(System.currentTimeMillis()*1000);
//			System.err.println("SystemTimes: dt_usec = "+dt);
//		}




	}

}
