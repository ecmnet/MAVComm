package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_highres_imu;

public class PX4HighResIMUPlugin extends MAVLinkPluginBase {

	public PX4HighResIMUPlugin() {
		super(msg_highres_imu.class);
	}

	@Override
	public void received(Object o) {

		msg_highres_imu imu = (msg_highres_imu) o;
		model.imu.accx = imu.xacc;
		model.imu.accy = imu.yacc;
		model.imu.accz = imu.zacc;

		model.imu.gyrox = imu.xgyro;
		model.imu.gyroy = imu.ygyro;
		model.imu.gyroz = imu.zgyro;

		model.imu.magx = imu.xmag;
		model.imu.magy = imu.ymag;
		model.imu.magz = imu.zmag;
		model.hud.ap = imu.pressure_alt;

		model.imu.abs_pressure = imu.abs_pressure;

		model.sys.imu_temp = (byte) imu.temperature;
		model.imu.tms = model.sys.getSynchronizedPX4Time_us();

	}
}
