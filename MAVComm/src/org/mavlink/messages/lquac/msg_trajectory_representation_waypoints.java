/**
 * Generated class : msg_trajectory_representation_waypoints
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
    payload_length = 239;
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
   * Scheduled action for each waypoint, UINT16_MAX if not being used.
   */
  public int[] command = new int[5];
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
  for (int i=0; i<5; i++) {
    command[i] = (int)dis.readUnsignedShort()&0x00FFFF;
  }
  valid_points = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+239];
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
  for (int i=0; i<5; i++) {
    dos.writeShort(command[i]&0x00FFFF);
  }
  dos.writeByte(valid_points&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 239);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[249] = crcl;
  buffer[250] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_TRAJECTORY_REPRESENTATION_WAYPOINTS : " +   "  time_usec="+time_usec
+  "  pos_x[0]="+pos_x[0]
+  "  pos_x[1]="+pos_x[1]
+  "  pos_x[2]="+pos_x[2]
+  "  pos_x[3]="+pos_x[3]
+  "  pos_x[4]="+pos_x[4]
+  "  pos_y[0]="+pos_y[0]
+  "  pos_y[1]="+pos_y[1]
+  "  pos_y[2]="+pos_y[2]
+  "  pos_y[3]="+pos_y[3]
+  "  pos_y[4]="+pos_y[4]
+  "  pos_z[0]="+pos_z[0]
+  "  pos_z[1]="+pos_z[1]
+  "  pos_z[2]="+pos_z[2]
+  "  pos_z[3]="+pos_z[3]
+  "  pos_z[4]="+pos_z[4]
+  "  vel_x[0]="+vel_x[0]
+  "  vel_x[1]="+vel_x[1]
+  "  vel_x[2]="+vel_x[2]
+  "  vel_x[3]="+vel_x[3]
+  "  vel_x[4]="+vel_x[4]
+  "  vel_y[0]="+vel_y[0]
+  "  vel_y[1]="+vel_y[1]
+  "  vel_y[2]="+vel_y[2]
+  "  vel_y[3]="+vel_y[3]
+  "  vel_y[4]="+vel_y[4]
+  "  vel_z[0]="+vel_z[0]
+  "  vel_z[1]="+vel_z[1]
+  "  vel_z[2]="+vel_z[2]
+  "  vel_z[3]="+vel_z[3]
+  "  vel_z[4]="+vel_z[4]
+  "  acc_x[0]="+acc_x[0]
+  "  acc_x[1]="+acc_x[1]
+  "  acc_x[2]="+acc_x[2]
+  "  acc_x[3]="+acc_x[3]
+  "  acc_x[4]="+acc_x[4]
+  "  acc_y[0]="+acc_y[0]
+  "  acc_y[1]="+acc_y[1]
+  "  acc_y[2]="+acc_y[2]
+  "  acc_y[3]="+acc_y[3]
+  "  acc_y[4]="+acc_y[4]
+  "  acc_z[0]="+acc_z[0]
+  "  acc_z[1]="+acc_z[1]
+  "  acc_z[2]="+acc_z[2]
+  "  acc_z[3]="+acc_z[3]
+  "  acc_z[4]="+acc_z[4]
+  "  pos_yaw[0]="+pos_yaw[0]
+  "  pos_yaw[1]="+pos_yaw[1]
+  "  pos_yaw[2]="+pos_yaw[2]
+  "  pos_yaw[3]="+pos_yaw[3]
+  "  pos_yaw[4]="+pos_yaw[4]
+  "  vel_yaw[0]="+vel_yaw[0]
+  "  vel_yaw[1]="+vel_yaw[1]
+  "  vel_yaw[2]="+vel_yaw[2]
+  "  vel_yaw[3]="+vel_yaw[3]
+  "  vel_yaw[4]="+vel_yaw[4]
+  "  command[0]="+command[0]
+  "  command[1]="+command[1]
+  "  command[2]="+command[2]
+  "  command[3]="+command[3]
+  "  command[4]="+command[4]
+  "  valid_points="+valid_points
;}
}
