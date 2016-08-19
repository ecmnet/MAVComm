/**
 * Generated class : msg_msp_mocap
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
 * Class msg_msp_mocap
 * MSP Status message.
 **/
public class msg_msp_mocap extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_MSP_MOCAP = 182;
  private static final long serialVersionUID = MAVLINK_MSG_ID_MSP_MOCAP;
  public msg_msp_mocap() {
    this(1,1);
}
  public msg_msp_mocap(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_MSP_MOCAP;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 28;
}

  /**
   * Timestamp
   */
  public long tms;
  /**
   * X Velocity
   */
  public float vx;
  /**
   * Y Velocity
   */
  public float vy;
  /**
   * Z Velocity
   */
  public float vz;
  /**
   * FPS of mocap system
   */
  public float fps;
  /**
   * Info flags
   */
  public long flags;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  tms = (long)dis.readLong();
  vx = (float)dis.readFloat();
  vy = (float)dis.readFloat();
  vz = (float)dis.readFloat();
  fps = (float)dis.readFloat();
  flags = (int)dis.readInt()&0x00FFFFFFFF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+28];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFE);
  dos.writeByte(length & 0x00FF);
  dos.writeByte(sequence & 0x00FF);
  dos.writeByte(sysId & 0x00FF);
  dos.writeByte(componentId & 0x00FF);
  dos.writeByte(messageType & 0x00FF);
  dos.writeLong(tms);
  dos.writeFloat(vx);
  dos.writeFloat(vy);
  dos.writeFloat(vz);
  dos.writeFloat(fps);
  dos.writeInt((int)(flags&0x00FFFFFFFF));
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 28);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[34] = crcl;
  buffer[35] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_MSP_MOCAP : " +   "  tms="+tms+  "  vx="+vx+  "  vy="+vy+  "  vz="+vz+  "  fps="+fps+  "  flags="+flags;}
}
