/**
 * Generated class : msg_msp_micro_slam
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
 * Class msg_msp_micro_slam
 * MSP MICRO SLAM Data (12x12x12 each 20cm), encoded in bytes
 **/
public class msg_msp_micro_slam extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_MSP_MICRO_SLAM = 183;
  private static final long serialVersionUID = MAVLINK_MSG_ID_MSP_MICRO_SLAM;
  public msg_msp_micro_slam() {
    this(1,1);
}
  public msg_msp_micro_slam(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_MSP_MICRO_SLAM;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 228;
}

  /**
   * Timestamp
   */
  public long tms;
  /**
   * Info flags
   */
  public long flags;
  /**
   * SLAM data bytes
   */
  public int[] data = new int[216];
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  tms = (long)dis.readLong();
  flags = (int)dis.readInt()&0x00FFFFFFFF;
  for (int i=0; i<216; i++) {
    data[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+228];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFD);
  dos.writeByte(length & 0x00FF);
  dos.writeByte(incompat & 0x00FF);
  dos.writeByte(compat & 0x00FF);
  dos.writeByte(packet & 0x00FF);
  dos.writeByte(sysId & 0x00FF);
  dos.writeByte(componentId & 0x00FF);
  dos.writeByte(messageType & 0x00FF);
  dos.writeByte((messageType >> 8) & 0x00FF);
  dos.writeByte((messageType >> 16) & 0x00FF);
  dos.writeLong(tms);
  dos.writeInt((int)(flags&0x00FFFFFFFF));
  for (int i=0; i<216; i++) {
    dos.writeByte(data[i]&0x00FF);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 228);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[238] = crcl;
  buffer[239] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_MSP_MICRO_SLAM : " +   "  tms="+tms+  "  flags="+flags+  "  data="+data;}
}
