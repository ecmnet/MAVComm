/**
 * Generated class : msg_nav_controller_output
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
 * Class msg_nav_controller_output
 * The state of the fixed wing navigation and position controller.
 **/
public class msg_nav_controller_output extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_NAV_CONTROLLER_OUTPUT = 62;
  private static final long serialVersionUID = MAVLINK_MSG_ID_NAV_CONTROLLER_OUTPUT;
  public msg_nav_controller_output() {
    this(1,1);
}
  public msg_nav_controller_output(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_NAV_CONTROLLER_OUTPUT;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 26;
}

  /**
   * Current desired roll in degrees
   */
  public float nav_roll;
  /**
   * Current desired pitch in degrees
   */
  public float nav_pitch;
  /**
   * Current altitude error in meters
   */
  public float alt_error;
  /**
   * Current airspeed error in meters/second
   */
  public float aspd_error;
  /**
   * Current crosstrack error on x-y plane in meters
   */
  public float xtrack_error;
  /**
   * Current desired heading in degrees
   */
  public int nav_bearing;
  /**
   * Bearing to current MISSION/target in degrees
   */
  public int target_bearing;
  /**
   * Distance to active MISSION in meters
   */
  public int wp_dist;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  nav_roll = (float)dis.readFloat();
  nav_pitch = (float)dis.readFloat();
  alt_error = (float)dis.readFloat();
  aspd_error = (float)dis.readFloat();
  xtrack_error = (float)dis.readFloat();
  nav_bearing = (int)dis.readShort();
  target_bearing = (int)dis.readShort();
  wp_dist = (int)dis.readUnsignedShort()&0x00FFFF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+26];
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
  dos.writeFloat(nav_roll);
  dos.writeFloat(nav_pitch);
  dos.writeFloat(alt_error);
  dos.writeFloat(aspd_error);
  dos.writeFloat(xtrack_error);
  dos.writeShort(nav_bearing&0x00FFFF);
  dos.writeShort(target_bearing&0x00FFFF);
  dos.writeShort(wp_dist&0x00FFFF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 26);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[36] = crcl;
  buffer[37] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_NAV_CONTROLLER_OUTPUT : " +   "  nav_roll="+nav_roll+  "  nav_pitch="+nav_pitch+  "  alt_error="+alt_error+  "  aspd_error="+aspd_error+  "  xtrack_error="+xtrack_error+  "  nav_bearing="+nav_bearing+  "  target_bearing="+target_bearing+  "  wp_dist="+wp_dist;}
}
