/**
 * Generated class : UAVCAN_NODE_HEALTH
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface UAVCAN_NODE_HEALTH
 * Generalized UAVCAN node health
 **/
public interface UAVCAN_NODE_HEALTH {
    /**
     * The node is functioning properly.
     */
    public final static int UAVCAN_NODE_HEALTH_OK = 0;
    /**
     * A critical parameter went out of range or the node has encountered a minor failure.
     */
    public final static int UAVCAN_NODE_HEALTH_WARNING = 1;
    /**
     * The node has encountered a major failure.
     */
    public final static int UAVCAN_NODE_HEALTH_ERROR = 2;
    /**
     * The node has suffered a fatal malfunction.
     */
    public final static int UAVCAN_NODE_HEALTH_CRITICAL = 3;
}
