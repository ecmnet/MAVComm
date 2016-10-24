/**
 * Generated class : msg_high_latency
 * DO NOT MODIFY!
 **/
package org.mavlink.messages.lquac;
import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.IMAVLinkCRC;
import org.mavlink.MAVLinkCRC;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.mavlink.io.LittleEndianDataInputStream;
import org.mavlink.io.LittleEndianDataOutputStream;
/**
 * Class msg_high_latency
 * Message appropriate for high latency connections like Iridium
 **/
public class msg_high_latency extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_HIGH_LATENCY = 234;
  private static final long serialVersionUID = MAVLINK_MSG_ID_HIGH_LATENCY;
  public msg_high_latency() {
    this(1,1);
}
  public msg_high_latency(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_HIGH_LATENCY;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 54;
}

  /**
   * Timestamp (microseconds since UNIX epoch)
   */
  public long time_usec;
  /**
   * A bitfield for use for autopilot-specific flags.
   */
  public long custom_mode;
  /**
   * Latitude, expressed as degrees * 1E7
   */
  public long latitude;
  /**
   * Longitude, expressed as degrees * 1E7
   */
  public long longitude;
  /**
   * roll (centidegrees)
   */
  public int roll;
  /**
   * pitch (centidegrees)
   */
  public int pitch;
  /**
   * heading (centidegrees)
   */
  public int heading;
  /**
   * roll setpoint (centidegrees)
   */
  public int roll_sp;
  /**
   * pitch setpoint (centidegrees)
   */
  public int pitch_sp;
  /**
   * heading setpoint (centidegrees)
   */
  public int heading_sp;
  /**
   * Altitude above the home position (meters)
   */
  public int altitude_home;
  /**
   * Altitude above mean sea level (meters)
   */
  public int altitude_amsl;
  /**
   * Altitude setpoint relative to the home position (meters)
   */
  public int altitude_sp;
  /**
   * distance to target (meters)
   */
  public int wp_distance;
  /**
   * System mode bitfield, see MAV_MODE_FLAG ENUM in mavlink/include/mavlink_types.h
   */
  public int base_mode;
  /**
   * The landed state. Is set to MAV_LANDED_STATE_UNDEFINED if landed state is unknown.
   */
  public int landed_state;
  /**
   * throttle (percentage)
   */
  public int throttle;
  /**
   * airspeed (m/s)
   */
  public int airspeed;
  /**
   * airspeed setpoint (m/s)
   */
  public int airspeed_sp;
  /**
   * groundspeed (m/s)
   */
  public int groundspeed;
  /**
   * climb rate (m/s)
   */
  public int climb_rate;
  /**
   * Number of satellites visible. If unknown, set to 255
   */
  public int gps_nsat;
  /**
   * See the GPS_FIX_TYPE enum.
   */
  public int gps_fix_type;
  /**
   * Remaining battery (percentage)
   */
  public int battery_remaining;
  /**
   * Autopilot temperature (degrees C)
   */
  public int temperature;
  /**
   * Air temperature (degrees C) from airspeed sensor
   */
  public int temperature_air;
  /**
   * failsafe (each bit represents a failsafe where 0=ok, 1=failsafe active (bit0:RC, bit1:batt, bit2:GPS, bit3:GCS, bit4:fence)
   */
  public int failsafe;
  /**
   * current waypoint number
   */
  public int wp_num;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  custom_mode = (int)dis.readInt()&0x00FFFFFFFF;
  latitude = (int)dis.readInt();
  longitude = (int)dis.readInt();
  roll = (int)dis.readShort();
  pitch = (int)dis.readShort();
  heading = (int)dis.readUnsignedShort()&0x00FFFF;
  roll_sp = (int)dis.readShort();
  pitch_sp = (int)dis.readShort();
  heading_sp = (int)dis.readShort();
  altitude_home = (int)dis.readShort();
  altitude_amsl = (int)dis.readShort();
  altitude_sp = (int)dis.readShort();
  wp_distance = (int)dis.readUnsignedShort()&0x00FFFF;
  base_mode = (int)dis.readUnsignedByte()&0x00FF;
  landed_state = (int)dis.readUnsignedByte()&0x00FF;
  throttle = (int)dis.readByte();
  airspeed = (int)dis.readUnsignedByte()&0x00FF;
  airspeed_sp = (int)dis.readUnsignedByte()&0x00FF;
  groundspeed = (int)dis.readUnsignedByte()&0x00FF;
  climb_rate = (int)dis.readByte();
  gps_nsat = (int)dis.readUnsignedByte()&0x00FF;
  gps_fix_type = (int)dis.readUnsignedByte()&0x00FF;
  battery_remaining = (int)dis.readUnsignedByte()&0x00FF;
  temperature = (int)dis.readByte();
  temperature_air = (int)dis.readByte();
  failsafe = (int)dis.readUnsignedByte()&0x00FF;
  wp_num = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+54];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFD);
  dos.writeByte(length & 0x00FF);
  dos.writeByte(incompat & 0x00FF);
  dos.writeByte(compat & 0x00FF);
  dos.writeByte(sequence & 0x00FF);
  dos.writeByte(sysId & 0x00FF);
  dos.writeByte(componentId & 0x00FF);
  dos.writeByte(messageType & 0x00FF);
  dos.writeByte((messageType >> 8) & 0x00FF);
  dos.writeByte((messageType >> 16) & 0x00FF);
  dos.writeLong(time_usec);
  dos.writeInt((int)(custom_mode&0x00FFFFFFFF));
  dos.writeInt((int)(latitude&0x00FFFFFFFF));
  dos.writeInt((int)(longitude&0x00FFFFFFFF));
  dos.writeShort(roll&0x00FFFF);
  dos.writeShort(pitch&0x00FFFF);
  dos.writeShort(heading&0x00FFFF);
  dos.writeShort(roll_sp&0x00FFFF);
  dos.writeShort(pitch_sp&0x00FFFF);
  dos.writeShort(heading_sp&0x00FFFF);
  dos.writeShort(altitude_home&0x00FFFF);
  dos.writeShort(altitude_amsl&0x00FFFF);
  dos.writeShort(altitude_sp&0x00FFFF);
  dos.writeShort(wp_distance&0x00FFFF);
  dos.writeByte(base_mode&0x00FF);
  dos.writeByte(landed_state&0x00FF);
  dos.write(throttle&0x00FF);
  dos.writeByte(airspeed&0x00FF);
  dos.writeByte(airspeed_sp&0x00FF);
  dos.writeByte(groundspeed&0x00FF);
  dos.write(climb_rate&0x00FF);
  dos.writeByte(gps_nsat&0x00FF);
  dos.writeByte(gps_fix_type&0x00FF);
  dos.writeByte(battery_remaining&0x00FF);
  dos.write(temperature&0x00FF);
  dos.write(temperature_air&0x00FF);
  dos.writeByte(failsafe&0x00FF);
  dos.writeByte(wp_num&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 54);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[64] = crcl;
  buffer[65] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_HIGH_LATENCY : " +   "  time_usec="+time_usec+  "  custom_mode="+custom_mode+  "  latitude="+latitude+  "  longitude="+longitude+  "  roll="+roll+  "  pitch="+pitch+  "  heading="+heading+  "  roll_sp="+roll_sp+  "  pitch_sp="+pitch_sp+  "  heading_sp="+heading_sp+  "  altitude_home="+altitude_home+  "  altitude_amsl="+altitude_amsl+  "  altitude_sp="+altitude_sp+  "  wp_distance="+wp_distance+  "  base_mode="+base_mode+  "  landed_state="+landed_state+  "  throttle="+throttle+  "  airspeed="+airspeed+  "  airspeed_sp="+airspeed_sp+  "  groundspeed="+groundspeed+  "  climb_rate="+climb_rate+  "  gps_nsat="+gps_nsat+  "  gps_fix_type="+gps_fix_type+  "  battery_remaining="+battery_remaining+  "  temperature="+temperature+  "  temperature_air="+temperature_air+  "  failsafe="+failsafe+  "  wp_num="+wp_num;}
}
