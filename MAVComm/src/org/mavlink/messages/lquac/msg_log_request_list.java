/**
 * Generated class : msg_log_request_list
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
 * Class msg_log_request_list
 * Request a list of available logs. On some systems calling this may stop on-board logging until LOG_REQUEST_END is called.
 **/
public class msg_log_request_list extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_LOG_REQUEST_LIST = 117;
  private static final long serialVersionUID = MAVLINK_MSG_ID_LOG_REQUEST_LIST;
  public msg_log_request_list() {
    this(1,1);
}
  public msg_log_request_list(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_LOG_REQUEST_LIST;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 6;
}

  /**
   * First log id (0 for first available)
   */
  public int start;
  /**
   * Last log id (0xffff for last available)
   */
  public int end;
  /**
   * System ID
   */
  public int target_system;
  /**
   * Component ID
   */
  public int target_component;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  start = (int)dis.readUnsignedShort()&0x00FFFF;
  end = (int)dis.readUnsignedShort()&0x00FFFF;
  target_system = (int)dis.readUnsignedByte()&0x00FF;
  target_component = (int)dis.readUnsignedByte()&0x00FF;
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
  dos.writeShort(start&0x00FFFF);
  dos.writeShort(end&0x00FFFF);
  dos.writeByte(target_system&0x00FF);
  dos.writeByte(target_component&0x00FF);
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
return "MAVLINK_MSG_ID_LOG_REQUEST_LIST : " +   "  start="+start
+  "  end="+end
+  "  target_system="+target_system
+  "  target_component="+target_component
;}
}
