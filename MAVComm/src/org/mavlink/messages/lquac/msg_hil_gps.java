/**
 * Generated class : msg_hil_gps
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
 * Class msg_hil_gps
 * The global position, as returned by the Global Positioning System (GPS). This is
                 NOT the global position estimate of the sytem, but rather a RAW sensor value. See message GLOBAL_POSITION for the global position estimate.
 **/
public class msg_hil_gps extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_HIL_GPS = 113;
  private static final long serialVersionUID = MAVLINK_MSG_ID_HIL_GPS;
  public msg_hil_gps() {
    this(1,1);
}
  public msg_hil_gps(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_HIL_GPS;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 36;
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
   * Altitude (MSL). Positive for up.
   */
  public long alt;
  /**
   * GPS HDOP horizontal dilution of position. If unknown, set to: 65535
   */
  public int eph;
  /**
   * GPS VDOP vertical dilution of position. If unknown, set to: 65535
   */
  public int epv;
  /**
   * GPS ground speed. If unknown, set to: 65535
   */
  public int vel;
  /**
   * GPS velocity in NORTH direction in earth-fixed NED frame
   */
  public int vn;
  /**
   * GPS velocity in EAST direction in earth-fixed NED frame
   */
  public int ve;
  /**
   * GPS velocity in DOWN direction in earth-fixed NED frame
   */
  public int vd;
  /**
   * Course over ground (NOT heading, but direction of movement), 0.0..359.99 degrees. If unknown, set to: 65535
   */
  public int cog;
  /**
   * 0-1: no fix, 2: 2D fix, 3: 3D fix. Some applications will not use the value of this field unless it is at least two, so always correctly fill in the fix.
   */
  public int fix_type;
  /**
   * Number of satellites visible. If unknown, set to 255
   */
  public int satellites_visible;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  lat = (int)dis.readInt();
  lon = (int)dis.readInt();
  alt = (int)dis.readInt();
  eph = (int)dis.readUnsignedShort()&0x00FFFF;
  epv = (int)dis.readUnsignedShort()&0x00FFFF;
  vel = (int)dis.readUnsignedShort()&0x00FFFF;
  vn = (int)dis.readShort();
  ve = (int)dis.readShort();
  vd = (int)dis.readShort();
  cog = (int)dis.readUnsignedShort()&0x00FFFF;
  fix_type = (int)dis.readUnsignedByte()&0x00FF;
  satellites_visible = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+36];
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
  dos.writeShort(eph&0x00FFFF);
  dos.writeShort(epv&0x00FFFF);
  dos.writeShort(vel&0x00FFFF);
  dos.writeShort(vn&0x00FFFF);
  dos.writeShort(ve&0x00FFFF);
  dos.writeShort(vd&0x00FFFF);
  dos.writeShort(cog&0x00FFFF);
  dos.writeByte(fix_type&0x00FF);
  dos.writeByte(satellites_visible&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 36);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[46] = crcl;
  buffer[47] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_HIL_GPS : " +   "  time_usec="+time_usec
+  "  lat="+lat
+  "  lon="+lon
+  "  alt="+alt
+  "  eph="+eph
+  "  epv="+epv
+  "  vel="+vel
+  "  vn="+vn
+  "  ve="+ve
+  "  vd="+vd
+  "  cog="+cog
+  "  fix_type="+fix_type
+  "  satellites_visible="+satellites_visible
;}
}
