/**
 * Generated class : msg_mount_orientation
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
 * Class msg_mount_orientation
 * Orientation of a mount
 **/
public class msg_mount_orientation extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_MOUNT_ORIENTATION = 265;
  private static final long serialVersionUID = MAVLINK_MSG_ID_MOUNT_ORIENTATION;
  public msg_mount_orientation() {
    this(1,1);
}
  public msg_mount_orientation(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_MOUNT_ORIENTATION;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 20;
}

  /**
   * Timestamp (time since system boot).
   */
  public long time_boot_ms;
  /**
   * Roll in global frame (set to NaN for invalid).
   */
  public float roll;
  /**
   * Pitch in global frame (set to NaN for invalid).
   */
  public float pitch;
  /**
   * Yaw relative to vehicle(set to NaN for invalid).
   */
  public float yaw;
  /**
   * Yaw in absolute frame, North is 0 (set to NaN for invalid).
   */
  public float yaw_absolute;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_boot_ms = (int)dis.readInt()&0x00FFFFFFFF;
  roll = (float)dis.readFloat();
  pitch = (float)dis.readFloat();
  yaw = (float)dis.readFloat();
  yaw_absolute = (float)dis.readFloat();
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+20];
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
  dos.writeFloat(roll);
  dos.writeFloat(pitch);
  dos.writeFloat(yaw);
  dos.writeFloat(yaw_absolute);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 20);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[30] = crcl;
  buffer[31] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_MOUNT_ORIENTATION : " +   "  time_boot_ms="+time_boot_ms
+  "  roll="+roll
+  "  pitch="+pitch
+  "  yaw="+yaw
+  "  yaw_absolute="+yaw_absolute
;}
}
