/**
 * Generated class : MAV_FIELD
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MAV_FIELD
 * This describes the type of a field in an array, for example usage (see ESTIMATOR_STATE)
 **/
public interface MAV_FIELD {
    /**
     * Unused state.
     */
    public final static int MAV_FIELD_UNUSED = 0;
    /**
     * Position North, m
     */
    public final static int MAV_FIELD_POS_N = 1;
    /**
     * Position East, m
     */
    public final static int MAV_FIELD_POS_E = 2;
    /**
     * Position Down, m
     */
    public final static int MAV_FIELD_POS_D = 3;
    /**
     * Altitude above sea level, m
     */
    public final static int MAV_FIELD_ASL = 4;
    /**
     * Altitude above ground level, m
     */
    public final static int MAV_FIELD_AGL = 5;
    /**
     * Velocity North, m/s
     */
    public final static int MAV_FIELD_VEL_N = 6;
    /**
     * Velocity East, m/s
     */
    public final static int MAV_FIELD_VEL_E = 7;
    /**
     * Velocity Down, m/s
     */
    public final static int MAV_FIELD_VEL_D = 8;
    /**
     * Velocity body x (forward), m/s
     */
    public final static int MAV_FIELD_VEL_X = 9;
    /**
     * Velocity body y (right), m/s
     */
    public final static int MAV_FIELD_VEL_Y = 10;
    /**
     * Velocity body z (down), m/s
     */
    public final static int MAV_FIELD_VEL_Z = 11;
    /**
     * Acceleration North, m/s
     */
    public final static int MAV_FIELD_ACC_N = 12;
    /**
     * Acceleration East, m/s
     */
    public final static int MAV_FIELD_ACC_E = 13;
    /**
     * Acceleration Down, m/s
     */
    public final static int MAV_FIELD_ACC_D = 14;
    /**
     * Acceleration body x (forward), m/s
     */
    public final static int MAV_FIELD_ACC_X = 15;
    /**
     * Acceleration body y (right), m/s
     */
    public final static int MAV_FIELD_ACC_Y = 16;
    /**
     * Acceleration body z (down), m/s
     */
    public final static int MAV_FIELD_ACC_Z = 17;
    /**
     * Quaternion from nav to body scalar component
     */
    public final static int MAV_FIELD_Q0 = 18;
    /**
     * Quaternion from nav to body vector[0] component
     */
    public final static int MAV_FIELD_Q1 = 19;
    /**
     * Quaternion from nav to body vector[1] component
     */
    public final static int MAV_FIELD_Q2 = 20;
    /**
     * Quaternion from nav to body vector[2] component
     */
    public final static int MAV_FIELD_Q3 = 21;
    /**
     * Euler Body 3-2-1 (Tait-Bryan) roll angle, radians
     */
    public final static int MAV_FIELD_ROLL = 22;
    /**
     * Euler Body 3-2-1 (Tait-Bryan) pitch angle, radians
     */
    public final static int MAV_FIELD_PITCH = 23;
    /**
     * Euler Body 3-2-1 (Tait-Bryan) yaw angle, radians
     */
    public final static int MAV_FIELD_YAW = 24;
    /**
     * Euler Body x rate, radians/second
     */
    public final static int MAV_FIELD_ANGVEL_X = 25;
    /**
     * Euler Body y rate, radians/second
     */
    public final static int MAV_FIELD_ANGVEL_Y = 26;
    /**
     * Euler Body z rate, radians/second
     */
    public final static int MAV_FIELD_ANGVEL_Z = 27;
    /**
     * Euler Body 3-2-1 (Tait-Bryan) roll angle rate, radians/second
     */
    public final static int MAV_FIELD_ROLLRATE = 28;
    /**
     * Euler Body 3-2-1 (Tait-Bryan) pitch angle rate, radians/second
     */
    public final static int MAV_FIELD_PITCHRATE = 29;
    /**
     * Euler Body 3-2-1 (Tait-Bryan) yaw angle rate, radians/second
     */
    public final static int MAV_FIELD_YAWRATE = 30;
    /**
     * Latitude, radians
     */
    public final static int MAV_FIELD_LAT = 31;
    /**
     * Longitude, radians
     */
    public final static int MAV_FIELD_LON = 32;
    /**
     * North bias (normalized)
     */
    public final static int MAV_FIELD_BIAS_N = 33;
    /**
     * East  bias (normalized)
     */
    public final static int MAV_FIELD_BIAS_E = 34;
    /**
     * Down z bias (normalized)
     */
    public final static int MAV_FIELD_BIAS_D = 35;
    /**
     * Body x bias (normalized)
     */
    public final static int MAV_FIELD_BIAS_X = 36;
    /**
     * Body y bias (normalized)
     */
    public final static int MAV_FIELD_BIAS_Y = 37;
    /**
     * Body z bias (normalized)
     */
    public final static int MAV_FIELD_BIAS_Z = 38;
    /**
     * Altitude of terrain above sea level, m
     */
    public final static int MAV_FIELD_TERRAIN_ASL = 39;
    /**
     * Airspeed, m/s
     */
    public final static int MAV_FIELD_AIRSPEED = 40;
    /**
     * Optical flow in body x, radians
     */
    public final static int MAV_FIELD_FLOW_X = 41;
    /**
     * Optical flow in body y, radians
     */
    public final static int MAV_FIELD_FLOW_Y = 42;
    /**
     * Magnetic field, body x, Gauss
     */
    public final static int MAV_FIELD_MAG_X = 43;
    /**
     * Magnetic field, body y, Gauss
     */
    public final static int MAV_FIELD_MAG_Y = 44;
    /**
     * Magnetic field, body z, Gauss
     */
    public final static int MAV_FIELD_MAG_Z = 45;
    /**
     * Magnetic heading, radians
     */
    public final static int MAV_FIELD_MAG_HDG = 46;
    /**
     * Distance bottom, m
     */
    public final static int MAV_FIELD_DIST_BOTTOM = 47;
    /**
     * Distance top, m
     */
    public final static int MAV_FIELD_DIST_TOP = 48;
    /**
     * Distance front, m
     */
    public final static int MAV_FIELD_DIST_FRONT = 49;
    /**
     * Distance back, m
     */
    public final static int MAV_FIELD_DIST_BACK = 50;
    /**
     * Distance left, m
     */
    public final static int MAV_FIELD_DIST_LEFT = 51;
    /**
     * Distance right, m
     */
    public final static int MAV_FIELD_DIST_RIGHT = 52;
}
