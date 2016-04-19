/**
 * Generated class : msg_hil_optical_flow
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
 * Class msg_hil_optical_flow
 * Simulated optical flow from a flow sensor (e.g. PX4FLOW or optical mouse sensor)
 **/
public class msg_hil_optical_flow extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_HIL_OPTICAL_FLOW = 114;
  private static final long serialVersionUID = MAVLINK_MSG_ID_HIL_OPTICAL_FLOW;
  public msg_hil_optical_flow() {
    this(1,1);
}
  public msg_hil_optical_flow(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_HIL_OPTICAL_FLOW;
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
public void decode(ByteBuffer dis) throws IOException {
  time_usec = (long)dis.getLong();
  integration_time_us = (int)dis.getInt()&0x00FFFFFFFF;
  integrated_x = (float)dis.getFloat();
  integrated_y = (float)dis.getFloat();
  integrated_xgyro = (float)dis.getFloat();
  integrated_ygyro = (float)dis.getFloat();
  integrated_zgyro = (float)dis.getFloat();
  time_delta_distance_us = (int)dis.getInt()&0x00FFFFFFFF;
  distance = (float)dis.getFloat();
  temperature = (int)dis.getShort();
  sensor_id = (int)dis.get()&0x00FF;
  quality = (int)dis.get()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+44];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.putLong(time_usec);
  dos.putInt((int)(integration_time_us&0x00FFFFFFFF));
  dos.putFloat(integrated_x);
  dos.putFloat(integrated_y);
  dos.putFloat(integrated_xgyro);
  dos.putFloat(integrated_ygyro);
  dos.putFloat(integrated_zgyro);
  dos.putInt((int)(time_delta_distance_us&0x00FFFFFFFF));
  dos.putFloat(distance);
  dos.putShort((short)(temperature&0x00FFFF));
  dos.put((byte)(sensor_id&0x00FF));
  dos.put((byte)(quality&0x00FF));
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 44);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[50] = crcl;
  buffer[51] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_HIL_OPTICAL_FLOW : " +   "  time_usec="+time_usec+  "  integration_time_us="+integration_time_us+  "  integrated_x="+integrated_x+  "  integrated_y="+integrated_y+  "  integrated_xgyro="+integrated_xgyro+  "  integrated_ygyro="+integrated_ygyro+  "  integrated_zgyro="+integrated_zgyro+  "  time_delta_distance_us="+time_delta_distance_us+  "  distance="+distance+  "  temperature="+temperature+  "  sensor_id="+sensor_id+  "  quality="+quality;}
}
