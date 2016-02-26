/**
 * Generated class : MAV_PROTOCOL_CAPABILITY
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MAV_PROTOCOL_CAPABILITY
 * Bitmask of (optional) autopilot capabilities (64 bit). If a bit is set, the autopilot supports this capability.
 **/
public interface MAV_PROTOCOL_CAPABILITY {
    /**
     * Autopilot supports MISSION float message type.
     */
    public final static int MAV_PROTOCOL_CAPABILITY_MISSION_FLOAT = 1;
    /**
     * Autopilot supports the new param float message type.
     */
    public final static int MAV_PROTOCOL_CAPABILITY_PARAM_FLOAT = 2;
    /**
     * Autopilot supports MISSION_INT scaled integer message type.
     */
    public final static int MAV_PROTOCOL_CAPABILITY_MISSION_INT = 4;
    /**
     * Autopilot supports COMMAND_INT scaled integer message type.
     */
    public final static int MAV_PROTOCOL_CAPABILITY_COMMAND_INT = 8;
    /**
     * Autopilot supports the new param union message type.
     */
    public final static int MAV_PROTOCOL_CAPABILITY_PARAM_UNION = 16;
    /**
     * Autopilot supports the new FILE_TRANSFER_PROTOCOL message type.
     */
    public final static int MAV_PROTOCOL_CAPABILITY_FTP = 32;
    /**
     * Autopilot supports commanding attitude offboard.
     */
    public final static int MAV_PROTOCOL_CAPABILITY_SET_ATTITUDE_TARGET = 64;
    /**
     * Autopilot supports commanding position and velocity targets in local NED frame.
     */
    public final static int MAV_PROTOCOL_CAPABILITY_SET_POSITION_TARGET_LOCAL_NED = 128;
    /**
     * Autopilot supports commanding position and velocity targets in global scaled integers.
     */
    public final static int MAV_PROTOCOL_CAPABILITY_SET_POSITION_TARGET_GLOBAL_INT = 256;
    /**
     * Autopilot supports terrain protocol / data handling.
     */
    public final static int MAV_PROTOCOL_CAPABILITY_TERRAIN = 512;
    /**
     * Autopilot supports direct actuator control.
     */
    public final static int MAV_PROTOCOL_CAPABILITY_SET_ACTUATOR_TARGET = 1024;
    /**
     * Autopilot supports the flight termination command.
     */
    public final static int MAV_PROTOCOL_CAPABILITY_FLIGHT_TERMINATION = 2048;
    /**
     * Autopilot supports onboard compass calibration.
     */
    public final static int MAV_PROTOCOL_CAPABILITY_COMPASS_CALIBRATION = 4096;
}
