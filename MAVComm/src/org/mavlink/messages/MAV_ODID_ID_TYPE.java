/**
 * Generated class : MAV_ODID_ID_TYPE
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MAV_ODID_ID_TYPE
 * 
 **/
public interface MAV_ODID_ID_TYPE {
    /**
     * No type defined.
     */
    public final static int MAV_ODID_ID_TYPE_NONE = 0;
    /**
     * Manufacturer Serial Number (ANSI/CTA-2063 format).
     */
    public final static int MAV_ODID_ID_TYPE_SERIAL_NUMBER = 1;
    /**
     * CAA (Civil Aviation Authority) registered ID. Format: [ICAO Country Code].[CAA Assigned ID].
     */
    public final static int MAV_ODID_ID_TYPE_CAA_REGISTRATION_ID = 2;
    /**
     * UTM (Unmanned Traffic Management) assigned UUID (RFC4122).
     */
    public final static int MAV_ODID_ID_TYPE_UTM_ASSIGNED_UUID = 3;
}
