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

package bubo.clouds.motion;

import bubo.clouds.fit.Lrf2dScanToScan;
import bubo.desc.sensors.lrf2d.Lrf2dParam;
import georegression.struct.se.Se2_F64;
import org.ddogleg.struct.CircularQueue;

/**
 * Selects key-frames intelligently from a rolling window to improve state estimation accuracy.  When a key-frame
 * is changed it back estimates from the current frame (which has an accurate state estimate) to older scans
 * which might not have an accurate estimate.  The number of scans in the history is dynamically selected. The
 * specific scan matching algorithm is provided externally.  The focus of this class is key-frame maintenance.
 *
 * Accuracy of scan-to-scan algorithms pose estimation depends on the magnitude of the true motion.  Smaller
 * the true motion, more the estimation noise will dominate.  However, if two scans are too different
 * then accuracy will suffer.  Thus pose should be estimated from two scans which are as different as possible
 * without them being too different.
 *
 * When a new key-frame is selected is determined using a heuristic based on the number of matched scan points.
 * If too many are matched then it is assumed that the scans are too similar and if too few are matched then
 * they are too different.
 *
 * @author Peter Abeles
 */
// TODO Detect if a scan match has happened, but is the geometry is so poor it's likely to diverge
public class Lrf2dMotionRollingKeyFrame {

	Lrf2dScanToScan estimator;
	Lrf2dParam param;

	CircularQueue<ScanInfo> history;

	boolean first;

	int maxHistory;
	double keyFraction = 0.75;
	int keyValidScans;

	Se2_F64 odomWorldToKey = new Se2_F64();
	Se2_F64 odomCurrToKey = new Se2_F64();
	Se2_F64 tmpCurrToWorld = new Se2_F64();

	boolean updateFromOdometry;

	public Lrf2dMotionRollingKeyFrame(Lrf2dScanToScan estimator, int maxHistory) {
		this.estimator = estimator;
		this.maxHistory = maxHistory;
	}

	public void init( final Lrf2dParam param ) {
		this.param = param;
		this.history = new CircularQueue<ScanInfo>(ScanInfo.class,maxHistory) {
			@Override
			protected ScanInfo createInstance() {
				return new ScanInfo(param.getNumberOfScans());
			}
		};
		estimator.setSensorParam(param);
		reset();
	}

	public void setKeyFraction(double keyFraction) {
		this.keyFraction = keyFraction;
	}

	public void reset() {
		first = true;
		history.reset();
	}

	public Se2_F64 getSensorToWorld() {
		return history.tail().sensorToWorld;
	}


	public void process( Se2_F64 odometrySensorToWorld , double[] scan ) {
		int totalValid = countValidScans(scan);

		updateFromOdometry = true;
		ScanInfo key = history.head();
		if( key == null ) {
			handleFirstScan(odometrySensorToWorld, scan, totalValid);
		} else if( !key.validScan ) {
			handleKeyNoScan(odometrySensorToWorld, scan, totalValid, key);
		} else {
			handleKeyWithScan(odometrySensorToWorld, scan, totalValid, key);
		}
	}

	/**
	 * Key-frame has a valid scan
	 */
	private void handleKeyWithScan(Se2_F64 odometrySensorToWorld, double[] scan, int totalValid, ScanInfo key) {
		if( totalValid >0 ) {
			key.odometrySensorToWorld.invert(odomWorldToKey);
			odometrySensorToWorld.concat(odomWorldToKey, odomCurrToKey);

			estimator.setSource(scan);
			if (!estimator.process(odomCurrToKey))
				throw new RuntimeException("Crap it failed.  I should something smart here");

			ScanInfo curr = history.grow();
			curr.init(scan, odometrySensorToWorld);

			Se2_F64 currToKey = estimator.getSourceToDestination();
			currToKey.concat(key.sensorToWorld, curr.sensorToWorld);

			// if too few scans are matched or there are too many new scans change the key-frame
			if (estimator.totalScansMatched() < keyValidScans * keyFraction ||
					totalValid > keyValidScans/keyFraction ) {
				while (history.size() > 1) {
					history.removeHead();
				}

				key = history.head();
				estimator.setDestination(key.scan);
				keyValidScans = totalValid;
			}
			updateFromOdometry = false;
		} else {
			ScanInfo curr = history.grow();
			curr.init(odometrySensorToWorld);

			// the sensor update is the previous sensor pose plus the change from odometry
			key.odometrySensorToWorld.invert(odomWorldToKey);
			odometrySensorToWorld.concat(odomWorldToKey, odomCurrToKey);
			odomCurrToKey.concat(key.sensorToWorld, curr.sensorToWorld);

			// discard its past history to make the current frame the key-frame
			while (history.size() > 1) {
				history.removeHead();
			}
		}
	}

