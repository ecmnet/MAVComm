/**
 * Generated class : msg_extended_sys_state
 * DO NOT MODIFY!
 **/
package org.mavlink.messages.lquac;
import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.IMAVLinkCRC;
import org.mavlink.MAVLinkCRC;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/**
 * Class msg_extended_sys_state
 * Provides state for additional features
 **/
public class msg_extended_sys_state extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_EXTENDED_SYS_STATE = 245;
  private static final long serialVersionUID = MAVLINK_MSG_ID_EXTENDED_SYS_STATE;
  public msg_extended_sys_state() {
    this(1,1);
}
  public msg_extended_sys_state(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_EXTENDED_SYS_STATE;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 2;
}

  /**
   * The VTOL state if applicable. Is set to MAV_VTOL_STATE_UNDEFINED if UAV is not in VTOL configuration.
   */
  public int vtol_state;
  /**
   * The landed state. Is set to MAV_LANDED_STATE_UNDEFINED if landed state is unknown.
   */
  public int landed_state;
/**
 * Decode message with raw data
 */
public void decode(ByteBuffer dis) throws IOException {
  vtol_state = (int)dis.get()&0x00FF;
  landed_state = (int)dis.get()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+2];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.put((byte)(vtol_state&0x00FF));
  dos.put((byte)(landed_state&0x00FF));
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 2);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[8] = crcl;
  buffer[9] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_EXTENDED_SYS_STATE : " +   "  vtol_state="+vtol_state+  "  landed_state="+landed_state;}
}
