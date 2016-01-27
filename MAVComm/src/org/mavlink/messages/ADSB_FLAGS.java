/**
 * Generated class : ADSB_FLAGS
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface ADSB_FLAGS
 * These flags indicate status such as data validity of each data source. Set = data valid
 **/
public interface ADSB_FLAGS {
    /**
     * null
     */
    public final static int ADSB_FLAGS_VALID_COORDS = 1;
    /**
     * null
     */
    public final static int ADSB_FLAGS_VALID_ALTITUDE = 2;
    /**
     * null
     */
    public final static int ADSB_FLAGS_VALID_HEADING = 4;
    /**
     * null
     */
    public final static int ADSB_FLAGS_VALID_VELOCITY = 8;
    /**
     * null
     */
    public final static int ADSB_FLAGS_VALID_CALLSIGN = 16;
    /**
     * null
     */
    public final static int ADSB_FLAGS_VALID_SQUAWK = 32;
    /**
     * null
     */
    public final static int ADSB_FLAGS_SIMULATED = 64;
}
