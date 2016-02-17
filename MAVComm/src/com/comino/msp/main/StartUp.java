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

import com.comino.mav.control.impl.MAVProxyController;
import com.comino.msp.main.control.listener.IMAVLinkMsgListener;

public class StartUp {

	MAVProxyController control = null;
	MSPConfig	          config  = null;

	public StartUp() {

		config  = MSPConfig.getInstance("msp.properties");
		System.out.println("MSPControlService version "+config.getVersion());
		control = new MAVProxyController();


		// TODO 1.0: Start services if required

		// TODO 1.0: register MSP commands here

		control.registerListener(msg_msp_command.class, new IMAVLinkMsgListener() {
			@Override
			public void received(Object o) {
				msg_msp_command hud = (msg_msp_command)o;
				System.out.println("MSP Command "+hud.command+" executed");
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
		new StartUp();

	}

}
