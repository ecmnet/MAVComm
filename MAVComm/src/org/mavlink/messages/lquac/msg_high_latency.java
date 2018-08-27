/**
 * Generated class : msg_high_latency
 * DO NOT MODIFY!
 **/
package org.mavlink.messages.lquac;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.mavlink.IMAVLinkCRC;
import org.mavlink.MAVLinkCRC;
import org.mavlink.io.LittleEndianDataInputStream;
import org.mavlink.io.LittleEndianDataOutputStream;
import org.mavlink.messages.MAVLinkMessage;
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
    payload_length = 40;
}

  /**
   * A bitfield for use for autopilot-specific flags.
   */
  public long custom_mode;
  /**
   * Latitude
   */
  public long latitude;
  /**
   * Longitude
   */
  public long longitude;
  /**
   * roll
   */
  public int roll;
  /**
   * pitch
   */
  public int pitch;
  /**
   * heading
   */
  public int heading;
  /**
   * heading setpoint
   */
  public int heading_sp;
  /**
   * Altitude above mean sea level
   */
  public int altitude_amsl;
  /**
   * Altitude setpoint relative to the home position
   */
  public int altitude_sp;
  /**
   * distance to target
   */
  public int wp_distance;
  /**
   * Bitmap of enabled system modes.
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
   * airspeed
   */
  public int airspeed;
  /**
   * airspeed setpoint
   */
  public int airspeed_sp;
  /**
   * groundspeed
   */
  public int groundspeed;
  /**
   * climb rate
   */
  public int climb_rate;
  /**
   * Number of satellites visible. If unknown, set to 255
   */
  public int gps_nsat;
  /**
   * GPS Fix type.
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
  custom_mode = (int)dis.readInt()&0x00FFFFFFFF;
  latitude = (int)dis.readInt();
  longitude = (int)dis.readInt();
  roll = (int)dis.readShort();
  pitch = (int)dis.readShort();
  heading = (int)dis.readUnsignedShort()&0x00FFFF;
  heading_sp = (int)dis.readShort();
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
  byte[] buffer = new byte[12+40];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFD);
  dos.writeByte(payload_length & 0x00FF);
  dos.writeByte(incompat & 0x00FF);
  dos.writeByte(compat & 0x00FF);
  dos.writeByte(packet & 0x00FF);
  dos.writeByte(sysId & 0x00FF);
  dos.writeByte(componentId & 0x00FF);
  dos.writeByte(messageType & 0x00FF);
  dos.writeByte((messageType >> 8) & 0x00FF);
  dos.writeByte((messageType >> 16) & 0x00FF);
  dos.writeInt((int)(custom_mode&0x00FFFFFFFF));
  dos.writeInt((int)(latitude&0x00FFFFFFFF));
  dos.writeInt((int)(longitude&0x00FFFFFFFF));
  dos.writeShort(roll&0x00FFFF);
  dos.writeShort(pitch&0x00FFFF);
  dos.writeShort(heading&0x00FFFF);
  dos.writeShort(heading_sp&0x00FFFF);
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
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 40);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[50] = crcl;
  buffer[51] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_HIGH_LATENCY : " +   "  custom_mode="+custom_mode+  "  latitude="+latitude+  "  longitude="+longitude+  "  roll="+roll+  "  pitch="+pitch+  "  heading="+heading+  "  heading_sp="+heading_sp+  "  altitude_amsl="+altitude_amsl+  "  altitude_sp="+altitude_sp+  "  wp_distance="+wp_distance+  "  base_mode="+base_mode+  "  landed_state="+landed_state+  "  throttle="+throttle+  "  airspeed="+airspeed+  "  airspeed_sp="+airspeed_sp+  "  groundspeed="+groundspeed+  "  climb_rate="+climb_rate+  "  gps_nsat="+gps_nsat+  "  gps_fix_type="+gps_fix_type+  "  battery_remaining="+battery_remaining+  "  temperature="+temperature+  "  temperature_air="+temperature_air+  "  failsafe="+failsafe+  "  wp_num="+wp_num;}
}
