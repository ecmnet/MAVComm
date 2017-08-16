/**
 * Generated class : GPS_FIX_TYPE
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface GPS_FIX_TYPE
 * Type of GPS fix
 **/
public interface GPS_FIX_TYPE {
    /**
     * No GPS connected
     */
    public final static int GPS_FIX_TYPE_NO_GPS = 0;
    /**
     * No position information, GPS is connected
     */
    public final static int GPS_FIX_TYPE_NO_FIX = 1;
    /**
     * 2D position
     */
    public final static int GPS_FIX_TYPE_2D_FIX = 2;
    /**
     * 3D position
     */
    public final static int GPS_FIX_TYPE_3D_FIX = 3;
    /**
     * DGPS/SBAS aided 3D position
     */
    public final static int GPS_FIX_TYPE_DGPS = 4;
    /**
     * RTK float, 3D position
     */
    public final static int GPS_FIX_TYPE_RTK_FLOAT = 5;
    /**
     * RTK Fixed, 3D position
     */
    public final static int GPS_FIX_TYPE_RTK_FIXED = 6;
    /**
     * Static fixed, typically used for base stations
     */
    public final static int GPS_FIX_TYPE_STATIC = 7;
    /**
     * PPP, 3D position.
     */
    public final static int GPS_FIX_TYPE_PPP = 8;
}
