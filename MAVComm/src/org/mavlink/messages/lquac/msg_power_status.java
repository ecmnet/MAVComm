/**
 * Generated class : msg_power_status
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
 * Class msg_power_status
 * Power supply status
 **/
public class msg_power_status extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_POWER_STATUS = 125;
  private static final long serialVersionUID = MAVLINK_MSG_ID_POWER_STATUS;
  public msg_power_status() {
    this(1,1);
}
  public msg_power_status(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_POWER_STATUS;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 6;
}

  /**
   * 5V rail voltage.
   */
  public int Vcc;
  /**
   * Servo rail voltage.
   */
  public int Vservo;
  /**
   * Bitmap of power supply status flags.
   */
  public int flags;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  Vcc = (int)dis.readUnsignedShort()&0x00FFFF;
  Vservo = (int)dis.readUnsignedShort()&0x00FFFF;
  flags = (int)dis.readUnsignedShort()&0x00FFFF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+6];
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
  dos.writeShort(Vcc&0x00FFFF);
  dos.writeShort(Vservo&0x00FFFF);
  dos.writeShort(flags&0x00FFFF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 6);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[16] = crcl;
  buffer[17] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_POWER_STATUS : " +   "  Vcc="+Vcc+  "  Vservo="+Vservo+  "  flags="+flags;}
}
