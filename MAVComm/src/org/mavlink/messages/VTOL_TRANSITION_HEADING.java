/**
 * Generated class : VTOL_TRANSITION_HEADING
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface VTOL_TRANSITION_HEADING
 * Direction of VTOL transition
 **/
public interface VTOL_TRANSITION_HEADING {
    /**
     * Respect the heading configuration of the vehicle.
     */
    public final static int VTOL_TRANSITION_HEADING_VEHICLE_DEFAULT = 0;
    /**
     * Use the heading pointing towards the next waypoint.
     */
    public final static int VTOL_TRANSITION_HEADING_NEXT_WAYPOINT = 1;
    /**
     * Use the heading on takeoff (while sitting on the ground).
     */
    public final static int VTOL_TRANSITION_HEADING_TAKEOFF = 2;
    /**
     * Use the specified heading in parameter 4.
     */
    public final static int VTOL_TRANSITION_HEADING_SPECIFIED = 3;
    /**
     * Use the current heading when reaching takeoff altitude (potentially facing the wind when weather-vaning is active).
     */
    public final static int VTOL_TRANSITION_HEADING_ANY = 4;
}