	/**
	 * There is a a key-frame, but it doesn't have a valid scan
	 */
	private void handleKeyNoScan(Se2_F64 odometrySensorToWorld, double[] scan, int totalValid, ScanInfo key) {
		if( totalValid > 0 ) {
			// the current frame has valid data
			ScanInfo curr = history.grow();
			curr.init(scan, odometrySensorToWorld);

			// compute the current location using the last location from sensor data plus odometry
			key.odometrySensorToWorld.invert(odomWorldToKey);
			odometrySensorToWorld.concat(odomWorldToKey, odomCurrToKey);
			odomCurrToKey.concat(key.sensorToWorld, curr.sensorToWorld);

			estimator.setDestination(curr.scan);
			keyValidScans = totalValid;

			// discard its past history to make the current frame the key-frame
			while (history.size() > 1) {
				history.removeHead();
			}
		} else {
			// There is no new scan information, so just update odometry information
			key.odometrySensorToWorld.invert(odomWorldToKey);
			odometrySensorToWorld.concat(odomWorldToKey, odomCurrToKey);
			odomCurrToKey.concat(key.sensorToWorld, tmpCurrToWorld);
			key.sensorToWorld.set(tmpCurrToWorld);
			key.odometrySensorToWorld.set(odometrySensorToWorld);
		}
	}

	/**
	 * This is the very first scan which has been seen
	 */
	private void handleFirstScan(Se2_F64 odometrySensorToWorld, double[] scan, int totalValid) {
		ScanInfo key;
		key = history.grow();

		if( totalValid > 0 ) {
			// there is a sensor scan which can be matched.  Use odometry and save the scan
			key.init(scan,odometrySensorToWorld);
			key.sensorToWorld.set(odometrySensorToWorld);
			estimator.setDestination(key.scan);
			keyValidScans = totalValid;
		} else {
			// there is NO sensor scan which can be matched.  Just use odometry
			key.init(odometrySensorToWorld);
			key.sensorToWorld.set(odometrySensorToWorld);
		}
	}

	private int countValidScans( double scan[]) {
		int total = 0;
		for (int i = 0; i < param.getNumberOfScans(); i++) {
			if( param.isValidRange(scan[i]))
				total++;
		}
		return total;
	}

	public boolean isUpdateFromOdometry() {
		return updateFromOdometry;
	}

	/**
	 * Information on a scan.
	 */
	public static class ScanInfo
	{
		double scan[];
		Se2_F64 odometrySensorToWorld = new Se2_F64();
		Se2_F64 sensorToWorld = new Se2_F64();
		boolean validScan; // true if the scan

		public ScanInfo( int numScans ) {
			scan = new double[numScans];
		}

		public void init( double scan[] , Se2_F64 odometrySensorToWorld ) {
			set(scan);
			this.odometrySensorToWorld.set(odometrySensorToWorld);
			sensorToWorld.reset();
			validScan = true;
		}

		public void init( Se2_F64 odometrySensorToWorld ) {
			this.odometrySensorToWorld.set(odometrySensorToWorld);
			sensorToWorld.reset();
			validScan = false;
		}

		public void set( double scan[] ) {
			System.arraycopy(scan,0,this.scan,0,scan.length);
		}
	}
}
