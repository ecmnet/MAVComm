package com.comino.msp.execution.autopilot.offboard;

public interface IOffboardSpeedControl {

	public static final int ANGLE = 0;
	public static final int SPEED = 1;

	public float[] determine(float current_speed, float target_dir);

}
