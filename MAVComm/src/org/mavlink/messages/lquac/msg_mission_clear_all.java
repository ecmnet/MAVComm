/**
 * Generated class : msg_mission_clear_all
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
 * Class msg_mission_clear_all
 * Delete all mission items at once.
 **/
public class msg_mission_clear_all extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_MISSION_CLEAR_ALL = 45;
  private static final long serialVersionUID = MAVLINK_MSG_ID_MISSION_CLEAR_ALL;
  public msg_mission_clear_all() {
    this(1,1);
}
  public msg_mission_clear_all(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_MISSION_CLEAR_ALL;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 3;
}

  /**
   * System ID
   */
  public int target_system;
  /**
   * Component ID
   */
  public int target_component;
  /**
   * Mission type, see MAV_MISSION_TYPE
   */
  public int mission_type;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  target_system = (int)dis.readUnsignedByte()&0x00FF;
  target_component = (int)dis.readUnsignedByte()&0x00FF;
  mission_type = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+3];
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
  dos.writeByte(target_system&0x00FF);
  dos.writeByte(target_component&0x00FF);
  dos.writeByte(mission_type&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 3);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[13] = crcl;
  buffer[14] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_MISSION_CLEAR_ALL : " +   "  target_system="+target_system+  "  target_component="+target_component+  "  mission_type="+mission_type;}
}
