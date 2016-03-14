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

package com.comino.msp.main;

import org.mavlink.messages.lquac.msg_msp_command;

import com.comino.mav.control.IMAVMSPController;
import com.comino.mav.control.impl.MAVProxyController;
import com.comino.msp.log.MSPLogger;
import com.comino.msp.main.control.listener.IMAVLinkListener;

public class StartUp {

	IMAVMSPController    control = null;
	MSPConfig	          config  = null;

	public StartUp(String[] args) {

		config  = MSPConfig.getInstance("msp.properties");
		System.out.println("MSPControlService version "+config.getVersion());

		if(args.length>0)
			control = new MAVProxyController(true);
		else
			control = new MAVProxyController(false);


		MSPLogger.getInstance(control);


		// TODO 1.0: Start services if required

		// TODO 1.0: register MSP commands here

		control.registerListener(msg_msp_command.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_msp_command hud = (msg_msp_command)o;
				MSPLogger.getInstance().writeLocalMsg("MSP Command "+hud.command+" executed");
			}
		});

		control.start();

		control.connect();


		while(true) {
			try {
				Thread.sleep(100);
				if(!control.isConnected())
					control.connect();

			} catch (Exception e) {
				control.close();
			}
		}
	}



	public static void main(String[] args) {
		new StartUp(args);

	}

}
