/**
 * Generated class : msg_hil_actuator_controls
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
 * Class msg_hil_actuator_controls
 * Sent from autopilot to simulation. Hardware in the loop control outputs (replacement for HIL_CONTROLS)
 **/
public class msg_hil_actuator_controls extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_HIL_ACTUATOR_CONTROLS = 93;
  private static final long serialVersionUID = MAVLINK_MSG_ID_HIL_ACTUATOR_CONTROLS;
  public msg_hil_actuator_controls() {
    this(1,1);
}
  public msg_hil_actuator_controls(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_HIL_ACTUATOR_CONTROLS;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 81;
}

  /**
   * Timestamp (UNIX Epoch time or time since system boot). The receiving end can infer timestamp format (since 1.1.1970 or since system boot) by checking for the magnitude the number.
   */
  public long time_usec;
  /**
   * Flags as bitfield, reserved for future use.
   */
  public long flags;
  /**
   * Control outputs -1 .. 1. Channel assignment depends on the simulated hardware.
   */
  public float[] controls = new float[16];
  /**
   * System mode. Includes arming state.
   */
  public int mode;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  flags = (long)dis.readLong();
  for (int i=0; i<16; i++) {
    controls[i] = (float)dis.readFloat();
  }
  mode = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+81];
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
  dos.writeLong(flags);
  for (int i=0; i<16; i++) {
    dos.writeFloat(controls[i]);
  }
  dos.writeByte(mode&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 81);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[91] = crcl;
  buffer[92] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_HIL_ACTUATOR_CONTROLS : " +   "  time_usec="+time_usec
+  "  flags="+flags
+  "  controls[0]="+controls[0]
+  "  controls[1]="+controls[1]
+  "  controls[2]="+controls[2]
+  "  controls[3]="+controls[3]
+  "  controls[4]="+controls[4]
+  "  controls[5]="+controls[5]
+  "  controls[6]="+controls[6]
+  "  controls[7]="+controls[7]
+  "  controls[8]="+controls[8]
+  "  controls[9]="+controls[9]
+  "  controls[10]="+controls[10]
+  "  controls[11]="+controls[11]
+  "  controls[12]="+controls[12]
+  "  controls[13]="+controls[13]
+  "  controls[14]="+controls[14]
+  "  controls[15]="+controls[15]
+  "  mode="+mode
;}
}
