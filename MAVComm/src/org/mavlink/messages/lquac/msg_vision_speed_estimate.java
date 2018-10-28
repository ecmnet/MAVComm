/**
 * Generated class : msg_vision_speed_estimate
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
 * Class msg_vision_speed_estimate
 * Speed estimate from a vision source.
 **/
public class msg_vision_speed_estimate extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_VISION_SPEED_ESTIMATE = 103;
  private static final long serialVersionUID = MAVLINK_MSG_ID_VISION_SPEED_ESTIMATE;
  public msg_vision_speed_estimate() {
    this(1,1);
}
  public msg_vision_speed_estimate(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_VISION_SPEED_ESTIMATE;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 56;
}

  /**
   * Timestamp (UNIX time or time since system boot)
   */
  public long usec;
  /**
   * Global X speed
   */
  public float x;
  /**
   * Global Y speed
   */
  public float y;
  /**
   * Global Z speed
   */
  public float z;
  /**
   * Linear velocity covariance matrix (1st three entries - 1st row, etc.)
   */
  public float[] covariance = new float[9];
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  usec = (long)dis.readLong();
  x = (float)dis.readFloat();
  y = (float)dis.readFloat();
  z = (float)dis.readFloat();
  for (int i=0; i<9; i++) {
    covariance[i] = (float)dis.readFloat();
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+56];
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
  for (int i=0; i<9; i++) {
    dos.writeFloat(covariance[i]);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 56);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[66] = crcl;
  buffer[67] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_VISION_SPEED_ESTIMATE : " +   "  usec="+usec+  "  x="+x+  "  y="+y+  "  z="+z+  "  covariance="+covariance;}
}
