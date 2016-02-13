/**
 * Generated class : msg_altitude
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
 * Class msg_altitude
 * The current system altitude.
 **/
public class msg_altitude extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_ALTITUDE = 141;
  private static final long serialVersionUID = MAVLINK_MSG_ID_ALTITUDE;
  public msg_altitude() {
    this(1,1);
}
  public msg_altitude(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_ALTITUDE;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 32;
}

  /**
   * Timestamp (milliseconds since system boot)
   */
  public long time_usec;
  /**
   * This altitude measure is initialized on system boot and monotonic (it is never reset, but represents the local altitude change). The only guarantee on this field is that it will never be reset and is consistent within a flight. The recommended value for this field is the uncorrected barometric altitude at boot time. This altitude will also drift and vary between flights.
   */
  public float altitude_monotonic;
  /**
   * This altitude measure is strictly above mean sea level and might be non-monotonic (it might reset on events like GPS lock or when a new QNH value is set). It should be the altitude to which global altitude waypoints are compared to. Note that it is *not* the GPS altitude, however, most GPS modules already output AMSL by default and not the WGS84 altitude.
   */
  public float altitude_amsl;
  /**
   * This is the local altitude in the local coordinate frame. It is not the altitude above home, but in reference to the coordinate origin (0, 0, 0). It is up-positive.
   */
  public float altitude_local;
  /**
   * This is the altitude above the home position. It resets on each change of the current home position.
   */
  public float altitude_relative;
  /**
   * This is the altitude above terrain. It might be fed by a terrain database or an altimeter. Values smaller than -1000 should be interpreted as unknown.
   */
  public float altitude_terrain;
  /**
   * This is not the altitude, but the clear space below the system according to the fused clearance estimate. It generally should max out at the maximum range of e.g. the laser altimeter. It is generally a moving target. A negative value indicates no measurement available.
   */
  public float bottom_clearance;
/**
 * Decode message with raw data
 */
public void decode(ByteBuffer dis) throws IOException {
  time_usec = (long)dis.getLong();
  altitude_monotonic = (float)dis.getFloat();
  altitude_amsl = (float)dis.getFloat();
  altitude_local = (float)dis.getFloat();
  altitude_relative = (float)dis.getFloat();
  altitude_terrain = (float)dis.getFloat();
  bottom_clearance = (float)dis.getFloat();
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+32];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.putLong(time_usec);
  dos.putFloat(altitude_monotonic);
  dos.putFloat(altitude_amsl);
  dos.putFloat(altitude_local);
  dos.putFloat(altitude_relative);
  dos.putFloat(altitude_terrain);
  dos.putFloat(bottom_clearance);
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 32);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[38] = crcl;
  buffer[39] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_ALTITUDE : " +   "  time_usec="+time_usec+  "  altitude_monotonic="+altitude_monotonic+  "  altitude_amsl="+altitude_amsl+  "  altitude_local="+altitude_local+  "  altitude_relative="+altitude_relative+  "  altitude_terrain="+altitude_terrain+  "  bottom_clearance="+bottom_clearance;}
}
