/**
 * Generated class : msg_high_latency2
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
 * Class msg_high_latency2
 * Message appropriate for high latency connections like Iridium (version 2)
 **/
public class msg_high_latency2 extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_HIGH_LATENCY2 = 235;
  private static final long serialVersionUID = MAVLINK_MSG_ID_HIGH_LATENCY2;
  public msg_high_latency2() {
    this(1,1);
}
  public msg_high_latency2(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_HIGH_LATENCY2;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 42;
}

  /**
   * Timestamp (milliseconds since boot or Unix epoch)
   */
  public long timestamp;
  /**
   * Latitude
   */
  public long latitude;
  /**
   * Longitude
   */
  public long longitude;
  /**
   * A bitfield for use for autopilot-specific flags (2 byte version).
   */
  public int custom_mode;
  /**
   * Altitude above mean sea level
   */
  public int altitude;
  /**
   * Altitude setpoint
   */
  public int target_altitude;
  /**
   * Distance to target waypoint or position
   */
  public int target_distance;
  /**
   * Current waypoint number
   */
  public int wp_num;
  /**
   * Bitmap of failure flags.
   */
  public int failure_flags;
  /**
   * Type of the MAV (quadrotor, helicopter, etc.)
   */
  public int type;
  /**
   * Autopilot type / class.
   */
  public int autopilot;
  /**
   * Heading
   */
  public int heading;
  /**
   * Heading setpoint
   */
  public int target_heading;
  /**
   * Throttle
   */
  public int throttle;
  /**
   * Airspeed
   */
  public int airspeed;
  /**
   * Airspeed setpoint
   */
  public int airspeed_sp;
  /**
   * Groundspeed
   */
  public int groundspeed;
  /**
   * Windspeed
   */
  public int windspeed;
  /**
   * Wind heading
   */
  public int wind_heading;
  /**
   * Maximum error horizontal position since last message
   */
  public int eph;
  /**
   * Maximum error vertical position since last message
   */
  public int epv;
  /**
   * Air temperature from airspeed sensor
   */
  public int temperature_air;
  /**
   * Maximum climb rate magnitude since last message
   */
  public int climb_rate;
  /**
   * Battery (percentage, -1 for DNU)
   */
  public int battery;
  /**
   * Field for custom payload.
   */
  public int custom0;
  /**
   * Field for custom payload.
   */
  public int custom1;
  /**
   * Field for custom payload.
   */
  public int custom2;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  timestamp = (int)dis.readInt()&0x00FFFFFFFF;
  latitude = (int)dis.readInt();
  longitude = (int)dis.readInt();
  custom_mode = (int)dis.readUnsignedShort()&0x00FFFF;
  altitude = (int)dis.readShort();
  target_altitude = (int)dis.readShort();
  target_distance = (int)dis.readUnsignedShort()&0x00FFFF;
  wp_num = (int)dis.readUnsignedShort()&0x00FFFF;
  failure_flags = (int)dis.readUnsignedShort()&0x00FFFF;
  type = (int)dis.readUnsignedByte()&0x00FF;
  autopilot = (int)dis.readUnsignedByte()&0x00FF;
  heading = (int)dis.readUnsignedByte()&0x00FF;
  target_heading = (int)dis.readUnsignedByte()&0x00FF;
  throttle = (int)dis.readUnsignedByte()&0x00FF;
  airspeed = (int)dis.readUnsignedByte()&0x00FF;
  airspeed_sp = (int)dis.readUnsignedByte()&0x00FF;
  groundspeed = (int)dis.readUnsignedByte()&0x00FF;
  windspeed = (int)dis.readUnsignedByte()&0x00FF;
  wind_heading = (int)dis.readUnsignedByte()&0x00FF;
  eph = (int)dis.readUnsignedByte()&0x00FF;
  epv = (int)dis.readUnsignedByte()&0x00FF;
  temperature_air = (int)dis.readByte();
  climb_rate = (int)dis.readByte();
  battery = (int)dis.readByte();
  custom0 = (int)dis.readByte();
  custom1 = (int)dis.readByte();
  custom2 = (int)dis.readByte();
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+42];
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
  dos.writeInt((int)(timestamp&0x00FFFFFFFF));
  dos.writeInt((int)(latitude&0x00FFFFFFFF));
  dos.writeInt((int)(longitude&0x00FFFFFFFF));
  dos.writeShort(custom_mode&0x00FFFF);
  dos.writeShort(altitude&0x00FFFF);
  dos.writeShort(target_altitude&0x00FFFF);
  dos.writeShort(target_distance&0x00FFFF);
  dos.writeShort(wp_num&0x00FFFF);
  dos.writeShort(failure_flags&0x00FFFF);
  dos.writeByte(type&0x00FF);
  dos.writeByte(autopilot&0x00FF);
  dos.writeByte(heading&0x00FF);
  dos.writeByte(target_heading&0x00FF);
  dos.writeByte(throttle&0x00FF);
  dos.writeByte(airspeed&0x00FF);
  dos.writeByte(airspeed_sp&0x00FF);
  dos.writeByte(groundspeed&0x00FF);
  dos.writeByte(windspeed&0x00FF);
  dos.writeByte(wind_heading&0x00FF);
  dos.writeByte(eph&0x00FF);
  dos.writeByte(epv&0x00FF);
  dos.write(temperature_air&0x00FF);
  dos.write(climb_rate&0x00FF);
  dos.write(battery&0x00FF);
  dos.write(custom0&0x00FF);
  dos.write(custom1&0x00FF);
  dos.write(custom2&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 42);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[52] = crcl;
  buffer[53] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_HIGH_LATENCY2 : " +   "  timestamp="+timestamp
+  "  latitude="+latitude
+  "  longitude="+longitude
+  "  custom_mode="+custom_mode
+  "  altitude="+altitude
+  "  target_altitude="+target_altitude
+  "  target_distance="+target_distance
+  "  wp_num="+wp_num
+  "  failure_flags="+failure_flags
+  "  type="+type
+  "  autopilot="+autopilot
+  "  heading="+heading
+  "  target_heading="+target_heading
+  "  throttle="+throttle
+  "  airspeed="+airspeed
+  "  airspeed_sp="+airspeed_sp
+  "  groundspeed="+groundspeed
+  "  windspeed="+windspeed
+  "  wind_heading="+wind_heading
+  "  eph="+eph
+  "  epv="+epv
+  "  temperature_air="+temperature_air
+  "  climb_rate="+climb_rate
+  "  battery="+battery
+  "  custom0="+custom0
+  "  custom1="+custom1
+  "  custom2="+custom2
;}
}
