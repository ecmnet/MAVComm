/**
 * Generated class : msg_gps_status
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
 * Class msg_gps_status
 * The positioning status, as reported by GPS. This message is intended to display status information about each satellite visible to the receiver. See message GLOBAL_POSITION for the global position estimate. This message can contain information for up to 20 satellites.
 **/
public class msg_gps_status extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_GPS_STATUS = 25;
  private static final long serialVersionUID = MAVLINK_MSG_ID_GPS_STATUS;
  public msg_gps_status() {
    this(1,1);
}
  public msg_gps_status(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_GPS_STATUS;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 101;
}

  /**
   * Number of satellites visible
   */
  public int satellites_visible;
  /**
   * Global satellite ID
   */
  public int[] satellite_prn = new int[20];
  /**
   * 0: Satellite not used, 1: used for localization
   */
  public int[] satellite_used = new int[20];
  /**
   * Elevation (0: right on top of receiver, 90: on the horizon) of satellite
   */
  public int[] satellite_elevation = new int[20];
  /**
   * Direction of satellite, 0: 0 deg, 255: 360 deg.
   */
  public int[] satellite_azimuth = new int[20];
  /**
   * Signal to noise ratio of satellite
   */
  public int[] satellite_snr = new int[20];
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  satellites_visible = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<20; i++) {
    satellite_prn[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
  for (int i=0; i<20; i++) {
    satellite_used[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
  for (int i=0; i<20; i++) {
    satellite_elevation[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
  for (int i=0; i<20; i++) {
    satellite_azimuth[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
  for (int i=0; i<20; i++) {
    satellite_snr[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+101];
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
  dos.writeByte(satellites_visible&0x00FF);
  for (int i=0; i<20; i++) {
    dos.writeByte(satellite_prn[i]&0x00FF);
  }
  for (int i=0; i<20; i++) {
    dos.writeByte(satellite_used[i]&0x00FF);
  }
  for (int i=0; i<20; i++) {
    dos.writeByte(satellite_elevation[i]&0x00FF);
  }
  for (int i=0; i<20; i++) {
    dos.writeByte(satellite_azimuth[i]&0x00FF);
  }
  for (int i=0; i<20; i++) {
    dos.writeByte(satellite_snr[i]&0x00FF);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 101);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[111] = crcl;
  buffer[112] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_GPS_STATUS : " +   "  satellites_visible="+satellites_visible
+  "  satellite_prn[0]="+satellite_prn[0]
+  "  satellite_prn[1]="+satellite_prn[1]
+  "  satellite_prn[2]="+satellite_prn[2]
+  "  satellite_prn[3]="+satellite_prn[3]
+  "  satellite_prn[4]="+satellite_prn[4]
+  "  satellite_prn[5]="+satellite_prn[5]
+  "  satellite_prn[6]="+satellite_prn[6]
+  "  satellite_prn[7]="+satellite_prn[7]
+  "  satellite_prn[8]="+satellite_prn[8]
+  "  satellite_prn[9]="+satellite_prn[9]
+  "  satellite_prn[10]="+satellite_prn[10]
+  "  satellite_prn[11]="+satellite_prn[11]
+  "  satellite_prn[12]="+satellite_prn[12]
+  "  satellite_prn[13]="+satellite_prn[13]
+  "  satellite_prn[14]="+satellite_prn[14]
+  "  satellite_prn[15]="+satellite_prn[15]
+  "  satellite_prn[16]="+satellite_prn[16]
+  "  satellite_prn[17]="+satellite_prn[17]
+  "  satellite_prn[18]="+satellite_prn[18]
+  "  satellite_prn[19]="+satellite_prn[19]
+  "  satellite_used[0]="+satellite_used[0]
+  "  satellite_used[1]="+satellite_used[1]
+  "  satellite_used[2]="+satellite_used[2]
+  "  satellite_used[3]="+satellite_used[3]
+  "  satellite_used[4]="+satellite_used[4]
+  "  satellite_used[5]="+satellite_used[5]
+  "  satellite_used[6]="+satellite_used[6]
+  "  satellite_used[7]="+satellite_used[7]
+  "  satellite_used[8]="+satellite_used[8]
+  "  satellite_used[9]="+satellite_used[9]
+  "  satellite_used[10]="+satellite_used[10]
+  "  satellite_used[11]="+satellite_used[11]
+  "  satellite_used[12]="+satellite_used[12]
+  "  satellite_used[13]="+satellite_used[13]
+  "  satellite_used[14]="+satellite_used[14]
+  "  satellite_used[15]="+satellite_used[15]
+  "  satellite_used[16]="+satellite_used[16]
+  "  satellite_used[17]="+satellite_used[17]
+  "  satellite_used[18]="+satellite_used[18]
+  "  satellite_used[19]="+satellite_used[19]
+  "  satellite_elevation[0]="+satellite_elevation[0]
+  "  satellite_elevation[1]="+satellite_elevation[1]
+  "  satellite_elevation[2]="+satellite_elevation[2]
+  "  satellite_elevation[3]="+satellite_elevation[3]
+  "  satellite_elevation[4]="+satellite_elevation[4]
+  "  satellite_elevation[5]="+satellite_elevation[5]
+  "  satellite_elevation[6]="+satellite_elevation[6]
+  "  satellite_elevation[7]="+satellite_elevation[7]
+  "  satellite_elevation[8]="+satellite_elevation[8]
+  "  satellite_elevation[9]="+satellite_elevation[9]
+  "  satellite_elevation[10]="+satellite_elevation[10]
+  "  satellite_elevation[11]="+satellite_elevation[11]
+  "  satellite_elevation[12]="+satellite_elevation[12]
+  "  satellite_elevation[13]="+satellite_elevation[13]
+  "  satellite_elevation[14]="+satellite_elevation[14]
+  "  satellite_elevation[15]="+satellite_elevation[15]
+  "  satellite_elevation[16]="+satellite_elevation[16]
+  "  satellite_elevation[17]="+satellite_elevation[17]
+  "  satellite_elevation[18]="+satellite_elevation[18]
+  "  satellite_elevation[19]="+satellite_elevation[19]
+  "  satellite_azimuth[0]="+satellite_azimuth[0]
+  "  satellite_azimuth[1]="+satellite_azimuth[1]
+  "  satellite_azimuth[2]="+satellite_azimuth[2]
+  "  satellite_azimuth[3]="+satellite_azimuth[3]
+  "  satellite_azimuth[4]="+satellite_azimuth[4]
+  "  satellite_azimuth[5]="+satellite_azimuth[5]
+  "  satellite_azimuth[6]="+satellite_azimuth[6]
+  "  satellite_azimuth[7]="+satellite_azimuth[7]
+  "  satellite_azimuth[8]="+satellite_azimuth[8]
+  "  satellite_azimuth[9]="+satellite_azimuth[9]
+  "  satellite_azimuth[10]="+satellite_azimuth[10]
+  "  satellite_azimuth[11]="+satellite_azimuth[11]
+  "  satellite_azimuth[12]="+satellite_azimuth[12]
+  "  satellite_azimuth[13]="+satellite_azimuth[13]
+  "  satellite_azimuth[14]="+satellite_azimuth[14]
+  "  satellite_azimuth[15]="+satellite_azimuth[15]
+  "  satellite_azimuth[16]="+satellite_azimuth[16]
+  "  satellite_azimuth[17]="+satellite_azimuth[17]
+  "  satellite_azimuth[18]="+satellite_azimuth[18]
+  "  satellite_azimuth[19]="+satellite_azimuth[19]
+  "  satellite_snr[0]="+satellite_snr[0]
+  "  satellite_snr[1]="+satellite_snr[1]
+  "  satellite_snr[2]="+satellite_snr[2]
+  "  satellite_snr[3]="+satellite_snr[3]
+  "  satellite_snr[4]="+satellite_snr[4]
+  "  satellite_snr[5]="+satellite_snr[5]
+  "  satellite_snr[6]="+satellite_snr[6]
+  "  satellite_snr[7]="+satellite_snr[7]
+  "  satellite_snr[8]="+satellite_snr[8]
+  "  satellite_snr[9]="+satellite_snr[9]
+  "  satellite_snr[10]="+satellite_snr[10]
+  "  satellite_snr[11]="+satellite_snr[11]
+  "  satellite_snr[12]="+satellite_snr[12]
+  "  satellite_snr[13]="+satellite_snr[13]
+  "  satellite_snr[14]="+satellite_snr[14]
+  "  satellite_snr[15]="+satellite_snr[15]
+  "  satellite_snr[16]="+satellite_snr[16]
+  "  satellite_snr[17]="+satellite_snr[17]
+  "  satellite_snr[18]="+satellite_snr[18]
+  "  satellite_snr[19]="+satellite_snr[19]
;}
}
