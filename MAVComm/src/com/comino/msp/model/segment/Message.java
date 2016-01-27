package com.comino.msp.model.segment;

import com.comino.msp.model.segment.generic.Segment;

public class Message extends Segment {
	
	private static final long serialVersionUID = 7810155510611726123L;
	
	public String     msg = null;
	public int   severity = 0;
	
	
	public Message() {
		
	}
	
	public Message(String msg, int severity) {
		this.msg = msg;
		this.severity = severity;
		this.tms = System.currentTimeMillis()*1000;
	}

}
