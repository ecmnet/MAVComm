/**
 * Generated class : PARAM_ACK
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface PARAM_ACK
 * Result from a PARAM_EXT_SET message.
 **/
public interface PARAM_ACK {
    /**
     * Parameter value ACCEPTED and SET
     */
    public final static int PARAM_ACK_ACCEPTED = 0;
    /**
     * Parameter value UNKNOWN/UNSUPPORTED
     */
    public final static int PARAM_ACK_VALUE_UNSUPPORTED = 1;
    /**
     * Parameter failed to set
     */
    public final static int PARAM_ACK_FAILED = 2;
    /**
     * Parameter value received but not yet validated or set. A subsequent PARAM_EXT_ACK will follow once operation is completed with the actual result. These are for parameters that may take longer to set. Instead of waiting for an ACK and potentially timing out, you will immediately receive this response to let you know it was received.
     */
    public final static int PARAM_ACK_IN_PROGRESS = 3;
}
