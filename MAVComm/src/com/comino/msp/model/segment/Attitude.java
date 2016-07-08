package com.comino.msp.model.segment;

import com.comino.msp.model.segment.generic.Segment;

public class Attitude extends Segment {

	private static final long serialVersionUID = -5331001446960382012L;

	public float r        	 = 0;	// roll in degrees
	public float p           = 0;	// pitch in degrees
	public float y           = 0;	// yaw in degrees
	public float t           = 0;

	public float rr        	 = 0;	// rollrate in degrees/s
	public float pr          = 0;	// pitchrate in degrees/s
	public float yr          = 0;	// yawrate in degrees/s

	public float sr        	 = 0;	// setpoint roll in degrees
	public float sp          = 0;	// setpoint pitch in degrees
	public float sy          = 0;	// setpoint yaw in degrees
	public float st          = 0;

	public float srr          = 0;	// setpoint rollrate in degrees/s
	public float spr          = 0;	// setpoint pitchrate in degrees/s
	public float syr          = 0;	// setpoint yawrate in degrees/s


	public void set(Attitude a) {

		r 	= a.r;
		p   = a.p;
		y   = a.y;
		t   = a.t;
		rr 	= a.rr;
		pr  = a.pr;
		yr  = a.yr;
		sr 	= a.sr;
		sp  = a.sp;
		sy  = a.sy;
		st  = a.st;
		srr = a.srr;
		spr = a.spr;
		syr = a.syr;
	}

	public Attitude clone() {
		Attitude a = new Attitude();
		a.set(this);
		return a;
	}

	public void clear() {
		r 	= 0;
		p   = 0;
		y   = 0;
		t   = 0;
		rr 	= 0;
		pr  = 0;
		yr  = 0;
		sr 	= 0;
		sp  = 0;
		sy  = 0;
		st  = 0;
		srr = 0;
		spr = 0;
		syr = 0;
	}

}
