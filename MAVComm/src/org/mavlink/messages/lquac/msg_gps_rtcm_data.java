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
 * RTCM message for injecting into the onboard GPS (used for DGPS)
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
    payload_length = 182;
}

  /**
   * LSB: 1 means message is fragmented, next 2 bits are the fragment ID, the remaining 5 bits are used for the sequence ID. Messages are only to be flushed to the GPS when the entire message has been reconstructed on the autopilot. The fragment ID specifies which order the fragments should be assembled into a buffer, while the sequence ID is used to detect a mismatch between different buffers. The buffer is considered fully reconstructed when either all 4 fragments are present, or all the fragments before the first fragment with a non full payload is received. This management is used to ensure that normal GPS operation doesn't corrupt RTCM data, and to recover from a unreliable transport delivery order.
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
  dos.writeByte(payload_length & 0x00FF);
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
return "MAVLINK_MSG_ID_GPS_RTCM_DATA : " +   "  flags="+flags
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
+  "  data[110]="+data[110]
+  "  data[111]="+data[111]
+  "  data[112]="+data[112]
+  "  data[113]="+data[113]
+  "  data[114]="+data[114]
+  "  data[115]="+data[115]
+  "  data[116]="+data[116]
+  "  data[117]="+data[117]
+  "  data[118]="+data[118]
+  "  data[119]="+data[119]
+  "  data[120]="+data[120]
+  "  data[121]="+data[121]
+  "  data[122]="+data[122]
+  "  data[123]="+data[123]
+  "  data[124]="+data[124]
+  "  data[125]="+data[125]
+  "  data[126]="+data[126]
+  "  data[127]="+data[127]
+  "  data[128]="+data[128]
+  "  data[129]="+data[129]
+  "  data[130]="+data[130]
+  "  data[131]="+data[131]
+  "  data[132]="+data[132]
+  "  data[133]="+data[133]
+  "  data[134]="+data[134]
+  "  data[135]="+data[135]
+  "  data[136]="+data[136]
+  "  data[137]="+data[137]
+  "  data[138]="+data[138]
+  "  data[139]="+data[139]
+  "  data[140]="+data[140]
+  "  data[141]="+data[141]
+  "  data[142]="+data[142]
+  "  data[143]="+data[143]
+  "  data[144]="+data[144]
+  "  data[145]="+data[145]
+  "  data[146]="+data[146]
+  "  data[147]="+data[147]
+  "  data[148]="+data[148]
+  "  data[149]="+data[149]
+  "  data[150]="+data[150]
+  "  data[151]="+data[151]
+  "  data[152]="+data[152]
+  "  data[153]="+data[153]
+  "  data[154]="+data[154]
+  "  data[155]="+data[155]
+  "  data[156]="+data[156]
+  "  data[157]="+data[157]
+  "  data[158]="+data[158]
+  "  data[159]="+data[159]
+  "  data[160]="+data[160]
+  "  data[161]="+data[161]
+  "  data[162]="+data[162]
+  "  data[163]="+data[163]
+  "  data[164]="+data[164]
+  "  data[165]="+data[165]
+  "  data[166]="+data[166]
+  "  data[167]="+data[167]
+  "  data[168]="+data[168]
+  "  data[169]="+data[169]
+  "  data[170]="+data[170]
+  "  data[171]="+data[171]
+  "  data[172]="+data[172]
+  "  data[173]="+data[173]
+  "  data[174]="+data[174]
+  "  data[175]="+data[175]
+  "  data[176]="+data[176]
+  "  data[177]="+data[177]
+  "  data[178]="+data[178]
+  "  data[179]="+data[179]
;}
}
