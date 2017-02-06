/**
 * Generated class : msg_attitude_quaternion_cov
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
 * Class msg_attitude_quaternion_cov
 * The attitude in the aeronautical frame (right-handed, Z-down, X-front, Y-right), expressed as quaternion. Quaternion order is w, x, y, z and a zero rotation would be expressed as (1 0 0 0).
 **/
public class msg_attitude_quaternion_cov extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_ATTITUDE_QUATERNION_COV = 61;
  private static final long serialVersionUID = MAVLINK_MSG_ID_ATTITUDE_QUATERNION_COV;
  public msg_attitude_quaternion_cov() {
    this(1,1);
}
  public msg_attitude_quaternion_cov(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_ATTITUDE_QUATERNION_COV;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 72;
}

  /**
   * Timestamp (microseconds since system boot or since UNIX epoch)
   */
  public long time_usec;
  /**
   * Quaternion components, w, x, y, z (1 0 0 0 is the null-rotation)
   */
  public float[] q = new float[4];
  /**
   * Roll angular speed (rad/s)
   */
  public float rollspeed;
  /**
   * Pitch angular speed (rad/s)
   */
  public float pitchspeed;
  /**
   * Yaw angular speed (rad/s)
   */
  public float yawspeed;
  /**
   * Attitude covariance
   */
  public float[] covariance = new float[9];
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  for (int i=0; i<4; i++) {
    q[i] = (float)dis.readFloat();
  }
  rollspeed = (float)dis.readFloat();
  pitchspeed = (float)dis.readFloat();
  yawspeed = (float)dis.readFloat();
  for (int i=0; i<9; i++) {
    covariance[i] = (float)dis.readFloat();
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+72];
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
  for (int i=0; i<4; i++) {
    dos.writeFloat(q[i]);
  }
  dos.writeFloat(rollspeed);
  dos.writeFloat(pitchspeed);
  dos.writeFloat(yawspeed);
  for (int i=0; i<9; i++) {
    dos.writeFloat(covariance[i]);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 72);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[82] = crcl;
  buffer[83] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_ATTITUDE_QUATERNION_COV : " +   "  time_usec="+time_usec+  "  q="+q+  "  rollspeed="+rollspeed+  "  pitchspeed="+pitchspeed+  "  yawspeed="+yawspeed+  "  covariance="+covariance;}
}
