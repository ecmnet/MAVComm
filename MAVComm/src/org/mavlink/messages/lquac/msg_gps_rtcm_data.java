/**
 * Generated class : msg_gps_rtcm_data
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
 * Class msg_gps_rtcm_data
 * WORK IN PROGRESS! RTCM message for injecting into the onboard GPS (used for DGPS)
 **/
public class msg_gps_rtcm_data extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_GPS_RTCM_DATA = 233;
  private static final long serialVersionUID = MAVLINK_MSG_ID_GPS_RTCM_DATA;
  public msg_gps_rtcm_data() {
    this(1,1);
}
  public msg_gps_rtcm_data(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_GPS_RTCM_DATA;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 182;
}

  /**
   * LSB: 1 means message is fragmented
   */
  public int flags;
  /**
   * data length
   */
  public int len;
  /**
   * RTCM message (may be fragmented)
   */
  public int[] data = new int[180];
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  flags = (int)dis.readUnsignedByte()&0x00FF;
  len = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<180; i++) {
    data[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+182];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFD);
  dos.writeByte(length & 0x00FF);
  dos.writeByte(incompat & 0x00FF);
  dos.writeByte(compat & 0x00FF);
  dos.writeByte(packet & 0x00FF);
  dos.writeByte(sysId & 0x00FF);
  dos.writeByte(componentId & 0x00FF);
  dos.writeByte(messageType & 0x00FF);
  dos.writeByte((messageType >> 8) & 0x00FF);
  dos.writeByte((messageType >> 16) & 0x00FF);
  dos.writeByte(flags&0x00FF);
  dos.writeByte(len&0x00FF);
  for (int i=0; i<180; i++) {
    dos.writeByte(data[i]&0x00FF);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 182);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[192] = crcl;
  buffer[193] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_GPS_RTCM_DATA : " +   "  flags="+flags+  "  len="+len+  "  data="+data;}
}
