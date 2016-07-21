/**
 * Generated class : MOTOR_TEST_THROTTLE_TYPE
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;
/**
 * Interface MOTOR_TEST_THROTTLE_TYPE
 * 
 **/
public interface MOTOR_TEST_THROTTLE_TYPE {
    /**
     * throttle as a percentage from 0 ~ 100
     */
    public final static int MOTOR_TEST_THROTTLE_PERCENT = 0;
    /**
     * throttle as an absolute PWM value (normally in range of 1000~2000)
     */
    public final static int MOTOR_TEST_THROTTLE_PWM = 1;
    /**
     * throttle pass-through from pilot's transmitter
     */
    public final static int MOTOR_TEST_THROTTLE_PILOT = 2;
}
