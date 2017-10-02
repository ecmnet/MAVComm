/**
 * Generated class : msg_distance_sensor
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
 * Class msg_distance_sensor
 * 
 **/
public class msg_distance_sensor extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_DISTANCE_SENSOR = 132;
  private static final long serialVersionUID = MAVLINK_MSG_ID_DISTANCE_SENSOR;
  public msg_distance_sensor() {
    this(1,1);
}
  public msg_distance_sensor(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_DISTANCE_SENSOR;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 14;
}

  /**
   * Time since system boot
   */
  public long time_boot_ms;
  /**
   * Minimum distance the sensor can measure in centimeters
   */
  public int min_distance;
  /**
   * Maximum distance the sensor can measure in centimeters
   */
  public int max_distance;
  /**
   * Current distance reading
   */
  public int current_distance;
  /**
   * Type from MAV_DISTANCE_SENSOR enum.
   */
  public int type;
  /**
   * Onboard ID of the sensor
   */
  public int id;
  /**
   * Direction the sensor faces from MAV_SENSOR_ORIENTATION enum. downward-facing: ROTATION_PITCH_270, upward-facing: ROTATION_PITCH_90, backward-facing: ROTATION_PITCH_180, forward-facing: ROTATION_NONE, left-facing: ROTATION_YAW_90, right-facing: ROTATION_YAW_270
   */
  public int orientation;
  /**
   * Measurement covariance in centimeters, 0 for unknown / invalid readings
   */
  public int covariance;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_boot_ms = (int)dis.readInt()&0x00FFFFFFFF;
  min_distance = (int)dis.readUnsignedShort()&0x00FFFF;
  max_distance = (int)dis.readUnsignedShort()&0x00FFFF;
  current_distance = (int)dis.readUnsignedShort()&0x00FFFF;
  type = (int)dis.readUnsignedByte()&0x00FF;
  id = (int)dis.readUnsignedByte()&0x00FF;
  orientation = (int)dis.readUnsignedByte()&0x00FF;
  covariance = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+14];
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
  dos.writeShort(min_distance&0x00FFFF);
  dos.writeShort(max_distance&0x00FFFF);
  dos.writeShort(current_distance&0x00FFFF);
  dos.writeByte(type&0x00FF);
  dos.writeByte(id&0x00FF);
  dos.writeByte(orientation&0x00FF);
  dos.writeByte(covariance&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 14);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[24] = crcl;
  buffer[25] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_DISTANCE_SENSOR : " +   "  time_boot_ms="+time_boot_ms+  "  min_distance="+min_distance+  "  max_distance="+max_distance+  "  current_distance="+current_distance+  "  type="+type+  "  id="+id+  "  orientation="+orientation+  "  covariance="+covariance;}
}
