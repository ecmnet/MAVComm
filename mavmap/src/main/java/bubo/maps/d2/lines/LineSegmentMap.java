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

package bubo.maps.d2.lines;

import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Rectangle2D_F64;

import java.util.ArrayList;
import java.util.List;

/**
 * 2D world in which the environmental constraints are described using line segments.
 *
 * @author Peter Abeles
 */
public class LineSegmentMap {
	public List<LineSegment2D_F64> lines = new ArrayList<LineSegment2D_F64>();

	public void add(  double x0, double y0, double x1, double y1 ) {
		lines.add( new LineSegment2D_F64(x0,y0,x1,y1));
	}

	public void add( LineSegment2D_F64 line ) {
		lines.add(line.copy());
	}

	public List<LineSegment2D_F64> getLines() {
		return lines;
	}

	public Rectangle2D_F64 computeBoundingRectangle() {
		double minX,minY,maxX,maxY;

		minX=minY=Double.MAX_VALUE;
		maxX=maxY=-Double.MAX_VALUE;

		for ( LineSegment2D_F64 l : lines ) {
			if( l.a.x < minX )
				minX = l.a.x;
			if( l.a.y < minY )
				minY = l.a.y;
			if( l.a.x > maxX )
				maxX = l.a.x;
			if( l.a.y > maxY )
				maxY = l.a.y;

			if( l.b.x < minX )
				minX = l.b.x;
			if( l.b.y < minY )
				minY = l.b.y;
			if( l.b.x > maxX )
				maxX = l.b.x;
			if( l.b.y > maxY )
				maxY = l.b.y;
		}

		return new Rectangle2D_F64(minX,minY,maxX,maxY);
	}

	public Point2D_F64 computeCenter() {
		double x = 0;
		double y = 0;

		for ( LineSegment2D_F64 l : lines ) {
			x += l.a.x;
			y += l.a.y;
			x += l.b.x;
			y += l.b.y;
		}

		x /= lines.size()*2;
		y /= lines.size()*2;

		return new Point2D_F64(x,y);
	}
}
