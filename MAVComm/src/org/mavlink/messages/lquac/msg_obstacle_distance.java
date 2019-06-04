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
    payload_length = 166;
}

  /**
   * Timestamp (UNIX Epoch time or time since system boot). The receiving end can infer timestamp format (since 1.1.1970 or since system boot) by checking for the magnitude the number.
   */
  public long time_usec;
  /**
   * Distance of obstacles around the UAV with index 0 corresponding to local forward + angle_offset. A value of 0 means that the obstacle is right in front of the sensor. A value of max_distance +1 means no obstacle is present. A value of UINT16_MAX for unknown/not used. In a array element, one unit corresponds to 1cm.
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
   * Angular width in degrees of each array element. (Ignored if increment_f greater than 0).
   */
  public int increment;
  /**
   * Angular width in degrees of each array element as a float. If greater than 0 then this value is used instead of the uint8_t increment field.
   */
  public float increment_f;
  /**
   * Relative angle offset of the 0-index element in the distances array. Value of 0 corresponds to forward. Positive values are offsets to the right.
   */
  public float angle_offset;
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
  increment_f = (float)dis.readFloat();
  angle_offset = (float)dis.readFloat();
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+166];
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
  dos.writeFloat(increment_f);
  dos.writeFloat(angle_offset);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 166);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[176] = crcl;
  buffer[177] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_OBSTACLE_DISTANCE : " +   "  time_usec="+time_usec
+  "  distances[0]="+distances[0]
+  "  distances[1]="+distances[1]
+  "  distances[2]="+distances[2]
+  "  distances[3]="+distances[3]
+  "  distances[4]="+distances[4]
+  "  distances[5]="+distances[5]
+  "  distances[6]="+distances[6]
+  "  distances[7]="+distances[7]
+  "  distances[8]="+distances[8]
+  "  distances[9]="+distances[9]
+  "  distances[10]="+distances[10]
+  "  distances[11]="+distances[11]
+  "  distances[12]="+distances[12]
+  "  distances[13]="+distances[13]
+  "  distances[14]="+distances[14]
+  "  distances[15]="+distances[15]
+  "  distances[16]="+distances[16]
+  "  distances[17]="+distances[17]
+  "  distances[18]="+distances[18]
+  "  distances[19]="+distances[19]
+  "  distances[20]="+distances[20]
+  "  distances[21]="+distances[21]
+  "  distances[22]="+distances[22]
+  "  distances[23]="+distances[23]
+  "  distances[24]="+distances[24]
+  "  distances[25]="+distances[25]
+  "  distances[26]="+distances[26]
+  "  distances[27]="+distances[27]
+  "  distances[28]="+distances[28]
+  "  distances[29]="+distances[29]
+  "  distances[30]="+distances[30]
+  "  distances[31]="+distances[31]
+  "  distances[32]="+distances[32]
+  "  distances[33]="+distances[33]
+  "  distances[34]="+distances[34]
+  "  distances[35]="+distances[35]
+  "  distances[36]="+distances[36]
+  "  distances[37]="+distances[37]
+  "  distances[38]="+distances[38]
+  "  distances[39]="+distances[39]
+  "  distances[40]="+distances[40]
+  "  distances[41]="+distances[41]
+  "  distances[42]="+distances[42]
+  "  distances[43]="+distances[43]
+  "  distances[44]="+distances[44]
+  "  distances[45]="+distances[45]
+  "  distances[46]="+distances[46]
+  "  distances[47]="+distances[47]
+  "  distances[48]="+distances[48]
+  "  distances[49]="+distances[49]
+  "  distances[50]="+distances[50]
+  "  distances[51]="+distances[51]
+  "  distances[52]="+distances[52]
+  "  distances[53]="+distances[53]
+  "  distances[54]="+distances[54]
+  "  distances[55]="+distances[55]
+  "  distances[56]="+distances[56]
+  "  distances[57]="+distances[57]
+  "  distances[58]="+distances[58]
+  "  distances[59]="+distances[59]
+  "  distances[60]="+distances[60]
+  "  distances[61]="+distances[61]
+  "  distances[62]="+distances[62]
+  "  distances[63]="+distances[63]
+  "  distances[64]="+distances[64]
+  "  distances[65]="+distances[65]
+  "  distances[66]="+distances[66]
+  "  distances[67]="+distances[67]
+  "  distances[68]="+distances[68]
+  "  distances[69]="+distances[69]
+  "  distances[70]="+distances[70]
+  "  distances[71]="+distances[71]
+  "  min_distance="+min_distance
+  "  max_distance="+max_distance
+  "  sensor_type="+sensor_type
+  "  increment="+increment
+  "  increment_f="+increment_f
+  "  angle_offset="+angle_offset
;}
}
