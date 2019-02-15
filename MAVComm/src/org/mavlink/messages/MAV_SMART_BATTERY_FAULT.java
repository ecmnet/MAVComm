/**
 * Generated class : MAV_SMART_BATTERY_FAULT
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MAV_SMART_BATTERY_FAULT
 * Smart battery supply status/fault flags (bitmask) for health indication.
 **/
public interface MAV_SMART_BATTERY_FAULT {
    /**
     * Battery has deep discharged.
     */
    public final static int MAV_SMART_BATTERY_FAULT_DEEP_DISCHARGE = 1;
    /**
     * Voltage spikes.
     */
    public final static int MAV_SMART_BATTERY_FAULT_SPIKES = 2;
    /**
     * Single cell has failed.
     */
    public final static int MAV_SMART_BATTERY_FAULT_SINGLE_CELL_FAIL = 4;
    /**
     * Over-current fault.
     */
    public final static int MAV_SMART_BATTERY_FAULT_OVER_CURRENT = 8;
    /**
     * Over-temperature fault.
     */
    public final static int MAV_SMART_BATTERY_FAULT_OVER_TEMPERATURE = 16;
    /**
     * Under-temperature fault.
     */
    public final static int MAV_SMART_BATTERY_FAULT_UNDER_TEMPERATURE = 32;
}
