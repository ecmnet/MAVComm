/**
 * Generated class : MAV_VTOL_STATE
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MAV_VTOL_STATE
 * Enumeration of VTOL states
 **/
public interface MAV_VTOL_STATE {
    /**
     * MAV is not configured as VTOL
     */
    public final static int MAV_VTOL_STATE_UNDEFINED = 0;
    /**
     * VTOL is in transition from multicopter to fixed-wing
     */
    public final static int MAV_VTOL_STATE_TRANSITION_TO_FW = 1;
    /**
     * VTOL is in transition from fixed-wing to multicopter
     */
    public final static int MAV_VTOL_STATE_TRANSITION_TO_MC = 2;
    /**
     * VTOL is in multicopter state
     */
    public final static int MAV_VTOL_STATE_MC = 3;
    /**
     * VTOL is in fixed-wing state
     */
    public final static int MAV_VTOL_STATE_FW = 4;
}
