/**
 * Generated class : MAV_ODID_AUTH_TYPE
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MAV_ODID_AUTH_TYPE
 * 
 **/
public interface MAV_ODID_AUTH_TYPE {
    /**
     * No authentication type is specified.
     */
    public final static int MAV_ODID_AUTH_TYPE_NONE = 0;
    /**
     * Signature for the UAS (Unmanned Aircraft System) ID.
     */
    public final static int MAV_ODID_AUTH_TYPE_UAS_ID_SIGNATURE = 1;
    /**
     * Signature for the Operator ID.
     */
    public final static int MAV_ODID_AUTH_TYPE_OPERATOR_ID_SIGNATURE = 2;
    /**
     * Signature for the entire message set.
     */
    public final static int MAV_ODID_AUTH_TYPE_MESSAGE_SET_SIGNATURE = 3;
    /**
     * Authentication is provided by Network Remote ID.
     */
    public final static int MAV_ODID_AUTH_TYPE_NETWORK_REMOTE_ID = 4;
}
