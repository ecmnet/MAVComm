/**
 * Generated class : msg_terrain_report
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
 * Class msg_terrain_report
 * Response from a TERRAIN_CHECK request
 **/
public class msg_terrain_report extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_TERRAIN_REPORT = 136;
  private static final long serialVersionUID = MAVLINK_MSG_ID_TERRAIN_REPORT;
  public msg_terrain_report() {
    this(1,1);
}
  public msg_terrain_report(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_TERRAIN_REPORT;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 22;
}

  /**
   * Latitude (degrees *10^7)
   */
  public long lat;
  /**
   * Longitude (degrees *10^7)
   */
  public long lon;
  /**
   * Terrain height in meters AMSL
   */
  public float terrain_height;
  /**
   * Current vehicle height above lat/lon terrain height (meters)
   */
  public float current_height;
  /**
   * grid spacing (zero if terrain at this location unavailable)
   */
  public int spacing;
  /**
   * Number of 4x4 terrain blocks waiting to be received or read from disk
   */
  public int pending;
  /**
   * Number of 4x4 terrain blocks in memory
   */
  public int loaded;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  lat = (int)dis.readInt();
  lon = (int)dis.readInt();
  terrain_height = (float)dis.readFloat();
  current_height = (float)dis.readFloat();
  spacing = (int)dis.readUnsignedShort()&0x00FFFF;
  pending = (int)dis.readUnsignedShort()&0x00FFFF;
  loaded = (int)dis.readUnsignedShort()&0x00FFFF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+22];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFE);
  dos.writeByte(length & 0x00FF);
  dos.writeByte(sequence & 0x00FF);
  dos.writeByte(sysId & 0x00FF);
  dos.writeByte(componentId & 0x00FF);
  dos.writeByte(messageType & 0x00FF);
  dos.writeInt((int)(lat&0x00FFFFFFFF));
  dos.writeInt((int)(lon&0x00FFFFFFFF));
  dos.writeFloat(terrain_height);
  dos.writeFloat(current_height);
  dos.writeShort(spacing&0x00FFFF);
  dos.writeShort(pending&0x00FFFF);
  dos.writeShort(loaded&0x00FFFF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 22);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[28] = crcl;
  buffer[29] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_TERRAIN_REPORT : " +   "  lat="+lat+  "  lon="+lon+  "  terrain_height="+terrain_height+  "  current_height="+current_height+  "  spacing="+spacing+  "  pending="+pending+  "  loaded="+loaded;}
}
