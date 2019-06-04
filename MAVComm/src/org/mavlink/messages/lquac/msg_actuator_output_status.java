/**
 * Generated class : msg_actuator_output_status
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
 * Class msg_actuator_output_status
 * The raw values of the actuator outputs.
 **/
public class msg_actuator_output_status extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_ACTUATOR_OUTPUT_STATUS = 375;
  private static final long serialVersionUID = MAVLINK_MSG_ID_ACTUATOR_OUTPUT_STATUS;
  public msg_actuator_output_status() {
    this(1,1);
}
  public msg_actuator_output_status(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_ACTUATOR_OUTPUT_STATUS;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 140;
}

  /**
   * Timestamp (since system boot).
   */
  public long time_usec;
  /**
   * Active outputs
   */
  public long active;
  /**
   * Servo / motor output array values. Zero values indicate unused channels.
   */
  public float[] actuator = new float[32];
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  active = (int)dis.readInt()&0x00FFFFFFFF;
  for (int i=0; i<32; i++) {
    actuator[i] = (float)dis.readFloat();
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+140];
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
  dos.writeLong(time_usec);
  dos.writeInt((int)(active&0x00FFFFFFFF));
  for (int i=0; i<32; i++) {
    dos.writeFloat(actuator[i]);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 140);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[150] = crcl;
  buffer[151] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_ACTUATOR_OUTPUT_STATUS : " +   "  time_usec="+time_usec
+  "  active="+active
+  "  actuator[0]="+actuator[0]
+  "  actuator[1]="+actuator[1]
+  "  actuator[2]="+actuator[2]
+  "  actuator[3]="+actuator[3]
+  "  actuator[4]="+actuator[4]
+  "  actuator[5]="+actuator[5]
+  "  actuator[6]="+actuator[6]
+  "  actuator[7]="+actuator[7]
+  "  actuator[8]="+actuator[8]
+  "  actuator[9]="+actuator[9]
+  "  actuator[10]="+actuator[10]
+  "  actuator[11]="+actuator[11]
+  "  actuator[12]="+actuator[12]
+  "  actuator[13]="+actuator[13]
+  "  actuator[14]="+actuator[14]
+  "  actuator[15]="+actuator[15]
+  "  actuator[16]="+actuator[16]
+  "  actuator[17]="+actuator[17]
+  "  actuator[18]="+actuator[18]
+  "  actuator[19]="+actuator[19]
+  "  actuator[20]="+actuator[20]
+  "  actuator[21]="+actuator[21]
+  "  actuator[22]="+actuator[22]
+  "  actuator[23]="+actuator[23]
+  "  actuator[24]="+actuator[24]
+  "  actuator[25]="+actuator[25]
+  "  actuator[26]="+actuator[26]
+  "  actuator[27]="+actuator[27]
+  "  actuator[28]="+actuator[28]
+  "  actuator[29]="+actuator[29]
+  "  actuator[30]="+actuator[30]
+  "  actuator[31]="+actuator[31]
;}
}
