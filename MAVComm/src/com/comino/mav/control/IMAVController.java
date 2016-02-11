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

package com.comino.mav.control;

import java.util.List;
import java.util.Map;

import org.mavlink.messages.MAVLinkMessage;

import com.comino.mav.mavlink.IMAVLinkMsgListener;
import com.comino.msp.main.control.listener.IMSPModeChangedListener;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.collector.ModelCollectorService;
import com.comino.msp.model.segment.Message;

public interface IMAVController {

	public boolean connect();
	public boolean close();

	public boolean isSimulation();
	public boolean isConnected();

	public DataModel getCurrentModel();


	public ModelCollectorService getCollector();

	public List<Message> getMessageList();

	public Map<Class<?>,MAVLinkMessage> getMavLinkMessageMap();

	public boolean sendMAVLinkCmd(int command, float...params);
	public boolean sendMSPLinkCmd(int command, float...params);

	public void addModeChangeListener(IMSPModeChangedListener listener);

	public void addMAVLinkMsgListener(IMAVLinkMsgListener listener);


}
