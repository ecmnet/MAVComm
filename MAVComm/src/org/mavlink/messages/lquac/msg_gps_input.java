/**
 * Generated class : msg_gps_input
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
 * Class msg_gps_input
 * GPS sensor input message.  This is a raw sensor value sent by the GPS. This is NOT the global position estimate of the system.
 **/
public class msg_gps_input extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_GPS_INPUT = 232;
  private static final long serialVersionUID = MAVLINK_MSG_ID_GPS_INPUT;
  public msg_gps_input() {
    this(1,1);
}
  public msg_gps_input(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_GPS_INPUT;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 63;
}

  /**
   * Timestamp (UNIX Epoch time or time since system boot). The receiving end can infer timestamp format (since 1.1.1970 or since system boot) by checking for the magnitude the number.
   */
  public long time_usec;
  /**
   * GPS time (from start of GPS week)
   */
  public long time_week_ms;
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
  public float alt;
  /**
   * GPS HDOP horizontal dilution of position
   */
  public float hdop;
  /**
   * GPS VDOP vertical dilution of position
   */
  public float vdop;
  /**
   * GPS velocity in NORTH direction in earth-fixed NED frame
   */
  public float vn;
  /**
   * GPS velocity in EAST direction in earth-fixed NED frame
   */
  public float ve;
  /**
   * GPS velocity in DOWN direction in earth-fixed NED frame
   */
  public float vd;
  /**
   * GPS speed accuracy
   */
  public float speed_accuracy;
  /**
   * GPS horizontal accuracy
   */
  public float horiz_accuracy;
  /**
   * GPS vertical accuracy
   */
  public float vert_accuracy;
  /**
   * Bitmap indicating which GPS input flags fields to ignore.  All other fields must be provided.
   */
  public int ignore_flags;
  /**
   * GPS week number
   */
  public int time_week;
  /**
   * ID of the GPS for multiple GPS inputs
   */
  public int gps_id;
  /**
   * 0-1: no fix, 2: 2D fix, 3: 3D fix. 4: 3D with DGPS. 5: 3D with RTK
   */
  public int fix_type;
  /**
   * Number of satellites visible.
   */
  public int satellites_visible;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  time_week_ms = (int)dis.readInt()&0x00FFFFFFFF;
  lat = (int)dis.readInt();
  lon = (int)dis.readInt();
  alt = (float)dis.readFloat();
  hdop = (float)dis.readFloat();
  vdop = (float)dis.readFloat();
  vn = (float)dis.readFloat();
  ve = (float)dis.readFloat();
  vd = (float)dis.readFloat();
  speed_accuracy = (float)dis.readFloat();
  horiz_accuracy = (float)dis.readFloat();
  vert_accuracy = (float)dis.readFloat();
  ignore_flags = (int)dis.readUnsignedShort()&0x00FFFF;
  time_week = (int)dis.readUnsignedShort()&0x00FFFF;
  gps_id = (int)dis.readUnsignedByte()&0x00FF;
  fix_type = (int)dis.readUnsignedByte()&0x00FF;
  satellites_visible = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+63];
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
  dos.writeInt((int)(time_week_ms&0x00FFFFFFFF));
  dos.writeInt((int)(lat&0x00FFFFFFFF));
  dos.writeInt((int)(lon&0x00FFFFFFFF));
  dos.writeFloat(alt);
  dos.writeFloat(hdop);
  dos.writeFloat(vdop);
  dos.writeFloat(vn);
  dos.writeFloat(ve);
  dos.writeFloat(vd);
  dos.writeFloat(speed_accuracy);
  dos.writeFloat(horiz_accuracy);
  dos.writeFloat(vert_accuracy);
  dos.writeShort(ignore_flags&0x00FFFF);
  dos.writeShort(time_week&0x00FFFF);
  dos.writeByte(gps_id&0x00FF);
  dos.writeByte(fix_type&0x00FF);
  dos.writeByte(satellites_visible&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 63);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[73] = crcl;
  buffer[74] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_GPS_INPUT : " +   "  time_usec="+time_usec+  "  time_week_ms="+time_week_ms+  "  lat="+lat+  "  lon="+lon+  "  alt="+alt+  "  hdop="+hdop+  "  vdop="+vdop+  "  vn="+vn+  "  ve="+ve+  "  vd="+vd+  "  speed_accuracy="+speed_accuracy+  "  horiz_accuracy="+horiz_accuracy+  "  vert_accuracy="+vert_accuracy+  "  ignore_flags="+ignore_flags+  "  time_week="+time_week+  "  gps_id="+gps_id+  "  fix_type="+fix_type+  "  satellites_visible="+satellites_visible;}
}
