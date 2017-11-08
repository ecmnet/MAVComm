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

package com.comino.msp.slam.map;

import java.util.Arrays;

import com.comino.msp.model.DataModel;

import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F32;

public class LocalMap2D implements IMSPLocalMap {

	private static final long OBLIVISION_TIME_MS = 10000;
	private static final int  MAX_CERTAINITY     = 1000;

	private short[][] 		map;
	private int   			diameter_mm;

	private short[][]    	window;
	private int              window_diameter_mm;

	private int 				cell_size_mm;
	private float			center_x_mm;
	private float			center_y_mm;

	private float			local_x_mm;
	private float			local_y_mm;

	private int 				map_dimension;
	private int             window_dimension;
	private long            tms;

	public LocalMap2D(float diameter_m, float cell_size_m, float window_diameter_m) {
		this(diameter_m, cell_size_m, window_diameter_m, diameter_m/2f, diameter_m/2f);
	}

	public LocalMap2D(float map_diameter_m, float cell_size_m, float window_diameter_m, float center_x_m, float center_y_m) {
		diameter_mm  = (int)(map_diameter_m * 1000f);
		window_diameter_mm = (int)(window_diameter_m * 1000f);
		cell_size_mm = (int)(cell_size_m * 1000f);

		map_dimension  = (int)Math.floor(map_diameter_m / cell_size_m );
		map = new short[map_dimension][map_dimension];

		window_dimension = (int)Math.floor(window_diameter_m / cell_size_m );
		window = new short[window_dimension][window_dimension];

		reset();

		this.center_x_mm = center_x_m * 1000f;
		this.center_y_mm = center_y_m * 1000f;

		System.out.println("LocalMap2D initialized with "+map_dimension+"x"+map_dimension+" map and "+window.length+"x"+window.length+" window cells. ");
	}

	public void reset() {
		for (short[] row : map)
			Arrays.fill(row, (short)0);
		for (short[] row : window)
			Arrays.fill(row, (short)0);
	}

	public short[][] get() {
		return map;
	}

	public void 	setLocalPosition(Vector3D_F32 point) {
		local_x_mm = point.x *1000f + center_x_mm;;
		local_y_mm = point.y *1000f + center_y_mm;;
	}

	public boolean update(Vector3D_F32 point) {
		return set(point.x, point.y,2);
	}

	public boolean update(Point3D_F64 point) {
		return set((float)point.x, (float)point.y,2);
	}

	public boolean update(float lpos_x, float lpos_y,Vector3D_F32 point) {
		return set(lpos_x+point.x, lpos_y+point.y,1);
	}

	public boolean merge(LocalMap2D m) {

		return true;
	}

	public short[][] getWindow(float lpos_x, float lpos_y) {

		int px,py, new_x,new_y;
		int center = window_dimension/2;

		px = (int)Math.floor( (lpos_x * 1000.0f + center_x_mm) / cell_size_mm);
		py = (int)Math.floor( (lpos_y * 1000.0f + center_y_mm) / cell_size_mm);


		for (int y = 0; y < window_dimension; y++) {
			for (int x = 0; x < window_dimension; x++) {

				new_x = x + px - center;
				new_y = y + py - center;

				if (new_x < map_dimension && new_y < map_dimension && new_x >= 0 && new_y >= 0)
					window[x][y] = map[new_x][new_y];
				else
					window[x][y] = Short.MAX_VALUE;
			}
		}
		//	System.err.println(windowToString());
		return window;
	}

	public float nearestDistance(float lpos_x, float lpos_y) {
		float distance = Float.MAX_VALUE, d;
		int center = window_dimension/2;

		for (int y = 0; y < window_dimension; y++) {
			for (int x = 0; x < window_dimension; x++) {
				if(window[x][y] <= 0)
					continue;
				d = (float)Math.sqrt((x - center)*(x - center) + (y - center)*(y - center));
				if(d < distance)
					distance = d;
			}
		}
		return (distance * cell_size_mm + cell_size_mm/2) / 1000.0f;
	}

	public short get(float xpos, float ypos) {
		int x = (int)Math.floor((xpos*1000f+center_x_mm)/cell_size_mm);
		int y = (int)Math.floor((ypos*1000f+center_y_mm)/cell_size_mm);
		if(x >=0 && x < map[0].length && y >=0 && y < map[0].length)
			return map[x][y];
		return -1;
	}

	public boolean set(float xpos, float ypos, int value) {
		int x = (int)Math.floor((xpos*1000f+center_x_mm)/cell_size_mm);
		int y = (int)Math.floor((ypos*1000f+center_y_mm)/cell_size_mm);
		if(x >=0 && x < map[0].length && y >=0 && y < map[0].length && map[x][y] < MAX_CERTAINITY ) {
			map[x][y] += (short)value;
			return true;
		}
		return false;
	}

	public int getDiameter_mm() {
		return diameter_mm;
	}

	public int getWindowDiameter_mm() {
		return window_diameter_mm;
	}

	public int getCellSize_mm() {
		return cell_size_mm;
	}

	public void toDataModel(DataModel model, int threshold, boolean debug) {
		for (int y = 0; y <map_dimension; y++) {
			for (int x = 0; x < map_dimension; x++) {
				if(map[x][y] > threshold)
					model.grid.setBlock((x*cell_size_mm-center_x_mm)/1000f,(y*cell_size_mm-center_y_mm)/1000f, true);
				else
					model.grid.setBlock((x*cell_size_mm-center_x_mm)/1000f,(y*cell_size_mm-center_y_mm)/1000f, false);
			}
		}
		if(debug)
			System.out.println(model.grid);
	}

	public void forget() {
		if((System.currentTimeMillis()-tms)>OBLIVISION_TIME_MS) {
			tms = System.currentTimeMillis();
			for (int i = 0; i < map_dimension; ++i)
				for (int j = 0; j < map_dimension; ++j)
					if(map[i][j] > 0)
						map[i][j] -= 1;
		}
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		for(int y=0; y<map_dimension; y++) {
			for(int x=0; x<map_dimension; x++) {
				if(Math.abs(local_x_mm - x * cell_size_mm)<cell_size_mm &&
						Math.abs(local_y_mm - y * cell_size_mm)<cell_size_mm)
					b.append("o ");
				else if(map[x][y]>0) {
					b.append("X ");
				}
				else
					b.append(". ");
			}
			b.append("\n");
		}
		b.append("\n");
		return b.toString();
	}

	public String windowToString() {
		StringBuilder b = new StringBuilder();
		for(int y=0; y<window.length; y++) {
			for(int x=0; x<window.length; x++) {
				if(window[x][y]>0) {
					b.append("X ");
				}
				else
					b.append(". ");
			}
			b.append("\n");
		}
		b.append("\n");
		return b.toString();
	}

	public static void main(String[] args) {
		LocalMap2D map = new LocalMap2D(11,1,2);

		System.out.println(map);

	}


}
