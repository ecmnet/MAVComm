/**
 * Generated class : MAV_ESTIMATOR_TYPE
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MAV_ESTIMATOR_TYPE
 * Enumeration of estimator types
 **/
public interface MAV_ESTIMATOR_TYPE {
    /**
     * This is a naive estimator without any real covariance feedback.
     */
    public final static int MAV_ESTIMATOR_TYPE_NAIVE = 1;
    /**
     * Computer vision based estimate. Might be up to scale.
     */
    public final static int MAV_ESTIMATOR_TYPE_VISION = 2;
    /**
     * Visual-inertial estimate.
     */
    public final static int MAV_ESTIMATOR_TYPE_VIO = 3;
    /**
     * Plain GPS estimate.
     */
    public final static int MAV_ESTIMATOR_TYPE_GPS = 4;
    /**
     * Estimator integrating GPS and inertial sensing.
     */
    public final static int MAV_ESTIMATOR_TYPE_GPS_INS = 5;
}
