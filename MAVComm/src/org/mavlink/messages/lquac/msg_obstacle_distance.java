/**
 * Generated class : msg_obstacle_distance
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
    payload_length = 22;
}

  /**
   * Timestamp (microseconds since system boot or since UNIX epoch)
   */
  public long time_usec;
  /**
   * Class id of the distance sensor type.
   */
  public int estimator_type;
  /**
   * Distance of obstacles in front of the sensor starting on the left side. A value of 0 means no obstacle. In a array element, each unit corresponds to 1m.
   */
  public int[] distances = new int[12];
  /**
   * Angular width in degrees of each array element.
   */
  public int increment;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  estimator_type = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<12; i++) {
    distances[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
  increment = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+22];
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
  dos.writeByte(estimator_type&0x00FF);
  for (int i=0; i<12; i++) {
    dos.writeByte(distances[i]&0x00FF);
  }
  dos.writeByte(increment&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 22);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[32] = crcl;
  buffer[33] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_OBSTACLE_DISTANCE : " +   "  time_usec="+time_usec+  "  estimator_type="+estimator_type+  "  distances="+distances+  "  increment="+increment;}
}
