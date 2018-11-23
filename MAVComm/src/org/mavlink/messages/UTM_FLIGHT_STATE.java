/**
 * Generated class : UTM_FLIGHT_STATE
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface UTM_FLIGHT_STATE
 * Airborne status of UAS.
 **/
public interface UTM_FLIGHT_STATE {
    /**
     * The flight state can't be determined.
     */
    public final static int UTM_FLIGHT_STATE_UNKNOWN = 1;
    /**
     * UAS on ground.
     */
    public final static int UTM_FLIGHT_STATE_GROUND = 2;
    /**
     * UAS airborne.
     */
    public final static int UTM_FLIGHT_STATE_AIRBORNE = 3;
    /**
     * UAS is in an emergency flight state.
     */
    public final static int UTM_FLIGHT_STATE_EMERGENCY = 16;
    /**
     * UAS has no active controls.
     */
    public final static int UTM_FLIGHT_STATE_NOCTRL = 32;
}
