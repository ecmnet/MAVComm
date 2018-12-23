/**
 * Generated class : msg_local_position_ned
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
 * Class msg_local_position_ned
 * The filtered local position (e.g. fused computer vision and accelerometers). Coordinate frame is right-handed, Z-axis down (aeronautical frame, NED / north-east-down convention)
 **/
public class msg_local_position_ned extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_LOCAL_POSITION_NED = 32;
  private static final long serialVersionUID = MAVLINK_MSG_ID_LOCAL_POSITION_NED;
  public msg_local_position_ned() {
    this(1,1);
}
  public msg_local_position_ned(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_LOCAL_POSITION_NED;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 28;
}

  /**
   * Timestamp (time since system boot).
   */
  public long time_boot_ms;
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
   * X Speed
   */
  public float vx;
  /**
   * Y Speed
   */
  public float vy;
  /**
   * Z Speed
   */
  public float vz;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_boot_ms = (int)dis.readInt()&0x00FFFFFFFF;
  x = (float)dis.readFloat();
  y = (float)dis.readFloat();
  z = (float)dis.readFloat();
  vx = (float)dis.readFloat();
  vy = (float)dis.readFloat();
  vz = (float)dis.readFloat();
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+28];
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
  dos.writeInt((int)(time_boot_ms&0x00FFFFFFFF));
  dos.writeFloat(x);
  dos.writeFloat(y);
  dos.writeFloat(z);
  dos.writeFloat(vx);
  dos.writeFloat(vy);
  dos.writeFloat(vz);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 28);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[38] = crcl;
  buffer[39] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_LOCAL_POSITION_NED : " +   "  time_boot_ms="+time_boot_ms
+  "  x="+x
+  "  y="+y
+  "  z="+z
+  "  vx="+vx
+  "  vy="+vy
+  "  vz="+vz
;}
}
