/**
 * Generated class : VIDEO_STREAM_TYPE
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface VIDEO_STREAM_TYPE
 * Video stream types
 **/
public interface VIDEO_STREAM_TYPE {
    /**
     * Stream is RTSP
     */
    public final static int VIDEO_STREAM_TYPE_RTSP = 0;
    /**
     * Stream is RTP UDP (URI gives the port number)
     */
    public final static int VIDEO_STREAM_TYPE_RTPUDP = 1;
    /**
     * Stream is MPEG on TCP
     */
    public final static int VIDEO_STREAM_TYPE_TCP_MPEG = 2;
    /**
     * Stream is h.264 on MPEG TS (URI gives the port number)
     */
    public final static int VIDEO_STREAM_TYPE_MPEG_TS_H264 = 3;
}
