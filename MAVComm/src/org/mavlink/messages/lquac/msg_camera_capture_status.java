/**
 * Generated class : msg_camera_capture_status
 * DO NOT MODIFY!
 **/
package org.mavlink.messages.lquac;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.mavlink.IMAVLinkCRC;
import org.mavlink.MAVLinkCRC;
import org.mavlink.io.LittleEndianDataInputStream;
import org.mavlink.io.LittleEndianDataOutputStream;
import org.mavlink.messages.MAVLinkMessage;
/**
 * Class msg_camera_capture_status
 * Information about the status of a capture.
 **/
public class msg_camera_capture_status extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_CAMERA_CAPTURE_STATUS = 262;
  private static final long serialVersionUID = MAVLINK_MSG_ID_CAMERA_CAPTURE_STATUS;
  public msg_camera_capture_status() {
    this(1,1);
}
  public msg_camera_capture_status(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_CAMERA_CAPTURE_STATUS;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 18;
}

  /**
   * Timestamp (time since system boot).
   */
  public long time_boot_ms;
  /**
   * Image capture interval
   */
  public float image_interval;
  /**
   * Time since recording started
   */
  public long recording_time_ms;
  /**
   * Available storage capacity.
   */
  public float available_capacity;
  /**
   * Current status of image capturing (0: idle, 1: capture in progress, 2: interval set but idle, 3: interval set and capture in progress)
   */
  public int image_status;
  /**
   * Current status of video capturing (0: idle, 1: capture in progress)
   */
  public int video_status;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_boot_ms = (int)dis.readInt()&0x00FFFFFFFF;
  image_interval = (float)dis.readFloat();
  recording_time_ms = (int)dis.readInt()&0x00FFFFFFFF;
  available_capacity = (float)dis.readFloat();
  image_status = (int)dis.readUnsignedByte()&0x00FF;
  video_status = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+18];
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
  dos.writeFloat(image_interval);
  dos.writeInt((int)(recording_time_ms&0x00FFFFFFFF));
  dos.writeFloat(available_capacity);
  dos.writeByte(image_status&0x00FF);
  dos.writeByte(video_status&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 18);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[28] = crcl;
  buffer[29] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_CAMERA_CAPTURE_STATUS : " +   "  time_boot_ms="+time_boot_ms+  "  image_interval="+image_interval+  "  recording_time_ms="+recording_time_ms+  "  available_capacity="+available_capacity+  "  image_status="+image_status+  "  video_status="+video_status;}
}
