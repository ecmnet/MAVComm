package com.comino.mav.comm;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.mavlink.messages.MAVLinkMessage;

import com.comino.mav.mavlink.IMAVLinkMsgListener;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.Message;

public interface IMAVComm {

	public boolean open();

	public DataModel getModel();

	public List<Message> getMessageList();
	
	public Map<Class<?>,MAVLinkMessage> getMavLinkMessageMap();

	void close();

	void write(MAVLinkMessage msg) throws IOException;
	
	public void registerProxyListener(IMAVLinkMsgListener listener);
	
	public boolean isConnected();

}