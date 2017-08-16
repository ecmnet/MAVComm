/**
 * Generated class : CAMERA_CAP_FLAGS
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface CAMERA_CAP_FLAGS
 * Camera capability flags (Bitmap).
 **/
public interface CAMERA_CAP_FLAGS {
    /**
     * Camera is able to record video.
     */
    public final static int CAMERA_CAP_FLAGS_CAPTURE_VIDEO = 1;
    /**
     * Camera is able to capture images.
     */
    public final static int CAMERA_CAP_FLAGS_CAPTURE_IMAGE = 2;
    /**
     * Camera has separate Video and Image/Photo modes (MAV_CMD_SET_CAMERA_MODE)
     */
    public final static int CAMERA_CAP_FLAGS_HAS_MODES = 4;
    /**
     * Camera can capture images while in video mode
     */
    public final static int CAMERA_CAP_FLAGS_CAN_CAPTURE_IMAGE_IN_VIDEO_MODE = 8;
    /**
     * Camera can capture videos while in Photo/Image mode
     */
    public final static int CAMERA_CAP_FLAGS_CAN_CAPTURE_VIDEO_IN_IMAGE_MODE = 16;
    /**
     * Camera has image survey mode (MAV_CMD_SET_CAMERA_MODE)
     */
    public final static int CAMERA_CAP_FLAGS_HAS_IMAGE_SURVEY_MODE = 32;
}
