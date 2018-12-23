/**
 * Generated class : msg_safety_allowed_area
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
 * Class msg_safety_allowed_area
 * Read out the safety zone the MAV currently assumes.
 **/
public class msg_safety_allowed_area extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_SAFETY_ALLOWED_AREA = 55;
  private static final long serialVersionUID = MAVLINK_MSG_ID_SAFETY_ALLOWED_AREA;
  public msg_safety_allowed_area() {
    this(1,1);
}
  public msg_safety_allowed_area(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_SAFETY_ALLOWED_AREA;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 25;
}

  /**
   * x position 1 / Latitude 1
   */
  public float p1x;
  /**
   * y position 1 / Longitude 1
   */
  public float p1y;
  /**
   * z position 1 / Altitude 1
   */
  public float p1z;
  /**
   * x position 2 / Latitude 2
   */
  public float p2x;
  /**
   * y position 2 / Longitude 2
   */
  public float p2y;
  /**
   * z position 2 / Altitude 2
   */
  public float p2z;
  /**
   * Coordinate frame. Can be either global, GPS, right-handed with Z axis up or local, right handed, Z axis down.
   */
  public int frame;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  p1x = (float)dis.readFloat();
  p1y = (float)dis.readFloat();
  p1z = (float)dis.readFloat();
  p2x = (float)dis.readFloat();
  p2y = (float)dis.readFloat();
  p2z = (float)dis.readFloat();
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
  dos.writeFloat(p1x);
  dos.writeFloat(p1y);
  dos.writeFloat(p1z);
  dos.writeFloat(p2x);
  dos.writeFloat(p2y);
  dos.writeFloat(p2z);
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
return "MAVLINK_MSG_ID_SAFETY_ALLOWED_AREA : " +   "  p1x="+p1x
+  "  p1y="+p1y
+  "  p1z="+p1z
+  "  p2x="+p2x
+  "  p2y="+p2y
+  "  p2z="+p2z
+  "  frame="+frame
;}
}
