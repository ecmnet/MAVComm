/**
 * Generated class : msg_utm_global_position
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
 * Class msg_utm_global_position
 * The global position resulting from GPS and sensor fusion.
 **/
public class msg_utm_global_position extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_UTM_GLOBAL_POSITION = 340;
  private static final long serialVersionUID = MAVLINK_MSG_ID_UTM_GLOBAL_POSITION;
  public msg_utm_global_position() {
    this(1,1);
}
  public msg_utm_global_position(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_UTM_GLOBAL_POSITION;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 70;
}

  /**
   * Time of applicability of position (microseconds since UNIX epoch).
   */
  public long time;
  /**
   * Latitude (WGS84)
   */
  public long lat;
  /**
   * Longitude (WGS84)
   */
  public long lon;
  /**
   * Altitude (WGS84)
   */
  public long alt;
  /**
   * Altitude above ground
   */
  public long relative_alt;
  /**
   * Next waypoint, latitude (WGS84)
   */
  public long next_lat;
  /**
   * Next waypoint, longitude (WGS84)
   */
  public long next_lon;
  /**
   * Next waypoint, altitude (WGS84)
   */
  public long next_alt;
  /**
   * Ground X speed (latitude, positive north)
   */
  public int vx;
  /**
   * Ground Y speed (longitude, positive east)
   */
  public int vy;
  /**
   * Ground Z speed (altitude, positive down)
   */
  public int vz;
  /**
   * Horizontal position uncertainty (standard deviation)
   */
  public int h_acc;
  /**
   * Altitude uncertainty (standard deviation)
   */
  public int v_acc;
  /**
   * Speed uncertainty (standard deviation)
   */
  public int vel_acc;
  /**
   * Seconds * 1E2 until next update. Set to 0 if unknown or in data driven mode.
   */
  public int update_rate;
  /**
   * Unique UAS ID.
   */
  public int[] uas_id = new int[18];
  /**
   * Flight state
   */
  public int flight_state;
  /**
   * Bitwise OR combination of the data available flags.
   */
  public int flags;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time = (long)dis.readLong();
  lat = (int)dis.readInt();
  lon = (int)dis.readInt();
  alt = (int)dis.readInt();
  relative_alt = (int)dis.readInt();
  next_lat = (int)dis.readInt();
  next_lon = (int)dis.readInt();
  next_alt = (int)dis.readInt();
  vx = (int)dis.readShort();
  vy = (int)dis.readShort();
  vz = (int)dis.readShort();
  h_acc = (int)dis.readUnsignedShort()&0x00FFFF;
  v_acc = (int)dis.readUnsignedShort()&0x00FFFF;
  vel_acc = (int)dis.readUnsignedShort()&0x00FFFF;
  update_rate = (int)dis.readUnsignedShort()&0x00FFFF;
  for (int i=0; i<18; i++) {
    uas_id[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
  flight_state = (int)dis.readUnsignedByte()&0x00FF;
  flags = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+70];
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
  dos.writeLong(time);
  dos.writeInt((int)(lat&0x00FFFFFFFF));
  dos.writeInt((int)(lon&0x00FFFFFFFF));
  dos.writeInt((int)(alt&0x00FFFFFFFF));
  dos.writeInt((int)(relative_alt&0x00FFFFFFFF));
  dos.writeInt((int)(next_lat&0x00FFFFFFFF));
  dos.writeInt((int)(next_lon&0x00FFFFFFFF));
  dos.writeInt((int)(next_alt&0x00FFFFFFFF));
  dos.writeShort(vx&0x00FFFF);
  dos.writeShort(vy&0x00FFFF);
  dos.writeShort(vz&0x00FFFF);
  dos.writeShort(h_acc&0x00FFFF);
  dos.writeShort(v_acc&0x00FFFF);
  dos.writeShort(vel_acc&0x00FFFF);
  dos.writeShort(update_rate&0x00FFFF);
  for (int i=0; i<18; i++) {
    dos.writeByte(uas_id[i]&0x00FF);
  }
  dos.writeByte(flight_state&0x00FF);
  dos.writeByte(flags&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 70);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[80] = crcl;
  buffer[81] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_UTM_GLOBAL_POSITION : " +   "  time="+time+  "  lat="+lat+  "  lon="+lon+  "  alt="+alt+  "  relative_alt="+relative_alt+  "  next_lat="+next_lat+  "  next_lon="+next_lon+  "  next_alt="+next_alt+  "  vx="+vx+  "  vy="+vy+  "  vz="+vz+  "  h_acc="+h_acc+  "  v_acc="+v_acc+  "  vel_acc="+vel_acc+  "  update_rate="+update_rate+  "  uas_id="+uas_id+  "  flight_state="+flight_state+  "  flags="+flags;}
}
