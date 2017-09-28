package com.comino.msp.execution.offboard;

import com.comino.msp.model.DataModel;

import georegression.struct.point.Vector3D_F32;

public class APSetPoint {

	public static final int TYPE_POSITION = 1;
	public static final int TYPE_SPEED    = 2;

	public Vector3D_F32 speed;
	public Vector3D_F32 position;
	public float        yaw;
	public int          type;
	public long         tms;

	public APSetPoint() {
		this.speed 		= new Vector3D_F32(Float.NaN, Float.NaN, Float.NaN);
		this.position 	= new Vector3D_F32();
		this.yaw         = 0;
		this.tms         = 0;
		this.type        = TYPE_POSITION;
	}

	public APSetPoint(DataModel current) {
		this();
		this.set(current);
	}

	public APSetPoint(Vector3D_F32 position) {
		this();
		this.position.set(position);
		this.yaw         = 0;
	}

	public APSetPoint(Vector3D_F32 position, Vector3D_F32 speed, long tms) {
		this.speed 		= new Vector3D_F32(speed);
		this.position 	= new Vector3D_F32(position);
		this.type = TYPE_SPEED;
		this.tms  = tms;
	}


	public APSetPoint(Vector3D_F32 speed, long tms) {
		this.speed 		= new Vector3D_F32(speed);
		this.position 	= new Vector3D_F32();
		this.type = TYPE_SPEED;
		this.tms  = tms;
	}

	public APSetPoint(long tms) {
		this.speed 		= new Vector3D_F32();
		this.position 	= new Vector3D_F32();
		this.type = TYPE_SPEED;
		this.tms  = tms;
	}

	public APSetPoint(float x, float y, float z, float yaw) {
		this();
		this.position.set(x, y, z);
		this.yaw  = yaw;
	}

	public void set(DataModel current) {
		this.speed.set(current.state.l_vx, current.state.l_vy, current.state.l_vz);
		this.position.set(current.state.l_x, current.state.l_y, current.state.l_z);
		this.yaw  = current.state.h;
	}

	public void set(APSetPoint setpoint) {
		this.speed.set(setpoint.speed);
		this.position.set(setpoint.position);
		this.yaw  = setpoint.yaw;
		this.type = setpoint.type;
		this.tms  = setpoint.tms;
	}

	public boolean reached(Vector3D_F32 current, float distance) {
        return this.position.distance(current) < distance;

	}

	public float getSpeed() {
		return (float)(Math.sqrt(speed.x*speed.x+speed.y*speed.y+speed.z*speed.z));
	}

	public APSetPoint copy() {
		APSetPoint c = new APSetPoint();
		c.set(this);
		return c;
	}

	public String toString() {
		return "["+type+"] "+position.toString() + " / " + speed.toString() + " at "+tms+"ms";
	}

}
