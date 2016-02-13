/**
 * Generated class : msg_global_position_int_cov
 * DO NOT MODIFY!
 **/
package org.mavlink.messages.lquac;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.mavlink.IMAVLinkCRC;
import org.mavlink.MAVLinkCRC;
import org.mavlink.messages.MAVLinkMessage;
/**
 * Class msg_global_position_int_cov
 * The filtered global position (e.g. fused GPS and accelerometers). The position is in GPS-frame (right-handed, Z-up). It  is designed as scaled integer message since the resolution of float is not sufficient. NOTE: This message is intended for onboard networks / companion computers and higher-bandwidth links and optimized for accuracy and completeness. Please use the GLOBAL_POSITION_INT message for a minimal subset.
 **/
public class msg_global_position_int_cov extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_GLOBAL_POSITION_INT_COV = 63;
  private static final long serialVersionUID = MAVLINK_MSG_ID_GLOBAL_POSITION_INT_COV;
  public msg_global_position_int_cov() {
    this(1,1);
}
  public msg_global_position_int_cov(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_GLOBAL_POSITION_INT_COV;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 185;
}

  /**
   * Timestamp (microseconds since UNIX epoch) in UTC. 0 for unknown. Commonly filled by the precision time source of a GPS receiver.
   */
  public long time_utc;
  /**
   * Timestamp (milliseconds since system boot)
   */
  public long time_boot_ms;
  /**
   * Latitude, expressed as degrees * 1E7
   */
  public long lat;
  /**
   * Longitude, expressed as degrees * 1E7
   */
  public long lon;
  /**
   * Altitude in meters, expressed as * 1000 (millimeters), above MSL
   */
  public long alt;
  /**
   * Altitude above ground in meters, expressed as * 1000 (millimeters)
   */
  public long relative_alt;
  /**
   * Ground X Speed (Latitude), expressed as m/s
   */
  public float vx;
  /**
   * Ground Y Speed (Longitude), expressed as m/s
   */
  public float vy;
  /**
   * Ground Z Speed (Altitude), expressed as m/s
   */
  public float vz;
  /**
   * Covariance matrix (first six entries are the first ROW, next six entries are the second row, etc.)
   */
  public float[] covariance = new float[36];
  /**
   * Class id of the estimator this estimate originated from.
   */
  public int estimator_type;
/**
 * Decode message with raw data
 */
public void decode(ByteBuffer dis) throws IOException {
  time_utc = (long)dis.getLong();
  time_boot_ms = (int)dis.getInt()&0x00FFFFFFFF;
  lat = (int)dis.getInt();
  lon = (int)dis.getInt();
  alt = (int)dis.getInt();
  relative_alt = (int)dis.getInt();
  vx = (float)dis.getFloat();
  vy = (float)dis.getFloat();
  vz = (float)dis.getFloat();
  for (int i=0; i<36; i++) {
    covariance[i] = (float)dis.getFloat();
  }
  estimator_type = (int)dis.get()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+185];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.putLong(time_utc);
  dos.putInt((int)(time_boot_ms&0x00FFFFFFFF));
  dos.putInt((int)(lat&0x00FFFFFFFF));
  dos.putInt((int)(lon&0x00FFFFFFFF));
  dos.putInt((int)(alt&0x00FFFFFFFF));
  dos.putInt((int)(relative_alt&0x00FFFFFFFF));
  dos.putFloat(vx);
  dos.putFloat(vy);
  dos.putFloat(vz);
  for (int i=0; i<36; i++) {
    dos.putFloat(covariance[i]);
  }
  dos.put((byte)(estimator_type&0x00FF));
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 185);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[191] = crcl;
  buffer[192] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_GLOBAL_POSITION_INT_COV : " +   "  time_utc="+time_utc+  "  time_boot_ms="+time_boot_ms+  "  lat="+lat+  "  lon="+lon+  "  alt="+alt+  "  relative_alt="+relative_alt+  "  vx="+vx+  "  vy="+vy+  "  vz="+vz+  "  covariance="+covariance+  "  estimator_type="+estimator_type;}
}
