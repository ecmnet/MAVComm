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
}
