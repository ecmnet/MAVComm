package com.comino.msp.utils;

import java.util.ArrayList;
import java.util.List;

import com.comino.msp.model.DataModel;

import georegression.geometry.ConvertRotation3D_F32;
import georegression.struct.EulerType;
import georegression.struct.point.Vector3D_F32;
import georegression.struct.point.Vector4D_F32;
import georegression.struct.se.Se3_F32;

public class MSP3DUtils {

	public static final int ROLL  = 0;
	public static final int PITCH = 1;
	public static final int YAW   = 2;

	public static float distance3D(Vector4D_F32 t, Vector4D_F32 c) {
		return (float)Math.sqrt((t.x-c.x)*(t.x-c.x) + (t.y-c.y)*(t.y-c.y) + (t.z-c.z)*(t.z-c.z));
	}

	public static float distance3D(Vector3D_F32 t, Vector3D_F32 c) {
		return (float)Math.sqrt((t.x-c.x)*(t.x-c.x) + (t.y-c.y)*(t.y-c.y) + (t.z-c.z)*(t.z-c.z));
	}

	public static float angleXY(Vector3D_F32 t, Vector3D_F32 c) {
		return (float)Math.atan2(t.y-c.y, t.x-c.x);
	}

	public static Vector4D_F32 getCurrentVector4D(DataModel model) {
		return new Vector4D_F32(model.state.l_x,model.state.l_y, model.state.l_z, model.attitude.y);
	}

	public static Vector3D_F32 convertTo3D(Vector4D_F32 v) {
		return new Vector3D_F32(v.x,v.y,v.z);
	}

	public static void convertModelToSe3_F32(DataModel model, Se3_F32 state) {
		convertToSe3_F32(model.state.l_x, model.state.l_y, model.state.l_z, model.attitude.r, model.attitude.p, model.attitude.y, state);
	}
	public static void convertModelXYToSe3_F32(DataModel model, Se3_F32 state) {
		convertToSe3_F32(model.state.l_x, model.state.l_y, model.state.l_z, 0, 0, model.attitude.y, state);
	}

	public static void convertToSe3_F32(float x, float y, float z, float r, float p, float yw, Se3_F32 state) {
		state.setTranslation(x, y, z);
		ConvertRotation3D_F32.eulerToMatrix(EulerType.XYZ, r, p, y, state.getRotation());
	}

	public static float ConvertSe3_F32ToYaw(Se3_F32 state) {
		float[] v = new float[3];
		return  ConvertSe3_F32ToYaw(state,v);
	}

	public static float ConvertSe3_F32ToYaw(Se3_F32 state, float[] v) {
		ConvertRotation3D_F32.matrixToEuler(state.R, EulerType.XYZ, v);
		return v[YAW];
	}

	public static float getXYDirection(Vector4D_F32 target,Vector4D_F32 current) {

		float dx = target.getX() - current.getX();
		float dy = target.getY() - current.getY();

		if((dx > 0 && dy > 0) || (dx > 0 && dy < 0))
		   return (float)Math.atan(dy/dx);
		if((dx < 0 && dy > 0) || (dx < 0 && dy < 0))
			return (float)(Math.atan(dy/dx)+Math.PI);

		return 0;
	}

	public static Vector3D_F32 interpolateLinear(float where, Vector3D_F32 start, Vector3D_F32 end, Vector3D_F32 result) {
		result.x = where * ( end.x - start.x);
		result.y = where * ( end.y - start.y);
		result.z = where * ( end.z - start.z);
		return result;
	}

	public List<Vector3D_F32> interpolateLinear (int steps,Vector3D_F32 start, Vector3D_F32 end) {
		List<Vector3D_F32> result = new ArrayList<Vector3D_F32>();
		float delta = end.distance(start) / steps;
		for(int i = 1; i< steps; i++)
			result.add(interpolateLinear(i*delta, start, end, new Vector3D_F32()));
		return result;
	}

}
