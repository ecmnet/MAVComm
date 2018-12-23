/**
 * Generated class : msg_resource_request
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
 * Class msg_resource_request
 * The autopilot is requesting a resource (file, binary, other type of data)
 **/
public class msg_resource_request extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_RESOURCE_REQUEST = 142;
  private static final long serialVersionUID = MAVLINK_MSG_ID_RESOURCE_REQUEST;
  public msg_resource_request() {
    this(1,1);
}
  public msg_resource_request(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_RESOURCE_REQUEST;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 243;
}

  /**
   * Request ID. This ID should be re-used when sending back URI contents
   */
  public int request_id;
  /**
   * The type of requested URI. 0 = a file via URL. 1 = a UAVCAN binary
   */
  public int uri_type;
  /**
   * The requested unique resource identifier (URI). It is not necessarily a straight domain name (depends on the URI type enum)
   */
  public int[] uri = new int[120];
  /**
   * The way the autopilot wants to receive the URI. 0 = MAVLink FTP. 1 = binary stream.
   */
  public int transfer_type;
  /**
   * The storage path the autopilot wants the URI to be stored in. Will only be valid if the transfer_type has a storage associated (e.g. MAVLink FTP).
   */
  public int[] storage = new int[120];
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  request_id = (int)dis.readUnsignedByte()&0x00FF;
  uri_type = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<120; i++) {
    uri[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
  transfer_type = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<120; i++) {
    storage[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+243];
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
  dos.writeByte(request_id&0x00FF);
  dos.writeByte(uri_type&0x00FF);
  for (int i=0; i<120; i++) {
    dos.writeByte(uri[i]&0x00FF);
  }
  dos.writeByte(transfer_type&0x00FF);
  for (int i=0; i<120; i++) {
    dos.writeByte(storage[i]&0x00FF);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 243);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[253] = crcl;
  buffer[254] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_RESOURCE_REQUEST : " +   "  request_id="+request_id
+  "  uri_type="+uri_type
+  "  uri[0]="+uri[0]
+  "  uri[1]="+uri[1]
+  "  uri[2]="+uri[2]
+  "  uri[3]="+uri[3]
+  "  uri[4]="+uri[4]
+  "  uri[5]="+uri[5]
+  "  uri[6]="+uri[6]
+  "  uri[7]="+uri[7]
+  "  uri[8]="+uri[8]
+  "  uri[9]="+uri[9]
+  "  uri[10]="+uri[10]
+  "  uri[11]="+uri[11]
+  "  uri[12]="+uri[12]
+  "  uri[13]="+uri[13]
+  "  uri[14]="+uri[14]
+  "  uri[15]="+uri[15]
+  "  uri[16]="+uri[16]
+  "  uri[17]="+uri[17]
+  "  uri[18]="+uri[18]
+  "  uri[19]="+uri[19]
+  "  uri[20]="+uri[20]
+  "  uri[21]="+uri[21]
+  "  uri[22]="+uri[22]
+  "  uri[23]="+uri[23]
+  "  uri[24]="+uri[24]
+  "  uri[25]="+uri[25]
+  "  uri[26]="+uri[26]
+  "  uri[27]="+uri[27]
+  "  uri[28]="+uri[28]
+  "  uri[29]="+uri[29]
+  "  uri[30]="+uri[30]
+  "  uri[31]="+uri[31]
+  "  uri[32]="+uri[32]
+  "  uri[33]="+uri[33]
+  "  uri[34]="+uri[34]
+  "  uri[35]="+uri[35]
+  "  uri[36]="+uri[36]
+  "  uri[37]="+uri[37]
+  "  uri[38]="+uri[38]
+  "  uri[39]="+uri[39]
+  "  uri[40]="+uri[40]
+  "  uri[41]="+uri[41]
+  "  uri[42]="+uri[42]
+  "  uri[43]="+uri[43]
+  "  uri[44]="+uri[44]
+  "  uri[45]="+uri[45]
+  "  uri[46]="+uri[46]
+  "  uri[47]="+uri[47]
+  "  uri[48]="+uri[48]
+  "  uri[49]="+uri[49]
+  "  uri[50]="+uri[50]
+  "  uri[51]="+uri[51]
+  "  uri[52]="+uri[52]
+  "  uri[53]="+uri[53]
+  "  uri[54]="+uri[54]
+  "  uri[55]="+uri[55]
+  "  uri[56]="+uri[56]
+  "  uri[57]="+uri[57]
+  "  uri[58]="+uri[58]
+  "  uri[59]="+uri[59]
+  "  uri[60]="+uri[60]
+  "  uri[61]="+uri[61]
+  "  uri[62]="+uri[62]
+  "  uri[63]="+uri[63]
+  "  uri[64]="+uri[64]
+  "  uri[65]="+uri[65]
+  "  uri[66]="+uri[66]
+  "  uri[67]="+uri[67]
+  "  uri[68]="+uri[68]
+  "  uri[69]="+uri[69]
+  "  uri[70]="+uri[70]
+  "  uri[71]="+uri[71]
+  "  uri[72]="+uri[72]
+  "  uri[73]="+uri[73]
+  "  uri[74]="+uri[74]
+  "  uri[75]="+uri[75]
+  "  uri[76]="+uri[76]
+  "  uri[77]="+uri[77]
+  "  uri[78]="+uri[78]
+  "  uri[79]="+uri[79]
+  "  uri[80]="+uri[80]
+  "  uri[81]="+uri[81]
+  "  uri[82]="+uri[82]
+  "  uri[83]="+uri[83]
+  "  uri[84]="+uri[84]
+  "  uri[85]="+uri[85]
+  "  uri[86]="+uri[86]
+  "  uri[87]="+uri[87]
+  "  uri[88]="+uri[88]
+  "  uri[89]="+uri[89]
+  "  uri[90]="+uri[90]
+  "  uri[91]="+uri[91]
+  "  uri[92]="+uri[92]
+  "  uri[93]="+uri[93]
+  "  uri[94]="+uri[94]
+  "  uri[95]="+uri[95]
+  "  uri[96]="+uri[96]
+  "  uri[97]="+uri[97]
+  "  uri[98]="+uri[98]
+  "  uri[99]="+uri[99]
+  "  uri[100]="+uri[100]
+  "  uri[101]="+uri[101]
+  "  uri[102]="+uri[102]
+  "  uri[103]="+uri[103]
+  "  uri[104]="+uri[104]
+  "  uri[105]="+uri[105]
+  "  uri[106]="+uri[106]
+  "  uri[107]="+uri[107]
+  "  uri[108]="+uri[108]
+  "  uri[109]="+uri[109]
+  "  uri[110]="+uri[110]
+  "  uri[111]="+uri[111]
+  "  uri[112]="+uri[112]
+  "  uri[113]="+uri[113]
+  "  uri[114]="+uri[114]
+  "  uri[115]="+uri[115]
+  "  uri[116]="+uri[116]
+  "  uri[117]="+uri[117]
+  "  uri[118]="+uri[118]
+  "  uri[119]="+uri[119]
+  "  transfer_type="+transfer_type
+  "  storage[0]="+storage[0]
+  "  storage[1]="+storage[1]
+  "  storage[2]="+storage[2]
+  "  storage[3]="+storage[3]
+  "  storage[4]="+storage[4]
+  "  storage[5]="+storage[5]
+  "  storage[6]="+storage[6]
+  "  storage[7]="+storage[7]
+  "  storage[8]="+storage[8]
+  "  storage[9]="+storage[9]
+  "  storage[10]="+storage[10]
+  "  storage[11]="+storage[11]
+  "  storage[12]="+storage[12]
+  "  storage[13]="+storage[13]
+  "  storage[14]="+storage[14]
+  "  storage[15]="+storage[15]
+  "  storage[16]="+storage[16]
+  "  storage[17]="+storage[17]
+  "  storage[18]="+storage[18]
+  "  storage[19]="+storage[19]
+  "  storage[20]="+storage[20]
+  "  storage[21]="+storage[21]
+  "  storage[22]="+storage[22]
+  "  storage[23]="+storage[23]
+  "  storage[24]="+storage[24]
+  "  storage[25]="+storage[25]
+  "  storage[26]="+storage[26]
+  "  storage[27]="+storage[27]
+  "  storage[28]="+storage[28]
+  "  storage[29]="+storage[29]
+  "  storage[30]="+storage[30]
+  "  storage[31]="+storage[31]
+  "  storage[32]="+storage[32]
+  "  storage[33]="+storage[33]
+  "  storage[34]="+storage[34]
+  "  storage[35]="+storage[35]
+  "  storage[36]="+storage[36]
+  "  storage[37]="+storage[37]
+  "  storage[38]="+storage[38]
+  "  storage[39]="+storage[39]
+  "  storage[40]="+storage[40]
+  "  storage[41]="+storage[41]
+  "  storage[42]="+storage[42]
+  "  storage[43]="+storage[43]
+  "  storage[44]="+storage[44]
+  "  storage[45]="+storage[45]
+  "  storage[46]="+storage[46]
+  "  storage[47]="+storage[47]
+  "  storage[48]="+storage[48]
+  "  storage[49]="+storage[49]
+  "  storage[50]="+storage[50]
+  "  storage[51]="+storage[51]
+  "  storage[52]="+storage[52]
+  "  storage[53]="+storage[53]
+  "  storage[54]="+storage[54]
+  "  storage[55]="+storage[55]
+  "  storage[56]="+storage[56]
+  "  storage[57]="+storage[57]
+  "  storage[58]="+storage[58]
+  "  storage[59]="+storage[59]
+  "  storage[60]="+storage[60]
+  "  storage[61]="+storage[61]
+  "  storage[62]="+storage[62]
+  "  storage[63]="+storage[63]
+  "  storage[64]="+storage[64]
+  "  storage[65]="+storage[65]
+  "  storage[66]="+storage[66]
+  "  storage[67]="+storage[67]
+  "  storage[68]="+storage[68]
+  "  storage[69]="+storage[69]
+  "  storage[70]="+storage[70]
+  "  storage[71]="+storage[71]
+  "  storage[72]="+storage[72]
+  "  storage[73]="+storage[73]
+  "  storage[74]="+storage[74]
+  "  storage[75]="+storage[75]
+  "  storage[76]="+storage[76]
+  "  storage[77]="+storage[77]
+  "  storage[78]="+storage[78]
+  "  storage[79]="+storage[79]
+  "  storage[80]="+storage[80]
+  "  storage[81]="+storage[81]
+  "  storage[82]="+storage[82]
+  "  storage[83]="+storage[83]
+  "  storage[84]="+storage[84]
+  "  storage[85]="+storage[85]
+  "  storage[86]="+storage[86]
+  "  storage[87]="+storage[87]
+  "  storage[88]="+storage[88]
+  "  storage[89]="+storage[89]
+  "  storage[90]="+storage[90]
+  "  storage[91]="+storage[91]
+  "  storage[92]="+storage[92]
+  "  storage[93]="+storage[93]
+  "  storage[94]="+storage[94]
+  "  storage[95]="+storage[95]
+  "  storage[96]="+storage[96]
+  "  storage[97]="+storage[97]
+  "  storage[98]="+storage[98]
+  "  storage[99]="+storage[99]
+  "  storage[100]="+storage[100]
+  "  storage[101]="+storage[101]
+  "  storage[102]="+storage[102]
+  "  storage[103]="+storage[103]
+  "  storage[104]="+storage[104]
+  "  storage[105]="+storage[105]
+  "  storage[106]="+storage[106]
+  "  storage[107]="+storage[107]
+  "  storage[108]="+storage[108]
+  "  storage[109]="+storage[109]
+  "  storage[110]="+storage[110]
+  "  storage[111]="+storage[111]
+  "  storage[112]="+storage[112]
+  "  storage[113]="+storage[113]
+  "  storage[114]="+storage[114]
+  "  storage[115]="+storage[115]
+  "  storage[116]="+storage[116]
+  "  storage[117]="+storage[117]
+  "  storage[118]="+storage[118]
+  "  storage[119]="+storage[119]
;}
}
