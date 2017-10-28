package com.comino.msp.slam.mapping;

import georegression.struct.point.Vector3D_F32;

public interface IMSPMap {

	public boolean update(Vector3D_F32 point);
	public void reset();

}
