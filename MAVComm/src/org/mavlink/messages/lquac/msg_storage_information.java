/**
 * Generated class : msg_storage_information
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
 * Class msg_storage_information
 * WIP: Information about a storage medium.
 **/
public class msg_storage_information extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_STORAGE_INFORMATION = 261;
  private static final long serialVersionUID = MAVLINK_MSG_ID_STORAGE_INFORMATION;
  public msg_storage_information() {
    this(1,1);
}
  public msg_storage_information(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_STORAGE_INFORMATION;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 27;
}

  /**
   * Timestamp (milliseconds since system boot)
   */
  public long time_boot_ms;
  /**
   * Total capacity in MiB
   */
  public float total_capacity;
  /**
   * Used capacity in MiB
   */
  public float used_capacity;
  /**
   * Available capacity in MiB
   */
  public float available_capacity;
  /**
   * Read speed in MiB/s
   */
  public float read_speed;
  /**
   * Write speed in MiB/s
   */
  public float write_speed;
  /**
   * Storage ID (1 for first, 2 for second, etc.)
   */
  public int storage_id;
  /**
   * Number of storage devices
   */
  public int storage_count;
  /**
   * Status of storage (0 not available, 1 unformatted, 2 formatted)
   */
  public int status;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_boot_ms = (int)dis.readInt()&0x00FFFFFFFF;
  total_capacity = (float)dis.readFloat();
  used_capacity = (float)dis.readFloat();
  available_capacity = (float)dis.readFloat();
  read_speed = (float)dis.readFloat();
  write_speed = (float)dis.readFloat();
  storage_id = (int)dis.readUnsignedByte()&0x00FF;
  storage_count = (int)dis.readUnsignedByte()&0x00FF;
  status = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+27];
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
  dos.writeInt((int)(time_boot_ms&0x00FFFFFFFF));
  dos.writeFloat(total_capacity);
  dos.writeFloat(used_capacity);
  dos.writeFloat(available_capacity);
  dos.writeFloat(read_speed);
  dos.writeFloat(write_speed);
  dos.writeByte(storage_id&0x00FF);
  dos.writeByte(storage_count&0x00FF);
  dos.writeByte(status&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 27);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[37] = crcl;
  buffer[38] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_STORAGE_INFORMATION : " +   "  time_boot_ms="+time_boot_ms+  "  total_capacity="+total_capacity+  "  used_capacity="+used_capacity+  "  available_capacity="+available_capacity+  "  read_speed="+read_speed+  "  write_speed="+write_speed+  "  storage_id="+storage_id+  "  storage_count="+storage_count+  "  status="+status;}
}
