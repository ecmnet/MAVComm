/**
 * Generated class : msg_camera_image_captured
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
 * Class msg_camera_image_captured
 * WIP: Information about a captured image
 **/
public class msg_camera_image_captured extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_CAMERA_IMAGE_CAPTURED = 263;
  private static final long serialVersionUID = MAVLINK_MSG_ID_CAMERA_IMAGE_CAPTURED;
  public msg_camera_image_captured() {
    this(1,1);
}
  public msg_camera_image_captured(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_CAMERA_IMAGE_CAPTURED;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 255;
}

  /**
   * Timestamp (microseconds since UNIX epoch) in UTC. 0 for unknown.
   */
  public long time_utc;
  /**
   * Timestamp (milliseconds since system boot)
   */
  public long time_boot_ms;
  /**
   * Latitude, expressed as degrees * 1E7 where image was taken
   */
  public long lat;
  /**
   * Longitude, expressed as degrees * 1E7 where capture was taken
   */
  public long lon;
  /**
   * Altitude in meters, expressed as * 1E3 (AMSL, not WGS84) where image was taken
   */
  public long alt;
  /**
   * Altitude above ground in meters, expressed as * 1E3 where image was taken
   */
  public long relative_alt;
  /**
   * Quaternion of camera orientation (w, x, y, z order, zero-rotation is 0, 0, 0, 0)
   */
  public float[] q = new float[4];
  /**
   * Zero based index of this image (image count since armed -1)
   */
  public long image_index;
  /**
   * Camera ID if there are multiple
   */
  public int camera_id;
  /**
   * Boolean indicating success (1) or failure (0) while capturing this image.
   */
  public int capture_result;
  /**
   * URL of image taken. Either local storage or http://foo.jpg if camera provides an HTTP interface.
   */
  public char[] file_url = new char[205];
  public void setFile_url(String tmp) {
    int len = Math.min(tmp.length(), 205);
    for (int i=0; i<len; i++) {
      file_url[i] = tmp.charAt(i);
    }
    for (int i=len; i<205; i++) {
      file_url[i] = 0;
    }
  }
  public String getFile_url() {
    String result="";
    for (int i=0; i<205; i++) {
      if (file_url[i] != 0) result=result+file_url[i]; else break;
    }
    return result;
  }
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_utc = (long)dis.readLong();
  time_boot_ms = (int)dis.readInt()&0x00FFFFFFFF;
  lat = (int)dis.readInt();
  lon = (int)dis.readInt();
  alt = (int)dis.readInt();
  relative_alt = (int)dis.readInt();
  for (int i=0; i<4; i++) {
    q[i] = (float)dis.readFloat();
  }
  image_index = (int)dis.readInt();
  camera_id = (int)dis.readUnsignedByte()&0x00FF;
  capture_result = (int)dis.readByte();
  for (int i=0; i<205; i++) {
    file_url[i] = (char)dis.readByte();
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+255];
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
  dos.writeLong(time_utc);
  dos.writeInt((int)(time_boot_ms&0x00FFFFFFFF));
  dos.writeInt((int)(lat&0x00FFFFFFFF));
  dos.writeInt((int)(lon&0x00FFFFFFFF));
  dos.writeInt((int)(alt&0x00FFFFFFFF));
  dos.writeInt((int)(relative_alt&0x00FFFFFFFF));
  for (int i=0; i<4; i++) {
    dos.writeFloat(q[i]);
  }
  dos.writeInt((int)(image_index&0x00FFFFFFFF));
  dos.writeByte(camera_id&0x00FF);
  dos.write(capture_result&0x00FF);
  for (int i=0; i<205; i++) {
    dos.writeByte(file_url[i]);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 255);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[265] = crcl;
  buffer[266] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_CAMERA_IMAGE_CAPTURED : " +   "  time_utc="+time_utc+  "  time_boot_ms="+time_boot_ms+  "  lat="+lat+  "  lon="+lon+  "  alt="+alt+  "  relative_alt="+relative_alt+  "  q="+q+  "  image_index="+image_index+  "  camera_id="+camera_id+  "  capture_result="+capture_result+  "  file_url="+getFile_url();}
}
