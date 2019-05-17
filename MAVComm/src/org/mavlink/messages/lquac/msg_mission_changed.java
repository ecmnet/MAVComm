/**
 * Generated class : msg_mission_changed
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
 * Class msg_mission_changed
 * A broadcast message to notify any ground station or SDK if a mission, geofence or safe points have changed on the vehicle.
 **/
public class msg_mission_changed extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_MISSION_CHANGED = 52;
  private static final long serialVersionUID = MAVLINK_MSG_ID_MISSION_CHANGED;
  public msg_mission_changed() {
    this(1,1);
}
  public msg_mission_changed(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_MISSION_CHANGED;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 7;
}

  /**
   * Start index for partial mission change (-1 for all items).
   */
  public int start_index;
  /**
   * End index of a partial mission change. -1 is a synonym for the last mission item (i.e. selects all items from start_index). Ignore field if start_index=-1.
   */
  public int end_index;
  /**
   * System ID of the author of the new mission.
   */
  public int origin_sysid;
  /**
   * Compnent ID of the author of the new mission.
   */
  public int origin_compid;
  /**
   * Mission type.
   */
  public int mission_type;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  start_index = (int)dis.readShort();
  end_index = (int)dis.readShort();
  origin_sysid = (int)dis.readUnsignedByte()&0x00FF;
  origin_compid = (int)dis.readUnsignedByte()&0x00FF;
  mission_type = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+7];
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
  dos.writeShort(start_index&0x00FFFF);
  dos.writeShort(end_index&0x00FFFF);
  dos.writeByte(origin_sysid&0x00FF);
  dos.writeByte(origin_compid&0x00FF);
  dos.writeByte(mission_type&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 7);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[17] = crcl;
  buffer[18] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_MISSION_CHANGED : " +   "  start_index="+start_index
+  "  end_index="+end_index
+  "  origin_sysid="+origin_sysid
+  "  origin_compid="+origin_compid
+  "  mission_type="+mission_type
;}
}
