/**
 * Generated class : MAV_BATTERY_FUNCTION
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MAV_BATTERY_FUNCTION
 * Enumeration of battery functions
 **/
public interface MAV_BATTERY_FUNCTION {
    /**
     * Battery function is unknown
     */
    public final static int MAV_BATTERY_FUNCTION_UNKNOWN = 0;
    /**
     * Battery supports all flight systems
     */
    public final static int MAV_BATTERY_FUNCTION_ALL = 1;
    /**
     * Battery for the propulsion system
     */
    public final static int MAV_BATTERY_FUNCTION_PROPULSION = 2;
    /**
     * Avionics battery
     */
    public final static int MAV_BATTERY_FUNCTION_AVIONICS = 3;
    /**
     * Payload battery
     */
    public final static int MAV_BATTERY_TYPE_PAYLOAD = 4;
}
