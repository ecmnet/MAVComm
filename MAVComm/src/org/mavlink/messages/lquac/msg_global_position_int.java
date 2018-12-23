/**
 * Generated class : msg_global_position_int
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
 * Class msg_global_position_int
 * The filtered global position (e.g. fused GPS and accelerometers). The position is in GPS-frame (right-handed, Z-up). It
               is designed as scaled integer message since the resolution of float is not sufficient.
 **/
public class msg_global_position_int extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_GLOBAL_POSITION_INT = 33;
  private static final long serialVersionUID = MAVLINK_MSG_ID_GLOBAL_POSITION_INT;
  public msg_global_position_int() {
    this(1,1);
}
  public msg_global_position_int(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_GLOBAL_POSITION_INT;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 28;
}

  /**
   * Timestamp (time since system boot).
   */
  public long time_boot_ms;
  /**
   * Latitude, expressed
   */
  public long lat;
  /**
   * Longitude, expressed
   */
  public long lon;
  /**
   * Altitude (MSL). Note that virtually all GPS modules provide both WGS84 and MSL.
   */
  public long alt;
  /**
   * Altitude above ground
   */
  public long relative_alt;
  /**
   * Ground X Speed (Latitude, positive north)
   */
  public int vx;
  /**
   * Ground Y Speed (Longitude, positive east)
   */
  public int vy;
  /**
   * Ground Z Speed (Altitude, positive down)
   */
  public int vz;
  /**
   * Vehicle heading (yaw angle), 0.0..359.99 degrees. If unknown, set to: UINT16_MAX
   */
  public int hdg;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_boot_ms = (int)dis.readInt()&0x00FFFFFFFF;
  lat = (int)dis.readInt();
  lon = (int)dis.readInt();
  alt = (int)dis.readInt();
  relative_alt = (int)dis.readInt();
  vx = (int)dis.readShort();
  vy = (int)dis.readShort();
  vz = (int)dis.readShort();
  hdg = (int)dis.readUnsignedShort()&0x00FFFF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+28];
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
  dos.writeInt((int)(time_boot_ms&0x00FFFFFFFF));
  dos.writeInt((int)(lat&0x00FFFFFFFF));
  dos.writeInt((int)(lon&0x00FFFFFFFF));
  dos.writeInt((int)(alt&0x00FFFFFFFF));
  dos.writeInt((int)(relative_alt&0x00FFFFFFFF));
  dos.writeShort(vx&0x00FFFF);
  dos.writeShort(vy&0x00FFFF);
  dos.writeShort(vz&0x00FFFF);
  dos.writeShort(hdg&0x00FFFF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 28);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[38] = crcl;
  buffer[39] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_GLOBAL_POSITION_INT : " +   "  time_boot_ms="+time_boot_ms
+  "  lat="+lat
+  "  lon="+lon
+  "  alt="+alt
+  "  relative_alt="+relative_alt
+  "  vx="+vx
+  "  vy="+vy
+  "  vz="+vz
+  "  hdg="+hdg
;}
}
