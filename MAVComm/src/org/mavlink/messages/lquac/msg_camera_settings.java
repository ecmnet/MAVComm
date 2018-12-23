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
 * Settings of a camera, can be requested using MAV_CMD_REQUEST_CAMERA_SETTINGS.
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
    payload_length = 13;
}

  /**
   * Timestamp (time since system boot).
   */
  public long time_boot_ms;
  /**
   * Camera mode
   */
  public int mode_id;
  /**
   * Current zoom level (0.0 to 100.0, NaN if not known)
   */
  public float zoomLevel;
  /**
   * Current focus level (0.0 to 100.0, NaN if not known)
   */
  public float focusLevel;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_boot_ms = (int)dis.readInt()&0x00FFFFFFFF;
  mode_id = (int)dis.readUnsignedByte()&0x00FF;
  zoomLevel = (float)dis.readFloat();
  focusLevel = (float)dis.readFloat();
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+13];
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
  dos.writeByte(mode_id&0x00FF);
  dos.writeFloat(zoomLevel);
  dos.writeFloat(focusLevel);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 13);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[23] = crcl;
  buffer[24] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_CAMERA_SETTINGS : " +   "  time_boot_ms="+time_boot_ms
+  "  mode_id="+mode_id
+  "  zoomLevel="+zoomLevel
+  "  focusLevel="+focusLevel
;}
}
