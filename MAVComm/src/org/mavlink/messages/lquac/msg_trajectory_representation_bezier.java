/**
 * Generated class : msg_trajectory_representation_bezier
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
 * Class msg_trajectory_representation_bezier
 * Describe a trajectory using an array of up-to 5 bezier points in the local frame.
 **/
public class msg_trajectory_representation_bezier extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_TRAJECTORY_REPRESENTATION_BEZIER = 333;
  private static final long serialVersionUID = MAVLINK_MSG_ID_TRAJECTORY_REPRESENTATION_BEZIER;
  public msg_trajectory_representation_bezier() {
    this(1,1);
}
  public msg_trajectory_representation_bezier(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_TRAJECTORY_REPRESENTATION_BEZIER;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 109;
}

  /**
   * Timestamp (UNIX Epoch time or time since system boot). The receiving end can infer timestamp format (since 1.1.1970 or since system boot) by checking for the magnitude the number.
   */
  public long time_usec;
  /**
   * X-coordinate of starting bezier point, set to NaN if not being used
   */
  public float[] pos_x = new float[5];
  /**
   * Y-coordinate of starting bezier point, set to NaN if not being used
   */
  public float[] pos_y = new float[5];
  /**
   * Z-coordinate of starting bezier point, set to NaN if not being used
   */
  public float[] pos_z = new float[5];
  /**
   * Bezier time horizon, set to NaN if velocity/acceleration should not be incorporated
   */
  public float[] delta = new float[5];
  /**
   * Yaw, set to NaN for unchanged
   */
  public float[] pos_yaw = new float[5];
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
    delta[i] = (float)dis.readFloat();
  }
  for (int i=0; i<5; i++) {
    pos_yaw[i] = (float)dis.readFloat();
  }
  valid_points = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+109];
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
    dos.writeFloat(delta[i]);
  }
  for (int i=0; i<5; i++) {
    dos.writeFloat(pos_yaw[i]);
  }
  dos.writeByte(valid_points&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 109);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[119] = crcl;
  buffer[120] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_TRAJECTORY_REPRESENTATION_BEZIER : " +   "  time_usec="+time_usec+  "  pos_x="+pos_x+  "  pos_y="+pos_y+  "  pos_z="+pos_z+  "  delta="+delta+  "  pos_yaw="+pos_yaw+  "  valid_points="+valid_points;}
}
