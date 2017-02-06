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
import org.mavlink.io.LittleEndianDataInputStream;
import org.mavlink.io.LittleEndianDataOutputStream;
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
    payload_length = 35;
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
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_last_baseline_ms = (int)dis.readInt()&0x00FFFFFFFF;
  tow = (int)dis.readInt()&0x00FFFFFFFF;
  baseline_a_mm = (int)dis.readInt();
  baseline_b_mm = (int)dis.readInt();
  baseline_c_mm = (int)dis.readInt();
  accuracy = (int)dis.readInt()&0x00FFFFFFFF;
  iar_num_hypotheses = (int)dis.readInt();
  wn = (int)dis.readUnsignedShort()&0x00FFFF;
  rtk_receiver_id = (int)dis.readUnsignedByte()&0x00FF;
  rtk_health = (int)dis.readUnsignedByte()&0x00FF;
  rtk_rate = (int)dis.readUnsignedByte()&0x00FF;
  nsats = (int)dis.readUnsignedByte()&0x00FF;
  baseline_coords_type = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[12+35];
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
  dos.writeInt((int)(time_last_baseline_ms&0x00FFFFFFFF));
  dos.writeInt((int)(tow&0x00FFFFFFFF));
  dos.writeInt((int)(baseline_a_mm&0x00FFFFFFFF));
  dos.writeInt((int)(baseline_b_mm&0x00FFFFFFFF));
  dos.writeInt((int)(baseline_c_mm&0x00FFFFFFFF));
  dos.writeInt((int)(accuracy&0x00FFFFFFFF));
  dos.writeInt((int)(iar_num_hypotheses&0x00FFFFFFFF));
  dos.writeShort(wn&0x00FFFF);
  dos.writeByte(rtk_receiver_id&0x00FF);
  dos.writeByte(rtk_health&0x00FF);
  dos.writeByte(rtk_rate&0x00FF);
  dos.writeByte(nsats&0x00FF);
  dos.writeByte(baseline_coords_type&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 35);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[45] = crcl;
  buffer[46] = crch;
  dos.close();
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_GPS2_RTK : " +   "  time_last_baseline_ms="+time_last_baseline_ms+  "  tow="+tow+  "  baseline_a_mm="+baseline_a_mm+  "  baseline_b_mm="+baseline_b_mm+  "  baseline_c_mm="+baseline_c_mm+  "  accuracy="+accuracy+  "  iar_num_hypotheses="+iar_num_hypotheses+  "  wn="+wn+  "  rtk_receiver_id="+rtk_receiver_id+  "  rtk_health="+rtk_health+  "  rtk_rate="+rtk_rate+  "  nsats="+nsats+  "  baseline_coords_type="+baseline_coords_type;}
}
