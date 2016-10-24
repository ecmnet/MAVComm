/**
 * Generated class : msg_altitude
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
   * Timestamp (micros since boot or Unix epoch)
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
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  altitude_monotonic = (float)dis.readFloat();
  altitude_amsl = (float)dis.readFloat();
  altitude_local = (float)dis.readFloat();
  altitude_relative = (float)dis.readFloat();
  altitude_terrain = (float)dis.readFloat();
  bottom_clearance = (float)dis.readFloat();
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+32];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFD);
  dos.writeByte(length & 0x00FF);
  dos.writeByte(incompat & 0x00FF);
  dos.writeByte(compat & 0x00FF);
  dos.writeByte(sequence & 0x00FF);
  dos.writeByte(sysId & 0x00FF);
  dos.writeByte(componentId & 0x00FF);
  dos.writeByte(messageType & 0x00FF);
  dos.writeByte((messageType >> 8) & 0x00FF);
  dos.writeByte((messageType >> 16) & 0x00FF);
  dos.writeLong(time_usec);
  dos.writeFloat(altitude_monotonic);
  dos.writeFloat(altitude_amsl);
  dos.writeFloat(altitude_local);
  dos.writeFloat(altitude_relative);
  dos.writeFloat(altitude_terrain);
  dos.writeFloat(bottom_clearance);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 32);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[42] = crcl;
  buffer[43] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_ALTITUDE : " +   "  time_usec="+time_usec+  "  altitude_monotonic="+altitude_monotonic+  "  altitude_amsl="+altitude_amsl+  "  altitude_local="+altitude_local+  "  altitude_relative="+altitude_relative+  "  altitude_terrain="+altitude_terrain+  "  bottom_clearance="+bottom_clearance;}
}
