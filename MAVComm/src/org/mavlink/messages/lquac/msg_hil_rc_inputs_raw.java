/**
 * Generated class : msg_hil_rc_inputs_raw
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
 * Class msg_hil_rc_inputs_raw
 * Sent from simulation to autopilot. The RAW values of the RC channels received. The standard PPM modulation is as follows: 1000 microseconds: 0%, 2000 microseconds: 100%. Individual receivers/transmitters might violate this specification.
 **/
public class msg_hil_rc_inputs_raw extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_HIL_RC_INPUTS_RAW = 92;
  private static final long serialVersionUID = MAVLINK_MSG_ID_HIL_RC_INPUTS_RAW;
  public msg_hil_rc_inputs_raw() {
    this(1,1);
}
  public msg_hil_rc_inputs_raw(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_HIL_RC_INPUTS_RAW;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 33;
}

  /**
   * Timestamp (UNIX Epoch time or time since system boot). The receiving end can infer timestamp format (since 1.1.1970 or since system boot) by checking for the magnitude the number.
   */
  public long time_usec;
  /**
   * RC channel 1 value
   */
  public int chan1_raw;
  /**
   * RC channel 2 value
   */
  public int chan2_raw;
  /**
   * RC channel 3 value
   */
  public int chan3_raw;
  /**
   * RC channel 4 value
   */
  public int chan4_raw;
  /**
   * RC channel 5 value
   */
  public int chan5_raw;
  /**
   * RC channel 6 value
   */
  public int chan6_raw;
  /**
   * RC channel 7 value
   */
  public int chan7_raw;
  /**
   * RC channel 8 value
   */
  public int chan8_raw;
  /**
   * RC channel 9 value
   */
  public int chan9_raw;
  /**
   * RC channel 10 value
   */
  public int chan10_raw;
  /**
   * RC channel 11 value
   */
  public int chan11_raw;
  /**
   * RC channel 12 value
   */
  public int chan12_raw;
  /**
   * Receive signal strength indicator. Values: [0-100], 255: invalid/unknown.
   */
  public int rssi;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  chan1_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  chan2_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  chan3_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  chan4_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  chan5_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  chan6_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  chan7_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  chan8_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  chan9_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  chan10_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  chan11_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  chan12_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  rssi = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+33];
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
  dos.writeShort(chan1_raw&0x00FFFF);
  dos.writeShort(chan2_raw&0x00FFFF);
  dos.writeShort(chan3_raw&0x00FFFF);
  dos.writeShort(chan4_raw&0x00FFFF);
  dos.writeShort(chan5_raw&0x00FFFF);
  dos.writeShort(chan6_raw&0x00FFFF);
  dos.writeShort(chan7_raw&0x00FFFF);
  dos.writeShort(chan8_raw&0x00FFFF);
  dos.writeShort(chan9_raw&0x00FFFF);
  dos.writeShort(chan10_raw&0x00FFFF);
  dos.writeShort(chan11_raw&0x00FFFF);
  dos.writeShort(chan12_raw&0x00FFFF);
  dos.writeByte(rssi&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 33);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[43] = crcl;
  buffer[44] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_HIL_RC_INPUTS_RAW : " +   "  time_usec="+time_usec
+  "  chan1_raw="+chan1_raw
+  "  chan2_raw="+chan2_raw
+  "  chan3_raw="+chan3_raw
+  "  chan4_raw="+chan4_raw
+  "  chan5_raw="+chan5_raw
+  "  chan6_raw="+chan6_raw
+  "  chan7_raw="+chan7_raw
+  "  chan8_raw="+chan8_raw
+  "  chan9_raw="+chan9_raw
+  "  chan10_raw="+chan10_raw
+  "  chan11_raw="+chan11_raw
+  "  chan12_raw="+chan12_raw
+  "  rssi="+rssi
;}
}
