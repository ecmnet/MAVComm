/**
 * Generated class : msg_log_data
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
 * Class msg_log_data
 * Reply to LOG_REQUEST_DATA
 **/
public class msg_log_data extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_LOG_DATA = 120;
  private static final long serialVersionUID = MAVLINK_MSG_ID_LOG_DATA;
  public msg_log_data() {
    this(1,1);
}
  public msg_log_data(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_LOG_DATA;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 97;
}

  /**
   * Offset into the log
   */
  public long ofs;
  /**
   * Log id (from LOG_ENTRY reply)
   */
  public int id;
  /**
   * Number of bytes (zero for end of log)
   */
  public int count;
  /**
   * log data
   */
  public int[] data = new int[90];
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  ofs = (int)dis.readInt()&0x00FFFFFFFF;
  id = (int)dis.readUnsignedShort()&0x00FFFF;
  count = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<90; i++) {
    data[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+97];
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
  dos.writeInt((int)(ofs&0x00FFFFFFFF));
  dos.writeShort(id&0x00FFFF);
  dos.writeByte(count&0x00FF);
  for (int i=0; i<90; i++) {
    dos.writeByte(data[i]&0x00FF);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 97);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[107] = crcl;
  buffer[108] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_LOG_DATA : " +   "  ofs="+ofs+  "  id="+id+  "  count="+count+  "  data="+data;}
}
