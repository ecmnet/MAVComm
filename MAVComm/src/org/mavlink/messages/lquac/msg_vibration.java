/**
 * Generated class : msg_vibration
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
    length = 32;
}

  /**
   * Timestamp (micros since boot or Unix epoch)
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
public void decode(ByteBuffer dis) throws IOException {
  time_usec = (long)dis.getLong();
  vibration_x = (float)dis.getFloat();
  vibration_y = (float)dis.getFloat();
  vibration_z = (float)dis.getFloat();
  clipping_0 = (int)dis.getInt()&0x00FFFFFFFF;
  clipping_1 = (int)dis.getInt()&0x00FFFFFFFF;
  clipping_2 = (int)dis.getInt()&0x00FFFFFFFF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+32];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.putLong(time_usec);
  dos.putFloat(vibration_x);
  dos.putFloat(vibration_y);
  dos.putFloat(vibration_z);
  dos.putInt((int)(clipping_0&0x00FFFFFFFF));
  dos.putInt((int)(clipping_1&0x00FFFFFFFF));
  dos.putInt((int)(clipping_2&0x00FFFFFFFF));
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 32);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[38] = crcl;
  buffer[39] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_VIBRATION : " +   "  time_usec="+time_usec+  "  vibration_x="+vibration_x+  "  vibration_y="+vibration_y+  "  vibration_z="+vibration_z+  "  clipping_0="+clipping_0+  "  clipping_1="+clipping_1+  "  clipping_2="+clipping_2;}
}
