package com.comino.mavmap.map.map2D.impl;

import com.comino.mavcom.model.DataModel;
import com.comino.mavcom.utils.MSP3DUtils;
import com.comino.mavmap.map.map2D.ILocalMap;
import com.comino.mavmap.struct.Polar3D_F32;
import com.comino.mavutils.MSPMathUtils;

public abstract class LocalMap2DBase implements ILocalMap {

	protected int threshold = 0;

	protected short[][] map;
	protected short[][] level;
	protected short[][] window;
	protected float[][] window_angles;

	protected int cell_size_mm;
	protected float center_x_mm;
	protected float center_y_mm;

	protected float local_x_mm;
	protected float local_y_mm;

	protected int map_dimension;
	protected int window_dimension;

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
					// Note: swap XY to reflect LPOS
					window[y][x] = map[new_x][new_y];
				else
					window[y][x] = Short.MAX_VALUE;
			}
		}
	}

	public void initWindowAngles() {

		this.window_angles = new float[window_dimension][window_dimension];
		int center = window_dimension / 2;

		for (int y = 0; y < window_dimension; y++) {
			for (int x = 0; x < window_dimension; x++) {
				// xy exchanged to rotate by 90 degree
				window_angles[y][x] = MSP3DUtils.getXYDirection(x-center, y-center);
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


	/******************************************************************************************************/

	public short get(float xpos, float ypos) {
		int x = (int) Math.floor((xpos * 1000f + center_x_mm) / cell_size_mm);
		int y = (int) Math.floor((ypos * 1000f + center_y_mm) / cell_size_mm);
		if (x >= 0 && x < map[0].length && y >= 0 && y < map[0].length)
			return map[x][y];
		return -1;
	}

	public void init() {
			for (int y = 0; y < window_dimension; y++) {
				for (int x = 0; x < window_dimension; x++)
					window[x][y] =  0;
			}
			initWindowAngles();
		}

	public void reset() {
		for (int y = 0; y < map_dimension; y++) {
			for (int x = 0; x < map_dimension; x++)
				map[x][y] = 0;
		}
		is_loaded = false;
	}

	public void nearestObstacle(Polar3D_F32 result) {

		float distance = Float.MAX_VALUE, d;
		int center = window_dimension/2;

		for (int y = 0; y < window_dimension; y++) {
			for (int x = 0; x < window_dimension; x++) {
				if(window[x][y] <= threshold)
					continue;
				d = (float)Math.sqrt((x - center)*(x - center) + (y - center)*(y - center));
				if(d < distance ) {
					   result.angle_xy = window_angles[x][y];
					   result.angle_xz = 0;
					   distance = d;
				}
			}
		}

		result.value = (distance * cell_size_mm + cell_size_mm/2) / 1000.0f;
		//System.out.println(result);
	}

	public float getMaxDistanceInDirection(float direction) {

		float distance = Float.MAX_VALUE;
		int center = window_dimension/2;



		return (distance * cell_size_mm + cell_size_mm/2) / 1000.0f;

	}

	/******************************************************************************************************/

	public void setDataModel(DataModel model) {
		this.model = model;
	}

	public void toDataModel(boolean debug) {

		for (int y = 0; y < map_dimension; y++) {
			for (int x = 0; x < map_dimension; x++) {
				if (map[x][y] > threshold)
					model.grid.setBlock((x * cell_size_mm - center_x_mm) / 1000f,
							(y * cell_size_mm - center_y_mm) / 1000f, -level[x][y]/1000f, true);
				else
					model.grid.setBlock((x * cell_size_mm - center_x_mm) / 1000f,
							(y * cell_size_mm - center_y_mm) / 1000f, -level[x][y]/1000f, false);
			}
		}
	}

/******************************************************************************************************/

	public String windowToString() {
		StringBuilder b = new StringBuilder();
		for(int y=window.length-1; y>=0; y--) {
			for(int x=0; x<window.length; x++) {
				if(x == window_dimension/2 && y == window_dimension/2)
					b.append("O   ");
				else
				if(window[x][y]>0) {
					b.append(String.format("%-3.0f ", MSPMathUtils.fromRad(window_angles[x][y])));
//				b.append("X   ");
				}
				else
					b.append(".   ");
			}
			b.append("\n");
		}
		b.append("\n");
		return b.toString();
	}

	public String anglesToString() {
		StringBuilder b = new StringBuilder();
		for(int y=window.length-1; y>=0; y--) {
			for(int x=0; x<window.length; x++) {
			//	b.append(MSPMathUtils.fromRad(window_angles[x][y]));
				b.append(window_angles[x][y]);
				b.append(" ");
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
				else if(map[x][y]==10) {
					b.append("X ");
				}
				else if(map[x][y]<0) {
					b.append("- ");
				}
				else if(map[x][y]>10) {
					b.append("+ ");
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
