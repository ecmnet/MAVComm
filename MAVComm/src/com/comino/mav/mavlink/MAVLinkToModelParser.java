/****************************************************************************
 *
 *   Copyright (c) 2016 Eike Mansfeld ecm@gmx.de. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 ****************************************************************************/


package com.comino.mav.mavlink;

import java.io.IOException;
import java.nio.channels.ByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.LockSupport;

import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.messages.MAV_MODE_FLAG;
import org.mavlink.messages.MAV_MODE_FLAG_DECODE_POSITION;
import org.mavlink.messages.MAV_STATE;
import org.mavlink.messages.MAV_SYS_STATUS_SENSOR;
import org.mavlink.messages.lquac.msg_altitude;
import org.mavlink.messages.lquac.msg_attitude;
import org.mavlink.messages.lquac.msg_attitude_quaternion;
import org.mavlink.messages.lquac.msg_attitude_target;
import org.mavlink.messages.lquac.msg_autopilot_version;
import org.mavlink.messages.lquac.msg_battery_status;
import org.mavlink.messages.lquac.msg_command_ack;
import org.mavlink.messages.lquac.msg_distance_sensor;
import org.mavlink.messages.lquac.msg_estimator_status;
import org.mavlink.messages.lquac.msg_extended_sys_state;
import org.mavlink.messages.lquac.msg_global_position_int;
import org.mavlink.messages.lquac.msg_gps_raw_int;
import org.mavlink.messages.lquac.msg_heartbeat;
import org.mavlink.messages.lquac.msg_highres_imu;
import org.mavlink.messages.lquac.msg_home_position;
import org.mavlink.messages.lquac.msg_local_position_ned;
import org.mavlink.messages.lquac.msg_local_position_ned_cov;
import org.mavlink.messages.lquac.msg_manual_control;
import org.mavlink.messages.lquac.msg_msp_status;
import org.mavlink.messages.lquac.msg_msp_vision;
import org.mavlink.messages.lquac.msg_optical_flow_rad;
import org.mavlink.messages.lquac.msg_position_target_local_ned;
import org.mavlink.messages.lquac.msg_rc_channels;
import org.mavlink.messages.lquac.msg_servo_output_raw;
import org.mavlink.messages.lquac.msg_statustext;
import org.mavlink.messages.lquac.msg_sys_status;
import org.mavlink.messages.lquac.msg_system_time;
import org.mavlink.messages.lquac.msg_timesync;
import org.mavlink.messages.lquac.msg_vfr_hud;
import org.mavlink.messages.lquac.msg_vibration;
import org.mavlink.messages.lquac.msg_vision_position_estimate;

import com.comino.mav.comm.IMAVComm;
import com.comino.msp.main.control.listener.IMAVLinkListener;
import com.comino.msp.main.control.listener.IMAVMessageListener;
import com.comino.msp.main.control.listener.IMSPStatusChangedListener;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.GPS;
import com.comino.msp.model.segment.LogMessage;
import com.comino.msp.model.segment.Status;
import com.comino.msp.utils.MSPMathUtils;


public class MAVLinkToModelParser {

	private static int TIME_SYNC_CYCLE_MS 				= 100;


	private MAVLinkStream stream;
	private DataModel model;

	private HashMap<Class<?>,MAVLinkMessage>	    mavList     = null;

	private IMAVComm link = null;

	private HashMap<Class<?>,List<IMAVLinkListener>> listeners = null;

	private List<IMAVLinkListener> mavListener 			 = null;
	private List<IMAVMessageListener> msgListener        = null;

	private List<IMSPStatusChangedListener> modeListener = null;

	private boolean isRunning = false;
	private long    startUpAt = 0;
	private long    t_armed_start = 0;

	private long    gpos_tms = 0;
	private long    mocap_tms = 0;

	private long    time_offset_ns   = 0;
	private long    time_offset_tms  = 0;

	private LogMessage lastMessage = null;

	private Status oldStatus = new Status();

