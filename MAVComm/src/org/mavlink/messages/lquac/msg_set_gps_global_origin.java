/**
 * Generated class : msg_set_gps_global_origin
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
 * Class msg_set_gps_global_origin
 * Sets the GPS co-ordinates of the vehicle local origin (0,0,0) position. Vehicle should emit GPS_GLOBAL_ORIGIN irrespective of whether the origin is changed. This enables transform between the local coordinate frame and the global (GPS) coordinate frame, which may be necessary when (for example) indoor and outdoor settings are connected and the MAV should move from in- to outdoor.
 **/
public class msg_set_gps_global_origin extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_SET_GPS_GLOBAL_ORIGIN = 48;
  private static final long serialVersionUID = MAVLINK_MSG_ID_SET_GPS_GLOBAL_ORIGIN;
  public msg_set_gps_global_origin() {
    this(1,1);
}
  public msg_set_gps_global_origin(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_SET_GPS_GLOBAL_ORIGIN;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 21;
}

  /**
   * Latitude (WGS84)
   */
  public long latitude;
  /**
   * Longitude (WGS84)
   */
  public long longitude;
  /**
   * Altitude (MSL). Positive for up.
   */
  public long altitude;
  /**
   * System ID
   */
  public int target_system;
  /**
   * Timestamp (UNIX Epoch time or time since system boot). The receiving end can infer timestamp format (since 1.1.1970 or since system boot) by checking for the magnitude the number.
   */
  public long time_usec;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  latitude = (int)dis.readInt();
  longitude = (int)dis.readInt();
  altitude = (int)dis.readInt();
  target_system = (int)dis.readUnsignedByte()&0x00FF;
  time_usec = (long)dis.readLong();
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+21];
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
  dos.writeInt((int)(latitude&0x00FFFFFFFF));
  dos.writeInt((int)(longitude&0x00FFFFFFFF));
  dos.writeInt((int)(altitude&0x00FFFFFFFF));
  dos.writeByte(target_system&0x00FF);
  dos.writeLong(time_usec);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 21);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[31] = crcl;
  buffer[32] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_SET_GPS_GLOBAL_ORIGIN : " +   "  latitude="+latitude
+  "  longitude="+longitude
+  "  altitude="+altitude
+  "  target_system="+target_system
+  "  time_usec="+time_usec
;}
}
