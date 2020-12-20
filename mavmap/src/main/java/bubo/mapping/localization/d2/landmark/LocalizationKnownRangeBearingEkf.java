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

package bubo.mapping.localization.d2.landmark;

import bubo.desc.sensors.landmark.RangeBearingMeasurement;
import bubo.desc.sensors.landmark.RangeBearingParam;
import bubo.filters.MultivariateGaussianDM;
import bubo.filters.ekf.EkfPredictor;
import bubo.filters.ekf.ExtendedKalmanFilter;
import bubo.mapping.models.sensor.ProjectorRangeBearing2D;
import bubo.maps.d2.LandmarkMap2D;
import georegression.metric.UtilAngle;
import georegression.struct.point.Point2D_F64;
import georegression.struct.se.Se2_F64;
import org.ejml.data.DMatrixRMaj;

/**
 * Straight forward implementation of localization using an EKF which assumes perfect association and a known set
 * landmarks with known perfect locations.
 *
 * @author Peter Abeles
 */
public class LocalizationKnownRangeBearingEkf<Control> extends ExtendedKalmanFilter<Control> {

	// known locations of landmarks
	private LandmarkMap2D map;

	// projector for range bearing measurements
	private ProjectorRangeBearing2D projector;

	// Robot's pose estimate
	private MultivariateGaussianDM state = new MultivariateGaussianDM(3);

	// storage for landmark observation
	private MultivariateGaussianDM measurement = new MultivariateGaussianDM(2);

	// storage for initial pose
	private Se2_F64 pose = new Se2_F64();

	public LocalizationKnownRangeBearingEkf(EkfPredictor<Control> predictor, RangeBearingParam param) {
		super(predictor, new ProjectorRangeBearing2D());

		if (predictor.getSystemSize() != 3)
			throw new IllegalArgumentException("Predictor: Expecting a state size of 3, (x,y,yaw)");

		projector = (ProjectorRangeBearing2D)super.getProjector();

		measurement.getCovariance().set(0,0,param.rangeSigma*param.rangeSigma);
		measurement.getCovariance().set(1,1,param.bearingSigma*param.bearingSigma);
	}

	public void setLandmarks(LandmarkMap2D map) {
		this.map = map;
	}

	/**
	 * Specifies the initial state of the robot and the confidence
	 *
	 * @param state Initial filter state
	 */
	public void setInitialState(MultivariateGaussianDM state) {
		this.state.set(state);
	}

	/**
	 * Predicts the robot's next pose and update its uncertainty.  Changes to the robot's control
	 * must be done to the predictor directly before invoking this function.
	 */
	public void predict( Control control ) {
		predict(state,control,-1);
	}

	/**
	 * Updates the filter given an observation of the specified landmark.
	 *
	 * @param obs Landmark observation
	 */
	public void update(RangeBearingMeasurement obs) {
		Point2D_F64 p = map.getLocation(obs.id);

		projector.setLandmarkLocation(p.x,p.y);

		// special kalman update
		projector.compute(state.getMean());
		DMatrixRMaj H = projector.getJacobianH();

		DMatrixRMaj x = state.getMean();
		DMatrixRMaj P = state.getCovariance();

		DMatrixRMaj R = measurement.getCovariance();

		// compute the residual while taking in account the non-linearities of the bearings measurement
		DMatrixRMaj z_hat = projector.getProjected();
		y.data[0] = obs.range - z_hat.get(0);
		y.data[1] = UtilAngle.minus(obs.bearing, z_hat.get(1));

//		System.out.println(" bearing residual "+y.data[1]);

		_updateCovariance(H, x, P, R);
	}

	/**
	 * The estimated robot pose
	 *
	 * @return Robot pose estimate
	 */
	public MultivariateGaussianDM getState() {
		return state;
	}

	public Se2_F64 getPose() {
		pose.T.x = state.x.data[0];
		pose.T.y = state.x.data[1];
		pose.setYaw(state.x.data[2]);
		return pose;
	}
}
