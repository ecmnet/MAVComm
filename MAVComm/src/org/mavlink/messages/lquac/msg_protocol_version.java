/**
 * Generated class : msg_protocol_version
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
 * Class msg_protocol_version
 * WIP: Version and capability of protocol version. This message is the response to REQUEST_PROTOCOL_VERSION and is used as part of the handshaking to establish which MAVLink version should be used on the network. Every node should respond to REQUEST_PROTOCOL_VERSION to enable the handshaking. Library implementers should consider adding this into the default decoding state machine to allow the protocol core to respond directly.
 **/
public class msg_protocol_version extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_PROTOCOL_VERSION = 300;
  private static final long serialVersionUID = MAVLINK_MSG_ID_PROTOCOL_VERSION;
  public msg_protocol_version() {
    this(1,1);
}
  public msg_protocol_version(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_PROTOCOL_VERSION;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 22;
}

  /**
   * Currently active MAVLink version number * 100: v1.0 is 100, v2.0 is 200, etc.
   */
  public int version;
  /**
   * Minimum MAVLink version supported
   */
  public int min_version;
  /**
   * Maximum MAVLink version supported (set to the same value as version by default)
   */
  public int max_version;
  /**
   * The first 8 bytes (not characters printed in hex!) of the git hash.
   */
  public int[] spec_version_hash = new int[8];
  /**
   * The first 8 bytes (not characters printed in hex!) of the git hash.
   */
  public int[] library_version_hash = new int[8];
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  version = (int)dis.readUnsignedShort()&0x00FFFF;
  min_version = (int)dis.readUnsignedShort()&0x00FFFF;
  max_version = (int)dis.readUnsignedShort()&0x00FFFF;
  for (int i=0; i<8; i++) {
    spec_version_hash[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
  for (int i=0; i<8; i++) {
    library_version_hash[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+22];
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
  dos.writeShort(version&0x00FFFF);
  dos.writeShort(min_version&0x00FFFF);
  dos.writeShort(max_version&0x00FFFF);
  for (int i=0; i<8; i++) {
    dos.writeByte(spec_version_hash[i]&0x00FF);
  }
  for (int i=0; i<8; i++) {
    dos.writeByte(library_version_hash[i]&0x00FF);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 22);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[32] = crcl;
  buffer[33] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_PROTOCOL_VERSION : " +   "  version="+version+  "  min_version="+min_version+  "  max_version="+max_version+  "  spec_version_hash="+spec_version_hash+  "  library_version_hash="+library_version_hash;}
}
