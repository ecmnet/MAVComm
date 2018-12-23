/**
 * Generated class : msg_highres_imu
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
 * Class msg_highres_imu
 * The IMU readings in SI units in NED body frame
 **/
public class msg_highres_imu extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_HIGHRES_IMU = 105;
  private static final long serialVersionUID = MAVLINK_MSG_ID_HIGHRES_IMU;
  public msg_highres_imu() {
    this(1,1);
}
  public msg_highres_imu(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_HIGHRES_IMU;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 62;
}

  /**
   * Timestamp (UNIX Epoch time or time since system boot). The receiving end can infer timestamp format (since 1.1.1970 or since system boot) by checking for the magnitude the number.
   */
  public long time_usec;
  /**
   * X acceleration
   */
  public float xacc;
  /**
   * Y acceleration
   */
  public float yacc;
  /**
   * Z acceleration
   */
  public float zacc;
  /**
   * Angular speed around X axis
   */
  public float xgyro;
  /**
   * Angular speed around Y axis
   */
  public float ygyro;
  /**
   * Angular speed around Z axis
   */
  public float zgyro;
  /**
   * X Magnetic field
   */
  public float xmag;
  /**
   * Y Magnetic field
   */
  public float ymag;
  /**
   * Z Magnetic field
   */
  public float zmag;
  /**
   * Absolute pressure
   */
  public float abs_pressure;
  /**
   * Differential pressure
   */
  public float diff_pressure;
  /**
   * Altitude calculated from pressure
   */
  public float pressure_alt;
  /**
   * Temperature
   */
  public float temperature;
  /**
   * Bitmap for fields that have updated since last message, bit 0 = xacc, bit 12: temperature
   */
  public int fields_updated;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  xacc = (float)dis.readFloat();
  yacc = (float)dis.readFloat();
  zacc = (float)dis.readFloat();
  xgyro = (float)dis.readFloat();
  ygyro = (float)dis.readFloat();
  zgyro = (float)dis.readFloat();
  xmag = (float)dis.readFloat();
  ymag = (float)dis.readFloat();
  zmag = (float)dis.readFloat();
  abs_pressure = (float)dis.readFloat();
  diff_pressure = (float)dis.readFloat();
  pressure_alt = (float)dis.readFloat();
  temperature = (float)dis.readFloat();
  fields_updated = (int)dis.readUnsignedShort()&0x00FFFF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+62];
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
  dos.writeFloat(xacc);
  dos.writeFloat(yacc);
  dos.writeFloat(zacc);
  dos.writeFloat(xgyro);
  dos.writeFloat(ygyro);
  dos.writeFloat(zgyro);
  dos.writeFloat(xmag);
  dos.writeFloat(ymag);
  dos.writeFloat(zmag);
  dos.writeFloat(abs_pressure);
  dos.writeFloat(diff_pressure);
  dos.writeFloat(pressure_alt);
  dos.writeFloat(temperature);
  dos.writeShort(fields_updated&0x00FFFF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 62);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[72] = crcl;
  buffer[73] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_HIGHRES_IMU : " +   "  time_usec="+time_usec
+  "  xacc="+xacc
+  "  yacc="+yacc
+  "  zacc="+zacc
+  "  xgyro="+xgyro
+  "  ygyro="+ygyro
+  "  zgyro="+zgyro
+  "  xmag="+xmag
+  "  ymag="+ymag
+  "  zmag="+zmag
+  "  abs_pressure="+abs_pressure
+  "  diff_pressure="+diff_pressure
+  "  pressure_alt="+pressure_alt
+  "  temperature="+temperature
+  "  fields_updated="+fields_updated
;}
}
