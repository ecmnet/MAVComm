/**
 * Generated class : UAVCAN_NODE_MODE
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface UAVCAN_NODE_MODE
 * Generalized UAVCAN node mode
 **/
public interface UAVCAN_NODE_MODE {
    /**
     * The node is performing its primary functions.
     */
    public final static int UAVCAN_NODE_MODE_OPERATIONAL = 0;
    /**
     * The node is initializing; this mode is entered immediately after startup.
     */
    public final static int UAVCAN_NODE_MODE_INITIALIZATION = 1;
    /**
     * The node is under maintenance.
     */
    public final static int UAVCAN_NODE_MODE_MAINTENANCE = 2;
    /**
     * The node is in the process of updating its software.
     */
    public final static int UAVCAN_NODE_MODE_SOFTWARE_UPDATE = 3;
    /**
     * The node is no longer available online.
     */
    public final static int UAVCAN_NODE_MODE_OFFLINE = 7;
}
