/**
 * Generated class : msg_rc_channels_raw
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
 * Class msg_rc_channels_raw
 * The RAW values of the RC channels received. The standard PPM modulation is as follows: 1000 microseconds: 0%, 2000 microseconds: 100%. A value of UINT16_MAX implies the channel is unused. Individual receivers/transmitters might violate this specification.
 **/
public class msg_rc_channels_raw extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_RC_CHANNELS_RAW = 35;
  private static final long serialVersionUID = MAVLINK_MSG_ID_RC_CHANNELS_RAW;
  public msg_rc_channels_raw() {
    this(1,1);
}
  public msg_rc_channels_raw(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_RC_CHANNELS_RAW;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 22;
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
   * Servo output port (set of 8 outputs = 1 port). Most MAVs will just use one, but this allows for more than 8 servos.
   */
  public int port;
  /**
   * Receive signal strength indicator. Values: [0-100], 255: invalid/unknown.
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
  port = (int)dis.readUnsignedByte()&0x00FF;
  rssi = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+22];
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
  dos.writeByte(port&0x00FF);
  dos.writeByte(rssi&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 22);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[32] = crcl;
  buffer[33] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_RC_CHANNELS_RAW : " +   "  time_boot_ms="+time_boot_ms+  "  chan1_raw="+chan1_raw+  "  chan2_raw="+chan2_raw+  "  chan3_raw="+chan3_raw+  "  chan4_raw="+chan4_raw+  "  chan5_raw="+chan5_raw+  "  chan6_raw="+chan6_raw+  "  chan7_raw="+chan7_raw+  "  chan8_raw="+chan8_raw+  "  port="+port+  "  rssi="+rssi;}
}
