package com.comino.msp.execution.offboard;

import com.comino.msp.utils.struct.Polar4D_F32;

import georegression.struct.point.Point4D_F32;
import georegression.struct.point.Vector3D_F32;
import georegression.struct.point.Vector4D_F32;

public class SetPoint4D_F32 extends Vector4D_F32 {

	private static final long serialVersionUID = 5387076120108437275L;
	private long  delta_tms = 0;

	public SetPoint4D_F32() {
		super(); clear();
	}

	public SetPoint4D_F32(float x, float y, float z, float w) {
		super(x, y, z, w);
		delta_tms = -1;
	}

	public SetPoint4D_F32(Point4D_F32 a, Point4D_F32 b) {
		super(a, b);
		delta_tms = -1;
	}

	public void set(Vector3D_F32 s, float w) {
		this.set(s.x, s.y, s.z, w);
		delta_tms = -1;
	}

	public void set(Vector3D_F32 s, float w, long delta_ms) {
		this.set(s.x, s.y, s.z, w);
		delta_tms = delta_ms;
	}

	public long getTimestampDelta() {
		return delta_tms;
	}

	public long getPolar(Polar4D_F32 p) {
		p.set(this);
		return delta_tms;
	}

	public void clear() {
		this.set(Float.NaN, Float.NaN, Float.NaN, Float.NaN);
		this.delta_tms = -1;
	}


}
