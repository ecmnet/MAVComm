/**
 * Generated class : msg_command_ack
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
    payload_length = 4;
}

  /**
   * Command ID, as defined by MAV_CMD enum.
   */
  public int command;
  /**
   * See MAV_RESULT enum
   */
  public int result;
  /**
   * WIP: Needs to be set when MAV_RESULT is MAV_RESULT_IN_PROGRESS, values from 0 to 100 for progress percentage, 255 for unknown progress.
   */
  public int progress;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  command = (int)dis.readUnsignedShort()&0x00FFFF;
  result = (int)dis.readUnsignedByte()&0x00FF;
  progress = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+4];
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
  dos.writeByte(progress&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 4);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[14] = crcl;
  buffer[15] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_COMMAND_ACK : " +   "  command="+command+  "  result="+result+  "  progress="+progress;}
}
