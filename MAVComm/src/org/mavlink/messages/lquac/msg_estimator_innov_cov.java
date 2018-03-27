/**
 * Generated class : msg_estimator_innov_cov
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
 * Class msg_estimator_innov_cov
 * An estimator agnostic innovation covariance message. Note: Very large, not meant for use over radio.
 **/
public class msg_estimator_innov_cov extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_ESTIMATOR_INNOV_COV = 340;
  private static final long serialVersionUID = MAVLINK_MSG_ID_ESTIMATOR_INNOV_COV;
  public msg_estimator_innov_cov() {
    this(1,1);
}
  public msg_estimator_innov_cov(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_ESTIMATOR_INNOV_COV;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 891;
}

  /**
   * Timestamp (microseconds since system boot or since UNIX epoch)
   */
  public long time_usec;
  /**
   * The estimator covariance matrix (upper triangle), horizontally stacked with lengths [n, n-1, n-2, ..., 1]
   */
  public float[] cov = new float[210];
  /**
   * Number of innovations, max 21, limited by mavlink protocol
   */
  public int n;
  /**
   * An array describing field type (see MAV_FIELD)
   */
  public int[] id = new int[21];
  /**
   * An array describing the sensor associated with the field (see MAV_SENSOR_TYPE)
   */
  public int[] sensor = new int[21];
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  for (int i=0; i<210; i++) {
    cov[i] = (float)dis.readFloat();
  }
  n = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<21; i++) {
    id[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
  for (int i=0; i<21; i++) {
    sensor[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+891];
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
  for (int i=0; i<210; i++) {
    dos.writeFloat(cov[i]);
  }
  dos.writeByte(n&0x00FF);
  for (int i=0; i<21; i++) {
    dos.writeByte(id[i]&0x00FF);
  }
  for (int i=0; i<21; i++) {
    dos.writeByte(sensor[i]&0x00FF);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 891);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[901] = crcl;
  buffer[902] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_ESTIMATOR_INNOV_COV : " +   "  time_usec="+time_usec+  "  cov="+cov+  "  n="+n+  "  id="+id+  "  sensor="+sensor;}
}
