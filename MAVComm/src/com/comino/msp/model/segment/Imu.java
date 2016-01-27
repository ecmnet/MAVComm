package com.comino.msp.model.segment;

import com.comino.msp.model.segment.generic.Segment;

public class Imu extends Segment {

	private static final long serialVersionUID = -7271311099121759585L;
	
	public float accx = 0;
	public float accy = 0;
	public float accz = 0;
	
	public float gyrox = 0;
	public float gyroy = 0;
	public float gyroz = 0;
	
	public float abs_pressure = 0;
	
	
	public void set(Imu a) {
		accx = a.accx;
		accy = a.accy;
		accz = a.accz;
		
		gyrox = a.gyrox;
		gyroy = a.gyroy;
		gyroz = a.gyroz;
		
		abs_pressure = a.abs_pressure;
		
	}
	
	public Imu clone() {
		Imu a = new Imu();
		a.accx = accx;
		a.accy = accy;
		a.accz = accz;
		
		a.gyrox = gyrox;
		a.gyroy = gyroy;
		a.gyroz = gyroz;
		
		a.abs_pressure = abs_pressure;
	
		
		return a;
	}
	
	
	public void clear() {
		accx = 0;
		accy = 0;
		accz = 0;
		
		gyrox = 0;
		gyroy = 0;
		gyroz = 0;
		
	}
	
	

}
