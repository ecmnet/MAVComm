/**
 * Generated class : msg_flight_information
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
 * Class msg_flight_information
 * WIP: Information about flight since last arming
 **/
public class msg_flight_information extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_FLIGHT_INFORMATION = 264;
  private static final long serialVersionUID = MAVLINK_MSG_ID_FLIGHT_INFORMATION;
  public msg_flight_information() {
    this(1,1);
}
  public msg_flight_information(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_FLIGHT_INFORMATION;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 28;
}

  /**
   * Timestamp at arming (microseconds since UNIX epoch) in UTC, 0 for unknown
   */
  public long arming_time_utc;
  /**
   * Timestamp at takeoff (microseconds since UNIX epoch) in UTC, 0 for unknown
   */
  public long takeoff_time_utc;
  /**
   * Universally unique identifier (UUID) of flight, should correspond to name of logfiles
   */
  public long flight_uuid;
  /**
   * Timestamp (milliseconds since system boot)
   */
  public long time_boot_ms;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  arming_time_utc = (long)dis.readLong();
  takeoff_time_utc = (long)dis.readLong();
  flight_uuid = (long)dis.readLong();
  time_boot_ms = (int)dis.readInt()&0x00FFFFFFFF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+28];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFD);
  dos.writeByte(length & 0x00FF);
  dos.writeByte(incompat & 0x00FF);
  dos.writeByte(compat & 0x00FF);
  dos.writeByte(packet & 0x00FF);
  dos.writeByte(sysId & 0x00FF);
  dos.writeByte(componentId & 0x00FF);
  dos.writeByte(messageType & 0x00FF);
  dos.writeByte((messageType >> 8) & 0x00FF);
  dos.writeByte((messageType >> 16) & 0x00FF);
  dos.writeLong(arming_time_utc);
  dos.writeLong(takeoff_time_utc);
  dos.writeLong(flight_uuid);
  dos.writeInt((int)(time_boot_ms&0x00FFFFFFFF));
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 28);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[38] = crcl;
  buffer[39] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_FLIGHT_INFORMATION : " +   "  arming_time_utc="+arming_time_utc+  "  takeoff_time_utc="+takeoff_time_utc+  "  flight_uuid="+flight_uuid+  "  time_boot_ms="+time_boot_ms;}
}
