/**
 * Generated class : msg_adsb_vehicle
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
 * Class msg_adsb_vehicle
 * The location and information of an ADSB vehicle
 **/
public class msg_adsb_vehicle extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_ADSB_VEHICLE = 246;
  private static final long serialVersionUID = MAVLINK_MSG_ID_ADSB_VEHICLE;
  public msg_adsb_vehicle() {
    this(1,1);
}
  public msg_adsb_vehicle(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_ADSB_VEHICLE;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 38;
}

  /**
   * ICAO address
   */
  public long ICAO_address;
  /**
   * Latitude
   */
  public long lat;
  /**
   * Longitude
   */
  public long lon;
  /**
   * Altitude(ASL)
   */
  public long altitude;
  /**
   * Course over ground
   */
  public int heading;
  /**
   * The horizontal velocity
   */
  public int hor_velocity;
  /**
   * The vertical velocity. Positive is up
   */
  public int ver_velocity;
  /**
   * Bitmap to indicate various statuses including valid data fields
   */
  public int flags;
  /**
   * Squawk code
   */
  public int squawk;
  /**
   * ADSB altitude type.
   */
  public int altitude_type;
  /**
   * The callsign, 8+null
   */
  public char[] callsign = new char[9];
  public void setCallsign(String tmp) {
    int len = Math.min(tmp.length(), 9);
    for (int i=0; i<len; i++) {
      callsign[i] = tmp.charAt(i);
    }
    for (int i=len; i<9; i++) {
      callsign[i] = 0;
    }
  }
  public String getCallsign() {
    String result="";
    for (int i=0; i<9; i++) {
      if (callsign[i] != 0) result=result+callsign[i]; else break;
    }
    return result;
  }
  /**
   * ADSB emitter type.
   */
  public int emitter_type;
  /**
   * Time since last communication in seconds
   */
  public int tslc;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  ICAO_address = (int)dis.readInt()&0x00FFFFFFFF;
  lat = (int)dis.readInt();
  lon = (int)dis.readInt();
  altitude = (int)dis.readInt();
  heading = (int)dis.readUnsignedShort()&0x00FFFF;
  hor_velocity = (int)dis.readUnsignedShort()&0x00FFFF;
  ver_velocity = (int)dis.readShort();
  flags = (int)dis.readUnsignedShort()&0x00FFFF;
  squawk = (int)dis.readUnsignedShort()&0x00FFFF;
  altitude_type = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<9; i++) {
    callsign[i] = (char)dis.readByte();
  }
  emitter_type = (int)dis.readUnsignedByte()&0x00FF;
  tslc = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+38];
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
  dos.writeInt((int)(ICAO_address&0x00FFFFFFFF));
  dos.writeInt((int)(lat&0x00FFFFFFFF));
  dos.writeInt((int)(lon&0x00FFFFFFFF));
  dos.writeInt((int)(altitude&0x00FFFFFFFF));
  dos.writeShort(heading&0x00FFFF);
  dos.writeShort(hor_velocity&0x00FFFF);
  dos.writeShort(ver_velocity&0x00FFFF);
  dos.writeShort(flags&0x00FFFF);
  dos.writeShort(squawk&0x00FFFF);
  dos.writeByte(altitude_type&0x00FF);
  for (int i=0; i<9; i++) {
    dos.writeByte(callsign[i]);
  }
  dos.writeByte(emitter_type&0x00FF);
  dos.writeByte(tslc&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 38);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[48] = crcl;
  buffer[49] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_ADSB_VEHICLE : " +   "  ICAO_address="+ICAO_address
+  "  lat="+lat
+  "  lon="+lon
+  "  altitude="+altitude
+  "  heading="+heading
+  "  hor_velocity="+hor_velocity
+  "  ver_velocity="+ver_velocity
+  "  flags="+flags
+  "  squawk="+squawk
+  "  altitude_type="+altitude_type
+  "  callsign="+getCallsign()
+  "  emitter_type="+emitter_type
+  "  tslc="+tslc
;}
}
