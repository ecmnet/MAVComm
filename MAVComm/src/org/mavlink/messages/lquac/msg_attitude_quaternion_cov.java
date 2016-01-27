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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
    length = 68;
}

  /**
   * Timestamp (milliseconds since system boot)
   */
  public long time_boot_ms;
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
public void decode(ByteBuffer dis) throws IOException {
  time_boot_ms = (int)dis.getInt()&0x00FFFFFFFF;
  for (int i=0; i<4; i++) {
    q[i] = (float)dis.getFloat();
  }
  rollspeed = (float)dis.getFloat();
  pitchspeed = (float)dis.getFloat();
  yawspeed = (float)dis.getFloat();
  for (int i=0; i<9; i++) {
    covariance[i] = (float)dis.getFloat();
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+68];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.putInt((int)(time_boot_ms&0x00FFFFFFFF));
  for (int i=0; i<4; i++) {
    dos.putFloat(q[i]);
  }
  dos.putFloat(rollspeed);
  dos.putFloat(pitchspeed);
  dos.putFloat(yawspeed);
  for (int i=0; i<9; i++) {
    dos.putFloat(covariance[i]);
  }
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 68);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[74] = crcl;
  buffer[75] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_ATTITUDE_QUATERNION_COV : " +   "  time_boot_ms="+time_boot_ms+  "  q="+q+  "  rollspeed="+rollspeed+  "  pitchspeed="+pitchspeed+  "  yawspeed="+yawspeed+  "  covariance="+covariance;}
}
