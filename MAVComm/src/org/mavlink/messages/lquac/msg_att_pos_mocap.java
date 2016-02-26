/**
 * Generated class : msg_att_pos_mocap
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
 * Class msg_att_pos_mocap
 * Motion capture attitude and position
 **/
public class msg_att_pos_mocap extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_ATT_POS_MOCAP = 138;
  private static final long serialVersionUID = MAVLINK_MSG_ID_ATT_POS_MOCAP;
  public msg_att_pos_mocap() {
    this(1,1);
}
  public msg_att_pos_mocap(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_ATT_POS_MOCAP;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 36;
}

  /**
   * Timestamp (micros since boot or Unix epoch)
   */
  public long time_usec;
  /**
   * Attitude quaternion (w, x, y, z order, zero-rotation is 1, 0, 0, 0)
   */
  public float[] q = new float[4];
  /**
   * X position in meters (NED)
   */
  public float x;
  /**
   * Y position in meters (NED)
   */
  public float y;
  /**
   * Z position in meters (NED)
   */
  public float z;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  for (int i=0; i<4; i++) {
    q[i] = (float)dis.readFloat();
  }
  x = (float)dis.readFloat();
  y = (float)dis.readFloat();
  z = (float)dis.readFloat();
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+36];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFE);
  dos.writeByte(length & 0x00FF);
  dos.writeByte(sequence & 0x00FF);
  dos.writeByte(sysId & 0x00FF);
  dos.writeByte(componentId & 0x00FF);
  dos.writeByte(messageType & 0x00FF);
  dos.writeLong(time_usec);
  for (int i=0; i<4; i++) {
    dos.writeFloat(q[i]);
  }
  dos.writeFloat(x);
  dos.writeFloat(y);
  dos.writeFloat(z);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 36);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[42] = crcl;
  buffer[43] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_ATT_POS_MOCAP : " +   "  time_usec="+time_usec+  "  q="+q+  "  x="+x+  "  y="+y+  "  z="+z;}
}
