/**
 * Generated class : msg_encapsulated_data
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
 * Class msg_encapsulated_data
 * 
 **/
public class msg_encapsulated_data extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_ENCAPSULATED_DATA = 131;
  private static final long serialVersionUID = MAVLINK_MSG_ID_ENCAPSULATED_DATA;
  public msg_encapsulated_data() {
    this(1,1);
}
  public msg_encapsulated_data(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_ENCAPSULATED_DATA;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 255;
}

  /**
   * sequence number (starting with 0 on every transmission)
   */
  public int seqnr;
  /**
   * image data bytes
   */
  public int[] data = new int[253];
/**
 * Decode message with raw data
 */
public void decode(ByteBuffer dis) throws IOException {
  seqnr = (int)dis.getShort()&0x00FFFF;
  for (int i=0; i<253; i++) {
    data[i] = (int)dis.get()&0x00FF;
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+255];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.putShort((short)(seqnr&0x00FFFF));
  for (int i=0; i<253; i++) {
    dos.put((byte)(data[i]&0x00FF));
  }
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 255);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[261] = crcl;
  buffer[262] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_ENCAPSULATED_DATA : " +   "  seqnr="+seqnr+  "  data="+data;}
}
