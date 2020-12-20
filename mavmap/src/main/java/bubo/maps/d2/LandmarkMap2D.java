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

package bubo.maps.d2;

import georegression.geometry.UtilPoint2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Rectangle2D_F64;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A map which specifies the location of landmarks
 *
 * @author Peter Abeles
 */
public class LandmarkMap2D implements Serializable {
	List<Point2D_F64> locations = new ArrayList<Point2D_F64>();

	public void add( double x , double y ) {
		locations.add( new Point2D_F64(x,y));
	}

	public int getTotal() {
		return locations.size();
	}

	public Point2D_F64 getLocation( int which ) {
		return locations.get(which);
	}

	public List<Point2D_F64> getLocations() {
		return locations;
	}

	public void setLocations(List<Point2D_F64> locations) {
		this.locations = locations;
	}

	public Rectangle2D_F64 computeBoundingRectangle() {
		return UtilPoint2D_F64.bounding(locations,(Rectangle2D_F64)null);
	}
}
