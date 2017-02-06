/**
 * Generated class : msg_data_stream
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
 * Class msg_data_stream
 * THIS INTERFACE IS DEPRECATED. USE MESSAGE_INTERVAL INSTEAD.
 **/
public class msg_data_stream extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_DATA_STREAM = 67;
  private static final long serialVersionUID = MAVLINK_MSG_ID_DATA_STREAM;
  public msg_data_stream() {
    this(1,1);
}
  public msg_data_stream(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_DATA_STREAM;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 4;
}

  /**
   * The message rate
   */
  public int message_rate;
  /**
   * The ID of the requested data stream
   */
  public int stream_id;
  /**
   * 1 stream is enabled, 0 stream is stopped.
   */
  public int on_off;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  message_rate = (int)dis.readUnsignedShort()&0x00FFFF;
  stream_id = (int)dis.readUnsignedByte()&0x00FF;
  on_off = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+4];
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
  dos.writeShort(message_rate&0x00FFFF);
  dos.writeByte(stream_id&0x00FF);
  dos.writeByte(on_off&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 4);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[14] = crcl;
  buffer[15] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_DATA_STREAM : " +   "  message_rate="+message_rate+  "  stream_id="+stream_id+  "  on_off="+on_off;}
}
