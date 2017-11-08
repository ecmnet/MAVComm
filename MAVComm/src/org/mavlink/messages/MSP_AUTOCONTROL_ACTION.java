/**
 * Generated class : MSP_AUTOCONTROL_ACTION
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MSP_AUTOCONTROL_ACTION
 * Auto-Control action (Bit 16-31)
 **/
public interface MSP_AUTOCONTROL_ACTION {
    /**
     * Circles at center with diameter (param3)
     */
    public final static int CIRCLE_MODE = 16;
    /**
     * Executes a list of waypoints defined in MSP (param3)
     */
    public final static int WAYPOINT_MODE = 17;
    /**
     * Execute a MSP mission (param3)
     */
    public final static int AUTO_MISSION = 18;
    /**
     * Execute a DEBUG1 sequence for testing
     */
    public final static int DEBUG_MODE1 = 19;
    /**
     * Execute a DEBUG2 sequence for testing
     */
    public final static int DEBUG_MODE2 = 20;
    /**
     * Saves a map corresponding to the global position
     */
    public final static int SAVE_MAP2D = 21;
    /**
     * Execute a single step in the current sequence for testing
     */
    public final static int STEP = 30;
    /**
     * Controls offboard updater
     */
    public final static int OFFBOARD_UPDATER = 31;
}
