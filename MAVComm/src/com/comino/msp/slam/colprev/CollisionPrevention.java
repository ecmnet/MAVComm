package com.comino.msp.slam.colprev;

import com.comino.msp.slam.map2D.ILocalMap;
import com.comino.msp.utils.MSPMathUtils;

import georegression.struct.point.Vector3D_F32;

public class CollisionPrevention {

	private static final int	ALPHA = 5;

	private ILocalMap 		map = null;

	private float[][]    	distance;
	private float[]  		polar;

	private int             threshold           	= 0;

	public CollisionPrevention(ILocalMap map, int threshold) {

		this.map       = map;
		this.polar     = new float[360 / ALPHA];
		this.threshold = threshold;

		int window_dim = map.getWindowDimension();

		this.distance  = new float[window_dim][window_dim];

		for (int y = 0; y < window_dim; y++) {
			for (int x = 0; x < window_dim; x++) {
				distance[x][y]  = (float)Math.sqrt((x - window_dim/2)*(x - window_dim/2)  +
						(y - window_dim/2)*(y - window_dim/2)) * map.getCellSize_mm();
			}
		}
	}

	public float[] update(Vector3D_F32 current) {

		int beta = 0; int ix;

		for(int i=0;i<polar.length;i++)
			polar[i] = Float.MAX_VALUE;

		map.processWindow(current.x, current.y);

		int window_center = map.getWindowDimension()/2;

		for (int y = 0; y < map.getWindowDimension(); y++) {
			for (int x = 0; x < map.getWindowDimension(); x++) {

				if(map.getWindowValue(x, y) <= threshold)
					continue;

				beta = (int)(MSPMathUtils.fromRad((float)Math.atan2((y - window_center), (x - window_center)))) % 360;
				if(beta < 0) beta += 360;
				ix = beta / ALPHA;

				if(distance[x][y] < polar[ix])
					polar[ix] = distance[x][y];

			}
		}
		return polar;
	}

	public float[] getPolarDistances() {
		return polar;
	}
}
