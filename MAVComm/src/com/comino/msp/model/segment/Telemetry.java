package com.comino.msp.model.segment;

import com.comino.msp.model.segment.generic.Segment;

public class Telemetry extends Segment {
	
	private static final long serialVersionUID = -5513070249760162722L;
	
	public float  rssi            = 0;
	
	
	public void set(Telemetry m) {
		rssi = m.rssi;
	}
	
	public Telemetry clone() {
		Telemetry s = new Telemetry();
		s.rssi	= rssi;
		return s;
	}
	
	public void clear() {
		rssi    = 0;

	}
	

}