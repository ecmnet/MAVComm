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

package bubo.simulation.d2;

import bubo.desc.sensors.landmark.RangeBearingMeasurement;
import bubo.desc.sensors.landmark.RangeBearingParam;
import bubo.desc.sensors.lrf2d.Lrf2dMeasurement;
import bubo.desc.sensors.lrf2d.Lrf2dParam;
import georegression.struct.se.Se2_F64;

/**
 * Provides access to simulated robot control and sensors.
 *
 * @author Peter Abeles
 */
public interface RobotInterface2D {

	/**
	 * All control should be done when this function is called.  This allows the output to be
	 * deterministic.
	 */
	public void doControl( long timeStamp );

	public void setControlListener( ControlListener2D listener );

	public void setIntrinsic(Se2_F64 ladarToRobot ,
							 Lrf2dParam paramLrf ,
							 RangeBearingParam paramRb );

	public void odometry( long timeStamp , Se2_F64 robotToWorld );

	public void ladar( long timeStamp , Lrf2dMeasurement measurement );

	public void rangeBearing( long timeStamp , RangeBearingMeasurement measurement );

	public boolean isDone();
}
