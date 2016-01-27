package com.comino.msp.model.segment;

import com.comino.msp.model.segment.generic.Segment;

public class Attitude extends Segment {

	private static final long serialVersionUID = -1123989934184248219L;
	
	public float aX     = 0;
	public float aY     = 0;

	public float h      = 0;
	public float al     = 0;
	
	public float s    	= 0;


	public void set(Attitude a) {
		aX    	= a.aX;
		aY    	= a.aY;
		h   	= a.h;
		al      = a.al;
		s   	= a.s;
		
	}

	public Attitude clone() {
		Attitude at = new Attitude();
		at.aX 		= aX;
		at.aY 		= aY;
		at.h		= h;
		at.al       = al;
		at.s		= s;
		return at;
	}

	//--------------------------------------------------------------------------------------------------------


	public void clear() {
		aX    	= 0;
		aY    	= 0;
		h   	= 0;
		al 		= 0;
		s   	= 0;
	}

}
