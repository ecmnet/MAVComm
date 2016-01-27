package com.comino.mav.mavlink;

import java.nio.channels.ByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.messages.MAV_MODE_FLAG;
import org.mavlink.messages.MAV_MODE_FLAG_DECODE_POSITION;
import org.mavlink.messages.MAV_STATE;
import org.mavlink.messages.MAV_SYS_STATUS_SENSOR;
import org.mavlink.messages.lquac.msg_altitude;
import org.mavlink.messages.lquac.msg_attitude;
import org.mavlink.messages.lquac.msg_distance_sensor;
import org.mavlink.messages.lquac.msg_extended_sys_state;
import org.mavlink.messages.lquac.msg_global_position_int;
import org.mavlink.messages.lquac.msg_gps_raw_int;
import org.mavlink.messages.lquac.msg_heartbeat;
import org.mavlink.messages.lquac.msg_highres_imu;
import org.mavlink.messages.lquac.msg_home_position;
import org.mavlink.messages.lquac.msg_local_position_ned;
import org.mavlink.messages.lquac.msg_optical_flow_rad;
import org.mavlink.messages.lquac.msg_position_target_local_ned;
import org.mavlink.messages.lquac.msg_rc_channels;
import org.mavlink.messages.lquac.msg_statustext;
import org.mavlink.messages.lquac.msg_sys_status;
import org.mavlink.messages.lquac.msg_vfr_hud;

import com.comino.mav.comm.IMAVComm;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.GPS;
import com.comino.msp.model.segment.Message;
import com.comino.msp.model.segment.Status;


public class MAVLinkToModelParser {


	private MAVLinkStream stream;
	private DataModel model;

	private ArrayList<Message>					msgList     = null;
	private HashMap<Class<?>,MAVLinkMessage>	mavList     = null;

	private IMAVComm link = null;

	private HashMap<Class<?>,IMAVLinkMsgListener> listeners = null;
	private IMAVLinkMsgListener proxyListener = null;
	private boolean isRunning = false;

