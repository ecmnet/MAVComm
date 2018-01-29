/**
 * Generated class : MAV_FAILURE_FLAG
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MAV_FAILURE_FLAG
 * These flags encode if a certain failsafe mode is triggered.
 **/
public interface MAV_FAILURE_FLAG {
    /**
     * GPS failure.
     */
    public final static int MAV_FAILURE_FLAG_GPS = 1;
    /**
     * Airspeed failure.
     */
    public final static int MAV_FAILURE_FLAG_AIRSPEED = 2;
    /**
     * Barometer failure.
     */
    public final static int MAV_FAILURE_FLAG_BAROMETER = 4;
    /**
     * Accelerometer failure.
     */
    public final static int MAV_FAILURE_FLAG_ACCELEROMETER = 8;
    /**
     * Gyroscope failure.
     */
    public final static int MAV_FAILURE_FLAG_GYROSCOPE = 16;
    /**
     * Magnetometer.
     */
    public final static int MAV_FAILURE_FLAG_MAGNETOMETER = 32;
    /**
     * Mission failure.
     */
    public final static int MAV_FAILURE_FLAG_MISSION = 64;
    /**
     * Estimator failure.
     */
    public final static int MAV_FAILURE_FLAG_ESTIMATOR = 128;
    /**
     * Lidar failure.
     */
    public final static int MAV_FAILURE_FLAG_LIDAR = 256;
    /**
     * Offboard link failure.
     */
    public final static int MAV_FAILURE_FLAG_OFFBOARD_LINK = 512;
    /**
     * Reserved for future use.
     */
    public final static int MAV_FAILURE_FLAG_RESERVED1 = 1024;
    /**
     * Reserved for future use.
     */
    public final static int MAV_FAILURE_FLAG_RESERVED2 = 2048;
    /**
     * Reserved for future use.
     */
    public final static int MAV_FAILURE_FLAG_RESERVED3 = 4096;
    /**
     * Reserved for future use.
     */
    public final static int MAV_FAILURE_FLAG_RESERVED4 = 8192;
    /**
     * Reserved for future use.
     */
    public final static int MAV_FAILURE_FLAG_RESERVED5 = 16384;
    /**
     * Reserved for future use.
     */
    public final static int MAV_FAILURE_FLAG_RESERVED6 = 32768;
}
