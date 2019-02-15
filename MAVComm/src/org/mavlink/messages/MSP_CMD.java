/**
 * Generated class : MSP_CMD
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MSP_CMD
 * MSP Companion commands
 **/
public interface MSP_CMD {
    /**
     * Reboot the MSP companion
     */
    public final static int MSP_CMD_RESTART = 99;
    /**
     * Control offboard updater
     * PARAM 1 : component command
     */
    public final static int MSP_CMD_OFFBOARD = 70;
    /**
     * EControl vision system
     * PARAM 1 : component command
     */
    public final static int MSP_CMD_VISION = 71;
    /**
     * Control filestream updater
     * PARAM 1 : component command
     */
    public final static int MSP_CMD_COMBINEDFILESTREAM = 72;
    /**
     * Set offboard position via MSP
     * PARAM 1 : LocalNED.X (set to NAN to ignore)
     * PARAM 2 : LocalNED.Y (set to NAN to ignore)
     * PARAM 3 : LocalNED.Z (set to NAN to ignore)
     * PARAM 4 : Yaw (deg)  (set to NAN to ignore)
     */
    public final static int MSP_CMD_OFFBOARD_SETLOCALPOS = 73;
    /**
     * Invalidate MICROSLAM transfer
     */
    public final static int MSP_TRANSFER_MICROSLAM = 74;
    /**
     * Control microslam system
     * PARAM 1 : component command
     */
    public final static int MSP_CMD_MICROSLAM = 75;
    /**
     * Set Autopilot mode
     * PARAM 1 : component command
     * PARAM 2 : autopilot mode
     * PARAM 3 : mode parameter
     */
    public final static int MSP_CMD_AUTOMODE = 76;
    /**
     * Sets home position and enables GPOS
     * PARAM 1 : latitude
     * PARAM 2 : longitude
     * PARAM 3 : altitude
     */
    public final static int MSP_CMD_SET_HOMEPOS = 77;
}
