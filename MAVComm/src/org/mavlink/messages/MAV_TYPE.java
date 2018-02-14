/**
 * Generated class : MAV_TYPE
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MAV_TYPE
 * 
 **/
public interface MAV_TYPE {
    /**
     * Generic micro air vehicle.
     */
    public final static int MAV_TYPE_GENERIC = 0;
    /**
     * Fixed wing aircraft.
     */
    public final static int MAV_TYPE_FIXED_WING = 1;
    /**
     * Quadrotor
     */
    public final static int MAV_TYPE_QUADROTOR = 2;
    /**
     * Coaxial helicopter
     */
    public final static int MAV_TYPE_COAXIAL = 3;
    /**
     * Normal helicopter with tail rotor.
     */
    public final static int MAV_TYPE_HELICOPTER = 4;
    /**
     * Ground installation
     */
    public final static int MAV_TYPE_ANTENNA_TRACKER = 5;
    /**
     * Operator control unit / ground control station
     */
    public final static int MAV_TYPE_GCS = 6;
    /**
     * Airship, controlled
     */
    public final static int MAV_TYPE_AIRSHIP = 7;
    /**
     * Free balloon, uncontrolled
     */
    public final static int MAV_TYPE_FREE_BALLOON = 8;
    /**
     * Rocket
     */
    public final static int MAV_TYPE_ROCKET = 9;
    /**
     * Ground rover
     */
    public final static int MAV_TYPE_GROUND_ROVER = 10;
    /**
     * Surface vessel, boat, ship
     */
    public final static int MAV_TYPE_SURFACE_BOAT = 11;
    /**
     * Submarine
     */
    public final static int MAV_TYPE_SUBMARINE = 12;
    /**
     * Hexarotor
     */
    public final static int MAV_TYPE_HEXAROTOR = 13;
    /**
     * Octorotor
     */
    public final static int MAV_TYPE_OCTOROTOR = 14;
    /**
     * Tricopter
     */
    public final static int MAV_TYPE_TRICOPTER = 15;
    /**
     * Flapping wing
     */
    public final static int MAV_TYPE_FLAPPING_WING = 16;
    /**
     * Kite
     */
    public final static int MAV_TYPE_KITE = 17;
    /**
     * Onboard companion controller
     */
    public final static int MAV_TYPE_ONBOARD_CONTROLLER = 18;
    /**
     * Two-rotor VTOL using control surfaces in vertical operation in addition. Tailsitter.
     */
    public final static int MAV_TYPE_VTOL_DUOROTOR = 19;
    /**
     * Quad-rotor VTOL using a V-shaped quad config in vertical operation. Tailsitter.
     */
    public final static int MAV_TYPE_VTOL_QUADROTOR = 20;
    /**
     * Tiltrotor VTOL
     */
    public final static int MAV_TYPE_VTOL_TILTROTOR = 21;
    /**
     * VTOL reserved 2
     */
    public final static int MAV_TYPE_VTOL_RESERVED2 = 22;
    /**
     * VTOL reserved 3
     */
    public final static int MAV_TYPE_VTOL_RESERVED3 = 23;
    /**
     * VTOL reserved 4
     */
    public final static int MAV_TYPE_VTOL_RESERVED4 = 24;
    /**
     * VTOL reserved 5
     */
    public final static int MAV_TYPE_VTOL_RESERVED5 = 25;
    /**
     * Onboard gimbal
     */
    public final static int MAV_TYPE_GIMBAL = 26;
    /**
     * Onboard ADSB peripheral
     */
    public final static int MAV_TYPE_ADSB = 27;
    /**
     * Steerable, nonrigid airfoil
     */
    public final static int MAV_TYPE_PARAFOIL = 28;
    /**
     * Dodecarotor
     */
    public final static int MAV_TYPE_DODECAROTOR = 29;
    /**
     * Camera
     */
    public final static int MAV_TYPE_CAMERA = 30;
}
