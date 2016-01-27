/**
 * Generated class : msg_set_position_target_local_ned
 * DO NOT MODIFY!
 **/
package org.mavlink.messages.lquac;
import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.IMAVLinkCRC;
import org.mavlink.MAVLinkCRC;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/**
 * Class msg_set_position_target_local_ned
 * Set vehicle position, velocity and acceleration setpoint in local frame.
 **/
public class msg_set_position_target_local_ned extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_SET_POSITION_TARGET_LOCAL_NED = 84;
  private static final long serialVersionUID = MAVLINK_MSG_ID_SET_POSITION_TARGET_LOCAL_NED;
  public msg_set_position_target_local_ned() {
    this(1,1);
}
  public msg_set_position_target_local_ned(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_SET_POSITION_TARGET_LOCAL_NED;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 53;
}

  /**
   * Timestamp in milliseconds since system boot
   */
  public long time_boot_ms;
  /**
   * X Position in NED frame in meters
   */
  public float x;
  /**
   * Y Position in NED frame in meters
   */
  public float y;
  /**
   * Z Position in NED frame in meters (note, altitude is negative in NED)
   */
  public float z;
  /**
   * X velocity in NED frame in meter / s
   */
  public float vx;
  /**
   * Y velocity in NED frame in meter / s
   */
  public float vy;
  /**
   * Z velocity in NED frame in meter / s
   */
  public float vz;
  /**
   * X acceleration or force (if bit 10 of type_mask is set) in NED frame in meter / s^2 or N
   */
  public float afx;
  /**
   * Y acceleration or force (if bit 10 of type_mask is set) in NED frame in meter / s^2 or N
   */
  public float afy;
  /**
   * Z acceleration or force (if bit 10 of type_mask is set) in NED frame in meter / s^2 or N
   */
  public float afz;
  /**
   * yaw setpoint in rad
   */
  public float yaw;
  /**
   * yaw rate setpoint in rad/s
   */
  public float yaw_rate;
  /**
   * Bitmask to indicate which dimensions should be ignored by the vehicle: a value of 0b0000000000000000 or 0b0000001000000000 indicates that none of the setpoint dimensions should be ignored. If bit 10 is set the floats afx afy afz should be interpreted as force instead of acceleration. Mapping: bit 1: x, bit 2: y, bit 3: z, bit 4: vx, bit 5: vy, bit 6: vz, bit 7: ax, bit 8: ay, bit 9: az, bit 10: is force setpoint, bit 11: yaw, bit 12: yaw rate
   */
  public int type_mask;
  /**
   * System ID
   */
  public int target_system;
  /**
   * Component ID
   */
  public int target_component;
  /**
   * Valid options are: MAV_FRAME_LOCAL_NED = 1, MAV_FRAME_LOCAL_OFFSET_NED = 7, MAV_FRAME_BODY_NED = 8, MAV_FRAME_BODY_OFFSET_NED = 9
   */
  public int coordinate_frame;
/**
 * Decode message with raw data
 */
public void decode(ByteBuffer dis) throws IOException {
  time_boot_ms = (int)dis.getInt()&0x00FFFFFFFF;
  x = (float)dis.getFloat();
  y = (float)dis.getFloat();
  z = (float)dis.getFloat();
  vx = (float)dis.getFloat();
  vy = (float)dis.getFloat();
  vz = (float)dis.getFloat();
  afx = (float)dis.getFloat();
  afy = (float)dis.getFloat();
  afz = (float)dis.getFloat();
  yaw = (float)dis.getFloat();
  yaw_rate = (float)dis.getFloat();
  type_mask = (int)dis.getShort()&0x00FFFF;
  target_system = (int)dis.get()&0x00FF;
  target_component = (int)dis.get()&0x00FF;
  coordinate_frame = (int)dis.get()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+53];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.putInt((int)(time_boot_ms&0x00FFFFFFFF));
  dos.putFloat(x);
  dos.putFloat(y);
  dos.putFloat(z);
  dos.putFloat(vx);
  dos.putFloat(vy);
  dos.putFloat(vz);
  dos.putFloat(afx);
  dos.putFloat(afy);
  dos.putFloat(afz);
  dos.putFloat(yaw);
  dos.putFloat(yaw_rate);
  dos.putShort((short)(type_mask&0x00FFFF));
  dos.put((byte)(target_system&0x00FF));
  dos.put((byte)(target_component&0x00FF));
  dos.put((byte)(coordinate_frame&0x00FF));
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 53);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[59] = crcl;
  buffer[60] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_SET_POSITION_TARGET_LOCAL_NED : " +   "  time_boot_ms="+time_boot_ms+  "  x="+x+  "  y="+y+  "  z="+z+  "  vx="+vx+  "  vy="+vy+  "  vz="+vz+  "  afx="+afx+  "  afy="+afy+  "  afz="+afz+  "  yaw="+yaw+  "  yaw_rate="+yaw_rate+  "  type_mask="+type_mask+  "  target_system="+target_system+  "  target_component="+target_component+  "  coordinate_frame="+coordinate_frame;}
}
