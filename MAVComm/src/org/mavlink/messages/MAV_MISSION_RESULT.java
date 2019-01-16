/**
 * Generated class : MAV_MISSION_RESULT
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MAV_MISSION_RESULT
 * Result of mission operation (in a MISSION_ACK message).
 **/
public interface MAV_MISSION_RESULT {
    /**
     * mission accepted OK
     */
    public final static int MAV_MISSION_ACCEPTED = 0;
    /**
     * Generic error / not accepting mission commands at all right now.
     */
    public final static int MAV_MISSION_ERROR = 1;
    /**
     * Coordinate frame is not supported.
     */
    public final static int MAV_MISSION_UNSUPPORTED_FRAME = 2;
    /**
     * Command is not supported.
     */
    public final static int MAV_MISSION_UNSUPPORTED = 3;
    /**
     * Mission item exceeds storage space.
     */
    public final static int MAV_MISSION_NO_SPACE = 4;
    /**
     * One of the parameters has an invalid value.
     */
    public final static int MAV_MISSION_INVALID = 5;
    /**
     * param1 has an invalid value.
     */
    public final static int MAV_MISSION_INVALID_PARAM1 = 6;
    /**
     * param2 has an invalid value.
     */
    public final static int MAV_MISSION_INVALID_PARAM2 = 7;
    /**
     * param3 has an invalid value.
     */
    public final static int MAV_MISSION_INVALID_PARAM3 = 8;
    /**
     * param4 has an invalid value.
     */
    public final static int MAV_MISSION_INVALID_PARAM4 = 9;
    /**
     * x / param5 has an invalid value.
     */
    public final static int MAV_MISSION_INVALID_PARAM5_X = 10;
    /**
     * y / param6 has an invalid value.
     */
    public final static int MAV_MISSION_INVALID_PARAM6_Y = 11;
    /**
     * z / param7 has an invalid value.
     */
    public final static int MAV_MISSION_INVALID_PARAM7 = 12;
    /**
     * Mission item received out of sequence
     */
    public final static int MAV_MISSION_INVALID_SEQUENCE = 13;
    /**
     * Not accepting any mission commands from this communication partner.
     */
    public final static int MAV_MISSION_DENIED = 14;
    /**
     * Current mission operation cancelled (e.g. mission upload, mission download).
     */
    public final static int MAV_MISSION_OPERATION_CANCELLED = 15;
}
