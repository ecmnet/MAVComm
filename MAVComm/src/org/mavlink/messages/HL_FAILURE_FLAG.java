/**
 * Generated class : HL_FAILURE_FLAG
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface HL_FAILURE_FLAG
 * Flags to report failure cases over the high latency telemtry.
 **/
public interface HL_FAILURE_FLAG {
    /**
     * GPS failure.
     */
    public final static int HL_FAILURE_FLAG_GPS = 1;
    /**
     * Differential pressure sensor failure.
     */
    public final static int HL_FAILURE_FLAG_DIFFERENTIAL_PRESSURE = 2;
    /**
     * Absolute pressure sensor failure.
     */
    public final static int HL_FAILURE_FLAG_ABSOLUTE_PRESSURE = 4;
    /**
     * Accelerometer sensor failure.
     */
    public final static int HL_FAILURE_FLAG_3D_ACCEL = 8;
    /**
     * Gyroscope sensor failure.
     */
    public final static int HL_FAILURE_FLAG_3D_GYRO = 16;
    /**
     * Magnetometer sensor failure.
     */
    public final static int HL_FAILURE_FLAG_3D_MAG = 32;
    /**
     * Terrain subsystem failure.
     */
    public final static int HL_FAILURE_FLAG_TERRAIN = 64;
    /**
     * Battery failure/critical low battery.
     */
    public final static int HL_FAILURE_FLAG_BATTERY = 128;
    /**
     * RC receiver failure/no rc connection.
     */
    public final static int HL_FAILURE_FLAG_RC_RECEIVER = 256;
    /**
     * Offboard link failure.
     */
    public final static int HL_FAILURE_FLAG_OFFBOARD_LINK = 512;
    /**
     * Engine failure.
     */
    public final static int HL_FAILURE_FLAG_ENGINE = 1024;
    /**
     * Geofence violation.
     */
    public final static int HL_FAILURE_FLAG_GEOFENCE = 2048;
    /**
     * Estimator failure, for example measurement rejection or large variances.
     */
    public final static int HL_FAILURE_FLAG_ESTIMATOR = 4096;
    /**
     * Mission failure.
     */
    public final static int HL_FAILURE_FLAG_MISSION = 8192;
}
