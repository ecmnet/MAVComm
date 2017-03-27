/**
 * Generated class : msg_mission_request_partial_list
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
 * Class msg_mission_request_partial_list
 * Request a partial list of mission items from the system/component. http://qgroundcontrol.org/mavlink/waypoint_protocol. If start and end index are the same, just send one waypoint.
 **/
public class msg_mission_request_partial_list extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_MISSION_REQUEST_PARTIAL_LIST = 37;
  private static final long serialVersionUID = MAVLINK_MSG_ID_MISSION_REQUEST_PARTIAL_LIST;
  public msg_mission_request_partial_list() {
    this(1,1);
}
  public msg_mission_request_partial_list(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_MISSION_REQUEST_PARTIAL_LIST;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 7;
}

  /**
   * Start index, 0 by default
   */
  public int start_index;
  /**
   * End index, -1 by default (-1: send list to end). Else a valid index of the list
   */
  public int end_index;
  /**
   * System ID
   */
  public int target_system;
  /**
   * Component ID
   */
  public int target_component;
  /**
   * Mission type, see MAV_MISSION_TYPE
   */
  public int mission_type;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  start_index = (int)dis.readShort();
  end_index = (int)dis.readShort();
  target_system = (int)dis.readUnsignedByte()&0x00FF;
  target_component = (int)dis.readUnsignedByte()&0x00FF;
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
  dos.writeByte(target_system&0x00FF);
  dos.writeByte(target_component&0x00FF);
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
return "MAVLINK_MSG_ID_MISSION_REQUEST_PARTIAL_LIST : " +   "  start_index="+start_index+  "  end_index="+end_index+  "  target_system="+target_system+  "  target_component="+target_component+  "  mission_type="+mission_type;}
}
