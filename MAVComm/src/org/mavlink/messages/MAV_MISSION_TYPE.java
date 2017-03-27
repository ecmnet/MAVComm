/**
 * Generated class : MAV_MISSION_TYPE
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MAV_MISSION_TYPE
 * Type of mission items being requested/sent in mission protocol.
 **/
public interface MAV_MISSION_TYPE {
    /**
     * Items are mission commands for main mission.
     */
    public final static int MAV_MISSION_TYPE_MISSION = 0;
    /**
     * Specifies GeoFence area(s). Items are MAV_CMD_FENCE_ GeoFence items.
     */
    public final static int MAV_MISSION_TYPE_FENCE = 1;
    /**
     * Specifies the rally points for the vehicle. Rally points are alternative RTL points. Items are MAV_CMD_RALLY_POINT rally point items.
     */
    public final static int MAV_MISSION_TYPE_RALLY = 2;
    /**
     * Only used in MISSION_CLEAR_ALL to clear all mission types.
     */
    public final static int MAV_MISSION_TYPE_ALL = 255;
}
