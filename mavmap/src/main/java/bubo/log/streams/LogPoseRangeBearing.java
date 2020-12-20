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

package bubo.log.streams;

import georegression.struct.se.Se2_F64;

/**
 * @author Peter Abeles
 */
public class LogPoseRangeBearing {
	long time;
	Se2_F64 pose = new Se2_F64();
	int id;
	double range;
	double bearing;

	public void setX( double x ) {
		pose.T.x = x;
	}

	public void setY( double y ) {
		pose.T.y = y;
	}

	public void setYaw( double yaw ) {
		pose.setYaw(yaw);
	}

	public double getX() {
		return pose.getX();
	}

	public double getY() {
		return pose.getY();
	}

	public double getYaw() {
		return pose.getYaw();
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public double getBearing() {
		return bearing;
	}

	public void setBearing(double bearing) {
		this.bearing = bearing;
	}
}
