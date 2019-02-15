/**
 * Generated class : msg_smart_battery_info
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
 * Class msg_smart_battery_info
 * Smart Battery information (static/infrequent update). Use for updates from: smart battery to flight stack, flight stack to GCS. Use instead of BATTERY_STATUS for smart batteries.
 **/
public class msg_smart_battery_info extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_SMART_BATTERY_INFO = 370;
  private static final long serialVersionUID = MAVLINK_MSG_ID_SMART_BATTERY_INFO;
  public msg_smart_battery_info() {
    this(1,1);
}
  public msg_smart_battery_info(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_SMART_BATTERY_INFO;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 73;
}

  /**
   * Capacity when full according to manufacturer, -1: field not provided.
   */
  public long capacity_full_specification;
  /**
   * Capacity when full (accounting for battery degradation), -1: field not provided.
   */
  public long capacity_full;
  /**
   * Serial number. -1: field not provided.
   */
  public long serial_number;
  /**
   * Charge/discharge cycle count. -1: field not provided.
   */
  public int cycle_count;
  /**
   * Battery weight. 0: field not provided.
   */
  public int weight;
  /**
   * Minimum per-cell voltage when discharging. If not supplied set to UINT16_MAX value.
   */
  public int discharge_minimum_voltage;
  /**
   * Minimum per-cell voltage when charging. If not supplied set to UINT16_MAX value.
   */
  public int charging_minimum_voltage;
  /**
   * Minimum per-cell voltage when resting. If not supplied set to UINT16_MAX value.
   */
  public int resting_minimum_voltage;
  /**
   * Battery ID
   */
  public int id;
  /**
   * Static device name. Encode as manufacturer and product names separated using an underscore.
   */
  public char[] device_name = new char[50];
  public void setDevice_name(String tmp) {
    int len = Math.min(tmp.length(), 50);
    for (int i=0; i<len; i++) {
      device_name[i] = tmp.charAt(i);
    }
    for (int i=len; i<50; i++) {
      device_name[i] = 0;
    }
  }
  public String getDevice_name() {
    String result="";
    for (int i=0; i<50; i++) {
      if (device_name[i] != 0) result=result+device_name[i]; else break;
    }
    return result;
  }
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  capacity_full_specification = (int)dis.readInt();
  capacity_full = (int)dis.readInt();
  serial_number = (int)dis.readInt();
  cycle_count = (int)dis.readUnsignedShort()&0x00FFFF;
  weight = (int)dis.readUnsignedShort()&0x00FFFF;
  discharge_minimum_voltage = (int)dis.readUnsignedShort()&0x00FFFF;
  charging_minimum_voltage = (int)dis.readUnsignedShort()&0x00FFFF;
  resting_minimum_voltage = (int)dis.readUnsignedShort()&0x00FFFF;
  id = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<50; i++) {
    device_name[i] = (char)dis.readByte();
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+73];
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
  dos.writeInt((int)(capacity_full_specification&0x00FFFFFFFF));
  dos.writeInt((int)(capacity_full&0x00FFFFFFFF));
  dos.writeInt((int)(serial_number&0x00FFFFFFFF));
  dos.writeShort(cycle_count&0x00FFFF);
  dos.writeShort(weight&0x00FFFF);
  dos.writeShort(discharge_minimum_voltage&0x00FFFF);
  dos.writeShort(charging_minimum_voltage&0x00FFFF);
  dos.writeShort(resting_minimum_voltage&0x00FFFF);
  dos.writeByte(id&0x00FF);
  for (int i=0; i<50; i++) {
    dos.writeByte(device_name[i]);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 73);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[83] = crcl;
  buffer[84] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_SMART_BATTERY_INFO : " +   "  capacity_full_specification="+capacity_full_specification
+  "  capacity_full="+capacity_full
+  "  serial_number="+serial_number
+  "  cycle_count="+cycle_count
+  "  weight="+weight
+  "  discharge_minimum_voltage="+discharge_minimum_voltage
+  "  charging_minimum_voltage="+charging_minimum_voltage
+  "  resting_minimum_voltage="+resting_minimum_voltage
+  "  id="+id
+  "  device_name="+getDevice_name()
;}
}
