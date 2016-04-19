/**
 * Generated class : msg_control_system_state
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
 * Class msg_control_system_state
 * The smoothed, monotonic system state used to feed the control loops of the system.
 **/
public class msg_control_system_state extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_CONTROL_SYSTEM_STATE = 146;
  private static final long serialVersionUID = MAVLINK_MSG_ID_CONTROL_SYSTEM_STATE;
  public msg_control_system_state() {
    this(1,1);
}
  public msg_control_system_state(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_CONTROL_SYSTEM_STATE;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 100;
}

  /**
   * Timestamp (micros since boot or Unix epoch)
   */
  public long time_usec;
  /**
   * X acceleration in body frame
   */
  public float x_acc;
  /**
   * Y acceleration in body frame
   */
  public float y_acc;
  /**
   * Z acceleration in body frame
   */
  public float z_acc;
  /**
   * X velocity in body frame
   */
  public float x_vel;
  /**
   * Y velocity in body frame
   */
  public float y_vel;
  /**
   * Z velocity in body frame
   */
  public float z_vel;
  /**
   * X position in local frame
   */
  public float x_pos;
  /**
   * Y position in local frame
   */
  public float y_pos;
  /**
   * Z position in local frame
   */
  public float z_pos;
  /**
   * Airspeed, set to -1 if unknown
   */
  public float airspeed;
  /**
   * Variance of body velocity estimate
   */
  public float[] vel_variance = new float[3];
  /**
   * Variance in local position
   */
  public float[] pos_variance = new float[3];
  /**
   * The attitude, represented as Quaternion
   */
  public float[] q = new float[4];
  /**
   * Angular rate in roll axis
   */
  public float roll_rate;
  /**
   * Angular rate in pitch axis
   */
  public float pitch_rate;
  /**
   * Angular rate in yaw axis
   */
  public float yaw_rate;
/**
 * Decode message with raw data
 */
public void decode(ByteBuffer dis) throws IOException {
  time_usec = (long)dis.getLong();
  x_acc = (float)dis.getFloat();
  y_acc = (float)dis.getFloat();
  z_acc = (float)dis.getFloat();
  x_vel = (float)dis.getFloat();
  y_vel = (float)dis.getFloat();
  z_vel = (float)dis.getFloat();
  x_pos = (float)dis.getFloat();
  y_pos = (float)dis.getFloat();
  z_pos = (float)dis.getFloat();
  airspeed = (float)dis.getFloat();
  for (int i=0; i<3; i++) {
    vel_variance[i] = (float)dis.getFloat();
  }
  for (int i=0; i<3; i++) {
    pos_variance[i] = (float)dis.getFloat();
  }
  for (int i=0; i<4; i++) {
    q[i] = (float)dis.getFloat();
  }
  roll_rate = (float)dis.getFloat();
  pitch_rate = (float)dis.getFloat();
  yaw_rate = (float)dis.getFloat();
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+100];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.putLong(time_usec);
  dos.putFloat(x_acc);
  dos.putFloat(y_acc);
  dos.putFloat(z_acc);
  dos.putFloat(x_vel);
  dos.putFloat(y_vel);
  dos.putFloat(z_vel);
  dos.putFloat(x_pos);
  dos.putFloat(y_pos);
  dos.putFloat(z_pos);
  dos.putFloat(airspeed);
  for (int i=0; i<3; i++) {
    dos.putFloat(vel_variance[i]);
  }
  for (int i=0; i<3; i++) {
    dos.putFloat(pos_variance[i]);
  }
  for (int i=0; i<4; i++) {
    dos.putFloat(q[i]);
  }
  dos.putFloat(roll_rate);
  dos.putFloat(pitch_rate);
  dos.putFloat(yaw_rate);
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 100);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[106] = crcl;
  buffer[107] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_CONTROL_SYSTEM_STATE : " +   "  time_usec="+time_usec+  "  x_acc="+x_acc+  "  y_acc="+y_acc+  "  z_acc="+z_acc+  "  x_vel="+x_vel+  "  y_vel="+y_vel+  "  z_vel="+z_vel+  "  x_pos="+x_pos+  "  y_pos="+y_pos+  "  z_pos="+z_pos+  "  airspeed="+airspeed+  "  vel_variance="+vel_variance+  "  pos_variance="+pos_variance+  "  q="+q+  "  roll_rate="+roll_rate+  "  pitch_rate="+pitch_rate+  "  yaw_rate="+yaw_rate;}
}
