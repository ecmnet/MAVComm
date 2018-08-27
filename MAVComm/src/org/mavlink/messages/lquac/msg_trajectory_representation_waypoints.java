/**
 * Generated class : msg_trajectory_representation_waypoints
 * DO NOT MODIFY!
 **/
package org.mavlink.messages.lquac;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.mavlink.IMAVLinkCRC;
import org.mavlink.MAVLinkCRC;
import org.mavlink.io.LittleEndianDataInputStream;
import org.mavlink.io.LittleEndianDataOutputStream;
import org.mavlink.messages.MAVLinkMessage;
/**
 * Class msg_trajectory_representation_waypoints
 * Describe a trajectory using an array of up-to 5 waypoints in the local frame.
 **/
public class msg_trajectory_representation_waypoints extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_TRAJECTORY_REPRESENTATION_WAYPOINTS = 332;
  private static final long serialVersionUID = MAVLINK_MSG_ID_TRAJECTORY_REPRESENTATION_WAYPOINTS;
  public msg_trajectory_representation_waypoints() {
    this(1,1);
}
  public msg_trajectory_representation_waypoints(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_TRAJECTORY_REPRESENTATION_WAYPOINTS;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 229;
}

  /**
   * Timestamp (UNIX Epoch time or time since system boot). The receiving end can infer timestamp format (since 1.1.1970 or since system boot) by checking for the magnitude the number.
   */
  public long time_usec;
  /**
   * X-coordinate of waypoint, set to NaN if not being used
   */
  public float[] pos_x = new float[5];
  /**
   * Y-coordinate of waypoint, set to NaN if not being used
   */
  public float[] pos_y = new float[5];
  /**
   * Z-coordinate of waypoint, set to NaN if not being used
   */
  public float[] pos_z = new float[5];
  /**
   * X-velocity of waypoint, set to NaN if not being used
   */
  public float[] vel_x = new float[5];
  /**
   * Y-velocity of waypoint, set to NaN if not being used
   */
  public float[] vel_y = new float[5];
  /**
   * Z-velocity of waypoint, set to NaN if not being used
   */
  public float[] vel_z = new float[5];
  /**
   * X-acceleration of waypoint, set to NaN if not being used
   */
  public float[] acc_x = new float[5];
  /**
   * Y-acceleration of waypoint, set to NaN if not being used
   */
  public float[] acc_y = new float[5];
  /**
   * Z-acceleration of waypoint, set to NaN if not being used
   */
  public float[] acc_z = new float[5];
  /**
   * Yaw angle, set to NaN if not being used
   */
  public float[] pos_yaw = new float[5];
  /**
   * Yaw rate, set to NaN if not being used
   */
  public float[] vel_yaw = new float[5];
  /**
   * Number of valid points (up-to 5 waypoints are possible)
   */
  public int valid_points;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  for (int i=0; i<5; i++) {
    pos_x[i] = (float)dis.readFloat();
  }
  for (int i=0; i<5; i++) {
    pos_y[i] = (float)dis.readFloat();
  }
  for (int i=0; i<5; i++) {
    pos_z[i] = (float)dis.readFloat();
  }
  for (int i=0; i<5; i++) {
    vel_x[i] = (float)dis.readFloat();
  }
  for (int i=0; i<5; i++) {
    vel_y[i] = (float)dis.readFloat();
  }
  for (int i=0; i<5; i++) {
    vel_z[i] = (float)dis.readFloat();
  }
  for (int i=0; i<5; i++) {
    acc_x[i] = (float)dis.readFloat();
  }
  for (int i=0; i<5; i++) {
    acc_y[i] = (float)dis.readFloat();
  }
  for (int i=0; i<5; i++) {
    acc_z[i] = (float)dis.readFloat();
  }
  for (int i=0; i<5; i++) {
    pos_yaw[i] = (float)dis.readFloat();
  }
  for (int i=0; i<5; i++) {
    vel_yaw[i] = (float)dis.readFloat();
  }
  valid_points = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+229];
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
  for (int i=0; i<5; i++) {
    dos.writeFloat(pos_x[i]);
  }
  for (int i=0; i<5; i++) {
    dos.writeFloat(pos_y[i]);
  }
  for (int i=0; i<5; i++) {
    dos.writeFloat(pos_z[i]);
  }
  for (int i=0; i<5; i++) {
    dos.writeFloat(vel_x[i]);
  }
  for (int i=0; i<5; i++) {
    dos.writeFloat(vel_y[i]);
  }
  for (int i=0; i<5; i++) {
    dos.writeFloat(vel_z[i]);
  }
  for (int i=0; i<5; i++) {
    dos.writeFloat(acc_x[i]);
  }
  for (int i=0; i<5; i++) {
    dos.writeFloat(acc_y[i]);
  }
  for (int i=0; i<5; i++) {
    dos.writeFloat(acc_z[i]);
  }
  for (int i=0; i<5; i++) {
    dos.writeFloat(pos_yaw[i]);
  }
  for (int i=0; i<5; i++) {
    dos.writeFloat(vel_yaw[i]);
  }
  dos.writeByte(valid_points&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 229);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[239] = crcl;
  buffer[240] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_TRAJECTORY_REPRESENTATION_WAYPOINTS : " +   "  time_usec="+time_usec+  "  pos_x="+pos_x+  "  pos_y="+pos_y+  "  pos_z="+pos_z+  "  vel_x="+vel_x+  "  vel_y="+vel_y+  "  vel_z="+vel_z+  "  acc_x="+acc_x+  "  acc_y="+acc_y+  "  acc_z="+acc_z+  "  pos_yaw="+pos_yaw+  "  vel_yaw="+vel_yaw+  "  valid_points="+valid_points;}
}
