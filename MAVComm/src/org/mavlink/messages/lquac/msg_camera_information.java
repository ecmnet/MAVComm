/**
 * Generated class : msg_camera_information
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
 * Class msg_camera_information
 * WIP: Information about a camera
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
    payload_length = 86;
}

  /**
   * Timestamp (milliseconds since system boot)
   */
  public long time_boot_ms;
  /**
   * Focal length in mm
   */
  public float focal_length;
  /**
   * Image sensor size horizontal in mm
   */
  public float sensor_size_h;
  /**
   * Image sensor size vertical in mm
   */
  public float sensor_size_v;
  /**
   * Image resolution in pixels horizontal
   */
  public int resolution_h;
  /**
   * Image resolution in pixels vertical
   */
  public int resolution_v;
  /**
   * Camera ID if there are multiple
   */
  public int camera_id;
  /**
   * Name of the camera vendor
   */
  public int[] vendor_name = new int[32];
  /**
   * Name of the camera model
   */
  public int[] model_name = new int[32];
  /**
   * Reserved for a lense ID
   */
  public int lense_id;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_boot_ms = (int)dis.readInt()&0x00FFFFFFFF;
  focal_length = (float)dis.readFloat();
  sensor_size_h = (float)dis.readFloat();
  sensor_size_v = (float)dis.readFloat();
  resolution_h = (int)dis.readUnsignedShort()&0x00FFFF;
  resolution_v = (int)dis.readUnsignedShort()&0x00FFFF;
  camera_id = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<32; i++) {
    vendor_name[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
  for (int i=0; i<32; i++) {
    model_name[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
  lense_id = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+86];
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
  dos.writeFloat(focal_length);
  dos.writeFloat(sensor_size_h);
  dos.writeFloat(sensor_size_v);
  dos.writeShort(resolution_h&0x00FFFF);
  dos.writeShort(resolution_v&0x00FFFF);
  dos.writeByte(camera_id&0x00FF);
  for (int i=0; i<32; i++) {
    dos.writeByte(vendor_name[i]&0x00FF);
  }
  for (int i=0; i<32; i++) {
    dos.writeByte(model_name[i]&0x00FF);
  }
  dos.writeByte(lense_id&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 86);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[96] = crcl;
  buffer[97] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_CAMERA_INFORMATION : " +   "  time_boot_ms="+time_boot_ms+  "  focal_length="+focal_length+  "  sensor_size_h="+sensor_size_h+  "  sensor_size_v="+sensor_size_v+  "  resolution_h="+resolution_h+  "  resolution_v="+resolution_v+  "  camera_id="+camera_id+  "  vendor_name="+vendor_name+  "  model_name="+model_name+  "  lense_id="+lense_id;}
}
