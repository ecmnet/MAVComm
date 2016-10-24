/**
 * Generated class : msg_logging_data_acked
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
 * Class msg_logging_data_acked
 * A message containing logged data which requires a LOGGING_ACK to be sent back
 **/
public class msg_logging_data_acked extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_LOGGING_DATA_ACKED = 267;
  private static final long serialVersionUID = MAVLINK_MSG_ID_LOGGING_DATA_ACKED;
  public msg_logging_data_acked() {
    this(1,1);
}
  public msg_logging_data_acked(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_LOGGING_DATA_ACKED;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 255;
}

  /**
   * sequence number (can wrap)
   */
  public int sequence;
  /**
   * system ID of the target
   */
  public int target_system;
  /**
   * component ID of the target
   */
  public int target_component;
  /**
   * data length
   */
  public int length;
  /**
   * offset into data where first message starts. This can be used for recovery, when a previous message got lost (set to 255 if no start exists).
   */
  public int first_message_offset;
  /**
   * logged data
   */
  public int[] data = new int[249];
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  sequence = (int)dis.readUnsignedShort()&0x00FFFF;
  target_system = (int)dis.readUnsignedByte()&0x00FF;
  target_component = (int)dis.readUnsignedByte()&0x00FF;
  length = (int)dis.readUnsignedByte()&0x00FF;
  first_message_offset = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<249; i++) {
    data[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+255];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFD);
  dos.writeByte(length & 0x00FF);
  dos.writeByte(incompat & 0x00FF);
  dos.writeByte(compat & 0x00FF);
  dos.writeByte(sequence & 0x00FF);
  dos.writeByte(sysId & 0x00FF);
  dos.writeByte(componentId & 0x00FF);
  dos.writeByte(messageType & 0x00FF);
  dos.writeByte((messageType >> 8) & 0x00FF);
  dos.writeByte((messageType >> 16) & 0x00FF);
  dos.writeShort(sequence&0x00FFFF);
  dos.writeByte(target_system&0x00FF);
  dos.writeByte(target_component&0x00FF);
  dos.writeByte(length&0x00FF);
  dos.writeByte(first_message_offset&0x00FF);
  for (int i=0; i<249; i++) {
    dos.writeByte(data[i]&0x00FF);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 255);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[265] = crcl;
  buffer[266] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_LOGGING_DATA_ACKED : " +   "  sequence="+sequence+  "  target_system="+target_system+  "  target_component="+target_component+  "  length="+length+  "  first_message_offset="+first_message_offset+  "  data="+data;}
}
