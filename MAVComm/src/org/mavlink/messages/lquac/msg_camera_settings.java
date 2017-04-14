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
    payload_length = 28;
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
   * Shutter speed in s
   */
  public float shutter_speed;
  /**
   * ISO sensitivity
   */
  public float iso_sensitivity;
  /**
   * Color temperature in degrees Kelvin
   */
  public float white_balance;
  /**
   * Camera ID if there are multiple
   */
  public int camera_id;
  /**
   * Aperture locked (0: auto, 1: locked)
   */
  public int aperture_locked;
  /**
   * Shutter speed locked (0: auto, 1: locked)
   */
  public int shutter_speed_locked;
  /**
   * ISO sensitivity locked (0: auto, 1: locked)
   */
  public int iso_sensitivity_locked;
  /**
   * Color temperature locked (0: auto, 1: locked)
   */
  public int white_balance_locked;
  /**
   * Reserved for a camera mode ID
   */
  public int mode_id;
  /**
   * Reserved for a color mode ID
   */
  public int color_mode_id;
  /**
   * Reserved for image format ID
   */
  public int image_format_id;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_boot_ms = (int)dis.readInt()&0x00FFFFFFFF;
  aperture = (float)dis.readFloat();
  shutter_speed = (float)dis.readFloat();
  iso_sensitivity = (float)dis.readFloat();
  white_balance = (float)dis.readFloat();
  camera_id = (int)dis.readUnsignedByte()&0x00FF;
  aperture_locked = (int)dis.readUnsignedByte()&0x00FF;
  shutter_speed_locked = (int)dis.readUnsignedByte()&0x00FF;
  iso_sensitivity_locked = (int)dis.readUnsignedByte()&0x00FF;
  white_balance_locked = (int)dis.readUnsignedByte()&0x00FF;
  mode_id = (int)dis.readUnsignedByte()&0x00FF;
  color_mode_id = (int)dis.readUnsignedByte()&0x00FF;
  image_format_id = (int)dis.readUnsignedByte()&0x00FF;
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
  dos.writeFloat(aperture);
  dos.writeFloat(shutter_speed);
  dos.writeFloat(iso_sensitivity);
  dos.writeFloat(white_balance);
  dos.writeByte(camera_id&0x00FF);
  dos.writeByte(aperture_locked&0x00FF);
  dos.writeByte(shutter_speed_locked&0x00FF);
  dos.writeByte(iso_sensitivity_locked&0x00FF);
  dos.writeByte(white_balance_locked&0x00FF);
  dos.writeByte(mode_id&0x00FF);
  dos.writeByte(color_mode_id&0x00FF);
  dos.writeByte(image_format_id&0x00FF);
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
return "MAVLINK_MSG_ID_CAMERA_SETTINGS : " +   "  time_boot_ms="+time_boot_ms+  "  aperture="+aperture+  "  shutter_speed="+shutter_speed+  "  iso_sensitivity="+iso_sensitivity+  "  white_balance="+white_balance+  "  camera_id="+camera_id+  "  aperture_locked="+aperture_locked+  "  shutter_speed_locked="+shutter_speed_locked+  "  iso_sensitivity_locked="+iso_sensitivity_locked+  "  white_balance_locked="+white_balance_locked+  "  mode_id="+mode_id+  "  color_mode_id="+color_mode_id+  "  image_format_id="+image_format_id;}
}
