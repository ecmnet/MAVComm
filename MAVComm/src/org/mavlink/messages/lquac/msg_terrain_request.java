/**
 * Generated class : msg_terrain_request
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
 * Class msg_terrain_request
 * Request for terrain data and terrain status
 **/
public class msg_terrain_request extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_TERRAIN_REQUEST = 133;
  private static final long serialVersionUID = MAVLINK_MSG_ID_TERRAIN_REQUEST;
  public msg_terrain_request() {
    this(1,1);
}
  public msg_terrain_request(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_TERRAIN_REQUEST;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 18;
}

  /**
   * Bitmask of requested 4x4 grids (row major 8x7 array of grids, 56 bits)
   */
  public long mask;
  /**
   * Latitude of SW corner of first grid (degrees *10^7)
   */
  public long lat;
  /**
   * Longitude of SW corner of first grid (in degrees *10^7)
   */
  public long lon;
  /**
   * Grid spacing in meters
   */
  public int grid_spacing;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  mask = (long)dis.readLong();
  lat = (int)dis.readInt();
  lon = (int)dis.readInt();
  grid_spacing = (int)dis.readUnsignedShort()&0x00FFFF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+18];
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
  dos.writeLong(mask);
  dos.writeInt((int)(lat&0x00FFFFFFFF));
  dos.writeInt((int)(lon&0x00FFFFFFFF));
  dos.writeShort(grid_spacing&0x00FFFF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 18);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[28] = crcl;
  buffer[29] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_TERRAIN_REQUEST : " +   "  mask="+mask+  "  lat="+lat+  "  lon="+lon+  "  grid_spacing="+grid_spacing;}
}
