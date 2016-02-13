/**
 * Generated class : msg_command_int
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
 * Class msg_command_int
 * Message encoding a command with parameters as scaled integers. Scaling depends on the actual command value.
 **/
public class msg_command_int extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_COMMAND_INT = 75;
  private static final long serialVersionUID = MAVLINK_MSG_ID_COMMAND_INT;
  public msg_command_int() {
    this(1,1);
}
  public msg_command_int(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_COMMAND_INT;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 35;
}

  /**
   * PARAM1, see MAV_CMD enum
   */
  public float param1;
  /**
   * PARAM2, see MAV_CMD enum
   */
  public float param2;
  /**
   * PARAM3, see MAV_CMD enum
   */
  public float param3;
  /**
   * PARAM4, see MAV_CMD enum
   */
  public float param4;
  /**
   * PARAM5 / local: x position in meters * 1e4, global: latitude in degrees * 10^7
   */
  public long x;
  /**
   * PARAM6 / local: y position in meters * 1e4, global: longitude in degrees * 10^7
   */
  public long y;
  /**
   * PARAM7 / z position: global: altitude in meters (relative or absolute, depending on frame.
   */
  public float z;
  /**
   * The scheduled action for the mission item. see MAV_CMD in common.xml MAVLink specs
   */
  public int command;
  /**
   * System ID
   */
  public int target_system;
  /**
   * Component ID
   */
  public int target_component;
  /**
   * The coordinate system of the COMMAND. see MAV_FRAME in mavlink_types.h
   */
  public int frame;
  /**
   * false:0, true:1
   */
  public int current;
  /**
   * autocontinue to next wp
   */
  public int autocontinue;
/**
 * Decode message with raw data
 */
public void decode(ByteBuffer dis) throws IOException {
  param1 = (float)dis.getFloat();
  param2 = (float)dis.getFloat();
  param3 = (float)dis.getFloat();
  param4 = (float)dis.getFloat();
  x = (int)dis.getInt();
  y = (int)dis.getInt();
  z = (float)dis.getFloat();
  command = (int)dis.getShort()&0x00FFFF;
  target_system = (int)dis.get()&0x00FF;
  target_component = (int)dis.get()&0x00FF;
  frame = (int)dis.get()&0x00FF;
  current = (int)dis.get()&0x00FF;
  autocontinue = (int)dis.get()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+35];
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
  dos.putInt((int)(x&0x00FFFFFFFF));
  dos.putInt((int)(y&0x00FFFFFFFF));
  dos.putFloat(z);
  dos.putShort((short)(command&0x00FFFF));
  dos.put((byte)(target_system&0x00FF));
  dos.put((byte)(target_component&0x00FF));
  dos.put((byte)(frame&0x00FF));
  dos.put((byte)(current&0x00FF));
  dos.put((byte)(autocontinue&0x00FF));
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 35);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[41] = crcl;
  buffer[42] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_COMMAND_INT : " +   "  param1="+param1+  "  param2="+param2+  "  param3="+param3+  "  param4="+param4+  "  x="+x+  "  y="+y+  "  z="+z+  "  command="+command+  "  target_system="+target_system+  "  target_component="+target_component+  "  frame="+frame+  "  current="+current+  "  autocontinue="+autocontinue;}
}