	public MAVLinkToModelParser(DataModel model, IMAVComm link) {

		this.model = model;
		this.link = link;
		this.mavList   = new HashMap<Class<?>,MAVLinkMessage>();

		this.modeListener = new ArrayList<IMSPStatusChangedListener>();
		this.mavListener = new ArrayList<IMAVLinkListener>();
		this.msgListener = new ArrayList<IMAVMessageListener>();

		listeners = new HashMap<Class<?>,List<IMAVLinkListener>>();

		registerListener(msg_msp_vision.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_msp_vision mocap = (msg_msp_vision)o;
				model.vision.vx= mocap.vx;
				model.vision.vy= mocap.vy;
				model.vision.vz= mocap.vz;

				model.vision.x = mocap.x;
				model.vision.y = mocap.y;
				model.vision.z = mocap.z;

				model.vision.dh= mocap.h;
				model.vision.qual= mocap.quality;
				model.vision.errors = (int)mocap.errors;

				model.vision.flags= (int)mocap.flags;
				model.vision.fps= mocap.fps;
				model.vision.tms= System.nanoTime()/1000;
				if(model.vision.errors < 5) {
					mocap_tms = System.currentTimeMillis();
					model.sys.setSensor(Status.MSP_OPCV_AVAILABILITY, true );
				}
			}
		});

		registerListener(msg_msp_status.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_msp_status status = (msg_msp_status)o;
				model.sys.load_m = status.load / 4f;
				model.sys.setSensor(Status.MSP_MSP_AVAILABILITY, true);
			}
		});

		registerListener(msg_vfr_hud.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_vfr_hud hud   = (msg_vfr_hud)o;
				model.hud.s  = hud.groundspeed;
				model.hud.vs = hud.climb;
				model.hud.as = hud.airspeed;
			}
		});

		registerListener(msg_distance_sensor.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_distance_sensor lidar = (msg_distance_sensor)o;
				model.raw.di = lidar.current_distance / 100f;
				model.raw.dicov = lidar.covariance / 100f;
				model.sys.setSensor(Status.MSP_LIDAR_AVAILABILITY, true);


			}
		});

		registerListener(msg_servo_output_raw.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_servo_output_raw servo = (msg_servo_output_raw)o;
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
		});

		registerListener(msg_vibration.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_vibration vib = (msg_vibration)o;
				model.vibration.vibx = vib.vibration_x;
				model.vibration.viby = vib.vibration_y;
				model.vibration.vibz = vib.vibration_z;
				model.vibration.cli0 = vib.clipping_0;
				model.vibration.cli1 = vib.clipping_1;
				model.vibration.cli2 = vib.clipping_2;
				model.vibration.tms  = vib.time_usec;
			}
		});


		registerListener(msg_optical_flow_rad.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_optical_flow_rad flow = (msg_optical_flow_rad)o;
				model.raw.fX   = flow.integrated_x;
				model.raw.fY   = flow.integrated_y;
				model.raw.fq   = flow.quality;
				model.raw.fd   = flow.distance;

				model.raw.tms  = flow.time_usec;
			}
		});

		registerListener(msg_altitude.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_altitude alt = (msg_altitude)o;
				model.hud.al   = alt.altitude_local;
				model.hud.ag   = alt.altitude_amsl;
				model.hud.at   = alt.altitude_terrain;
				model.hud.ar   = alt.altitude_relative;
				model.hud.bc   = alt.bottom_clearance;

			}
		});

		registerListener(msg_command_ack.class, new IMAVLinkListener() {

			@Override
			public void received(Object o) {
				msg_command_ack ack = (msg_command_ack)o;
				switch(ack.result) {
				case 1:
					System.err.println("Command "+ack.command+" failed"); break;
				case 2:
					System.err.println("Command "+ack.command+" is denied"); break;
				case 3:
					System.err.println("Command "+ack.command+" is unsupported"); break;
				default:
				}
			}

		});

		registerListener(msg_attitude.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_attitude att = (msg_attitude)o;

				model.attitude.r = att.roll;
				model.attitude.p = att.pitch;
				model.attitude.y = att.yaw;
				model.hud.h      = MSPMathUtils.fromRad(model.attitude.y);
				model.state.h    = model.hud.h;

				model.attitude.rr = att.rollspeed;
				model.attitude.pr = att.pitchspeed;
				model.attitude.yr = att.yawspeed;

				model.attitude.tms  = att.time_boot_ms*1000;

				model.hud.aX   = att.roll;
				model.hud.aY   = att.pitch;
				model.hud.tms  = att.time_boot_ms*1000;
				model.sys.setSensor(Status.MSP_IMU_AVAILABILITY, true);

				//System.out.println(att.toString());
			}
		});

		registerListener(msg_attitude_quaternion.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_attitude_quaternion att = (msg_attitude_quaternion)o;

				model.attitude.q1 = att.q1;
				model.attitude.q2 = att.q2;
				model.attitude.q3 = att.q3;
				model.attitude.q4 = att.q4;
			}
		});


		registerListener(msg_attitude_target.class, new IMAVLinkListener() {
			float[] sp = new float[3];
			@Override
			public void received(Object o) {
				msg_attitude_target att = (msg_attitude_target)o;

				MSPMathUtils.eulerAnglesByQuaternion(sp,att.q);

				model.attitude.sr = sp[0];
				model.attitude.sp = sp[1];
				model.attitude.sy = sp[2];
				model.attitude.st = att.thrust;

				model.attitude.srr = att.body_roll_rate;
				model.attitude.spr = att.body_pitch_rate;
				model.attitude.syr = att.body_yaw_rate;

				//System.out.println(att.toString());
			}
		});

		registerListener(msg_gps_raw_int.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_gps_raw_int gps = (msg_gps_raw_int)o;
				model.gps.numsat = (byte) gps.satellites_visible;
				model.gps.setFlag(GPS.GPS_SAT_FIX, gps.fix_type>0);
				model.gps.setFlag(GPS.GPS_SAT_VALID, true);

				model.gps.hdop   = gps.eph/100f;
				model.gps.latitude = gps.lat/1e7f;
				model.gps.longitude = gps.lon/1e7f;
				model.gps.altitude = (short)(gps.alt/1000);
				model.sys.setSensor(Status.MSP_GPS_AVAILABILITY, model.gps.numsat>6);

			}
		});

		registerListener(msg_system_time.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_system_time time = (msg_system_time)o;
				model.sys.t_boot_ms = time.time_boot_ms;

			}
		});


		registerListener(msg_home_position.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_home_position ref = (msg_home_position)o;

				model.home_state.l_x = ref.x;
				model.home_state.l_y = ref.y;
				model.home_state.l_z = ref.z;

				model.home_state.g_lat = ref.latitude/10000000f;
				model.home_state.g_lon = ref.longitude/10000000f;
				model.home_state.g_alt = (int)((ref.altitude+500)/1000f);

			}
		});

		registerListener(msg_manual_control.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_manual_control ctl = (msg_manual_control)o;
				model.sys.setStatus(Status.MSP_JOY_ATTACHED,true);
			}
		});

		registerListener(msg_local_position_ned_cov.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_local_position_ned_cov ned = (msg_local_position_ned_cov)o;

				model.state.l_ax = ned.ax;
				model.state.l_ay = ned.ay;
				model.state.l_az = ned.az;

				model.state.l_x = ned.x;
				model.state.l_y = ned.y;
				model.state.l_z = ned.z;

				model.state.l_vx = ned.vx;
				model.state.l_vy = ned.vy;
				model.state.l_vz = ned.vz;

				model.state.v = (float)Math.sqrt( ned.vx* ned.vx +  ned.vy* ned.vy);

				model.state.tms = ned.time_boot_ms*1000;

			}
		});

		registerListener(msg_local_position_ned.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_local_position_ned ned = (msg_local_position_ned)o;

				model.state.l_x = ned.x;
				model.state.l_y = ned.y;
				model.state.l_z = ned.z;

				model.state.l_vx = ned.vx;
				model.state.l_vy = ned.vy;
				model.state.l_vz = ned.vz;

				model.state.v = (float)Math.sqrt( ned.vx* ned.vx +  ned.vy* ned.vy);

				model.state.tms = ned.time_boot_ms*1000;

			}
		});

		registerListener(msg_estimator_status.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_estimator_status est = (msg_estimator_status)o;
				model.est.haglRatio         = est.hagl_ratio;
				model.est.magRatio          = est.mag_ratio;
				model.est.horizRatio        = est.pos_horiz_ratio;
				model.est.vertRatio         = est.pos_vert_ratio;
				model.est.posHorizAccuracy  = est.pos_horiz_accuracy;
				model.est.posVertAccuracy   = est.pos_vert_accuracy;
				model.est.flags             = est.flags;
				model.est.tasRatio          = est.tas_ratio;
				model.est.velRatio          = est.vel_ratio;

				model.est.tms = est.time_usec * 1000;
			}
		});


		registerListener(msg_position_target_local_ned.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_position_target_local_ned ned = (msg_position_target_local_ned)o;


				model.target_state.l_x = ned.x;
				model.target_state.l_y = ned.y;
				model.target_state.l_z = ned.z;

				model.target_state.h   = MSPMathUtils.fromRad(ned.yaw);

				model.target_state.l_vx = ned.vx;
				model.target_state.l_vy = ned.vy;
				model.target_state.l_vz = ned.vz;

				model.target_state.vh  = MSPMathUtils.fromRad(ned.yaw_rate);

				model.target_state.l_ax = ned.afx;
				model.target_state.l_ay = ned.afy;
				model.target_state.l_az = ned.afz;

				model.target_state.c_frame = ned.coordinate_frame;

				model.target_state.tms = ned.time_boot_ms*1000;

				model.sys.setStatus(Status.MSP_LPOS_AVAILABILITY, true);

			}
		});

		registerListener(msg_global_position_int.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_global_position_int pos = (msg_global_position_int)o;
				model.state.g_lat 	= pos.lat/10000000f;
				model.state.g_lon	= pos.lon/10000000f;
				model.state.g_alt   = (pos.alt/1000);
				model.gps.heading   = (short)(pos.hdg/1000f);
				model.gps.altitude  = (short)(pos.alt/1000);
				model.gps.tms = pos.time_boot_ms * 1000;
				model.state.g_vx = pos.vx/100f;
				model.state.g_vy = pos.vy/100f;
				model.state.g_vz = pos.vz/100f;

				gpos_tms = System.currentTimeMillis();
				model.sys.setStatus(Status.MSP_GPOS_AVAILABILITY, true);

			}
		});

		registerListener(msg_highres_imu.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_highres_imu imu = (msg_highres_imu)o;
				model.imu.accx = imu.xacc;
				model.imu.accy = imu.yacc;
				model.imu.accz = imu.zacc;

				model.imu.gyrox = imu.xgyro;
				model.imu.gyroy = imu.ygyro;
				model.imu.gyroz = imu.zgyro;

				model.imu.magx = imu.xmag;
				model.imu.magy = imu.ymag;
				model.imu.magz = imu.zmag;
				model.hud.ap   = imu.pressure_alt;


				model.imu.abs_pressure = imu.abs_pressure;

				model.sys.imu_temp = (int)imu.temperature;
				model.imu.tms = System.nanoTime()/1000;;

				model.sys.setStatus(Status.MSP_READY,true);
				notifyStatusChange();


			}
		});


		registerListener(msg_statustext.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_statustext msg = (msg_statustext)o;
				LogMessage m = new LogMessage();
				m.msg = new String(msg.text);
				m.tms = System.currentTimeMillis();
				m.severity = msg.severity;
				model.msg.set(m);
				writeMessage(m);
			}
		});

		registerListener(msg_rc_channels.class, new IMAVLinkListener() {

			short rssi_old=0;

			@Override
			public void received(Object o) {

				msg_rc_channels rc = (msg_rc_channels)o;

				model.rc.rssi = (short)((rc.rssi+rssi_old)/2);
				rssi_old = (short)rc.rssi;

				model.sys.setStatus(Status.MSP_RC_ATTACHED, (model.rc.rssi>0));
				//				model.sys.setStatus(Status.MSP_RC_ATTACHED, (true));

				model.rc.s0 = rc.chan1_raw < 65534 ? (short)rc.chan1_raw : 1500;
				model.rc.s1 = rc.chan2_raw < 65534 ? (short)rc.chan2_raw : 1500;
				model.rc.s2 = rc.chan3_raw < 65534 ? (short)rc.chan3_raw : 1500;
				model.rc.s3 = rc.chan4_raw < 65534 ? (short)rc.chan4_raw : 1500;
				model.rc.tms = rc.time_boot_ms;

				notifyStatusChange();

			}
		});


		registerListener(msg_heartbeat.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_heartbeat hb = (msg_heartbeat)o;

				model.sys.px4_status = hb.system_status;

				model.sys.setStatus(Status.MSP_ARMED,(hb.base_mode & MAV_MODE_FLAG_DECODE_POSITION.MAV_MODE_FLAG_DECODE_POSITION_SAFETY)>0);

				model.sys.setStatus(Status.MSP_ACTIVE, (hb.system_status & MAV_STATE.MAV_STATE_ACTIVE)>0);
				model.sys.setStatus(Status.MSP_READY, (hb.system_status & MAV_STATE.MAV_STATE_STANDBY)>0);
				model.sys.setStatus(Status.MSP_ARMED, (hb.base_mode & MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED)!=0);

				model.sys.setStatus(Status.MSP_CONNECTED, true);


				model.sys.setStatus(Status.MSP_MODE_ALTITUDE, MAV_CUST_MODE.is(hb.custom_mode,MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_ALTCTL));
				model.sys.setStatus(Status.MSP_MODE_POSITION, MAV_CUST_MODE.is(hb.custom_mode,MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_POSCTL));
				model.sys.setStatus(Status.MSP_MODE_OFFBOARD, MAV_CUST_MODE.is(hb.custom_mode,MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_OFFBOARD));
				model.sys.setStatus(Status.MSP_MODE_STABILIZED, MAV_CUST_MODE.is(hb.custom_mode,MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_STABILIZED));

				model.sys.setStatus(Status.MSP_MODE_LOITER,
						MAV_CUST_MODE.is(hb.custom_mode,MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_AUTO,MAV_CUST_MODE.PX4_CUSTOM_SUB_MODE_AUTO_LOITER));
				model.sys.setStatus(Status.MSP_MODE_MISSION,
						MAV_CUST_MODE.is(hb.custom_mode,MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_AUTO,MAV_CUST_MODE.PX4_CUSTOM_SUB_MODE_AUTO_MISSION));
				model.sys.setStatus(Status.MSP_MODE_LANDING,
						MAV_CUST_MODE.is(hb.custom_mode,MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_AUTO,MAV_CUST_MODE.PX4_CUSTOM_SUB_MODE_AUTO_LAND));
				model.sys.setStatus(Status.MSP_MODE_RTL,
						MAV_CUST_MODE.is(hb.custom_mode,MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_AUTO,MAV_CUST_MODE.PX4_CUSTOM_SUB_MODE_AUTO_RTL));
				model.sys.setStatus(Status.MSP_MODE_TAKEOFF,
						MAV_CUST_MODE.is(hb.custom_mode,MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_AUTO,MAV_CUST_MODE.PX4_CUSTOM_SUB_MODE_AUTO_TAKEOFF));

				model.sys.basemode = hb.base_mode;
				model.sys.custommode = (int)(hb.custom_mode);

				model.sys.tms = System.nanoTime()/1000;

				notifyStatusChange();
			}
		});

		registerListener(msg_battery_status.class, new IMAVLinkListener() {

			@Override
			public void received(Object o) {
				msg_battery_status bat = (msg_battery_status)o;
				if(bat.current_consumed>0)
					model.battery.a0 = bat.current_consumed;
			}

		});

		registerListener(msg_timesync.class, new IMAVLinkListener() {

			@Override
			public void received(Object o) {
				try {

					if(!link.isSerial())
						return;

					long now_ns = System.nanoTime();
					msg_timesync sync = (msg_timesync)o;
					if(sync.tc1==0) {
						msg_timesync sync_s = new msg_timesync(1,1);
						sync_s.tc1 = now_ns;
						sync_s.ts1 = sync.ts1;
						link.write(sync_s);
						return;
					} else if (sync.tc1 > 0) {
						long offset_ns = (sync.ts1 + now_ns - sync.tc1 * 2) / 2;
						long dt = time_offset_ns - offset_ns;

						if (Math.abs(dt) > 10000000) {
							time_offset_ns = offset_ns;
							System.out.println("[sys]  Clock skew detected: "+dt);
						} else
							time_offset_ns = (long)((0.6 * offset_ns) + 0.4 * time_offset_ns);
						model.sys.t_offset_ms = time_offset_ns / 1000000;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		registerListener(msg_autopilot_version.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_autopilot_version version = (msg_autopilot_version)o;
				model.sys.version = String.format("%d.%d.%d",
						(version.flight_sw_version >> (8*3)) & 0xFF,
						(version.flight_sw_version >> (8*2)) & 0xFF,
						(version.flight_sw_version >> (8*1)) & 0xFF);
			}
		});


		registerListener(msg_sys_status.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_sys_status sys = (msg_sys_status)o;
				model.battery.p  = (short)sys.battery_remaining;
				model.battery.b0 = sys.voltage_battery / 1000f;
				model.battery.c0 = sys.current_battery / 100f;

				model.sys.error1  = sys.errors_count1;
				model.sys.load_p  = sys.load/10;
				model.sys.drops_p = sys.drop_rate_comm/10000f;

				// Sensor availability

				model.sys.setSensor(Status.MSP_PIX4FLOW_AVAILABILITY,
						(sys.onboard_control_sensors_enabled & MAV_SYS_STATUS_SENSOR.MAV_SYS_STATUS_SENSOR_OPTICAL_FLOW)>0);

				//
				//				model.sys.setSensor(Status.MSP_GPS_AVAILABILITY,
				//						(sys.onboard_control_sensors_enabled & MAV_SYS_STATUS_SENSOR.MAV_SYS_STATUS_SENSOR_GPS)>0);




			}
		});

		registerListener(msg_extended_sys_state.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_extended_sys_state sys = (msg_extended_sys_state)o;
				model.sys.setStatus(Status.MSP_LANDED,   sys.landed_state==1);
				model.sys.setStatus(Status.MSP_INAIR,    sys.landed_state==0);
				model.sys.setStatus(Status.MSP_FREEFALL, sys.landed_state==2);

				notifyStatusChange();
			}
		});

		System.out.println("MAVLink parser: "+listeners.size()+" MAVLink messagetypes registered");

		startUpAt = System.currentTimeMillis();
		model.sys.tms = System.nanoTime()/1000;

	}

	public void addMAVLinkListener(IMAVLinkListener listener) {
		mavListener.add(listener);
	}

	public void addMAVMessagekListener(IMAVMessageListener listener) {
		msgListener.add(listener);
	}


	public Map<Class<?>,MAVLinkMessage> getMavLinkMessageMap() {
		return mavList;
	}


	public void start(ByteChannel channel) {
		if(!isRunning) {
			isRunning = true;
			stream = new MAVLinkStream(channel);
			Thread s = new Thread(new MAVLinkParserWorker());
			s.start();
		}
	}

	public boolean isConnected() {

		if(!oldStatus.isEqual(model.sys)) {

			notifyStatusChange();

			if(!model.sys.isStatus(Status.MSP_CONNECTED)) {
				model.clear();
				return false;
			}
		}
		return model.sys.isStatus(Status.MSP_CONNECTED);
	}


	public void stop() {
		isRunning = false;
	}

	public void addStatusChangeListener(IMSPStatusChangedListener listener) {
		modeListener.add(listener);
	}

	public void writeMessage(LogMessage m) {
		if(lastMessage == null || lastMessage.tms < m.tms) {
			System.out.println(m.msg);
			if(msgListener!=null) {
				for(IMAVMessageListener msglistener : msgListener)
					msglistener.messageReceived(m);
			}
		}
	}


	private void registerListener(Class<?> clazz, IMAVLinkListener listener) {
		List<IMAVLinkListener> listenerList = null;
		if(!listeners.containsKey(clazz)) {
			listenerList = new ArrayList<IMAVLinkListener>();
			listeners.put(clazz, listenerList);
		} else
			listenerList = listeners.get(clazz);
		listenerList.add(listener);
	}

	public void parseMessage(MAVLinkMessage msg) throws IOException {
		List<IMAVLinkListener> listenerList = null;

		if(msg!=null) {
			model.sys.setStatus(Status.MSP_CONNECTED,true);
			model.sys.tms = System.nanoTime()/1000;

			mavList.put(msg.getClass(),msg);
			listenerList = listeners.get(msg.getClass());
			if(listenerList!=null) {
				for(IMAVLinkListener listener : listenerList)
					listener.received(msg);
			}

			if(mavListener!=null) {
				for(IMAVLinkListener mavlistener : mavListener)
					mavlistener.received(msg);
			}

			if(model.sys.isStatus(Status.MSP_ARMED))
				model.sys.t_armed_ms = System.currentTimeMillis() - t_armed_start;
		}

		// if no global position was published within the last second:
		if((System.currentTimeMillis() - gpos_tms)>1000)
			model.sys.setStatus(Status.MSP_GPOS_AVAILABILITY, false);

		if((System.currentTimeMillis() - mocap_tms)>1000) {
			model.sys.setSensor(Status.MSP_OPCV_AVAILABILITY, false);
			mocap_tms = System.currentTimeMillis();
		}


		if((System.nanoTime()/1000) > (model.sys.tms+1000000) &&
				model.sys.isStatus(Status.MSP_CONNECTED)) {

			model.sys.setStatus(Status.MSP_CONNECTED, false);
			model.sys.setStatus(Status.MSP_READY, false);
			notifyStatusChange();
			link.close(); link.open();
			model.sys.tms = System.nanoTime()/1000;
		}

		if((System.currentTimeMillis() - time_offset_tms) > TIME_SYNC_CYCLE_MS
				&& link.isSerial()) {
			time_offset_tms = System.currentTimeMillis();
			msg_timesync sync_s = new msg_timesync(1,1);
			sync_s.tc1 = 0;
			sync_s.ts1 = model.sys.getCurrentSyncTimeMS()*1000000;
			link.write(sync_s);
		}

		if((System.nanoTime()/1000) > (model.imu.tms+5000000) &&
				model.sys.isStatus(Status.MSP_READY)) {
			model.sys.setStatus(Status.MSP_READY, false);
			notifyStatusChange();
		}
	}

	private class MAVLinkParserWorker implements Runnable {
		MAVLinkMessage msg = null; List<IMAVLinkListener> listenerList = null;

		public void run() {

			try {
				Thread.sleep(20);
			} catch (InterruptedException e1) {
			}

			while (isRunning) {
				try {

					if(stream==null) {
						Thread.sleep(5);
						Thread.yield();
						continue;
					}

					while((msg = stream.read())!=null) {

						model.sys.setStatus(Status.MSP_CONNECTED,true);
						model.sys.tms = System.nanoTime()/1000;

						mavList.put(msg.getClass(),msg);
						listenerList = listeners.get(msg.getClass());
						if(listenerList!=null) {
							for(IMAVLinkListener listener : listenerList)
								listener.received(msg);
						}

						if(mavListener!=null) {
							for(IMAVLinkListener mavlistener : mavListener)
								mavlistener.received(msg);
						}

						if(model.sys.isStatus(Status.MSP_ARMED))
							model.sys.t_armed_ms = System.currentTimeMillis() - t_armed_start;
					}

					// if no global position was published within the last second:
					if((System.currentTimeMillis() - gpos_tms)>1000)
						model.sys.setStatus(Status.MSP_GPOS_AVAILABILITY, false);

					if((System.currentTimeMillis() - mocap_tms)>1000) {
						model.sys.setSensor(Status.MSP_OPCV_AVAILABILITY, false);
						mocap_tms = System.currentTimeMillis();
					}


					if((System.nanoTime()/1000) > (model.sys.tms+1000000) &&
							model.sys.isStatus(Status.MSP_CONNECTED)) {

						model.sys.setStatus(Status.MSP_CONNECTED, false);
						model.sys.setStatus(Status.MSP_READY, false);
						notifyStatusChange();
						link.close(); link.open();
						model.sys.tms = System.nanoTime()/1000;
						Thread.sleep(50);
					}

					if((System.currentTimeMillis() - time_offset_tms) > TIME_SYNC_CYCLE_MS
							&& link.isSerial()) {
						time_offset_tms = System.currentTimeMillis();
						msg_timesync sync_s = new msg_timesync(1,1);
						sync_s.tc1 = 0;
						sync_s.ts1 = model.sys.getCurrentSyncTimeMS()*1000000;
						link.write(sync_s);
					}

					if((System.nanoTime()/1000) > (model.imu.tms+5000000) &&
							model.sys.isStatus(Status.MSP_READY)) {
						model.sys.setStatus(Status.MSP_READY, false);
						notifyStatusChange();
					}

				} catch (Exception e) {

				}
			}
		}
	}


	private synchronized void notifyStatusChange() {
		if(!oldStatus.isEqual(model.sys) && (System.currentTimeMillis() - startUpAt)>2000) {

			for(IMSPStatusChangedListener listener : modeListener)
				listener.update(oldStatus, model.sys);

			if(model.sys.isStatusChanged(oldStatus,Status.MSP_ARMED))
				t_armed_start = System.currentTimeMillis();

			oldStatus.set(model.sys);

		}
	}

}
