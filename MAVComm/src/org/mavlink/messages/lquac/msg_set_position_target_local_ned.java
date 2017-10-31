/**
 * Generated class : msg_set_position_target_local_ned
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
 * Class msg_set_position_target_local_ned
 * Sets a desired vehicle position in a local north-east-down coordinate frame. Used by an external controller to command the vehicle (manual controller or other system).
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
    payload_length = 53;
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
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_boot_ms = (int)dis.readInt()&0x00FFFFFFFF;
  x = (float)dis.readFloat();
  y = (float)dis.readFloat();
  z = (float)dis.readFloat();
  vx = (float)dis.readFloat();
  vy = (float)dis.readFloat();
  vz = (float)dis.readFloat();
  afx = (float)dis.readFloat();
  afy = (float)dis.readFloat();
  afz = (float)dis.readFloat();
  yaw = (float)dis.readFloat();
  yaw_rate = (float)dis.readFloat();
  type_mask = (int)dis.readUnsignedShort()&0x00FFFF;
  target_system = (int)dis.readUnsignedByte()&0x00FF;
  target_component = (int)dis.readUnsignedByte()&0x00FF;
  coordinate_frame = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+53];
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
  dos.writeInt((int)(time_boot_ms&0x00FFFFFFFF));
  dos.writeFloat(x);
  dos.writeFloat(y);
  dos.writeFloat(z);
  dos.writeFloat(vx);
  dos.writeFloat(vy);
  dos.writeFloat(vz);
  dos.writeFloat(afx);
  dos.writeFloat(afy);
  dos.writeFloat(afz);
  dos.writeFloat(yaw);
  dos.writeFloat(yaw_rate);
  dos.writeShort(type_mask&0x00FFFF);
  dos.writeByte(target_system&0x00FF);
  dos.writeByte(target_component&0x00FF);
  dos.writeByte(coordinate_frame&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 53);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[63] = crcl;
  buffer[64] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_SET_POSITION_TARGET_LOCAL_NED : " +   "  time_boot_ms="+time_boot_ms+  "  x="+x+  "  y="+y+  "  z="+z+  "  vx="+vx+  "  vy="+vy+  "  vz="+vz+  "  afx="+afx+  "  afy="+afy+  "  afz="+afz+  "  yaw="+yaw+  "  yaw_rate="+yaw_rate+  "  type_mask="+type_mask+  "  target_system="+target_system+  "  target_component="+target_component+  "  coordinate_frame="+coordinate_frame;}
}
