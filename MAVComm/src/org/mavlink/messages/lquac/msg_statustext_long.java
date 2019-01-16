/**
 * Generated class : msg_statustext_long
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
 * Class msg_statustext_long
 * Status text message (use only for important status and error messages). The full message payload can be used for status text, but we recommend that updates be kept concise. Note: The message is intended as a less restrictive replacement for STATUSTEXT.
 **/
public class msg_statustext_long extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_STATUSTEXT_LONG = 365;
  private static final long serialVersionUID = MAVLINK_MSG_ID_STATUSTEXT_LONG;
  public msg_statustext_long() {
    this(1,1);
}
  public msg_statustext_long(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_STATUSTEXT_LONG;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 255;
}

  /**
   * Severity of status. Relies on the definitions within RFC-5424.
   */
  public int severity;
  /**
   * Status text message, without null termination character.
   */
  public char[] text = new char[254];
  public void setText(String tmp) {
    int len = Math.min(tmp.length(), 254);
    for (int i=0; i<len; i++) {
      text[i] = tmp.charAt(i);
    }
    for (int i=len; i<254; i++) {
      text[i] = 0;
    }
  }
  public String getText() {
    String result="";
    for (int i=0; i<254; i++) {
      if (text[i] != 0) result=result+text[i]; else break;
    }
    return result;
  }
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  severity = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<254; i++) {
    text[i] = (char)dis.readByte();
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+255];
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
  dos.writeByte(severity&0x00FF);
  for (int i=0; i<254; i++) {
    dos.writeByte(text[i]);
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
return "MAVLINK_MSG_ID_STATUSTEXT_LONG : " +   "  severity="+severity
+  "  text="+getText()
;}
}
