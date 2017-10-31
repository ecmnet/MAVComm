/**
 * Generated class : msg_file_transfer_protocol
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
 * Class msg_file_transfer_protocol
 * File transfer message
 **/
public class msg_file_transfer_protocol extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_FILE_TRANSFER_PROTOCOL = 110;
  private static final long serialVersionUID = MAVLINK_MSG_ID_FILE_TRANSFER_PROTOCOL;
  public msg_file_transfer_protocol() {
    this(1,1);
}
  public msg_file_transfer_protocol(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_FILE_TRANSFER_PROTOCOL;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 254;
}

  /**
   * Network ID (0 for broadcast)
   */
  public int target_network;
  /**
   * System ID (0 for broadcast)
   */
  public int target_system;
  /**
   * Component ID (0 for broadcast)
   */
  public int target_component;
  /**
   * Variable length payload. The length is defined by the remaining message length when subtracting the header and other fields.  The entire content of this block is opaque unless you understand any the encoding message_type.  The particular encoding used can be extension specific and might not always be documented as part of the mavlink specification.
   */
  public int[] payload = new int[251];
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  target_network = (int)dis.readUnsignedByte()&0x00FF;
  target_system = (int)dis.readUnsignedByte()&0x00FF;
  target_component = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<251; i++) {
    payload[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+254];
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
  dos.writeByte(target_network&0x00FF);
  dos.writeByte(target_system&0x00FF);
  dos.writeByte(target_component&0x00FF);
  for (int i=0; i<251; i++) {
    dos.writeByte(payload[i]&0x00FF);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 254);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[264] = crcl;
  buffer[265] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_FILE_TRANSFER_PROTOCOL : " +   "  target_network="+target_network+  "  target_system="+target_system+  "  target_component="+target_component+  "  payload="+payload;}
}
