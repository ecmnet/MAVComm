/**
 * Generated class : msg_msp_status
 * DO NOT MODIFY!
 **/
package org.mavlink.messages.lquac;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.mavlink.IMAVLinkCRC;
import org.mavlink.MAVLinkCRC;
import org.mavlink.messages.MAVLinkMessage;
/**
 * Class msg_msp_status
 * MSP Status message.
 **/
public class msg_msp_status extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_MSP_STATUS = 180;
  private static final long serialVersionUID = MAVLINK_MSG_ID_MSP_STATUS;
  public msg_msp_status() {
    this(1,1);
}
  public msg_msp_status(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_MSP_STATUS;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 1;
}

  /**
   * The CPU load of the companion
   */
  public int load;
/**
 * Decode message with raw data
 */
public void decode(ByteBuffer dis) throws IOException {
  load = (int)dis.get()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+1];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.put((byte)(load&0x00FF));
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 1);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[7] = crcl;
  buffer[8] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_MSP_STATUS : " +   "  load="+load;}
}
