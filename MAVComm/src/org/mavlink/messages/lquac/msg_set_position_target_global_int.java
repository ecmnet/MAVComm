/**
 * Generated class : msg_set_position_target_global_int
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
 * Class msg_set_position_target_global_int
 * Sets a desired vehicle position, velocity, and/or acceleration in a global coordinate system (WGS84). Used by an external controller to command the vehicle (manual controller or other system).
 **/
public class msg_set_position_target_global_int extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_SET_POSITION_TARGET_GLOBAL_INT = 86;
  private static final long serialVersionUID = MAVLINK_MSG_ID_SET_POSITION_TARGET_GLOBAL_INT;
  public msg_set_position_target_global_int() {
    this(1,1);
}
  public msg_set_position_target_global_int(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_SET_POSITION_TARGET_GLOBAL_INT;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 53;
}

  /**
   * Timestamp (time since system boot). The rationale for the timestamp in the setpoint is to allow the system to compensate for the transport delay of the setpoint. This allows the system to compensate processing latency.
   */
  public long time_boot_ms;
  /**
   * X Position in WGS84 frame
   */
  public long lat_int;
  /**
   * Y Position in WGS84 frame
   */
  public long lon_int;
  /**
   * Altitude (AMSL) if absolute or relative, above terrain if GLOBAL_TERRAIN_ALT_INT
   */
  public float alt;
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
   * System ID
   */
  public int target_system;
  /**
   * Component ID
   */
  public int target_component;
  /**
   * Valid options are: MAV_FRAME_GLOBAL_INT = 5, MAV_FRAME_GLOBAL_RELATIVE_ALT_INT = 6, MAV_FRAME_GLOBAL_TERRAIN_ALT_INT = 11
   */
  public int coordinate_frame;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_boot_ms = (int)dis.readInt()&0x00FFFFFFFF;
  lat_int = (int)dis.readInt();
  lon_int = (int)dis.readInt();
  alt = (float)dis.readFloat();
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
  dos.writeInt((int)(lat_int&0x00FFFFFFFF));
  dos.writeInt((int)(lon_int&0x00FFFFFFFF));
  dos.writeFloat(alt);
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
return "MAVLINK_MSG_ID_SET_POSITION_TARGET_GLOBAL_INT : " +   "  time_boot_ms="+time_boot_ms+  "  lat_int="+lat_int+  "  lon_int="+lon_int+  "  alt="+alt+  "  vx="+vx+  "  vy="+vy+  "  vz="+vz+  "  afx="+afx+  "  afy="+afy+  "  afz="+afz+  "  yaw="+yaw+  "  yaw_rate="+yaw_rate+  "  type_mask="+type_mask+  "  target_system="+target_system+  "  target_component="+target_component+  "  coordinate_frame="+coordinate_frame;}
}
