/**
 * Generated class : msg_serial_control
 * DO NOT MODIFY!
 **/
package org.mavlink.messages.lquac;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.mavlink.IMAVLinkCRC;
import org.mavlink.MAVLinkCRC;
import org.mavlink.messages.MAVLinkMessage;
/**
 * Class msg_serial_control
 * Control a serial port. This can be used for raw access to an onboard serial peripheral such as a GPS or telemetry radio. It is designed to make it possible to update the devices firmware via MAVLink messages or change the devices settings. A message with zero bytes can be used to change just the baudrate.
 **/
public class msg_serial_control extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_SERIAL_CONTROL = 126;
  private static final long serialVersionUID = MAVLINK_MSG_ID_SERIAL_CONTROL;
  public msg_serial_control() {
    this(1,1);
}
  public msg_serial_control(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_SERIAL_CONTROL;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 79;
}

  /**
   * Baudrate of transfer. Zero means no change.
   */
  public long baudrate;
  /**
   * Timeout for reply data in milliseconds
   */
  public int timeout;
  /**
   * See SERIAL_CONTROL_DEV enum
   */
  public int device;
  /**
   * See SERIAL_CONTROL_FLAG enum
   */
  public int flags;
  /**
   * how many bytes in this transfer
   */
  public int count;
  /**
   * serial data
   */
  public int[] data = new int[70];
/**
 * Decode message with raw data
 */
public void decode(ByteBuffer dis) throws IOException {
  baudrate = (int)dis.getInt()&0x00FFFFFFFF;
  timeout = (int)dis.getShort()&0x00FFFF;
  device = (int)dis.get()&0x00FF;
  flags = (int)dis.get()&0x00FF;
  count = (int)dis.get()&0x00FF;
  for (int i=0; i<70; i++) {
    data[i] = (int)dis.get()&0x00FF;
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+79];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.putInt((int)(baudrate&0x00FFFFFFFF));
  dos.putShort((short)(timeout&0x00FFFF));
  dos.put((byte)(device&0x00FF));
  dos.put((byte)(flags&0x00FF));
  dos.put((byte)(count&0x00FF));
  for (int i=0; i<70; i++) {
    dos.put((byte)(data[i]&0x00FF));
  }
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 79);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[85] = crcl;
  buffer[86] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_SERIAL_CONTROL : " +   "  baudrate="+baudrate+  "  timeout="+timeout+  "  device="+device+  "  flags="+flags+  "  count="+count+  "  data="+data;}
}
