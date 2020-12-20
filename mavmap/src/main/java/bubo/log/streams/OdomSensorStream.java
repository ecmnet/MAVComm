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


/**
 * Interface for providing a stream of sensor data with associated odometry data.  This type
 * of interface is often useful when building a mapping from a single sensor.  Typically data
 * will come from log file.
 *
 * @author Peter Abeles
 */
public interface OdomSensorStream<C, O, S> {

	/**
	 * <p>
	 * Get the robot and sensor configurations.
	 * </p>
	 *
	 * @return Senor and robot configuration.
	 */
	public C getConfiguration();

	/**
	 * <p>
	 * Checks to see if there is more data remaining.  If so the odometry and sensor
	 * data returned by {@link #getOdometry()} and {@link #getSensorObservation()} is updated
	 * and true returned.
	 * </p>
	 *
	 * @return If there is more data available to process.
	 */
	public boolean hasNext();

	/**
	 * <p>
	 * Vehicle's motion as estimated by odometry at the time when the new sensor data arrived.
	 * </p>
	 * <p>
	 * WARNING: The same variable instance might be returned each time, but its values changed.
	 * </p>
	 *
	 * @return Odometry data.
	 */
	public O getOdometry();

	/**
	 * <p> The latest sensor observations. </p>
	 * <p/>
	 * <p>
	 * WARNING: The same variable instance might be returned each time, but its values changed.
	 * </p>
	 *
	 * @return Sensor observation.
	 */
	public S getSensorObservation();

}
