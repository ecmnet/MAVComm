package com.comino.msp.execution.offboard;

import com.comino.msp.model.DataModel;

import georegression.struct.point.Vector3D_F32;

public class APSetPoint {

	public Vector3D_F32 speed;
	public Vector3D_F32 position;
	public float        yaw;
	public int          mode;

	public APSetPoint() {
		this.speed 		= new Vector3D_F32(Float.NaN, Float.NaN, Float.NaN);
		this.position 	= new Vector3D_F32();
		this.yaw         = 0;
	}

	public APSetPoint(DataModel current) {
		this();
		this.set(current);
	}

	public APSetPoint(Vector3D_F32 position) {
		this();
		this.speed 		= new Vector3D_F32();
		this.position 	= position.copy();
		this.yaw         = 0;
	}

	public APSetPoint(Vector3D_F32 position, int mode) {
		this(position);
		this.mode 		= mode;
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
		return position.toString() + " / " + speed.toString();
	}

}
