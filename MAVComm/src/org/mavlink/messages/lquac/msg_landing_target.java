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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/**
 * Class msg_landing_target
 * The location of a landing area captured from a downward facing camera
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
    length = 30;
}

  /**
   * Timestamp (micros since boot or Unix epoch)
   */
  public long time_usec;
  /**
   * X-axis angular offset (in radians) of the target from the center of the image
   */
  public float angle_x;
  /**
   * Y-axis angular offset (in radians) of the target from the center of the image
   */
  public float angle_y;
  /**
   * Distance to the target from the vehicle in meters
   */
  public float distance;
  /**
   * Size in radians of target along x-axis
   */
  public float size_x;
  /**
   * Size in radians of target along y-axis
   */
  public float size_y;
  /**
   * The ID of the target if multiple targets are present
   */
  public int target_num;
  /**
   * MAV_FRAME enum specifying the whether the following feilds are earth-frame, body-frame, etc.
   */
  public int frame;
/**
 * Decode message with raw data
 */
public void decode(ByteBuffer dis) throws IOException {
  time_usec = (long)dis.getLong();
  angle_x = (float)dis.getFloat();
  angle_y = (float)dis.getFloat();
  distance = (float)dis.getFloat();
  size_x = (float)dis.getFloat();
  size_y = (float)dis.getFloat();
  target_num = (int)dis.get()&0x00FF;
  frame = (int)dis.get()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+30];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.putLong(time_usec);
  dos.putFloat(angle_x);
  dos.putFloat(angle_y);
  dos.putFloat(distance);
  dos.putFloat(size_x);
  dos.putFloat(size_y);
  dos.put((byte)(target_num&0x00FF));
  dos.put((byte)(frame&0x00FF));
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 30);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[36] = crcl;
  buffer[37] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_LANDING_TARGET : " +   "  time_usec="+time_usec+  "  angle_x="+angle_x+  "  angle_y="+angle_y+  "  distance="+distance+  "  size_x="+size_x+  "  size_y="+size_y+  "  target_num="+target_num+  "  frame="+frame;}
}
