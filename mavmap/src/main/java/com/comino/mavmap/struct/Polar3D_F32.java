package com.comino.mavmap.struct;



import com.comino.mavutils.MSPMathUtils;

import georegression.struct.point.Vector3D_F32;
import georegression.struct.point.Vector4D_F32;

public class Polar3D_F32 {

	public float angle_xy = 0;
	public float angle_xz = 0;
	public float value    = 0;

	public boolean isValid = false;

	private Vector3D_F32 v = new Vector3D_F32();

	public Polar3D_F32() {
		super(); clear();
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
		value    = Float.POSITIVE_INFINITY;
		isValid  = false;
	}


    public void set(Polar3D_F32 t) {
    	this.angle_xy = t.angle_xy;
    	this.angle_xz = t.angle_xz;
    	this.value = t.value;
    	this.isValid = t.isValid;
    }

	public void set(float vx, float vy, float vz) {

		if(Float.isNaN(vx)) vx = 0;
		if(Float.isNaN(vy)) vy = 0;
		if(Float.isNaN(vz)) vz = 0;

		this.value    = (float)Math.sqrt((vx)*(vx) + (vy)*(vy) + (vz)*(vz));
		this.angle_xy = getDirection(vy, vx);

		if(this.value !=0)
		  this.angle_xz = (float)Math.asin((vz)/this.value);
		else
		  this.angle_xz = 0;

		this.angle_xy = MSPMathUtils.normAngle(this.angle_xy);
		this.angle_xz = MSPMathUtils.normAngle(this.angle_xz);
	}

	public boolean isValid() {
		return Float.isFinite(value) && isValid;
	}


	public void set(Vector3D_F32 t) {
		set(t.x,t.y,t.z);
	}

	public void set() {
		set(v);
	}

	public void setValidity(boolean valid) {
		isValid = valid;
	}

	public void set(Vector4D_F32 t, Vector4D_F32 c) {

		if(Float.isNaN(t.x)) t.x = c.x;
		if(Float.isNaN(t.y)) t.y = c.y;
		if(Float.isNaN(t.z)) t.z = c.z;

		this.value    = (float)Math.sqrt((t.x-c.x)*(t.x-c.x) + (t.y-c.y)*(t.y-c.y) + (t.z-c.z)*(t.z-c.z));
		this.angle_xy = getDirection(t.y-c.y, t.x-c.x);
		this.angle_xz = (float)Math.asin((t.z-c.z)/this.value);

		this.angle_xy = MSPMathUtils.normAngle(this.angle_xy);
		this.angle_xz = MSPMathUtils.normAngle(this.angle_xz);

	}

	public void set(Vector3D_F32 t, Vector3D_F32 c) {

		if(Float.isNaN(t.x)) t.x = c.x;
		if(Float.isNaN(t.y)) t.y = c.y;
		if(Float.isNaN(t.z)) t.z = c.z;

		this.value    = (float)Math.sqrt((t.x-c.x)*(t.x-c.x) + (t.y-c.y)*(t.y-c.y) + (t.z-c.z)*(t.z-c.z));
		this.angle_xy = getDirection(t.y-c.y, t.x-c.x);
		this.angle_xz = (float)Math.asin((t.z-c.z)/this.value);

		this.angle_xy = MSPMathUtils.normAngle(this.angle_xy);
		this.angle_xz = MSPMathUtils.normAngle(this.angle_xz);
	}

	public void constraint(float max_x, float max_y, float max_z) {
		get();
		v.x = Math.max(v.x, max_x);
		v.y = Math.max(v.y, max_x);
		v.z = Math.max(v.z, max_x);
		set();
	}

	public void get(Vector3D_F32 t) {
		get(); t.set(v);
	}

	public Vector3D_F32 get() {
		v.set((float)( Math.cos(angle_xy) * Math.cos(angle_xz)), (float)(Math.sin(angle_xy)* Math.cos(angle_xz)),(float)Math.sin(angle_xz));
		v.scale(value);
		return v;
	}

	public void get(Vector4D_F32 t) {
		get();
		t.setX(v.x);
		t.setY(v.y);
		t.setZ(v.z);
	}

	public void get(Vector4D_F32 t, float yaw) {
		get(t); t.w = yaw;
	}

	public float getX() {
		return (float)(Math.cos(angle_xy) * Math.cos(angle_xz)) * value;
	}

	public float getY() {
		return (float)(Math.sin(angle_xy) * Math.cos(angle_xz)) * value;
	}

	public float getZ() {
		return  (float)Math.sin(angle_xz) * value;
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

		if(dx == 0 && dy == 0)
			return 0;
		if(dx == 0 && dy > 0)
			return  (float)Math.PI/2;
		if(dx == 0 && dy < 0)
			return -(float)Math.PI/2;

		if((dx > 0 && dy > 0) || (dx > 0 && dy < 0))
			return (float)Math.atan(dy/dx);
		if((dx < 0 && dy > 0) || (dx < 0 && dy < 0))
			return (float)(Math.atan(dy/dx)+Math.PI);

		return 0;
	}


}
