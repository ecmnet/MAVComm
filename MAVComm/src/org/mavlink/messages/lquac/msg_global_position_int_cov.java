/**
 * Generated class : msg_global_position_int_cov
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
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_utc = (long)dis.readLong();
  time_boot_ms = (int)dis.readInt()&0x00FFFFFFFF;
  lat = (int)dis.readInt();
  lon = (int)dis.readInt();
  alt = (int)dis.readInt();
  relative_alt = (int)dis.readInt();
  vx = (float)dis.readFloat();
  vy = (float)dis.readFloat();
  vz = (float)dis.readFloat();
  for (int i=0; i<36; i++) {
    covariance[i] = (float)dis.readFloat();
  }
  estimator_type = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+185];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFE);
  dos.writeByte(length & 0x00FF);
  dos.writeByte(sequence & 0x00FF);
  dos.writeByte(sysId & 0x00FF);
  dos.writeByte(componentId & 0x00FF);
  dos.writeByte(messageType & 0x00FF);
  dos.writeLong(time_utc);
  dos.writeInt((int)(time_boot_ms&0x00FFFFFFFF));
  dos.writeInt((int)(lat&0x00FFFFFFFF));
  dos.writeInt((int)(lon&0x00FFFFFFFF));
  dos.writeInt((int)(alt&0x00FFFFFFFF));
  dos.writeInt((int)(relative_alt&0x00FFFFFFFF));
  dos.writeFloat(vx);
  dos.writeFloat(vy);
  dos.writeFloat(vz);
  for (int i=0; i<36; i++) {
    dos.writeFloat(covariance[i]);
  }
  dos.writeByte(estimator_type&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 185);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[191] = crcl;
  buffer[192] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_GLOBAL_POSITION_INT_COV : " +   "  time_utc="+time_utc+  "  time_boot_ms="+time_boot_ms+  "  lat="+lat+  "  lon="+lon+  "  alt="+alt+  "  relative_alt="+relative_alt+  "  vx="+vx+  "  vy="+vy+  "  vz="+vz+  "  covariance="+covariance+  "  estimator_type="+estimator_type;}
}
