/**
 * Generated class : msg_distance_sensor
 * DO NOT MODIFY!
 **/
package org.mavlink.messages.lquac;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.mavlink.IMAVLinkCRC;
import org.mavlink.MAVLinkCRC;
import org.mavlink.messages.MAVLinkMessage;
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
    length = 14;
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
   * Direction the sensor faces from MAV_SENSOR_ORIENTATION enum.
   */
  public int orientation;
  /**
   * Measurement covariance in centimeters, 0 for unknown / invalid readings
   */
  public int covariance;
/**
 * Decode message with raw data
 */
public void decode(ByteBuffer dis) throws IOException {
  time_boot_ms = (int)dis.getInt()&0x00FFFFFFFF;
  min_distance = (int)dis.getShort()&0x00FFFF;
  max_distance = (int)dis.getShort()&0x00FFFF;
  current_distance = (int)dis.getShort()&0x00FFFF;
  type = (int)dis.get()&0x00FF;
  id = (int)dis.get()&0x00FF;
  orientation = (int)dis.get()&0x00FF;
  covariance = (int)dis.get()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+14];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.putInt((int)(time_boot_ms&0x00FFFFFFFF));
  dos.putShort((short)(min_distance&0x00FFFF));
  dos.putShort((short)(max_distance&0x00FFFF));
  dos.putShort((short)(current_distance&0x00FFFF));
  dos.put((byte)(type&0x00FF));
  dos.put((byte)(id&0x00FF));
  dos.put((byte)(orientation&0x00FF));
  dos.put((byte)(covariance&0x00FF));
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 14);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[20] = crcl;
  buffer[21] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_DISTANCE_SENSOR : " +   "  time_boot_ms="+time_boot_ms+  "  min_distance="+min_distance+  "  max_distance="+max_distance+  "  current_distance="+current_distance+  "  type="+type+  "  id="+id+  "  orientation="+orientation+  "  covariance="+covariance;}
}
