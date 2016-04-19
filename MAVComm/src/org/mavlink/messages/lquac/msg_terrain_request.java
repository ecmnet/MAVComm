/**
 * Generated class : msg_terrain_request
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
    length = 18;
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
public void decode(ByteBuffer dis) throws IOException {
  mask = (long)dis.getLong();
  lat = (int)dis.getInt();
  lon = (int)dis.getInt();
  grid_spacing = (int)dis.getShort()&0x00FFFF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+18];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.putLong(mask);
  dos.putInt((int)(lat&0x00FFFFFFFF));
  dos.putInt((int)(lon&0x00FFFFFFFF));
  dos.putShort((short)(grid_spacing&0x00FFFF));
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 18);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[24] = crcl;
  buffer[25] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_TERRAIN_REQUEST : " +   "  mask="+mask+  "  lat="+lat+  "  lon="+lon+  "  grid_spacing="+grid_spacing;}
}
