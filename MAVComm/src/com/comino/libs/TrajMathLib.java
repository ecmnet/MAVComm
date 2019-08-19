package com.comino.libs;

     // Transferred to Java from https://github.com/PX4/Firmware/commit/4eb9c7d812c8ac78a6609057466135f47798e8c8


public class TrajMathLib {


	/* Compute the maximum possible speed on the track given the  distance,
	 * the maximum acceleration and the maximum jerk.
	 * We assume a constant acceleration profile with a delay of 2*accel/jerk
	 * (time to reach the desired acceleration from opposite max acceleration)
	 * Equation to solve: 0 = vel^2 - 2*accel*(x - vel*2*accel/jerk)
	 *
	 * @param jerk maximum jerk
	 * @param accel maximum acceleration
	 * @param speed maximim horizontal speed
	 * @param distance distance to the desired wp
	 *
	 * @return maximum speed
	 */

	public static float computeSpeedFromDistance(float jerk, float accel, float max_speed, float distance) {
		float b = 4 * accel * accel / jerk;
		float c = - 2 * accel * distance;
		return  Math.min(0.5f * (-b + (float)Math.sqrt(b * b - 4 * c)), max_speed * distance);
	}

	/* Compute the maximum tangential speed in a circle defined by two line segments of length "d"
	 * forming a V shape, opened by an angle "alpha". The circle is tangent to the end of the
	 * two segments as shown below:
	 *      \\
	 *      | \ d
	 *      /  \
	 *  __='___a\
	 *      d
	 *  @param alpha angle between the two line segments
	 *  @param accel maximum lateral acceleration
	 *  @param d length of the two line segments
	 *
	 *  @return maximum tangential speed
	 */

	public static float computeMaxSpeedInWaypoint(float alpha, float accel, float d) {
		return (float)Math.sqrt(accel * d * Math.tan(alpha / 2f));
	}

}
