/**
 * Generated class : msg_gps2_rtk
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
 * Class msg_gps2_rtk
 * RTK GPS data. Gives information on the relative baseline calculation the GPS is reporting
 **/
public class msg_gps2_rtk extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_GPS2_RTK = 128;
  private static final long serialVersionUID = MAVLINK_MSG_ID_GPS2_RTK;
  public msg_gps2_rtk() {
    this(1,1);
}
  public msg_gps2_rtk(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_GPS2_RTK;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 35;
}

  /**
   * Time since boot of last baseline message received in ms.
   */
  public long time_last_baseline_ms;
  /**
   * GPS Time of Week of last baseline
   */
  public long tow;
  /**
   * Current baseline in ECEF x or NED north component in mm.
   */
  public long baseline_a_mm;
  /**
   * Current baseline in ECEF y or NED east component in mm.
   */
  public long baseline_b_mm;
  /**
   * Current baseline in ECEF z or NED down component in mm.
   */
  public long baseline_c_mm;
  /**
   * Current estimate of baseline accuracy.
   */
  public long accuracy;
  /**
   * Current number of integer ambiguity hypotheses.
   */
  public long iar_num_hypotheses;
  /**
   * GPS Week Number of last baseline
   */
  public int wn;
  /**
   * Identification of connected RTK receiver.
   */
  public int rtk_receiver_id;
  /**
   * GPS-specific health report for RTK data.
   */
  public int rtk_health;
  /**
   * Rate of baseline messages being received by GPS, in HZ
   */
  public int rtk_rate;
  /**
   * Current number of sats used for RTK calculation.
   */
  public int nsats;
  /**
   * Coordinate system of baseline. 0 == ECEF, 1 == NED
   */
  public int baseline_coords_type;
/**
 * Decode message with raw data
 */
public void decode(ByteBuffer dis) throws IOException {
  time_last_baseline_ms = (int)dis.getInt()&0x00FFFFFFFF;
  tow = (int)dis.getInt()&0x00FFFFFFFF;
  baseline_a_mm = (int)dis.getInt();
  baseline_b_mm = (int)dis.getInt();
  baseline_c_mm = (int)dis.getInt();
  accuracy = (int)dis.getInt()&0x00FFFFFFFF;
  iar_num_hypotheses = (int)dis.getInt();
  wn = (int)dis.getShort()&0x00FFFF;
  rtk_receiver_id = (int)dis.get()&0x00FF;
  rtk_health = (int)dis.get()&0x00FF;
  rtk_rate = (int)dis.get()&0x00FF;
  nsats = (int)dis.get()&0x00FF;
  baseline_coords_type = (int)dis.get()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+35];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.putInt((int)(time_last_baseline_ms&0x00FFFFFFFF));
  dos.putInt((int)(tow&0x00FFFFFFFF));
  dos.putInt((int)(baseline_a_mm&0x00FFFFFFFF));
  dos.putInt((int)(baseline_b_mm&0x00FFFFFFFF));
  dos.putInt((int)(baseline_c_mm&0x00FFFFFFFF));
  dos.putInt((int)(accuracy&0x00FFFFFFFF));
  dos.putInt((int)(iar_num_hypotheses&0x00FFFFFFFF));
  dos.putShort((short)(wn&0x00FFFF));
  dos.put((byte)(rtk_receiver_id&0x00FF));
  dos.put((byte)(rtk_health&0x00FF));
  dos.put((byte)(rtk_rate&0x00FF));
  dos.put((byte)(nsats&0x00FF));
  dos.put((byte)(baseline_coords_type&0x00FF));
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 35);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[41] = crcl;
  buffer[42] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_GPS2_RTK : " +   "  time_last_baseline_ms="+time_last_baseline_ms+  "  tow="+tow+  "  baseline_a_mm="+baseline_a_mm+  "  baseline_b_mm="+baseline_b_mm+  "  baseline_c_mm="+baseline_c_mm+  "  accuracy="+accuracy+  "  iar_num_hypotheses="+iar_num_hypotheses+  "  wn="+wn+  "  rtk_receiver_id="+rtk_receiver_id+  "  rtk_health="+rtk_health+  "  rtk_rate="+rtk_rate+  "  nsats="+nsats+  "  baseline_coords_type="+baseline_coords_type;}
}
