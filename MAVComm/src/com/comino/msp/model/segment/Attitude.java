package com.comino.msp.model.segment;

import com.comino.msp.model.segment.generic.Segment;

public class Attitude extends Segment {

	private static final long serialVersionUID = -5331001446960382012L;

	public float r        	 = Float.NaN;	// roll in rad
	public float p           = Float.NaN;	// pitch in rad
	public float y           = Float.NaN;	// yaw in rad
	public float t           = Float.NaN;

	public float q1        	 = Float.NaN;	// Quaternion Q1
	public float q2        	 = Float.NaN;	// Quaternion Q2
	public float q3        	 = Float.NaN;	// Quaternion Q3
	public float q4        	 = Float.NaN;	// Quaternion Q4

	public float rr        	 = Float.NaN;	// rollrate in rad/s
	public float pr          = Float.NaN;	// pitchrate in rad/s
	public float yr          = Float.NaN;	// yawrate in rad/s

	public float sr        	 = Float.NaN;	// setpoint roll in rad
	public float sp          = Float.NaN;	// setpoint pitch in rad
	public float sy          = Float.NaN;	// setpoint yaw in rad
	public float st          = Float.NaN;

	public float srr          = Float.NaN;	// setpoint rollrate in rad/s
	public float spr          = Float.NaN;	// setpoint pitchrate in rad/s
	public float syr          = Float.NaN;	// setpoint yawrate in rad/s


	public void set(Attitude a) {

		r 	= a.r;
		p   = a.p;
		y   = a.y;
		t   = a.t;
		q1  = a.q1;
		q2  = a.q2;
		q3  = a.q3;
		q4  = a.q4;
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
		q1  = 0;
		q2  = 0;
		q3  = 0;
		q4  = 0;
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
