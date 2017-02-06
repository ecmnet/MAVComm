/**
 * Generated class : msg_param_map_rc
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
 * Class msg_param_map_rc
 * Bind a RC channel to a parameter. The parameter should change accoding to the RC channel value.
 **/
public class msg_param_map_rc extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_PARAM_MAP_RC = 50;
  private static final long serialVersionUID = MAVLINK_MSG_ID_PARAM_MAP_RC;
  public msg_param_map_rc() {
    this(1,1);
}
  public msg_param_map_rc(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_PARAM_MAP_RC;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 37;
}

  /**
   * Initial parameter value
   */
  public float param_value0;
  /**
   * Scale, maps the RC range [-1, 1] to a parameter value
   */
  public float scale;
  /**
   * Minimum param value. The protocol does not define if this overwrites an onboard minimum value. (Depends on implementation)
   */
  public float param_value_min;
  /**
   * Maximum param value. The protocol does not define if this overwrites an onboard maximum value. (Depends on implementation)
   */
  public float param_value_max;
  /**
   * Parameter index. Send -1 to use the param ID field as identifier (else the param id will be ignored), send -2 to disable any existing map for this rc_channel_index.
   */
  public int param_index;
  /**
   * System ID
   */
  public int target_system;
  /**
   * Component ID
   */
  public int target_component;
  /**
   * Onboard parameter id, terminated by NULL if the length is less than 16 human-readable chars and WITHOUT null termination (NULL) byte if the length is exactly 16 chars - applications have to provide 16+1 bytes storage if the ID is stored as string
   */
  public char[] param_id = new char[16];
  public void setParam_id(String tmp) {
    int len = Math.min(tmp.length(), 16);
    for (int i=0; i<len; i++) {
      param_id[i] = tmp.charAt(i);
    }
    for (int i=len; i<16; i++) {
      param_id[i] = 0;
    }
  }
  public String getParam_id() {
    String result="";
    for (int i=0; i<16; i++) {
      if (param_id[i] != 0) result=result+param_id[i]; else break;
    }
    return result;
  }
  /**
   * Index of parameter RC channel. Not equal to the RC channel id. Typically correpsonds to a potentiometer-knob on the RC.
   */
  public int parameter_rc_channel_index;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  param_value0 = (float)dis.readFloat();
  scale = (float)dis.readFloat();
  param_value_min = (float)dis.readFloat();
  param_value_max = (float)dis.readFloat();
  param_index = (int)dis.readShort();
  target_system = (int)dis.readUnsignedByte()&0x00FF;
  target_component = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<16; i++) {
    param_id[i] = (char)dis.readByte();
  }
  parameter_rc_channel_index = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+37];
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
  dos.writeFloat(param_value0);
  dos.writeFloat(scale);
  dos.writeFloat(param_value_min);
  dos.writeFloat(param_value_max);
  dos.writeShort(param_index&0x00FFFF);
  dos.writeByte(target_system&0x00FF);
  dos.writeByte(target_component&0x00FF);
  for (int i=0; i<16; i++) {
    dos.writeByte(param_id[i]);
  }
  dos.writeByte(parameter_rc_channel_index&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 37);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[47] = crcl;
  buffer[48] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_PARAM_MAP_RC : " +   "  param_value0="+param_value0+  "  scale="+scale+  "  param_value_min="+param_value_min+  "  param_value_max="+param_value_max+  "  param_index="+param_index+  "  target_system="+target_system+  "  target_component="+target_component+  "  param_id="+getParam_id()+  "  parameter_rc_channel_index="+parameter_rc_channel_index;}
}
