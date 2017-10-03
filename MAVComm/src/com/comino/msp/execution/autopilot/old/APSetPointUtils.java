package com.comino.msp.execution.autopilot.old;

import java.util.ArrayList;
import java.util.List;

import com.comino.msp.model.DataModel;
import com.comino.msp.utils.MSPMathUtils;

import georegression.struct.point.Vector3D_F32;

public class APSetPointUtils {

	public static APSetPoint interpolateLinear(float where, APSetPoint start, APSetPoint end, APSetPoint result) {
		result.position.x = where * ( end.position.x - start.position.x) + start.position.x;
		result.position.y = where * ( end.position.y - start.position.y) + start.position.y;
		result.position.z = where * ( end.position.z - start.position.z) + start.position.z;
		return result;
	}

	public static Vector3D_F32 determineSpeedVector(long total_time_ms, APSetPoint start, APSetPoint end) {
		Vector3D_F32 result = new Vector3D_F32();
		result.x = ( end.position.x - start.position.x) * 1000f / total_time_ms ;
		result.y = ( end.position.y - start.position.y) * 1000f / total_time_ms ;
		result.z = ( end.position.z - start.position.z) * 1000f / total_time_ms ;
		return result;
	}

	public static List<APSetPoint> interpolatePositionLinear(int steps, APSetPoint start, APSetPoint end) {
		List<APSetPoint> result = new ArrayList<APSetPoint>();
		for(int i = 1; i<= steps; i++)
			result.add(interpolateLinear(i/(float)steps, start, end, new APSetPoint()));
		return result;
	}

	public static List<APSetPoint> interpolateCombinedLinear(int steps, long total_time_ms, APSetPoint start, APSetPoint end) {

		Vector3D_F32 speed = determineSpeedVector(total_time_ms, start, end);
		Vector3D_F32 slow  = new Vector3D_F32(speed); slow.scale(0.2f);

		List<APSetPoint> result = new ArrayList<APSetPoint>();

		for(int i = 1; i< steps*4/7; i++)
			result.add(new APSetPoint(speed, total_time_ms / steps));

		for(int i = 1; i< steps*3/7; i++)
			result.add(new APSetPoint(slow,total_time_ms / steps));

		result.add(end.copy());
		return result;
	}

}
