package com.comino.msp.main.control.listener;

import java.util.List;

import com.comino.msp.model.segment.LogMessage;

public interface IMAVMessageListener {

	public void messageReceived(List<LogMessage> messageList, LogMessage currentMessage);

}
