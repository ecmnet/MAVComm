package com.comino.msp.model.segment;

import com.comino.msp.model.segment.generic.Segment;

public class Rc extends Segment {

	private static final long serialVersionUID = -1177166201561030663L;
	
	public short 	s0 = 0;
	public short 	s1 = 0;
	public short 	s2 = 0;
	public short 	s3 = 0;


	public void set(Rc a) {
		s0		= a.s0;
		s1		= a.s1;
		s2		= a.s2;
		s3		= a.s3;
	
		
	}

	public Rc clone() {
		Rc at = new Rc();
		at.s0 		= s0;
		at.s1 		= s1;
		at.s2 		= s2;
		at.s3 		= s3;
		
		
		return at;
	}

	//--------------------------------------------------------------------------------------------------------


	public void clear() {
		s0 = 0;
		s1 = 0;
		s2 = 0;
		s3 = 0;
		
	}

}
