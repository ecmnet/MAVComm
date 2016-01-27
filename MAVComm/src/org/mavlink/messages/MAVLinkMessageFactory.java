/**
 * Generated class : MAVLinkMessageFactory
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.IMAVLinkMessage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.mavlink.messages.lquac.msg_request_data_stream;
import org.mavlink.messages.lquac.msg_actuator_control_target;
import org.mavlink.messages.lquac.msg_hil_sensor;
import org.mavlink.messages.lquac.msg_camera_trigger;
import org.mavlink.messages.lquac.msg_param_request_list;
import org.mavlink.messages.lquac.msg_gps_rtk;
import org.mavlink.messages.lquac.msg_position_target_local_ned;
import org.mavlink.messages.lquac.msg_control_system_state;
import org.mavlink.messages.lquac.msg_timesync;
import org.mavlink.messages.lquac.msg_mission_item_reached;
import org.mavlink.messages.lquac.msg_v2_extension;
import org.mavlink.messages.lquac.msg_debug_vect;
import org.mavlink.messages.lquac.msg_battery_status;
import org.mavlink.messages.lquac.msg_mission_current;
import org.mavlink.messages.lquac.msg_vision_position_estimate;
import org.mavlink.messages.lquac.msg_mission_clear_all;
import org.mavlink.messages.lquac.msg_att_pos_mocap;
import org.mavlink.messages.lquac.msg_command_ack;
import org.mavlink.messages.lquac.msg_hil_gps;
import org.mavlink.messages.lquac.msg_log_request_list;
import org.mavlink.messages.lquac.msg_log_request_data;
import org.mavlink.messages.lquac.msg_log_erase;
import org.mavlink.messages.lquac.msg_distance_sensor;
import org.mavlink.messages.lquac.msg_local_position_ned_cov;
import org.mavlink.messages.lquac.msg_attitude_target;
import org.mavlink.messages.lquac.msg_change_operator_control;
import org.mavlink.messages.lquac.msg_mission_request;
import org.mavlink.messages.lquac.msg_global_position_int;
import org.mavlink.messages.lquac.msg_autopilot_version;
import org.mavlink.messages.lquac.msg_vicon_position_estimate;
import org.mavlink.messages.lquac.msg_auth_key;
import org.mavlink.messages.lquac.msg_hil_controls;
import org.mavlink.messages.lquac.msg_mission_write_partial_list;
import org.mavlink.messages.lquac.msg_extended_sys_state;
import org.mavlink.messages.lquac.msg_rc_channels_raw;
import org.mavlink.messages.lquac.msg_terrain_data;
import org.mavlink.messages.lquac.msg_hil_state;
import org.mavlink.messages.lquac.msg_file_transfer_protocol;
import org.mavlink.messages.lquac.msg_encapsulated_data;
import org.mavlink.messages.lquac.msg_mission_count;
import org.mavlink.messages.lquac.msg_set_mode;
import org.mavlink.messages.lquac.msg_vibration;
import org.mavlink.messages.lquac.msg_system_time;
import org.mavlink.messages.lquac.msg_scaled_pressure2;
import org.mavlink.messages.lquac.msg_safety_set_allowed_area;
import org.mavlink.messages.lquac.msg_global_vision_position_estimate;
import org.mavlink.messages.lquac.msg_scaled_pressure3;
import org.mavlink.messages.lquac.msg_ping;
import org.mavlink.messages.lquac.msg_home_position;
import org.mavlink.messages.lquac.msg_mission_item;
import org.mavlink.messages.lquac.msg_raw_imu;
import org.mavlink.messages.lquac.msg_highres_imu;
import org.mavlink.messages.lquac.msg_optical_flow;
import org.mavlink.messages.lquac.msg_landing_target;
import org.mavlink.messages.lquac.msg_command_long;
import org.mavlink.messages.lquac.msg_attitude_quaternion;
import org.mavlink.messages.lquac.msg_scaled_imu2;
import org.mavlink.messages.lquac.msg_data_stream;
import org.mavlink.messages.lquac.msg_scaled_imu3;
import org.mavlink.messages.lquac.msg_change_operator_control_ack;
import org.mavlink.messages.lquac.msg_terrain_request;
import org.mavlink.messages.lquac.msg_terrain_check;
import org.mavlink.messages.lquac.msg_adsb_vehicle;
import org.mavlink.messages.lquac.msg_memory_vect;
import org.mavlink.messages.lquac.msg_hil_rc_inputs_raw;
import org.mavlink.messages.lquac.msg_raw_pressure;
import org.mavlink.messages.lquac.msg_local_position_ned;
import org.mavlink.messages.lquac.msg_nav_controller_output;
import org.mavlink.messages.lquac.msg_gps2_rtk;
import org.mavlink.messages.lquac.msg_set_gps_global_origin;
import org.mavlink.messages.lquac.msg_log_data;
import org.mavlink.messages.lquac.msg_attitude;
import org.mavlink.messages.lquac.msg_serial_control;
import org.mavlink.messages.lquac.msg_param_value;
import org.mavlink.messages.lquac.msg_sim_state;
import org.mavlink.messages.lquac.msg_set_attitude_target;
import org.mavlink.messages.lquac.msg_safety_allowed_area;
import org.mavlink.messages.lquac.msg_gps_global_origin;
import org.mavlink.messages.lquac.msg_log_request_end;
import org.mavlink.messages.lquac.msg_radio_status;
import org.mavlink.messages.lquac.msg_gps_raw_int;
import org.mavlink.messages.lquac.msg_sys_status;
import org.mavlink.messages.lquac.msg_mission_item_int;
import org.mavlink.messages.lquac.msg_manual_setpoint;
import org.mavlink.messages.lquac.msg_named_value_float;
import org.mavlink.messages.lquac.msg_scaled_imu;
import org.mavlink.messages.lquac.msg_rc_channels_scaled;
import org.mavlink.messages.lquac.msg_altitude;
import org.mavlink.messages.lquac.msg_mission_request_partial_list;
import org.mavlink.messages.lquac.msg_global_position_int_cov;
import org.mavlink.messages.lquac.msg_vision_speed_estimate;
import org.mavlink.messages.lquac.msg_rc_channels_override;
import org.mavlink.messages.lquac.msg_vfr_hud;
import org.mavlink.messages.lquac.msg_optical_flow_rad;
import org.mavlink.messages.lquac.msg_set_position_target_local_ned;
import org.mavlink.messages.lquac.msg_log_entry;
import org.mavlink.messages.lquac.msg_data_transmission_handshake;
import org.mavlink.messages.lquac.msg_set_home_position;
import org.mavlink.messages.lquac.msg_gps2_raw;
import org.mavlink.messages.lquac.msg_position_target_global_int;
import org.mavlink.messages.lquac.msg_named_value_int;
import org.mavlink.messages.lquac.msg_heartbeat;
import org.mavlink.messages.lquac.msg_terrain_report;
import org.mavlink.messages.lquac.msg_mission_ack;
import org.mavlink.messages.lquac.msg_mission_request_list;
import org.mavlink.messages.lquac.msg_param_set;
import org.mavlink.messages.lquac.msg_gps_status;
import org.mavlink.messages.lquac.msg_set_position_target_global_int;
import org.mavlink.messages.lquac.msg_manual_control;
import org.mavlink.messages.lquac.msg_message_interval;
import org.mavlink.messages.lquac.msg_scaled_pressure;
import org.mavlink.messages.lquac.msg_hil_state_quaternion;
import org.mavlink.messages.lquac.msg_statustext;
import org.mavlink.messages.lquac.msg_param_map_rc;
import org.mavlink.messages.lquac.msg_power_status;
import org.mavlink.messages.lquac.msg_attitude_quaternion_cov;
import org.mavlink.messages.lquac.msg_hil_optical_flow;
import org.mavlink.messages.lquac.msg_servo_output_raw;
import org.mavlink.messages.lquac.msg_debug;
import org.mavlink.messages.lquac.msg_param_request_read;
import org.mavlink.messages.lquac.msg_command_int;
import org.mavlink.messages.lquac.msg_mission_set_current;
import org.mavlink.messages.lquac.msg_rc_channels;
import org.mavlink.messages.lquac.msg_gps_inject_data;
import org.mavlink.messages.lquac.msg_set_actuator_control_target;
import org.mavlink.messages.lquac.msg_local_position_ned_system_global_offset;
import org.mavlink.messages.lquac.msg_resource_request;
import org.mavlink.messages.lquac.msg_request_data_stream;
import org.mavlink.messages.lquac.msg_actuator_control_target;
import org.mavlink.messages.lquac.msg_hil_sensor;
import org.mavlink.messages.lquac.msg_camera_trigger;
import org.mavlink.messages.lquac.msg_param_request_list;
import org.mavlink.messages.lquac.msg_gps_rtk;
import org.mavlink.messages.lquac.msg_position_target_local_ned;
import org.mavlink.messages.lquac.msg_control_system_state;
import org.mavlink.messages.lquac.msg_timesync;
import org.mavlink.messages.lquac.msg_mission_item_reached;
import org.mavlink.messages.lquac.msg_v2_extension;
import org.mavlink.messages.lquac.msg_debug_vect;
import org.mavlink.messages.lquac.msg_msp_status;
import org.mavlink.messages.lquac.msg_battery_status;
import org.mavlink.messages.lquac.msg_mission_current;
import org.mavlink.messages.lquac.msg_vision_position_estimate;
import org.mavlink.messages.lquac.msg_mission_clear_all;
import org.mavlink.messages.lquac.msg_att_pos_mocap;
import org.mavlink.messages.lquac.msg_command_ack;
import org.mavlink.messages.lquac.msg_hil_gps;
import org.mavlink.messages.lquac.msg_log_request_list;
import org.mavlink.messages.lquac.msg_log_request_data;
import org.mavlink.messages.lquac.msg_log_erase;
import org.mavlink.messages.lquac.msg_distance_sensor;
import org.mavlink.messages.lquac.msg_local_position_ned_cov;
import org.mavlink.messages.lquac.msg_attitude_target;
import org.mavlink.messages.lquac.msg_change_operator_control;
import org.mavlink.messages.lquac.msg_mission_request;
import org.mavlink.messages.lquac.msg_global_position_int;
import org.mavlink.messages.lquac.msg_autopilot_version;
import org.mavlink.messages.lquac.msg_vicon_position_estimate;
import org.mavlink.messages.lquac.msg_auth_key;
import org.mavlink.messages.lquac.msg_hil_controls;
import org.mavlink.messages.lquac.msg_mission_write_partial_list;
import org.mavlink.messages.lquac.msg_extended_sys_state;
import org.mavlink.messages.lquac.msg_rc_channels_raw;
import org.mavlink.messages.lquac.msg_terrain_data;
import org.mavlink.messages.lquac.msg_hil_state;
import org.mavlink.messages.lquac.msg_file_transfer_protocol;
import org.mavlink.messages.lquac.msg_encapsulated_data;
import org.mavlink.messages.lquac.msg_mission_count;
import org.mavlink.messages.lquac.msg_set_mode;
import org.mavlink.messages.lquac.msg_vibration;
import org.mavlink.messages.lquac.msg_system_time;
import org.mavlink.messages.lquac.msg_scaled_pressure2;
import org.mavlink.messages.lquac.msg_safety_set_allowed_area;
import org.mavlink.messages.lquac.msg_global_vision_position_estimate;
import org.mavlink.messages.lquac.msg_scaled_pressure3;
import org.mavlink.messages.lquac.msg_ping;
import org.mavlink.messages.lquac.msg_home_position;
import org.mavlink.messages.lquac.msg_mission_item;
import org.mavlink.messages.lquac.msg_raw_imu;
import org.mavlink.messages.lquac.msg_highres_imu;
import org.mavlink.messages.lquac.msg_optical_flow;
import org.mavlink.messages.lquac.msg_landing_target;
import org.mavlink.messages.lquac.msg_command_long;
import org.mavlink.messages.lquac.msg_attitude_quaternion;
import org.mavlink.messages.lquac.msg_scaled_imu2;
import org.mavlink.messages.lquac.msg_data_stream;
import org.mavlink.messages.lquac.msg_scaled_imu3;
import org.mavlink.messages.lquac.msg_change_operator_control_ack;
import org.mavlink.messages.lquac.msg_terrain_request;
import org.mavlink.messages.lquac.msg_terrain_check;
import org.mavlink.messages.lquac.msg_adsb_vehicle;
import org.mavlink.messages.lquac.msg_memory_vect;
import org.mavlink.messages.lquac.msg_hil_rc_inputs_raw;
import org.mavlink.messages.lquac.msg_raw_pressure;
import org.mavlink.messages.lquac.msg_local_position_ned;
import org.mavlink.messages.lquac.msg_nav_controller_output;
import org.mavlink.messages.lquac.msg_gps2_rtk;
import org.mavlink.messages.lquac.msg_set_gps_global_origin;
import org.mavlink.messages.lquac.msg_log_data;
import org.mavlink.messages.lquac.msg_attitude;
import org.mavlink.messages.lquac.msg_serial_control;
import org.mavlink.messages.lquac.msg_param_value;
import org.mavlink.messages.lquac.msg_sim_state;
import org.mavlink.messages.lquac.msg_set_attitude_target;
import org.mavlink.messages.lquac.msg_safety_allowed_area;
import org.mavlink.messages.lquac.msg_gps_global_origin;
import org.mavlink.messages.lquac.msg_log_request_end;
import org.mavlink.messages.lquac.msg_radio_status;
import org.mavlink.messages.lquac.msg_gps_raw_int;
import org.mavlink.messages.lquac.msg_sys_status;
import org.mavlink.messages.lquac.msg_mission_item_int;
import org.mavlink.messages.lquac.msg_manual_setpoint;
import org.mavlink.messages.lquac.msg_named_value_float;
import org.mavlink.messages.lquac.msg_scaled_imu;
import org.mavlink.messages.lquac.msg_rc_channels_scaled;
import org.mavlink.messages.lquac.msg_altitude;
import org.mavlink.messages.lquac.msg_msp_command;
import org.mavlink.messages.lquac.msg_mission_request_partial_list;
import org.mavlink.messages.lquac.msg_global_position_int_cov;
import org.mavlink.messages.lquac.msg_vision_speed_estimate;
import org.mavlink.messages.lquac.msg_rc_channels_override;
import org.mavlink.messages.lquac.msg_vfr_hud;
import org.mavlink.messages.lquac.msg_optical_flow_rad;
import org.mavlink.messages.lquac.msg_set_position_target_local_ned;
import org.mavlink.messages.lquac.msg_log_entry;
import org.mavlink.messages.lquac.msg_data_transmission_handshake;
import org.mavlink.messages.lquac.msg_set_home_position;
import org.mavlink.messages.lquac.msg_gps2_raw;
import org.mavlink.messages.lquac.msg_position_target_global_int;
import org.mavlink.messages.lquac.msg_named_value_int;
import org.mavlink.messages.lquac.msg_heartbeat;
import org.mavlink.messages.lquac.msg_terrain_report;
import org.mavlink.messages.lquac.msg_mission_ack;
import org.mavlink.messages.lquac.msg_mission_request_list;
import org.mavlink.messages.lquac.msg_param_set;
import org.mavlink.messages.lquac.msg_gps_status;
import org.mavlink.messages.lquac.msg_set_position_target_global_int;
import org.mavlink.messages.lquac.msg_manual_control;
import org.mavlink.messages.lquac.msg_message_interval;
import org.mavlink.messages.lquac.msg_scaled_pressure;
import org.mavlink.messages.lquac.msg_hil_state_quaternion;
import org.mavlink.messages.lquac.msg_statustext;
import org.mavlink.messages.lquac.msg_param_map_rc;
import org.mavlink.messages.lquac.msg_power_status;
import org.mavlink.messages.lquac.msg_attitude_quaternion_cov;
import org.mavlink.messages.lquac.msg_hil_optical_flow;
import org.mavlink.messages.lquac.msg_servo_output_raw;
import org.mavlink.messages.lquac.msg_debug;
import org.mavlink.messages.lquac.msg_param_request_read;
import org.mavlink.messages.lquac.msg_command_int;
import org.mavlink.messages.lquac.msg_mission_set_current;
import org.mavlink.messages.lquac.msg_rc_channels;
import org.mavlink.messages.lquac.msg_gps_inject_data;
import org.mavlink.messages.lquac.msg_set_actuator_control_target;
import org.mavlink.messages.lquac.msg_local_position_ned_system_global_offset;
import org.mavlink.messages.lquac.msg_resource_request;
/**
 * Class MAVLinkMessageFactory
 * Generate MAVLink message classes from byte array
 **/
