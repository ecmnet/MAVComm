package com.comino.msp.model.segment;

import com.comino.msp.model.segment.generic.Segment;

public class Battery extends Segment {
	
	private static final long serialVersionUID = -4051617731611229443L;
	
	public float  b0              = 0;
	public float  c0			  = 0;
	public short  p               = 0;
	
	
	public void set(Battery m) {
		 b0 = m.b0;
		 c0 = m.c0;
		 p  = m.p;
	}
	
	public Battery clone() {
		Battery s = new Battery();
		s.b0	= b0;
		s.c0    = c0;
		s.p     = p;
		return s;
	}
	
	public void clear() {
		b0    = 0;
		c0    = 0;
		p     = 0;
	}
	

}