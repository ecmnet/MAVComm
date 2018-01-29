/**
 * Generated class : FLIGHT_MODE
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface FLIGHT_MODE
 * Flight modes of the MAV.
 **/
public interface FLIGHT_MODE {
    /**
     * Manual flight mode
     */
    public final static int FLIGHT_MODE_MANUAL = 0;
    /**
     * Altitude hold flight mode
     */
    public final static int FLIGHT_MODE_ALTCTL = 1;
    /**
     * Position control flight mode
     */
    public final static int FLIGHT_MODE_POSCTL = 2;
    /**
     * Stabilized mode
     */
    public final static int FLIGHT_MODE_STABILIZED = 3;
    /**
     * Acro mode
     */
    public final static int FLIGHT_MODE_ACRO = 4;
    /**
     * Rattitude mode
     */
    public final static int FLIGHT_MODE_RATTITIDE = 5;
    /**
     * Mission mode
     */
    public final static int FLIGHT_MODE_AUTO_MISSION = 6;
    /**
     * Loiter mode
     */
    public final static int FLIGHT_MODE_AUTO_LOITER = 7;
    /**
     * Return to land mode
     */
    public final static int FLIGHT_MODE_AUTO_RTL = 8;
    /**
     * RC recover mode
     */
    public final static int FLIGHT_MODE_AUTO_RCRECOVER = 9;
    /**
     * Return to ground station mode
     */
    public final static int FLIGHT_MODE_AUTO_RTGS = 10;
    /**
     * Land Engine Fail mode
     */
    public final static int FLIGHT_MODE_AUTO_LANDENGFAIL = 11;
    /**
     * Land GPS fail mode
     */
    public final static int FLIGHT_MODE_AUTO_LANDGPSFAIL = 12;
    /**
     * Takeoff mode
     */
    public final static int FLIGHT_MODE_AUTO_TAKEOFF = 13;
    /**
     * Land mode
     */
    public final static int FLIGHT_MODE_AUTO_LAND = 14;
    /**
     * Follow target/me mode
     */
    public final static int FLIGHT_MODE_AUTO_FOLLOW_TARGET = 15;
    /**
     * Precision landing mode
     */
    public final static int FLIGHT_MODE_AUTO_PRECLAND = 16;
    /**
     * Descend mode
     */
    public final static int FLIGHT_MODE_DESCEND = 17;
    /**
     * Offboard mode
     */
    public final static int FLIGHT_MODE_OFFBOARD = 18;
    /**
     * Termination mode
     */
    public final static int FLIGHT_MODE_TERMINATION = 19;
    /**
     * Auto tune mode
     */
    public final static int FLIGHT_MODE_AUTO_TUNE = 20;
    /**
     * Brake mode
     */
    public final static int FLIGHT_MODE_BRAKE = 21;
    /**
     * Circle mode
     */
    public final static int FLIGHT_MODE_CIRCLE = 22;
    /**
     * Drift mode
     */
    public final static int FLIGHT_MODE_DRIFT = 23;
    /**
     * Guided mode
     */
    public final static int FLIGHT_MODE_GUIDED = 24;
    /**
     * Sport mode
     */
    public final static int FLIGHT_MODE_SPORT = 25;
    /**
     * Throw mode
     */
    public final static int FLIGHT_MODE_THROW = 26;
    /**
     * Simplified multicopter control mode
     */
    public final static int FLIGHT_MODE_SUPER_SIMPLE = 27;
    /**
     * Avoid traffic mode
     */
    public final static int FLIGHT_MODE_AVIOD_TRAFFIC = 28;
    /**
     * Fly by wire (version A) mode
     */
    public final static int FLIGHT_MODE_FBWA = 29;
    /**
     * Flz by wire (version B) mode
     */
    public final static int FLIGHT_MODE_FBWB = 30;
    /**
     * Position control flight mode
     */
    public final static int FLIGHT_MODE_TRAINING = 31;
    /**
     * Cruise mode
     */
    public final static int FLIGHT_MODE_CRUISE = 32;
    /**
     * Position hold mode
     */
    public final static int FLIGHT_MODE_HOLD = 33;
    /**
     * Steering mode
     */
    public final static int FLIGHT_MODE_STEERING = 34;
    /**
     * Smart return-to-land-mode
     */
    public final static int FLIGHT_MODE_SMART_RTL = 35;
}
