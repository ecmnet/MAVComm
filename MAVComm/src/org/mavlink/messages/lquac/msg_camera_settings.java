/**
 * Generated class : msg_camera_settings
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
 * Class msg_camera_settings
 * WIP: Settings of a camera, can be requested using MAV_CMD_REQUEST_CAMERA_SETTINGS and written using MAV_CMD_SET_CAMERA_SETTINGS
 **/
public class msg_camera_settings extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_CAMERA_SETTINGS = 260;
  private static final long serialVersionUID = MAVLINK_MSG_ID_CAMERA_SETTINGS;
  public msg_camera_settings() {
    this(1,1);
}
  public msg_camera_settings(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_CAMERA_SETTINGS;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 35;
}

  /**
   * Timestamp (milliseconds since system boot)
   */
  public long time_boot_ms;
  /**
   * Aperture is 1/value
   */
  public float aperture;
  /**
   * Shutter speed in seconds
   */
  public float shutter_speed;
  /**
   * ISO sensitivity
   */
  public float iso_sensitivity;
  /**
   * Exposure Value
   */
  public float ev;
  /**
   * Color temperature in degrees Kelvin. (0: Auto WB)
   */
  public float white_balance;
  /**
   * Camera ID (1 for first, 2 for second, etc.)
   */
  public int camera_id;
  /**
   * 0: full auto 1: full manual 2: aperture priority 3: shutter priority
   */
  public int exposure_mode;
  /**
   * Reserved for a camera mode ID. (0: Photo 1: Video)
   */
  public int mode_id;
  /**
   * Audio recording enabled (0: off 1: on)
   */
  public int audio_recording;
  /**
   * Reserved for a color mode ID (Neutral, Vivid, etc.)
   */
  public int color_mode_id;
  /**
   * Reserved for image format ID (Jpeg/Raw/Jpeg+Raw)
   */
  public int image_format_id;
  /**
   * Reserved for image quality ID (Compression)
   */
  public int image_quality_id;
  /**
   * Reserved for metering mode ID (Average, Center, Spot, etc.)
   */
  public int metering_mode_id;
  /**
   * Reserved for flicker mode ID (Auto, 60Hz, 50Hz, etc.)
   */
  public int flicker_mode_id;
  /**
   * Photo resolution ID (4000x3000, 2560x1920, etc.)
   */
  public int photo_resolution_id;
  /**
   * Video resolution and rate ID (4K 60 Hz, 4K 30 Hz, HD 60 Hz, HD 30 Hz, etc.)
   */
  public int video_resolution_and_rate_id;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_boot_ms = (int)dis.readInt()&0x00FFFFFFFF;
  aperture = (float)dis.readFloat();
  shutter_speed = (float)dis.readFloat();
  iso_sensitivity = (float)dis.readFloat();
  ev = (float)dis.readFloat();
  white_balance = (float)dis.readFloat();
  camera_id = (int)dis.readUnsignedByte()&0x00FF;
  exposure_mode = (int)dis.readUnsignedByte()&0x00FF;
  mode_id = (int)dis.readUnsignedByte()&0x00FF;
  audio_recording = (int)dis.readUnsignedByte()&0x00FF;
  color_mode_id = (int)dis.readUnsignedByte()&0x00FF;
  image_format_id = (int)dis.readUnsignedByte()&0x00FF;
  image_quality_id = (int)dis.readUnsignedByte()&0x00FF;
  metering_mode_id = (int)dis.readUnsignedByte()&0x00FF;
  flicker_mode_id = (int)dis.readUnsignedByte()&0x00FF;
  photo_resolution_id = (int)dis.readUnsignedByte()&0x00FF;
  video_resolution_and_rate_id = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+35];
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
  dos.writeFloat(aperture);
  dos.writeFloat(shutter_speed);
  dos.writeFloat(iso_sensitivity);
  dos.writeFloat(ev);
  dos.writeFloat(white_balance);
  dos.writeByte(camera_id&0x00FF);
  dos.writeByte(exposure_mode&0x00FF);
  dos.writeByte(mode_id&0x00FF);
  dos.writeByte(audio_recording&0x00FF);
  dos.writeByte(color_mode_id&0x00FF);
  dos.writeByte(image_format_id&0x00FF);
  dos.writeByte(image_quality_id&0x00FF);
  dos.writeByte(metering_mode_id&0x00FF);
  dos.writeByte(flicker_mode_id&0x00FF);
  dos.writeByte(photo_resolution_id&0x00FF);
  dos.writeByte(video_resolution_and_rate_id&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 35);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[45] = crcl;
  buffer[46] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_CAMERA_SETTINGS : " +   "  time_boot_ms="+time_boot_ms+  "  aperture="+aperture+  "  shutter_speed="+shutter_speed+  "  iso_sensitivity="+iso_sensitivity+  "  ev="+ev+  "  white_balance="+white_balance+  "  camera_id="+camera_id+  "  exposure_mode="+exposure_mode+  "  mode_id="+mode_id+  "  audio_recording="+audio_recording+  "  color_mode_id="+color_mode_id+  "  image_format_id="+image_format_id+  "  image_quality_id="+image_quality_id+  "  metering_mode_id="+metering_mode_id+  "  flicker_mode_id="+flicker_mode_id+  "  photo_resolution_id="+photo_resolution_id+  "  video_resolution_and_rate_id="+video_resolution_and_rate_id;}
}
