/**
 * Generated class : MAV_LANDED_STATE
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MAV_LANDED_STATE
 * Enumeration of landed detector states
 **/
public interface MAV_LANDED_STATE {
    /**
     * MAV landed state is unknown
     */
    public final static int MAV_LANDED_STATE_UNDEFINED = 0;
    /**
     * MAV is landed (on ground)
     */
    public final static int MAV_LANDED_STATE_ON_GROUND = 1;
    /**
     * MAV is in air
     */
    public final static int MAV_LANDED_STATE_IN_AIR = 2;
    /**
     * MAV currently taking off
     */
    public final static int MAV_LANDED_STATE_TAKEOFF = 3;
    /**
     * MAV currently landing
     */
    public final static int MAV_LANDED_STATE_LANDING = 4;
}