	public MAVLinkToModelParser(DataModel model, ByteChannel channel, IMAVComm link) {

		this.model = model;
		this.link = link;
		this.msgList   = new ArrayList<Message>();
		this.mavList   = new HashMap<Class<?>,MAVLinkMessage>();

		listeners = new HashMap<Class<?>,IMAVLinkMsgListener>();
		stream = new MAVLinkStream(channel);


		registerListener(msg_vfr_hud.class, new IMAVLinkMsgListener() {
			@Override
			public void received(Object o) {
				msg_vfr_hud hud = (msg_vfr_hud)o;
				model.attitude.h = hud.heading;
				model.attitude.s = hud.groundspeed;
				model.state.h    = hud.heading;       
			}
		});
		
		registerListener(msg_distance_sensor.class, new IMAVLinkMsgListener() {
			@Override
			public void received(Object o) {
				msg_distance_sensor lidar = (msg_distance_sensor)o;
				model.raw.di = lidar.current_distance / 100f;
				model.sys.setSensor(Status.MSP_LIDAR_AVAILABILITY, true);
				
				
			}
		});
		
		registerListener(msg_optical_flow_rad.class, new IMAVLinkMsgListener() {
			@Override
			public void received(Object o) {
				msg_optical_flow_rad flow = (msg_optical_flow_rad)o;
				model.raw.fX  = flow.integrated_x;
				model.raw.fY  = flow.integrated_y;
				model.raw.tms = flow.time_usec;
			}
		});
		
		registerListener(msg_altitude.class, new IMAVLinkMsgListener() {
			@Override
			public void received(Object o) {
				msg_altitude alt = (msg_altitude)o;
				model.attitude.al   = alt.altitude_local;
			}
		});

		registerListener(msg_attitude.class, new IMAVLinkMsgListener() {
			@Override
			public void received(Object o) {
				msg_attitude att = (msg_attitude)o;
				model.attitude.aX   = att.roll;
				model.attitude.aY   = att.pitch;
				model.attitude.tms  = att.time_boot_ms*1000;
				
				model.sys.setSensor(Status.MSP_IMU_AVAILABILITY, true);
				
				//System.out.println(att.toString());
			}
		});

		registerListener(msg_gps_raw_int.class, new IMAVLinkMsgListener() {
			@Override
			public void received(Object o) {
				msg_gps_raw_int gps = (msg_gps_raw_int)o;
				model.gps.numsat = (byte) gps.satellites_visible;
				model.gps.setFlag(GPS.GPS_SAT_FIX, gps.fix_type>0);
				model.gps.setFlag(GPS.GPS_SAT_VALID, true);
				model.gps.error2d   = gps.eph/100f;
				
				model.sys.setSensor(Status.MSP_GPS_AVAILABILITY, model.gps.numsat>5);

			}
		});
	

		registerListener(msg_home_position.class, new IMAVLinkMsgListener() {
			@Override
			public void received(Object o) {
				msg_home_position ref = (msg_home_position)o;
				model.gps.ref_lat = ref.latitude/10000000f;
				model.gps.ref_lon = ref.longitude/10000000f;
				model.gps.ref_altitude = ref.altitude;	
				model.gps.setFlag(GPS.GPS_REF_VALID, true);
			}
		});

		registerListener(msg_local_position_ned.class, new IMAVLinkMsgListener() {
			@Override
			public void received(Object o) {
				msg_local_position_ned ned = (msg_local_position_ned)o;
				model.state.x = ned.x;
				model.state.y = ned.y;
				model.state.z = ned.z;

				model.state.vx = ned.vx;
				model.state.vy = ned.vy;
				model.state.vz = ned.vz;

				model.state.tms = ned.time_boot_ms*1000;
				
			}
		});
		
		
		registerListener(msg_position_target_local_ned.class, new IMAVLinkMsgListener() {
			@Override
			public void received(Object o) {
				msg_position_target_local_ned ned = (msg_position_target_local_ned)o;
				
				model.target_state.x = ned.x;
				model.target_state.y = ned.y;
				model.target_state.z = ned.z;

				model.target_state.vx = ned.vx;
				model.target_state.vy = ned.vy;
				model.target_state.vz = ned.vz;

				model.target_state.tms = ned.time_boot_ms*1000;
				
			}
		});

		registerListener(msg_global_position_int.class, new IMAVLinkMsgListener() {
			@Override
			public void received(Object o) {
				msg_global_position_int pos = (msg_global_position_int)o;

				model.gps.latitude 	= pos.lat/10000000f;
				model.gps.longitude	= pos.lon/10000000f;
				model.gps.heading   = (short)(pos.hdg/1000);
				model.gps.altitude  = (short)(pos.alt/1000);
				model.gps.tms = pos.time_boot_ms * 1000;

			}
		});

		registerListener(msg_highres_imu.class, new IMAVLinkMsgListener() {
			@Override
			public void received(Object o) {
				msg_highres_imu imu = (msg_highres_imu)o;
				model.imu.accx = imu.xacc;
				model.imu.accy = imu.yacc;
				model.imu.accz = imu.zacc;

				model.imu.gyrox = imu.xgyro;
				model.imu.gyroy = imu.ygyro;
				model.imu.gyroz = imu.zgyro;
				
				model.imu.abs_pressure = imu.abs_pressure;
				
				model.sys.imu_temp = (int)imu.temperature;

				model.imu.tms =imu.time_usec;
				
			}
		});


		registerListener(msg_statustext.class, new IMAVLinkMsgListener() {
			@Override
			public void received(Object o) {
				msg_statustext msg = (msg_statustext)o;
				Message m = new Message();
				m.msg = new String(msg.text);
				m.tms = System.nanoTime()/1000;
				m.severity = msg.severity;
			
				if(msgList.size()>0) {
					if(msgList.get(msgList.size()-1).tms < m.tms)
					   msgList.add(m);	
				} else
					  msgList.add(m);
			}
		});

		registerListener(msg_rc_channels.class, new IMAVLinkMsgListener() {
			@Override
			public void received(Object o) {
				msg_rc_channels rc = (msg_rc_channels)o;
				model.sys.setStatus(Status.MSP_RC_ATTACHED, (rc.rssi>0));
			//	System.out.println(rc);

			}
		});


		registerListener(msg_heartbeat.class, new IMAVLinkMsgListener() {
			@Override
			public void received(Object o) {
				msg_heartbeat hb = (msg_heartbeat)o;
				
				//System.out.println(hb.toString());

				// Status update
				
				
//				System.out.println("ALT "+PX4CustomModes.is(hb.custom_mode,PX4CustomModes.PX4_CUSTOM_MAIN_MODE_ALTCTL));
//				System.out.println("MAN "+PX4CustomModes.is(hb.custom_mode,PX4CustomModes.PX4_CUSTOM_MAIN_MODE_MANUAL));
//				
				model.sys.setStatus(Status.MSP_ARMED,(hb.base_mode & MAV_MODE_FLAG_DECODE_POSITION.MAV_MODE_FLAG_DECODE_POSITION_SAFETY)>0);

				model.sys.setStatus(Status.MSP_ACTIVE, (hb.system_status & MAV_STATE.MAV_STATE_ACTIVE)>0);
				model.sys.setStatus(Status.MSP_READY, (hb.system_status & MAV_STATE.MAV_STATE_STANDBY)>0);				
				model.sys.setStatus(Status.MSP_ARMED, (hb.base_mode & MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED)!=0);
				model.sys.setStatus(Status.MSP_MODE_STABILIZED, (hb.base_mode & MAV_MODE_FLAG.MAV_MODE_FLAG_STABILIZE_ENABLED)!=0);

				model.sys.setStatus(Status.MSP_CONNECTED, true);
				
				model.sys.setStatus(Status.MSP_MODE_ALTITUDE, MAV_CUST_MODE.is(hb.custom_mode,MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_ALTCTL));
				model.sys.setStatus(Status.MSP_MODE_POSITION, MAV_CUST_MODE.is(hb.custom_mode,MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_POSCTL));
				model.sys.setStatus(Status.MSP_MODE_LOITER, MAV_CUST_MODE.is(hb.custom_mode,MAV_CUST_MODE.PX4_CUSTOM_SUB_MODE_AUTO_LOITER));
				
				model.sys.basemode = hb.base_mode;
				
				model.sys.tms = System.nanoTime()/1000;

			}
		});


		registerListener(msg_sys_status.class, new IMAVLinkMsgListener() {
			@Override
			public void received(Object o) {
				msg_sys_status sys = (msg_sys_status)o;
				model.battery.p  = (short)sys.battery_remaining;
				model.battery.b0 = sys.voltage_battery / 1000f;
				model.battery.c0 = sys.current_battery / 100f;

				model.sys.error1  = sys.errors_count1;
				model.sys.load_p  = sys.load/1000f;
				model.sys.drops_p = sys.drop_rate_comm/10000f;

				// Sensor availabilit
				
				model.sys.tms = System.nanoTime()/1000;

				model.sys.setSensor(Status.MSP_PIX4FLOW_AVAILABILITY, 
						(sys.onboard_control_sensors_enabled & MAV_SYS_STATUS_SENSOR.MAV_SYS_STATUS_SENSOR_OPTICAL_FLOW)>0);

				// TODO: GPS not found
				model.sys.setSensor(Status.MSP_GPS_AVAILABILITY, 
						(sys.onboard_control_sensors_enabled & MAV_SYS_STATUS_SENSOR.MAV_SYS_STATUS_SENSOR_GPS)>0);
				
				


			}
		});
		
		registerListener(msg_extended_sys_state.class, new IMAVLinkMsgListener() {
			@Override
			public void received(Object o) {
				msg_extended_sys_state sys = (msg_extended_sys_state)o;
				model.sys.setStatus(Status.MSP_LANDED, sys.landed_state>0);
				   
			}
		});

		System.out.println("MAVLink parser: "+listeners.size()+" MAVLink messagetypes registered");

	}
	
