/**
 * Generated class : MSP_AUTOCONTROL_MODE
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MSP_AUTOCONTROL_MODE
 * Auto-Control modes (Bit 0-15)
 **/
public interface MSP_AUTOCONTROL_MODE {
    /**
     * Abort request: Terminates any autopilot action and returns to POSHOLD
     */
    public final static int ABORT = 0;
    /**
     * JumpBack mode
     */
    public final static int OBSTACLE_STOP = 1;
    /**
     * JumpBack mode
     */
    public final static int OBSTACLE_AVOIDANCE = 2;
    /**
     * Interactive mode (by mouse)
     */
    public final static int INTERACTIVE = 9;
}
