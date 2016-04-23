/**
 * Generated class : msg_set_actuator_control_target
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
 * Class msg_set_actuator_control_target
 * Set the vehicle attitude and body angular rates.
 **/
public class msg_set_actuator_control_target extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_SET_ACTUATOR_CONTROL_TARGET = 139;
  private static final long serialVersionUID = MAVLINK_MSG_ID_SET_ACTUATOR_CONTROL_TARGET;
  public msg_set_actuator_control_target() {
    this(1,1);
}
  public msg_set_actuator_control_target(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_SET_ACTUATOR_CONTROL_TARGET;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 43;
}

  /**
   * Timestamp (micros since boot or Unix epoch)
   */
  public long time_usec;
  /**
   * Actuator controls. Normed to -1..+1 where 0 is neutral position. Throttle for single rotation direction motors is 0..1, negative range for reverse direction. Standard mapping for attitude controls (group 0): (index 0-7): roll, pitch, yaw, throttle, flaps, spoilers, airbrakes, landing gear. Load a pass-through mixer to repurpose them as generic outputs.
   */
  public float[] controls = new float[8];
  /**
   * Actuator group. The "_mlx" indicates this is a multi-instance message and a MAVLink parser should use this field to difference between instances.
   */
  public int group_mlx;
  /**
   * System ID
   */
  public int target_system;
  /**
   * Component ID
   */
  public int target_component;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  for (int i=0; i<8; i++) {
    controls[i] = (float)dis.readFloat();
  }
  group_mlx = (int)dis.readUnsignedByte()&0x00FF;
  target_system = (int)dis.readUnsignedByte()&0x00FF;
  target_component = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+43];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFE);
  dos.writeByte(length & 0x00FF);
  dos.writeByte(sequence & 0x00FF);
  dos.writeByte(sysId & 0x00FF);
  dos.writeByte(componentId & 0x00FF);
  dos.writeByte(messageType & 0x00FF);
  dos.writeLong(time_usec);
  for (int i=0; i<8; i++) {
    dos.writeFloat(controls[i]);
  }
  dos.writeByte(group_mlx&0x00FF);
  dos.writeByte(target_system&0x00FF);
  dos.writeByte(target_component&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 43);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[49] = crcl;
  buffer[50] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_SET_ACTUATOR_CONTROL_TARGET : " +   "  time_usec="+time_usec+  "  controls="+controls+  "  group_mlx="+group_mlx+  "  target_system="+target_system+  "  target_component="+target_component;}
}
