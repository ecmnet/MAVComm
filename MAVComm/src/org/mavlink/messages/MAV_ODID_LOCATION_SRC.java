/**
 * Generated class : MAV_ODID_LOCATION_SRC
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MAV_ODID_LOCATION_SRC
 * 
 **/
public interface MAV_ODID_LOCATION_SRC {
    /**
     * The location of the operator is the same as the take-off location.
     */
    public final static int MAV_ODID_LOCATION_SRC_TAKEOFF = 0;
    /**
     * The location of the operator is based on live GNSS data.
     */
    public final static int MAV_ODID_LOCATION_SRC_LIVE_GNSS = 1;
    /**
     * The location of the operator is a fixed location.
     */
    public final static int MAV_ODID_LOCATION_SRC_FIXED = 2;
}
