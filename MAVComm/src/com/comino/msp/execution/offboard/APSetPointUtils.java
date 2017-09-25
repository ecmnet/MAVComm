package com.comino.msp.execution.offboard;

import java.util.ArrayList;
import java.util.List;

public class APSetPointUtils {

	public static APSetPoint interpolateLinear(float where, APSetPoint start, APSetPoint end, APSetPoint result) {
		result.position.x = where * ( end.position.x - start.position.x) + start.position.x;
		result.position.y = where * ( end.position.y - start.position.y) + start.position.y;
		result.position.z = where * ( end.position.z - start.position.z) + start.position.z;
		return result;
	}

	public static List<APSetPoint> interpolatePositionLinear(int steps, APSetPoint start, APSetPoint end) {
		List<APSetPoint> result = new ArrayList<APSetPoint>();
		for(int i = 1; i<= steps; i++)
			result.add(interpolateLinear(i/(float)steps, start, end, new APSetPoint()));
		return result;
	}

}
