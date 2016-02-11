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

package com.comino.mav.comm;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.mavlink.messages.MAVLinkMessage;

import com.comino.mav.mavlink.IMAVLinkMsgListener;
import com.comino.msp.main.control.listener.IMSPModeChangedListener;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.Message;

public interface IMAVComm {

	public boolean open();

	public DataModel getModel();

	public List<Message> getMessageList();

	public Map<Class<?>,MAVLinkMessage> getMavLinkMessageMap();

	void close();

	void write(MAVLinkMessage msg) throws IOException;

	public void addMAVLinkMsgListener(IMAVLinkMsgListener listener);

	public void addModeChangeListener(IMSPModeChangedListener listener);

	public boolean isConnected();

}