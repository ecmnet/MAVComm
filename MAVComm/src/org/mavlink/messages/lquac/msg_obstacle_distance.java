/**
 * Generated class : msg_obstacle_distance
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
 * Class msg_obstacle_distance
 * Obstacle distances in front of the sensor, starting from the left in increment degrees to the right
 **/
public class msg_obstacle_distance extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_OBSTACLE_DISTANCE = 330;
  private static final long serialVersionUID = MAVLINK_MSG_ID_OBSTACLE_DISTANCE;
  public msg_obstacle_distance() {
    this(1,1);
}
  public msg_obstacle_distance(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_OBSTACLE_DISTANCE;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 158;
}

  /**
   * Timestamp (UNIX Epoch time or time since system boot). The receiving end can infer timestamp format (since 1.1.1970 or since system boot) by checking for the magnitude the number.
   */
  public long time_usec;
  /**
   * Distance of obstacles around the UAV with index 0 corresponding to local North. A value of 0 means that the obstacle is right in front of the sensor. A value of max_distance +1 means no obstacle is present. A value of UINT16_MAX for unknown/not used. In a array element, one unit corresponds to 1cm.
   */
  public int[] distances = new int[72];
  /**
   * Minimum distance the sensor can measure.
   */
  public int min_distance;
  /**
   * Maximum distance the sensor can measure.
   */
  public int max_distance;
  /**
   * Class id of the distance sensor type.
   */
  public int sensor_type;
  /**
   * Angular width in degrees of each array element.
   */
  public int increment;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  for (int i=0; i<72; i++) {
    distances[i] = (int)dis.readUnsignedShort()&0x00FFFF;
  }
  min_distance = (int)dis.readUnsignedShort()&0x00FFFF;
  max_distance = (int)dis.readUnsignedShort()&0x00FFFF;
  sensor_type = (int)dis.readUnsignedByte()&0x00FF;
  increment = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+158];
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
  for (int i=0; i<72; i++) {
    dos.writeShort(distances[i]&0x00FFFF);
  }
  dos.writeShort(min_distance&0x00FFFF);
  dos.writeShort(max_distance&0x00FFFF);
  dos.writeByte(sensor_type&0x00FF);
  dos.writeByte(increment&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 158);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[168] = crcl;
  buffer[169] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_OBSTACLE_DISTANCE : " +   "  time_usec="+time_usec+  "  distances="+distances+  "  min_distance="+min_distance+  "  max_distance="+max_distance+  "  sensor_type="+sensor_type+  "  increment="+increment;}
}
