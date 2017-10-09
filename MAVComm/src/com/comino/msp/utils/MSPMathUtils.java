/****************************************************************************
 *
 *   Copyright (c) 2017 Eike Mansfeld ecm@gmx.de. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 ****************************************************************************/

package com.comino.msp.utils;

public class MSPMathUtils {

	public static final double  DBL_EPSILON 					= 2.2204460492503131e-16d;

	public static final double 	toRad   					  	= Math.PI / 180.0;
	public static final double 	fromRad 					  	= 180.0 / Math.PI ;

	private static final double 	CONSTANTS_RADIUS_OF_EARTH  	= 6371000.0;
	private static  Reference   	ref                          = new Reference();


	public static void map_projection_init(double lat0, double lon0) {
		ref.lat_rad = lat0 * toRad;
		ref.lon_rad = lon0 * toRad;
		ref.sin_lat = Math.sin(ref.lat_rad);
		ref.cos_lat = Math.sin(ref.lat_rad);
		ref.init    = true;
	}

	public static boolean map_projection_reproject(float x, float y, float z, double[] latlon) {
		if(!ref.init)
			return false;

		double x_rad = (double)x / CONSTANTS_RADIUS_OF_EARTH;
		double y_rad = (double)y / CONSTANTS_RADIUS_OF_EARTH;
		double c = Math.sqrt(x_rad * x_rad + y_rad * y_rad);
		double sin_c = Math.sin(c);
		double cos_c = Math.cos(c);

		double lat_rad;
		double lon_rad;

		if (Math.abs(c) > DBL_EPSILON) {
			lat_rad = Math.asin(cos_c * ref.sin_lat + (x_rad * sin_c * ref.cos_lat) / c);
			lon_rad = (ref.lon_rad + Math.atan2(y_rad * sin_c, c * ref.cos_lat * cos_c - x_rad * ref.sin_lat * sin_c));
		} else {
			lat_rad = ref.lat_rad;
			lon_rad = ref.lon_rad;
		}

		latlon[0] = lat_rad * fromRad;
		latlon[1] = lon_rad * fromRad;

		return true;
	}


	public static float getDistance(double lat1, double lon1, double lat2, double lon2) {


		float distance = (float) (
				Math.acos(Math.sin(lat1*toRad) * Math.sin(lat1*toRad)
						+ Math.cos(lat1*toRad) * Math.cos(lat2*toRad) *
						Math.cos((lon2 - lon1)*toRad)
						)
				* CONSTANTS_RADIUS_OF_EARTH );

		return distance;
	}

	public static float getLocalDistance(float x1, float y1, float z1, float x2, float y2, float z2 ) {
		return (float)Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1)+(z2-z1)*(z2-z1));
	}

	public static float[] eulerAnglesByQuaternion(float euler[], float[] q) {
		euler[0] = (float)Math.atan2(2.0 * (q[0] * q[1] + q[2] * q[3]), 1.0 - 2.0 * (q[1] * q[1] + q[2] * q[2]));
		euler[1] = (float)Math.asin(2 * (q[0] * q[2] - q[3] * q[1]));
		euler[2] = (float)Math.atan2(2.0 * (q[0] * q[3] + q[1] * q[2]), 1.0 - 2.0 * (q[2] * q[2] + q[3] * q[3]));
		return euler;
	}

	public static float fromRad(float radians) {
		return (float)(radians * fromRad ) % 360;
	}

	public static float toRad(float angle) {
		return (float)(angle * toRad);
	}

	public static float[] rotateRad(float[] rotated, float posx, float posy, float heading_rad) {
		rotated[0] =  posx * (float)Math.cos(heading_rad) + posy * (float)Math.sin(heading_rad);
		rotated[1] = -posx * (float)Math.sin(heading_rad) + posy * (float)Math.cos(heading_rad);
		return rotated;
	}

	private static class Reference {
		double lat_rad = 0;
		double lon_rad = 0;
		double sin_lat = 0;
		double cos_lat = 0;

		boolean init  = false;


	}
}


