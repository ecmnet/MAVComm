/**
 * Generated class : MAV_COLLISION_ACTION
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MAV_COLLISION_ACTION
 * Possible actions an aircraft can take to avoid a collision.
 **/
public interface MAV_COLLISION_ACTION {
    /**
     * Ignore any potential collisions
     */
    public final static int MAV_COLLISION_ACTION_NONE = 0;
    /**
     * Report potential collision
     */
    public final static int MAV_COLLISION_ACTION_REPORT = 1;
    /**
     * Ascend or Descend to avoid thread
     */
    public final static int MAV_COLLISION_ACTION_ASCEND_OR_DESCEND = 2;
    /**
     * Ascend or Descend to avoid thread
     */
    public final static int MAV_COLLISION_ACTION_MOVE_HORIZONTALLY = 3;
    /**
     * Aircraft to move perpendicular to the collision's velocity vector
     */
    public final static int MAV_COLLISION_ACTION_MOVE_PERPENDICULAR = 4;
    /**
     * Aircraft to fly directly back to its launch point
     */
    public final static int MAV_COLLISION_ACTION_RTL = 5;
    /**
     * Aircraft to stop in place
     */
    public final static int MAV_COLLISION_ACTION_HOVER = 6;
}
