/**
 * Generated class : msg_set_mode
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
 * Class msg_set_mode
 * THIS INTERFACE IS DEPRECATED. USE COMMAND_LONG with MAV_CMD_DO_SET_MODE INSTEAD. Set the system mode, as defined by enum MAV_MODE. There is no target component id as the mode is by definition for the overall aircraft, not only for one component.
 **/
public class msg_set_mode extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_SET_MODE = 11;
  private static final long serialVersionUID = MAVLINK_MSG_ID_SET_MODE;
  public msg_set_mode() {
    this(1,1);
}
  public msg_set_mode(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_SET_MODE;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 6;
}

  /**
   * The new autopilot-specific mode. This field can be ignored by an autopilot.
   */
  public long custom_mode;
  /**
   * The system setting the mode
   */
  public int target_system;
  /**
   * The new base mode
   */
  public int base_mode;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  custom_mode = (int)dis.readInt()&0x00FFFFFFFF;
  target_system = (int)dis.readUnsignedByte()&0x00FF;
  base_mode = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+6];
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
  dos.writeByte(target_system&0x00FF);
  dos.writeByte(base_mode&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 6);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[16] = crcl;
  buffer[17] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_SET_MODE : " +   "  custom_mode="+custom_mode+  "  target_system="+target_system+  "  base_mode="+base_mode;}
}
