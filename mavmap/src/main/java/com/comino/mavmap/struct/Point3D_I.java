package com.comino.mavmap.struct;

import georegression.struct.point.Point3D_I32;

public class Point3D_I extends Point3D_I32 {

	private static final long serialVersionUID = 5938524026484493659L;

	public Point3D_I(Point3D_I32 i) {
		super(i);
	}

	public Point3D_I() {
		super();
	}
	
	public Point3D_I(int x, int y, int z) {
		super(x,y,z);
	}

	public Point3D_I add(Point3D_I32 a) {
		this.x += a.x;
		this.y += a.y;
		this.z += a.z;
		return this;
	}

	public Point3D_I add(int c) {
		this.x += c;
		this.y += c;
		this.z += c;
		return this;
	}
	
	public Point3D_I diff(Point3D_I32 a) {
		this.x -= a.x;
		this.y -= a.y;
		this.z -= a.z;
		return this;
	}
	
	public void add(int index, int c) {
		switch(index) {
		case 0: this.x += c;
		case 1: this.y += c;
		case 2: this.z += c;
		}
	}

	public int get(int index) {
		switch(index) {
		case 0: return this.x;
		case 1: return this.y;
		case 2: return this.z;
		}
		return 0;
	}

	public Point3D_I set(int index, int v) {
		switch(index) {
		case 0: this.x = v; break;
		case 1: this.y = v; break;
		case 2: this.z = v; break;
		}
		return this;
	}

	public Point3D_I copy() {
		return new Point3D_I(this);
	}
	
	public Point3D_I min(Point3D_I32 a) {
		this.x = Math.min(x, a.x);
		this.y = Math.min(y, a.y);;
		this.z = Math.min(z, a.z);;
		return this;
	}
	
	public Point3D_I max(Point3D_I32 a) {
		this.x = Math.max(x, a.x);
		this.y = Math.max(y, a.y);;
		this.z = Math.max(z, a.z);;
		return this;
	}

}
