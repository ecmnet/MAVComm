/**
 * Generated class : msg_wheel_distance
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
 * Class msg_wheel_distance
 * Cumulative distance traveled for each reported wheel.
 **/
public class msg_wheel_distance extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_WHEEL_DISTANCE = 9000;
  private static final long serialVersionUID = MAVLINK_MSG_ID_WHEEL_DISTANCE;
  public msg_wheel_distance() {
    this(1,1);
}
  public msg_wheel_distance(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_WHEEL_DISTANCE;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 137;
}

  /**
   * Timestamp (synced to UNIX time or since system boot).
   */
  public long time_usec;
  /**
   * Distance reported by individual wheel encoders. Forward rotations increase values, reverse rotations decrease them. Not all wheels will necessarily have wheel encoders; the mapping of encoders to wheel positions must be agreed/understood by the endpoints.
   */
  public double[] distance = new double[16];
  /**
   * Number of wheels reported.
   */
  public int count;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  for (int i=0; i<16; i++) {
    distance[i] = (double)dis.readDouble();
  }
  count = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+137];
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
  for (int i=0; i<16; i++) {
    dos.writeDouble(distance[i]);
  }
  dos.writeByte(count&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 137);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[147] = crcl;
  buffer[148] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_WHEEL_DISTANCE : " +   "  time_usec="+time_usec
+  "  distance[0]="+distance[0]
+  "  distance[1]="+distance[1]
+  "  distance[2]="+distance[2]
+  "  distance[3]="+distance[3]
+  "  distance[4]="+distance[4]
+  "  distance[5]="+distance[5]
+  "  distance[6]="+distance[6]
+  "  distance[7]="+distance[7]
+  "  distance[8]="+distance[8]
+  "  distance[9]="+distance[9]
+  "  distance[10]="+distance[10]
+  "  distance[11]="+distance[11]
+  "  distance[12]="+distance[12]
+  "  distance[13]="+distance[13]
+  "  distance[14]="+distance[14]
+  "  distance[15]="+distance[15]
+  "  count="+count
;}
}
