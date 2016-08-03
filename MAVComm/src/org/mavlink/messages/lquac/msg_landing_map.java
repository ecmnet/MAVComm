/**
 * Generated class : msg_landing_map
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
 * Class msg_landing_map
 * Quality data about specific landing positions
 **/
public class msg_landing_map extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_LANDING_MAP = 240;
  private static final long serialVersionUID = MAVLINK_MSG_ID_LANDING_MAP;
  public msg_landing_map() {
    this(1,1);
}
  public msg_landing_map(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_LANDING_MAP;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 201;
}

  /**
   * Timestamp (micros since boot or Unix epoch)
   */
  public long time_usec;
  /**
   * Best landing position on X-axis
   */
  public float best_x;
  /**
   * Best landing position on Y-axis
   */
  public float best_y;
  /**
   * Best landing position on Z-axis
   */
  public float best_z;
  /**
   * Position on X-axis
   */
  public float local_x;
  /**
   * Position on Y-axis
   */
  public float local_y;
  /**
   * Position on Z-axis
   */
  public float local_z;
  /**
   * LSB 0-2: Score between 0 and 7, LSB 3-7: Distance to the vehicle in meters.
   */
  public int[] factors = new int[169];
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  best_x = (float)dis.readFloat();
  best_y = (float)dis.readFloat();
  best_z = (float)dis.readFloat();
  local_x = (float)dis.readFloat();
  local_y = (float)dis.readFloat();
  local_z = (float)dis.readFloat();
  for (int i=0; i<169; i++) {
    factors[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+201];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFE);
  dos.writeByte(length & 0x00FF);
  dos.writeByte(sequence & 0x00FF);
  dos.writeByte(sysId & 0x00FF);
  dos.writeByte(componentId & 0x00FF);
  dos.writeByte(messageType & 0x00FF);
  dos.writeLong(time_usec);
  dos.writeFloat(best_x);
  dos.writeFloat(best_y);
  dos.writeFloat(best_z);
  dos.writeFloat(local_x);
  dos.writeFloat(local_y);
  dos.writeFloat(local_z);
  for (int i=0; i<169; i++) {
    dos.writeByte(factors[i]&0x00FF);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 201);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[207] = crcl;
  buffer[208] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_LANDING_MAP : " +   "  time_usec="+time_usec+  "  best_x="+best_x+  "  best_y="+best_y+  "  best_z="+best_z+  "  local_x="+local_x+  "  local_y="+local_y+  "  local_z="+local_z+  "  factors="+factors;}
}
