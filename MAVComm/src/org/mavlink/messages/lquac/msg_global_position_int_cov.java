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
    payload_length = 181;
}

  /**
   * Timestamp (UNIX Epoch time or time since system boot). The receiving end can infer timestamp format (since 1.1.1970 or since system boot) by checking for the magnitude the number.
   */
  public long time_usec;
  /**
   * Latitude
   */
  public long lat;
  /**
   * Longitude
   */
  public long lon;
  /**
   * Altitude in meters above MSL
   */
  public long alt;
  /**
   * Altitude above ground
   */
  public long relative_alt;
  /**
   * Ground X Speed (Latitude)
   */
  public float vx;
  /**
   * Ground Y Speed (Longitude)
   */
  public float vy;
  /**
   * Ground Z Speed (Altitude)
   */
  public float vz;
  /**
   * Row-major representation of a 6x6 position and velocity 6x6 cross-covariance matrix (states: lat, lon, alt, vx, vy, vz; first six entries are the first ROW, next six entries are the second row, etc.). If unknown, assign NaN value to first element in the array.
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
  time_usec = (long)dis.readLong();
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
  byte[] buffer = new byte[12+181];
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
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 181);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[191] = crcl;
  buffer[192] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_GLOBAL_POSITION_INT_COV : " +   "  time_usec="+time_usec
+  "  lat="+lat
+  "  lon="+lon
+  "  alt="+alt
+  "  relative_alt="+relative_alt
+  "  vx="+vx
+  "  vy="+vy
+  "  vz="+vz
+  "  covariance[0]="+covariance[0]
+  "  covariance[1]="+covariance[1]
+  "  covariance[2]="+covariance[2]
+  "  covariance[3]="+covariance[3]
+  "  covariance[4]="+covariance[4]
+  "  covariance[5]="+covariance[5]
+  "  covariance[6]="+covariance[6]
+  "  covariance[7]="+covariance[7]
+  "  covariance[8]="+covariance[8]
+  "  covariance[9]="+covariance[9]
+  "  covariance[10]="+covariance[10]
+  "  covariance[11]="+covariance[11]
+  "  covariance[12]="+covariance[12]
+  "  covariance[13]="+covariance[13]
+  "  covariance[14]="+covariance[14]
+  "  covariance[15]="+covariance[15]
+  "  covariance[16]="+covariance[16]
+  "  covariance[17]="+covariance[17]
+  "  covariance[18]="+covariance[18]
+  "  covariance[19]="+covariance[19]
+  "  covariance[20]="+covariance[20]
+  "  covariance[21]="+covariance[21]
+  "  covariance[22]="+covariance[22]
+  "  covariance[23]="+covariance[23]
+  "  covariance[24]="+covariance[24]
+  "  covariance[25]="+covariance[25]
+  "  covariance[26]="+covariance[26]
+  "  covariance[27]="+covariance[27]
+  "  covariance[28]="+covariance[28]
+  "  covariance[29]="+covariance[29]
+  "  covariance[30]="+covariance[30]
+  "  covariance[31]="+covariance[31]
+  "  covariance[32]="+covariance[32]
+  "  covariance[33]="+covariance[33]
+  "  covariance[34]="+covariance[34]
+  "  covariance[35]="+covariance[35]
+  "  estimator_type="+estimator_type
;}
}
