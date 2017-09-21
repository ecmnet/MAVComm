/**
 * Generated class : msg_msp_micro_slam
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
 * Class msg_msp_micro_slam
 * MSP MICRO SLAM Data encoded in longs
 **/
public class msg_msp_micro_slam extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_MSP_MICRO_SLAM = 184;
  private static final long serialVersionUID = MAVLINK_MSG_ID_MSP_MICRO_SLAM;
  public msg_msp_micro_slam() {
    this(1,1);
}
  public msg_msp_micro_slam(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_MSP_MICRO_SLAM;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 28;
}

  /**
   * Timestamp
   */
  public long tms;
  /**
   * Planned path X
   */
  public float px;
  /**
   * Planned path Y
   */
  public float py;
  /**
   * Planned direction
   */
  public float pd;
  /**
   * Planned speed
   */
  public float pv;
  /**
   * Counter of waypoints
   */
  public long wpcount;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  tms = (long)dis.readLong();
  px = (float)dis.readFloat();
  py = (float)dis.readFloat();
  pd = (float)dis.readFloat();
  pv = (float)dis.readFloat();
  wpcount = (int)dis.readInt()&0x00FFFFFFFF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+28];
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
  dos.writeLong(tms);
  dos.writeFloat(px);
  dos.writeFloat(py);
  dos.writeFloat(pd);
  dos.writeFloat(pv);
  dos.writeInt((int)(wpcount&0x00FFFFFFFF));
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 28);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[38] = crcl;
  buffer[39] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_MSP_MICRO_SLAM : " +   "  tms="+tms+  "  px="+px+  "  py="+py+  "  pd="+pd+  "  pv="+pv+  "  wpcount="+wpcount;}
}
