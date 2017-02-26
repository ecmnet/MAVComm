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
import org.mavlink.io.LittleEndianDataInputStream;
import org.mavlink.io.LittleEndianDataOutputStream;
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
    payload_length = 100;
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
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  x_acc = (float)dis.readFloat();
  y_acc = (float)dis.readFloat();
  z_acc = (float)dis.readFloat();
  x_vel = (float)dis.readFloat();
  y_vel = (float)dis.readFloat();
  z_vel = (float)dis.readFloat();
  x_pos = (float)dis.readFloat();
  y_pos = (float)dis.readFloat();
  z_pos = (float)dis.readFloat();
  airspeed = (float)dis.readFloat();
  for (int i=0; i<3; i++) {
    vel_variance[i] = (float)dis.readFloat();
  }
  for (int i=0; i<3; i++) {
    pos_variance[i] = (float)dis.readFloat();
  }
  for (int i=0; i<4; i++) {
    q[i] = (float)dis.readFloat();
  }
  roll_rate = (float)dis.readFloat();
  pitch_rate = (float)dis.readFloat();
  yaw_rate = (float)dis.readFloat();
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+100];
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
  dos.writeFloat(x_acc);
  dos.writeFloat(y_acc);
  dos.writeFloat(z_acc);
  dos.writeFloat(x_vel);
  dos.writeFloat(y_vel);
  dos.writeFloat(z_vel);
  dos.writeFloat(x_pos);
  dos.writeFloat(y_pos);
  dos.writeFloat(z_pos);
  dos.writeFloat(airspeed);
  for (int i=0; i<3; i++) {
    dos.writeFloat(vel_variance[i]);
  }
  for (int i=0; i<3; i++) {
    dos.writeFloat(pos_variance[i]);
  }
  for (int i=0; i<4; i++) {
    dos.writeFloat(q[i]);
  }
  dos.writeFloat(roll_rate);
  dos.writeFloat(pitch_rate);
  dos.writeFloat(yaw_rate);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 100);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[110] = crcl;
  buffer[111] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_CONTROL_SYSTEM_STATE : " +   "  time_usec="+time_usec+  "  x_acc="+x_acc+  "  y_acc="+y_acc+  "  z_acc="+z_acc+  "  x_vel="+x_vel+  "  y_vel="+y_vel+  "  z_vel="+z_vel+  "  x_pos="+x_pos+  "  y_pos="+y_pos+  "  z_pos="+z_pos+  "  airspeed="+airspeed+  "  vel_variance="+vel_variance+  "  pos_variance="+pos_variance+  "  q="+q+  "  roll_rate="+roll_rate+  "  pitch_rate="+pitch_rate+  "  yaw_rate="+yaw_rate;}
}
