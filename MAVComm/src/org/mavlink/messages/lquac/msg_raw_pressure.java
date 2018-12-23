/**
 * Generated class : msg_raw_pressure
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
 * Class msg_raw_pressure
 * The RAW pressure readings for the typical setup of one absolute pressure and one differential pressure sensor. The sensor values should be the raw, UNSCALED ADC values.
 **/
public class msg_raw_pressure extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_RAW_PRESSURE = 28;
  private static final long serialVersionUID = MAVLINK_MSG_ID_RAW_PRESSURE;
  public msg_raw_pressure() {
    this(1,1);
}
  public msg_raw_pressure(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_RAW_PRESSURE;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 16;
}

  /**
   * Timestamp (UNIX Epoch time or time since system boot). The receiving end can infer timestamp format (since 1.1.1970 or since system boot) by checking for the magnitude the number.
   */
  public long time_usec;
  /**
   * Absolute pressure (raw)
   */
  public int press_abs;
  /**
   * Differential pressure 1 (raw, 0 if nonexistent)
   */
  public int press_diff1;
  /**
   * Differential pressure 2 (raw, 0 if nonexistent)
   */
  public int press_diff2;
  /**
   * Raw Temperature measurement (raw)
   */
  public int temperature;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  press_abs = (int)dis.readShort();
  press_diff1 = (int)dis.readShort();
  press_diff2 = (int)dis.readShort();
  temperature = (int)dis.readShort();
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+16];
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
  dos.writeLong(time_usec);
  dos.writeShort(press_abs&0x00FFFF);
  dos.writeShort(press_diff1&0x00FFFF);
  dos.writeShort(press_diff2&0x00FFFF);
  dos.writeShort(temperature&0x00FFFF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 16);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[26] = crcl;
  buffer[27] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_RAW_PRESSURE : " +   "  time_usec="+time_usec
+  "  press_abs="+press_abs
+  "  press_diff1="+press_diff1
+  "  press_diff2="+press_diff2
+  "  temperature="+temperature
;}
}
