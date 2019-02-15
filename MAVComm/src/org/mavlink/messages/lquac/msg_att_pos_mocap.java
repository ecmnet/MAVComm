/**
 * Generated class : msg_att_pos_mocap
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
 * Class msg_att_pos_mocap
 * Motion capture attitude and position
 **/
public class msg_att_pos_mocap extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_ATT_POS_MOCAP = 138;
  private static final long serialVersionUID = MAVLINK_MSG_ID_ATT_POS_MOCAP;
  public msg_att_pos_mocap() {
    this(1,1);
}
  public msg_att_pos_mocap(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_ATT_POS_MOCAP;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 120;
}

  /**
   * Timestamp (UNIX Epoch time or time since system boot). The receiving end can infer timestamp format (since 1.1.1970 or since system boot) by checking for the magnitude the number.
   */
  public long time_usec;
  /**
   * Attitude quaternion (w, x, y, z order, zero-rotation is 1, 0, 0, 0)
   */
  public float[] q = new float[4];
  /**
   * X position (NED)
   */
  public float x;
  /**
   * Y position (NED)
   */
  public float y;
  /**
   * Z position (NED)
   */
  public float z;
  /**
   * Row-major representation of a pose 6x6 cross-covariance matrix upper right triangle (states: x, y, z, roll, pitch, yaw; first six entries are the first ROW, next five entries are the second ROW, etc.). If unknown, assign NaN value to first element in the array.
   */
  public float[] covariance = new float[21];
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  for (int i=0; i<4; i++) {
    q[i] = (float)dis.readFloat();
  }
  x = (float)dis.readFloat();
  y = (float)dis.readFloat();
  z = (float)dis.readFloat();
  for (int i=0; i<21; i++) {
    covariance[i] = (float)dis.readFloat();
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+120];
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
  dos.writeFloat(x);
  dos.writeFloat(y);
  dos.writeFloat(z);
  for (int i=0; i<21; i++) {
    dos.writeFloat(covariance[i]);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 120);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[130] = crcl;
  buffer[131] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_ATT_POS_MOCAP : " +   "  time_usec="+time_usec
+  "  q[0]="+q[0]
+  "  q[1]="+q[1]
+  "  q[2]="+q[2]
+  "  q[3]="+q[3]
+  "  x="+x
+  "  y="+y
+  "  z="+z
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
;}
}
