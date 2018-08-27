/**
 * Generated class : msg_camera_information
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
 * Class msg_camera_information
 * Information about a camera
 **/
public class msg_camera_information extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_CAMERA_INFORMATION = 259;
  private static final long serialVersionUID = MAVLINK_MSG_ID_CAMERA_INFORMATION;
  public msg_camera_information() {
    this(1,1);
}
  public msg_camera_information(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_CAMERA_INFORMATION;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 235;
}

  /**
   * Timestamp (time since system boot).
   */
  public long time_boot_ms;
  /**
   * Version of the camera firmware (v << 24 & 0xff = Dev, v << 16 & 0xff = Patch, v << 8 & 0xff = Minor, v & 0xff = Major)
   */
  public long firmware_version;
  /**
   * Focal length
   */
  public float focal_length;
  /**
   * Image sensor size horizontal
   */
  public float sensor_size_h;
  /**
   * Image sensor size vertical
   */
  public float sensor_size_v;
  /**
   * Bitmap of camera capability flags.
   */
  public long flags;
  /**
   * Horizontal image resolution
   */
  public int resolution_h;
  /**
   * Vertical image resolution
   */
  public int resolution_v;
  /**
   * Camera definition version (iteration)
   */
  public int cam_definition_version;
  /**
   * Name of the camera vendor
   */
  public int[] vendor_name = new int[32];
  /**
   * Name of the camera model
   */
  public int[] model_name = new int[32];
  /**
   * Reserved for a lens ID
   */
  public int lens_id;
  /**
   * Camera definition URI (if any, otherwise only basic functions will be available).
   */
  public char[] cam_definition_uri = new char[140];
  public void setCam_definition_uri(String tmp) {
    int len = Math.min(tmp.length(), 140);
    for (int i=0; i<len; i++) {
      cam_definition_uri[i] = tmp.charAt(i);
    }
    for (int i=len; i<140; i++) {
      cam_definition_uri[i] = 0;
    }
  }
  public String getCam_definition_uri() {
    String result="";
    for (int i=0; i<140; i++) {
      if (cam_definition_uri[i] != 0) result=result+cam_definition_uri[i]; else break;
    }
    return result;
  }
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_boot_ms = (int)dis.readInt()&0x00FFFFFFFF;
  firmware_version = (int)dis.readInt()&0x00FFFFFFFF;
  focal_length = (float)dis.readFloat();
  sensor_size_h = (float)dis.readFloat();
  sensor_size_v = (float)dis.readFloat();
  flags = (int)dis.readInt()&0x00FFFFFFFF;
  resolution_h = (int)dis.readUnsignedShort()&0x00FFFF;
  resolution_v = (int)dis.readUnsignedShort()&0x00FFFF;
  cam_definition_version = (int)dis.readUnsignedShort()&0x00FFFF;
  for (int i=0; i<32; i++) {
    vendor_name[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
  for (int i=0; i<32; i++) {
    model_name[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
  lens_id = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<140; i++) {
    cam_definition_uri[i] = (char)dis.readByte();
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+235];
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
  dos.writeInt((int)(firmware_version&0x00FFFFFFFF));
  dos.writeFloat(focal_length);
  dos.writeFloat(sensor_size_h);
  dos.writeFloat(sensor_size_v);
  dos.writeInt((int)(flags&0x00FFFFFFFF));
  dos.writeShort(resolution_h&0x00FFFF);
  dos.writeShort(resolution_v&0x00FFFF);
  dos.writeShort(cam_definition_version&0x00FFFF);
  for (int i=0; i<32; i++) {
    dos.writeByte(vendor_name[i]&0x00FF);
  }
  for (int i=0; i<32; i++) {
    dos.writeByte(model_name[i]&0x00FF);
  }
  dos.writeByte(lens_id&0x00FF);
  for (int i=0; i<140; i++) {
    dos.writeByte(cam_definition_uri[i]);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 235);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[245] = crcl;
  buffer[246] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_CAMERA_INFORMATION : " +   "  time_boot_ms="+time_boot_ms+  "  firmware_version="+firmware_version+  "  focal_length="+focal_length+  "  sensor_size_h="+sensor_size_h+  "  sensor_size_v="+sensor_size_v+  "  flags="+flags+  "  resolution_h="+resolution_h+  "  resolution_v="+resolution_v+  "  cam_definition_version="+cam_definition_version+  "  vendor_name="+vendor_name+  "  model_name="+model_name+  "  lens_id="+lens_id+  "  cam_definition_uri="+getCam_definition_uri();}
}
