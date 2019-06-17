/**
 * Generated class : msg_isbd_link_status
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
 * Class msg_isbd_link_status
 * Status of the Iridium SBD link.
 **/
public class msg_isbd_link_status extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_ISBD_LINK_STATUS = 335;
  private static final long serialVersionUID = MAVLINK_MSG_ID_ISBD_LINK_STATUS;
  public msg_isbd_link_status() {
    this(1,1);
}
  public msg_isbd_link_status(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_ISBD_LINK_STATUS;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 24;
}

  /**
   * Timestamp (UNIX Epoch time or time since system boot). The receiving end can infer timestamp format (since 1.1.1970 or since system boot) by checking for the magnitude the number.
   */
  public long timestamp;
  /**
   * Timestamp of the last successful sbd session. The receiving end can infer timestamp format (since 1.1.1970 or since system boot) by checking for the magnitude the number.
   */
  public long last_heartbeat;
  /**
   * Number of failed SBD sessions.
   */
  public int failed_sessions;
  /**
   * Number of successful SBD sessions.
   */
  public int successful_sessions;
  /**
   * Signal quality equal to the number of bars displayed on the ISU signal strength indicator. Range is 0 to 5, where 0 indicates no signal and 5 indicates maximum signal strength.
   */
  public int signal_quality;
  /**
   * 1: Ring call pending, 0: No call pending.
   */
  public int ring_pending;
  /**
   * 1: Transmission session pending, 0: No transmission session pending.
   */
  public int tx_session_pending;
  /**
   * 1: Receiving session pending, 0: No receiving session pending.
   */
  public int rx_session_pending;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  timestamp = (long)dis.readLong();
  last_heartbeat = (long)dis.readLong();
  failed_sessions = (int)dis.readUnsignedShort()&0x00FFFF;
  successful_sessions = (int)dis.readUnsignedShort()&0x00FFFF;
  signal_quality = (int)dis.readUnsignedByte()&0x00FF;
  ring_pending = (int)dis.readUnsignedByte()&0x00FF;
  tx_session_pending = (int)dis.readUnsignedByte()&0x00FF;
  rx_session_pending = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+24];
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
  dos.writeLong(last_heartbeat);
  dos.writeShort(failed_sessions&0x00FFFF);
  dos.writeShort(successful_sessions&0x00FFFF);
  dos.writeByte(signal_quality&0x00FF);
  dos.writeByte(ring_pending&0x00FF);
  dos.writeByte(tx_session_pending&0x00FF);
  dos.writeByte(rx_session_pending&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 24);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[34] = crcl;
  buffer[35] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_ISBD_LINK_STATUS : " +   "  timestamp="+timestamp
+  "  last_heartbeat="+last_heartbeat
+  "  failed_sessions="+failed_sessions
+  "  successful_sessions="+successful_sessions
+  "  signal_quality="+signal_quality
+  "  ring_pending="+ring_pending
+  "  tx_session_pending="+tx_session_pending
+  "  rx_session_pending="+rx_session_pending
;}
}
