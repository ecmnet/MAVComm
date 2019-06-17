/**
 * Generated class : msg_link_node_status
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
 * Class msg_link_node_status
 * Status generated in each node in the communication chain and injected into MAVLink stream.
 **/
public class msg_link_node_status extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_LINK_NODE_STATUS = 8;
  private static final long serialVersionUID = MAVLINK_MSG_ID_LINK_NODE_STATUS;
  public msg_link_node_status() {
    this(1,1);
}
  public msg_link_node_status(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_LINK_NODE_STATUS;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 36;
}

  /**
   * Timestamp (time since system boot).
   */
  public long timestamp;
  /**
   * Transmit rate
   */
  public long tx_rate;
  /**
   * Receive rate
   */
  public long rx_rate;
  /**
   * Messages sent
   */
  public long messages_sent;
  /**
   * Messages received (estimated from counting seq)
   */
  public long messages_received;
  /**
   * Messages lost (estimated from counting seq)
   */
  public long messages_lost;
  /**
   * Number of bytes that could not be parsed correctly.
   */
  public int rx_parse_err;
  /**
   * Transmit buffer overflows. This number wraps around as it reaches UINT16_MAX
   */
  public int tx_overflows;
  /**
   * Receive buffer overflows. This number wraps around as it reaches UINT16_MAX
   */
  public int rx_overflows;
  /**
   * Remaining free transmit buffer space
   */
  public int tx_buf;
  /**
   * Remaining free receive buffer space
   */
  public int rx_buf;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  timestamp = (long)dis.readLong();
  tx_rate = (int)dis.readInt()&0x00FFFFFFFF;
  rx_rate = (int)dis.readInt()&0x00FFFFFFFF;
  messages_sent = (int)dis.readInt()&0x00FFFFFFFF;
  messages_received = (int)dis.readInt()&0x00FFFFFFFF;
  messages_lost = (int)dis.readInt()&0x00FFFFFFFF;
  rx_parse_err = (int)dis.readUnsignedShort()&0x00FFFF;
  tx_overflows = (int)dis.readUnsignedShort()&0x00FFFF;
  rx_overflows = (int)dis.readUnsignedShort()&0x00FFFF;
  tx_buf = (int)dis.readUnsignedByte()&0x00FF;
  rx_buf = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+36];
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
  dos.writeLong(timestamp);
  dos.writeInt((int)(tx_rate&0x00FFFFFFFF));
  dos.writeInt((int)(rx_rate&0x00FFFFFFFF));
  dos.writeInt((int)(messages_sent&0x00FFFFFFFF));
  dos.writeInt((int)(messages_received&0x00FFFFFFFF));
  dos.writeInt((int)(messages_lost&0x00FFFFFFFF));
  dos.writeShort(rx_parse_err&0x00FFFF);
  dos.writeShort(tx_overflows&0x00FFFF);
  dos.writeShort(rx_overflows&0x00FFFF);
  dos.writeByte(tx_buf&0x00FF);
  dos.writeByte(rx_buf&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 36);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[46] = crcl;
  buffer[47] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_LINK_NODE_STATUS : " +   "  timestamp="+timestamp
+  "  tx_rate="+tx_rate
+  "  rx_rate="+rx_rate
+  "  messages_sent="+messages_sent
+  "  messages_received="+messages_received
+  "  messages_lost="+messages_lost
+  "  rx_parse_err="+rx_parse_err
+  "  tx_overflows="+tx_overflows
+  "  rx_overflows="+rx_overflows
+  "  tx_buf="+tx_buf
+  "  rx_buf="+rx_buf
;}
}
