/**
 * Generated class : STORAGE_STATUS
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface STORAGE_STATUS
 * Flags to indicate the status of camera storage.
 **/
public interface STORAGE_STATUS {
    /**
     * Storage is missing (no microSD card loaded for example.)
     */
    public final static int STORAGE_STATUS_EMPTY = 0;
    /**
     * Storage present but unformatted.
     */
    public final static int STORAGE_STATUS_UNFORMATTED = 1;
    /**
     * Storage present and ready.
     */
    public final static int STORAGE_STATUS_READY = 2;
    /**
     * Camera does not supply storage status information. Capacity information in STORAGE_INFORMATION fields will be ignored.
     */
    public final static int STORAGE_STATUS_NOT_SUPPORTED = 3;
}
