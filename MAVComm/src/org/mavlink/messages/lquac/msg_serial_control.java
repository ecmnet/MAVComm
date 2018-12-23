/**
 * Generated class : msg_serial_control
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
 * Class msg_serial_control
 * Control a serial port. This can be used for raw access to an onboard serial peripheral such as a GPS or telemetry radio. It is designed to make it possible to update the devices firmware via MAVLink messages or change the devices settings. A message with zero bytes can be used to change just the baudrate.
 **/
public class msg_serial_control extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_SERIAL_CONTROL = 126;
  private static final long serialVersionUID = MAVLINK_MSG_ID_SERIAL_CONTROL;
  public msg_serial_control() {
    this(1,1);
}
  public msg_serial_control(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_SERIAL_CONTROL;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 79;
}

  /**
   * Baudrate of transfer. Zero means no change.
   */
  public long baudrate;
  /**
   * Timeout for reply data
   */
  public int timeout;
  /**
   * Serial control device type.
   */
  public int device;
  /**
   * Bitmap of serial control flags.
   */
  public int flags;
  /**
   * how many bytes in this transfer
   */
  public int count;
  /**
   * serial data
   */
  public int[] data = new int[70];
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  baudrate = (int)dis.readInt()&0x00FFFFFFFF;
  timeout = (int)dis.readUnsignedShort()&0x00FFFF;
  device = (int)dis.readUnsignedByte()&0x00FF;
  flags = (int)dis.readUnsignedByte()&0x00FF;
  count = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<70; i++) {
    data[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+79];
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
  dos.writeInt((int)(baudrate&0x00FFFFFFFF));
  dos.writeShort(timeout&0x00FFFF);
  dos.writeByte(device&0x00FF);
  dos.writeByte(flags&0x00FF);
  dos.writeByte(count&0x00FF);
  for (int i=0; i<70; i++) {
    dos.writeByte(data[i]&0x00FF);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 79);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[89] = crcl;
  buffer[90] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_SERIAL_CONTROL : " +   "  baudrate="+baudrate
+  "  timeout="+timeout
+  "  device="+device
+  "  flags="+flags
+  "  count="+count
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
;}
}
