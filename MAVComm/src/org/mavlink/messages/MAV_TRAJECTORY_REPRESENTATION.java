/**
 * Generated class : MAV_TRAJECTORY_REPRESENTATION
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MAV_TRAJECTORY_REPRESENTATION
 * WORK IN PROGRESS! DO NOT DEPLOY! Enumeration of possible waypoint/trajectory representation
 **/
public interface MAV_TRAJECTORY_REPRESENTATION {
    /**
     * Array of waypoints with the following order
     * PARAM 1 : X-coordinate of waypoint [m], set to NaN if not being used
     * PARAM 2 : Y-coordinate of waypoint [m], set to NaN if not being used
     * PARAM 3 : Z-coordinate of waypoint [m], set to NaN if not being used
     * PARAM 4 : X-velocity of waypoint [m/s], set to NaN if not being used
     * PARAM 5 : Y-velocity of waypoint [m/s], set to NaN if not being used
     * PARAM 6 : Z-velocity of waypoint [m/s], set to NaN if not being used
     * PARAM 7 : X-acceleration of waypoint [m/s/s], set to NaN if not being used
     * PARAM 8 : Y-acceleration of waypoint [m/s/s], set to NaN if not being used
     * PARAM 9 : Z-acceleration of waypoint [m/s/s], set to NaN if not being used
     * PARAM 10 : Yaw [rad], set to NaN for unchanged
     * PARAM 11 : Yaw-rate [rad/s], set to NaN for unchanged
     */
    public final static int MAV_TRAJECTORY_REPRESENTATION_WAYPOINTS = 0;
    /**
     * WORK IN PROGRESS! DO NOT DEPLOY! Array of bezier points with the following order
     * PARAM 1 : X-coordinate of starting bezier point [m], set to NaN if not being used
     * PARAM 2 : Y-coordinate of starting bezier point [m], set to NaN if not being used
     * PARAM 3 : Z-coordinate of starting bezier point [m], set to NaN if not being used
     * PARAM 4 : Bezier time horizon [s], set to NaN if velocity/acceleration should not be incorporated
     * PARAM 5 : Yaw [rad], set to NaN for unchanged
     */
    public final static int MAV_TRAJECTORY_REPRESENTATION_BEZIER = 1;
}
