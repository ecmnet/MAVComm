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
     * Set offboard position via MSP
     * PARAM 1 : LocalNED.X (set to NAN to ignore)
     * PARAM 2 : LocalNED.Y (set to NAN to ignore)
     * PARAM 3 : LocalNED.Z (set to NAN to ignore)
     */
    public final static int MSP_CMD_OFFBOARD_SETLOCALPOS = 71;
}
