/**
 * Generated class : msg_autopilot_version
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
 * Class msg_autopilot_version
 * Version and capability of autopilot software
 **/
public class msg_autopilot_version extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_AUTOPILOT_VERSION = 148;
  private static final long serialVersionUID = MAVLINK_MSG_ID_AUTOPILOT_VERSION;
  public msg_autopilot_version() {
    this(1,1);
}
  public msg_autopilot_version(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_AUTOPILOT_VERSION;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 78;
}

  /**
   * Bitmap of capabilities
   */
  public long capabilities;
  /**
   * UID if provided by hardware (see uid2)
   */
  public long uid;
  /**
   * Firmware version number
   */
  public long flight_sw_version;
  /**
   * Middleware version number
   */
  public long middleware_sw_version;
  /**
   * Operating system version number
   */
  public long os_sw_version;
  /**
   * HW / board version (last 8 bytes should be silicon ID, if any)
   */
  public long board_version;
  /**
   * ID of the board vendor
   */
  public int vendor_id;
  /**
   * ID of the product
   */
  public int product_id;
  /**
   * Custom version field, commonly the first 8 bytes of the git hash. This is not an unique identifier, but should allow to identify the commit using the main version number even for very large code bases.
   */
  public int[] flight_custom_version = new int[8];
  /**
   * Custom version field, commonly the first 8 bytes of the git hash. This is not an unique identifier, but should allow to identify the commit using the main version number even for very large code bases.
   */
  public int[] middleware_custom_version = new int[8];
  /**
   * Custom version field, commonly the first 8 bytes of the git hash. This is not an unique identifier, but should allow to identify the commit using the main version number even for very large code bases.
   */
  public int[] os_custom_version = new int[8];
  /**
   * UID if provided by hardware (supersedes the uid field. If this is non-zero, use this field, otherwise use uid)
   */
  public int[] uid2 = new int[18];
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  capabilities = (long)dis.readLong();
  uid = (long)dis.readLong();
  flight_sw_version = (int)dis.readInt()&0x00FFFFFFFF;
  middleware_sw_version = (int)dis.readInt()&0x00FFFFFFFF;
  os_sw_version = (int)dis.readInt()&0x00FFFFFFFF;
  board_version = (int)dis.readInt()&0x00FFFFFFFF;
  vendor_id = (int)dis.readUnsignedShort()&0x00FFFF;
  product_id = (int)dis.readUnsignedShort()&0x00FFFF;
  for (int i=0; i<8; i++) {
    flight_custom_version[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
  for (int i=0; i<8; i++) {
    middleware_custom_version[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
  for (int i=0; i<8; i++) {
    os_custom_version[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
  for (int i=0; i<18; i++) {
    uid2[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+78];
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
  dos.writeLong(capabilities);
  dos.writeLong(uid);
  dos.writeInt((int)(flight_sw_version&0x00FFFFFFFF));
  dos.writeInt((int)(middleware_sw_version&0x00FFFFFFFF));
  dos.writeInt((int)(os_sw_version&0x00FFFFFFFF));
  dos.writeInt((int)(board_version&0x00FFFFFFFF));
  dos.writeShort(vendor_id&0x00FFFF);
  dos.writeShort(product_id&0x00FFFF);
  for (int i=0; i<8; i++) {
    dos.writeByte(flight_custom_version[i]&0x00FF);
  }
  for (int i=0; i<8; i++) {
    dos.writeByte(middleware_custom_version[i]&0x00FF);
  }
  for (int i=0; i<8; i++) {
    dos.writeByte(os_custom_version[i]&0x00FF);
  }
  for (int i=0; i<18; i++) {
    dos.writeByte(uid2[i]&0x00FF);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 78);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[88] = crcl;
  buffer[89] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_AUTOPILOT_VERSION : " +   "  capabilities="+capabilities+  "  uid="+uid+  "  flight_sw_version="+flight_sw_version+  "  middleware_sw_version="+middleware_sw_version+  "  os_sw_version="+os_sw_version+  "  board_version="+board_version+  "  vendor_id="+vendor_id+  "  product_id="+product_id+  "  flight_custom_version="+flight_custom_version+  "  middleware_custom_version="+middleware_custom_version+  "  os_custom_version="+os_custom_version+  "  uid2="+uid2;}
}
