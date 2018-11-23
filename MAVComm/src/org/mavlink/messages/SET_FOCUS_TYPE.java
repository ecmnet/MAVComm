/**
 * Generated class : SET_FOCUS_TYPE
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface SET_FOCUS_TYPE
 * Focus types for MAV_CMD_SET_CAMERA_FOCUS
 **/
public interface SET_FOCUS_TYPE {
    /**
     * Focus one step increment (-1 for focusing in, 1 for focusing out towards infinity).
     */
    public final static int FOCUS_TYPE_STEP = 0;
    /**
     * Continuous focus up/down until stopped (-1 for focusing in, 1 for focusing out towards infinity, 0 to stop focusing)
     */
    public final static int FOCUS_TYPE_CONTINUOUS = 1;
    /**
     * Zoom value as proportion of full camera range (a value between 0.0 and 100.0)
     */
    public final static int FOCUS_TYPE_RANGE = 2;
}
