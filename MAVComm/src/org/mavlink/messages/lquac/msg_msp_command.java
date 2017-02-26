/**
 * Generated class : msg_msp_command
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
 * Class msg_msp_command
 * MSP Commands.
 **/
public class msg_msp_command extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_MSP_COMMAND = 181;
  private static final long serialVersionUID = MAVLINK_MSG_ID_MSP_COMMAND;
  public msg_msp_command() {
    this(1,1);
}
  public msg_msp_command(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_MSP_COMMAND;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 25;
}

  /**
   * Parameter 1, as defined by MSP_COMMANDS enum.
   */
  public float param1;
  /**
   * Parameter 2, as defined by MSP_COMMANDS enum.
   */
  public float param2;
  /**
   * Parameter 3, as defined by MSP_COMMANDS enum.
   */
  public float param3;
  /**
   * Parameter 4, as defined by MSP_COMMANDS enum.
   */
  public float param4;
  /**
   * Parameter 5, as defined by MSP_COMMANDS enum.
   */
  public float param5;
  /**
   * Parameter 6, as defined by MSP_COMMANDS enum.
   */
  public float param6;
  /**
   * Command to be executed by MSP (defined in MSP_COMMANDS)
   */
  public int command;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  param1 = (float)dis.readFloat();
  param2 = (float)dis.readFloat();
  param3 = (float)dis.readFloat();
  param4 = (float)dis.readFloat();
  param5 = (float)dis.readFloat();
  param6 = (float)dis.readFloat();
  command = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+25];
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
  dos.writeFloat(param1);
  dos.writeFloat(param2);
  dos.writeFloat(param3);
  dos.writeFloat(param4);
  dos.writeFloat(param5);
  dos.writeFloat(param6);
  dos.writeByte(command&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 25);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[35] = crcl;
  buffer[36] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_MSP_COMMAND : " +   "  param1="+param1+  "  param2="+param2+  "  param3="+param3+  "  param4="+param4+  "  param5="+param5+  "  param6="+param6+  "  command="+command;}
}
