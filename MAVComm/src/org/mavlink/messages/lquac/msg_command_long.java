/**
 * Generated class : msg_command_long
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
 * Class msg_command_long
 * Send a command with up to seven parameters to the MAV. The command microservice is documented at https://mavlink.io/en/services/command.html
 **/
public class msg_command_long extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_COMMAND_LONG = 76;
  private static final long serialVersionUID = MAVLINK_MSG_ID_COMMAND_LONG;
  public msg_command_long() {
    this(1,1);
}
  public msg_command_long(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_COMMAND_LONG;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 33;
}

  /**
   * Parameter 1 (for the specific command).
   */
  public float param1;
  /**
   * Parameter 2 (for the specific command).
   */
  public float param2;
  /**
   * Parameter 3 (for the specific command).
   */
  public float param3;
  /**
   * Parameter 4 (for the specific command).
   */
  public float param4;
  /**
   * Parameter 5 (for the specific command).
   */
  public float param5;
  /**
   * Parameter 6 (for the specific command).
   */
  public float param6;
  /**
   * Parameter 7 (for the specific command).
   */
  public float param7;
  /**
   * Command ID (of command to send).
   */
  public int command;
  /**
   * System which should execute the command
   */
  public int target_system;
  /**
   * Component which should execute the command, 0 for all components
   */
  public int target_component;
  /**
   * 0: First transmission of this command. 1-255: Confirmation transmissions (e.g. for kill command)
   */
  public int confirmation;
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
  param7 = (float)dis.readFloat();
  command = (int)dis.readUnsignedShort()&0x00FFFF;
  target_system = (int)dis.readUnsignedByte()&0x00FF;
  target_component = (int)dis.readUnsignedByte()&0x00FF;
  confirmation = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+33];
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
  dos.writeFloat(param7);
  dos.writeShort(command&0x00FFFF);
  dos.writeByte(target_system&0x00FF);
  dos.writeByte(target_component&0x00FF);
  dos.writeByte(confirmation&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 33);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[43] = crcl;
  buffer[44] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_COMMAND_LONG : " +   "  param1="+param1
+  "  param2="+param2
+  "  param3="+param3
+  "  param4="+param4
+  "  param5="+param5
+  "  param6="+param6
+  "  param7="+param7
+  "  command="+command
+  "  target_system="+target_system
+  "  target_component="+target_component
+  "  confirmation="+confirmation
;}
}
