/**
 * Generated class : MAV_BATTERY_CHARGE_STATE
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MAV_BATTERY_CHARGE_STATE
 * Enumeration for states of low battery extent
 **/
public interface MAV_BATTERY_CHARGE_STATE {
    /**
     * Low battery state is not provided
     */
    public final static int MAV_BATTERY_CHARGE_STATE_UNDEFINED = 0;
    /**
     * Battery is not nearly empty, normal operation
     */
    public final static int MAV_BATTERY_CHARGE_STATE_OK = 1;
    /**
     * Battery state is low, warn and monitor close
     */
    public final static int MAV_BATTERY_CHARGE_STATE_LOW = 2;
    /**
     * Battry state is critical, return / abort immediately
     */
    public final static int MAV_BATTERY_CHARGE_STATE_CRITICAL = 3;
    /**
     * Battry state is too low for ordinary abortion, fastest possible emergency stop preventing damage
     */
    public final static int MAV_BATTERY_CHARGE_STATE_EMERGENCY = 4;
    /**
     * Battry failed, damage unavoidable
     */
    public final static int MAV_BATTERY_CHARGE_STATE_FAILED = 5;
    /**
     * Battry is diagnosed to be broken or an error occurred, usage is discouraged / prohibited
     */
    public final static int MAV_BATTERY_CHARGE_STATE_UNHEALTHY = 6;
}
