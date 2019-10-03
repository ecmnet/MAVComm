/**
 * Generated class : msg_heartbeat
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
 * Class msg_heartbeat
 * The heartbeat message shows that a system or component is present and responding. The type and autopilot fields (along with the message component id), allow the receiving system to treat further messages from this system appropriately (e.g. by laying out the user interface based on the autopilot). This microservice is documented at https://mavlink.io/en/services/heartbeat.html
 **/
public class msg_heartbeat extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_HEARTBEAT = 0;
  private static final long serialVersionUID = MAVLINK_MSG_ID_HEARTBEAT;
  public msg_heartbeat() {
    this(1,1);
}
  public msg_heartbeat(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_HEARTBEAT;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 9;
}

  /**
   * A bitfield for use for autopilot-specific flags
   */
  public long custom_mode;
  /**
   * Vehicle or component type. For a flight controller component the vehicle type (quadrotor, helicopter, etc.). For other components the component type (e.g. camera, gimbal, etc.). This should be used in preference to component id for identifying the component type.
   */
  public int type;
  /**
   * Autopilot type / class. Use MAV_AUTOPILOT_INVALID for components that are not flight controllers.
   */
  public int autopilot;
  /**
   * System mode bitmap.
   */
  public int base_mode;
  /**
   * System status flag.
   */
  public int system_status;
  /**
   * MAVLink version, not writable by user, gets added by protocol because of magic data type: uint8_t_mavlink_version
   */
  public int mavlink_version;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  custom_mode = (int)dis.readInt()&0x00FFFFFFFF;
  type = (int)dis.readUnsignedByte()&0x00FF;
  autopilot = (int)dis.readUnsignedByte()&0x00FF;
  base_mode = (int)dis.readUnsignedByte()&0x00FF;
  system_status = (int)dis.readUnsignedByte()&0x00FF;
  mavlink_version = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+9];
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
  dos.writeInt((int)(custom_mode&0x00FFFFFFFF));
  dos.writeByte(type&0x00FF);
  dos.writeByte(autopilot&0x00FF);
  dos.writeByte(base_mode&0x00FF);
  dos.writeByte(system_status&0x00FF);
  dos.writeByte(mavlink_version&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 9);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[19] = crcl;
  buffer[20] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_HEARTBEAT : " +   "  custom_mode="+custom_mode
+  "  type="+type
+  "  autopilot="+autopilot
+  "  base_mode="+base_mode
+  "  system_status="+system_status
+  "  mavlink_version="+mavlink_version
;}
}
