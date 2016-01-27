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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
public void decode(ByteBuffer dis) throws IOException {
  time_usec = (long)dis.getLong();
  seq = (int)dis.getInt()&0x00FFFFFFFF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+12];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.putLong(time_usec);
  dos.putInt((int)(seq&0x00FFFFFFFF));
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 12);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[18] = crcl;
  buffer[19] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_CAMERA_TRIGGER : " +   "  time_usec="+time_usec+  "  seq="+seq;}
}
