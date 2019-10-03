/**
 * Generated class : msg_tunnel
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
 * Class msg_tunnel
 * Message for transporting "arbitrary" variable-length data from one component to another (broadcast is not forbidden, but discouraged). The encoding of the data is usually extension specific, i.e. determined by the source, and is usually not documented as part of the MAVLink specification.
 **/
public class msg_tunnel extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_TUNNEL = 385;
  private static final long serialVersionUID = MAVLINK_MSG_ID_TUNNEL;
  public msg_tunnel() {
    this(1,1);
}
  public msg_tunnel(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_TUNNEL;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 133;
}

  /**
   * A code that identifies the content of the payload (0 for unknown, which is the default). If this code is less than 32768, it is a 'registered' payload type and the corresponding code should be added to the MAV_TUNNEL_PAYLOAD_TYPE enum. Software creators can register blocks of types as needed. Codes greater than 32767 are considered local experiments and should not be checked in to any widely distributed codebase.
   */
  public int payload_type;
  /**
   * System ID (can be 0 for broadcast, but this is discouraged)
   */
  public int target_system;
  /**
   * Component ID (can be 0 for broadcast, but this is discouraged)
   */
  public int target_component;
  /**
   * Length of the data transported in payload
   */
  public int payload_length;
  /**
   * Variable length payload. The payload length is defined by payload_length. The entire content of this block is opaque unless you understand the encoding specified by payload_type.
   */
  public int[] payload = new int[128];
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  payload_type = (int)dis.readUnsignedShort()&0x00FFFF;
  target_system = (int)dis.readUnsignedByte()&0x00FF;
  target_component = (int)dis.readUnsignedByte()&0x00FF;
  payload_length = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<128; i++) {
    payload[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+133];
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
  dos.writeShort(payload_type&0x00FFFF);
  dos.writeByte(target_system&0x00FF);
  dos.writeByte(target_component&0x00FF);
  dos.writeByte(payload_length&0x00FF);
  for (int i=0; i<128; i++) {
    dos.writeByte(payload[i]&0x00FF);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 133);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[143] = crcl;
  buffer[144] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_TUNNEL : " +   "  payload_type="+payload_type
+  "  target_system="+target_system
+  "  target_component="+target_component
+  "  payload_length="+payload_length
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
;}
}
