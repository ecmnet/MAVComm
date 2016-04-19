/**
 * Generated class : msg_follow_target
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
 * Class msg_follow_target
 * current motion information from a designated system
 **/
public class msg_follow_target extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_FOLLOW_TARGET = 144;
  private static final long serialVersionUID = MAVLINK_MSG_ID_FOLLOW_TARGET;
  public msg_follow_target() {
    this(1,1);
}
  public msg_follow_target(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_FOLLOW_TARGET;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 93;
}

  /**
   * Timestamp in milliseconds since system boot
   */
  public long timestamp;
  /**
   * button states or switches of a tracker device
   */
  public long custom_state;
  /**
   * Latitude (WGS84), in degrees * 1E7
   */
  public long lat;
  /**
   * Longitude (WGS84), in degrees * 1E7
   */
  public long lon;
  /**
   * AMSL, in meters
   */
  public float alt;
  /**
   * target velocity (0,0,0) for unknown
   */
  public float[] vel = new float[3];
  /**
   * linear target acceleration (0,0,0) for unknown
   */
  public float[] acc = new float[3];
  /**
   * (1 0 0 0 for unknown)
   */
  public float[] attitude_q = new float[4];
  /**
   * (0 0 0 for unknown)
   */
  public float[] rates = new float[3];
  /**
   * eph epv
   */
  public float[] position_cov = new float[3];
  /**
   * bit positions for tracker reporting capabilities (POS = 0, VEL = 1, ACCEL = 2, ATT + RATES = 3)
   */
  public int est_capabilities;
/**
 * Decode message with raw data
 */
public void decode(ByteBuffer dis) throws IOException {
  timestamp = (long)dis.getLong();
  custom_state = (long)dis.getLong();
  lat = (int)dis.getInt();
  lon = (int)dis.getInt();
  alt = (float)dis.getFloat();
  for (int i=0; i<3; i++) {
    vel[i] = (float)dis.getFloat();
  }
  for (int i=0; i<3; i++) {
    acc[i] = (float)dis.getFloat();
  }
  for (int i=0; i<4; i++) {
    attitude_q[i] = (float)dis.getFloat();
  }
  for (int i=0; i<3; i++) {
    rates[i] = (float)dis.getFloat();
  }
  for (int i=0; i<3; i++) {
    position_cov[i] = (float)dis.getFloat();
  }
  est_capabilities = (int)dis.get()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+93];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.putLong(timestamp);
  dos.putLong(custom_state);
  dos.putInt((int)(lat&0x00FFFFFFFF));
  dos.putInt((int)(lon&0x00FFFFFFFF));
  dos.putFloat(alt);
  for (int i=0; i<3; i++) {
    dos.putFloat(vel[i]);
  }
  for (int i=0; i<3; i++) {
    dos.putFloat(acc[i]);
  }
  for (int i=0; i<4; i++) {
    dos.putFloat(attitude_q[i]);
  }
  for (int i=0; i<3; i++) {
    dos.putFloat(rates[i]);
  }
  for (int i=0; i<3; i++) {
    dos.putFloat(position_cov[i]);
  }
  dos.put((byte)(est_capabilities&0x00FF));
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 93);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[99] = crcl;
  buffer[100] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_FOLLOW_TARGET : " +   "  timestamp="+timestamp+  "  custom_state="+custom_state+  "  lat="+lat+  "  lon="+lon+  "  alt="+alt+  "  vel="+vel+  "  acc="+acc+  "  attitude_q="+attitude_q+  "  rates="+rates+  "  position_cov="+position_cov+  "  est_capabilities="+est_capabilities;}
}
