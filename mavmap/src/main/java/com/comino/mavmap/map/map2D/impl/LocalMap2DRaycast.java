/****************************************************************************
*
*   Copyright (c) 2020 Eike Mansfeld ecm@gmx.de. All rights reserved.
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
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F32;
import georegression.struct.point.Vector4D_F64;

public class LocalMap2DRaycast extends LocalMap2DBase implements ILocalMap {

	private static final int  FILTER_CYCLE_MS    = 1000;

	private static final int  MAX_CERTAINITY     = 500;
	private static final int  HYSTERESIS         =  10;
	private static final int  CERTAINITY_INCR    =  30;
	private static final int  CERTAINITY_DECR    = -10;


	public LocalMap2DRaycast() {
		this(40.0f,0.05f,2.0f,2);
	}

	public LocalMap2DRaycast(DataModel model, float window, int certainity) {
		this(model.grid.getExtension(),model.grid.getResolution(),window,certainity);
		this.model = model;

	}

	public LocalMap2DRaycast(float diameter_m, float cell_size_m, float window_diameter_m, int threshold) {
		this(diameter_m, cell_size_m, window_diameter_m, diameter_m/2f, diameter_m/2f, threshold );
		this.model = new DataModel();
	}

	public LocalMap2DRaycast(float map_diameter_m, float cell_size_m, float window_diameter_m, float center_x_m, float center_y_m,  int threshold) {
		cell_size_mm = (int)(cell_size_m * 1000f);
		this.threshold = threshold;

		map_dimension  = (int)Math.floor(map_diameter_m / cell_size_m );
		map = new short[map_dimension][map_dimension];

		window_dimension = (int)Math.floor(window_diameter_m / cell_size_m );
		window = new short[window_dimension][window_dimension];

		initWindowAngles();

		reset();

		this.center_x_mm = center_x_m * 1000f;
		this.center_y_mm = center_y_m * 1000f;

		System.out.println("LocalMap2DRayCast initialized with "+map_dimension+"x"+map_dimension+" map and "+window.length+"x"+window.length+" window cells. ");
	}


	public void setLocalPosition(Vector3D_F32 point) {
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
		return set(pos.x,pos.y, point.x,point.y) ;
	}


	public boolean update(float lpos_x, float lpos_y, Vector3D_F32 point) {
		return set(lpos_x,lpos_y, point.x,point.y) ;
	}

	public boolean update(float lpos_x, float lpos_y, Point3D_F64 point) {
		return set(lpos_x,lpos_y, point.x,point.y) ;
	}

	public boolean merge(LocalMap2DRaycast m, float weight) {
		return true;
	}


	public boolean set(double xpos1, double ypos1, double xpos2, double ypos2) {

		int x1 = (int)Math.floor((xpos1*1000f+center_x_mm)/cell_size_mm);
		int y1 = (int)Math.floor((ypos1*1000f+center_y_mm)/cell_size_mm);
		int x2 = (int)Math.floor((xpos2*1000f+center_x_mm)/cell_size_mm);
		int y2 = (int)Math.floor((ypos2*1000f+center_y_mm)/cell_size_mm);

		return set(x1,y1,x2,y2);
	}


	@Override
	public GrayU16 getMap() {
		return MSPArrayUtils.convertToGrayU16(map, null);
	}

	private boolean set(int x1, int y1, int x2, int y2) {

//		int x3 = -1;
//		int y3 = -1;
//		float m=0;

//		if(get(x2,y2)>= MAX_CERTAINITY)
//			return false;

//		float dx = (int)(x2-x1); float dy = (int)(y2-y1);

//		System.out.println("dx="+dx+"/dy="+dy);
//		if(dx == 0) {
//			x3 = x2;
//			if(dy >= 0)
//				y3 = map_dimension - 2;
//			else
//				y3 = 0;
//
//		} else
//			if(dy == 0) {
//				y3 = y2;
//				if(dx >= 0)
//					x3 = map_dimension - 2;
//				else
//					x3 = 0;
//
//			} else {
//
//				 m = dy/dx;
//
//				if(dx > 0 && dy > 0) {
//
//
//					if(m < 1f) {
//
//						x3 = map_dimension - 1;
//						y3 = (int)(y1 + (map_dimension-y1) * m);
//
//					} else {
//						y3 = map_dimension - 1;
//						x3 = (int)(x1 + (map_dimension-x1) / m);
//
//					}
//
//				}
//				else
//					if(dx > 0 && dy < 0) {
//
//						if(m < -1f) {
//
//							x3 = map_dimension - 1;
//							y3 = (int)(y1 + (map_dimension-y1) * m);
//
//						} else {
//							x3 = map_dimension - 1;
//							y3 = (int)(y1 + (map_dimension-y1) * m);
//
//						}
//
//
//
//					} else
//						if(dx < 0 && dy > 0) {
//
//							if(m < -1f) {
//
//								x3 = 0;
//								y3 = (int)(y1 - (map_dimension-y1) * m);
//
//							} else {
//								x3 = 0;
//								y3 = (int)(y1 - (map_dimension-y1) * m);
//
//							}
//
//
//						}
//						else
//
//							if(m < 1f) {
//								x3 =0;
//								y3 = (int)(y1 - (map_dimension-y1) * m);
//
//							} else {
//								y3 = 0;
//								x3 = (int)(x1 - (map_dimension-x1) / m);
//
//							}
//			}


		// remove hidden obstacles

	//	drawLine(x2,y2,x3,y3);
		drawLine(x1,y1,x2,y2);


		return true;
	}


	private void drawLine(int x1, int y1, int x2, int y2) {
		// delta of exact value and rounded value of the dependent variable
		int d = 0;

		int dx = Math.abs(x2 - x1);
		int dy = Math.abs(y2 - y1);

		int dx2 = 2 * dx; // slope scaling factors to
		int dy2 = 2 * dy; // avoid floating point

		int ix = x1 < x2 ? 1 : -1; // increment direction
		int iy = y1 < y2 ? 1 : -1;

		int x = x1;
		int y = y1;

		if (dx >= dy) {
			while (true) {
				draw_into_map(x,y,CERTAINITY_DECR);
				if (x == x2)
					break;
				x += ix;
				d += dy2;
				if (d > dx) {
					y += iy;
					d -= dx2;
				}
			}
		} else {
			while (true) {
				draw_into_map(x,y,CERTAINITY_DECR);
				if (y == y2)
					break;
				y += iy;
				d += dx2;
				if (d > dy) {
					x += ix;
					d -= dy2;
				}
			}
		}
		draw_into_map(x,y,CERTAINITY_INCR);
	}



	private void draw_into_map(int xm, int ym, int value) {

		if (xm< 0 || xm >= map.length || ym < 0 || ym >= map.length)
			return;

		if(value != 0)
			set_map_point(xm,ym,value);
		else
			clear_map_point(xm,ym);

	}

	private boolean set_map_point(int x,int y, int dr) {
		if(x >=0 && y>=0 && x < map.length && y < map.length) {
				map[x][y] += (short)dr;
				if(map[x][y]<0) map[x][y] =0;
				if(map[x][y]>MAX_CERTAINITY) map[x][y] = MAX_CERTAINITY;
				if(map[x][y] > threshold+HYSTERESIS)
					model.grid.setBlock((x*cell_size_mm-center_x_mm)/1000f,(y*cell_size_mm-center_y_mm)/1000f, 0, true);
				if(map[x][y] < threshold-HYSTERESIS)
					model.grid.setBlock((x*cell_size_mm-center_x_mm)/1000f,(y*cell_size_mm-center_y_mm)/1000f, 0, false);
				return true;
		}
		return false;
	}

	private boolean clear_map_point(int x,int y) {
		if(x >=0 && y>=0 && x < map.length && y < map.length) {
			if(map[x][y] > threshold) {
				map[x][y] = 0;
				model.grid.setBlock((x*cell_size_mm-center_x_mm)/1000f,(y*cell_size_mm-center_y_mm)/1000f, 0, false);
			}

			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		LocalMap2DRaycast map = new LocalMap2DRaycast(40,1,2,1);


	//  map.set(10, 10, 15, 13, 10);

	    map.set(20, 20, 17, 30);


		System.out.println(map);



	}

	long tms;

	@Override
	public void applyMapFilter(ILocalMapFilter filter) {

		if((System.currentTimeMillis() - tms) < FILTER_CYCLE_MS)
			return;

		filter.apply(map);

		for (int y = 0; y <map_dimension; y++) {
			for (int x = 0; x < map_dimension; x++) {
				if(map[x][y] == 0)
				  continue;

				if(map[x][y] > threshold)
					model.grid.setBlock((x*cell_size_mm-center_x_mm)/1000f,(y*cell_size_mm-center_y_mm)/1000f, 0, true);
				else
					model.grid.setBlock((x*cell_size_mm-center_x_mm)/1000f,(y*cell_size_mm-center_y_mm)/1000f, 0, false);
			}
		}

		tms = System.currentTimeMillis();

	}
}
