package com.comino.msp.execution;

import georegression.struct.se.Se3_F32;

public interface IOffboardListener {

	public final static int  TYPE_ABORT               = 0;
	public final static int  TYPE_NEXT_TARGET_REACHED = 1;
	public final static int  TYPE_CONTINUOUS          = 2;
	public final static int  TYPE_LIST_COMPLETED      = 3;

	public void action( Se3_F32 current, float distance, int action_type);

}
