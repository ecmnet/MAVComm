/**
 * Generated class : msg_vicon_position_estimate
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
 * Class msg_vicon_position_estimate
 * 
 **/
public class msg_vicon_position_estimate extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_VICON_POSITION_ESTIMATE = 104;
  private static final long serialVersionUID = MAVLINK_MSG_ID_VICON_POSITION_ESTIMATE;
  public msg_vicon_position_estimate() {
    this(1,1);
}
  public msg_vicon_position_estimate(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_VICON_POSITION_ESTIMATE;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 32;
}

  /**
   * Timestamp (microseconds, synced to UNIX time or since system boot)
   */
  public long usec;
  /**
   * Global X position
   */
  public float x;
  /**
   * Global Y position
   */
  public float y;
  /**
   * Global Z position
   */
  public float z;
  /**
   * Roll angle in rad
   */
  public float roll;
  /**
   * Pitch angle in rad
   */
  public float pitch;
  /**
   * Yaw angle in rad
   */
  public float yaw;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  usec = (long)dis.readLong();
  x = (float)dis.readFloat();
  y = (float)dis.readFloat();
  z = (float)dis.readFloat();
  roll = (float)dis.readFloat();
  pitch = (float)dis.readFloat();
  yaw = (float)dis.readFloat();
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+32];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFD);
  dos.writeByte(length & 0x00FF);
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
  dos.writeFloat(roll);
  dos.writeFloat(pitch);
  dos.writeFloat(yaw);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 32);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[42] = crcl;
  buffer[43] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_VICON_POSITION_ESTIMATE : " +   "  usec="+usec+  "  x="+x+  "  y="+y+  "  z="+z+  "  roll="+roll+  "  pitch="+pitch+  "  yaw="+yaw;}
}
