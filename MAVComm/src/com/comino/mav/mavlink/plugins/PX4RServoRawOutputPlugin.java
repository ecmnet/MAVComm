package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_servo_output_raw;

public class PX4RServoRawOutputPlugin extends MAVLinkPluginBase {

	public PX4RServoRawOutputPlugin() {
		super(msg_servo_output_raw.class);
	}

	@Override
	public void received(Object o) {

		msg_servo_output_raw servo = (msg_servo_output_raw) o;
		model.servo.servo1 = servo.servo1_raw;
		model.servo.servo2 = servo.servo2_raw;
		model.servo.servo3 = servo.servo3_raw;
		model.servo.servo4 = servo.servo4_raw;
		model.servo.servo5 = servo.servo5_raw;
		model.servo.servo6 = servo.servo6_raw;
		model.servo.servo7 = servo.servo7_raw;
		model.servo.servo8 = servo.servo8_raw;

		model.servo.tms = servo.time_usec;

	}
}
