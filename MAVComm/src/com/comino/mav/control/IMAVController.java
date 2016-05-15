/****************************************************************************
 *
 *   Copyright (c) 2016 Eike Mansfeld ecm@gmx.de. All rights reserved.
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


package com.comino.mav.control;

import java.util.List;
import java.util.Map;

import org.mavlink.messages.MAVLinkMessage;

import com.comino.msp.main.control.listener.IMAVLinkListener;
import com.comino.msp.main.control.listener.IMAVMessageListener;
import com.comino.msp.main.control.listener.IMSPModeChangedListener;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.collector.ModelCollectorService;
import com.comino.msp.model.segment.LogMessage;

public interface IMAVController {

	public boolean connect();
	public boolean close();

	public boolean isSimulation();
	public boolean isConnected();

	public DataModel getCurrentModel();

	public String getConnectedAddress();


	public ModelCollectorService getCollector();

	public List<LogMessage> getMessageList();
	public void writeLogMessage(LogMessage m);

	public Map<Class<?>,MAVLinkMessage> getMavLinkMessageMap();

	public boolean sendMAVLinkCmd(int command, float...params);
	public boolean sendMAVLinkMessage(MAVLinkMessage msg);
	public boolean sendMSPLinkCmd(int command, float...params);

	public void addModeChangeListener(IMSPModeChangedListener listener);
	public void addMAVLinkListener(IMAVLinkListener listener);
	public void addMAVMessageListener(IMAVMessageListener listener);


}
