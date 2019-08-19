package com.comino.msp.utils.struct;


import com.comino.msp.utils.MSPMathUtils;

import georegression.struct.point.Vector3D_F32;
import georegression.struct.point.Vector4D_F32;

public class Polar3D_F32 {

	public float angle_xy = 0;
	public float angle_xz = 0;
	public float value    = 0;

	private static final float PI2 = 2f*(float)Math.PI;

	public Polar3D_F32() {
		super();
	}

	public Polar3D_F32(float angle_xy, float angle_xz, float length) {
		super();
		this.angle_xy = angle_xy;
		this.angle_xz = angle_xz;
		this.value = length;
	}


	public void clear() {

		angle_xy = 0;
		angle_xz = 0;
		value    = 0;
	}


	public void set(float angle_xy, float angle_xz, float length) {
		this.angle_xy = MSPMathUtils.normAngle(angle_xy);
		this.angle_xz = MSPMathUtils.normAngle(angle_xz);
		this.value    = length;
	}


	public void set(Vector4D_F32 t, Vector4D_F32 c) {
		this.value    = (float)Math.sqrt((t.x-c.x)*(t.x-c.x) + (t.y-c.y)*(t.y-c.y) + (t.z-c.z)*(t.z-c.z));
		this.angle_xy = getDirection(t.y-c.y, t.x-c.x);
		this.angle_xz = (float)Math.asin((t.z-c.z)/this.value);

		this.angle_xy = MSPMathUtils.normAngle(this.angle_xy);
		this.angle_xz = MSPMathUtils.normAngle(this.angle_xz);

	}

	public void set(Vector3D_F32 t, Vector3D_F32 c) {
		this.value    = (float)Math.sqrt((t.x-c.x)*(t.x-c.x) + (t.y-c.y)*(t.y-c.y) + (t.z-c.z)*(t.z-c.z));
		this.angle_xy = getDirection(t.y-c.y, t.x-c.x);
		this.angle_xz = (float)Math.asin((t.z-c.z)/this.value);

		this.angle_xy = MSPMathUtils.normAngle(this.angle_xy);
		this.angle_xz = MSPMathUtils.normAngle(this.angle_xz);
	}

	public void get(Vector3D_F32 t) {
		t.set((float)( Math.cos(angle_xy) * Math.cos(angle_xz)), (float)(Math.sin(angle_xy)* Math.cos(angle_xz)),(float)Math.sin(angle_xz));
		t.scale(value);
	}

	public String toString() {
		return "XY="+MSPMathUtils.fromRad(angle_xy)+" XZ="+MSPMathUtils.fromRad(angle_xz)+" L="+value;
	}

	public static void main(String[] args) {

		Vector3D_F32 current = new Vector3D_F32();
		Vector3D_F32 target  = new Vector3D_F32(1,1,1);
		Vector3D_F32 result  = new Vector3D_F32();

		Polar3D_F32 p = new Polar3D_F32();
		p.set(target, current);

		p.get(result);

		System.out.println(target);
		System.out.println(p);
		System.out.println(result);
	}

	private float getDirection(float dy, float dx) {

		if((dx > 0 && dy > 0) || (dx > 0 && dy < 0))
			return (float)Math.atan(dy/dx);
		if((dx < 0 && dy > 0) || (dx < 0 && dy < 0))
			return (float)(Math.atan(dy/dx)+Math.PI);

		return 0;
	}


}