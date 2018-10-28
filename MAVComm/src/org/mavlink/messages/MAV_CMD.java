/**
 * Generated class : MAV_CMD
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MAV_CMD
 * Commands to be executed by the MAV. They can be executed on user request, or as part of a mission script. If the action is used in a mission, the parameter mapping to the waypoint/mission message is as follows: Param 1, Param 2, Param 3, Param 4, X: Param 5, Y:Param 6, Z:Param 7. This command list is similar what ARINC 424 is for commercial aircraft: A data format how to interpret waypoint/mission data.
 **/
public interface MAV_CMD {
    /**
     * Navigate to waypoint.
     * PARAM 1 : Hold time in decimal seconds. (ignored by fixed wing, time to stay at waypoint for rotary wing)
     * PARAM 2 : Acceptance radius in meters (if the sphere with this radius is hit, the waypoint counts as reached)
     * PARAM 3 : 0 to pass through the WP, if > 0 radius in meters to pass by WP. Positive value for clockwise orbit, negative value for counter-clockwise orbit. Allows trajectory control.
     * PARAM 4 : Desired yaw angle at waypoint (rotary wing). NaN for unchanged.
     * PARAM 5 : Latitude
     * PARAM 6 : Longitude
     * PARAM 7 : Altitude
     */
    public final static int MAV_CMD_NAV_WAYPOINT = 16;
    /**
     * Loiter around this waypoint an unlimited amount of time
     * PARAM 1 : Empty
     * PARAM 2 : Empty
     * PARAM 3 : Radius around waypoint, in meters. If positive loiter clockwise, else counter-clockwise
     * PARAM 4 : Desired yaw angle.
     * PARAM 5 : Latitude
     * PARAM 6 : Longitude
     * PARAM 7 : Altitude
     */
    public final static int MAV_CMD_NAV_LOITER_UNLIM = 17;
    /**
     * Loiter around this waypoint for X turns
     * PARAM 1 : Turns
     * PARAM 2 : Empty
     * PARAM 3 : Radius around waypoint, in meters. If positive loiter clockwise, else counter-clockwise
     * PARAM 4 : Forward moving aircraft this sets exit xtrack location: 0 for center of loiter wp, 1 for exit location. Else, this is desired yaw angle
     * PARAM 5 : Latitude
     * PARAM 6 : Longitude
     * PARAM 7 : Altitude
     */
    public final static int MAV_CMD_NAV_LOITER_TURNS = 18;
    /**
     * Loiter around this waypoint for X seconds
     * PARAM 1 : Seconds (decimal)
     * PARAM 2 : Empty
     * PARAM 3 : Radius around waypoint, in meters. If positive loiter clockwise, else counter-clockwise
     * PARAM 4 : Forward moving aircraft this sets exit xtrack location: 0 for center of loiter wp, 1 for exit location. Else, this is desired yaw angle
     * PARAM 5 : Latitude
     * PARAM 6 : Longitude
     * PARAM 7 : Altitude
     */
    public final static int MAV_CMD_NAV_LOITER_TIME = 19;
    /**
     * Return to launch location
     * PARAM 1 : Empty
     * PARAM 2 : Empty
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_NAV_RETURN_TO_LAUNCH = 20;
    /**
     * Land at location
     * PARAM 1 : Abort Alt
     * PARAM 2 : Precision land mode. (0 = normal landing, 1 = opportunistic precision landing, 2 = required precsion landing)
     * PARAM 3 : Empty
     * PARAM 4 : Desired yaw angle. NaN for unchanged.
     * PARAM 5 : Latitude
     * PARAM 6 : Longitude
     * PARAM 7 : Altitude (ground level)
     */
    public final static int MAV_CMD_NAV_LAND = 21;
    /**
     * Takeoff from ground / hand
     * PARAM 1 : Minimum pitch (if airspeed sensor present), desired pitch without sensor
     * PARAM 2 : Empty
     * PARAM 3 : Empty
     * PARAM 4 : Yaw angle (if magnetometer present), ignored without magnetometer. NaN for unchanged.
     * PARAM 5 : Latitude
     * PARAM 6 : Longitude
     * PARAM 7 : Altitude
     */
    public final static int MAV_CMD_NAV_TAKEOFF = 22;
    /**
     * Land at local position (local frame only)
     * PARAM 1 : Landing target number (if available)
     * PARAM 2 : Maximum accepted offset from desired landing position [m] - computed magnitude from spherical coordinates: d = sqrt(x^2 + y^2 + z^2), which gives the maximum accepted distance between the desired landing position and the position where the vehicle is about to land
     * PARAM 3 : Landing descend rate [ms^-1]
     * PARAM 4 : Desired yaw angle [rad]
     * PARAM 5 : Y-axis position [m]
     * PARAM 6 : X-axis position [m]
     * PARAM 7 : Z-axis / ground level position [m]
     */
    public final static int MAV_CMD_NAV_LAND_LOCAL = 23;
    /**
     * Takeoff from local position (local frame only)
     * PARAM 1 : Minimum pitch (if airspeed sensor present), desired pitch without sensor [rad]
     * PARAM 2 : Empty
     * PARAM 3 : Takeoff ascend rate [ms^-1]
     * PARAM 4 : Yaw angle [rad] (if magnetometer or another yaw estimation source present), ignored without one of these
     * PARAM 5 : Y-axis position [m]
     * PARAM 6 : X-axis position [m]
     * PARAM 7 : Z-axis position [m]
     */
    public final static int MAV_CMD_NAV_TAKEOFF_LOCAL = 24;
    /**
     * Vehicle following, i.e. this waypoint represents the position of a moving vehicle
     * PARAM 1 : Following logic to use (e.g. loitering or sinusoidal following) - depends on specific autopilot implementation
     * PARAM 2 : Ground speed of vehicle to be followed
     * PARAM 3 : Radius around waypoint, in meters. If positive loiter clockwise, else counter-clockwise
     * PARAM 4 : Desired yaw angle.
     * PARAM 5 : Latitude
     * PARAM 6 : Longitude
     * PARAM 7 : Altitude
     */
    public final static int MAV_CMD_NAV_FOLLOW = 25;
    /**
     * Continue on the current course and climb/descend to specified altitude.  When the altitude is reached continue to the next command (i.e., don't proceed to the next command until the desired altitude is reached.
     * PARAM 1 : Climb or Descend (0 = Neutral, command completes when within 5m of this command's altitude, 1 = Climbing, command completes when at or above this command's altitude, 2 = Descending, command completes when at or below this command's altitude.
     * PARAM 2 : Empty
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Desired altitude in meters
     */
    public final static int MAV_CMD_NAV_CONTINUE_AND_CHANGE_ALT = 30;
    /**
     * Begin loiter at the specified Latitude and Longitude.  If Lat=Lon=0, then loiter at the current position.  Don't consider the navigation command complete (don't leave loiter) until the altitude has been reached.  Additionally, if the Heading Required parameter is non-zero the  aircraft will not leave the loiter until heading toward the next waypoint.
     * PARAM 1 : Heading Required (0 = False)
     * PARAM 2 : Radius in meters. If positive loiter clockwise, negative counter-clockwise, 0 means no change to standard loiter.
     * PARAM 3 : Empty
     * PARAM 4 : Forward moving aircraft this sets exit xtrack location: 0 for center of loiter wp, 1 for exit location
     * PARAM 5 : Latitude
     * PARAM 6 : Longitude
     * PARAM 7 : Altitude
     */
    public final static int MAV_CMD_NAV_LOITER_TO_ALT = 31;
    /**
     * Being following a target
     * PARAM 1 : System ID (the system ID of the FOLLOW_TARGET beacon). Send 0 to disable follow-me and return to the default position hold mode
     * PARAM 2 : RESERVED
     * PARAM 3 : RESERVED
     * PARAM 4 : altitude flag: 0: Keep current altitude, 1: keep altitude difference to target, 2: go to a fixed altitude above home
     * PARAM 5 : altitude
     * PARAM 6 : RESERVED
     * PARAM 7 : TTL in seconds in which the MAV should go to the default position hold mode after a message rx timeout
     */
    public final static int MAV_CMD_DO_FOLLOW = 32;
    /**
     * Reposition the MAV after a follow target command has been sent
     * PARAM 1 : Camera q1 (where 0 is on the ray from the camera to the tracking device)
     * PARAM 2 : Camera q2
     * PARAM 3 : Camera q3
     * PARAM 4 : Camera q4
     * PARAM 5 : altitude offset from target (m)
     * PARAM 6 : X offset from target (m)
     * PARAM 7 : Y offset from target (m)
     */
    public final static int MAV_CMD_DO_FOLLOW_REPOSITION = 33;
    /**
     * Start orbiting on the circumference of a circle defined by the parameters. Setting any value NaN results in using defaults.
     * PARAM 1 : Radius of the circle in meters. positive: Orbit clockwise. negative: Orbit counter-clockwise.
     * PARAM 2 : Velocity tangential in m/s. NaN: Vehicle configuration default.
     * PARAM 3 : Yaw behavior of the vehicle. 0: vehicle front points to the center (default). 1: Hold last heading. 2: Leave yaw uncontrolled.
     * PARAM 4 : Reserved (e.g. for dynamic center beacon options)
     * PARAM 5 : Center point latitude (if no MAV_FRAME specified) / X coordinate according to MAV_FRAME. NaN: Use current vehicle position or current center if already orbiting.
     * PARAM 6 : Center point longitude (if no MAV_FRAME specified) / Y coordinate according to MAV_FRAME. NaN: Use current vehicle position or current center if already orbiting.
     * PARAM 7 : Center point altitude (AMSL) (if no MAV_FRAME specified) / Z coordinate according to MAV_FRAME. NaN: Use current vehicle position or current center if already orbiting.
     */
    public final static int MAV_CMD_DO_ORBIT = 34;
    /**
     * Sets the region of interest (ROI) for a sensor set or the vehicle itself. This can then be used by the vehicles control system to control the vehicle attitude and the attitude of various sensors such as cameras.
     * PARAM 1 : Region of interest mode. (see MAV_ROI enum)
     * PARAM 2 : Waypoint index/ target ID. (see MAV_ROI enum)
     * PARAM 3 : ROI index (allows a vehicle to manage multiple ROI's)
     * PARAM 4 : Empty
     * PARAM 5 : x the location of the fixed ROI (see MAV_FRAME)
     * PARAM 6 : y
     * PARAM 7 : z
     */
    public final static int MAV_CMD_NAV_ROI = 80;
    /**
     * Control autonomous path planning on the MAV.
     * PARAM 1 : 0: Disable local obstacle avoidance / local path planning (without resetting map), 1: Enable local path planning, 2: Enable and reset local path planning
     * PARAM 2 : 0: Disable full path planning (without resetting map), 1: Enable, 2: Enable and reset map/occupancy grid, 3: Enable and reset planned route, but not occupancy grid
     * PARAM 3 : Empty
     * PARAM 4 : Yaw angle at goal, in compass degrees, [0..360]
     * PARAM 5 : Latitude/X of goal
     * PARAM 6 : Longitude/Y of goal
     * PARAM 7 : Altitude/Z of goal
     */
    public final static int MAV_CMD_NAV_PATHPLANNING = 81;
    /**
     * Navigate to waypoint using a spline path.
     * PARAM 1 : Hold time in decimal seconds. (ignored by fixed wing, time to stay at waypoint for rotary wing)
     * PARAM 2 : Empty
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Latitude/X of goal
     * PARAM 6 : Longitude/Y of goal
     * PARAM 7 : Altitude/Z of goal
     */
    public final static int MAV_CMD_NAV_SPLINE_WAYPOINT = 82;
    /**
     * Takeoff from ground using VTOL mode
     * PARAM 1 : Empty
     * PARAM 2 : Front transition heading, see VTOL_TRANSITION_HEADING enum.
     * PARAM 3 : Empty
     * PARAM 4 : Yaw angle in degrees. NaN for unchanged.
     * PARAM 5 : Latitude
     * PARAM 6 : Longitude
     * PARAM 7 : Altitude
     */
    public final static int MAV_CMD_NAV_VTOL_TAKEOFF = 84;
    /**
     * Land using VTOL mode
     * PARAM 1 : Empty
     * PARAM 2 : Empty
     * PARAM 3 : Approach altitude (with the same reference as the Altitude field). NaN if unspecified.
     * PARAM 4 : Yaw angle in degrees. NaN for unchanged.
     * PARAM 5 : Latitude
     * PARAM 6 : Longitude
     * PARAM 7 : Altitude (ground level)
     */
    public final static int MAV_CMD_NAV_VTOL_LAND = 85;
    /**
     * hand control over to an external controller
     * PARAM 1 : On / Off (> 0.5f on)
     * PARAM 2 : Empty
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_NAV_GUIDED_ENABLE = 92;
    /**
     * Delay the next navigation command a number of seconds or until a specified time
     * PARAM 1 : Delay in seconds (decimal, -1 to enable time-of-day fields)
     * PARAM 2 : hour (24h format, UTC, -1 to ignore)
     * PARAM 3 : minute (24h format, UTC, -1 to ignore)
     * PARAM 4 : second (24h format, UTC)
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_NAV_DELAY = 93;
    /**
     * Descend and place payload.  Vehicle descends until it detects a hanging payload has reached the ground, the gripper is opened to release the payload
     * PARAM 1 : Maximum distance to descend (meters)
     * PARAM 2 : Empty
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Latitude (deg * 1E7)
     * PARAM 6 : Longitude (deg * 1E7)
     * PARAM 7 : Altitude (meters)
     */
    public final static int MAV_CMD_NAV_PAYLOAD_PLACE = 94;
    /**
     * NOP - This command is only used to mark the upper limit of the NAV/ACTION commands in the enumeration
     * PARAM 1 : Empty
     * PARAM 2 : Empty
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_NAV_LAST = 95;
    /**
     * Delay mission state machine.
     * PARAM 1 : Delay in seconds (decimal)
     * PARAM 2 : Empty
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_CONDITION_DELAY = 112;
    /**
     * Ascend/descend at rate.  Delay mission state machine until desired altitude reached.
     * PARAM 1 : Descent / Ascend rate (m/s)
     * PARAM 2 : Empty
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Finish Altitude
     */
    public final static int MAV_CMD_CONDITION_CHANGE_ALT = 113;
    /**
     * Delay mission state machine until within desired distance of next NAV point.
     * PARAM 1 : Distance (meters)
     * PARAM 2 : Empty
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_CONDITION_DISTANCE = 114;
    /**
     * Reach a certain target angle.
     * PARAM 1 : target angle: [0-360], 0 is north
     * PARAM 2 : speed during yaw change:[deg per second]
     * PARAM 3 : direction: negative: counter clockwise, positive: clockwise [-1,1]
     * PARAM 4 : relative offset or absolute angle: [ 1,0]
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_CONDITION_YAW = 115;
    /**
     * NOP - This command is only used to mark the upper limit of the CONDITION commands in the enumeration
     * PARAM 1 : Empty
     * PARAM 2 : Empty
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_CONDITION_LAST = 159;
    /**
     * Set system mode.
     * PARAM 1 : Mode, as defined by ENUM MAV_MODE
     * PARAM 2 : Custom mode - this is system specific, please refer to the individual autopilot specifications for details.
     * PARAM 3 : Custom sub mode - this is system specific, please refer to the individual autopilot specifications for details.
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_SET_MODE = 176;
    /**
     * Jump to the desired command in the mission list.  Repeat this action only the specified number of times
     * PARAM 1 : Sequence number
     * PARAM 2 : Repeat count
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_JUMP = 177;
    /**
     * Change speed and/or throttle set points.
     * PARAM 1 : Speed type (0=Airspeed, 1=Ground Speed, 2=Climb Speed, 3=Descent Speed)
     * PARAM 2 : Speed  (m/s, -1 indicates no change)
     * PARAM 3 : Throttle  ( Percent, -1 indicates no change)
     * PARAM 4 : absolute or relative [0,1]
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_CHANGE_SPEED = 178;
    /**
     * Changes the home location either to the current location or a specified location.
     * PARAM 1 : Use current (1=use current location, 0=use specified location)
     * PARAM 2 : Empty
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Latitude
     * PARAM 6 : Longitude
     * PARAM 7 : Altitude
     */
    public final static int MAV_CMD_DO_SET_HOME = 179;
    /**
     * Set a system parameter.  Caution!  Use of this command requires knowledge of the numeric enumeration value of the parameter.
     * PARAM 1 : Parameter number
     * PARAM 2 : Parameter value
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_SET_PARAMETER = 180;
    /**
     * Set a relay to a condition.
     * PARAM 1 : Relay number
     * PARAM 2 : Setting (1=on, 0=off, others possible depending on system hardware)
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_SET_RELAY = 181;
    /**
     * Cycle a relay on and off for a desired number of cycles with a desired period.
     * PARAM 1 : Relay number
     * PARAM 2 : Cycle count
     * PARAM 3 : Cycle time (seconds, decimal)
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_REPEAT_RELAY = 182;
    /**
     * Set a servo to a desired PWM value.
     * PARAM 1 : Servo number
     * PARAM 2 : PWM (microseconds, 1000 to 2000 typical)
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_SET_SERVO = 183;
    /**
     * Cycle a between its nominal setting and a desired PWM for a desired number of cycles with a desired period.
     * PARAM 1 : Servo number
     * PARAM 2 : PWM (microseconds, 1000 to 2000 typical)
     * PARAM 3 : Cycle count
     * PARAM 4 : Cycle time (seconds)
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_REPEAT_SERVO = 184;
    /**
     * Terminate flight immediately
     * PARAM 1 : Flight termination activated if > 0.5
     * PARAM 2 : Empty
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_FLIGHTTERMINATION = 185;
    /**
     * Change altitude set point.
     * PARAM 1 : Altitude in meters
     * PARAM 2 : Mav frame of new altitude (see MAV_FRAME)
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_CHANGE_ALTITUDE = 186;
    /**
     * Mission command to perform a landing. This is used as a marker in a mission to tell the autopilot where a sequence of mission items that represents a landing starts. It may also be sent via a COMMAND_LONG to trigger a landing, in which case the nearest (geographically) landing sequence in the mission will be used. The Latitude/Longitude is optional, and may be set to 0 if not needed. If specified then it will be used to help find the closest landing sequence.
     * PARAM 1 : Empty
     * PARAM 2 : Empty
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Latitude
     * PARAM 6 : Longitude
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_LAND_START = 189;
    /**
     * Mission command to perform a landing from a rally point.
     * PARAM 1 : Break altitude (meters)
     * PARAM 2 : Landing speed (m/s)
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_RALLY_LAND = 190;
    /**
     * Mission command to safely abort an autonomous landing.
     * PARAM 1 : Altitude (meters)
     * PARAM 2 : Empty
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_GO_AROUND = 191;
    /**
     * Reposition the vehicle to a specific WGS84 global position.
     * PARAM 1 : Ground speed, less than 0 (-1) for default
     * PARAM 2 : Bitmask of option flags, see the MAV_DO_REPOSITION_FLAGS enum.
     * PARAM 3 : Reserved
     * PARAM 4 : Yaw heading, NaN for unchanged. For planes indicates loiter direction (0: clockwise, 1: counter clockwise)
     * PARAM 5 : Latitude (deg * 1E7)
     * PARAM 6 : Longitude (deg * 1E7)
     * PARAM 7 : Altitude (meters)
     */
    public final static int MAV_CMD_DO_REPOSITION = 192;
    /**
     * If in a GPS controlled position mode, hold the current position or continue.
     * PARAM 1 : 0: Pause current mission or reposition command, hold current position. 1: Continue mission. A VTOL capable vehicle should enter hover mode (multicopter and VTOL planes). A plane should loiter with the default loiter radius.
     * PARAM 2 : Reserved
     * PARAM 3 : Reserved
     * PARAM 4 : Reserved
     * PARAM 5 : Reserved
     * PARAM 6 : Reserved
     * PARAM 7 : Reserved
     */
    public final static int MAV_CMD_DO_PAUSE_CONTINUE = 193;
    /**
     * Set moving direction to forward or reverse.
     * PARAM 1 : Direction (0=Forward, 1=Reverse)
     * PARAM 2 : Empty
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_SET_REVERSE = 194;
    /**
     * Sets the region of interest (ROI) to a location. This can then be used by the vehicles control system to control the vehicle attitude and the attitude of various sensors such as cameras.
     * PARAM 1 : Empty
     * PARAM 2 : Empty
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Latitude
     * PARAM 6 : Longitude
     * PARAM 7 : Altitude
     */
    public final static int MAV_CMD_DO_SET_ROI_LOCATION = 195;
    /**
     * Sets the region of interest (ROI) to be toward next waypoint, with optional pitch/roll/yaw offset. This can then be used by the vehicles control system to control the vehicle attitude and the attitude of various sensors such as cameras.
     * PARAM 1 : Empty
     * PARAM 2 : Empty
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : pitch offset from next waypoint
     * PARAM 6 : roll offset from next waypoint
     * PARAM 7 : yaw offset from next waypoint
     */
    public final static int MAV_CMD_DO_SET_ROI_WPNEXT_OFFSET = 196;
    /**
     * Cancels any previous ROI command returning the vehicle/sensors to default flight characteristics. This can then be used by the vehicles control system to control the vehicle attitude and the attitude of various sensors such as cameras.
     * PARAM 1 : Empty
     * PARAM 2 : Empty
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_SET_ROI_NONE = 197;
    /**
     * Control onboard camera system.
     * PARAM 1 : Camera ID (-1 for all)
     * PARAM 2 : Transmission: 0: disabled, 1: enabled compressed, 2: enabled raw
     * PARAM 3 : Transmission mode: 0: video stream, >0: single images every n seconds (decimal)
     * PARAM 4 : Recording: 0: disabled, 1: enabled compressed, 2: enabled raw
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_CONTROL_VIDEO = 200;
    /**
     * Sets the region of interest (ROI) for a sensor set or the vehicle itself. This can then be used by the vehicles control system to control the vehicle attitude and the attitude of various sensors such as cameras.
     * PARAM 1 : Region of interest mode. (see MAV_ROI enum)
     * PARAM 2 : Waypoint index/ target ID. (see MAV_ROI enum)
     * PARAM 3 : ROI index (allows a vehicle to manage multiple ROI's)
     * PARAM 4 : Empty
     * PARAM 5 : MAV_ROI_WPNEXT: pitch offset from next waypoint, MAV_ROI_LOCATION: latitude
     * PARAM 6 : MAV_ROI_WPNEXT: roll offset from next waypoint, MAV_ROI_LOCATION: longitude
     * PARAM 7 : MAV_ROI_WPNEXT: yaw offset from next waypoint, MAV_ROI_LOCATION: altitude
     */
    public final static int MAV_CMD_DO_SET_ROI = 201;
    /**
     * THIS INTERFACE IS DEPRECATED since 2018-01. Please use PARAM_EXT_XXX messages and the camera definition format described in https://mavlink.io/en/protocol/camera_def.html.
     * PARAM 1 : Modes: P, TV, AV, M, Etc
     * PARAM 2 : Shutter speed: Divisor number for one second
     * PARAM 3 : Aperture: F stop number
     * PARAM 4 : ISO number e.g. 80, 100, 200, Etc
     * PARAM 5 : Exposure type enumerator
     * PARAM 6 : Command Identity
     * PARAM 7 : Main engine cut-off time before camera trigger in seconds/10 (0 means no cut-off)
     */
    public final static int MAV_CMD_DO_DIGICAM_CONFIGURE = 202;
    /**
     * THIS INTERFACE IS DEPRECATED since 2018-01. Please use PARAM_EXT_XXX messages and the camera definition format described in https://mavlink.io/en/protocol/camera_def.html.
     * PARAM 1 : Session control e.g. show/hide lens
     * PARAM 2 : Zoom's absolute position
     * PARAM 3 : Zooming step value to offset zoom from the current position
     * PARAM 4 : Focus Locking, Unlocking or Re-locking
     * PARAM 5 : Shooting Command
     * PARAM 6 : Command Identity
     * PARAM 7 : Test shot identifier. If set to 1, image will only be captured, but not counted towards internal frame count.
     */
    public final static int MAV_CMD_DO_DIGICAM_CONTROL = 203;
    /**
     * Mission command to configure a camera or antenna mount
     * PARAM 1 : Mount operation mode (see MAV_MOUNT_MODE enum)
     * PARAM 2 : stabilize roll? (1 = yes, 0 = no)
     * PARAM 3 : stabilize pitch? (1 = yes, 0 = no)
     * PARAM 4 : stabilize yaw? (1 = yes, 0 = no)
     * PARAM 5 : roll input (0 = angle body frame, 1 = angular rate, 2 = angle absolute frame)
     * PARAM 6 : pitch input (0 = angle body frame, 1 = angular rate, 2 = angle absolute frame)
     * PARAM 7 : yaw input (0 = angle body frame, 1 = angular rate, 2 = angle absolute frame)
     */
    public final static int MAV_CMD_DO_MOUNT_CONFIGURE = 204;
    /**
     * Mission command to control a camera or antenna mount
     * PARAM 1 : pitch depending on mount mode (degrees or degrees/second depending on pitch input).
     * PARAM 2 : roll depending on mount mode (degrees or degrees/second depending on roll input).
     * PARAM 3 : yaw depending on mount mode (degrees or degrees/second depending on yaw input).
     * PARAM 4 : alt in meters depending on mount mode.
     * PARAM 5 : latitude in degrees * 1E7, set if appropriate mount mode.
     * PARAM 6 : longitude in degrees * 1E7, set if appropriate mount mode.
     * PARAM 7 : MAV_MOUNT_MODE enum value
     */
    public final static int MAV_CMD_DO_MOUNT_CONTROL = 205;
    /**
     * Mission command to set camera trigger distance for this flight. The camera is triggered each time this distance is exceeded. This command can also be used to set the shutter integration time for the camera.
     * PARAM 1 : Camera trigger distance (meters). 0 to stop triggering.
     * PARAM 2 : Camera shutter integration time (milliseconds). -1 or 0 to ignore
     * PARAM 3 : Trigger camera once immediately. (0 = no trigger, 1 = trigger)
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_SET_CAM_TRIGG_DIST = 206;
    /**
     * Mission command to enable the geofence
     * PARAM 1 : enable? (0=disable, 1=enable, 2=disable_floor_only)
     * PARAM 2 : Empty
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_FENCE_ENABLE = 207;
    /**
     * Mission command to trigger a parachute
     * PARAM 1 : action (0=disable, 1=enable, 2=release, for some systems see PARACHUTE_ACTION enum, not in general message set.)
     * PARAM 2 : Empty
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_PARACHUTE = 208;
    /**
     * Mission command to perform motor test
     * PARAM 1 : motor number (a number from 1 to max number of motors on the vehicle)
     * PARAM 2 : throttle type (0=throttle percentage, 1=PWM, 2=pilot throttle channel pass-through. See MOTOR_TEST_THROTTLE_TYPE enum)
     * PARAM 3 : throttle
     * PARAM 4 : timeout (in seconds)
     * PARAM 5 : motor count (number of motors to test to test in sequence, waiting for the timeout above between them; 0=1 motor, 1=1 motor, 2=2 motors...)
     * PARAM 6 : motor test order (See MOTOR_TEST_ORDER enum)
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_MOTOR_TEST = 209;
    /**
     * Change to/from inverted flight
     * PARAM 1 : inverted (0=normal, 1=inverted)
     * PARAM 2 : Empty
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_INVERTED_FLIGHT = 210;
    /**
     * Sets a desired vehicle turn angle and speed change
     * PARAM 1 : yaw angle to adjust steering by in centidegress
     * PARAM 2 : speed - normalized to 0 .. 1
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_NAV_SET_YAW_SPEED = 213;
    /**
     * Mission command to set camera trigger interval for this flight. If triggering is enabled, the camera is triggered each time this interval expires. This command can also be used to set the shutter integration time for the camera.
     * PARAM 1 : Camera trigger cycle time (milliseconds). -1 or 0 to ignore.
     * PARAM 2 : Camera shutter integration time (milliseconds). Should be less than trigger cycle time. -1 or 0 to ignore.
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_SET_CAM_TRIGG_INTERVAL = 214;
    /**
     * Mission command to control a camera or antenna mount, using a quaternion as reference.
     * PARAM 1 : q1 - quaternion param #1, w (1 in null-rotation)
     * PARAM 2 : q2 - quaternion param #2, x (0 in null-rotation)
     * PARAM 3 : q3 - quaternion param #3, y (0 in null-rotation)
     * PARAM 4 : q4 - quaternion param #4, z (0 in null-rotation)
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_MOUNT_CONTROL_QUAT = 220;
    /**
     * set id of master controller
     * PARAM 1 : System ID
     * PARAM 2 : Component ID
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_GUIDED_MASTER = 221;
    /**
     * set limits for external control
     * PARAM 1 : timeout - maximum time (in seconds) that external controller will be allowed to control vehicle. 0 means no timeout
     * PARAM 2 : Absolute altitude (AMSL) min, in meters - if vehicle moves below this alt, the command will be aborted and the mission will continue. 0 means no lower altitude limit
     * PARAM 3 : Absolute altitude (AMSL) max, in meters - if vehicle moves above this alt, the command will be aborted and the mission will continue. 0 means no upper altitude limit
     * PARAM 4 : Horizontal move limit (AMSL), in meters - if vehicle moves more than this distance from its location at the moment the command was executed, the command will be aborted and the mission will continue. 0 means no horizontal altitude limit
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_GUIDED_LIMITS = 222;
    /**
     * Control vehicle engine. This is interpreted by the vehicles engine controller to change the target engine state. It is intended for vehicles with internal combustion engines
     * PARAM 1 : 0: Stop engine, 1:Start Engine
     * PARAM 2 : 0: Warm start, 1:Cold start. Controls use of choke where applicable
     * PARAM 3 : Height delay (meters). This is for commanding engine start only after the vehicle has gained the specified height. Used in VTOL vehicles during takeoff to start engine after the aircraft is off the ground. Zero for no delay.
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_ENGINE_CONTROL = 223;
    /**
     * Set the mission item with sequence number seq as current item. This means that the MAV will continue to this mission item on the shortest path (not following the mission items in-between).
     * PARAM 1 : Mission sequence value to set
     * PARAM 2 : Empty
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_SET_MISSION_CURRENT = 224;
    /**
     * NOP - This command is only used to mark the upper limit of the DO commands in the enumeration
     * PARAM 1 : Empty
     * PARAM 2 : Empty
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_DO_LAST = 240;
    /**
     * Trigger calibration. This command will be only accepted if in pre-flight mode. Except for Temperature Calibration, only one sensor should be set in a single message and all others should be zero.
     * PARAM 1 : 1: gyro calibration, 3: gyro temperature calibration
     * PARAM 2 : 1: magnetometer calibration
     * PARAM 3 : 1: ground pressure calibration
     * PARAM 4 : 1: radio RC calibration, 2: RC trim calibration
     * PARAM 5 : 1: accelerometer calibration, 2: board level calibration, 3: accelerometer temperature calibration, 4: simple accelerometer calibration
     * PARAM 6 : 1: APM: compass/motor interference calibration (PX4: airspeed calibration, deprecated), 2: airspeed calibration
     * PARAM 7 : 1: ESC calibration, 3: barometer temperature calibration
     */
    public final static int MAV_CMD_PREFLIGHT_CALIBRATION = 241;
    /**
     * Set sensor offsets. This command will be only accepted if in pre-flight mode.
     * PARAM 1 : Sensor to adjust the offsets for: 0: gyros, 1: accelerometer, 2: magnetometer, 3: barometer, 4: optical flow, 5: second magnetometer, 6: third magnetometer
     * PARAM 2 : X axis offset (or generic dimension 1), in the sensor's raw units
     * PARAM 3 : Y axis offset (or generic dimension 2), in the sensor's raw units
     * PARAM 4 : Z axis offset (or generic dimension 3), in the sensor's raw units
     * PARAM 5 : Generic dimension 4, in the sensor's raw units
     * PARAM 6 : Generic dimension 5, in the sensor's raw units
     * PARAM 7 : Generic dimension 6, in the sensor's raw units
     */
    public final static int MAV_CMD_PREFLIGHT_SET_SENSOR_OFFSETS = 242;
    /**
     * Trigger UAVCAN config. This command will be only accepted if in pre-flight mode.
     * PARAM 1 : 1: Trigger actuator ID assignment and direction mapping.
     * PARAM 2 : Reserved
     * PARAM 3 : Reserved
     * PARAM 4 : Reserved
     * PARAM 5 : Reserved
     * PARAM 6 : Reserved
     * PARAM 7 : Reserved
     */
    public final static int MAV_CMD_PREFLIGHT_UAVCAN = 243;
    /**
     * Request storage of different parameter values and logs. This command will be only accepted if in pre-flight mode.
     * PARAM 1 : Parameter storage: 0: READ FROM FLASH/EEPROM, 1: WRITE CURRENT TO FLASH/EEPROM, 2: Reset to defaults
     * PARAM 2 : Mission storage: 0: READ FROM FLASH/EEPROM, 1: WRITE CURRENT TO FLASH/EEPROM, 2: Reset to defaults
     * PARAM 3 : Onboard logging: 0: Ignore, 1: Start default rate logging, -1: Stop logging, > 1: start logging with rate of param 3 in Hz (e.g. set to 1000 for 1000 Hz logging)
     * PARAM 4 : Reserved
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_PREFLIGHT_STORAGE = 245;
    /**
     * Request the reboot or shutdown of system components.
     * PARAM 1 : 0: Do nothing for autopilot, 1: Reboot autopilot, 2: Shutdown autopilot, 3: Reboot autopilot and keep it in the bootloader until upgraded.
     * PARAM 2 : 0: Do nothing for onboard computer, 1: Reboot onboard computer, 2: Shutdown onboard computer, 3: Reboot onboard computer and keep it in the bootloader until upgraded.
     * PARAM 3 : WIP: 0: Do nothing for camera, 1: Reboot onboard camera, 2: Shutdown onboard camera, 3: Reboot onboard camera and keep it in the bootloader until upgraded
     * PARAM 4 : WIP: 0: Do nothing for mount (e.g. gimbal), 1: Reboot mount, 2: Shutdown mount, 3: Reboot mount and keep it in the bootloader until upgraded
     * PARAM 5 : Reserved, send 0
     * PARAM 6 : Reserved, send 0
     * PARAM 7 : WIP: ID (e.g. camera ID -1 for all IDs)
     */
    public final static int MAV_CMD_PREFLIGHT_REBOOT_SHUTDOWN = 246;
    /**
     * Hold / continue the current action
     * PARAM 1 : MAV_GOTO_DO_HOLD: hold MAV_GOTO_DO_CONTINUE: continue with next item in mission plan
     * PARAM 2 : MAV_GOTO_HOLD_AT_CURRENT_POSITION: Hold at current position MAV_GOTO_HOLD_AT_SPECIFIED_POSITION: hold at specified position
     * PARAM 3 : MAV_FRAME coordinate frame of hold point
     * PARAM 4 : Desired yaw angle in degrees
     * PARAM 5 : Latitude / X position
     * PARAM 6 : Longitude / Y position
     * PARAM 7 : Altitude / Z position
     */
    public final static int MAV_CMD_OVERRIDE_GOTO = 252;
    /**
     * start running a mission
     * PARAM 1 : first_item: the first mission item to run
     * PARAM 2 : last_item:  the last mission item to run (after this item is run, the mission ends)
     */
    public final static int MAV_CMD_MISSION_START = 300;
    /**
     * Arms / Disarms a component
     * PARAM 1 : 1 to arm, 0 to disarm
     */
    public final static int MAV_CMD_COMPONENT_ARM_DISARM = 400;
    /**
     * Request the home position from the vehicle.
     * PARAM 1 : Reserved
     * PARAM 2 : Reserved
     * PARAM 3 : Reserved
     * PARAM 4 : Reserved
     * PARAM 5 : Reserved
     * PARAM 6 : Reserved
     * PARAM 7 : Reserved
     */
    public final static int MAV_CMD_GET_HOME_POSITION = 410;
    /**
     * Starts receiver pairing
     * PARAM 1 : 0:Spektrum
     * PARAM 2 : RC type (see RC_TYPE enum)
     */
    public final static int MAV_CMD_START_RX_PAIR = 500;
    /**
     * Request the interval between messages for a particular MAVLink message ID
     * PARAM 1 : The MAVLink message ID
     */
    public final static int MAV_CMD_GET_MESSAGE_INTERVAL = 510;
    /**
     * Set the interval between messages for a particular MAVLink message ID. This interface replaces REQUEST_DATA_STREAM
     * PARAM 1 : The MAVLink message ID
     * PARAM 2 : The interval between two messages, in microseconds. Set to -1 to disable and 0 to request default rate.
     */
    public final static int MAV_CMD_SET_MESSAGE_INTERVAL = 511;
    /**
     * Request MAVLink protocol version compatibility
     * PARAM 1 : 1: Request supported protocol versions by all nodes on the network
     * PARAM 2 : Reserved (all remaining params)
     */
    public final static int MAV_CMD_REQUEST_PROTOCOL_VERSION = 519;
    /**
     * Request autopilot capabilities
     * PARAM 1 : 1: Request autopilot version
     * PARAM 2 : Reserved (all remaining params)
     */
    public final static int MAV_CMD_REQUEST_AUTOPILOT_CAPABILITIES = 520;
    /**
     * Request camera information (CAMERA_INFORMATION).
     * PARAM 1 : 0: No action 1: Request camera capabilities
     * PARAM 2 : Reserved (all remaining params)
     */
    public final static int MAV_CMD_REQUEST_CAMERA_INFORMATION = 521;
    /**
     * Request camera settings (CAMERA_SETTINGS).
     * PARAM 1 : 0: No Action 1: Request camera settings
     * PARAM 2 : Reserved (all remaining params)
     */
    public final static int MAV_CMD_REQUEST_CAMERA_SETTINGS = 522;
    /**
     * Request storage information (STORAGE_INFORMATION). Use the command's target_component to target a specific component's storage.
     * PARAM 1 : Storage ID (0 for all, 1 for first, 2 for second, etc.)
     * PARAM 2 : 0: No Action 1: Request storage information
     * PARAM 3 : Reserved (all remaining params)
     */
    public final static int MAV_CMD_REQUEST_STORAGE_INFORMATION = 525;
    /**
     * Format a storage medium. Once format is complete, a STORAGE_INFORMATION message is sent. Use the command's target_component to target a specific component's storage.
     * PARAM 1 : Storage ID (1 for first, 2 for second, etc.)
     * PARAM 2 : 0: No action 1: Format storage
     * PARAM 3 : Reserved (all remaining params)
     */
    public final static int MAV_CMD_STORAGE_FORMAT = 526;
    /**
     * Request camera capture status (CAMERA_CAPTURE_STATUS)
     * PARAM 1 : 0: No Action 1: Request camera capture status
     * PARAM 2 : Reserved (all remaining params)
     */
    public final static int MAV_CMD_REQUEST_CAMERA_CAPTURE_STATUS = 527;
    /**
     * Request flight information (FLIGHT_INFORMATION)
     * PARAM 1 : 1: Request flight information
     * PARAM 2 : Reserved (all remaining params)
     */
    public final static int MAV_CMD_REQUEST_FLIGHT_INFORMATION = 528;
    /**
     * Reset all camera settings to Factory Default
     * PARAM 1 : 0: No Action 1: Reset all settings
     * PARAM 2 : Reserved (all remaining params)
     */
    public final static int MAV_CMD_RESET_CAMERA_SETTINGS = 529;
    /**
     * Set camera running mode. Use NAN for reserved values.
     * PARAM 1 : Reserved (Set to 0)
     * PARAM 2 : Camera mode (see CAMERA_MODE enum)
     * PARAM 3 : Reserved (all remaining params)
     */
    public final static int MAV_CMD_SET_CAMERA_MODE = 530;
    /**
     * Start image capture sequence. Sends CAMERA_IMAGE_CAPTURED after each capture. Use NAN for reserved values.
     * PARAM 1 : Reserved (Set to 0)
     * PARAM 2 : Duration between two consecutive pictures (in seconds)
     * PARAM 3 : Number of images to capture total - 0 for unlimited capture
     * PARAM 4 : Capture sequence (ID to prevent double captures when a command is retransmitted, 0: unused, >= 1: used)
     * PARAM 5 : Reserved (all remaining params)
     */
    public final static int MAV_CMD_IMAGE_START_CAPTURE = 2000;
    /**
     * Stop image capture sequence Use NAN for reserved values.
     * PARAM 1 : Reserved (Set to 0)
     * PARAM 2 : Reserved (all remaining params)
     */
    public final static int MAV_CMD_IMAGE_STOP_CAPTURE = 2001;
    /**
     * Re-request a CAMERA_IMAGE_CAPTURE packet. Use NAN for reserved values.
     * PARAM 1 : Sequence number for missing CAMERA_IMAGE_CAPTURE packet
     * PARAM 2 : Reserved (all remaining params)
     */
    public final static int MAV_CMD_REQUEST_CAMERA_IMAGE_CAPTURE = 2002;
    /**
     * Enable or disable on-board camera triggering system.
     * PARAM 1 : Trigger enable/disable (0 for disable, 1 for start), -1 to ignore
     * PARAM 2 : 1 to reset the trigger sequence, -1 or 0 to ignore
     * PARAM 3 : 1 to pause triggering, but without switching the camera off or retracting it. -1 to ignore
     */
    public final static int MAV_CMD_DO_TRIGGER_CONTROL = 2003;
    /**
     * Starts video capture (recording). Use NAN for reserved values.
     * PARAM 1 : Reserved (Set to 0)
     * PARAM 2 : Frequency CAMERA_CAPTURE_STATUS messages should be sent while recording (0 for no messages, otherwise frequency in Hz)
     * PARAM 3 : Reserved (all remaining params)
     */
    public final static int MAV_CMD_VIDEO_START_CAPTURE = 2500;
    /**
     * Stop the current video capture (recording). Use NAN for reserved values.
     * PARAM 1 : Reserved (Set to 0)
     * PARAM 2 : Reserved (all remaining params)
     */
    public final static int MAV_CMD_VIDEO_STOP_CAPTURE = 2501;
    /**
     * Start video streaming
     * PARAM 1 : Camera ID (0 for all cameras, 1 for first, 2 for second, etc.)
     * PARAM 2 : Reserved
     */
    public final static int MAV_CMD_VIDEO_START_STREAMING = 2502;
    /**
     * Stop the current video streaming
     * PARAM 1 : Camera ID (0 for all cameras, 1 for first, 2 for second, etc.)
     * PARAM 2 : Reserved
     */
    public final static int MAV_CMD_VIDEO_STOP_STREAMING = 2503;
    /**
     * Request video stream information (VIDEO_STREAM_INFORMATION)
     * PARAM 1 : Camera ID (0 for all cameras, 1 for first, 2 for second, etc.)
     * PARAM 2 : 0: No Action 1: Request video stream information
     * PARAM 3 : Reserved (all remaining params)
     */
    public final static int MAV_CMD_REQUEST_VIDEO_STREAM_INFORMATION = 2504;
    /**
     * Request to start streaming logging data over MAVLink (see also LOGGING_DATA message)
     * PARAM 1 : Format: 0: ULog
     * PARAM 2 : Reserved (set to 0)
     * PARAM 3 : Reserved (set to 0)
     * PARAM 4 : Reserved (set to 0)
     * PARAM 5 : Reserved (set to 0)
     * PARAM 6 : Reserved (set to 0)
     * PARAM 7 : Reserved (set to 0)
     */
    public final static int MAV_CMD_LOGGING_START = 2510;
    /**
     * Request to stop streaming log data over MAVLink
     * PARAM 1 : Reserved (set to 0)
     * PARAM 2 : Reserved (set to 0)
     * PARAM 3 : Reserved (set to 0)
     * PARAM 4 : Reserved (set to 0)
     * PARAM 5 : Reserved (set to 0)
     * PARAM 6 : Reserved (set to 0)
     * PARAM 7 : Reserved (set to 0)
     */
    public final static int MAV_CMD_LOGGING_STOP = 2511;
    /**
     * 
     * PARAM 1 : Landing gear ID (default: 0, -1 for all)
     * PARAM 2 : Landing gear position (Down: 0, Up: 1, NAN for no change)
     * PARAM 3 : Reserved, set to NAN
     * PARAM 4 : Reserved, set to NAN
     * PARAM 5 : Reserved, set to NAN
     * PARAM 6 : Reserved, set to NAN
     * PARAM 7 : Reserved, set to NAN
     */
    public final static int MAV_CMD_AIRFRAME_CONFIGURATION = 2520;
    /**
     * Request to start/stop transmitting over the high latency telemetry
     * PARAM 1 : Control transmission over high latency telemetry (0: stop, 1: start)
     * PARAM 2 : Empty
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Empty
     * PARAM 6 : Empty
     * PARAM 7 : Empty
     */
    public final static int MAV_CMD_CONTROL_HIGH_LATENCY = 2600;
    /**
     * Create a panorama at the current position
     * PARAM 1 : Viewing angle horizontal of the panorama (in degrees, +- 0.5 the total angle)
     * PARAM 2 : Viewing angle vertical of panorama (in degrees)
     * PARAM 3 : Speed of the horizontal rotation (in degrees per second)
     * PARAM 4 : Speed of the vertical rotation (in degrees per second)
     */
    public final static int MAV_CMD_PANORAMA_CREATE = 2800;
    /**
     * Request VTOL transition
     * PARAM 1 : The target VTOL state, as defined by ENUM MAV_VTOL_STATE. Only MAV_VTOL_STATE_MC and MAV_VTOL_STATE_FW can be used.
     */
    public final static int MAV_CMD_DO_VTOL_TRANSITION = 3000;
    /**
     * Request authorization to arm the vehicle to a external entity, the arm authorizer is responsible to request all data that is needs from the vehicle before authorize or deny the request. If approved the progress of command_ack message should be set with period of time that this authorization is valid in seconds or in case it was denied it should be set with one of the reasons in ARM_AUTH_DENIED_REASON.
     * PARAM 1 : Vehicle system id, this way ground station can request arm authorization on behalf of any vehicle
     */
    public final static int MAV_CMD_ARM_AUTHORIZATION_REQUEST = 3001;
    /**
     * This command sets the submode to standard guided when vehicle is in guided mode. The vehicle holds position and altitude and the user can input the desired velocities along all three axes.
     */
    public final static int MAV_CMD_SET_GUIDED_SUBMODE_STANDARD = 4000;
    /**
     * This command sets submode circle when vehicle is in guided mode. Vehicle flies along a circle facing the center of the circle. The user can input the velocity along the circle and change the radius. If no input is given the vehicle will hold position.
     * PARAM 1 : Radius of desired circle in CIRCLE_MODE
     * PARAM 2 : User defined
     * PARAM 3 : User defined
     * PARAM 4 : User defined
     * PARAM 5 : Unscaled target latitude of center of circle in CIRCLE_MODE
     * PARAM 6 : Unscaled target longitude of center of circle in CIRCLE_MODE
     */
    public final static int MAV_CMD_SET_GUIDED_SUBMODE_CIRCLE = 4001;
    /**
     * Delay mission state machine until gate has been reached.
     * PARAM 1 : Geometry: 0: orthogonal to path between previous and next waypoint.
     * PARAM 2 : Altitude: 0: ignore altitude
     * PARAM 3 : Empty
     * PARAM 4 : Empty
     * PARAM 5 : Latitude
     * PARAM 6 : Longitude
     * PARAM 7 : Altitude
     */
    public final static int MAV_CMD_CONDITION_GATE = 4501;
    /**
     * Fence return point. There can only be one fence return point.
     * PARAM 1 : Reserved
     * PARAM 2 : Reserved
     * PARAM 3 : Reserved
     * PARAM 4 : Reserved
     * PARAM 5 : Latitude
     * PARAM 6 : Longitude
     * PARAM 7 : Altitude
     */
    public final static int MAV_CMD_NAV_FENCE_RETURN_POINT = 5000;
    /**
     * Fence vertex for an inclusion polygon (the polygon must not be self-intersecting). The vehicle must stay within this area. Minimum of 3 vertices required.
     * PARAM 1 : Polygon vertex count
     * PARAM 2 : Reserved
     * PARAM 3 : Reserved
     * PARAM 4 : Reserved
     * PARAM 5 : Latitude
     * PARAM 6 : Longitude
     * PARAM 7 : Reserved
     */
    public final static int MAV_CMD_NAV_FENCE_POLYGON_VERTEX_INCLUSION = 5001;
    /**
     * Fence vertex for an exclusion polygon (the polygon must not be self-intersecting). The vehicle must stay outside this area. Minimum of 3 vertices required.
     * PARAM 1 : Polygon vertex count
     * PARAM 2 : Reserved
     * PARAM 3 : Reserved
     * PARAM 4 : Reserved
     * PARAM 5 : Latitude
     * PARAM 6 : Longitude
     * PARAM 7 : Reserved
     */
    public final static int MAV_CMD_NAV_FENCE_POLYGON_VERTEX_EXCLUSION = 5002;
    /**
     * Circular fence area. The vehicle must stay inside this area.
     * PARAM 1 : radius in meters
     * PARAM 2 : Reserved
     * PARAM 3 : Reserved
     * PARAM 4 : Reserved
     * PARAM 5 : Latitude
     * PARAM 6 : Longitude
     * PARAM 7 : Reserved
     */
    public final static int MAV_CMD_NAV_FENCE_CIRCLE_INCLUSION = 5003;
    /**
     * Circular fence area. The vehicle must stay outside this area.
     * PARAM 1 : radius in meters
     * PARAM 2 : Reserved
     * PARAM 3 : Reserved
     * PARAM 4 : Reserved
     * PARAM 5 : Latitude
     * PARAM 6 : Longitude
     * PARAM 7 : Reserved
     */
    public final static int MAV_CMD_NAV_FENCE_CIRCLE_EXCLUSION = 5004;
    /**
     * Rally point. You can have multiple rally points defined.
     * PARAM 1 : Reserved
     * PARAM 2 : Reserved
     * PARAM 3 : Reserved
     * PARAM 4 : Reserved
     * PARAM 5 : Latitude
     * PARAM 6 : Longitude
     * PARAM 7 : Altitude
     */
    public final static int MAV_CMD_NAV_RALLY_POINT = 5100;
    /**
     * Commands the vehicle to respond with a sequence of messages UAVCAN_NODE_INFO, one message per every UAVCAN node that is online. Note that some of the response messages can be lost, which the receiver can detect easily by checking whether every received UAVCAN_NODE_STATUS has a matching message UAVCAN_NODE_INFO received earlier; if not, this command should be sent again in order to request re-transmission of the node information messages.
     * PARAM 1 : Reserved (set to 0)
     * PARAM 2 : Reserved (set to 0)
     * PARAM 3 : Reserved (set to 0)
     * PARAM 4 : Reserved (set to 0)
     * PARAM 5 : Reserved (set to 0)
     * PARAM 6 : Reserved (set to 0)
     * PARAM 7 : Reserved (set to 0)
     */
    public final static int MAV_CMD_UAVCAN_GET_NODE_INFO = 5200;
    /**
     * Deploy payload on a Lat / Lon / Alt position. This includes the navigation to reach the required release position and velocity.
     * PARAM 1 : Operation mode. 0: prepare single payload deploy (overwriting previous requests), but do not execute it. 1: execute payload deploy immediately (rejecting further deploy commands during execution, but allowing abort). 2: add payload deploy to existing deployment list.
     * PARAM 2 : Desired approach vector in degrees compass heading (0..360). A negative value indicates the system can define the approach vector at will.
     * PARAM 3 : Desired ground speed at release time. This can be overridden by the airframe in case it needs to meet minimum airspeed. A negative value indicates the system can define the ground speed at will.
     * PARAM 4 : Minimum altitude clearance to the release position in meters. A negative value indicates the system can define the clearance at will.
     * PARAM 5 : Latitude unscaled for MISSION_ITEM or in 1e7 degrees for MISSION_ITEM_INT
     * PARAM 6 : Longitude unscaled for MISSION_ITEM or in 1e7 degrees for MISSION_ITEM_INT
     * PARAM 7 : Altitude (AMSL), in meters
     */
    public final static int MAV_CMD_PAYLOAD_PREPARE_DEPLOY = 30001;
    /**
     * Control the payload deployment.
     * PARAM 1 : Operation mode. 0: Abort deployment, continue normal mission. 1: switch to payload deployment mode. 100: delete first payload deployment request. 101: delete all payload deployment requests.
     * PARAM 2 : Reserved
     * PARAM 3 : Reserved
     * PARAM 4 : Reserved
     * PARAM 5 : Reserved
     * PARAM 6 : Reserved
     * PARAM 7 : Reserved
     */
    public final static int MAV_CMD_PAYLOAD_CONTROL_DEPLOY = 30002;
    /**
     * User defined waypoint item. Ground Station will show the Vehicle as flying through this item.
     * PARAM 1 : User defined
     * PARAM 2 : User defined
     * PARAM 3 : User defined
     * PARAM 4 : User defined
     * PARAM 5 : Latitude unscaled
     * PARAM 6 : Longitude unscaled
     * PARAM 7 : Altitude (AMSL), in meters
     */
    public final static int MAV_CMD_WAYPOINT_USER_1 = 31000;
    /**
     * User defined waypoint item. Ground Station will show the Vehicle as flying through this item.
     * PARAM 1 : User defined
     * PARAM 2 : User defined
     * PARAM 3 : User defined
     * PARAM 4 : User defined
     * PARAM 5 : Latitude unscaled
     * PARAM 6 : Longitude unscaled
     * PARAM 7 : Altitude (AMSL), in meters
     */
    public final static int MAV_CMD_WAYPOINT_USER_2 = 31001;
    /**
     * User defined waypoint item. Ground Station will show the Vehicle as flying through this item.
     * PARAM 1 : User defined
     * PARAM 2 : User defined
     * PARAM 3 : User defined
     * PARAM 4 : User defined
     * PARAM 5 : Latitude unscaled
     * PARAM 6 : Longitude unscaled
     * PARAM 7 : Altitude (AMSL), in meters
     */
    public final static int MAV_CMD_WAYPOINT_USER_3 = 31002;
    /**
     * User defined waypoint item. Ground Station will show the Vehicle as flying through this item.
     * PARAM 1 : User defined
     * PARAM 2 : User defined
     * PARAM 3 : User defined
     * PARAM 4 : User defined
     * PARAM 5 : Latitude unscaled
     * PARAM 6 : Longitude unscaled
     * PARAM 7 : Altitude (AMSL), in meters
     */
    public final static int MAV_CMD_WAYPOINT_USER_4 = 31003;
    /**
     * User defined waypoint item. Ground Station will show the Vehicle as flying through this item.
     * PARAM 1 : User defined
     * PARAM 2 : User defined
     * PARAM 3 : User defined
     * PARAM 4 : User defined
     * PARAM 5 : Latitude unscaled
     * PARAM 6 : Longitude unscaled
     * PARAM 7 : Altitude (AMSL), in meters
     */
    public final static int MAV_CMD_WAYPOINT_USER_5 = 31004;
    /**
     * User defined spatial item. Ground Station will not show the Vehicle as flying through this item. Example: ROI item.
     * PARAM 1 : User defined
     * PARAM 2 : User defined
     * PARAM 3 : User defined
     * PARAM 4 : User defined
     * PARAM 5 : Latitude unscaled
     * PARAM 6 : Longitude unscaled
     * PARAM 7 : Altitude (AMSL), in meters
     */
    public final static int MAV_CMD_SPATIAL_USER_1 = 31005;
    /**
     * User defined spatial item. Ground Station will not show the Vehicle as flying through this item. Example: ROI item.
     * PARAM 1 : User defined
     * PARAM 2 : User defined
     * PARAM 3 : User defined
     * PARAM 4 : User defined
     * PARAM 5 : Latitude unscaled
     * PARAM 6 : Longitude unscaled
     * PARAM 7 : Altitude (AMSL), in meters
     */
    public final static int MAV_CMD_SPATIAL_USER_2 = 31006;
    /**
     * User defined spatial item. Ground Station will not show the Vehicle as flying through this item. Example: ROI item.
     * PARAM 1 : User defined
     * PARAM 2 : User defined
     * PARAM 3 : User defined
     * PARAM 4 : User defined
     * PARAM 5 : Latitude unscaled
     * PARAM 6 : Longitude unscaled
     * PARAM 7 : Altitude (AMSL), in meters
     */
    public final static int MAV_CMD_SPATIAL_USER_3 = 31007;
    /**
     * User defined spatial item. Ground Station will not show the Vehicle as flying through this item. Example: ROI item.
     * PARAM 1 : User defined
     * PARAM 2 : User defined
     * PARAM 3 : User defined
     * PARAM 4 : User defined
     * PARAM 5 : Latitude unscaled
     * PARAM 6 : Longitude unscaled
     * PARAM 7 : Altitude (AMSL), in meters
     */
    public final static int MAV_CMD_SPATIAL_USER_4 = 31008;
    /**
     * User defined spatial item. Ground Station will not show the Vehicle as flying through this item. Example: ROI item.
     * PARAM 1 : User defined
     * PARAM 2 : User defined
     * PARAM 3 : User defined
     * PARAM 4 : User defined
     * PARAM 5 : Latitude unscaled
     * PARAM 6 : Longitude unscaled
     * PARAM 7 : Altitude (AMSL), in meters
     */
    public final static int MAV_CMD_SPATIAL_USER_5 = 31009;
    /**
     * User defined command. Ground Station will not show the Vehicle as flying through this item. Example: MAV_CMD_DO_SET_PARAMETER item.
     * PARAM 1 : User defined
     * PARAM 2 : User defined
     * PARAM 3 : User defined
     * PARAM 4 : User defined
     * PARAM 5 : User defined
     * PARAM 6 : User defined
     * PARAM 7 : User defined
     */
    public final static int MAV_CMD_USER_1 = 31010;
    /**
     * User defined command. Ground Station will not show the Vehicle as flying through this item. Example: MAV_CMD_DO_SET_PARAMETER item.
     * PARAM 1 : User defined
     * PARAM 2 : User defined
     * PARAM 3 : User defined
     * PARAM 4 : User defined
     * PARAM 5 : User defined
     * PARAM 6 : User defined
     * PARAM 7 : User defined
     */
    public final static int MAV_CMD_USER_2 = 31011;
    /**
     * User defined command. Ground Station will not show the Vehicle as flying through this item. Example: MAV_CMD_DO_SET_PARAMETER item.
     * PARAM 1 : User defined
     * PARAM 2 : User defined
     * PARAM 3 : User defined
     * PARAM 4 : User defined
     * PARAM 5 : User defined
     * PARAM 6 : User defined
     * PARAM 7 : User defined
     */
    public final static int MAV_CMD_USER_3 = 31012;
    /**
     * User defined command. Ground Station will not show the Vehicle as flying through this item. Example: MAV_CMD_DO_SET_PARAMETER item.
     * PARAM 1 : User defined
     * PARAM 2 : User defined
     * PARAM 3 : User defined
     * PARAM 4 : User defined
     * PARAM 5 : User defined
     * PARAM 6 : User defined
     * PARAM 7 : User defined
     */
    public final static int MAV_CMD_USER_4 = 31013;
    /**
     * User defined command. Ground Station will not show the Vehicle as flying through this item. Example: MAV_CMD_DO_SET_PARAMETER item.
     * PARAM 1 : User defined
     * PARAM 2 : User defined
     * PARAM 3 : User defined
     * PARAM 4 : User defined
     * PARAM 5 : User defined
     * PARAM 6 : User defined
     * PARAM 7 : User defined
     */
    public final static int MAV_CMD_USER_5 = 31014;
}
