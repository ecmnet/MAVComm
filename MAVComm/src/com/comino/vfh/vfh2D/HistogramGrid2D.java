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

/*
 * Source: https://github.com/agarie/vector-field-histogram
 */

package com.comino.vfh.vfh2D;

import com.comino.msp.model.DataModel;
import com.comino.msp.slam.mapping.IMSPLocalMap;
import com.comino.vfh.VfhGrid;
import com.comino.vfh.VfhHist;

import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F32;

public class HistogramGrid2D  implements IMSPLocalMap {

	private static final long OBLIVISION_TIME_MS = 10000;
	private static final int  MAX_CERTAINITY     = 1000;

	private VfhGrid vfhg     = null;
	private VfhGrid window   = null;

	private long        tms  = 0;
	private float   centerx  = 0;
	private float   centery  = 0;

	public HistogramGrid2D(float dimension,  float windowsize, float resolution) {
		this(dimension/2f, dimension/2f, dimension, windowsize, resolution);
	}

	public HistogramGrid2D(float cenx, float ceny, float dimension,  float windowsize, float resolution) {
		assert(dimension % 2 == 1);

		centerx = cenx;
		centery = ceny;

		vfhg   = new VfhGrid(dimension, resolution);
		window = new VfhGrid(windowsize, resolution);

		System.out.println("HistogramGricd2D initialized at ("+cenx+","+ceny+") with size "+dimension+" and resolution "+ resolution);
	}

	public void reset() {
		vfhg.clear();
	}

	// Updates the grid with an absolute observation
	public boolean update(Vector3D_F32 obstacle_absolute) {
		return gridUpdate(0,0,obstacle_absolute);
	}

	// Updates the grid with an relative observation
	// TODO: 1. Weight increment relative to the distance
	//       2. Mark surrounding cells with a default if distance is < limit
	public boolean gridUpdate(float lpos_x, float lpos_y, Vector3D_F32 obstacle) {

		int new_x = (int)Math.floor((lpos_x+centerx+obstacle.x)*100f / vfhg.resolution);
		int new_y = (int)Math.floor((lpos_y+centery+obstacle.y)*100f / vfhg.resolution);

		if(updateCell(new_x,new_y,3)) {

			// now mark surrounding cells with lower value
			updateCell(new_x+1,new_y,2);
			updateCell(new_x-1,new_y,2);
			updateCell(new_x,new_y+1,2);
			updateCell(new_x,new_y-1,2);

			updateCell(new_x+1,new_y+1,1);
			updateCell(new_x-1,new_y+1,1);
			updateCell(new_x+1,new_y-1,1);
			updateCell(new_x-1,new_y-1,1);

			return true;
		}
		return false;
	}

	// creates a moveing window at the current position with a certain window size
	public VfhGrid getMovingWindow(float lpos_x, float lpos_y) {
		window.clear(); int new_x  = 0; int new_y = 0;
		for (int y = 0; y < window.dimension; y++) {
			for (int x = 0; x < window.dimension;x++) {
				new_x = x + (int)Math.floor((lpos_x+centerx)*100f / vfhg.resolution) - (window.dimension - 1) / 2;
				new_y = y + (int)Math.floor((lpos_y+centery)*100f / vfhg.resolution) - (window.dimension - 1) / 2;

				if (new_x < vfhg.dimension && new_y < vfhg.dimension && new_x >= 0 && new_y >= 0) {
					window.cells[y * window.dimension + x] = vfhg.cells[new_y * vfhg.dimension + new_x];
				}
			}
		}
		return window;
	}

//	public void transferWindowToModel(DataModel model, int threshold, float lpos_x, float lpos_y, boolean debug) {
//		model.grid.clear();
//		for (int i = 0; i < window.dimension; ++i) {
//			for (int j = 0; j < window.dimension; ++j) {
//
//				if(window.cells[i * window.dimension + j] == 0)
//					continue;
//
//				if(vfhg.cells[i * window.dimension + j] > threshold) {
//					model.grid.setBlock(j*vfhg.resolution/100f-centerx-lpos_x,i*vfhg.resolution/100f-centery-lpos_y, true);
//					//	System.out.println("ADD: "+(j*grid.resolution/100f-center_x)+ ":"+ (i*grid.resolution/100f-center_y));
//				}
//				else
//					model.grid.setBlock(j*vfhg.resolution/100f-centerx-lpos_x,i*vfhg.resolution/100f-centery-lpos_y, false);
//			}
//		}
//		if(debug)
//			System.out.println(model.grid);
//	}

