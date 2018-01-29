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
 * (DRAFT) Message appropriate for high latency connections like Iridium (version 2)
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
   * Latitude, expressed as degrees * 1E7
   */
  public long latitude;
  /**
   * Longitude, expressed as degrees * 1E7
   */
  public long longitude;
  /**
   * Altitude above mean sea level
   */
  public int altitude;
  /**
   * Altitude setpoint
   */
  public int target_altitude;
  /**
   * Distance to target waypoint or position (meters / 10)
   */
  public int target_distance;
  /**
   * Current waypoint number
   */
  public int wp_num;
  /**
   * Indicates failures as defined in MAV_FAILURE_FLAG ENUM.
   */
  public int failure_flags;
  /**
   * Type of the MAV (quadrotor, helicopter, etc., up to 15 types, defined in MAV_TYPE ENUM)
   */
  public int type;
  /**
   * Autopilot type / class. defined in MAV_AUTOPILOT ENUM
   */
  public int autopilot;
  /**
   * Flight Mode of the vehicle as defined in the FLIGHT_MODE ENUM
   */
  public int flight_mode;
  /**
   * Heading (degrees / 2)
   */
  public int heading;
  /**
   * Heading setpoint (degrees / 2)
   */
  public int target_heading;
  /**
   * Throttle (percentage)
   */
  public int throttle;
  /**
   * Airspeed (m/s * 5)
   */
  public int airspeed;
  /**
   * Airspeed setpoint (m/s * 5)
   */
  public int airspeed_sp;
  /**
   * Groundspeed (m/s * 5)
   */
  public int groundspeed;
  /**
   * Windspeed (m/s * 5)
   */
  public int windspeed;
  /**
   * Wind heading (deg / 2)
   */
  public int wind_heading;
  /**
   * Maximum error horizontal position since last message (m * 10)
   */
  public int eph;
  /**
   * Maximum error vertical position since last message (m * 10)
   */
  public int epv;
  /**
   * Air temperature (degrees C) from airspeed sensor
   */
  public int temperature_air;
  /**
   * Maximum climb rate magnitude since last message (m/s * 10)
   */
  public int climb_rate;
  /**
   * Battery (percentage, -1 for DNU)
   */
  public int battery;
  /**
   * Indicates if a failsafe mode is triggered, defined in MAV_FAILSAFE_FLAG ENUM
   */
  public int failsafe;
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
  altitude = (int)dis.readShort();
  target_altitude = (int)dis.readShort();
  target_distance = (int)dis.readUnsignedShort()&0x00FFFF;
  wp_num = (int)dis.readUnsignedShort()&0x00FFFF;
  failure_flags = (int)dis.readUnsignedShort()&0x00FFFF;
  type = (int)dis.readUnsignedByte()&0x00FF;
  autopilot = (int)dis.readUnsignedByte()&0x00FF;
  flight_mode = (int)dis.readUnsignedByte()&0x00FF;
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
  failsafe = (int)dis.readUnsignedByte()&0x00FF;
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
  dos.writeShort(altitude&0x00FFFF);
  dos.writeShort(target_altitude&0x00FFFF);
  dos.writeShort(target_distance&0x00FFFF);
  dos.writeShort(wp_num&0x00FFFF);
  dos.writeShort(failure_flags&0x00FFFF);
  dos.writeByte(type&0x00FF);
  dos.writeByte(autopilot&0x00FF);
  dos.writeByte(flight_mode&0x00FF);
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
  dos.writeByte(failsafe&0x00FF);
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
return "MAVLINK_MSG_ID_HIGH_LATENCY2 : " +   "  timestamp="+timestamp+  "  latitude="+latitude+  "  longitude="+longitude+  "  altitude="+altitude+  "  target_altitude="+target_altitude+  "  target_distance="+target_distance+  "  wp_num="+wp_num+  "  failure_flags="+failure_flags+  "  type="+type+  "  autopilot="+autopilot+  "  flight_mode="+flight_mode+  "  heading="+heading+  "  target_heading="+target_heading+  "  throttle="+throttle+  "  airspeed="+airspeed+  "  airspeed_sp="+airspeed_sp+  "  groundspeed="+groundspeed+  "  windspeed="+windspeed+  "  wind_heading="+wind_heading+  "  eph="+eph+  "  epv="+epv+  "  temperature_air="+temperature_air+  "  climb_rate="+climb_rate+  "  battery="+battery+  "  failsafe="+failsafe+  "  custom0="+custom0+  "  custom1="+custom1+  "  custom2="+custom2;}
}
