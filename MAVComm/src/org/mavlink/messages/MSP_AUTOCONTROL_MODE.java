/**
 * Generated class : MSP_AUTOCONTROL_MODE
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MSP_AUTOCONTROL_MODE
 * Auto-Control mode
 **/
public interface MSP_AUTOCONTROL_MODE {
    /**
     * Abort request: Terminates any autopilot action and returns to POSHOLD
     */
    public final static int ABORT = 0;
    /**
     * JumpBack mode
     */
    public final static int JUMPBACK = 1;
    /**
     * Circles at center with diameter (param3)
     */
    public final static int CIRCLE_MODE = 2;
    /**
     * Executes a list of waypoints defined in MSP (param3)
     */
    public final static int WAYPOINT_MODE = 3;
    /**
     * Execute a MSP mission (param3)
     */
    public final static int AUTO_MISSION = 4;
}