public class MAVLinkMessageFactory implements IMAVLinkMessage, IMAVLinkMessageID {
public static MAVLinkMessage getMessage(int msgid, int sysId, int componentId, byte[] rawData) throws IOException {
    MAVLinkMessage msg=null;
    ByteBuffer dis = ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN);
    switch(msgid) {
  case MAVLINK_MSG_ID_REQUEST_DATA_STREAM:
      msg = new msg_request_data_stream(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_ACTUATOR_CONTROL_TARGET:
      msg = new msg_actuator_control_target(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_HIL_SENSOR:
      msg = new msg_hil_sensor(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_CAMERA_TRIGGER:
      msg = new msg_camera_trigger(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_PARAM_REQUEST_LIST:
      msg = new msg_param_request_list(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_GPS_RTK:
      msg = new msg_gps_rtk(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_POSITION_TARGET_LOCAL_NED:
      msg = new msg_position_target_local_ned(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_CONTROL_SYSTEM_STATE:
      msg = new msg_control_system_state(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_TIMESYNC:
      msg = new msg_timesync(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_MISSION_ITEM_REACHED:
      msg = new msg_mission_item_reached(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_V2_EXTENSION:
      msg = new msg_v2_extension(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_DEBUG_VECT:
      msg = new msg_debug_vect(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_MSP_STATUS:
      msg = new msg_msp_status(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_BATTERY_STATUS:
      msg = new msg_battery_status(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_MISSION_CURRENT:
      msg = new msg_mission_current(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_VISION_POSITION_ESTIMATE:
      msg = new msg_vision_position_estimate(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_MISSION_CLEAR_ALL:
      msg = new msg_mission_clear_all(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_ATT_POS_MOCAP:
      msg = new msg_att_pos_mocap(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_COMMAND_ACK:
      msg = new msg_command_ack(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_HIL_GPS:
      msg = new msg_hil_gps(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_LOG_REQUEST_LIST:
      msg = new msg_log_request_list(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_LOG_REQUEST_DATA:
      msg = new msg_log_request_data(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_LOG_ERASE:
      msg = new msg_log_erase(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_DISTANCE_SENSOR:
      msg = new msg_distance_sensor(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_LOCAL_POSITION_NED_COV:
      msg = new msg_local_position_ned_cov(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_ATTITUDE_TARGET:
      msg = new msg_attitude_target(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_CHANGE_OPERATOR_CONTROL:
      msg = new msg_change_operator_control(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_MISSION_REQUEST:
      msg = new msg_mission_request(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_GLOBAL_POSITION_INT:
      msg = new msg_global_position_int(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_AUTOPILOT_VERSION:
      msg = new msg_autopilot_version(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_VICON_POSITION_ESTIMATE:
      msg = new msg_vicon_position_estimate(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_AUTH_KEY:
      msg = new msg_auth_key(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_HIL_CONTROLS:
      msg = new msg_hil_controls(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_MISSION_WRITE_PARTIAL_LIST:
      msg = new msg_mission_write_partial_list(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_EXTENDED_SYS_STATE:
      msg = new msg_extended_sys_state(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_RC_CHANNELS_RAW:
      msg = new msg_rc_channels_raw(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_TERRAIN_DATA:
      msg = new msg_terrain_data(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_HIL_STATE:
      msg = new msg_hil_state(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_FILE_TRANSFER_PROTOCOL:
      msg = new msg_file_transfer_protocol(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_ENCAPSULATED_DATA:
      msg = new msg_encapsulated_data(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_MISSION_COUNT:
      msg = new msg_mission_count(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_SET_MODE:
      msg = new msg_set_mode(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_VIBRATION:
      msg = new msg_vibration(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_SYSTEM_TIME:
      msg = new msg_system_time(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_SCALED_PRESSURE2:
      msg = new msg_scaled_pressure2(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_SAFETY_SET_ALLOWED_AREA:
      msg = new msg_safety_set_allowed_area(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_GLOBAL_VISION_POSITION_ESTIMATE:
      msg = new msg_global_vision_position_estimate(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_SCALED_PRESSURE3:
      msg = new msg_scaled_pressure3(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_PING:
      msg = new msg_ping(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_HOME_POSITION:
      msg = new msg_home_position(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_MISSION_ITEM:
      msg = new msg_mission_item(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_RAW_IMU:
      msg = new msg_raw_imu(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_HIGHRES_IMU:
      msg = new msg_highres_imu(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_OPTICAL_FLOW:
      msg = new msg_optical_flow(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_LANDING_TARGET:
      msg = new msg_landing_target(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_COMMAND_LONG:
      msg = new msg_command_long(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_ATTITUDE_QUATERNION:
      msg = new msg_attitude_quaternion(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_SCALED_IMU2:
      msg = new msg_scaled_imu2(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_DATA_STREAM:
      msg = new msg_data_stream(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_SCALED_IMU3:
      msg = new msg_scaled_imu3(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_CHANGE_OPERATOR_CONTROL_ACK:
      msg = new msg_change_operator_control_ack(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_TERRAIN_REQUEST:
      msg = new msg_terrain_request(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_TERRAIN_CHECK:
      msg = new msg_terrain_check(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_ADSB_VEHICLE:
      msg = new msg_adsb_vehicle(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_MEMORY_VECT:
      msg = new msg_memory_vect(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_HIL_RC_INPUTS_RAW:
      msg = new msg_hil_rc_inputs_raw(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_RAW_PRESSURE:
      msg = new msg_raw_pressure(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_LOCAL_POSITION_NED:
      msg = new msg_local_position_ned(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_NAV_CONTROLLER_OUTPUT:
      msg = new msg_nav_controller_output(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_GPS2_RTK:
      msg = new msg_gps2_rtk(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_SET_GPS_GLOBAL_ORIGIN:
      msg = new msg_set_gps_global_origin(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_LOG_DATA:
      msg = new msg_log_data(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_ATTITUDE:
      msg = new msg_attitude(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_SERIAL_CONTROL:
      msg = new msg_serial_control(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_PARAM_VALUE:
      msg = new msg_param_value(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_SIM_STATE:
      msg = new msg_sim_state(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_SET_ATTITUDE_TARGET:
      msg = new msg_set_attitude_target(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_SAFETY_ALLOWED_AREA:
      msg = new msg_safety_allowed_area(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_GPS_GLOBAL_ORIGIN:
      msg = new msg_gps_global_origin(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_LOG_REQUEST_END:
      msg = new msg_log_request_end(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_RADIO_STATUS:
      msg = new msg_radio_status(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_GPS_RAW_INT:
      msg = new msg_gps_raw_int(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_SYS_STATUS:
      msg = new msg_sys_status(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_MISSION_ITEM_INT:
      msg = new msg_mission_item_int(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_MANUAL_SETPOINT:
      msg = new msg_manual_setpoint(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_NAMED_VALUE_FLOAT:
      msg = new msg_named_value_float(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_SCALED_IMU:
      msg = new msg_scaled_imu(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_RC_CHANNELS_SCALED:
      msg = new msg_rc_channels_scaled(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_ALTITUDE:
      msg = new msg_altitude(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_MSP_COMMAND:
      msg = new msg_msp_command(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_MISSION_REQUEST_PARTIAL_LIST:
      msg = new msg_mission_request_partial_list(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_GLOBAL_POSITION_INT_COV:
      msg = new msg_global_position_int_cov(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_VISION_SPEED_ESTIMATE:
      msg = new msg_vision_speed_estimate(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_RC_CHANNELS_OVERRIDE:
      msg = new msg_rc_channels_override(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_VFR_HUD:
      msg = new msg_vfr_hud(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_OPTICAL_FLOW_RAD:
      msg = new msg_optical_flow_rad(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_SET_POSITION_TARGET_LOCAL_NED:
      msg = new msg_set_position_target_local_ned(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_LOG_ENTRY:
      msg = new msg_log_entry(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_DATA_TRANSMISSION_HANDSHAKE:
      msg = new msg_data_transmission_handshake(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_SET_HOME_POSITION:
      msg = new msg_set_home_position(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_GPS2_RAW:
      msg = new msg_gps2_raw(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_POSITION_TARGET_GLOBAL_INT:
      msg = new msg_position_target_global_int(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_NAMED_VALUE_INT:
      msg = new msg_named_value_int(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_HEARTBEAT:
      msg = new msg_heartbeat(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_TERRAIN_REPORT:
      msg = new msg_terrain_report(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_MISSION_ACK:
      msg = new msg_mission_ack(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_MISSION_REQUEST_LIST:
      msg = new msg_mission_request_list(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_PARAM_SET:
      msg = new msg_param_set(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_GPS_STATUS:
      msg = new msg_gps_status(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_SET_POSITION_TARGET_GLOBAL_INT:
      msg = new msg_set_position_target_global_int(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_MANUAL_CONTROL:
      msg = new msg_manual_control(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_MESSAGE_INTERVAL:
      msg = new msg_message_interval(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_SCALED_PRESSURE:
      msg = new msg_scaled_pressure(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_HIL_STATE_QUATERNION:
      msg = new msg_hil_state_quaternion(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_STATUSTEXT:
      msg = new msg_statustext(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_PARAM_MAP_RC:
      msg = new msg_param_map_rc(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_POWER_STATUS:
      msg = new msg_power_status(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_ATTITUDE_QUATERNION_COV:
      msg = new msg_attitude_quaternion_cov(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_HIL_OPTICAL_FLOW:
      msg = new msg_hil_optical_flow(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_SERVO_OUTPUT_RAW:
      msg = new msg_servo_output_raw(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_DEBUG:
      msg = new msg_debug(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_PARAM_REQUEST_READ:
      msg = new msg_param_request_read(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_COMMAND_INT:
      msg = new msg_command_int(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_MISSION_SET_CURRENT:
      msg = new msg_mission_set_current(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_RC_CHANNELS:
      msg = new msg_rc_channels(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_GPS_INJECT_DATA:
      msg = new msg_gps_inject_data(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_SET_ACTUATOR_CONTROL_TARGET:
      msg = new msg_set_actuator_control_target(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_LOCAL_POSITION_NED_SYSTEM_GLOBAL_OFFSET:
      msg = new msg_local_position_ned_system_global_offset(sysId, componentId);
      msg.decode(dis);
      break;
  case MAVLINK_MSG_ID_RESOURCE_REQUEST:
      msg = new msg_resource_request(sysId, componentId);
      msg.decode(dis);
      break;
  default:
      System.out.println("Mavlink Factory Error : unknown MsgId : " + msgid);
    }
    return msg;
  }
}
