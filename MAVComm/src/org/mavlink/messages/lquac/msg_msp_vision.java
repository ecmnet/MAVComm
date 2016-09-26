/**
 * Generated class : msg_msp_vision
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
 * Class msg_msp_vision
 * MSP Vision
 **/
public class msg_msp_vision extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_MSP_VISION = 182;
  private static final long serialVersionUID = MAVLINK_MSG_ID_MSP_VISION;
  public msg_msp_vision() {
    this(1,1);
}
  public msg_msp_vision(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_MSP_VISION;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 61;
}

  /**
   * Timestamp
   */
  public long tms;
  /**
   * X Position
   */
  public float x;
  /**
   * Y Position
   */
  public float y;
  /**
   * Z Position
   */
  public float z;
  /**
   * Roll
   */
  public float ro;
  /**
   * Pitch
   */
  public float pi;
  /**
   * Yaw
   */
  public float ya;
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
   * Initial heading
   */
  public float h;
  /**
   * FPS of mocap system
   */
  public float fps;
  /**
   * Info flags
   */
  public long flags;
  /**
   * Error counter
   */
  public long errors;
  /**
   * Quality of estimation
   */
  public int quality;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  tms = (long)dis.readLong();
  x = (float)dis.readFloat();
  y = (float)dis.readFloat();
  z = (float)dis.readFloat();
  ro = (float)dis.readFloat();
  pi = (float)dis.readFloat();
  ya = (float)dis.readFloat();
  vx = (float)dis.readFloat();
  vy = (float)dis.readFloat();
  vz = (float)dis.readFloat();
  h = (float)dis.readFloat();
  fps = (float)dis.readFloat();
  flags = (int)dis.readInt()&0x00FFFFFFFF;
  errors = (int)dis.readInt()&0x00FFFFFFFF;
  quality = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+61];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFE);
  dos.writeByte(length & 0x00FF);
  dos.writeByte(sequence & 0x00FF);
  dos.writeByte(sysId & 0x00FF);
  dos.writeByte(componentId & 0x00FF);
  dos.writeByte(messageType & 0x00FF);
  dos.writeLong(tms);
  dos.writeFloat(x);
  dos.writeFloat(y);
  dos.writeFloat(z);
  dos.writeFloat(ro);
  dos.writeFloat(pi);
  dos.writeFloat(ya);
  dos.writeFloat(vx);
  dos.writeFloat(vy);
  dos.writeFloat(vz);
  dos.writeFloat(h);
  dos.writeFloat(fps);
  dos.writeInt((int)(flags&0x00FFFFFFFF));
  dos.writeInt((int)(errors&0x00FFFFFFFF));
  dos.writeByte(quality&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 61);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[67] = crcl;
  buffer[68] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_MSP_VISION : " +   "  tms="+tms+  "  x="+x+  "  y="+y+  "  z="+z+  "  ro="+ro+  "  pi="+pi+  "  ya="+ya+  "  vx="+vx+  "  vy="+vy+  "  vz="+vz+  "  h="+h+  "  fps="+fps+  "  flags="+flags+  "  errors="+errors+  "  quality="+quality;}
}
