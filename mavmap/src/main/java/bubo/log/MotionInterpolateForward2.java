/*
 * Copyright (c) 2013-2014, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Project BUBO.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bubo.log;

import georegression.struct.se.Se2_F64;
import georegression.transform.se.InterpolateLinearSe2_F64;

/**
 * Interpolates the pose at requested time.  All future requests must be >= the previous requests.
 *
 * @author Peter Abeles
 */
public class MotionInterpolateForward2 {
	// log where each element is monotonically increasing in time
	LogMotion2 log;

	// true if it is the first request
	boolean first = true;
	// the index closes to the previous request
	int index;
	// time of the previous request
	double prevTime = -Double.MAX_VALUE;

	/**
	 * Provides the log.  A reference is saved internally.  Time stamps in log must be monotonically increasing.
	 *
	 * @param log Set's the log.
	 */
	public void setLog( LogMotion2 log ) {
		this.log = log;
		this.first= true;
		this.prevTime = -Double.MAX_VALUE;
	}

	/**
	 * Interpolates the position from a log at a specific time.
	 * @param time (input) Requested time for robots pose
	 * @param pose (output) Interpolated pose
	 * @return true if the requested time is bounded by the log
	 */
	public boolean lookup( double time , Se2_F64 pose ) {
		if( prevTime > time )
			throw new IllegalArgumentException("Time must always be >= the previous time provided");

		if( first ) {
			index = bound(time);
			if( index == -1 )
				return false;
		} else {
			boolean found = false;
			for (int i = index+1; i < log.history.size; i++) {
				LogMotion2.Element m = log.getHistory().get(i);
				if( m.time > time ) {
					index = i-1;
					found = true;
				}
			}

			if( !found )
				return false;
		}

		interpolate(time, pose);

		return true;
	}

	/**
	 * Linearly interpolates the position
	 */
	protected void interpolate(double time, Se2_F64 pose) {
		LogMotion2.Element e0 = log.getHistory().get(index);
		LogMotion2.Element e1 = log.getHistory().get(index+1);

		double range = e1.time-e0.time;
		double where = (time-e0.time)/range;

		InterpolateLinearSe2_F64.interpolate(e0.motion,e1.motion,where,pose);
	}


	/**
	 * Performs a divide and conquer search to find two elements which bound it
	 */
	protected int bound( double time ) {
		int lower = 0;
		int upper = log.history.size-1;

		if( log.getHistory().size() < 2 )
			return -1;

		// make sure it is in bounds
		if( log.getHistory().get(lower).time > time )
			return -1;
		else if( log.getHistory().get(upper).time < time )
			return -1;

		while( upper-lower > 1 ) {
			int middle = (upper+lower)/2;
			LogMotion2.Element m = log.getHistory().get(middle);

			if( m.time < time ) {
				lower = middle;
			} else if( m.time > time ) {
				upper = middle;
			} else {
				return middle;
			}
		}
		return lower;
	}
}
