package com.comino.msp.main.control.listener;

import java.util.List;

import com.comino.msp.model.segment.Message;

public interface IMAVMessageListener {

	public void messageReceived(List<Message> messageList, Message currentMessage);

}
