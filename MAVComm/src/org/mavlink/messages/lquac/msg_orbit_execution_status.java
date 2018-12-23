/**
 * Generated class : msg_orbit_execution_status
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
 * Class msg_orbit_execution_status
 * Vehicle status report that is sent out while orbit execution is in progress (see MAV_CMD_DO_ORBIT).
 **/
public class msg_orbit_execution_status extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_ORBIT_EXECUTION_STATUS = 360;
  private static final long serialVersionUID = MAVLINK_MSG_ID_ORBIT_EXECUTION_STATUS;
  public msg_orbit_execution_status() {
    this(1,1);
}
  public msg_orbit_execution_status(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_ORBIT_EXECUTION_STATUS;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 25;
}

  /**
   * Timestamp (UNIX Epoch time or time since system boot). The receiving end can infer timestamp format (since 1.1.1970 or since system boot) by checking for the magnitude the number.
   */
  public long time_usec;
  /**
   * Radius of the orbit circle. Positive values orbit clockwise, negative values orbit counter-clockwise.
   */
  public float radius;
  /**
   * X coordinate of center point. Coordinate system depends on frame field: local = x position in meters * 1e4, global = latitude in degrees * 1e7.
   */
  public long x;
  /**
   * Y coordinate of center point.  Coordinate system depends on frame field: local = x position in meters * 1e4, global = latitude in degrees * 1e7.
   */
  public long y;
  /**
   * Altitude of center point. Coordinate system depends on frame field.
   */
  public float z;
  /**
   * The coordinate system of the fields: x, y, z.
   */
  public int frame;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  radius = (float)dis.readFloat();
  x = (int)dis.readInt();
  y = (int)dis.readInt();
  z = (float)dis.readFloat();
  frame = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+25];
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
  dos.writeFloat(radius);
  dos.writeInt((int)(x&0x00FFFFFFFF));
  dos.writeInt((int)(y&0x00FFFFFFFF));
  dos.writeFloat(z);
  dos.writeByte(frame&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 25);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[35] = crcl;
  buffer[36] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_ORBIT_EXECUTION_STATUS : " +   "  time_usec="+time_usec
+  "  radius="+radius
+  "  x="+x
+  "  y="+y
+  "  z="+z
+  "  frame="+frame
;}
}
