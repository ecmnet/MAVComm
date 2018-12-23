/**
 * Generated class : msg_landing_target
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
 * Class msg_landing_target
 * The location of a landing target. See: https://mavlink.io/en/services/landing_target.html
 **/
public class msg_landing_target extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_LANDING_TARGET = 149;
  private static final long serialVersionUID = MAVLINK_MSG_ID_LANDING_TARGET;
  public msg_landing_target() {
    this(1,1);
}
  public msg_landing_target(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_LANDING_TARGET;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 60;
}

  /**
   * Timestamp (UNIX Epoch time or time since system boot). The receiving end can infer timestamp format (since 1.1.1970 or since system boot) by checking for the magnitude the number.
   */
  public long time_usec;
  /**
   * X-axis angular offset of the target from the center of the image
   */
  public float angle_x;
  /**
   * Y-axis angular offset of the target from the center of the image
   */
  public float angle_y;
  /**
   * Distance to the target from the vehicle
   */
  public float distance;
  /**
   * Size of target along x-axis
   */
  public float size_x;
  /**
   * Size of target along y-axis
   */
  public float size_y;
  /**
   * The ID of the target if multiple targets are present
   */
  public int target_num;
  /**
   * Coordinate frame used for following fields.
   */
  public int frame;
  /**
   * X Position of the landing target in MAV_FRAME
   */
  public float x;
  /**
   * Y Position of the landing target in MAV_FRAME
   */
  public float y;
  /**
   * Z Position of the landing target in MAV_FRAME
   */
  public float z;
  /**
   * Quaternion of landing target orientation (w, x, y, z order, zero-rotation is 1, 0, 0, 0)
   */
  public float[] q = new float[4];
  /**
   * Type of landing target
   */
  public int type;
  /**
   * Boolean indicating whether the position fields (x, y, z, q, type) contain valid target position information (valid: 1, invalid: 0). Default is 0 (invalid).
   */
  public int position_valid;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  angle_x = (float)dis.readFloat();
  angle_y = (float)dis.readFloat();
  distance = (float)dis.readFloat();
  size_x = (float)dis.readFloat();
  size_y = (float)dis.readFloat();
  target_num = (int)dis.readUnsignedByte()&0x00FF;
  frame = (int)dis.readUnsignedByte()&0x00FF;
  x = (float)dis.readFloat();
  y = (float)dis.readFloat();
  z = (float)dis.readFloat();
  for (int i=0; i<4; i++) {
    q[i] = (float)dis.readFloat();
  }
  type = (int)dis.readUnsignedByte()&0x00FF;
  position_valid = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+60];
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
  dos.writeFloat(angle_x);
  dos.writeFloat(angle_y);
  dos.writeFloat(distance);
  dos.writeFloat(size_x);
  dos.writeFloat(size_y);
  dos.writeByte(target_num&0x00FF);
  dos.writeByte(frame&0x00FF);
  dos.writeFloat(x);
  dos.writeFloat(y);
  dos.writeFloat(z);
  for (int i=0; i<4; i++) {
    dos.writeFloat(q[i]);
  }
  dos.writeByte(type&0x00FF);
  dos.writeByte(position_valid&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 60);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[70] = crcl;
  buffer[71] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_LANDING_TARGET : " +   "  time_usec="+time_usec
+  "  angle_x="+angle_x
+  "  angle_y="+angle_y
+  "  distance="+distance
+  "  size_x="+size_x
+  "  size_y="+size_y
+  "  target_num="+target_num
+  "  frame="+frame
+  "  x="+x
+  "  y="+y
+  "  z="+z
+  "  q[0]="+q[0]
+  "  q[1]="+q[1]
+  "  q[2]="+q[2]
+  "  q[3]="+q[3]
+  "  type="+type
+  "  position_valid="+position_valid
;}
}
