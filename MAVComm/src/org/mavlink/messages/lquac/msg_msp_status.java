/**
 * Generated class : msg_msp_status
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
    length = 7;
}

  /**
   * AMA0 communication errors
   */
  public long com_error;
  /**
   * MAVComm version running
   */
  public int version;
  /**
   * The CPU load of the companion
   */
  public int load;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  com_error = (int)dis.readInt()&0x00FFFFFFFF;
  version = (int)dis.readUnsignedShort()&0x00FFFF;
  load = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+7];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFE);
  dos.writeByte(length & 0x00FF);
  dos.writeByte(sequence & 0x00FF);
  dos.writeByte(sysId & 0x00FF);
  dos.writeByte(componentId & 0x00FF);
  dos.writeByte(messageType & 0x00FF);
  dos.writeInt((int)(com_error&0x00FFFFFFFF));
  dos.writeShort(version&0x00FFFF);
  dos.writeByte(load&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 7);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[13] = crcl;
  buffer[14] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_MSP_STATUS : " +   "  com_error="+com_error+  "  version="+version+  "  load="+load;}
}
