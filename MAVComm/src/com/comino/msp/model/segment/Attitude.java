package com.comino.msp.model.segment;

import com.comino.msp.model.segment.generic.Segment;

public class Attitude extends Segment {

	private static final long serialVersionUID = -1123989934184248219L;
	
	public float aX     = 0;	// angleX
	public float aY     = 0;	// angleY

	public float h      = 0;	// heading (compass)
	public float al     = 0;	// altitude above ground
	public float ag     = 0;	// est.altitude above sealevel
	
	public float s    	= 0;	// ground speed


	public void set(Attitude a) {
		aX    	= a.aX;
		aY    	= a.aY;
		h   	= a.h;
		al      = a.al;
		ag      = a.ag;
		s   	= a.s;
		
	}

	public Attitude clone() {
		Attitude at = new Attitude();
		at.aX 		= aX;
		at.aY 		= aY;
		at.h		= h;
		at.al       = al;
		at.ag       = ag;
		at.s		= s;
		return at;
	}

	//--------------------------------------------------------------------------------------------------------


	public void clear() {
		aX    	= 0;
		aY    	= 0;
		h   	= 0;
		al 		= 0;
		ag      = 0;
		s   	= 0;
	}

}
