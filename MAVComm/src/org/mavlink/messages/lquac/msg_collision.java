/**
 * Generated class : msg_collision
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
 * Class msg_collision
 * Information about a potential collision
 **/
public class msg_collision extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_COLLISION = 247;
  private static final long serialVersionUID = MAVLINK_MSG_ID_COLLISION;
  public msg_collision() {
    this(1,1);
}
  public msg_collision(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_COLLISION;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 19;
}

  /**
   * Unique identifier, domain based on src field
   */
  public long id;
  /**
   * Estimated time until collision occurs (seconds)
   */
  public float time_to_minimum_delta;
  /**
   * Closest vertical distance in meters between vehicle and object
   */
  public float altitude_minimum_delta;
  /**
   * Closest horizontal distance in meteres between vehicle and object
   */
  public float horizontal_minimum_delta;
  /**
   * Collision data source
   */
  public int src;
  /**
   * Action that is being taken to avoid this collision
   */
  public int action;
  /**
   * How concerned the aircraft is about this collision
   */
  public int threat_level;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  id = (int)dis.readInt()&0x00FFFFFFFF;
  time_to_minimum_delta = (float)dis.readFloat();
  altitude_minimum_delta = (float)dis.readFloat();
  horizontal_minimum_delta = (float)dis.readFloat();
  src = (int)dis.readUnsignedByte()&0x00FF;
  action = (int)dis.readUnsignedByte()&0x00FF;
  threat_level = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+19];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFD);
  dos.writeByte(length & 0x00FF);
  dos.writeByte(sequence & 0x00FF);
  dos.writeByte(sysId & 0x00FF);
  dos.writeByte(componentId & 0x00FF);
  dos.writeByte(messageType & 0x00FF);
  dos.writeInt((int)(id&0x00FFFFFFFF));
  dos.writeFloat(time_to_minimum_delta);
  dos.writeFloat(altitude_minimum_delta);
  dos.writeFloat(horizontal_minimum_delta);
  dos.writeByte(src&0x00FF);
  dos.writeByte(action&0x00FF);
  dos.writeByte(threat_level&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 19);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[25] = crcl;
  buffer[26] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_COLLISION : " +   "  id="+id+  "  time_to_minimum_delta="+time_to_minimum_delta+  "  altitude_minimum_delta="+altitude_minimum_delta+  "  horizontal_minimum_delta="+horizontal_minimum_delta+  "  src="+src+  "  action="+action+  "  threat_level="+threat_level;}
}
