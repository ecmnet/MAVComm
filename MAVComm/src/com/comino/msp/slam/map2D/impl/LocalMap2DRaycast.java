/****************************************************************************
 *
 *   Copyright (c) 2017,2018 Eike Mansfeld ecm@gmx.de. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 ****************************************************************************/

package com.comino.msp.slam.map2D.impl;

import com.comino.msp.model.DataModel;
import com.comino.msp.slam.map2D.ILocalMap;
import com.comino.msp.slam.map2D.filter.ILocalMapFilter;
import com.comino.msp.utils.MSPArrayUtils;

import boofcv.struct.image.GrayU16;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F32;
import georegression.struct.point.Vector4D_F64;

public class LocalMap2DRaycast extends LocalMap2DBase implements ILocalMap {

	private static final int  MAX_CERTAINITY     = 600;
	private static final int  CERTAINITY_INCR    = 20;


	public LocalMap2DRaycast() {
		this(40.0f,0.05f,2.0f,2);
	}

	public LocalMap2DRaycast(DataModel model, float window, int certainity) {
		this(model.grid.getExtension(),model.grid.getResolution(),window,certainity);
		this.model = model;

	}

	public LocalMap2DRaycast(float diameter_m, float cell_size_m, float window_diameter_m, int threshold) {
		this(diameter_m, cell_size_m, window_diameter_m, diameter_m/2f, diameter_m/2f, threshold );
	}

	public LocalMap2DRaycast(float map_diameter_m, float cell_size_m, float window_diameter_m, float center_x_m, float center_y_m,  int threshold) {
		cell_size_mm = (int)(cell_size_m * 1000f);
		this.threshold = threshold;

		map_dimension  = (int)Math.floor(map_diameter_m / cell_size_m );
		map = new short[map_dimension][map_dimension];

		window_dimension = (int)Math.floor(window_diameter_m / cell_size_m );
		window = new short[window_dimension][window_dimension];

		reset();

		this.center_x_mm = center_x_m * 1000f;
		this.center_y_mm = center_y_m * 1000f;

		System.out.println("LocalMap2DRayCast initialized with "+map_dimension+"x"+map_dimension+" map and "+window.length+"x"+window.length+" window cells. ");
	}


	public void 	setLocalPosition(Vector3D_F32 point) {
		local_x_mm = point.x *1000f + center_x_mm;;
		local_y_mm = point.y *1000f + center_y_mm;;
	}

	public boolean update(Vector3D_F32 point) {
		return false;
	}

	public boolean update(Point3D_F64 point) {
		return false;
	}

	public boolean update(Point3D_F64 point, Vector4D_F64 pos) {
		return set(pos.x,pos.y, point.x,point.y,CERTAINITY_INCR) ;
	}


	public boolean update(float lpos_x, float lpos_y, Vector3D_F32 point) {
		return set(lpos_x,lpos_y, point.x,point.y,CERTAINITY_INCR) ;
	}

	public boolean update(float lpos_x, float lpos_y, Point3D_F64 point) {
		return set(lpos_x,lpos_y, point.x,point.y,CERTAINITY_INCR) ;
	}

	public boolean merge(LocalMap2DRaycast m, float weight) {
		return true;
	}


	public boolean set(double xpos1, double ypos1, double xpos2, double ypos2, int value) {
		int x1 = (int)Math.floor((xpos1*1000f+center_x_mm)/cell_size_mm);
		int y1 = (int)Math.floor((ypos1*1000f+center_y_mm)/cell_size_mm);
		int x2 = (int)Math.floor((xpos2*1000f+center_x_mm)/cell_size_mm);
		int y2 = (int)Math.floor((ypos2*1000f+center_y_mm)/cell_size_mm);
		drawBresenhamLine(x1,y1,x2,y2,value);
		return true;
	}


	@Override
	public GrayU16 getMap() {
		return MSPArrayUtils.convertToGrayU16(map, null);
	}

	private void drawBresenhamLine( int x1, int y1, int x2, int y2, int value)
	{
		int xIncrement = 1,
				yIncrement = 1,
				dy = 2*(y2-y1),
				dx = 2*(x1-x2),
				tmp;

		if ( x1 > x2 ) {      // Spiegeln an Y-Achse
			xIncrement = -1;
			dx = -dx;
		}

		if ( y1 > y2 ) {      // Spiegeln an X-Achse
			yIncrement= -1;
			dy= -dy;
		}

		int e = 2*dy + dx;
		int x = x1;           // Startpunkte setzen
		int y = y1;

		if ( dy < -dx )       // Steigung < 1
		{
			while( x != (x2+1) )
			{
				e += dy;
				if ( e > 0)
				{
					e += dx;
					y += yIncrement;
				}
				draw_into_map(x,y,0);
				x += xIncrement;
			}
		}
		else // ( dy >= -dx )   Steigung >=1
		{
			tmp = -dx;
			dx = -dy;
			dy = tmp;

			e = 2*dy + dx;

			while( y != (y2+1) )
			{
				e += dy;
				if( e > 0 ) {
					e += dx;
					x += xIncrement;
				}
				draw_into_map(x,y,0);
				y += yIncrement;
			}
		}
		draw_into_map(x,y,value);
	}

	private void draw_into_map(int xm, int ym, int value) {

		if (xm< 0 || xm >= map.length || ym < 0 || ym >= map.length)
			return;

		if(value > 0)
			set_map_point(xm,ym,value);
		else
			clear_map_point(xm,ym);

	}

	private boolean set_map_point(int x,int y, int dr) {
		if(x >=0 && y>=0 && x < map.length && y < map.length) {
			if(map[x][y]<MAX_CERTAINITY) {
				map[x][y] +=dr;
				if(map[x][y] > threshold)
					model.grid.setBlock((x*cell_size_mm-center_x_mm)/1000f,(y*cell_size_mm-center_y_mm)/1000f, 0, true);
				return true;
			}
		}
		return false;
	}

	private boolean clear_map_point(int x,int y) {
		if(x >=0 && y>=0 && x < map.length && y < map.length) {
			map[x][y] = 0;
			model.grid.setBlock((x*cell_size_mm-center_x_mm)/1000f,(y*cell_size_mm-center_y_mm)/1000f, 0, false);
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		LocalMap2DRaycast map = new LocalMap2DRaycast(11,1,2, 1);
		System.out.println(map);

	}

	@Override
	public void applyMapFilter(ILocalMapFilter filter) {

		filter.apply(map);

		for (int y = 0; y <map_dimension; y++) {
			for (int x = 0; x < map_dimension; x++) {
				if(map[x][y] > threshold)
					model.grid.setBlock((x*cell_size_mm-center_x_mm)/1000f,(y*cell_size_mm-center_y_mm)/1000f, 0, true);
				else
					model.grid.setBlock((x*cell_size_mm-center_x_mm)/1000f,(y*cell_size_mm-center_y_mm)/1000f, 0, false);
			}
		}

	}
}
