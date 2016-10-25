/**
 * Generated class : msg_resource_request
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
 * Class msg_resource_request
 * The autopilot is requesting a resource (file, binary, other type of data)
 **/
public class msg_resource_request extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_RESOURCE_REQUEST = 142;
  private static final long serialVersionUID = MAVLINK_MSG_ID_RESOURCE_REQUEST;
  public msg_resource_request() {
    this(1,1);
}
  public msg_resource_request(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_RESOURCE_REQUEST;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 243;
}

  /**
   * Request ID. This ID should be re-used when sending back URI contents
   */
  public int request_id;
  /**
   * The type of requested URI. 0 = a file via URL. 1 = a UAVCAN binary
   */
  public int uri_type;
  /**
   * The requested unique resource identifier (URI). It is not necessarily a straight domain name (depends on the URI type enum)
   */
  public int[] uri = new int[120];
  /**
   * The way the autopilot wants to receive the URI. 0 = MAVLink FTP. 1 = binary stream.
   */
  public int transfer_type;
  /**
   * The storage path the autopilot wants the URI to be stored in. Will only be valid if the transfer_type has a storage associated (e.g. MAVLink FTP).
   */
  public int[] storage = new int[120];
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  request_id = (int)dis.readUnsignedByte()&0x00FF;
  uri_type = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<120; i++) {
    uri[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
  transfer_type = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<120; i++) {
    storage[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+243];
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
  dos.writeByte(request_id&0x00FF);
  dos.writeByte(uri_type&0x00FF);
  for (int i=0; i<120; i++) {
    dos.writeByte(uri[i]&0x00FF);
  }
  dos.writeByte(transfer_type&0x00FF);
  for (int i=0; i<120; i++) {
    dos.writeByte(storage[i]&0x00FF);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 243);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[253] = crcl;
  buffer[254] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_RESOURCE_REQUEST : " +   "  request_id="+request_id+  "  uri_type="+uri_type+  "  uri="+uri+  "  transfer_type="+transfer_type+  "  storage="+storage;}
}
