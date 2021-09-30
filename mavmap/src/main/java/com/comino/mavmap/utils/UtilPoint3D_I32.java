package com.comino.mavmap.utils;



import java.util.List;

import georegression.struct.point.Point3D_I32;
import georegression.struct.shapes.Box3D_F32;
import georegression.struct.shapes.Box3D_I32;

/**
 *
 *
 */
public class UtilPoint3D_I32 {

	/**
	 * Euclidean distance between the two specified points
	 * @param x0 x-axis on Point 0
	 * @param y0 y-axis on Point 0
	 * @param z0 z-axis on Point 0
	 * @param x1 x-axis on Point 1
	 * @param y1 y-axis on Point 1
	 * @param z1 z-axis on Point 1
	 * @return Euclidean distance
	 */
	public static float distance( int x0 , int y0 , int z0 ,
			int x1 , int y1 , int z1) {
		return norm(x1-x0,y1-y0,z1-z0);
	}

	/**
	 * Euclidean distance squared between the two specified points
	 * @param x0 x-axis on Point 0
	 * @param y0 y-axis on Point 0
	 * @param z0 z-axis on Point 0
	 * @param x1 x-axis on Point 1
	 * @param y1 y-axis on Point 1
	 * @param z1 z-axis on Point 1
	 * @return Euclidean distance squared
	 */
	public static float distanceSq( int x0 , int y0 , int z0 ,
			int x1 , int y1 , int z1) {
		float dx = x1-x0;
		float dy = y1-y0;
		float dz = z1-z0;

		return dx*dx + dy*dy + dz*dz;
	}
	
	public static float distanceSq(Point3D_I32 p0, Point3D_I32 p1) {
		return distanceSq(p0.x,p0.y,p0.z, p1.x,p1.y,p1.z);
	}

	public static float norm( int x , int y , int z ) {
		return (float)Math.sqrt(x*x + y*y + z*z);
	}
	

	/**
	 * Finds the minimal volume {@link Box3D_F32} which contains all the points.
	 *
	 * @param points Input: List of points.
	 * @param bounding Output: Bounding box
	 */
	public static void boundingBox(List<Point3D_I32> points, Box3D_I32 bounding) {
		int minX=Integer.MAX_VALUE,maxX=-Integer.MAX_VALUE;
		int minY=Integer.MAX_VALUE,maxY=-Integer.MAX_VALUE;
		int minZ=Integer.MAX_VALUE,maxZ=-Integer.MAX_VALUE;

		for( int i = 0; i < points.size(); i++ ) {
			Point3D_I32 p = points.get(i);
			if( p.x < minX )
				minX = p.x;
			if( p.x > maxX )
				maxX = p.x;
			if( p.y < minY )
				minY = p.y;
			if( p.y > maxY )
				maxY = p.y;
			if( p.z < minZ )
				minZ = p.z;
			if( p.z > maxZ )
				maxZ = p.z;
		}

		bounding.p0.setTo(minX,minY,minZ);
		bounding.p1.setTo(maxX, maxY, maxZ);
	}
}
