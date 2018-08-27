/**
 * Generated class : msg_estimator_state_std
 * DO NOT MODIFY!
 **/
package org.mavlink.messages.lquac;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.mavlink.IMAVLinkCRC;
import org.mavlink.MAVLinkCRC;
import org.mavlink.io.LittleEndianDataInputStream;
import org.mavlink.io.LittleEndianDataOutputStream;
import org.mavlink.messages.MAVLinkMessage;
/**
 * Class msg_estimator_state_std
 * An estimator agnostic state standard deviation message
 **/
public class msg_estimator_state_std extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_ESTIMATOR_STATE_STD = 336;
  private static final long serialVersionUID = MAVLINK_MSG_ID_ESTIMATOR_STATE_STD;
  public msg_estimator_state_std() {
    this(1,1);
}
  public msg_estimator_state_std(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_ESTIMATOR_STATE_STD;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 189;
}

  /**
   * Timestamp (microseconds since system boot or since UNIX epoch)
   */
  public long time_usec;
  /**
   * The estimator state standard deviation sqrt(diag(P))
   */
  public float[] std = new float[30];
  /**
   * Number of states, max 30
   */
  public int n;
  /**
   * An array describing field type (see MAV_FIELD)
   */
  public int[] id = new int[30];
  /**
   * An array describing the sensor associated with the field (see MAV_SENSOR_TYPE)
   */
  public int[] sensor = new int[30];
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  for (int i=0; i<30; i++) {
    std[i] = (float)dis.readFloat();
  }
  n = (int)dis.readUnsignedByte()&0x00FF;
  for (int i=0; i<30; i++) {
    id[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
  for (int i=0; i<30; i++) {
    sensor[i] = (int)dis.readUnsignedByte()&0x00FF;
  }
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+189];
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
  for (int i=0; i<30; i++) {
    dos.writeFloat(std[i]);
  }
  dos.writeByte(n&0x00FF);
  for (int i=0; i<30; i++) {
    dos.writeByte(id[i]&0x00FF);
  }
  for (int i=0; i<30; i++) {
    dos.writeByte(sensor[i]&0x00FF);
  }
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 189);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[199] = crcl;
  buffer[200] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_ESTIMATOR_STATE_STD : " +   "  time_usec="+time_usec+  "  std="+std+  "  n="+n+  "  id="+id+  "  sensor="+sensor;}
}
