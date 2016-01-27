package com.comino.msp.model.segment;

import com.comino.msp.model.segment.generic.Segment;

public class Raw extends Segment {

	private static final long serialVersionUID = -1123989934184248219L;
	
	public float di     = 0;		// LIDAR distance
	public float fX     = 0;		// Flow integrated X
	public float fY     = 0;		// Flow integrated Y



	public void set(Raw a) {
		fX    	= a.fX;
		fY    	= a.fY;
		di   	= a.di;
		
	}

	public Raw clone() {
		Raw at = new Raw();
		at.fX 		= fX;
		at.fY 		= fY;
		at.di		= di;
		
		return at;
	}

	//--------------------------------------------------------------------------------------------------------


	public void clear() {
		fX    	= 0;
		fY    	= 0;
		di   	= 0;
	}

}
