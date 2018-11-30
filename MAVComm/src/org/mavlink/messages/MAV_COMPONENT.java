/**
 * Generated class : MAV_COMPONENT
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MAV_COMPONENT
 * Component ids (values) for the different types and instances of onboard hardware/software that might make up a MAVLink system (autopilot, cameras, servos, GPS systems, avoidance systems etc.). 
      Components must use the appropriate ID in their source address when sending messages. Components can also use IDs to determine if they are the intended recipient of an incoming message. The MAV_COMP_ID_ALL value is used to indicate messages that must be processed by all components.
      When creating new entries, components that can have multiple instances (e.g. cameras, servos etc.) should be allocated sequential values. An appropriate number of values should be left free after these components to allow the number of instances to be expanded.
 **/
public interface MAV_COMPONENT {
    /**
     * Used to broadcast messages to all components of the receiving system. Components should attempt to process messages with this component ID and forward to components on any other interfaces.
     */
    public final static int MAV_COMP_ID_ALL = 0;
    /**
     * System flight controller component ("autopilot"). Only one autopilot is expected in a particular system.
     */
    public final static int MAV_COMP_ID_AUTOPILOT1 = 1;
    /**
     * Camera #1.
     */
    public final static int MAV_COMP_ID_CAMERA = 100;
    /**
     * Camera #2.
     */
    public final static int MAV_COMP_ID_CAMERA2 = 101;
    /**
     * Camera #3.
     */
    public final static int MAV_COMP_ID_CAMERA3 = 102;
    /**
     * Camera #4.
     */
    public final static int MAV_COMP_ID_CAMERA4 = 103;
    /**
     * Camera #5.
     */
    public final static int MAV_COMP_ID_CAMERA5 = 104;
    /**
     * Camera #6.
     */
    public final static int MAV_COMP_ID_CAMERA6 = 105;
    /**
     * Servo #1.
     */
    public final static int MAV_COMP_ID_SERVO1 = 140;
    /**
     * Servo #2.
     */
    public final static int MAV_COMP_ID_SERVO2 = 141;
    /**
     * Servo #3.
     */
    public final static int MAV_COMP_ID_SERVO3 = 142;
    /**
     * Servo #4.
     */
    public final static int MAV_COMP_ID_SERVO4 = 143;
    /**
     * Servo #5.
     */
    public final static int MAV_COMP_ID_SERVO5 = 144;
    /**
     * Servo #6.
     */
    public final static int MAV_COMP_ID_SERVO6 = 145;
    /**
     * Servo #7.
     */
    public final static int MAV_COMP_ID_SERVO7 = 146;
    /**
     * Servo #8.
     */
    public final static int MAV_COMP_ID_SERVO8 = 147;
    /**
     * Servo #9.
     */
    public final static int MAV_COMP_ID_SERVO9 = 148;
    /**
     * Servo #10.
     */
    public final static int MAV_COMP_ID_SERVO10 = 149;
    /**
     * Servo #11.
     */
    public final static int MAV_COMP_ID_SERVO11 = 150;
    /**
     * Servo #12.
     */
    public final static int MAV_COMP_ID_SERVO12 = 151;
    /**
     * Servo #13.
     */
    public final static int MAV_COMP_ID_SERVO13 = 152;
    /**
     * Servo #14.
     */
    public final static int MAV_COMP_ID_SERVO14 = 153;
    /**
     * Gimbal component.
     */
    public final static int MAV_COMP_ID_GIMBAL = 154;
    /**
     * Logging component.
     */
    public final static int MAV_COMP_ID_LOG = 155;
    /**
     * Automatic Dependent Surveillance-Broadcast (ADS-B) component.
     */
    public final static int MAV_COMP_ID_ADSB = 156;
    /**
     * On Screen Display (OSD) devices for video links.
     */
    public final static int MAV_COMP_ID_OSD = 157;
    /**
     * Generic autopilot peripheral component ID. Meant for devices that do not implement the parameter microservice.
     */
    public final static int MAV_COMP_ID_PERIPHERAL = 158;
    /**
     * Gimbal ID for QX1.
     */
    public final static int MAV_COMP_ID_QX1_GIMBAL = 159;
    /**
     * FLARM collision alert component.
     */
    public final static int MAV_COMP_ID_FLARM = 160;
    /**
     * Component that can generate/supply a mission flight plan (e.g. GCS or developer API).
     */
    public final static int MAV_COMP_ID_MISSIONPLANNER = 190;
    /**
     * Component that finds an optimal path between points based on a certain constraint (e.g. minimum snap, shortest path, cost, etc.).
     */
    public final static int MAV_COMP_ID_PATHPLANNER = 195;
    /**
     * Component that plans a collision free path between two points.
     */
    public final static int MAV_COMP_ID_OBSTACLE_AVOIDANCE = 196;
    /**
     * Component that provides position estimates using VIO techniques.
     */
    public final static int MAV_COMP_ID_VISUAL_INERTIAL_ODOMETRY = 197;
    /**
     * Inertial Measurement Unit (IMU) #1.
     */
    public final static int MAV_COMP_ID_IMU = 200;
    /**
     * Inertial Measurement Unit (IMU) #2.
     */
    public final static int MAV_COMP_ID_IMU_2 = 201;
    /**
     * Inertial Measurement Unit (IMU) #3.
     */
    public final static int MAV_COMP_ID_IMU_3 = 202;
    /**
     * GPS #1.
     */
    public final static int MAV_COMP_ID_GPS = 220;
    /**
     * GPS #2.
     */
    public final static int MAV_COMP_ID_GPS2 = 221;
    /**
     * Component to bridge MAVLink to UDP (i.e. from a UART).
     */
    public final static int MAV_COMP_ID_UDP_BRIDGE = 240;
    /**
     * Component to bridge to UART (i.e. from UDP).
     */
    public final static int MAV_COMP_ID_UART_BRIDGE = 241;
    /**
     * Component for handling system messages (e.g. to ARM, takeoff, etc.).
     */
    public final static int MAV_COMP_ID_SYSTEM_CONTROL = 250;
}
