/**
 * Generated class : msg_msp_command
 * DO NOT MODIFY!
 **/
package org.mavlink.messages.lquac;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.mavlink.IMAVLinkCRC;
import org.mavlink.MAVLinkCRC;
import org.mavlink.messages.MAVLinkMessage;
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
    length = 29;
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
   * Parameter 7, as defined by MSP_COMMANDS enum.
   */
  public float param7;
  /**
   * Command to be executed by MSP (defined in MSP_COMMANDS)
   */
  public int command;
/**
 * Decode message with raw data
 */
public void decode(ByteBuffer dis) throws IOException {
  param1 = (float)dis.getFloat();
  param2 = (float)dis.getFloat();
  param3 = (float)dis.getFloat();
  param4 = (float)dis.getFloat();
  param5 = (float)dis.getFloat();
  param6 = (float)dis.getFloat();
  param7 = (float)dis.getFloat();
  command = (int)dis.get()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+29];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.putFloat(param1);
  dos.putFloat(param2);
  dos.putFloat(param3);
  dos.putFloat(param4);
  dos.putFloat(param5);
  dos.putFloat(param6);
  dos.putFloat(param7);
  dos.put((byte)(command&0x00FF));
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 29);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[35] = crcl;
  buffer[36] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_MSP_COMMAND : " +   "  param1="+param1+  "  param2="+param2+  "  param3="+param3+  "  param4="+param4+  "  param5="+param5+  "  param6="+param6+  "  param7="+param7+  "  command="+command;}
}
