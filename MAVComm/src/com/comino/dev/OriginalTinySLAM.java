/****************************************************************************
 *
 *   Copyright (c) 2017 Eike Mansfeld ecm@gmx.de. All rights reserved.
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

/* Based on tinySLAM: https://svn.openslam.org/data/svn/tinyslam/ */

package com.comino.dev;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.comino.msp.slam.map2D.ILocalMap;

import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F32;


public class OriginalTinySLAM {

	private final static int   INFINITY       = 2000000000;
	private final static short TS_NO_OBSTACLE = (short) 32760;
	private final static short TS_OBSTACLE 	 = (short) 0;

	private short[]			map 				= null;
	private ILocalMap		target_map  		= null;
	private int             diameter_mm		= 0;
	private int             cell_size_mm		= 0;

	private Vector3D_F32 	currentpos 		= new Vector3D_F32();
	private Vector3D_F32 	bestpos    		= new Vector3D_F32();
	private Vector3D_F32 	lastbestpos 		= new Vector3D_F32();
	private int 				bestdist 		= 0;

	private Ziggurat randomizer    = null;

	public OriginalTinySLAM(ILocalMap target_map) {
		this.target_map    = target_map;
		this.cell_size_mm  = target_map.getCellSize_mm();
		this.diameter_mm   = target_map.getMapDimension() *  cell_size_mm;
		this.map = new short[target_map.getMapDimension() * target_map.getMapDimension()];
		this.randomizer = new Ziggurat();
		init_map();
	}

	public void init_map() {
		Arrays.fill(map, (short)((TS_OBSTACLE + TS_NO_OBSTACLE) / 2));
	}


	public void update(List<Point3D_F64> p_list, Vector3D_F32 current_pos) {
            Vector3D_F32 guessed_pos = monte_carlo_search(p_list, current_pos, 0.1f , 0, 1000);
            System.out.println("Current: "+current_pos+" guessed: "+guessed_pos);
            update_map(p_list,guessed_pos, 50, 30);
            transfer_to_target_map();
	}

	private int distance_to_features(List<Point3D_F64> p_list) {
		int x,y; int sum = 0;

		if(p_list.isEmpty())
			return INFINITY;

		for(Point3D_F64 p : p_list) {
			x = (int)Math.floor((p.x*1000f+diameter_mm/2)/cell_size_mm);
			y = (int)Math.floor((p.y*1000f+diameter_mm/2)/cell_size_mm);
			if (x >= 0 && x < target_map.getMapDimension() && y >= 0 && y < target_map.getMapDimension()) {
				sum += map[y * target_map.getMapDimension() + x];
			}
		}
		return sum  / p_list.size();
	}

	private void update_map(List<Point3D_F64> p_list, Vector3D_F32 current_pos, int quality, int hole_width) {
		int xp,yp,x1,y1,x2,y2; float dist=0; float add = 0;

		x1 = (int)Math.floor((current_pos.x*1000f+diameter_mm/2)/cell_size_mm);
		y1 = (int)Math.floor((current_pos.y*1000f+diameter_mm/2)/cell_size_mm);

		for(Point3D_F64 p : p_list) {
			xp = (int)Math.floor((p.x*1000f+diameter_mm/2)/cell_size_mm);
			yp = (int)Math.floor((p.y*1000f+diameter_mm/2)/cell_size_mm);

			dist = (float)Math.hypot(xp-x1, yp-y1);
			add  = hole_width / 2f / dist;

			x2   = (int)Math.floor(((p.x - current_pos.x) * (1 + add) + current_pos.x ) * 1000f / cell_size_mm);
			y2   = (int)Math.floor(((p.y - current_pos.y) * (1 + add) + current_pos.y ) * 1000f / cell_size_mm);

			//TODO: Problem: We do only know obstacles, not no obstacles as with laser

			map2map(x1, y1, x2, y2, xp, yp, TS_OBSTACLE, quality);

		}
	}

