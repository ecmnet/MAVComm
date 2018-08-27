/**
 * Generated class : msg_data_transmission_handshake
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
 * Class msg_data_transmission_handshake
 * 
 **/
public class msg_data_transmission_handshake extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_DATA_TRANSMISSION_HANDSHAKE = 130;
  private static final long serialVersionUID = MAVLINK_MSG_ID_DATA_TRANSMISSION_HANDSHAKE;
  public msg_data_transmission_handshake() {
    this(1,1);
}
  public msg_data_transmission_handshake(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_DATA_TRANSMISSION_HANDSHAKE;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 13;
}

  /**
   * total data size (set on ACK only).
   */
  public long size;
  /**
   * Width of a matrix or image.
   */
  public int width;
  /**
   * Height of a matrix or image.
   */
  public int height;
  /**
   * Number of packets being sent (set on ACK only).
   */
  public int packets;
  /**
   * Type of requested/acknowledged data.
   */
  public int type;
  /**
   * Payload size per packet (normally 253 byte, see DATA field size in message ENCAPSULATED_DATA) (set on ACK only).
   */
  public int payload;
  /**
   * JPEG quality. Values: [1-100].
   */
  public int jpg_quality;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  size = (int)dis.readInt()&0x00FFFFFFFF;
  width = (int)dis.readUnsignedShort()&0x00FFFF;
  height = (int)dis.readUnsignedShort()&0x00FFFF;
  packets = (int)dis.readUnsignedShort()&0x00FFFF;
  type = (int)dis.readUnsignedByte()&0x00FF;
  payload = (int)dis.readUnsignedByte()&0x00FF;
  jpg_quality = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+13];
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
  dos.writeInt((int)(size&0x00FFFFFFFF));
  dos.writeShort(width&0x00FFFF);
  dos.writeShort(height&0x00FFFF);
  dos.writeShort(packets&0x00FFFF);
  dos.writeByte(type&0x00FF);
  dos.writeByte(payload&0x00FF);
  dos.writeByte(jpg_quality&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 13);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[23] = crcl;
  buffer[24] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_DATA_TRANSMISSION_HANDSHAKE : " +   "  size="+size+  "  width="+width+  "  height="+height+  "  packets="+packets+  "  type="+type+  "  payload="+payload+  "  jpg_quality="+jpg_quality;}
}
