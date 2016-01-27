/**
 * Generated class : ADSB_ALTITUDE_TYPE
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface ADSB_ALTITUDE_TYPE
 * Enumeration of the ADSB altimeter types
 **/
public interface ADSB_ALTITUDE_TYPE {
    /**
     * Altitude reported from a Baro source using QNH reference
     */
    public final static int ADSB_ALTITUDE_TYPE_PRESSURE_QNH = 0;
    /**
     * Altitude reported from a GNSS source
     */
    public final static int ADSB_ALTITUDE_TYPE_GEOMETRIC = 1;
}
