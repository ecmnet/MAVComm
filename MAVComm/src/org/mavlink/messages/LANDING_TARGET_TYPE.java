/**
 * Generated class : LANDING_TARGET_TYPE
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface LANDING_TARGET_TYPE
 * Type of landing target
 **/
public interface LANDING_TARGET_TYPE {
    /**
     * Landing target signaled by light beacon (ex: IR-LOCK)
     */
    public final static int LANDING_TARGET_TYPE_LIGHT_BEACON = 0;
    /**
     * Landing target signaled by radio beacon (ex: ILS, NDB)
     */
    public final static int LANDING_TARGET_TYPE_RADIO_BEACON = 1;
    /**
     * Landing target represented by a fiducial marker (ex: ARTag)
     */
    public final static int LANDING_TARGET_TYPE_VISION_FIDUCIAL = 2;
    /**
     * Landing target represented by a pre-defined visual shape/feature (ex: X-marker, H-marker, square)
     */
    public final static int LANDING_TARGET_TYPE_VISION_OTHER = 3;
}
