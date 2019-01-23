/**
 * Generated class : PRECISION_LAND_MODE
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface PRECISION_LAND_MODE
 * Precision land modes (used in MAV_CMD_NAV_LAND).
 **/
public interface PRECISION_LAND_MODE {
    /**
     * Normal (non-precision) landing.
     */
    public final static int PRECISION_LAND_MODE_DISABLED = 0;
    /**
     * Use precision landing if beacon detected when land command accepted, otherwise land normally.
     */
    public final static int PRECISION_LAND_MODE_OPPORTUNISTIC = 1;
    /**
     * Use precision landing, searching for beacon if not found when land command accepted (land normally if beacon cannot be found).
     */
    public final static int PRECISION_LAND_MODE_REQUIRED = 2;
}
