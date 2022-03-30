package com.comino.mavmap.struct;

import boofcv.struct.geo.Point2D3D;


import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point3D_F64;

/**
* Observed point feature location on the image plane and its 3D position and its extension in location coordinates.
*/
public class Point2D3DW extends Point2D3D {
	
	public double extension;

	public Point2D3DW() {
		super();
		this.extension = 0;
	}

	public Point2D3DW( Point2D_F64 observation, Point3D_F64 location, double extension ) {
		super(observation,location);
		this.extension = extension;
	}

	/**
	 * Sets 'this' to be identical to 'src'.
	 */
	public void setTo( Point2D3DW src ) {
		observation.setTo(src.observation);
		location.setTo(src.location);
		extension = src.extension;
	}

	
	public void setTo( double x2, double y2, double x3, double y3, double z3, double extension ) {
		observation.setTo(x2, y2);
		location.setTo(x3, y3, z3);
		this.extension = extension;
	}

	public Point2D3DW copy() {
		return new Point2D3DW(observation.copy(), location.copy(), extension);
	}
}
