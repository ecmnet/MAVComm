/**
 * Generated class : msg_message_interval
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
 * Class msg_message_interval
 * This interface replaces DATA_STREAM
 **/
public class msg_message_interval extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_MESSAGE_INTERVAL = 244;
  private static final long serialVersionUID = MAVLINK_MSG_ID_MESSAGE_INTERVAL;
  public msg_message_interval() {
    this(1,1);
}
  public msg_message_interval(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_MESSAGE_INTERVAL;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 6;
}

  /**
   * The interval between two messages, in microseconds. A value of -1 indicates this stream is disabled, 0 indicates it is not available, > 0 indicates the interval at which it is sent.
   */
  public long interval_us;
  /**
   * The ID of the requested MAVLink message. v1.0 is limited to 254 messages.
   */
  public int message_id;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  interval_us = (int)dis.readInt();
  message_id = (int)dis.readUnsignedShort()&0x00FFFF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+6];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFD);
  dos.writeByte(length & 0x00FF);
  dos.writeByte(sequence & 0x00FF);
  dos.writeByte(sysId & 0x00FF);
  dos.writeByte(componentId & 0x00FF);
  dos.writeByte(messageType & 0x00FF);
  dos.writeInt((int)(interval_us&0x00FFFFFFFF));
  dos.writeShort(message_id&0x00FFFF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 6);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[12] = crcl;
  buffer[13] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_MESSAGE_INTERVAL : " +   "  interval_us="+interval_us+  "  message_id="+message_id;}
}
