/**
 * Generated class : msg_v2_extension
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
 * Class msg_v2_extension
 * Message implementing parts of the V2 payload specs in V1 frames for transitional support.
 **/
public class msg_v2_extension extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_V2_EXTENSION = 248;
  private static final long serialVersionUID = MAVLINK_MSG_ID_V2_EXTENSION;
  public msg_v2_extension() {
    this(1,1);
}
  public msg_v2_extension(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_V2_EXTENSION;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 254;
}

  /**
   * A code that identifies the software component that understands this message (analogous to USB device classes or mime type strings).  If this code is less than 32768, it is considered a 'registered' protocol extension and the corresponding entry should be added to https://github.com/mavlink/mavlink/extension-message-ids.xml.  Software creators can register blocks of message IDs as needed (useful for GCS specific metadata, etc...). Message_types greater than 32767 are considered local experiments and should not be checked in to any widely distributed codebase.
   */
  public int message_type;
  /**
   * Network ID (0 for broadcast)
   */
  public int target_network;
  /**
   * System ID (0 for broadcast)
   */
  public int target_system;
  /**
   * Component ID (0 for broadcast)
   */
  public int target_component;
  /**
   * Variable length payload. The length is defined by the remaining message length when subtracting the header and other fields.  The entire content of this block is opaque unless you understand any the encoding message_type.  The particular encoding used can be extension specific and might not always be documented as part of the mavlink specification.
   */
  public int[] payload = new int[249];
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  message_type = (int)dis.readUnsignedShort()&0x00FFFF;
  target_network = (int)dis.readUnsignedByte()&0x00FF;
  target_system = (int)dis.readUnsignedByte()&0x00FF;
  target_component = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<249; i++) {
    payload[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+254];
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
  dos.writeShort(message_type&0x00FFFF);
  dos.writeByte(target_network&0x00FF);
  dos.writeByte(target_system&0x00FF);
  dos.writeByte(target_component&0x00FF);
  for (int i=0; i<249; i++) {
    dos.writeByte(payload[i]&0x00FF);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 254);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[264] = crcl;
  buffer[265] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_V2_EXTENSION : " +   "  message_type="+message_type
+  "  target_network="+target_network
+  "  target_system="+target_system
+  "  target_component="+target_component
+  "  payload[0]="+payload[0]
+  "  payload[1]="+payload[1]
+  "  payload[2]="+payload[2]
+  "  payload[3]="+payload[3]
+  "  payload[4]="+payload[4]
+  "  payload[5]="+payload[5]
+  "  payload[6]="+payload[6]
+  "  payload[7]="+payload[7]
+  "  payload[8]="+payload[8]
+  "  payload[9]="+payload[9]
+  "  payload[10]="+payload[10]
+  "  payload[11]="+payload[11]
+  "  payload[12]="+payload[12]
+  "  payload[13]="+payload[13]
+  "  payload[14]="+payload[14]
+  "  payload[15]="+payload[15]
+  "  payload[16]="+payload[16]
+  "  payload[17]="+payload[17]
+  "  payload[18]="+payload[18]
+  "  payload[19]="+payload[19]
+  "  payload[20]="+payload[20]
+  "  payload[21]="+payload[21]
+  "  payload[22]="+payload[22]
+  "  payload[23]="+payload[23]
+  "  payload[24]="+payload[24]
+  "  payload[25]="+payload[25]
+  "  payload[26]="+payload[26]
+  "  payload[27]="+payload[27]
+  "  payload[28]="+payload[28]
+  "  payload[29]="+payload[29]
+  "  payload[30]="+payload[30]
+  "  payload[31]="+payload[31]
+  "  payload[32]="+payload[32]
+  "  payload[33]="+payload[33]
+  "  payload[34]="+payload[34]
+  "  payload[35]="+payload[35]
+  "  payload[36]="+payload[36]
+  "  payload[37]="+payload[37]
+  "  payload[38]="+payload[38]
+  "  payload[39]="+payload[39]
+  "  payload[40]="+payload[40]
+  "  payload[41]="+payload[41]
+  "  payload[42]="+payload[42]
+  "  payload[43]="+payload[43]
+  "  payload[44]="+payload[44]
+  "  payload[45]="+payload[45]
+  "  payload[46]="+payload[46]
+  "  payload[47]="+payload[47]
+  "  payload[48]="+payload[48]
+  "  payload[49]="+payload[49]
+  "  payload[50]="+payload[50]
+  "  payload[51]="+payload[51]
+  "  payload[52]="+payload[52]
+  "  payload[53]="+payload[53]
+  "  payload[54]="+payload[54]
+  "  payload[55]="+payload[55]
+  "  payload[56]="+payload[56]
+  "  payload[57]="+payload[57]
+  "  payload[58]="+payload[58]
+  "  payload[59]="+payload[59]
+  "  payload[60]="+payload[60]
+  "  payload[61]="+payload[61]
+  "  payload[62]="+payload[62]
+  "  payload[63]="+payload[63]
+  "  payload[64]="+payload[64]
+  "  payload[65]="+payload[65]
+  "  payload[66]="+payload[66]
+  "  payload[67]="+payload[67]
+  "  payload[68]="+payload[68]
+  "  payload[69]="+payload[69]
+  "  payload[70]="+payload[70]
+  "  payload[71]="+payload[71]
+  "  payload[72]="+payload[72]
+  "  payload[73]="+payload[73]
+  "  payload[74]="+payload[74]
+  "  payload[75]="+payload[75]
+  "  payload[76]="+payload[76]
+  "  payload[77]="+payload[77]
+  "  payload[78]="+payload[78]
+  "  payload[79]="+payload[79]
+  "  payload[80]="+payload[80]
+  "  payload[81]="+payload[81]
+  "  payload[82]="+payload[82]
+  "  payload[83]="+payload[83]
+  "  payload[84]="+payload[84]
+  "  payload[85]="+payload[85]
+  "  payload[86]="+payload[86]
+  "  payload[87]="+payload[87]
+  "  payload[88]="+payload[88]
+  "  payload[89]="+payload[89]
+  "  payload[90]="+payload[90]
+  "  payload[91]="+payload[91]
+  "  payload[92]="+payload[92]
+  "  payload[93]="+payload[93]
+  "  payload[94]="+payload[94]
+  "  payload[95]="+payload[95]
+  "  payload[96]="+payload[96]
+  "  payload[97]="+payload[97]
+  "  payload[98]="+payload[98]
+  "  payload[99]="+payload[99]
+  "  payload[100]="+payload[100]
+  "  payload[101]="+payload[101]
+  "  payload[102]="+payload[102]
+  "  payload[103]="+payload[103]
+  "  payload[104]="+payload[104]
+  "  payload[105]="+payload[105]
+  "  payload[106]="+payload[106]
+  "  payload[107]="+payload[107]
+  "  payload[108]="+payload[108]
+  "  payload[109]="+payload[109]
+  "  payload[110]="+payload[110]
+  "  payload[111]="+payload[111]
+  "  payload[112]="+payload[112]
+  "  payload[113]="+payload[113]
+  "  payload[114]="+payload[114]
+  "  payload[115]="+payload[115]
+  "  payload[116]="+payload[116]
+  "  payload[117]="+payload[117]
+  "  payload[118]="+payload[118]
+  "  payload[119]="+payload[119]
+  "  payload[120]="+payload[120]
+  "  payload[121]="+payload[121]
+  "  payload[122]="+payload[122]
+  "  payload[123]="+payload[123]
+  "  payload[124]="+payload[124]
+  "  payload[125]="+payload[125]
+  "  payload[126]="+payload[126]
+  "  payload[127]="+payload[127]
+  "  payload[128]="+payload[128]
+  "  payload[129]="+payload[129]
+  "  payload[130]="+payload[130]
+  "  payload[131]="+payload[131]
+  "  payload[132]="+payload[132]
+  "  payload[133]="+payload[133]
+  "  payload[134]="+payload[134]
+  "  payload[135]="+payload[135]
+  "  payload[136]="+payload[136]
+  "  payload[137]="+payload[137]
+  "  payload[138]="+payload[138]
+  "  payload[139]="+payload[139]
+  "  payload[140]="+payload[140]
+  "  payload[141]="+payload[141]
+  "  payload[142]="+payload[142]
+  "  payload[143]="+payload[143]
+  "  payload[144]="+payload[144]
+  "  payload[145]="+payload[145]
+  "  payload[146]="+payload[146]
+  "  payload[147]="+payload[147]
+  "  payload[148]="+payload[148]
+  "  payload[149]="+payload[149]
+  "  payload[150]="+payload[150]
+  "  payload[151]="+payload[151]
+  "  payload[152]="+payload[152]
+  "  payload[153]="+payload[153]
+  "  payload[154]="+payload[154]
+  "  payload[155]="+payload[155]
+  "  payload[156]="+payload[156]
+  "  payload[157]="+payload[157]
+  "  payload[158]="+payload[158]
+  "  payload[159]="+payload[159]
+  "  payload[160]="+payload[160]
+  "  payload[161]="+payload[161]
+  "  payload[162]="+payload[162]
+  "  payload[163]="+payload[163]
+  "  payload[164]="+payload[164]
+  "  payload[165]="+payload[165]
+  "  payload[166]="+payload[166]
+  "  payload[167]="+payload[167]
+  "  payload[168]="+payload[168]
+  "  payload[169]="+payload[169]
+  "  payload[170]="+payload[170]
+  "  payload[171]="+payload[171]
+  "  payload[172]="+payload[172]
+  "  payload[173]="+payload[173]
+  "  payload[174]="+payload[174]
+  "  payload[175]="+payload[175]
+  "  payload[176]="+payload[176]
+  "  payload[177]="+payload[177]
+  "  payload[178]="+payload[178]
+  "  payload[179]="+payload[179]
+  "  payload[180]="+payload[180]
+  "  payload[181]="+payload[181]
+  "  payload[182]="+payload[182]
+  "  payload[183]="+payload[183]
+  "  payload[184]="+payload[184]
+  "  payload[185]="+payload[185]
+  "  payload[186]="+payload[186]
+  "  payload[187]="+payload[187]
+  "  payload[188]="+payload[188]
+  "  payload[189]="+payload[189]
+  "  payload[190]="+payload[190]
+  "  payload[191]="+payload[191]
+  "  payload[192]="+payload[192]
+  "  payload[193]="+payload[193]
+  "  payload[194]="+payload[194]
+  "  payload[195]="+payload[195]
+  "  payload[196]="+payload[196]
+  "  payload[197]="+payload[197]
+  "  payload[198]="+payload[198]
+  "  payload[199]="+payload[199]
+  "  payload[200]="+payload[200]
+  "  payload[201]="+payload[201]
+  "  payload[202]="+payload[202]
+  "  payload[203]="+payload[203]
+  "  payload[204]="+payload[204]
+  "  payload[205]="+payload[205]
+  "  payload[206]="+payload[206]
+  "  payload[207]="+payload[207]
+  "  payload[208]="+payload[208]
+  "  payload[209]="+payload[209]
+  "  payload[210]="+payload[210]
+  "  payload[211]="+payload[211]
+  "  payload[212]="+payload[212]
+  "  payload[213]="+payload[213]
+  "  payload[214]="+payload[214]
+  "  payload[215]="+payload[215]
+  "  payload[216]="+payload[216]
+  "  payload[217]="+payload[217]
+  "  payload[218]="+payload[218]
+  "  payload[219]="+payload[219]
+  "  payload[220]="+payload[220]
+  "  payload[221]="+payload[221]
+  "  payload[222]="+payload[222]
+  "  payload[223]="+payload[223]
+  "  payload[224]="+payload[224]
+  "  payload[225]="+payload[225]
+  "  payload[226]="+payload[226]
+  "  payload[227]="+payload[227]
+  "  payload[228]="+payload[228]
+  "  payload[229]="+payload[229]
+  "  payload[230]="+payload[230]
+  "  payload[231]="+payload[231]
+  "  payload[232]="+payload[232]
+  "  payload[233]="+payload[233]
+  "  payload[234]="+payload[234]
+  "  payload[235]="+payload[235]
+  "  payload[236]="+payload[236]
+  "  payload[237]="+payload[237]
+  "  payload[238]="+payload[238]
+  "  payload[239]="+payload[239]
+  "  payload[240]="+payload[240]
+  "  payload[241]="+payload[241]
+  "  payload[242]="+payload[242]
+  "  payload[243]="+payload[243]
+  "  payload[244]="+payload[244]
+  "  payload[245]="+payload[245]
+  "  payload[246]="+payload[246]
+  "  payload[247]="+payload[247]
+  "  payload[248]="+payload[248]
;}
}
