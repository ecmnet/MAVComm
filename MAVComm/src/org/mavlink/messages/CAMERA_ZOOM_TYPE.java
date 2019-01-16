/**
 * Generated class : CAMERA_ZOOM_TYPE
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface CAMERA_ZOOM_TYPE
 * Zoom types for MAV_CMD_SET_CAMERA_ZOOM
 **/
public interface CAMERA_ZOOM_TYPE {
    /**
     * Zoom one step increment (-1 for wide, 1 for tele)
     */
    public final static int ZOOM_TYPE_STEP = 0;
    /**
     * Continuous zoom up/down until stopped (-1 for wide, 1 for tele, 0 to stop zooming)
     */
    public final static int ZOOM_TYPE_CONTINUOUS = 1;
    /**
     * Zoom value as proportion of full camera range (a value between 0.0 and 100.0)
     */
    public final static int ZOOM_TYPE_RANGE = 2;
}
