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
    length = 33;
}

  /**
   * MAVComm version running
   */
  public long uptime_ms;
  /**
   * AMA0 communication errors
   */
  public long com_error;
  /**
   * MSP Statusflags
   */
  public long status;
  /**
   * The CPU load of the companion
   */
  public int load;
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
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  uptime_ms = (long)dis.readLong();
  com_error = (int)dis.readInt()&0x00FFFFFFFF;
  status = (int)dis.readInt()&0x00FFFFFFFF;
  load = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<16; i++) {
    version[i] = (char)dis.readByte();
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+33];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFE);
  dos.writeByte(length & 0x00FF);
  dos.writeByte(sequence & 0x00FF);
  dos.writeByte(sysId & 0x00FF);
  dos.writeByte(componentId & 0x00FF);
  dos.writeByte(messageType & 0x00FF);
  dos.writeLong(uptime_ms);
  dos.writeInt((int)(com_error&0x00FFFFFFFF));
  dos.writeInt((int)(status&0x00FFFFFFFF));
  dos.writeByte(load&0x00FF);
  for (int i=0; i<16; i++) {
    dos.writeByte(version[i]);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 33);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[39] = crcl;
  buffer[40] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_MSP_STATUS : " +   "  uptime_ms="+uptime_ms+  "  com_error="+com_error+  "  status="+status+  "  load="+load+  "  version="+getVersion();}
}
