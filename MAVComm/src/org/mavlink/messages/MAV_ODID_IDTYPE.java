/**
 * Generated class : MAV_ODID_IDTYPE
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MAV_ODID_IDTYPE
 * 
 **/
public interface MAV_ODID_IDTYPE {
    /**
     * No type defined.
     */
    public final static int MAV_ODID_IDTYPE_NONE = 0;
    /**
     * Manufacturer Serial Number (ANSI/CTA-2063 format).
     */
    public final static int MAV_ODID_IDTYPE_SERIAL_NUMBER = 1;
    /**
     * CAA (Civil Aviation Authority) assigned ID. Format: [ICAO Country Code].[CAA Assigned ID]
     */
    public final static int MAV_ODID_IDTYPE_CAA_ASSIGNED_ID = 2;
    /**
     * UTM (Unmanned Traffic Management) assigned ID (UUID RFC4122).
     */
    public final static int MAV_ODID_IDTYPE_UTM_ASSIGNED_ID = 3;
}
