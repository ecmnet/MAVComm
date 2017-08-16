/**
 * Generated class : CAMERA_MODE
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface CAMERA_MODE
 * Camera Modes.
 **/
public interface CAMERA_MODE {
    /**
     * Camera is in image/photo capture mode.
     */
    public final static int CAMERA_MODE_IMAGE = 0;
    /**
     * Camera is in video capture mode.
     */
    public final static int CAMERA_MODE_VIDEO = 1;
    /**
     * Camera is in image survey capture mode. It allows for camera controller to do specific settings for surveys.
     */
    public final static int CAMERA_MODE_IMAGE_SURVEY = 2;
}