	private void map2map(int x1, int y1, int x2, int y2, int xp, int yp, int value, int alpha) {

		int x2c, y2c, dx, dy, dxc, dyc, error, errorv, derrorv, x;
	    int incv, sincv, incerrorv, incptrx, incptry, pixval, horiz, diago;
	    int ptr;

	   // check if vehicle is out of map
	    if (x1 < 0 || x1 >= target_map.getMapDimension() || y1 < 0 || y1 >= target_map.getMapDimension())
	        return;

	    x2c = x2; y2c = y2;

	    // Clipping
	    if (x2c < 0) {
	        if (x2c == x1) return;
	        y2c += (y2c - y1) * (-x2c) / (x2c - x1);
	        x2c = 0;
	    }
	    if (x2c >= target_map.getMapDimension()) {
	        if (x1 == x2c) return;
	        y2c += (y2c - y1) * (target_map.getMapDimension() - 1 - x2c) / (x2c - x1);
	        x2c = target_map.getMapDimension() - 1;
	    }
	    if (y2c < 0) {
	        if (y1 == y2c) return;
	        x2c += (x1 - x2c) * (-y2c) / (y1 - y2c);
	        y2c = 0;
	    }
	    if (y2c >= target_map.getMapDimension()) {
	        if (y1 == y2c) return;
	        x2c += (x1 - x2c) * (target_map.getMapDimension() - 1 - y2c) / (y1 - y2c);
	        y2c = target_map.getMapDimension() - 1;
	    }

	    dx = Math.abs(x2 - x1); dy = Math.abs(y2 - y1);
	    dxc = Math.abs(x2c - x1); dyc = Math.abs(y2c - y1);
	    incptrx = (x2 > x1) ? 1 : -1;
	    incptry = (y2 > y1) ? target_map.getMapDimension() : -target_map.getMapDimension();
	    sincv = (value > TS_NO_OBSTACLE) ? 1 : -1;

	    if (dx > dy) {
	        derrorv = Math.abs(xp - x2);
	    } else {
	    	    dx ^= dy ^= dx ^= dy;
	    	    dxc ^= dyc ^= dxc ^= dyc;
	    	    incptrx ^= incptry ^= incptrx ^= incptry;
	        derrorv = Math.abs(yp - y2);
	    }

	    error = 2 * dyc - dxc;
	    horiz = 2 * dyc;
	    diago = 2 * (dyc - dxc);
	    errorv = derrorv / 2;
	    incv = (value - TS_NO_OBSTACLE) / derrorv;

	    incerrorv = value - TS_NO_OBSTACLE - derrorv * incv;
	    ptr = y1 * target_map.getMapDimension() + x1;

	    pixval = TS_NO_OBSTACLE;

	    for (x = 0; x <= dxc; x++, ptr += incptrx) {
	        if (x > dx - 2 * derrorv) {
	            if (x <= dx - derrorv) {
	                pixval += incv;
	                errorv += incerrorv;
	                if (errorv > derrorv) {
	                    pixval += sincv;
	                    errorv -= derrorv;
	                }
	            } else {
	                pixval -= incv;
	                errorv -= incerrorv;
	                if (errorv < 0) {
	                    pixval -= sincv;
	                    errorv += derrorv;
	                }
	            }
	        }

	        // Integration into the map
	        map[ptr] = (short)(((256 - alpha) * map[ptr] + alpha * pixval) >> 8);
	        if (error > 0) {
	            ptr += incptry;
	            error += diago;
	        } else error += horiz;
	    }

	}

	private Vector3D_F32 monte_carlo_search(List<Point3D_F64> p_list, Vector3D_F32 startpos, float sigma_xy, float sigma_theta, int stop) {

        int currentdist = 0;;
	    int lastbestdist = 0;
	    int counter = 0;

	    boolean debug = false;

	    if (stop < 0) {
	        debug = true;
	        stop = -stop;
	    }
	    currentpos.set(startpos);  bestpos.set(startpos); lastbestpos.set(startpos);
	    currentdist = distance_to_features(p_list);
	    bestdist = lastbestdist = currentdist;

	    do {

	    	currentpos.set(lastbestpos);
		currentpos.x = randomizer.normal(currentpos.x, sigma_xy);
		currentpos.y = randomizer.normal(currentpos.y, sigma_xy);
    //	currentpos.w = randomizer.normal(currentpos.w, sigma_theta); //??

		currentdist = distance_to_features(p_list);
	//	 System.out.printf("Monte carlo = %g %g %d (count = %d)\n", currentpos.x, currentpos.y, currentdist, counter);

		if (currentdist < bestdist) {
		    bestdist = currentdist;
		    bestpos.set(currentpos);
		    System.out.printf("Monte carlo ! %g %g %d (count = %d)\n", bestpos.x, bestpos.y, bestdist, counter);
		} else {
		    counter++;
		}
	        if (counter > stop / 3) {
	            if (bestdist < lastbestdist) {
	                lastbestpos.set(bestpos);
	                lastbestdist = bestdist;
	                counter = 0;
	                sigma_xy *= 0.5;
	                sigma_theta *= 0.5;
	            }
	        }
	    } while (counter < stop);

	    return bestpos;
	}

	private void transfer_to_target_map() {

	}

	public static void main(String[] args) {

		LocalMap2DArray_old map    = new LocalMap2DArray_old();

		OriginalTinySLAM slam = new OriginalTinySLAM(map);

		ArrayList<Point3D_F64> list = new ArrayList<Point3D_F64>();

		Vector3D_F32 pos = new Vector3D_F32();
		Point3D_F64 p = new Point3D_F64();

		p.set(1,1,0);
		list.add(p.copy());
		p.set(2,0,0);
		list.add(p.copy());

		slam.update(list, pos);

		p.set(0.8,0.2,0);
		list.add(p.copy());
		p.set(1.8,0.2,0);
		list.add(p.copy());

		pos.set(0.1f,0,0);
		slam.update(list, pos);
	}


}
