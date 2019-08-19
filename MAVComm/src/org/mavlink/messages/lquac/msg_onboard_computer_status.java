/**
 * Generated class : msg_onboard_computer_status
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
 * Class msg_onboard_computer_status
 * Hardware status sent by an onboard computer.
 **/
public class msg_onboard_computer_status extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_ONBOARD_COMPUTER_STATUS = 390;
  private static final long serialVersionUID = MAVLINK_MSG_ID_ONBOARD_COMPUTER_STATUS;
  public msg_onboard_computer_status() {
    this(1,1);
}
  public msg_onboard_computer_status(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_ONBOARD_COMPUTER_STATUS;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 238;
}

  /**
   * Timestamp (UNIX Epoch time or time since system boot). The receiving end can infer timestamp format (since 1.1.1970 or since system boot) by checking for the magnitude the number.
   */
  public long time_usec;
  /**
   * Time since system boot.
   */
  public long uptime;
  /**
   * Amount of used RAM on the component system. A value of UINT32_MAX implies the field is unused.
   */
  public long ram_usage;
  /**
   * Total amount of RAM on the component system. A value of UINT32_MAX implies the field is unused.
   */
  public long ram_total;
  /**
   * Storage type: 0: HDD, 1: SSD, 2: EMMC, 3: SD card (non-removable), 4: SD card (removable). A value of UINT32_MAX implies the field is unused.
   */
  public long[] storage_type = new long[4];
  /**
   * Amount of used storage space on the component system. A value of UINT32_MAX implies the field is unused.
   */
  public long[] storage_usage = new long[4];
  /**
   * Total amount of storage space on the component system. A value of UINT32_MAX implies the field is unused.
   */
  public long[] storage_total = new long[4];
  /**
   * Link type: 0-9: UART, 10-19: Wired network, 20-29: Wifi, 30-39: Point-to-point proprietary, 40-49: Mesh proprietary
   */
  public long[] link_type = new long[6];
  /**
   * Network traffic from the component system. A value of UINT32_MAX implies the field is unused.
   */
  public long[] link_tx_rate = new long[6];
  /**
   * Network traffic to the component system. A value of UINT32_MAX implies the field is unused.
   */
  public long[] link_rx_rate = new long[6];
  /**
   * Network capacity from the component system. A value of UINT32_MAX implies the field is unused.
   */
  public long[] link_tx_max = new long[6];
  /**
   * Network capacity to the component system. A value of UINT32_MAX implies the field is unused.
   */
  public long[] link_rx_max = new long[6];
  /**
   * Fan speeds. A value of INT16_MAX implies the field is unused.
   */
  public int[] fan_speed = new int[4];
  /**
   * Type of the onboard computer: 0: Mission computer primary, 1: Mission computer backup 1, 2: Mission computer backup 2, 3: Compute node, 4-5: Compute spares, 6-9: Payload computers.
   */
  public int type;
  /**
   * CPU usage on the component in percent (100 - idle). A value of UINT8_MAX implies the field is unused.
   */
  public int[] cpu_cores = new int[8];
  /**
   * Combined CPU usage as the last 10 slices of 100 MS (a histogram). This allows to identify spikes in load that max out the system, but only for a short amount of time. A value of UINT8_MAX implies the field is unused.
   */
  public int[] cpu_combined = new int[10];
  /**
   * GPU usage on the component in percent (100 - idle). A value of UINT8_MAX implies the field is unused.
   */
  public int[] gpu_cores = new int[4];
  /**
   * Combined GPU usage as the last 10 slices of 100 MS (a histogram). This allows to identify spikes in load that max out the system, but only for a short amount of time. A value of UINT8_MAX implies the field is unused.
   */
  public int[] gpu_combined = new int[10];
  /**
   * Temperature of the board. A value of INT8_MAX implies the field is unused.
   */
  public int temperature_board;
  /**
   * Temperature of the CPU core. A value of INT8_MAX implies the field is unused.
   */
  public int[] temperature_core = new int[8];
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  uptime = (int)dis.readInt()&0x00FFFFFFFF;
  ram_usage = (int)dis.readInt()&0x00FFFFFFFF;
  ram_total = (int)dis.readInt()&0x00FFFFFFFF;
  for (int i=0; i<4; i++) {
    storage_type[i] = (int)dis.readInt()&0x00FFFFFFFF;
  }
  for (int i=0; i<4; i++) {
    storage_usage[i] = (int)dis.readInt()&0x00FFFFFFFF;
  }
  for (int i=0; i<4; i++) {
    storage_total[i] = (int)dis.readInt()&0x00FFFFFFFF;
  }
  for (int i=0; i<6; i++) {
    link_type[i] = (int)dis.readInt()&0x00FFFFFFFF;
  }
  for (int i=0; i<6; i++) {
    link_tx_rate[i] = (int)dis.readInt()&0x00FFFFFFFF;
  }
  for (int i=0; i<6; i++) {
    link_rx_rate[i] = (int)dis.readInt()&0x00FFFFFFFF;
  }
  for (int i=0; i<6; i++) {
    link_tx_max[i] = (int)dis.readInt()&0x00FFFFFFFF;
  }
  for (int i=0; i<6; i++) {
    link_rx_max[i] = (int)dis.readInt()&0x00FFFFFFFF;
  }
  for (int i=0; i<4; i++) {
    fan_speed[i] = (int)dis.readShort();
  }
  type = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<8; i++) {
    cpu_cores[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
  for (int i=0; i<10; i++) {
    cpu_combined[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
  for (int i=0; i<4; i++) {
    gpu_cores[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
  for (int i=0; i<10; i++) {
    gpu_combined[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
  temperature_board = (int)dis.readByte();
  for (int i=0; i<8; i++) {
    temperature_core[i] = (int)dis.readByte();
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+238];
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
  dos.writeInt((int)(uptime&0x00FFFFFFFF));
  dos.writeInt((int)(ram_usage&0x00FFFFFFFF));
  dos.writeInt((int)(ram_total&0x00FFFFFFFF));
  for (int i=0; i<4; i++) {
    dos.writeInt((int)(storage_type[i]&0x00FFFFFFFF));
  }
  for (int i=0; i<4; i++) {
    dos.writeInt((int)(storage_usage[i]&0x00FFFFFFFF));
  }
  for (int i=0; i<4; i++) {
    dos.writeInt((int)(storage_total[i]&0x00FFFFFFFF));
  }
  for (int i=0; i<6; i++) {
    dos.writeInt((int)(link_type[i]&0x00FFFFFFFF));
  }
  for (int i=0; i<6; i++) {
    dos.writeInt((int)(link_tx_rate[i]&0x00FFFFFFFF));
  }
  for (int i=0; i<6; i++) {
    dos.writeInt((int)(link_rx_rate[i]&0x00FFFFFFFF));
  }
  for (int i=0; i<6; i++) {
    dos.writeInt((int)(link_tx_max[i]&0x00FFFFFFFF));
  }
  for (int i=0; i<6; i++) {
    dos.writeInt((int)(link_rx_max[i]&0x00FFFFFFFF));
  }
  for (int i=0; i<4; i++) {
    dos.writeShort(fan_speed[i]&0x00FFFF);
  }
  dos.writeByte(type&0x00FF);
  for (int i=0; i<8; i++) {
    dos.writeByte(cpu_cores[i]&0x00FF);
  }
  for (int i=0; i<10; i++) {
    dos.writeByte(cpu_combined[i]&0x00FF);
  }
  for (int i=0; i<4; i++) {
    dos.writeByte(gpu_cores[i]&0x00FF);
  }
  for (int i=0; i<10; i++) {
    dos.writeByte(gpu_combined[i]&0x00FF);
  }
  dos.write(temperature_board&0x00FF);
  for (int i=0; i<8; i++) {
    dos.write(temperature_core[i]&0x00FF);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 238);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[248] = crcl;
  buffer[249] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_ONBOARD_COMPUTER_STATUS : " +   "  time_usec="+time_usec
+  "  uptime="+uptime
+  "  ram_usage="+ram_usage
+  "  ram_total="+ram_total
+  "  storage_type[0]="+storage_type[0]
+  "  storage_type[1]="+storage_type[1]
+  "  storage_type[2]="+storage_type[2]
+  "  storage_type[3]="+storage_type[3]
+  "  storage_usage[0]="+storage_usage[0]
+  "  storage_usage[1]="+storage_usage[1]
+  "  storage_usage[2]="+storage_usage[2]
+  "  storage_usage[3]="+storage_usage[3]
+  "  storage_total[0]="+storage_total[0]
+  "  storage_total[1]="+storage_total[1]
+  "  storage_total[2]="+storage_total[2]
+  "  storage_total[3]="+storage_total[3]
+  "  link_type[0]="+link_type[0]
+  "  link_type[1]="+link_type[1]
+  "  link_type[2]="+link_type[2]
+  "  link_type[3]="+link_type[3]
+  "  link_type[4]="+link_type[4]
+  "  link_type[5]="+link_type[5]
+  "  link_tx_rate[0]="+link_tx_rate[0]
+  "  link_tx_rate[1]="+link_tx_rate[1]
+  "  link_tx_rate[2]="+link_tx_rate[2]
+  "  link_tx_rate[3]="+link_tx_rate[3]
+  "  link_tx_rate[4]="+link_tx_rate[4]
+  "  link_tx_rate[5]="+link_tx_rate[5]
+  "  link_rx_rate[0]="+link_rx_rate[0]
+  "  link_rx_rate[1]="+link_rx_rate[1]
+  "  link_rx_rate[2]="+link_rx_rate[2]
+  "  link_rx_rate[3]="+link_rx_rate[3]
+  "  link_rx_rate[4]="+link_rx_rate[4]
+  "  link_rx_rate[5]="+link_rx_rate[5]
+  "  link_tx_max[0]="+link_tx_max[0]
+  "  link_tx_max[1]="+link_tx_max[1]
+  "  link_tx_max[2]="+link_tx_max[2]
+  "  link_tx_max[3]="+link_tx_max[3]
+  "  link_tx_max[4]="+link_tx_max[4]
+  "  link_tx_max[5]="+link_tx_max[5]
+  "  link_rx_max[0]="+link_rx_max[0]
+  "  link_rx_max[1]="+link_rx_max[1]
+  "  link_rx_max[2]="+link_rx_max[2]
+  "  link_rx_max[3]="+link_rx_max[3]
+  "  link_rx_max[4]="+link_rx_max[4]
+  "  link_rx_max[5]="+link_rx_max[5]
+  "  fan_speed[0]="+fan_speed[0]
+  "  fan_speed[1]="+fan_speed[1]
+  "  fan_speed[2]="+fan_speed[2]
+  "  fan_speed[3]="+fan_speed[3]
+  "  type="+type
+  "  cpu_cores[0]="+cpu_cores[0]
+  "  cpu_cores[1]="+cpu_cores[1]
+  "  cpu_cores[2]="+cpu_cores[2]
+  "  cpu_cores[3]="+cpu_cores[3]
+  "  cpu_cores[4]="+cpu_cores[4]
+  "  cpu_cores[5]="+cpu_cores[5]
+  "  cpu_cores[6]="+cpu_cores[6]
+  "  cpu_cores[7]="+cpu_cores[7]
+  "  cpu_combined[0]="+cpu_combined[0]
+  "  cpu_combined[1]="+cpu_combined[1]
+  "  cpu_combined[2]="+cpu_combined[2]
+  "  cpu_combined[3]="+cpu_combined[3]
+  "  cpu_combined[4]="+cpu_combined[4]
+  "  cpu_combined[5]="+cpu_combined[5]
+  "  cpu_combined[6]="+cpu_combined[6]
+  "  cpu_combined[7]="+cpu_combined[7]
+  "  cpu_combined[8]="+cpu_combined[8]
+  "  cpu_combined[9]="+cpu_combined[9]
+  "  gpu_cores[0]="+gpu_cores[0]
+  "  gpu_cores[1]="+gpu_cores[1]
+  "  gpu_cores[2]="+gpu_cores[2]
+  "  gpu_cores[3]="+gpu_cores[3]
+  "  gpu_combined[0]="+gpu_combined[0]
+  "  gpu_combined[1]="+gpu_combined[1]
+  "  gpu_combined[2]="+gpu_combined[2]
+  "  gpu_combined[3]="+gpu_combined[3]
+  "  gpu_combined[4]="+gpu_combined[4]
+  "  gpu_combined[5]="+gpu_combined[5]
+  "  gpu_combined[6]="+gpu_combined[6]
+  "  gpu_combined[7]="+gpu_combined[7]
+  "  gpu_combined[8]="+gpu_combined[8]
+  "  gpu_combined[9]="+gpu_combined[9]
+  "  temperature_board="+temperature_board
+  "  temperature_core[0]="+temperature_core[0]
+  "  temperature_core[1]="+temperature_core[1]
+  "  temperature_core[2]="+temperature_core[2]
+  "  temperature_core[3]="+temperature_core[3]
+  "  temperature_core[4]="+temperature_core[4]
+  "  temperature_core[5]="+temperature_core[5]
+  "  temperature_core[6]="+temperature_core[6]
+  "  temperature_core[7]="+temperature_core[7]
;}
}
