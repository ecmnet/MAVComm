/**
 * Generated class : msg_attitude
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
 * Class msg_attitude
 * The attitude in the aeronautical frame (right-handed, Z-down, X-front, Y-right).
 **/
public class msg_attitude extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_ATTITUDE = 30;
  private static final long serialVersionUID = MAVLINK_MSG_ID_ATTITUDE;
  public msg_attitude() {
    this(1,1);
}
  public msg_attitude(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_ATTITUDE;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 28;
}

  /**
   * Timestamp (time since system boot).
   */
  public long time_boot_ms;
  /**
   * Roll angle (-pi..+pi)
   */
  public float roll;
  /**
   * Pitch angle (-pi..+pi)
   */
  public float pitch;
  /**
   * Yaw angle (-pi..+pi)
   */
  public float yaw;
  /**
   * Roll angular speed
   */
  public float rollspeed;
  /**
   * Pitch angular speed
   */
  public float pitchspeed;
  /**
   * Yaw angular speed
   */
  public float yawspeed;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_boot_ms = (int)dis.readInt()&0x00FFFFFFFF;
  roll = (float)dis.readFloat();
  pitch = (float)dis.readFloat();
  yaw = (float)dis.readFloat();
  rollspeed = (float)dis.readFloat();
  pitchspeed = (float)dis.readFloat();
  yawspeed = (float)dis.readFloat();
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
  dos.writeFloat(roll);
  dos.writeFloat(pitch);
  dos.writeFloat(yaw);
  dos.writeFloat(rollspeed);
  dos.writeFloat(pitchspeed);
  dos.writeFloat(yawspeed);
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
return "MAVLINK_MSG_ID_ATTITUDE : " +   "  time_boot_ms="+time_boot_ms
+  "  roll="+roll
+  "  pitch="+pitch
+  "  yaw="+yaw
+  "  rollspeed="+rollspeed
+  "  pitchspeed="+pitchspeed
+  "  yawspeed="+yawspeed
;}
}
