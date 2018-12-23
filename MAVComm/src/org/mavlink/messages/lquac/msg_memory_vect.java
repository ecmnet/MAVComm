/**
 * Generated class : msg_memory_vect
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
 * Class msg_memory_vect
 * Send raw controller memory. The use of this message is discouraged for normal packets, but a quite efficient way for testing new messages and getting experimental debug output.
 **/
public class msg_memory_vect extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_MEMORY_VECT = 249;
  private static final long serialVersionUID = MAVLINK_MSG_ID_MEMORY_VECT;
  public msg_memory_vect() {
    this(1,1);
}
  public msg_memory_vect(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_MEMORY_VECT;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 36;
}

  /**
   * Starting address of the debug variables
   */
  public int address;
  /**
   * Version code of the type variable. 0=unknown, type ignored and assumed int16_t. 1=as below
   */
  public int ver;
  /**
   * Type code of the memory variables. for ver = 1: 0=16 x int16_t, 1=16 x uint16_t, 2=16 x Q15, 3=16 x 1Q14
   */
  public int type;
  /**
   * Memory contents at specified address
   */
  public int[] value = new int[32];
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  address = (int)dis.readUnsignedShort()&0x00FFFF;
  ver = (int)dis.readUnsignedByte()&0x00FF;
  type = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<32; i++) {
    value[i] = (int)dis.readByte();
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+36];
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
  dos.writeShort(address&0x00FFFF);
  dos.writeByte(ver&0x00FF);
  dos.writeByte(type&0x00FF);
  for (int i=0; i<32; i++) {
    dos.write(value[i]&0x00FF);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 36);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[46] = crcl;
  buffer[47] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_MEMORY_VECT : " +   "  address="+address
+  "  ver="+ver
+  "  type="+type
+  "  value[0]="+value[0]
+  "  value[1]="+value[1]
+  "  value[2]="+value[2]
+  "  value[3]="+value[3]
+  "  value[4]="+value[4]
+  "  value[5]="+value[5]
+  "  value[6]="+value[6]
+  "  value[7]="+value[7]
+  "  value[8]="+value[8]
+  "  value[9]="+value[9]
+  "  value[10]="+value[10]
+  "  value[11]="+value[11]
+  "  value[12]="+value[12]
+  "  value[13]="+value[13]
+  "  value[14]="+value[14]
+  "  value[15]="+value[15]
+  "  value[16]="+value[16]
+  "  value[17]="+value[17]
+  "  value[18]="+value[18]
+  "  value[19]="+value[19]
+  "  value[20]="+value[20]
+  "  value[21]="+value[21]
+  "  value[22]="+value[22]
+  "  value[23]="+value[23]
+  "  value[24]="+value[24]
+  "  value[25]="+value[25]
+  "  value[26]="+value[26]
+  "  value[27]="+value[27]
+  "  value[28]="+value[28]
+  "  value[29]="+value[29]
+  "  value[30]="+value[30]
+  "  value[31]="+value[31]
;}
}
