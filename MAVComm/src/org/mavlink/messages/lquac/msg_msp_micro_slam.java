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
 * MSP MICRO SLAM Data (12x12x12), encoded in longs
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
    length = 244;
}

  /**
   * SLAM data bytes
   */
  public long[] data = new long[27];
  /**
   * Timestamp
   */
  public long tms;
  /**
   * Center x
   */
  public float cx;
  /**
   * Center y
   */
  public float cy;
  /**
   * Center z
   */
  public float cz;
  /**
   * Center x
   */
  public float res;
  /**
   * Info flags
   */
  public long flags;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  for (int i=0; i<27; i++) {
    data[i] = (long)dis.readLong();
  }
  tms = (long)dis.readLong();
  cx = (float)dis.readFloat();
  cy = (float)dis.readFloat();
  cz = (float)dis.readFloat();
  res = (float)dis.readFloat();
  flags = (int)dis.readInt()&0x00FFFFFFFF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+244];
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
  for (int i=0; i<27; i++) {
    dos.writeLong(data[i]);
  }
  dos.writeLong(tms);
  dos.writeFloat(cx);
  dos.writeFloat(cy);
  dos.writeFloat(cz);
  dos.writeFloat(res);
  dos.writeInt((int)(flags&0x00FFFFFFFF));
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 244);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[254] = crcl;
  buffer[255] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_MSP_MICRO_SLAM : " +   "  data="+data+  "  tms="+tms+  "  cx="+cx+  "  cy="+cy+  "  cz="+cz+  "  res="+res+  "  flags="+flags;}
}
