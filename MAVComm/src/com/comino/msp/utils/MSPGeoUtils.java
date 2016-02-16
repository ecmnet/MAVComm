package com.comino.msp.utils;

public class MSPGeoUtils {

	public static final double toRad   = Math.PI / 180.0;
	public static final double fromRad = 180.0 / Math.PI ;
	private static final double CONSTANTS_RADIUS_OF_EARTH  = 6371000.0;

	public static float getDistance(double lat1, double lon1, double lat2, double lon2) {


		float distance = (float) (
				Math.acos(Math.sin(lat1*toRad) * Math.sin(lat1*toRad)
						+ Math.cos(lat1*toRad) * Math.cos(lat2*toRad) *
						Math.cos((lon2 - lon1)*toRad)
						)
				* CONSTANTS_RADIUS_OF_EARTH );

		return distance;
	}

}
