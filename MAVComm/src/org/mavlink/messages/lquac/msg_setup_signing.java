/**
 * Generated class : msg_setup_signing
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
 * Class msg_setup_signing
 * Setup a MAVLink2 signing key. If called with secret_key of all zero and zero initial_timestamp will disable signing
 **/
public class msg_setup_signing extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_SETUP_SIGNING = 256;
  private static final long serialVersionUID = MAVLINK_MSG_ID_SETUP_SIGNING;
  public msg_setup_signing() {
    this(1,1);
}
  public msg_setup_signing(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_SETUP_SIGNING;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 42;
}

  /**
   * initial timestamp
   */
  public long initial_timestamp;
  /**
   * system id of the target
   */
  public int target_system;
  /**
   * component ID of the target
   */
  public int target_component;
  /**
   * signing key
   */
  public int[] secret_key = new int[32];
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  initial_timestamp = (long)dis.readLong();
  target_system = (int)dis.readUnsignedByte()&0x00FF;
  target_component = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<32; i++) {
    secret_key[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+42];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFD);
  dos.writeByte(length & 0x00FF);
  dos.writeByte(incompat & 0x00FF);
  dos.writeByte(compat & 0x00FF);
  dos.writeByte(sequence & 0x00FF);
  dos.writeByte(sysId & 0x00FF);
  dos.writeByte(componentId & 0x00FF);
  dos.writeByte(messageType & 0x00FF);
  dos.writeByte((messageType >> 8) & 0x00FF);
  dos.writeByte((messageType >> 16) & 0x00FF);
  dos.writeLong(initial_timestamp);
  dos.writeByte(target_system&0x00FF);
  dos.writeByte(target_component&0x00FF);
  for (int i=0; i<32; i++) {
    dos.writeByte(secret_key[i]&0x00FF);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 42);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[52] = crcl;
  buffer[53] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_SETUP_SIGNING : " +   "  initial_timestamp="+initial_timestamp+  "  target_system="+target_system+  "  target_component="+target_component+  "  secret_key="+secret_key;}
}
