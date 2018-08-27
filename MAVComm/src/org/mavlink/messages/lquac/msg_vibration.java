/**
 * Generated class : msg_vibration
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
 * Class msg_vibration
 * Vibration levels and accelerometer clipping
 **/
public class msg_vibration extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_VIBRATION = 241;
  private static final long serialVersionUID = MAVLINK_MSG_ID_VIBRATION;
  public msg_vibration() {
    this(1,1);
}
  public msg_vibration(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_VIBRATION;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 32;
}

  /**
   * Timestamp (UNIX Epoch time or time since system boot). The receiving end can infer timestamp format (since 1.1.1970 or since system boot) by checking for the magnitude the number.
   */
  public long time_usec;
  /**
   * Vibration levels on X-axis
   */
  public float vibration_x;
  /**
   * Vibration levels on Y-axis
   */
  public float vibration_y;
  /**
   * Vibration levels on Z-axis
   */
  public float vibration_z;
  /**
   * first accelerometer clipping count
   */
  public long clipping_0;
  /**
   * second accelerometer clipping count
   */
  public long clipping_1;
  /**
   * third accelerometer clipping count
   */
  public long clipping_2;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  vibration_x = (float)dis.readFloat();
  vibration_y = (float)dis.readFloat();
  vibration_z = (float)dis.readFloat();
  clipping_0 = (int)dis.readInt()&0x00FFFFFFFF;
  clipping_1 = (int)dis.readInt()&0x00FFFFFFFF;
  clipping_2 = (int)dis.readInt()&0x00FFFFFFFF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+32];
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
  dos.writeFloat(vibration_x);
  dos.writeFloat(vibration_y);
  dos.writeFloat(vibration_z);
  dos.writeInt((int)(clipping_0&0x00FFFFFFFF));
  dos.writeInt((int)(clipping_1&0x00FFFFFFFF));
  dos.writeInt((int)(clipping_2&0x00FFFFFFFF));
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 32);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[42] = crcl;
  buffer[43] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_VIBRATION : " +   "  time_usec="+time_usec+  "  vibration_x="+vibration_x+  "  vibration_y="+vibration_y+  "  vibration_z="+vibration_z+  "  clipping_0="+clipping_0+  "  clipping_1="+clipping_1+  "  clipping_2="+clipping_2;}
}
