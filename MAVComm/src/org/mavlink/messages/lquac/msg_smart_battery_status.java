/**
 * Generated class : msg_smart_battery_status
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
 * Class msg_smart_battery_status
 * Smart Battery information (dynamic). Use for updates from: smart battery to flight stack, flight stack to GCS. Use instead of BATTERY_STATUS for smart batteries.
 **/
public class msg_smart_battery_status extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_SMART_BATTERY_STATUS = 371;
  private static final long serialVersionUID = MAVLINK_MSG_ID_SMART_BATTERY_STATUS;
  public msg_smart_battery_status() {
    this(1,1);
}
  public msg_smart_battery_status(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_SMART_BATTERY_STATUS;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 50;
}

  /**
   * Fault/health indications.
   */
  public long fault_bitmask;
  /**
   * Estimated remaining battery time. -1: field not provided.
   */
  public long time_remaining;
  /**
   * Battery ID
   */
  public int id;
  /**
   * Remaining battery energy. Values: [0-100], -1: field not provided.
   */
  public int capacity_remaining;
  /**
   * Battery current (through all cells/loads). Positive if discharging, negative if charging. UINT16_MAX: field not provided.
   */
  public int current;
  /**
   * Battery temperature. -1: field not provided.
   */
  public int temperature;
  /**
   * The cell number of the first index in the 'voltages' array field. Using this field allows you to specify cell voltages for batteries with more than 16 cells.
   */
  public int cell_offset;
  /**
   * Individual cell voltages. Batteries with more 16 cells can use the cell_offset field to specify the cell offset for the array specified in the current message . Index values above the valid cell count for this battery should have the UINT16_MAX value.
   */
  public int[] voltages = new int[16];
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  fault_bitmask = (int)dis.readInt();
  time_remaining = (int)dis.readInt();
  id = (int)dis.readUnsignedShort()&0x00FFFF;
  capacity_remaining = (int)dis.readShort();
  current = (int)dis.readShort();
  temperature = (int)dis.readShort();
  cell_offset = (int)dis.readUnsignedShort()&0x00FFFF;
  for (int i=0; i<16; i++) {
    voltages[i] = (int)dis.readUnsignedShort()&0x00FFFF;
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+50];
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
  dos.writeInt((int)(fault_bitmask&0x00FFFFFFFF));
  dos.writeInt((int)(time_remaining&0x00FFFFFFFF));
  dos.writeShort(id&0x00FFFF);
  dos.writeShort(capacity_remaining&0x00FFFF);
  dos.writeShort(current&0x00FFFF);
  dos.writeShort(temperature&0x00FFFF);
  dos.writeShort(cell_offset&0x00FFFF);
  for (int i=0; i<16; i++) {
    dos.writeShort(voltages[i]&0x00FFFF);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 50);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[60] = crcl;
  buffer[61] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_SMART_BATTERY_STATUS : " +   "  fault_bitmask="+fault_bitmask
+  "  time_remaining="+time_remaining
+  "  id="+id
+  "  capacity_remaining="+capacity_remaining
+  "  current="+current
+  "  temperature="+temperature
+  "  cell_offset="+cell_offset
+  "  voltages[0]="+voltages[0]
+  "  voltages[1]="+voltages[1]
+  "  voltages[2]="+voltages[2]
+  "  voltages[3]="+voltages[3]
+  "  voltages[4]="+voltages[4]
+  "  voltages[5]="+voltages[5]
+  "  voltages[6]="+voltages[6]
+  "  voltages[7]="+voltages[7]
+  "  voltages[8]="+voltages[8]
+  "  voltages[9]="+voltages[9]
+  "  voltages[10]="+voltages[10]
+  "  voltages[11]="+voltages[11]
+  "  voltages[12]="+voltages[12]
+  "  voltages[13]="+voltages[13]
+  "  voltages[14]="+voltages[14]
+  "  voltages[15]="+voltages[15]
;}
}
