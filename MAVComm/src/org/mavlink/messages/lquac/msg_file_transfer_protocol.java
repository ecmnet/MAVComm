/**
 * Generated class : msg_file_transfer_protocol
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
    length = 254;
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
public void decode(ByteBuffer dis) throws IOException {
  target_network = (int)dis.get()&0x00FF;
  target_system = (int)dis.get()&0x00FF;
  target_component = (int)dis.get()&0x00FF;
  for (int i=0; i<251; i++) {
    payload[i] = (int)dis.get()&0x00FF;
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+254];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.put((byte)(target_network&0x00FF));
  dos.put((byte)(target_system&0x00FF));
  dos.put((byte)(target_component&0x00FF));
  for (int i=0; i<251; i++) {
    dos.put((byte)(payload[i]&0x00FF));
  }
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 254);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[260] = crcl;
  buffer[261] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_FILE_TRANSFER_PROTOCOL : " +   "  target_network="+target_network+  "  target_system="+target_system+  "  target_component="+target_component+  "  payload="+payload;}
}
