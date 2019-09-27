package com.comino.libs;

import com.comino.msp.utils.MSPMathUtils;

// Transferred to Java from https://github.com/PX4/Firmware/commit/4eb9c7d812c8ac78a6609057466135f47798e8c8


public class TrajMathLib {


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
		if(alpha == Math.PI)
			return d;
		return (float)Math.sqrt(accel * d * Math.abs(Math.tan(alpha / 2f)));
	}


	public static float getAvoidanceDistance( float speed, float mindistance_0ms, float mindistance_1ms ) {
		float val = mindistance_0ms + (speed *( mindistance_1ms-mindistance_0ms ));
		if ( val < 0 ) 	val = 0;
		return val;
	}



	public static void main(String[] args) {
		float alpha = MSPMathUtils.normAngle(MSPMathUtils.toRad(191));
		System.out.println(Math.tan(alpha / 2f));
		System.out.println(alpha);
	}

	public static float[] computeTrajectoryTimepoints(float amax, float vmax, float dist) {
		float[] t = new float[8];

		t[0] = 0;
		t[1] = ( amax - 0 ) / 1.0f;
		t[2] = ( 2 * amax * amax - 2 * vmax ) / ( - 2 * amax );
		t[3] = ( amax );

		float A = (- amax ) * ( amax * amax * amax * amax * amax );
		float B = 0;
		float C = 12 * ( - amax ) * vmax * vmax;
		float D = -12 * amax * vmax * vmax;
		float E = -24 * ( - amax ) * amax * dist;
		float F = 12 * ( - amax ) * amax * amax * vmax - 12 * ( amax * amax ) * amax * vmax;

		float J = - 24 * ( - amax ) * amax * vmax;

		t[4] = ( A + B + C + D + E + F ) / J ;
		t[5] = ( amax );
		t[6] = ( amax * amax + amax * amax - 2 * vmax ) / (- 2 * amax);
		t[7] = ( amax );


		return t;
	}

}
