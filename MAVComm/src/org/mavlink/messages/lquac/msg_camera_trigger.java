/**
 * Generated class : msg_camera_trigger
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
 * Class msg_camera_trigger
 * Camera-IMU triggering and synchronisation message.
 **/
public class msg_camera_trigger extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_CAMERA_TRIGGER = 112;
  private static final long serialVersionUID = MAVLINK_MSG_ID_CAMERA_TRIGGER;
  public msg_camera_trigger() {
    this(1,1);
}
  public msg_camera_trigger(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_CAMERA_TRIGGER;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 12;
}

  /**
   * Timestamp for the image frame in microseconds
   */
  public long time_usec;
  /**
   * Image frame sequence
   */
  public long seq;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  seq = (int)dis.readInt()&0x00FFFFFFFF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+12];
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
  dos.writeLong(time_usec);
  dos.writeInt((int)(seq&0x00FFFFFFFF));
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 12);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[22] = crcl;
  buffer[23] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_CAMERA_TRIGGER : " +   "  time_usec="+time_usec+  "  seq="+seq;}
}
