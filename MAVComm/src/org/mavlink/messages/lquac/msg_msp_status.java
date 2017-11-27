/**
 * Generated class : msg_msp_status
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
 * Class msg_msp_status
 * MSP Status message.
 **/
public class msg_msp_status extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_MSP_STATUS = 180;
  private static final long serialVersionUID = MAVLINK_MSG_ID_MSP_STATUS;
  public msg_msp_status() {
    this(1,1);
}
  public msg_msp_status(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_MSP_STATUS;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 58;
}

  /**
   * Boot time ms
   */
  public long uptime_ms;
  /**
   * Unix time us
   */
  public long unix_time_us;
  /**
   * AMA0 communication errors
   */
  public long com_error;
  /**
   * MSP Statusflags
   */
  public long status;
  /**
   * Autopilot mode
   */
  public long autopilot_mode;
  /**
   * CPU load of the companion
   */
  public int load;
  /**
   * Memory usage of the companion
   */
  public int memory;
  /**
   * Quality of Wifi connection in %
   */
  public int wifi_quality;
  /**
   * CPU Temperature
   */
  public int cpu_temp;
  /**
   * MSP software build
   */
  public char[] version = new char[16];
  public void setVersion(String tmp) {
    int len = Math.min(tmp.length(), 16);
    for (int i=0; i<len; i++) {
      version[i] = tmp.charAt(i);
    }
    for (int i=len; i<16; i++) {
      version[i] = 0;
    }
  }
  public String getVersion() {
    String result="";
    for (int i=0; i<16; i++) {
      if (version[i] != 0) result=result+version[i]; else break;
    }
    return result;
  }
  /**
   * Companion architecture
   */
  public char[] arch = new char[10];
  public void setArch(String tmp) {
    int len = Math.min(tmp.length(), 10);
    for (int i=0; i<len; i++) {
      arch[i] = tmp.charAt(i);
    }
    for (int i=len; i<10; i++) {
      arch[i] = 0;
    }
  }
  public String getArch() {
    String result="";
    for (int i=0; i<10; i++) {
      if (arch[i] != 0) result=result+arch[i]; else break;
    }
    return result;
  }
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  uptime_ms = (long)dis.readLong();
  unix_time_us = (long)dis.readLong();
  com_error = (int)dis.readInt()&0x00FFFFFFFF;
  status = (int)dis.readInt()&0x00FFFFFFFF;
  autopilot_mode = (int)dis.readInt()&0x00FFFFFFFF;
  load = (int)dis.readUnsignedByte()&0x00FF;
  memory = (int)dis.readUnsignedByte()&0x00FF;
  wifi_quality = (int)dis.readUnsignedByte()&0x00FF;
  cpu_temp = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<16; i++) {
    version[i] = (char)dis.readByte();
  }
  for (int i=0; i<10; i++) {
    arch[i] = (char)dis.readByte();
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+58];
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
  dos.writeLong(uptime_ms);
  dos.writeLong(unix_time_us);
  dos.writeInt((int)(com_error&0x00FFFFFFFF));
  dos.writeInt((int)(status&0x00FFFFFFFF));
  dos.writeInt((int)(autopilot_mode&0x00FFFFFFFF));
  dos.writeByte(load&0x00FF);
  dos.writeByte(memory&0x00FF);
  dos.writeByte(wifi_quality&0x00FF);
  dos.writeByte(cpu_temp&0x00FF);
  for (int i=0; i<16; i++) {
    dos.writeByte(version[i]);
  }
  for (int i=0; i<10; i++) {
    dos.writeByte(arch[i]);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 58);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[68] = crcl;
  buffer[69] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_MSP_STATUS : " +   "  uptime_ms="+uptime_ms+  "  unix_time_us="+unix_time_us+  "  com_error="+com_error+  "  status="+status+  "  autopilot_mode="+autopilot_mode+  "  load="+load+  "  memory="+memory+  "  wifi_quality="+wifi_quality+  "  cpu_temp="+cpu_temp+  "  version="+getVersion()+  "  arch="+getArch();}
}
