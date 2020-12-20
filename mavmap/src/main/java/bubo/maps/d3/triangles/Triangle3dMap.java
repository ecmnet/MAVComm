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

package bubo.maps.d3.triangles;

import georegression.geometry.UtilPoint3D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.shapes.Box3D_F64;
import georegression.struct.shapes.Triangle3D_F64;

import java.util.ArrayList;
import java.util.List;

/**
 * 3D world where environmental constraints are specified using 3D triangles.
 *
 * @author Peter Abeles
 */
public class Triangle3dMap {
	public List<Triangle3D_F64> triangles = new ArrayList<Triangle3D_F64>();

	public void addTriangle( Triangle3D_F64 triangle ) {
		triangles.add(triangle.copy());
	}

	public Box3D_F64 computeBoundingBox() {
		List<Point3D_F64> points = new ArrayList<Point3D_F64>();

		for( Triangle3D_F64 t : triangles ) {
			points.add(t.v0);
			points.add(t.v1);
			points.add(t.v2);
		}

		Box3D_F64 box = new Box3D_F64();
		UtilPoint3D_F64.boundingBox(points,box);
		return box;
	}
}
