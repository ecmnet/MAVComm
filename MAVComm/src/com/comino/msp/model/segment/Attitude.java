package com.comino.msp.model.segment;

import com.comino.msp.model.segment.generic.Segment;

public class Attitude extends Segment {

	private static final long serialVersionUID = -5331001446960382012L;

	public float r        	 = Float.NaN;	// roll in degrees
	public float p           = Float.NaN;	// pitch in degrees
	public float y           = Float.NaN;	// yaw in degrees
	public float t           = Float.NaN;

	public float rr        	 = Float.NaN;	// rollrate in degrees/s
	public float pr          = Float.NaN;	// pitchrate in degrees/s
	public float yr          = Float.NaN;	// yawrate in degrees/s

	public float sr        	 = Float.NaN;	// setpoint roll in degrees
	public float sp          = Float.NaN;	// setpoint pitch in degrees
	public float sy          = Float.NaN;	// setpoint yaw in degrees
	public float st          = Float.NaN;

	public float srr          = Float.NaN;	// setpoint rollrate in degrees/s
	public float spr          = Float.NaN;	// setpoint pitchrate in degrees/s
	public float syr          = Float.NaN;	// setpoint yawrate in degrees/s


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
