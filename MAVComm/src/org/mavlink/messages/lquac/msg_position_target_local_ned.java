/**
 * Generated class : msg_position_target_local_ned
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
 * Class msg_position_target_local_ned
 * Reports the current commanded vehicle position, velocity, and acceleration as specified by the autopilot. This should match the commands sent in SET_POSITION_TARGET_LOCAL_NED if the vehicle is being controlled this way.
 **/
public class msg_position_target_local_ned extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_POSITION_TARGET_LOCAL_NED = 85;
  private static final long serialVersionUID = MAVLINK_MSG_ID_POSITION_TARGET_LOCAL_NED;
  public msg_position_target_local_ned() {
    this(1,1);
}
  public msg_position_target_local_ned(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_POSITION_TARGET_LOCAL_NED;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 51;
}

  /**
   * Timestamp (time since system boot).
   */
  public long time_boot_ms;
  /**
   * X Position in NED frame
   */
  public float x;
  /**
   * Y Position in NED frame
   */
  public float y;
  /**
   * Z Position in NED frame (note, altitude is negative in NED)
   */
  public float z;
  /**
   * X velocity in NED frame
   */
  public float vx;
  /**
   * Y velocity in NED frame
   */
  public float vy;
  /**
   * Z velocity in NED frame
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
   * yaw setpoint
   */
  public float yaw;
  /**
   * yaw rate setpoint
   */
  public float yaw_rate;
  /**
   * Bitmap to indicate which dimensions should be ignored by the vehicle.
   */
  public int type_mask;
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
  coordinate_frame = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+51];
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
  dos.writeByte(coordinate_frame&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 51);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[61] = crcl;
  buffer[62] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_POSITION_TARGET_LOCAL_NED : " +   "  time_boot_ms="+time_boot_ms
+  "  x="+x
+  "  y="+y
+  "  z="+z
+  "  vx="+vx
+  "  vy="+vy
+  "  vz="+vz
+  "  afx="+afx
+  "  afy="+afy
+  "  afz="+afz
+  "  yaw="+yaw
+  "  yaw_rate="+yaw_rate
+  "  type_mask="+type_mask
+  "  coordinate_frame="+coordinate_frame
;}
}
