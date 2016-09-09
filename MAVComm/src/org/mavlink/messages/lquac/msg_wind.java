/**
 * Generated class : msg_wind
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
 * Class msg_wind
 * 
 **/
public class msg_wind extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_WIND = 231;
  private static final long serialVersionUID = MAVLINK_MSG_ID_WIND;
  public msg_wind() {
    this(1,1);
}
  public msg_wind(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_WIND;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 40;
}

  /**
   * Timestamp (micros since boot or Unix epoch)
   */
  public long time_usec;
  /**
   * Wind in X (NED) direction in m/s
   */
  public float wind_x;
  /**
   * Wind in Y (NED) direction in m/s
   */
  public float wind_y;
  /**
   * Wind in Z (NED) direction in m/s
   */
  public float wind_z;
  /**
   * Variability of the wind in XY. RMS of a 1 Hz lowpassed wind estimate.
   */
  public float var_horiz;
  /**
   * Variability of the wind in Z. RMS of a 1 Hz lowpassed wind estimate.
   */
  public float var_vert;
  /**
   * AMSL altitude (m) this measurement was taken at
   */
  public float wind_alt;
  /**
   * Horizontal position 1-STD accuracy relative to the EKF local origin (m)
   */
  public float pos_horiz_accuracy;
  /**
   * Vertical position 1-STD accuracy relative to the EKF local origin (m)
   */
  public float pos_vert_accuracy;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  wind_x = (float)dis.readFloat();
  wind_y = (float)dis.readFloat();
  wind_z = (float)dis.readFloat();
  var_horiz = (float)dis.readFloat();
  var_vert = (float)dis.readFloat();
  wind_alt = (float)dis.readFloat();
  pos_horiz_accuracy = (float)dis.readFloat();
  pos_vert_accuracy = (float)dis.readFloat();
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+40];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFE);
  dos.writeByte(length & 0x00FF);
  dos.writeByte(sequence & 0x00FF);
  dos.writeByte(sysId & 0x00FF);
  dos.writeByte(componentId & 0x00FF);
  dos.writeByte(messageType & 0x00FF);
  dos.writeLong(time_usec);
  dos.writeFloat(wind_x);
  dos.writeFloat(wind_y);
  dos.writeFloat(wind_z);
  dos.writeFloat(var_horiz);
  dos.writeFloat(var_vert);
  dos.writeFloat(wind_alt);
  dos.writeFloat(pos_horiz_accuracy);
  dos.writeFloat(pos_vert_accuracy);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 40);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[46] = crcl;
  buffer[47] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_WIND : " +   "  time_usec="+time_usec+  "  wind_x="+wind_x+  "  wind_y="+wind_y+  "  wind_z="+wind_z+  "  var_horiz="+var_horiz+  "  var_vert="+var_vert+  "  wind_alt="+wind_alt+  "  pos_horiz_accuracy="+pos_horiz_accuracy+  "  pos_vert_accuracy="+pos_vert_accuracy;}
}
