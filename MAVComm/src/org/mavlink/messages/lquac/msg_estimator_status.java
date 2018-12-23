/**
 * Generated class : msg_estimator_status
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
 * Class msg_estimator_status
 * Estimator status message including flags, innovation test ratios and estimated accuracies. The flags message is an integer bitmask containing information on which EKF outputs are valid. See the ESTIMATOR_STATUS_FLAGS enum definition for further information. The innovation test ratios show the magnitude of the sensor innovation divided by the innovation check threshold. Under normal operation the innovation test ratios should be below 0.5 with occasional values up to 1.0. Values greater than 1.0 should be rare under normal operation and indicate that a measurement has been rejected by the filter. The user should be notified if an innovation test ratio greater than 1.0 is recorded. Notifications for values in the range between 0.5 and 1.0 should be optional and controllable by the user.
 **/
public class msg_estimator_status extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_ESTIMATOR_STATUS = 230;
  private static final long serialVersionUID = MAVLINK_MSG_ID_ESTIMATOR_STATUS;
  public msg_estimator_status() {
    this(1,1);
}
  public msg_estimator_status(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_ESTIMATOR_STATUS;
    this.sysId = sysId;
    this.componentId = componentId;
    payload_length = 42;
}

  /**
   * Timestamp (UNIX Epoch time or time since system boot). The receiving end can infer timestamp format (since 1.1.1970 or since system boot) by checking for the magnitude the number.
   */
  public long time_usec;
  /**
   * Velocity innovation test ratio
   */
  public float vel_ratio;
  /**
   * Horizontal position innovation test ratio
   */
  public float pos_horiz_ratio;
  /**
   * Vertical position innovation test ratio
   */
  public float pos_vert_ratio;
  /**
   * Magnetometer innovation test ratio
   */
  public float mag_ratio;
  /**
   * Height above terrain innovation test ratio
   */
  public float hagl_ratio;
  /**
   * True airspeed innovation test ratio
   */
  public float tas_ratio;
  /**
   * Horizontal position 1-STD accuracy relative to the EKF local origin
   */
  public float pos_horiz_accuracy;
  /**
   * Vertical position 1-STD accuracy relative to the EKF local origin
   */
  public float pos_vert_accuracy;
  /**
   * Bitmap indicating which EKF outputs are valid.
   */
  public int flags;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_usec = (long)dis.readLong();
  vel_ratio = (float)dis.readFloat();
  pos_horiz_ratio = (float)dis.readFloat();
  pos_vert_ratio = (float)dis.readFloat();
  mag_ratio = (float)dis.readFloat();
  hagl_ratio = (float)dis.readFloat();
  tas_ratio = (float)dis.readFloat();
  pos_horiz_accuracy = (float)dis.readFloat();
  pos_vert_accuracy = (float)dis.readFloat();
  flags = (int)dis.readUnsignedShort()&0x00FFFF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+42];
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
  dos.writeFloat(vel_ratio);
  dos.writeFloat(pos_horiz_ratio);
  dos.writeFloat(pos_vert_ratio);
  dos.writeFloat(mag_ratio);
  dos.writeFloat(hagl_ratio);
  dos.writeFloat(tas_ratio);
  dos.writeFloat(pos_horiz_accuracy);
  dos.writeFloat(pos_vert_accuracy);
  dos.writeShort(flags&0x00FFFF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 42);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[52] = crcl;
  buffer[53] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_ESTIMATOR_STATUS : " +   "  time_usec="+time_usec
+  "  vel_ratio="+vel_ratio
+  "  pos_horiz_ratio="+pos_horiz_ratio
+  "  pos_vert_ratio="+pos_vert_ratio
+  "  mag_ratio="+mag_ratio
+  "  hagl_ratio="+hagl_ratio
+  "  tas_ratio="+tas_ratio
+  "  pos_horiz_accuracy="+pos_horiz_accuracy
+  "  pos_vert_accuracy="+pos_vert_accuracy
+  "  flags="+flags
;}
}
