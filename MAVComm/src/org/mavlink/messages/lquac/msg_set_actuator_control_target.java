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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
public void decode(ByteBuffer dis) throws IOException {
  time_usec = (long)dis.getLong();
  for (int i=0; i<8; i++) {
    controls[i] = (float)dis.getFloat();
  }
  group_mlx = (int)dis.get()&0x00FF;
  target_system = (int)dis.get()&0x00FF;
  target_component = (int)dis.get()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+43];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.putLong(time_usec);
  for (int i=0; i<8; i++) {
    dos.putFloat(controls[i]);
  }
  dos.put((byte)(group_mlx&0x00FF));
  dos.put((byte)(target_system&0x00FF));
  dos.put((byte)(target_component&0x00FF));
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 43);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[49] = crcl;
  buffer[50] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_SET_ACTUATOR_CONTROL_TARGET : " +   "  time_usec="+time_usec+  "  controls="+controls+  "  group_mlx="+group_mlx+  "  target_system="+target_system+  "  target_component="+target_component;}
}
