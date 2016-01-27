/**
 * Generated class : FIRMWARE_VERSION_TYPE
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface FIRMWARE_VERSION_TYPE
 * These values define the type of firmware release.  These values indicate the first version or release of this type.  For example the first alpha release would be 64, the second would be 65.
 **/
public interface FIRMWARE_VERSION_TYPE {
    /**
     * development release
     */
    public final static int FIRMWARE_VERSION_TYPE_DEV = 0;
    /**
     * alpha release
     */
    public final static int FIRMWARE_VERSION_TYPE_ALPHA = 64;
    /**
     * beta release
     */
    public final static int FIRMWARE_VERSION_TYPE_BETA = 128;
    /**
     * release candidate
     */
    public final static int FIRMWARE_VERSION_TYPE_RC = 192;
    /**
     * official stable release
     */
    public final static int FIRMWARE_VERSION_TYPE_OFFICIAL = 255;
}
