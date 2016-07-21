/**
 * Generated class : GPS_INPUT_IGNORE_FLAGS
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface GPS_INPUT_IGNORE_FLAGS
 * 
 **/
public interface GPS_INPUT_IGNORE_FLAGS {
    /**
     * ignore altitude field
     */
    public final static int GPS_INPUT_IGNORE_FLAG_ALT = 1;
    /**
     * ignore hdop field
     */
    public final static int GPS_INPUT_IGNORE_FLAG_HDOP = 2;
    /**
     * ignore vdop field
     */
    public final static int GPS_INPUT_IGNORE_FLAG_VDOP = 4;
    /**
     * ignore horizontal velocity field (vn and ve)
     */
    public final static int GPS_INPUT_IGNORE_FLAG_VEL_HORIZ = 8;
    /**
     * ignore vertical velocity field (vd)
     */
    public final static int GPS_INPUT_IGNORE_FLAG_VEL_VERT = 16;
    /**
     * ignore speed accuracy field
     */
    public final static int GPS_INPUT_IGNORE_FLAG_SPEED_ACCURACY = 32;
    /**
     * ignore horizontal accuracy field
     */
    public final static int GPS_INPUT_IGNORE_FLAG_HORIZONTAL_ACCURACY = 64;
    /**
     * ignore vertical accuracy field
     */
    public final static int GPS_INPUT_IGNORE_FLAG_VERTICAL_ACCURACY = 128;
}
