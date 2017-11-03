package com.comino.msp.slam.map;

import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F32;

public interface IMSPLocalMap {

	public boolean update(Vector3D_F32 point);
	public boolean update(Point3D_F64 point);
	public void reset();
	public void forget();
	public float nearestDistance(float lpos_y, float lpos_x);

}
