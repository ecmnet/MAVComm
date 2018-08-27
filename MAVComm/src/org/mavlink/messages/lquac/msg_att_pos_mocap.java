/**
 * Generated class : msg_att_pos_mocap
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
   * Pose covariance matrix upper right triangular (first six entries are the first ROW, next five entries are the second ROW, etc.)
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
return "MAVLINK_MSG_ID_ATT_POS_MOCAP : " +   "  time_usec="+time_usec+  "  q="+q+  "  x="+x+  "  y="+y+  "  z="+z+  "  covariance="+covariance;}
}
