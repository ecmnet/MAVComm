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

package com.comino.mav.control.impl;

import com.comino.mav.comm.serial.MAVSerialComm;
import com.comino.mav.control.IMAVController;

/*
 * Direct serial controller up to 115200 baud for telem1 connections e.g. Radio
 */

public class MAVSerialController extends MAVController implements IMAVController {


	public MAVSerialController() {
		super();
		System.out.println("Serial Controller loaded");
		comm = MAVSerialComm.getInstance(model);

	}

	@Override
	public boolean connect() {
		return comm.open();
	}

	@Override
	public boolean isSimulation() {
		return false;
	}

	@Override
	public boolean isConnected() {
		return comm.isConnected();
	}

}
