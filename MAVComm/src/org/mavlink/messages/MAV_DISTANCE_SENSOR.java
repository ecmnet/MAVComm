/**
 * Generated class : MAV_DISTANCE_SENSOR
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MAV_DISTANCE_SENSOR
 * Enumeration of distance sensor types
 **/
public interface MAV_DISTANCE_SENSOR {
    /**
     * Laser rangefinder, e.g. LightWare SF02/F or PulsedLight units
     */
    public final static int MAV_DISTANCE_SENSOR_LASER = 0;
    /**
     * Ultrasound rangefinder, e.g. MaxBotix units
     */
    public final static int MAV_DISTANCE_SENSOR_ULTRASOUND = 1;
    /**
     * Infrared rangefinder, e.g. Sharp units
     */
    public final static int MAV_DISTANCE_SENSOR_INFRARED = 2;
    /**
     * Radar type, e.g. uLanding units
     */
    public final static int MAV_DISTANCE_SENSOR_RADAR = 3;
    /**
     * Broken or unknown type, e.g. analog units
     */
    public final static int MAV_DISTANCE_SENSOR_UNKNOWN = 4;
}
