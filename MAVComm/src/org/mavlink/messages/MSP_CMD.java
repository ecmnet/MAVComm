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
     * Enable/Disable offboard control
     * PARAM 1 : enable/disable offboard
     */
    public final static int MSP_CMD_OFFBOARD = 70;
    /**
     * Enable/Disable vision control
     * PARAM 1 : enable/disable vision
     */
    public final static int MSP_CMD_VISION = 71;
    /**
     * Enable/Disable combinedFileStream recording
     * PARAM 1 : enable/disable Stream recording
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
}
