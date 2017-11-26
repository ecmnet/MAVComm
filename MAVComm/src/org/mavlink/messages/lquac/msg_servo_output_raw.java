/**
 * Generated class : msg_servo_output_raw
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
 * Class msg_servo_output_raw
 * The RAW values of the servo outputs (for RC input from the remote, use the RC_CHANNELS messages). The standard PPM modulation is as follows: 1000 microseconds: 0%, 2000 microseconds: 100%.
 **/
public class msg_servo_output_raw extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_SERVO_OUTPUT_RAW = 36;
  private static final long serialVersionUID = MAVLINK_MSG_ID_SERVO_OUTPUT_RAW;
  public msg_servo_output_raw() {
    this(1,1);
}
  public msg_servo_output_raw(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_SERVO_OUTPUT_RAW;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 37;
}

  /**
   * Timestamp (microseconds since system boot)
   */
  public long time_usec;
  /**
   * Servo output 1 value, in microseconds
   */
  public int servo1_raw;
  /**
   * Servo output 2 value, in microseconds
   */
  public int servo2_raw;
  /**
   * Servo output 3 value, in microseconds
   */
  public int servo3_raw;
  /**
   * Servo output 4 value, in microseconds
   */
  public int servo4_raw;
  /**
   * Servo output 5 value, in microseconds
   */
  public int servo5_raw;
  /**
   * Servo output 6 value, in microseconds
   */
  public int servo6_raw;
  /**
   * Servo output 7 value, in microseconds
   */
  public int servo7_raw;
  /**
   * Servo output 8 value, in microseconds
   */
  public int servo8_raw;
  /**
   * Servo output port (set of 8 outputs = 1 port). Most MAVs will just use one, but this allows to encode more than 8 servos.
   */
  public int port;
  /**
   * Servo output 9 value, in microseconds
   */
  public int servo9_raw;
  /**
   * Servo output 10 value, in microseconds
   */
  public int servo10_raw;
  /**
   * Servo output 11 value, in microseconds
   */
  public int servo11_raw;
  /**
   * Servo output 12 value, in microseconds
   */
  public int servo12_raw;
  /**
   * Servo output 13 value, in microseconds
   */
  public int servo13_raw;
  /**
   * Servo output 14 value, in microseconds
   */
  public int servo14_raw;
  /**
   * Servo output 15 value, in microseconds
   */
  public int servo15_raw;
  /**
   * Servo output 16 value, in microseconds
   */
  public int servo16_raw;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (int)dis.readInt()&0x00FFFFFFFF;
  servo1_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  servo2_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  servo3_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  servo4_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  servo5_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  servo6_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  servo7_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  servo8_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  port = (int)dis.readUnsignedByte()&0x00FF;
  servo9_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  servo10_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  servo11_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  servo12_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  servo13_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  servo14_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  servo15_raw = (int)dis.readUnsignedShort()&0x00FFFF;
  servo16_raw = (int)dis.readUnsignedShort()&0x00FFFF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+37];
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
  dos.writeInt((int)(time_usec&0x00FFFFFFFF));
  dos.writeShort(servo1_raw&0x00FFFF);
  dos.writeShort(servo2_raw&0x00FFFF);
  dos.writeShort(servo3_raw&0x00FFFF);
  dos.writeShort(servo4_raw&0x00FFFF);
  dos.writeShort(servo5_raw&0x00FFFF);
  dos.writeShort(servo6_raw&0x00FFFF);
  dos.writeShort(servo7_raw&0x00FFFF);
  dos.writeShort(servo8_raw&0x00FFFF);
  dos.writeByte(port&0x00FF);
  dos.writeShort(servo9_raw&0x00FFFF);
  dos.writeShort(servo10_raw&0x00FFFF);
  dos.writeShort(servo11_raw&0x00FFFF);
  dos.writeShort(servo12_raw&0x00FFFF);
  dos.writeShort(servo13_raw&0x00FFFF);
  dos.writeShort(servo14_raw&0x00FFFF);
  dos.writeShort(servo15_raw&0x00FFFF);
  dos.writeShort(servo16_raw&0x00FFFF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 37);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[47] = crcl;
  buffer[48] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_SERVO_OUTPUT_RAW : " +   "  time_usec="+time_usec+  "  servo1_raw="+servo1_raw+  "  servo2_raw="+servo2_raw+  "  servo3_raw="+servo3_raw+  "  servo4_raw="+servo4_raw+  "  servo5_raw="+servo5_raw+  "  servo6_raw="+servo6_raw+  "  servo7_raw="+servo7_raw+  "  servo8_raw="+servo8_raw+  "  port="+port+  "  servo9_raw="+servo9_raw+  "  servo10_raw="+servo10_raw+  "  servo11_raw="+servo11_raw+  "  servo12_raw="+servo12_raw+  "  servo13_raw="+servo13_raw+  "  servo14_raw="+servo14_raw+  "  servo15_raw="+servo15_raw+  "  servo16_raw="+servo16_raw;}
}
