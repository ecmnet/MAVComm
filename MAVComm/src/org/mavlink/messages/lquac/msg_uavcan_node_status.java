/**
 * Generated class : msg_uavcan_node_status
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
 * Class msg_uavcan_node_status
 * General status information of an UAVCAN node. Please refer to the definition of the UAVCAN message "uavcan.protocol.NodeStatus" for the background information. The UAVCAN specification is available at http://uavcan.org.
 **/
public class msg_uavcan_node_status extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_UAVCAN_NODE_STATUS = 310;
  private static final long serialVersionUID = MAVLINK_MSG_ID_UAVCAN_NODE_STATUS;
  public msg_uavcan_node_status() {
    this(1,1);
}
  public msg_uavcan_node_status(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_UAVCAN_NODE_STATUS;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 17;
}

  /**
   * Timestamp (UNIX Epoch time or time since system boot). The receiving end can infer timestamp format (since 1.1.1970 or since system boot) by checking for the magnitude the number.
   */
  public long time_usec;
  /**
   * Time since the start-up of the node.
   */
  public long uptime_sec;
  /**
   * Vendor-specific status information.
   */
  public int vendor_specific_status_code;
  /**
   * Generalized node health status.
   */
  public int health;
  /**
   * Generalized operating mode.
   */
  public int mode;
  /**
   * Not used currently.
   */
  public int sub_mode;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  uptime_sec = (int)dis.readInt()&0x00FFFFFFFF;
  vendor_specific_status_code = (int)dis.readUnsignedShort()&0x00FFFF;
  health = (int)dis.readUnsignedByte()&0x00FF;
  mode = (int)dis.readUnsignedByte()&0x00FF;
  sub_mode = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+17];
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
  dos.writeShort(vendor_specific_status_code&0x00FFFF);
  dos.writeByte(health&0x00FF);
  dos.writeByte(mode&0x00FF);
  dos.writeByte(sub_mode&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 17);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[27] = crcl;
  buffer[28] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_UAVCAN_NODE_STATUS : " +   "  time_usec="+time_usec
+  "  uptime_sec="+uptime_sec
+  "  vendor_specific_status_code="+vendor_specific_status_code
+  "  health="+health
+  "  mode="+mode
+  "  sub_mode="+sub_mode
;}
}
