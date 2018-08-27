/**
 * Generated class : msg_hil_controls
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
 * Class msg_hil_controls
 * Sent from autopilot to simulation. Hardware in the loop control outputs
 **/
public class msg_hil_controls extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_HIL_CONTROLS = 91;
  private static final long serialVersionUID = MAVLINK_MSG_ID_HIL_CONTROLS;
  public msg_hil_controls() {
    this(1,1);
}
  public msg_hil_controls(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_HIL_CONTROLS;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 42;
}

  /**
   * Timestamp (UNIX Epoch time or time since system boot). The receiving end can infer timestamp format (since 1.1.1970 or since system boot) by checking for the magnitude the number.
   */
  public long time_usec;
  /**
   * Control output -1 .. 1
   */
  public float roll_ailerons;
  /**
   * Control output -1 .. 1
   */
  public float pitch_elevator;
  /**
   * Control output -1 .. 1
   */
  public float yaw_rudder;
  /**
   * Throttle 0 .. 1
   */
  public float throttle;
  /**
   * Aux 1, -1 .. 1
   */
  public float aux1;
  /**
   * Aux 2, -1 .. 1
   */
  public float aux2;
  /**
   * Aux 3, -1 .. 1
   */
  public float aux3;
  /**
   * Aux 4, -1 .. 1
   */
  public float aux4;
  /**
   * System mode.
   */
  public int mode;
  /**
   * Navigation mode (MAV_NAV_MODE)
   */
  public int nav_mode;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  roll_ailerons = (float)dis.readFloat();
  pitch_elevator = (float)dis.readFloat();
  yaw_rudder = (float)dis.readFloat();
  throttle = (float)dis.readFloat();
  aux1 = (float)dis.readFloat();
  aux2 = (float)dis.readFloat();
  aux3 = (float)dis.readFloat();
  aux4 = (float)dis.readFloat();
  mode = (int)dis.readUnsignedByte()&0x00FF;
  nav_mode = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+42];
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
  dos.writeLong(time_usec);
  dos.writeFloat(roll_ailerons);
  dos.writeFloat(pitch_elevator);
  dos.writeFloat(yaw_rudder);
  dos.writeFloat(throttle);
  dos.writeFloat(aux1);
  dos.writeFloat(aux2);
  dos.writeFloat(aux3);
  dos.writeFloat(aux4);
  dos.writeByte(mode&0x00FF);
  dos.writeByte(nav_mode&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 42);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[52] = crcl;
  buffer[53] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_HIL_CONTROLS : " +   "  time_usec="+time_usec+  "  roll_ailerons="+roll_ailerons+  "  pitch_elevator="+pitch_elevator+  "  yaw_rudder="+yaw_rudder+  "  throttle="+throttle+  "  aux1="+aux1+  "  aux2="+aux2+  "  aux3="+aux3+  "  aux4="+aux4+  "  mode="+mode+  "  nav_mode="+nav_mode;}
}
