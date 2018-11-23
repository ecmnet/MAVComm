/**
 * Generated class : UTM_DATA_AVAIL_FLAGS
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface UTM_DATA_AVAIL_FLAGS
 * Flags for the global position report.
 **/
public interface UTM_DATA_AVAIL_FLAGS {
    /**
     * The field time contains valid data.
     */
    public final static int UTM_DATA_AVAIL_FLAGS_TIME_VALID = 1;
    /**
     * The field uas_id contains valid data.
     */
    public final static int UTM_DATA_AVAIL_FLAGS_UAS_ID_AVAILABLE = 2;
    /**
     * The fields lat, lon and h_acc contain valid data.
     */
    public final static int UTM_DATA_AVAIL_FLAGS_POSITION_AVAILABLE = 4;
    /**
     * The fields alt and v_acc contain valid data.
     */
    public final static int UTM_DATA_AVAIL_FLAGS_ALTITUDE_AVAILABLE = 8;
    /**
     * The field relative_alt contains valid data.
     */
    public final static int UTM_DATA_AVAIL_FLAGS_RELATIVE_ALTITUDE_AVAILABLE = 16;
    /**
     * The fields vx and vy contain valid data.
     */
    public final static int UTM_DATA_AVAIL_FLAGS_HORIZONTAL_VELO_AVAILABLE = 32;
    /**
     * The field vz contains valid data.
     */
    public final static int UTM_DATA_AVAIL_FLAGS_VERTICAL_VELO_AVAILABLE = 64;
    /**
     * The fields next_lat, next_lon and next_alt contain valid data.
     */
    public final static int UTM_DATA_AVAIL_FLAGS_NEXT_WAYPOINT_AVAILABLE = 128;
}
