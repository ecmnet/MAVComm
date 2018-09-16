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
     * Returns to local home position and lands device
     */
    public final static int RTL = 16;
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
     * Load a map corresponding to the global position
     */
    public final static int LOAD_MAP2D = 22;
    /**
     * Applies a map filter to the current map
     */
    public final static int APPLY_MAP_FILTER = 30;
    /**
     * Controls offboard updater
     */
    public final static int OFFBOARD_UPDATER = 31;
}
