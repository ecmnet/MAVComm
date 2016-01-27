/**
 * Generated class : msg_set_home_position
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
 * Class msg_set_home_position
 * The position the system will return to and land on. The position is set automatically by the system during the takeoff in case it was not explicitely set by the operator before or after. The global and local positions encode the position in the respective coordinate frames, while the q parameter encodes the orientation of the surface. Under normal conditions it describes the heading and terrain slope, which can be used by the aircraft to adjust the approach. The approach 3D vector describes the point to which the system should fly in normal flight mode and then perform a landing sequence along the vector.
 **/
public class msg_set_home_position extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_SET_HOME_POSITION = 243;
  private static final long serialVersionUID = MAVLINK_MSG_ID_SET_HOME_POSITION;
  public msg_set_home_position() {
    this(1,1);
}
  public msg_set_home_position(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_SET_HOME_POSITION;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 53;
}

  /**
   * Latitude (WGS84), in degrees * 1E7
   */
  public long latitude;
  /**
   * Longitude (WGS84, in degrees * 1E7
   */
  public long longitude;
  /**
   * Altitude (AMSL), in meters * 1000 (positive for up)
   */
  public long altitude;
  /**
   * Local X position of this position in the local coordinate frame
   */
  public float x;
  /**
   * Local Y position of this position in the local coordinate frame
   */
  public float y;
  /**
   * Local Z position of this position in the local coordinate frame
   */
  public float z;
  /**
   * World to surface normal and heading transformation of the takeoff position. Used to indicate the heading and slope of the ground
   */
  public float[] q = new float[4];
  /**
   * Local X position of the end of the approach vector. Multicopters should set this position based on their takeoff path. Grass-landing fixed wing aircraft should set it the same way as multicopters. Runway-landing fixed wing aircraft should set it to the opposite direction of the takeoff, assuming the takeoff happened from the threshold / touchdown zone.
   */
  public float approach_x;
  /**
   * Local Y position of the end of the approach vector. Multicopters should set this position based on their takeoff path. Grass-landing fixed wing aircraft should set it the same way as multicopters. Runway-landing fixed wing aircraft should set it to the opposite direction of the takeoff, assuming the takeoff happened from the threshold / touchdown zone.
   */
  public float approach_y;
  /**
   * Local Z position of the end of the approach vector. Multicopters should set this position based on their takeoff path. Grass-landing fixed wing aircraft should set it the same way as multicopters. Runway-landing fixed wing aircraft should set it to the opposite direction of the takeoff, assuming the takeoff happened from the threshold / touchdown zone.
   */
  public float approach_z;
  /**
   * System ID.
   */
  public int target_system;
/**
 * Decode message with raw data
 */
public void decode(ByteBuffer dis) throws IOException {
  latitude = (int)dis.getInt();
  longitude = (int)dis.getInt();
  altitude = (int)dis.getInt();
  x = (float)dis.getFloat();
  y = (float)dis.getFloat();
  z = (float)dis.getFloat();
  for (int i=0; i<4; i++) {
    q[i] = (float)dis.getFloat();
  }
  approach_x = (float)dis.getFloat();
  approach_y = (float)dis.getFloat();
  approach_z = (float)dis.getFloat();
  target_system = (int)dis.get()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+53];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.putInt((int)(latitude&0x00FFFFFFFF));
  dos.putInt((int)(longitude&0x00FFFFFFFF));
  dos.putInt((int)(altitude&0x00FFFFFFFF));
  dos.putFloat(x);
  dos.putFloat(y);
  dos.putFloat(z);
  for (int i=0; i<4; i++) {
    dos.putFloat(q[i]);
  }
  dos.putFloat(approach_x);
  dos.putFloat(approach_y);
  dos.putFloat(approach_z);
  dos.put((byte)(target_system&0x00FF));
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 53);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[59] = crcl;
  buffer[60] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_SET_HOME_POSITION : " +   "  latitude="+latitude+  "  longitude="+longitude+  "  altitude="+altitude+  "  x="+x+  "  y="+y+  "  z="+z+  "  q="+q+  "  approach_x="+approach_x+  "  approach_y="+approach_y+  "  approach_z="+approach_z+  "  target_system="+target_system;}
}
