/**
 * Generated class : msg_mount_status
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
 * Class msg_mount_status
 * WIP: Status and orientation of a mount
 **/
public class msg_mount_status extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_MOUNT_STATUS = 265;
  private static final long serialVersionUID = MAVLINK_MSG_ID_MOUNT_STATUS;
  public msg_mount_status() {
    this(1,1);
}
  public msg_mount_status(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_MOUNT_STATUS;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 29;
}

  /**
   * Timestamp (milliseconds since system boot)
   */
  public long time_boot_ms;
  /**
   * Roll in degrees, set if appropriate mount mode.
   */
  public float roll;
  /**
   * Pitch in degrees, set if appropriate mount mode.
   */
  public float pitch;
  /**
   * Yaw in degrees, set if appropriate mount mode.
   */
  public float yaw;
  /**
   * Latitude, in degrees * 1E7, set if appropriate mount mode.
   */
  public long lat;
  /**
   * Longitude, in degrees * 1E7, set if appropriate mount mode.
   */
  public long lon;
  /**
   * Altitude in meters, set if appropriate mount mode.
   */
  public float alt;
  /**
   * Mount operation mode (see MAV_MOUNT_MODE enum)
   */
  public int mode;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_boot_ms = (int)dis.readInt()&0x00FFFFFFFF;
  roll = (float)dis.readFloat();
  pitch = (float)dis.readFloat();
  yaw = (float)dis.readFloat();
  lat = (int)dis.readInt();
  lon = (int)dis.readInt();
  alt = (float)dis.readFloat();
  mode = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+29];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFD);
  dos.writeByte(length & 0x00FF);
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
  dos.writeInt((int)(lat&0x00FFFFFFFF));
  dos.writeInt((int)(lon&0x00FFFFFFFF));
  dos.writeFloat(alt);
  dos.writeByte(mode&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 29);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[39] = crcl;
  buffer[40] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_MOUNT_STATUS : " +   "  time_boot_ms="+time_boot_ms+  "  roll="+roll+  "  pitch="+pitch+  "  yaw="+yaw+  "  lat="+lat+  "  lon="+lon+  "  alt="+alt+  "  mode="+mode;}
}
