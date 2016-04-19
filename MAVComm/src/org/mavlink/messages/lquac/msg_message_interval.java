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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
public void decode(ByteBuffer dis) throws IOException {
  interval_us = (int)dis.getInt();
  message_id = (int)dis.getShort()&0x00FFFF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+6];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.putInt((int)(interval_us&0x00FFFFFFFF));
  dos.putShort((short)(message_id&0x00FFFF));
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 6);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[12] = crcl;
  buffer[13] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_MESSAGE_INTERVAL : " +   "  interval_us="+interval_us+  "  message_id="+message_id;}
}
