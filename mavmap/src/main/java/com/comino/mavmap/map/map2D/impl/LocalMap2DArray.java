/****************************************************************************
 *
 *   Copyright (c) 2017, 2019 Eike Mansfeld ecm@gmx.de. All rights reserved.
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

package com.comino.mavmap.map.map2D.impl;



import com.comino.mavcom.model.DataModel;
import com.comino.mavmap.map.map2D.ILocalMap;
import com.comino.mavmap.map.map2D.filter.ILocalMapFilter;
import com.comino.mavmap.utils.MSPArrayUtils;

import boofcv.struct.image.GrayU16;
import boofcv.struct.image.GrayU8;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F32;
import georegression.struct.point.Vector4D_F64;

public class LocalMap2DArray extends LocalMap2DBase implements ILocalMap {

	private static int        FILTER_SIZE_PX     = 2;

	private static final int  MAX_CERTAINITY     = 80;
	private static final int  CERTAINITY_INCR    = 20;


	private GrayU8          mapU8 = null;

	public LocalMap2DArray() {
		this(40.0f,0.05f,2.0f,2);
	}

	public LocalMap2DArray(DataModel model, float window, int certainity) {
		this(model.grid.getExtension(),model.grid.getResolution(),window,certainity);
		this.model = model;
	}

	public LocalMap2DArray(float diameter_m, float cell_size_m, float window_diameter_m, int threshold) {
		this(diameter_m, cell_size_m, window_diameter_m, diameter_m/2f, diameter_m/2f, threshold );
	}

	public LocalMap2DArray(float map_diameter_m, float cell_size_m, float window_diameter_m, float center_x_m, float center_y_m,  int threshold) {
		cell_size_mm = (int)(cell_size_m * 1000f);
		this.threshold = threshold;

		map_dimension  = (int)Math.floor(map_diameter_m / cell_size_m );
		map = new short[map_dimension][map_dimension];

		mapU8 = new GrayU8(map_dimension,map_dimension);

		window_dimension = (int)Math.floor(window_diameter_m / cell_size_m );
		window = new short[window_dimension][window_dimension];

		initWindowAngles();

		reset();

		this.center_x_mm = center_x_m * 1000f;
		this.center_y_mm = center_y_m * 1000f;

		System.out.println("LocalMap2DArray initialized with "+map_dimension+"x"+map_dimension+" map and "+window.length+"x"+window.length+" window cells. ");
		System.out.println(" and filter radius  "+FILTER_SIZE_PX*cell_size_mm+"mm");
	}

	public void setLocalPosition(Vector3D_F32 point) {
		local_x_mm = point.x *1000f + center_x_mm;;
		local_y_mm = point.y *1000f + center_y_mm;;
	}

	public boolean update(Vector3D_F32 point) {
		return set(point.x, point.y,CERTAINITY_INCR);
	}

	public boolean update(Point3D_F64 point) {
		return set((float)point.x, (float)point.y,CERTAINITY_INCR);
	}

	public boolean update(Point3D_F64 point, Vector4D_F64 pos) {
		return set((float)(point.x+pos.x), (float)(point.y+pos.y),CERTAINITY_INCR);
	}

	public boolean update(float lpos_x, float lpos_y, Point3D_F64 point) {
		return set(lpos_x+(float)point.x, lpos_y+(float)point.y,CERTAINITY_INCR);
	}


	public boolean update(float lpos_x, float lpos_y, Vector3D_F32 point) {
		return set(lpos_x+point.x, lpos_y+point.y,CERTAINITY_INCR);
	}

	public boolean merge(LocalMap2DArray m, float weight) {
		return true;
	}


	public boolean set(float xpos, float ypos, int value) {
		int x = (int)Math.floor((xpos*1000f+center_x_mm)/cell_size_mm);
		int y = (int)Math.floor((ypos*1000f+center_y_mm)/cell_size_mm);

		if(map[x][y] >= MAX_CERTAINITY )
			return true;


		draw_into_map(x, y, FILTER_SIZE_PX, value);
		return true;
	}


	@Override
	public GrayU16 getMap() {
		return MSPArrayUtils.convertToGrayU16(map, null);
	}


	private void draw_into_map(int xm, int ym, int radius, int value) {

		if (xm< 0 || xm >= map.length || ym < 0 || ym >= map.length)
			return;

		if(!set_map_point(xm,ym,value))
			return;
		if(radius == 0)
			return;

		int i=0; int y_old=0; int dr=0;
		int r = radius, x = -r, y = 0, err = 2-2*r;
		do {
			for(i=x;i<=0;i++) {
				if(y!=y_old) {
					dr = ( value - sqrt(y*y+i*i) * value / radius );
					dr = dr < 0 ? 0 : dr;
					if(dr!=0) {
						set_map_point(xm-i,ym+y,dr);
						set_map_point(xm-y,ym-i,dr);
						set_map_point(xm+i,ym-y,dr);
						set_map_point(xm+y,ym+i,dr);
					}
				}
			}
			y_old = y;
			r = err;
			if (r < y) err += ++y*2+1;            /* e_xy+e_y < 0 */
			if (r > x || err > y) err += ++x*2+1; /* e_xy+e_x > 0 or no 2nd y-step */
		} while (x <= 0);
	}

	private int sqrt(int n) {
		int sc, lc;
		if(n < 2) return 1;
		sc = sqrt( n >> 2) << 1;
		lc = sc + 1;
		if((lc*lc > n))
			return sc;
		else
			return lc;
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


	public static void main(String[] args) {
		LocalMap2DArray map = new LocalMap2DArray(11,1,2, 1);
		System.out.println(map);

	}


}
