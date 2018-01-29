/**
 * Generated class : MAV_FAILSAFE_FLAG
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MAV_FAILSAFE_FLAG
 * These flags encode if a certain failsafe mode is triggered.
 **/
public interface MAV_FAILSAFE_FLAG {
    /**
     * 0b10000000 RC loss failsafe active.
     */
    public final static int MAV_FAILSAFE_FLAG_RC_LOSS = 128;
    /**
     * 0b01000000 remote control input is enabled.
     */
    public final static int MAV_FAILSAFE_FLAG_TELEM_LOSS = 64;
    /**
     * 0b00100000 Engine failure failsafe active.
     */
    public final static int MAV_FAILSAFE_FLAG_ENGINE = 32;
    /**
     * 0b00010000 GPS failure failsafe active.
     */
    public final static int MAV_FAILSAFE_FLAG_GPS = 16;
    /**
     * 0b00001000 Battery failure failsafe active.
     */
    public final static int MAV_FAILSAFE_FLAG_BATTERY = 8;
    /**
     * 0b00000100 Geofence failsafe active.
     */
    public final static int MAV_FAILSAFE_FLAG_GEOFENCE = 4;
    /**
     * 0b00000010 Reserved for future use.
     */
    public final static int MAV_FAILSAFE_FLAG_RESERVED1 = 2;
    /**
     * 0b00000001 Reserved for future use.
     */
    public final static int MAV_FAILSAFE_FLAG_RESERVED2 = 1;
}
