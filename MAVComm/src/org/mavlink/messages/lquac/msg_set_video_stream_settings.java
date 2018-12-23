/**
 * Generated class : msg_set_video_stream_settings
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
 * Class msg_set_video_stream_settings
 * Message that sets video stream settings
 **/
public class msg_set_video_stream_settings extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_SET_VIDEO_STREAM_SETTINGS = 270;
  private static final long serialVersionUID = MAVLINK_MSG_ID_SET_VIDEO_STREAM_SETTINGS;
  public msg_set_video_stream_settings() {
    this(1,1);
}
  public msg_set_video_stream_settings(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_SET_VIDEO_STREAM_SETTINGS;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 177;
}

  /**
   * Frame rate (set to -1 for highest framerate possible)
   */
  public float framerate;
  /**
   * Bit rate (set to -1 for auto)
   */
  public long bitrate;
  /**
   * Horizontal resolution (set to -1 for highest resolution possible)
   */
  public int resolution_h;
  /**
   * Vertical resolution (set to -1 for highest resolution possible)
   */
  public int resolution_v;
  /**
   * Video image rotation clockwise (0-359 degrees)
   */
  public int rotation;
  /**
   * system ID of the target
   */
  public int target_system;
  /**
   * component ID of the target
   */
  public int target_component;
  /**
   * Stream ID (1 for first, 2 for second, etc.)
   */
  public int camera_id;
  /**
   * Video stream URI (mostly for UDP/RTP)
   */
  public char[] uri = new char[160];
  public void setUri(String tmp) {
    int len = Math.min(tmp.length(), 160);
    for (int i=0; i<len; i++) {
      uri[i] = tmp.charAt(i);
    }
    for (int i=len; i<160; i++) {
      uri[i] = 0;
    }
  }
  public String getUri() {
    String result="";
    for (int i=0; i<160; i++) {
      if (uri[i] != 0) result=result+uri[i]; else break;
    }
    return result;
  }
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  framerate = (float)dis.readFloat();
  bitrate = (int)dis.readInt()&0x00FFFFFFFF;
  resolution_h = (int)dis.readUnsignedShort()&0x00FFFF;
  resolution_v = (int)dis.readUnsignedShort()&0x00FFFF;
  rotation = (int)dis.readUnsignedShort()&0x00FFFF;
  target_system = (int)dis.readUnsignedByte()&0x00FF;
  target_component = (int)dis.readUnsignedByte()&0x00FF;
  camera_id = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<160; i++) {
    uri[i] = (char)dis.readByte();
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+177];
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
  dos.writeFloat(framerate);
  dos.writeInt((int)(bitrate&0x00FFFFFFFF));
  dos.writeShort(resolution_h&0x00FFFF);
  dos.writeShort(resolution_v&0x00FFFF);
  dos.writeShort(rotation&0x00FFFF);
  dos.writeByte(target_system&0x00FF);
  dos.writeByte(target_component&0x00FF);
  dos.writeByte(camera_id&0x00FF);
  for (int i=0; i<160; i++) {
    dos.writeByte(uri[i]);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 177);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[187] = crcl;
  buffer[188] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_SET_VIDEO_STREAM_SETTINGS : " +   "  framerate="+framerate
+  "  bitrate="+bitrate
+  "  resolution_h="+resolution_h
+  "  resolution_v="+resolution_v
+  "  rotation="+rotation
+  "  target_system="+target_system
+  "  target_component="+target_component
+  "  camera_id="+camera_id
+  "  uri="+getUri()
;}
}
