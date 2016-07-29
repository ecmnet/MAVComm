/**
 * Generated class : msg_button_change
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
 * Class msg_button_change
 * Report button state change
 **/
public class msg_button_change extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_BUTTON_CHANGE = 257;
  private static final long serialVersionUID = MAVLINK_MSG_ID_BUTTON_CHANGE;
  public msg_button_change() {
    this(1,1);
}
  public msg_button_change(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_BUTTON_CHANGE;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 9;
}

  /**
   * Timestamp (milliseconds since system boot)
   */
  public long time_boot_ms;
  /**
   * Time of last change of button state
   */
  public long last_change_ms;
  /**
   * Bitmap state of buttons
   */
  public int state;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_boot_ms = (int)dis.readInt()&0x00FFFFFFFF;
  last_change_ms = (int)dis.readInt()&0x00FFFFFFFF;
  state = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+9];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFE);
  dos.writeByte(length & 0x00FF);
  dos.writeByte(sequence & 0x00FF);
  dos.writeByte(sysId & 0x00FF);
  dos.writeByte(componentId & 0x00FF);
  dos.writeByte(messageType & 0x00FF);
  dos.writeInt((int)(time_boot_ms&0x00FFFFFFFF));
  dos.writeInt((int)(last_change_ms&0x00FFFFFFFF));
  dos.writeByte(state&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 9);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[15] = crcl;
  buffer[16] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_BUTTON_CHANGE : " +   "  time_boot_ms="+time_boot_ms+  "  last_change_ms="+last_change_ms+  "  state="+state;}
}
