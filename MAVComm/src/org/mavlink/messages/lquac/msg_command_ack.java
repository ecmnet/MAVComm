/**
 * Generated class : msg_command_ack
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
 * Class msg_command_ack
 * Report status of a command. Includes feedback whether the command was executed.
 **/
public class msg_command_ack extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_COMMAND_ACK = 77;
  private static final long serialVersionUID = MAVLINK_MSG_ID_COMMAND_ACK;
  public msg_command_ack() {
    this(1,1);
}
  public msg_command_ack(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_COMMAND_ACK;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 10;
}

  /**
   * Command ID (of acknowledged command).
   */
  public int command;
  /**
   * Result of command.
   */
  public int result;
  /**
   * WIP: Additional parameter of the result, example: which parameter of MAV_CMD_NAV_WAYPOINT caused it to be denied.
   */
  public long result_param2;
  /**
   * WIP: Also used as result_param1, it can be set with a enum containing the errors reasons of why the command was denied or the progress percentage or 255 if unknown the progress when result is MAV_RESULT_IN_PROGRESS.
   */
  public int progress;
  /**
   * WIP: System which requested the command to be executed
   */
  public int target_system;
  /**
   * WIP: Component which requested the command to be executed
   */
  public int target_component;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  command = (int)dis.readUnsignedShort()&0x00FFFF;
  result = (int)dis.readUnsignedByte()&0x00FF;
  result_param2 = (int)dis.readInt();
  progress = (int)dis.readUnsignedByte()&0x00FF;
  target_system = (int)dis.readUnsignedByte()&0x00FF;
  target_component = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+10];
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
  dos.writeShort(command&0x00FFFF);
  dos.writeByte(result&0x00FF);
  dos.writeInt((int)(result_param2&0x00FFFFFFFF));
  dos.writeByte(progress&0x00FF);
  dos.writeByte(target_system&0x00FF);
  dos.writeByte(target_component&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 10);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[20] = crcl;
  buffer[21] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_COMMAND_ACK : " +   "  command="+command+  "  result="+result+  "  result_param2="+result_param2+  "  progress="+progress+  "  target_system="+target_system+  "  target_component="+target_component;}
}
