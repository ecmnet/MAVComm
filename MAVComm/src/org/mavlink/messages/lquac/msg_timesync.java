/**
 * Generated class : msg_timesync
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
    length = 16;
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
public void decode(ByteBuffer dis) throws IOException {
  tc1 = (long)dis.getLong();
  ts1 = (long)dis.getLong();
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+16];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.putLong(tc1);
  dos.putLong(ts1);
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 16);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[22] = crcl;
  buffer[23] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_TIMESYNC : " +   "  tc1="+tc1+  "  ts1="+ts1;}
}
