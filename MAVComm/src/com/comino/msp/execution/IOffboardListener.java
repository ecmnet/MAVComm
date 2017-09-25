package com.comino.msp.execution;

import com.comino.msp.execution.offboard.APSetPoint;


public interface IOffboardListener {

	public final static int  TYPE_ABORT               = 0;
	public final static int  TYPE_NEXT_TARGET_REACHED = 1;
	public final static int  TYPE_CONTINUOUS          = 2;
	public final static int  TYPE_LIST_COMPLETED      = 3;

	public void action( APSetPoint current, float distance, int action_type);

}
