/**
 * Generated class : msg_gps2_raw
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
 * Class msg_gps2_raw
 * Second GPS data.
 **/
public class msg_gps2_raw extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_GPS2_RAW = 124;
  private static final long serialVersionUID = MAVLINK_MSG_ID_GPS2_RAW;
  public msg_gps2_raw() {
    this(1,1);
}
  public msg_gps2_raw(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_GPS2_RAW;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 35;
}

  /**
   * Timestamp (UNIX Epoch time or time since system boot). The receiving end can infer timestamp format (since 1.1.1970 or since system boot) by checking for the magnitude the number.
   */
  public long time_usec;
  /**
   * Latitude (WGS84)
   */
  public long lat;
  /**
   * Longitude (WGS84)
   */
  public long lon;
  /**
   * Altitude (AMSL). Positive for up.
   */
  public long alt;
  /**
   * Age of DGPS info
   */
  public long dgps_age;
  /**
   * GPS HDOP horizontal dilution of position. If unknown, set to: UINT16_MAX
   */
  public int eph;
  /**
   * GPS VDOP vertical dilution of position. If unknown, set to: UINT16_MAX
   */
  public int epv;
  /**
   * GPS ground speed. If unknown, set to: UINT16_MAX
   */
  public int vel;
  /**
   * Course over ground (NOT heading, but direction of movement): 0.0..359.99 degrees. If unknown, set to: UINT16_MAX
   */
  public int cog;
  /**
   * GPS fix type.
   */
  public int fix_type;
  /**
   * Number of satellites visible. If unknown, set to 255
   */
  public int satellites_visible;
  /**
   * Number of DGPS satellites
   */
  public int dgps_numch;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  lat = (int)dis.readInt();
  lon = (int)dis.readInt();
  alt = (int)dis.readInt();
  dgps_age = (int)dis.readInt()&0x00FFFFFFFF;
  eph = (int)dis.readUnsignedShort()&0x00FFFF;
  epv = (int)dis.readUnsignedShort()&0x00FFFF;
  vel = (int)dis.readUnsignedShort()&0x00FFFF;
  cog = (int)dis.readUnsignedShort()&0x00FFFF;
  fix_type = (int)dis.readUnsignedByte()&0x00FF;
  satellites_visible = (int)dis.readUnsignedByte()&0x00FF;
  dgps_numch = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+35];
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
  dos.writeInt((int)(lat&0x00FFFFFFFF));
  dos.writeInt((int)(lon&0x00FFFFFFFF));
  dos.writeInt((int)(alt&0x00FFFFFFFF));
  dos.writeInt((int)(dgps_age&0x00FFFFFFFF));
  dos.writeShort(eph&0x00FFFF);
  dos.writeShort(epv&0x00FFFF);
  dos.writeShort(vel&0x00FFFF);
  dos.writeShort(cog&0x00FFFF);
  dos.writeByte(fix_type&0x00FF);
  dos.writeByte(satellites_visible&0x00FF);
  dos.writeByte(dgps_numch&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 35);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[45] = crcl;
  buffer[46] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_GPS2_RAW : " +   "  time_usec="+time_usec+  "  lat="+lat+  "  lon="+lon+  "  alt="+alt+  "  dgps_age="+dgps_age+  "  eph="+eph+  "  epv="+epv+  "  vel="+vel+  "  cog="+cog+  "  fix_type="+fix_type+  "  satellites_visible="+satellites_visible+  "  dgps_numch="+dgps_numch;}
}
