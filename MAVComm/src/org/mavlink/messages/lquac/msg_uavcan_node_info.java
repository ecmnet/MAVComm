/**
 * Generated class : msg_uavcan_node_info
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
 * Class msg_uavcan_node_info
 * General information describing a particular UAVCAN node. Please refer to the definition of the UAVCAN service "uavcan.protocol.GetNodeInfo" for the background information. This message should be emitted by the system whenever a new node appears online, or an existing node reboots. Additionally, it can be emitted upon request from the other end of the MAVLink channel (see MAV_CMD_UAVCAN_GET_NODE_INFO). It is also not prohibited to emit this message unconditionally at a low frequency. The UAVCAN specification is available at http://uavcan.org.
 **/
public class msg_uavcan_node_info extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_UAVCAN_NODE_INFO = 311;
  private static final long serialVersionUID = MAVLINK_MSG_ID_UAVCAN_NODE_INFO;
  public msg_uavcan_node_info() {
    this(1,1);
}
  public msg_uavcan_node_info(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_UAVCAN_NODE_INFO;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 116;
}

  /**
   * Timestamp (microseconds since UNIX epoch or microseconds since system boot)
   */
  public long time_usec;
  /**
   * The number of seconds since the start-up of the node.
   */
  public long uptime_sec;
  /**
   * Version control system (VCS) revision identifier (e.g. git short commit hash). Zero if unknown.
   */
  public long sw_vcs_commit;
  /**
   * Node name string. For example, "sapog.px4.io".
   */
  public char[] name = new char[80];
  public void setName(String tmp) {
    int len = Math.min(tmp.length(), 80);
    for (int i=0; i<len; i++) {
      name[i] = tmp.charAt(i);
    }
    for (int i=len; i<80; i++) {
      name[i] = 0;
    }
  }
  public String getName() {
    String result="";
    for (int i=0; i<80; i++) {
      if (name[i] != 0) result=result+name[i]; else break;
    }
    return result;
  }
  /**
   * Hardware major version number.
   */
  public int hw_version_major;
  /**
   * Hardware minor version number.
   */
  public int hw_version_minor;
  /**
   * Hardware unique 128-bit ID.
   */
  public int[] hw_unique_id = new int[16];
  /**
   * Software major version number.
   */
  public int sw_version_major;
  /**
   * Software minor version number.
   */
  public int sw_version_minor;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  uptime_sec = (int)dis.readInt()&0x00FFFFFFFF;
  sw_vcs_commit = (int)dis.readInt()&0x00FFFFFFFF;
  for (int i=0; i<80; i++) {
    name[i] = (char)dis.readByte();
  }
  hw_version_major = (int)dis.readUnsignedByte()&0x00FF;
  hw_version_minor = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<16; i++) {
    hw_unique_id[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
  sw_version_major = (int)dis.readUnsignedByte()&0x00FF;
  sw_version_minor = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+116];
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
  dos.writeLong(time_usec);
  dos.writeInt((int)(uptime_sec&0x00FFFFFFFF));
  dos.writeInt((int)(sw_vcs_commit&0x00FFFFFFFF));
  for (int i=0; i<80; i++) {
    dos.writeByte(name[i]);
  }
  dos.writeByte(hw_version_major&0x00FF);
  dos.writeByte(hw_version_minor&0x00FF);
  for (int i=0; i<16; i++) {
    dos.writeByte(hw_unique_id[i]&0x00FF);
  }
  dos.writeByte(sw_version_major&0x00FF);
  dos.writeByte(sw_version_minor&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 116);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[126] = crcl;
  buffer[127] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_UAVCAN_NODE_INFO : " +   "  time_usec="+time_usec+  "  uptime_sec="+uptime_sec+  "  sw_vcs_commit="+sw_vcs_commit+  "  name="+getName()+  "  hw_version_major="+hw_version_major+  "  hw_version_minor="+hw_version_minor+  "  hw_unique_id="+hw_unique_id+  "  sw_version_major="+sw_version_major+  "  sw_version_minor="+sw_version_minor;}
}
