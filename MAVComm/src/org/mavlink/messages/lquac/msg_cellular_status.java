/**
 * Generated class : msg_cellular_status
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
 * Class msg_cellular_status
 * Report current used cellular network status
 **/
public class msg_cellular_status extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_CELLULAR_STATUS = 334;
  private static final long serialVersionUID = MAVLINK_MSG_ID_CELLULAR_STATUS;
  public msg_cellular_status() {
    this(1,1);
}
  public msg_cellular_status(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_CELLULAR_STATUS;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 14;
}

  /**
   * Cell ID. If unknown, set to: UINT32_MAX
   */
  public long cid;
  /**
   * Status bitmap
   */
  public int status;
  /**
   * Mobile country code. If unknown, set to: UINT16_MAX
   */
  public int mcc;
  /**
   * Mobile network code. If unknown, set to: UINT16_MAX
   */
  public int mnc;
  /**
   * Location area code. If unknown, set to: 0
   */
  public int lac;
  /**
   * Cellular network radio type: gsm, cdma, lte...
   */
  public int type;
  /**
   * Cellular network RSSI/RSRP in dBm, absolute value
   */
  public int quality;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  cid = (int)dis.readInt()&0x00FFFFFFFF;
  status = (int)dis.readUnsignedShort()&0x00FFFF;
  mcc = (int)dis.readUnsignedShort()&0x00FFFF;
  mnc = (int)dis.readUnsignedShort()&0x00FFFF;
  lac = (int)dis.readUnsignedShort()&0x00FFFF;
  type = (int)dis.readUnsignedByte()&0x00FF;
  quality = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+14];
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
  dos.writeInt((int)(cid&0x00FFFFFFFF));
  dos.writeShort(status&0x00FFFF);
  dos.writeShort(mcc&0x00FFFF);
  dos.writeShort(mnc&0x00FFFF);
  dos.writeShort(lac&0x00FFFF);
  dos.writeByte(type&0x00FF);
  dos.writeByte(quality&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 14);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[24] = crcl;
  buffer[25] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_CELLULAR_STATUS : " +   "  cid="+cid
+  "  status="+status
+  "  mcc="+mcc
+  "  mnc="+mnc
+  "  lac="+lac
+  "  type="+type
+  "  quality="+quality
;}
}
