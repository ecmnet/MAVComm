/**
 * Generated class : msg_time_estimate_to_target
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
 * Class msg_time_estimate_to_target
 * Time/duration estimates for various events and actions given the current vehicle state and position.
 **/
public class msg_time_estimate_to_target extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_TIME_ESTIMATE_TO_TARGET = 380;
  private static final long serialVersionUID = MAVLINK_MSG_ID_TIME_ESTIMATE_TO_TARGET;
  public msg_time_estimate_to_target() {
    this(1,1);
}
  public msg_time_estimate_to_target(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_TIME_ESTIMATE_TO_TARGET;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 20;
}

  /**
   * Estimated time to complete the vehicle's configured "safe return" action from its current position (e.g. RTL, Smart RTL, etc.). -1 indicates that the vehicle is landed, or that no time estimate available.
   */
  public long safe_return;
  /**
   * Estimated time for vehicle to complete the LAND action from its current position. -1 indicates that the vehicle is landed, or that no time estimate available.
   */
  public long land;
  /**
   * Estimated time for reaching/completing the currently active mission item. -1 means no time estimate available.
   */
  public long mission_next_item;
  /**
   * Estimated time for completing the current mission. -1 means no mission active and/or no estimate available.
   */
  public long mission_end;
  /**
   * Estimated time for completing the current commanded action (i.e. Go To, Takeoff, Land, etc.). -1 means no action active and/or no estimate available.
   */
  public long commanded_action;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  safe_return = (int)dis.readInt();
  land = (int)dis.readInt();
  mission_next_item = (int)dis.readInt();
  mission_end = (int)dis.readInt();
  commanded_action = (int)dis.readInt();
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+20];
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
  dos.writeInt((int)(safe_return&0x00FFFFFFFF));
  dos.writeInt((int)(land&0x00FFFFFFFF));
  dos.writeInt((int)(mission_next_item&0x00FFFFFFFF));
  dos.writeInt((int)(mission_end&0x00FFFFFFFF));
  dos.writeInt((int)(commanded_action&0x00FFFFFFFF));
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 20);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[30] = crcl;
  buffer[31] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_TIME_ESTIMATE_TO_TARGET : " +   "  safe_return="+safe_return
+  "  land="+land
+  "  mission_next_item="+mission_next_item
+  "  mission_end="+mission_end
+  "  commanded_action="+commanded_action
;}
}
