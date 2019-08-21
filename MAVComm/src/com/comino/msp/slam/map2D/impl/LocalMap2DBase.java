package com.comino.msp.slam.map2D.impl;

import com.comino.msp.model.DataModel;

/****************************************************************************
*
*   Copyright (c) 2017 - 2019 Eike Mansfeld ecm@gmx.de. All rights reserved.
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

import com.comino.msp.slam.map2D.ILocalMap;
import com.comino.msp.utils.MSP3DUtils;

import georegression.struct.point.Point3D_F64;

public abstract class LocalMap2DBase implements ILocalMap {

	protected int threshold = 0;

	protected short[][] map;
	protected short[][] window;

	protected int cell_size_mm;
	protected float center_x_mm;
	protected float center_y_mm;

	protected float local_x_mm;
	protected float local_y_mm;

	protected int map_dimension;
	protected int window_dimension;

	protected Point3D_F64 nearestObstaclePoition = new Point3D_F64();

	protected DataModel model = null;

	protected boolean is_loaded = false;

	/******************************************************************************************************/

	public void processWindow(float lpos_x, float lpos_y) {

		int px, py, new_x, new_y;
		int center = window_dimension / 2;

		px = (int) Math.floor((lpos_x * 1000.0f + center_x_mm) / cell_size_mm);
		py = (int) Math.floor((lpos_y * 1000.0f + center_y_mm) / cell_size_mm);

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
	}

	/******************************************************************************************************/

	public short[][] get() {
		return map;
	}

	public int getWindowValue(int x, int y) {
		return window[x][y];
	}

	public int getWindowDimension() {
		return window_dimension;
	}

	public int getMapDimension() {
		return map_dimension;
	}

	public int getCellSize_mm() {
		return cell_size_mm;
	}

	public void setIsLoaded(boolean loaded) {
		is_loaded = loaded;
	}

	public boolean isLoaded() {
		return is_loaded;
	}

	public Point3D_F64 getNearestObstaclePosition() {
		 return nearestObstaclePoition;
	}

	/******************************************************************************************************/

	public short get(float xpos, float ypos) {
		int x = (int) Math.floor((xpos * 1000f + center_x_mm) / cell_size_mm);
		int y = (int) Math.floor((ypos * 1000f + center_y_mm) / cell_size_mm);
		if (x >= 0 && x < map[0].length && y >= 0 && y < map[0].length)
			return map[x][y];
		return -1;
	}

	public void reset() {
		for (int y = 0; y < map_dimension; y++) {
			for (int x = 0; x < map_dimension; x++)
				map[x][y] = 0;
		}
		is_loaded = false;
	}

	// TODO: Limit nearest distance to flight direction
	public float nearestDistance(float lpos_x, float lpos_y) {

		float distance = Float.MAX_VALUE, d;
		int center = window_dimension/2;

		for (int y = 0; y < window_dimension; y++) {
			for (int x = 0; x < window_dimension; x++) {
				if(window[x][y] <= threshold)
					continue;
				d = (float)Math.sqrt((x - center)*(x - center) + (y - center)*(y - center));
				if(d < distance) {
					distance = d;
					nearestObstaclePoition.set(((x-center)*cell_size_mm - cell_size_mm/2f)/1000f+lpos_x,
							                   ((y-center)*cell_size_mm - cell_size_mm/2f)/1000f+lpos_y, 0);
				}
			}
		}
		return (distance * cell_size_mm + cell_size_mm/2) / 1000.0f;
	}

	/******************************************************************************************************/

	public void setDataModel(DataModel model) {
		this.model = model;
	}

	public void toDataModel(boolean debug) {
//		//TODO: Only transfer changes
		for (int y = 0; y < map_dimension; y++) {
			for (int x = 0; x < map_dimension; x++) {
				if (map[x][y] > threshold)
					model.grid.setBlock((x * cell_size_mm - center_x_mm) / 1000f,
							(y * cell_size_mm - center_y_mm) / 1000f, 0, true);
				else
					model.grid.setBlock((x * cell_size_mm - center_x_mm) / 1000f,
							(y * cell_size_mm - center_y_mm) / 1000f, 0, false);
			}
		}
		if (debug)
			System.out.println(model.grid);
	}

/******************************************************************************************************/

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

}
