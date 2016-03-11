/**
 * Generated class : msg_battery_status
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
   * Battery voltage of cells, in millivolts (1 = 1 millivolt). Cells above the valid cell count for this battery should have the UINT16_MAX value.
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
public void decode(LittleEndianDataInputStream dis) throws IOException {
  current_consumed = (int)dis.readInt();
  energy_consumed = (int)dis.readInt();
  temperature = (int)dis.readShort();
  for (int i=0; i<10; i++) {
    voltages[i] = (int)dis.readUnsignedShort()&0x00FFFF;
  }
  current_battery = (int)dis.readShort();
  id = (int)dis.readUnsignedByte()&0x00FF;
  battery_function = (int)dis.readUnsignedByte()&0x00FF;
  type = (int)dis.readUnsignedByte()&0x00FF;
  battery_remaining = (int)dis.readByte();
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
  dos.writeInt((int)(current_consumed&0x00FFFFFFFF));
  dos.writeInt((int)(energy_consumed&0x00FFFFFFFF));
  dos.writeShort(temperature&0x00FFFF);
  for (int i=0; i<10; i++) {
    dos.writeShort(voltages[i]&0x00FFFF);
  }
  dos.writeShort(current_battery&0x00FFFF);
  dos.writeByte(id&0x00FF);
  dos.writeByte(battery_function&0x00FF);
  dos.writeByte(type&0x00FF);
  dos.write(battery_remaining&0x00FF);
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
return "MAVLINK_MSG_ID_BATTERY_STATUS : " +   "  current_consumed="+current_consumed+  "  energy_consumed="+energy_consumed+  "  temperature="+temperature+  "  voltages="+voltages+  "  current_battery="+current_battery+  "  id="+id+  "  battery_function="+battery_function+  "  type="+type+  "  battery_remaining="+battery_remaining;}
}
