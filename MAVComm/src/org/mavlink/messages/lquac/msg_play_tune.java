/**
 * Generated class : msg_play_tune
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
 * Class msg_play_tune
 * Control vehicle tone generation (buzzer)
 **/
public class msg_play_tune extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_PLAY_TUNE = 258;
  private static final long serialVersionUID = MAVLINK_MSG_ID_PLAY_TUNE;
  public msg_play_tune() {
    this(1,1);
}
  public msg_play_tune(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_PLAY_TUNE;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 32;
}

  /**
   * System ID
   */
  public int target_system;
  /**
   * Component ID
   */
  public int target_component;
  /**
   * tune in board specific format
   */
  public char[] tune = new char[30];
  public void setTune(String tmp) {
    int len = Math.min(tmp.length(), 30);
    for (int i=0; i<len; i++) {
      tune[i] = tmp.charAt(i);
    }
    for (int i=len; i<30; i++) {
      tune[i] = 0;
    }
  }
  public String getTune() {
    String result="";
    for (int i=0; i<30; i++) {
      if (tune[i] != 0) result=result+tune[i]; else break;
    }
    return result;
  }
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  target_system = (int)dis.readUnsignedByte()&0x00FF;
  target_component = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<30; i++) {
    tune[i] = (char)dis.readByte();
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+32];
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
  dos.writeByte(target_system&0x00FF);
  dos.writeByte(target_component&0x00FF);
  for (int i=0; i<30; i++) {
    dos.writeByte(tune[i]);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 32);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[42] = crcl;
  buffer[43] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_PLAY_TUNE : " +   "  target_system="+target_system+  "  target_component="+target_component+  "  tune="+getTune();}
}
