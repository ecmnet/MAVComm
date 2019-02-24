/**
 * Generated class : msg_rc_channels
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
 * Class msg_rc_channels
 * The PPM values of the RC channels received. The standard PPM modulation is as follows: 1000 microseconds: 0%, 2000 microseconds: 100%.  A value of UINT16_MAX implies the channel is unused. Individual receivers/transmitters might violate this specification.
 **/
public class msg_rc_channels extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_RC_CHANNELS = 65;
  private static final long serialVersionUID = MAVLINK_MSG_ID_RC_CHANNELS;
  public msg_rc_channels() {
    this(1,1);
}
  public msg_rc_channels(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_RC_CHANNELS;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 42;
}

  /**
   * Timestamp (time since system boot).
   */
  public long time_boot_ms;
  /**
   * RC channel 1 value.
   */
  public int chan1_raw;
  /**
   * RC channel 2 value.
   */
  public int chan2_raw;
  /**
   * RC channel 3 value.
   */
  public int chan3_raw;
  /**
   * RC channel 4 value.
   */
  public int chan4_raw;
  /**
   * RC channel 5 value.
   */
  public int chan5_raw;
  /**
   * RC channel 6 value.
   */
  public int chan6_raw;
  /**
   * RC channel 7 value.
   */
  public int chan7_raw;
  /**
   * RC channel 8 value.
   */
  public int chan8_raw;
  /**
   * RC channel 9 value.
   */
  public int chan9_raw;
  /**
   * RC channel 10 value.
   */
  public int chan10_raw;
  /**
   * RC channel 11 value.
   */
  public int chan11_raw;
  /**
   * RC channel 12 value.
   */
  public int chan12_raw;
  /**
   * RC channel 13 value.
   */
  public int chan13_raw;
  /**
   * RC channel 14 value.
   */
  public int chan14_raw;
  /**
   * RC channel 15 value.
   */
  public int chan15_raw;
  /**
   * RC channel 16 value.
   */
  public int chan16_raw;
  /**
   * RC channel 17 value.
   */
  public int chan17_raw;
  /**
   * RC channel 18 value.
   */
  public int chan18_raw;
  /**
   * Total number of RC channels being received. This can be larger than 18, indicating that more channels are available but not given in this message. This value should be 0 when no RC channels are available.
   */
  public int chancount;
  /**
   * Receive signal strength indicator in device-dependent units/scale. Values: [0-254], 255: invalid/unknown.
   */
  public int rssi;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_boot_ms = (int)dis.readInt()&0x00FFFFFFFF;
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
  chan13_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  chan14_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  chan15_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  chan16_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  chan17_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  chan18_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  chancount = (int)dis.readUnsignedByte()&0x00FF;
  rssi = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+42];
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
  dos.writeInt((int)(time_boot_ms&0x00FFFFFFFF));
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
  dos.writeShort(chan13_raw&0x00FFFF);
  dos.writeShort(chan14_raw&0x00FFFF);
  dos.writeShort(chan15_raw&0x00FFFF);
  dos.writeShort(chan16_raw&0x00FFFF);
  dos.writeShort(chan17_raw&0x00FFFF);
  dos.writeShort(chan18_raw&0x00FFFF);
  dos.writeByte(chancount&0x00FF);
  dos.writeByte(rssi&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 42);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[52] = crcl;
  buffer[53] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_RC_CHANNELS : " +   "  time_boot_ms="+time_boot_ms
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
+  "  chan13_raw="+chan13_raw
+  "  chan14_raw="+chan14_raw
+  "  chan15_raw="+chan15_raw
+  "  chan16_raw="+chan16_raw
+  "  chan17_raw="+chan17_raw
+  "  chan18_raw="+chan18_raw
+  "  chancount="+chancount
+  "  rssi="+rssi
;}
}
