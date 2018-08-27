/**
 * Generated class : msg_timesync
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
 * Class msg_timesync
 * Time synchronization message.
 **/
public class msg_timesync extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_TIMESYNC = 111;
  private static final long serialVersionUID = MAVLINK_MSG_ID_TIMESYNC;
  public msg_timesync() {
    this(1,1);
}
  public msg_timesync(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_TIMESYNC;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 16;
}

  /**
   * Time sync timestamp 1
   */
  public long tc1;
  /**
   * Time sync timestamp 2
   */
  public long ts1;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  tc1 = (long)dis.readLong();
  ts1 = (long)dis.readLong();
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+16];
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
  dos.writeLong(tc1);
  dos.writeLong(ts1);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 16);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[26] = crcl;
  buffer[27] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_TIMESYNC : " +   "  tc1="+tc1+  "  ts1="+ts1;}
}
