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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/**
 * Class msg_estimator_status
 * Estimator status message including flags, innovation test ratios and estimated accuracies. The flags message is an integer bitmask containing information on which EKF outputs are valid. See the ESTIMATOR_STATUS_FLAGS enum definition for further information. The innovaton test ratios show the magnitude of the sensor innovation divided by the innovation check threshold. Under normal operation the innovaton test ratios should be below 0.5 with occasional values up to 1.0. Values greater than 1.0 should be rare under normal operation and indicate that a measurement has been rejected by the filter. The user should be notified if an innovation test ratio greater than 1.0 is recorded. Notifications for values in the range between 0.5 and 1.0 should be optional and controllable by the user.
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
    length = 42;
}

  /**
   * Timestamp (micros since boot or Unix epoch)
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
   * Horizontal position 1-STD accuracy relative to the EKF local origin (m)
   */
  public float pos_horiz_accuracy;
  /**
   * Vertical position 1-STD accuracy relative to the EKF local origin (m)
   */
  public float pos_vert_accuracy;
  /**
   * Integer bitmask indicating which EKF outputs are valid. See definition for ESTIMATOR_STATUS_FLAGS.
   */
  public int flags;
/**
 * Decode message with raw data
 */
public void decode(ByteBuffer dis) throws IOException {
  time_usec = (long)dis.getLong();
  vel_ratio = (float)dis.getFloat();
  pos_horiz_ratio = (float)dis.getFloat();
  pos_vert_ratio = (float)dis.getFloat();
  mag_ratio = (float)dis.getFloat();
  hagl_ratio = (float)dis.getFloat();
  tas_ratio = (float)dis.getFloat();
  pos_horiz_accuracy = (float)dis.getFloat();
  pos_vert_accuracy = (float)dis.getFloat();
  flags = (int)dis.getShort()&0x00FFFF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+42];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.putLong(time_usec);
  dos.putFloat(vel_ratio);
  dos.putFloat(pos_horiz_ratio);
  dos.putFloat(pos_vert_ratio);
  dos.putFloat(mag_ratio);
  dos.putFloat(hagl_ratio);
  dos.putFloat(tas_ratio);
  dos.putFloat(pos_horiz_accuracy);
  dos.putFloat(pos_vert_accuracy);
  dos.putShort((short)(flags&0x00FFFF));
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 42);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[48] = crcl;
  buffer[49] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_ESTIMATOR_STATUS : " +   "  time_usec="+time_usec+  "  vel_ratio="+vel_ratio+  "  pos_horiz_ratio="+pos_horiz_ratio+  "  pos_vert_ratio="+pos_vert_ratio+  "  mag_ratio="+mag_ratio+  "  hagl_ratio="+hagl_ratio+  "  tas_ratio="+tas_ratio+  "  pos_horiz_accuracy="+pos_horiz_accuracy+  "  pos_vert_accuracy="+pos_vert_accuracy+  "  flags="+flags;}
}
