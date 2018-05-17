/**
 * Generated class : MAV_BATTERY_CHARGE_STATE
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MAV_BATTERY_CHARGE_STATE
 * Enumeration for low battery states.
 **/
public interface MAV_BATTERY_CHARGE_STATE {
    /**
     * Low battery state is not provided
     */
    public final static int MAV_BATTERY_CHARGE_STATE_UNDEFINED = 0;
    /**
     * Battery is not in low state. Normal operation.
     */
    public final static int MAV_BATTERY_CHARGE_STATE_OK = 1;
    /**
     * Battery state is low, warn and monitor close.
     */
    public final static int MAV_BATTERY_CHARGE_STATE_LOW = 2;
    /**
     * Battery state is critical, return or abort immediately.
     */
    public final static int MAV_BATTERY_CHARGE_STATE_CRITICAL = 3;
    /**
     * Battery state is too low for ordinary abort sequence. Perform fastest possible emergency stop to prevent damage.
     */
    public final static int MAV_BATTERY_CHARGE_STATE_EMERGENCY = 4;
    /**
     * Battery failed, damage unavoidable.
     */
    public final static int MAV_BATTERY_CHARGE_STATE_FAILED = 5;
    /**
     * Battery is diagnosed to be defective or an error occurred, usage is discouraged / prohibited.
     */
    public final static int MAV_BATTERY_CHARGE_STATE_UNHEALTHY = 6;
}
