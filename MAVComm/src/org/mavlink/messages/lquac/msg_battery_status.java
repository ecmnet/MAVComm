/**
 * Generated class : msg_battery_status
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
 * Class msg_battery_status
 * Battery information
 **/
public class msg_battery_status extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_BATTERY_STATUS = 147;
  private static final long serialVersionUID = MAVLINK_MSG_ID_BATTERY_STATUS;
  public msg_battery_status() {
    this(1,1);
}
  public msg_battery_status(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_BATTERY_STATUS;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 36;
}

  /**
   * Consumed charge, in milliampere hours (1 = 1 mAh), -1: autopilot does not provide mAh consumption estimate
   */
  public long current_consumed;
  /**
   * Consumed energy, in 100*Joules (intergrated U*I*dt)  (1 = 100 Joule), -1: autopilot does not provide energy consumption estimate
   */
  public long energy_consumed;
  /**
   * Temperature of the battery in centi-degrees celsius. INT16_MAX for unknown temperature.
   */
  public int temperature;
  /**
   * Battery voltage of cells, in millivolts (1 = 1 millivolt)
   */
  public int[] voltages = new int[10];
  /**
   * Battery current, in 10*milliamperes (1 = 10 milliampere), -1: autopilot does not measure the current
   */
  public int current_battery;
  /**
   * Battery ID
   */
  public int id;
  /**
   * Function of the battery
   */
  public int battery_function;
  /**
   * Type (chemistry) of the battery
   */
  public int type;
  /**
   * Remaining battery energy: (0%: 0, 100%: 100), -1: autopilot does not estimate the remaining battery
   */
  public int battery_remaining;
/**
 * Decode message with raw data
 */
public void decode(ByteBuffer dis) throws IOException {
  current_consumed = (int)dis.getInt();
  energy_consumed = (int)dis.getInt();
  temperature = (int)dis.getShort();
  for (int i=0; i<10; i++) {
    voltages[i] = (int)dis.getShort()&0x00FFFF;
  }
  current_battery = (int)dis.getShort();
  id = (int)dis.get()&0x00FF;
  battery_function = (int)dis.get()&0x00FF;
  type = (int)dis.get()&0x00FF;
  battery_remaining = (int)dis.get();
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+36];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.putInt((int)(current_consumed&0x00FFFFFFFF));
  dos.putInt((int)(energy_consumed&0x00FFFFFFFF));
  dos.putShort((short)(temperature&0x00FFFF));
  for (int i=0; i<10; i++) {
    dos.putShort((short)(voltages[i]&0x00FFFF));
  }
  dos.putShort((short)(current_battery&0x00FFFF));
  dos.put((byte)(id&0x00FF));
  dos.put((byte)(battery_function&0x00FF));
  dos.put((byte)(type&0x00FF));
  dos.put((byte)(battery_remaining&0x00FF));
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 36);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[42] = crcl;
  buffer[43] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_BATTERY_STATUS : " +   "  current_consumed="+current_consumed+  "  energy_consumed="+energy_consumed+  "  temperature="+temperature+  "  voltages="+voltages+  "  current_battery="+current_battery+  "  id="+id+  "  battery_function="+battery_function+  "  type="+type+  "  battery_remaining="+battery_remaining;}
}
