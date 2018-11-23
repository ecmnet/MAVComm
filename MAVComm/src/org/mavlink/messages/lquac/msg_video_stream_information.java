/**
 * Generated class : msg_video_stream_information
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
 * Class msg_video_stream_information
 * Information about video stream
 **/
public class msg_video_stream_information extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_VIDEO_STREAM_INFORMATION = 269;
  private static final long serialVersionUID = MAVLINK_MSG_ID_VIDEO_STREAM_INFORMATION;
  public msg_video_stream_information() {
    this(1,1);
}
  public msg_video_stream_information(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_VIDEO_STREAM_INFORMATION;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 180;
}

  /**
   * Frame rate
   */
  public float framerate;
  /**
   * Bit rate in bits per second
   */
  public long bitrate;
  /**
   * Bitmap of stream status flags
   */
  public int flags;
  /**
   * Horizontal resolution
   */
  public int resolution_h;
  /**
   * Vertical resolution
   */
  public int resolution_v;
  /**
   * Video image rotation clockwise
   */
  public int rotation;
  /**
   * Horizontal Field of view
   */
  public int hfov;
  /**
   * Stream ID (1 for first, 2 for second, etc.)
   */
  public int stream_id;
  /**
   * Number of streams available
   */
  public int count;
  /**
   * Video stream URI
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
  flags = (int)dis.readUnsignedShort()&0x00FFFF;
  resolution_h = (int)dis.readUnsignedShort()&0x00FFFF;
  resolution_v = (int)dis.readUnsignedShort()&0x00FFFF;
  rotation = (int)dis.readUnsignedShort()&0x00FFFF;
  hfov = (int)dis.readUnsignedShort()&0x00FFFF;
  stream_id = (int)dis.readUnsignedByte()&0x00FF;
  count = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<160; i++) {
    uri[i] = (char)dis.readByte();
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+180];
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
  dos.writeShort(flags&0x00FFFF);
  dos.writeShort(resolution_h&0x00FFFF);
  dos.writeShort(resolution_v&0x00FFFF);
  dos.writeShort(rotation&0x00FFFF);
  dos.writeShort(hfov&0x00FFFF);
  dos.writeByte(stream_id&0x00FF);
  dos.writeByte(count&0x00FF);
  for (int i=0; i<160; i++) {
    dos.writeByte(uri[i]);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 180);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[190] = crcl;
  buffer[191] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_VIDEO_STREAM_INFORMATION : " +   "  framerate="+framerate+  "  bitrate="+bitrate+  "  flags="+flags+  "  resolution_h="+resolution_h+  "  resolution_v="+resolution_v+  "  rotation="+rotation+  "  hfov="+hfov+  "  stream_id="+stream_id+  "  count="+count+  "  uri="+getUri();}
}
