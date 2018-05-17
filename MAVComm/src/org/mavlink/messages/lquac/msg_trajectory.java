/**
 * Generated class : msg_trajectory
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
 * Class msg_trajectory
 * WORK IN PROGRESS! DO NOT DEPLOY! Message to describe a trajectory in the local frame. Supported trajectory types are enumerated in MAV_TRAJECTORY_REPRESENTATION
 **/
public class msg_trajectory extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_TRAJECTORY = 332;
  private static final long serialVersionUID = MAVLINK_MSG_ID_TRAJECTORY;
  public msg_trajectory() {
    this(1,1);
}
  public msg_trajectory(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_TRAJECTORY;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 234;
}

  /**
   * Timestamp (microseconds since system boot or since UNIX epoch).
   */
  public long time_usec;
  /**
   * Depending on the type (see MAV_TRAJECTORY_REPRESENTATION)
   */
  public float[] point_1 = new float[11];
  /**
   * Depending on the type (see MAV_TRAJECTORY_REPRESENTATION)
   */
  public float[] point_2 = new float[11];
  /**
   * Depending on the type (see MAV_TRAJECTORY_REPRESENTATION)
   */
  public float[] point_3 = new float[11];
  /**
   * Depending on the type (see MAV_TRAJECTORY_REPRESENTATION)
   */
  public float[] point_4 = new float[11];
  /**
   * Depending on the type (see MAV_TRAJECTORY_REPRESENTATION)
   */
  public float[] point_5 = new float[11];
  /**
   * Waypoints, Bezier etc. see MAV_TRAJECTORY_REPRESENTATION
   */
  public int type;
  /**
   * States if respective point is valid (boolean)
   */
  public int[] point_valid = new int[5];
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  for (int i=0; i<11; i++) {
    point_1[i] = (float)dis.readFloat();
  }
  for (int i=0; i<11; i++) {
    point_2[i] = (float)dis.readFloat();
  }
  for (int i=0; i<11; i++) {
    point_3[i] = (float)dis.readFloat();
  }
  for (int i=0; i<11; i++) {
    point_4[i] = (float)dis.readFloat();
  }
  for (int i=0; i<11; i++) {
    point_5[i] = (float)dis.readFloat();
  }
  type = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<5; i++) {
    point_valid[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+234];
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
  for (int i=0; i<11; i++) {
    dos.writeFloat(point_1[i]);
  }
  for (int i=0; i<11; i++) {
    dos.writeFloat(point_2[i]);
  }
  for (int i=0; i<11; i++) {
    dos.writeFloat(point_3[i]);
  }
  for (int i=0; i<11; i++) {
    dos.writeFloat(point_4[i]);
  }
  for (int i=0; i<11; i++) {
    dos.writeFloat(point_5[i]);
  }
  dos.writeByte(type&0x00FF);
  for (int i=0; i<5; i++) {
    dos.writeByte(point_valid[i]&0x00FF);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 234);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[244] = crcl;
  buffer[245] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_TRAJECTORY : " +   "  time_usec="+time_usec+  "  point_1="+point_1+  "  point_2="+point_2+  "  point_3="+point_3+  "  point_4="+point_4+  "  point_5="+point_5+  "  type="+type+  "  point_valid="+point_valid;}
}
