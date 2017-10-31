/**
 * Generated class : msg_msp_micro_grid
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
 * Class msg_msp_micro_grid
 * MSP MICRO GRID Data encoded in longs
 **/
public class msg_msp_micro_grid extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_MSP_MICRO_GRID = 183;
  private static final long serialVersionUID = MAVLINK_MSG_ID_MSP_MICRO_GRID;
  public msg_msp_micro_grid() {
    this(1,1);
}
  public msg_msp_micro_grid(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_MSP_MICRO_GRID;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 188;
}

  /**
   * Timestamp
   */
  public long tms;
  /**
   * SLAM data integers
   */
  public long[] data = new long[40];
  /**
   * Center x
   */
  public float cx;
  /**
   * Center y
   */
  public float cy;
  /**
   * Resolution in m
   */
  public float resolution;
  /**
   * Extension in m per direction
   */
  public float extension;
  /**
   * BlockCount
   */
  public long count;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  tms = (long)dis.readLong();
  for (int i=0; i<40; i++) {
    data[i] = (int)dis.readInt();
  }
  cx = (float)dis.readFloat();
  cy = (float)dis.readFloat();
  resolution = (float)dis.readFloat();
  extension = (float)dis.readFloat();
  count = (int)dis.readInt()&0x00FFFFFFFF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+188];
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
  dos.writeLong(tms);
  for (int i=0; i<40; i++) {
    dos.writeInt((int)(data[i]&0x00FFFFFFFF));
  }
  dos.writeFloat(cx);
  dos.writeFloat(cy);
  dos.writeFloat(resolution);
  dos.writeFloat(extension);
  dos.writeInt((int)(count&0x00FFFFFFFF));
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 188);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[198] = crcl;
  buffer[199] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_MSP_MICRO_GRID : " +   "  tms="+tms+  "  data="+data+  "  cx="+cx+  "  cy="+cy+  "  resolution="+resolution+  "  extension="+extension+  "  count="+count;}
}
