/**
 * Generated class : msg_gps_inject_data
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
 * Class msg_gps_inject_data
 * Data for injecting into the onboard GPS (used for DGPS)
 **/
public class msg_gps_inject_data extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_GPS_INJECT_DATA = 123;
  private static final long serialVersionUID = MAVLINK_MSG_ID_GPS_INJECT_DATA;
  public msg_gps_inject_data() {
    this(1,1);
}
  public msg_gps_inject_data(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_GPS_INJECT_DATA;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 113;
}

  /**
   * System ID
   */
  public int target_system;
  /**
   * Component ID
   */
  public int target_component;
  /**
   * Data length
   */
  public int len;
  /**
   * Raw data (110 is enough for 12 satellites of RTCMv2)
   */
  public int[] data = new int[110];
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  target_system = (int)dis.readUnsignedByte()&0x00FF;
  target_component = (int)dis.readUnsignedByte()&0x00FF;
  len = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<110; i++) {
    data[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+113];
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
  dos.writeByte(target_system&0x00FF);
  dos.writeByte(target_component&0x00FF);
  dos.writeByte(len&0x00FF);
  for (int i=0; i<110; i++) {
    dos.writeByte(data[i]&0x00FF);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 113);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[123] = crcl;
  buffer[124] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_GPS_INJECT_DATA : " +   "  target_system="+target_system
+  "  target_component="+target_component
+  "  len="+len
+  "  data[0]="+data[0]
+  "  data[1]="+data[1]
+  "  data[2]="+data[2]
+  "  data[3]="+data[3]
+  "  data[4]="+data[4]
+  "  data[5]="+data[5]
+  "  data[6]="+data[6]
+  "  data[7]="+data[7]
+  "  data[8]="+data[8]
+  "  data[9]="+data[9]
+  "  data[10]="+data[10]
+  "  data[11]="+data[11]
+  "  data[12]="+data[12]
+  "  data[13]="+data[13]
+  "  data[14]="+data[14]
+  "  data[15]="+data[15]
+  "  data[16]="+data[16]
+  "  data[17]="+data[17]
+  "  data[18]="+data[18]
+  "  data[19]="+data[19]
+  "  data[20]="+data[20]
+  "  data[21]="+data[21]
+  "  data[22]="+data[22]
+  "  data[23]="+data[23]
+  "  data[24]="+data[24]
+  "  data[25]="+data[25]
+  "  data[26]="+data[26]
+  "  data[27]="+data[27]
+  "  data[28]="+data[28]
+  "  data[29]="+data[29]
+  "  data[30]="+data[30]
+  "  data[31]="+data[31]
+  "  data[32]="+data[32]
+  "  data[33]="+data[33]
+  "  data[34]="+data[34]
+  "  data[35]="+data[35]
+  "  data[36]="+data[36]
+  "  data[37]="+data[37]
+  "  data[38]="+data[38]
+  "  data[39]="+data[39]
+  "  data[40]="+data[40]
+  "  data[41]="+data[41]
+  "  data[42]="+data[42]
+  "  data[43]="+data[43]
+  "  data[44]="+data[44]
+  "  data[45]="+data[45]
+  "  data[46]="+data[46]
+  "  data[47]="+data[47]
+  "  data[48]="+data[48]
+  "  data[49]="+data[49]
+  "  data[50]="+data[50]
+  "  data[51]="+data[51]
+  "  data[52]="+data[52]
+  "  data[53]="+data[53]
+  "  data[54]="+data[54]
+  "  data[55]="+data[55]
+  "  data[56]="+data[56]
+  "  data[57]="+data[57]
+  "  data[58]="+data[58]
+  "  data[59]="+data[59]
+  "  data[60]="+data[60]
+  "  data[61]="+data[61]
+  "  data[62]="+data[62]
+  "  data[63]="+data[63]
+  "  data[64]="+data[64]
+  "  data[65]="+data[65]
+  "  data[66]="+data[66]
+  "  data[67]="+data[67]
+  "  data[68]="+data[68]
+  "  data[69]="+data[69]
+  "  data[70]="+data[70]
+  "  data[71]="+data[71]
+  "  data[72]="+data[72]
+  "  data[73]="+data[73]
+  "  data[74]="+data[74]
+  "  data[75]="+data[75]
+  "  data[76]="+data[76]
+  "  data[77]="+data[77]
+  "  data[78]="+data[78]
+  "  data[79]="+data[79]
+  "  data[80]="+data[80]
+  "  data[81]="+data[81]
+  "  data[82]="+data[82]
+  "  data[83]="+data[83]
+  "  data[84]="+data[84]
+  "  data[85]="+data[85]
+  "  data[86]="+data[86]
+  "  data[87]="+data[87]
+  "  data[88]="+data[88]
+  "  data[89]="+data[89]
+  "  data[90]="+data[90]
+  "  data[91]="+data[91]
+  "  data[92]="+data[92]
+  "  data[93]="+data[93]
+  "  data[94]="+data[94]
+  "  data[95]="+data[95]
+  "  data[96]="+data[96]
+  "  data[97]="+data[97]
+  "  data[98]="+data[98]
+  "  data[99]="+data[99]
+  "  data[100]="+data[100]
+  "  data[101]="+data[101]
+  "  data[102]="+data[102]
+  "  data[103]="+data[103]
+  "  data[104]="+data[104]
+  "  data[105]="+data[105]
+  "  data[106]="+data[106]
+  "  data[107]="+data[107]
+  "  data[108]="+data[108]
+  "  data[109]="+data[109]
;}
}
