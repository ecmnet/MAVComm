/**
 * Generated class : msg_vision_position_estimate
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
 * Class msg_vision_position_estimate
 * Global position/attitude estimate from a vision source.
 **/
public class msg_vision_position_estimate extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_VISION_POSITION_ESTIMATE = 102;
  private static final long serialVersionUID = MAVLINK_MSG_ID_VISION_POSITION_ESTIMATE;
  public msg_vision_position_estimate() {
    this(1,1);
}
  public msg_vision_position_estimate(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_VISION_POSITION_ESTIMATE;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 117;
}

  /**
   * Timestamp (UNIX time or time since system boot)
   */
  public long usec;
  /**
   * Global X position
   */
  public float x;
  /**
   * Global Y position
   */
  public float y;
  /**
   * Global Z position
   */
  public float z;
  /**
   * Roll angle
   */
  public float roll;
  /**
   * Pitch angle
   */
  public float pitch;
  /**
   * Yaw angle
   */
  public float yaw;
  /**
   * Row-major representation of pose 6x6 cross-covariance matrix upper right triangle (states: x, y, z, roll, pitch, yaw; first six entries are the first ROW, next five entries are the second ROW, etc.). If unknown, assign NaN value to first element in the array.
   */
  public float[] covariance = new float[21];
  /**
   * Estimate reset counter. This should be incremented when the estimate resets in any of the dimensions (position, velocity, attitude, angular speed). This is designed to be used when e.g an external SLAM system detects a loop-closure and the estimate jumps.
   */
  public int reset_counter;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  usec = (long)dis.readLong();
  x = (float)dis.readFloat();
  y = (float)dis.readFloat();
  z = (float)dis.readFloat();
  roll = (float)dis.readFloat();
  pitch = (float)dis.readFloat();
  yaw = (float)dis.readFloat();
  for (int i=0; i<21; i++) {
    covariance[i] = (float)dis.readFloat();
  }
  reset_counter = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+117];
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
  dos.writeLong(usec);
  dos.writeFloat(x);
  dos.writeFloat(y);
  dos.writeFloat(z);
  dos.writeFloat(roll);
  dos.writeFloat(pitch);
  dos.writeFloat(yaw);
  for (int i=0; i<21; i++) {
    dos.writeFloat(covariance[i]);
  }
  dos.writeByte(reset_counter&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 117);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[127] = crcl;
  buffer[128] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_VISION_POSITION_ESTIMATE : " +   "  usec="+usec
+  "  x="+x
+  "  y="+y
+  "  z="+z
+  "  roll="+roll
+  "  pitch="+pitch
+  "  yaw="+yaw
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
+  "  reset_counter="+reset_counter
;}
}
