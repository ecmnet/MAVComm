/**
 * Generated class : MAV_COLLISION_THREAT_LEVEL
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MAV_COLLISION_THREAT_LEVEL
 * Aircraft-rated danger from this threat.
 **/
public interface MAV_COLLISION_THREAT_LEVEL {
    /**
     * Not a threat
     */
    public final static int MAV_COLLISION_THREAT_LEVEL_NONE = 0;
    /**
     * Craft is mildly concerned about this threat
     */
    public final static int MAV_COLLISION_THREAT_LEVEL_LOW = 1;
    /**
     * Craft is panicing, and may take actions to avoid threat
     */
    public final static int MAV_COLLISION_THREAT_LEVEL_HIGH = 2;
}
