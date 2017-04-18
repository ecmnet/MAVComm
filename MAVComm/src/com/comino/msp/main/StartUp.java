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

import org.mavlink.messages.lquac.msg_msp_micro_grid;
import org.mavlink.messages.lquac.msg_msp_micro_slam;
import org.mavlink.messages.lquac.msg_msp_status;

import com.comino.mav.control.IMAVMSPController;
import com.comino.mav.control.impl.MAVProxyController2;
import com.comino.msp.log.MSPLogger;
import com.comino.msp.main.commander.MSPCommander;
import com.comino.msp.model.segment.Grid;
import com.comino.msp.model.segment.Slam;

public class StartUp implements Runnable {

	private IMAVMSPController    control = null;
	private MSPConfig	          config = null;

	private MSPCommander commander = null;;

	public StartUp(String[] args) {

		config  = MSPConfig.getInstance("home/pi","msp.properties");
		System.out.println("MSPService version "+config.getVersion());

		if(args.length>0)
			control = new MAVProxyController2(true);
		else
			control = new MAVProxyController2(false);

		MSPLogger.getInstance(control);

		commander = new MSPCommander(control);

		// Start services if required

		control.start();
		MSPLogger.getInstance().writeLocalMsg("MAVProxy "+config.getVersion()+" loaded");
		Thread worker = new Thread(this);
		worker.start();

	}



	public static void main(String[] args) {
		new StartUp(args);

	}



	@Override
	public void run() {
		long tms = System.currentTimeMillis();
		Grid s = new Grid();
		while(true) {
			try {
				Thread.sleep(200);
				if(!control.isConnected()) {
					if(control.isConnected())
						control.close();
					control.connect();
				}

				float f = (float)(Math.random()-0.5);

				s.setBlock(f,0.1f);
				s.setBlock(0.3f,1.0f);
				s.setBlock(-0.8f,-1.5f);


				msg_msp_micro_grid grid = new msg_msp_micro_grid(2,1);
				grid.tms = System.nanoTime() / 1000;
				grid.cx = 0.1f;
				grid.cy = 0.2f;
				grid.resolution = s.getResolution();
			    s.toArray(grid.data);
				control.sendMAVLinkMessage(grid);

				s.setBlock(f,0.1f, false);

				Thread.sleep(200);

				msg_msp_status msg = new msg_msp_status(2,1);
//				msg.arch=" ".toCharArray();
//				msg.version="1".toCharArray();
                msg.uptime_ms = System.currentTimeMillis()/1000;
				msg.unix_time_us = System.currentTimeMillis() * 1000;
				control.sendMAVLinkMessage(msg);

				if(!Float.isNaN(control.getCurrentModel().hud.h))
				   System.out.println("Altitude:"+control.getCurrentModel().hud.h);

			} catch (Exception e) {
				control.close();
			}
		}

	}

}
