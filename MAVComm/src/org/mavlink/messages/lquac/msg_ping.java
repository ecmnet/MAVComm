/**
 * Generated class : msg_ping
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
 * Class msg_ping
 * A ping message either requesting or responding to a ping. This allows to measure the system latencies, including serial port, radio modem and UDP connections.
 **/
public class msg_ping extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_PING = 4;
  private static final long serialVersionUID = MAVLINK_MSG_ID_PING;
  public msg_ping() {
    this(1,1);
}
  public msg_ping(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_PING;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 14;
}

  /**
   * Timestamp (UNIX Epoch time or time since system boot). The receiving end can infer timestamp format (since 1.1.1970 or since system boot) by checking for the magnitude the number.
   */
  public long time_usec;
  /**
   * PING sequence
   */
  public long seq;
  /**
   * 0: request ping from all receiving systems. If greater than 0: message is a ping response and number is the system id of the requesting system
   */
  public int target_system;
  /**
   * 0: request ping from all receiving components. If greater than 0: message is a ping response and number is the component id of the requesting component.
   */
  public int target_component;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  seq = (int)dis.readInt()&0x00FFFFFFFF;
  target_system = (int)dis.readUnsignedByte()&0x00FF;
  target_component = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+14];
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
  dos.writeInt((int)(seq&0x00FFFFFFFF));
  dos.writeByte(target_system&0x00FF);
  dos.writeByte(target_component&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 14);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[24] = crcl;
  buffer[25] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_PING : " +   "  time_usec="+time_usec
+  "  seq="+seq
+  "  target_system="+target_system
+  "  target_component="+target_component
;}
}