	public void toDataModel(DataModel model, int threshold, boolean debug) {
		for (int i = 0; i < vfhg.dimension; ++i) {
			for (int j = 0; j < vfhg.dimension; ++j) {

				if(vfhg.cells[i * vfhg.dimension + j] == 0)
					continue;

				if(vfhg.cells[i * vfhg.dimension + j] > threshold) {
					model.grid.setBlock(j*vfhg.resolution/100f-centerx,i*vfhg.resolution/100f-centery, true);
					//	System.out.println("ADD: "+(j*grid.resolution/100f-center_x)+ ":"+ (i*grid.resolution/100f-center_y));
				}
				else
					model.grid.setBlock(j*vfhg.resolution/100f-centerx,i*vfhg.resolution/100f-centery, false);
			}
		}
		if(debug)
			System.out.println(model.grid);
	}

	public String toString(int threshold) {
		return vfhg.toString(threshold);
	}

	public float nearestDistance(float lpos_y, float lpos_x) {
		float x, y; float distance = Float.MAX_VALUE, d;
		for (int i = 0; i < vfhg.dimension; ++i) {
			for (int j = 0; j < vfhg.dimension; ++j) {
				if(vfhg.cells[i * vfhg.dimension + j] < 1)
					continue;

				x = j*vfhg.resolution/100f-centerx+vfhg.resolution/200f;
				y = i*vfhg.resolution/100f-centery+vfhg.resolution/200f;

				d = (float)Math.sqrt((lpos_x-x)*(lpos_x-x) + (lpos_y-y)*(lpos_y-y));
				if(d < distance)
					distance = d;
			}
		}
		return distance;
	}


	public void forget() {
		if((System.currentTimeMillis()-tms)>OBLIVISION_TIME_MS) {
			tms = System.currentTimeMillis();
			for (int i = 0; i < vfhg.dimension; ++i)
				for (int j = 0; j < vfhg.dimension; ++j)
					if(vfhg.cells[i * vfhg.dimension + j] > 1)
						vfhg.cells[i * vfhg.dimension + j] -= 1;
		}
	}

	public String toString() {
		return vfhg.toString();
	}

	private boolean updateCell(int new_x, int new_y, int value) {
		if (new_x <= vfhg.dimension && new_y <= vfhg.dimension && new_x > 0 && new_y > 0
				&& vfhg.cells[new_y * vfhg.dimension + new_x]<MAX_CERTAINITY) {
			vfhg.cells[new_y * vfhg.dimension + new_x] += value;
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
//		Grid grid = new Grid(5,0.1f);
//		grid.setIndicator(0.1f, 0.1f);

		HistogramGrid2D hist = new HistogramGrid2D(5,5,10,1f,0.1f);

		Vector3D_F32 p = new Vector3D_F32();

		for(int c= 0; c < 5; c++) {

			for(int i=0;i<10;i++) {
				p.set(0.5f,i/10f-0.15f,0); hist.update(p);
			}

			for(int i=0;i<10;i++) {
				p.set(i/10f-1.15f,0.5f,0); hist.update(p);
			}
		}

		VfhGrid win = hist.getMovingWindow(0, 0);

		System.out.println(win.getCellDirection(6, 3));


		System.out.println(win.toString());



	}

}
