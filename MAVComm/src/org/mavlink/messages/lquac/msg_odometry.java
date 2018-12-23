/**
 * Generated class : msg_odometry
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
 * Class msg_odometry
 * Odometry message to communicate odometry information with an external interface. Fits ROS REP 147 standard for aerial vehicles (http://www.ros.org/reps/rep-0147.html).
 **/
public class msg_odometry extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_ODOMETRY = 331;
  private static final long serialVersionUID = MAVLINK_MSG_ID_ODOMETRY;
  public msg_odometry() {
    this(1,1);
}
  public msg_odometry(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_ODOMETRY;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 230;
}

  /**
   * Timestamp (UNIX Epoch time or time since system boot). The receiving end can infer timestamp format (since 1.1.1970 or since system boot) by checking for the magnitude the number.
   */
  public long time_usec;
  /**
   * X Position
   */
  public float x;
  /**
   * Y Position
   */
  public float y;
  /**
   * Z Position
   */
  public float z;
  /**
   * Quaternion components, w, x, y, z (1 0 0 0 is the null-rotation)
   */
  public float[] q = new float[4];
  /**
   * X linear speed
   */
  public float vx;
  /**
   * Y linear speed
   */
  public float vy;
  /**
   * Z linear speed
   */
  public float vz;
  /**
   * Roll angular speed
   */
  public float rollspeed;
  /**
   * Pitch angular speed
   */
  public float pitchspeed;
  /**
   * Yaw angular speed
   */
  public float yawspeed;
  /**
   * Pose (states: x, y, z, roll, pitch, yaw) covariance matrix upper right triangle (first six entries are the first ROW, next five entries are the second ROW, etc.)
   */
  public float[] pose_covariance = new float[21];
  /**
   * Twist (states: vx, vy, vz, rollspeed, pitchspeed, yawspeed) covariance matrix upper right triangle (first six entries are the first ROW, next five entries are the second ROW, etc.)
   */
  public float[] twist_covariance = new float[21];
  /**
   * Coordinate frame of reference for the pose data.
   */
  public int frame_id;
  /**
   * Coordinate frame of reference for the velocity in free space (twist) data.
   */
  public int child_frame_id;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  x = (float)dis.readFloat();
  y = (float)dis.readFloat();
  z = (float)dis.readFloat();
  for (int i=0; i<4; i++) {
    q[i] = (float)dis.readFloat();
  }
  vx = (float)dis.readFloat();
  vy = (float)dis.readFloat();
  vz = (float)dis.readFloat();
  rollspeed = (float)dis.readFloat();
  pitchspeed = (float)dis.readFloat();
  yawspeed = (float)dis.readFloat();
  for (int i=0; i<21; i++) {
    pose_covariance[i] = (float)dis.readFloat();
  }
  for (int i=0; i<21; i++) {
    twist_covariance[i] = (float)dis.readFloat();
  }
  frame_id = (int)dis.readUnsignedByte()&0x00FF;
  child_frame_id = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+230];
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
  dos.writeFloat(x);
  dos.writeFloat(y);
  dos.writeFloat(z);
  for (int i=0; i<4; i++) {
    dos.writeFloat(q[i]);
  }
  dos.writeFloat(vx);
  dos.writeFloat(vy);
  dos.writeFloat(vz);
  dos.writeFloat(rollspeed);
  dos.writeFloat(pitchspeed);
  dos.writeFloat(yawspeed);
  for (int i=0; i<21; i++) {
    dos.writeFloat(pose_covariance[i]);
  }
  for (int i=0; i<21; i++) {
    dos.writeFloat(twist_covariance[i]);
  }
  dos.writeByte(frame_id&0x00FF);
  dos.writeByte(child_frame_id&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 230);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[240] = crcl;
  buffer[241] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_ODOMETRY : " +   "  time_usec="+time_usec
+  "  x="+x
+  "  y="+y
+  "  z="+z
+  "  q[0]="+q[0]
+  "  q[1]="+q[1]
+  "  q[2]="+q[2]
+  "  q[3]="+q[3]
+  "  vx="+vx
+  "  vy="+vy
+  "  vz="+vz
+  "  rollspeed="+rollspeed
+  "  pitchspeed="+pitchspeed
+  "  yawspeed="+yawspeed
+  "  pose_covariance[0]="+pose_covariance[0]
+  "  pose_covariance[1]="+pose_covariance[1]
+  "  pose_covariance[2]="+pose_covariance[2]
+  "  pose_covariance[3]="+pose_covariance[3]
+  "  pose_covariance[4]="+pose_covariance[4]
+  "  pose_covariance[5]="+pose_covariance[5]
+  "  pose_covariance[6]="+pose_covariance[6]
+  "  pose_covariance[7]="+pose_covariance[7]
+  "  pose_covariance[8]="+pose_covariance[8]
+  "  pose_covariance[9]="+pose_covariance[9]
+  "  pose_covariance[10]="+pose_covariance[10]
+  "  pose_covariance[11]="+pose_covariance[11]
+  "  pose_covariance[12]="+pose_covariance[12]
+  "  pose_covariance[13]="+pose_covariance[13]
+  "  pose_covariance[14]="+pose_covariance[14]
+  "  pose_covariance[15]="+pose_covariance[15]
+  "  pose_covariance[16]="+pose_covariance[16]
+  "  pose_covariance[17]="+pose_covariance[17]
+  "  pose_covariance[18]="+pose_covariance[18]
+  "  pose_covariance[19]="+pose_covariance[19]
+  "  pose_covariance[20]="+pose_covariance[20]
+  "  twist_covariance[0]="+twist_covariance[0]
+  "  twist_covariance[1]="+twist_covariance[1]
+  "  twist_covariance[2]="+twist_covariance[2]
+  "  twist_covariance[3]="+twist_covariance[3]
+  "  twist_covariance[4]="+twist_covariance[4]
+  "  twist_covariance[5]="+twist_covariance[5]
+  "  twist_covariance[6]="+twist_covariance[6]
+  "  twist_covariance[7]="+twist_covariance[7]
+  "  twist_covariance[8]="+twist_covariance[8]
+  "  twist_covariance[9]="+twist_covariance[9]
+  "  twist_covariance[10]="+twist_covariance[10]
+  "  twist_covariance[11]="+twist_covariance[11]
+  "  twist_covariance[12]="+twist_covariance[12]
+  "  twist_covariance[13]="+twist_covariance[13]
+  "  twist_covariance[14]="+twist_covariance[14]
+  "  twist_covariance[15]="+twist_covariance[15]
+  "  twist_covariance[16]="+twist_covariance[16]
+  "  twist_covariance[17]="+twist_covariance[17]
+  "  twist_covariance[18]="+twist_covariance[18]
+  "  twist_covariance[19]="+twist_covariance[19]
+  "  twist_covariance[20]="+twist_covariance[20]
+  "  frame_id="+frame_id
+  "  child_frame_id="+child_frame_id
;}
}
