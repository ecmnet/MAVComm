/**
 * Generated class : msg_optical_flow_rad
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
 * Class msg_optical_flow_rad
 * Optical flow from an angular rate flow sensor (e.g. PX4FLOW or mouse sensor)
 **/
public class msg_optical_flow_rad extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_OPTICAL_FLOW_RAD = 106;
  private static final long serialVersionUID = MAVLINK_MSG_ID_OPTICAL_FLOW_RAD;
  public msg_optical_flow_rad() {
    this(1,1);
}
  public msg_optical_flow_rad(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_OPTICAL_FLOW_RAD;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 44;
}

  /**
   * Timestamp (microseconds, synced to UNIX time or since system boot)
   */
  public long time_usec;
  /**
   * Integration time in microseconds. Divide integrated_x and integrated_y by the integration time to obtain average flow. The integration time also indicates the.
   */
  public long integration_time_us;
  /**
   * Flow in radians around X axis (Sensor RH rotation about the X axis induces a positive flow. Sensor linear motion along the positive Y axis induces a negative flow.)
   */
  public float integrated_x;
  /**
   * Flow in radians around Y axis (Sensor RH rotation about the Y axis induces a positive flow. Sensor linear motion along the positive X axis induces a positive flow.)
   */
  public float integrated_y;
  /**
   * RH rotation around X axis (rad)
   */
  public float integrated_xgyro;
  /**
   * RH rotation around Y axis (rad)
   */
  public float integrated_ygyro;
  /**
   * RH rotation around Z axis (rad)
   */
  public float integrated_zgyro;
  /**
   * Time in microseconds since the distance was sampled.
   */
  public long time_delta_distance_us;
  /**
   * Distance to the center of the flow field in meters. Positive value (including zero): distance known. Negative value: Unknown distance.
   */
  public float distance;
  /**
   * Temperature * 100 in centi-degrees Celsius
   */
  public int temperature;
  /**
   * Sensor ID
   */
  public int sensor_id;
  /**
   * Optical flow quality / confidence. 0: no valid flow, 255: maximum quality
   */
  public int quality;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  integration_time_us = (int)dis.readInt()&0x00FFFFFFFF;
  integrated_x = (float)dis.readFloat();
  integrated_y = (float)dis.readFloat();
  integrated_xgyro = (float)dis.readFloat();
  integrated_ygyro = (float)dis.readFloat();
  integrated_zgyro = (float)dis.readFloat();
  time_delta_distance_us = (int)dis.readInt()&0x00FFFFFFFF;
  distance = (float)dis.readFloat();
  temperature = (int)dis.readShort();
  sensor_id = (int)dis.readUnsignedByte()&0x00FF;
  quality = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+44];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFD);
  dos.writeByte(length & 0x00FF);
  dos.writeByte(incompat & 0x00FF);
  dos.writeByte(compat & 0x00FF);
  dos.writeByte(packet & 0x00FF);
  dos.writeByte(sysId & 0x00FF);
  dos.writeByte(componentId & 0x00FF);
  dos.writeByte(messageType & 0x00FF);
  dos.writeByte((messageType >> 8) & 0x00FF);
  dos.writeByte((messageType >> 16) & 0x00FF);
  dos.writeLong(time_usec);
  dos.writeInt((int)(integration_time_us&0x00FFFFFFFF));
  dos.writeFloat(integrated_x);
  dos.writeFloat(integrated_y);
  dos.writeFloat(integrated_xgyro);
  dos.writeFloat(integrated_ygyro);
  dos.writeFloat(integrated_zgyro);
  dos.writeInt((int)(time_delta_distance_us&0x00FFFFFFFF));
  dos.writeFloat(distance);
  dos.writeShort(temperature&0x00FFFF);
  dos.writeByte(sensor_id&0x00FF);
  dos.writeByte(quality&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 44);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[54] = crcl;
  buffer[55] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_OPTICAL_FLOW_RAD : " +   "  time_usec="+time_usec+  "  integration_time_us="+integration_time_us+  "  integrated_x="+integrated_x+  "  integrated_y="+integrated_y+  "  integrated_xgyro="+integrated_xgyro+  "  integrated_ygyro="+integrated_ygyro+  "  integrated_zgyro="+integrated_zgyro+  "  time_delta_distance_us="+time_delta_distance_us+  "  distance="+distance+  "  temperature="+temperature+  "  sensor_id="+sensor_id+  "  quality="+quality;}
}
