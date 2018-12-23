/**
 * Generated class : msg_system_time
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
 * Class msg_system_time
 * The system time is the time of the master clock, typically the computer clock of the main onboard computer.
 **/
public class msg_system_time extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_SYSTEM_TIME = 2;
  private static final long serialVersionUID = MAVLINK_MSG_ID_SYSTEM_TIME;
  public msg_system_time() {
    this(1,1);
}
  public msg_system_time(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_SYSTEM_TIME;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 12;
}

  /**
   * Timestamp (UNIX epoch time).
   */
  public long time_unix_usec;
  /**
   * Timestamp (time since system boot).
   */
  public long time_boot_ms;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_unix_usec = (long)dis.readLong();
  time_boot_ms = (int)dis.readInt()&0x00FFFFFFFF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+12];
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
  dos.writeLong(time_unix_usec);
  dos.writeInt((int)(time_boot_ms&0x00FFFFFFFF));
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 12);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[22] = crcl;
  buffer[23] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_SYSTEM_TIME : " +   "  time_unix_usec="+time_unix_usec
+  "  time_boot_ms="+time_boot_ms
;}
}