	public void registerProxyListener(IMAVLinkMsgListener listener) {
		System.out.println("ProxyListener "+listener.getClass().getName()+" loaded");
		this.proxyListener = listener;
	}

	public List<Message> getMessageList() {
		return msgList;
	}
	
	public Map<Class<?>,MAVLinkMessage> getMavLinkMessageMap() {
		return mavList;
	}

	public void start() {
		isRunning = true;
		Thread s = new Thread(new MAVLinkParserWorker());
		s.start();
	}


	public void stop() {
		isRunning = false;
	}
	

	private void registerListener(Class<?> clazz, IMAVLinkMsgListener listener) {
		listeners.put(clazz, listener);
	}

	private class MAVLinkParserWorker implements Runnable {

		public void run() {
			model.sys.tms = System.nanoTime()/1000;
			while (isRunning) {
				try {

					MAVLinkMessage msg = stream.read();
					if (msg == null) {
						
						if((System.nanoTime()/1000) > (model.sys.tms+2500000) && 
							    model.sys.isStatus(Status.MSP_CONNECTED)) {
						model.sys.setStatus(Status.MSP_CONNECTED, false);
						msgList.add(new Message("Connection lost",2));
						System.out.println("Connection lost");
						link.close(); link.open();
						model.sys.tms = System.nanoTime()/1000;
					}
						continue;
					}
					
					model.sys.setStatus(Status.MSP_CONNECTED,true);
					model.sys.tms = System.nanoTime()/1000;
					
					mavList.put(msg.getClass(),msg);
					IMAVLinkMsgListener listener = listeners.get(msg.getClass());
					if(listener!=null)
						listener.received(msg);
					
					if(proxyListener!=null)
						proxyListener.received(msg);
					

				} catch (Exception e) {
					model.sys.setStatus(Status.MSP_CONNECTED,false);
			    	
				}

			}
		}
	}

}
