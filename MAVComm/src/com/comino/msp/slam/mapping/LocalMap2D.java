package com.comino.msp.slam.mapping;

import java.util.Arrays;

import com.comino.msp.model.DataModel;
import com.comino.msp.utils.MSPMathUtils;

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

	private float[]			polar = new float[360];

	public LocalMap2D(float diameter_m, float cell_size_m, float window_diameter_m) {
		this(diameter_m, cell_size_m, window_diameter_m, diameter_m/2f, diameter_m/2f);
	}

	public LocalMap2D(float map_diameter_m, float cell_size_m, float window_diameter_m, float center_x_m, float center_y_m) {
		diameter_mm  = (int)(map_diameter_m * 1000f);
		cell_size_mm = (int)(cell_size_m * 1000f);

		map_dimension  = (int)Math.floor(map_diameter_m / cell_size_m );
		map = new short[map_dimension][map_dimension];

		window_dimension = (int)Math.floor(window_diameter_m / cell_size_m );
		window = new short[window_dimension][window_dimension];

		reset();

		this.center_x_mm = center_x_m * 1000f;
		this.center_y_mm = center_y_m * 1000f;

		System.out.println("LocalMap2D initialized with "+map_dimension+"x"+map_dimension+" map and "+window.length+"x"+window.length+" windows cells. ");
	}

	public void reset() {
		for (short[] row : map)
			Arrays.fill(row, (short)0);
		for (short[] row : window)
			Arrays.fill(row, (short)0);
	}

	public void 	setLocalPosition(Vector3D_F32 point) {
		local_x_mm = point.x *1000f + center_x_mm;;
		local_y_mm = point.y *1000f + center_y_mm;;
	}

	public boolean update(Vector3D_F32 point) {
		return set(point.x, point.y,1);
	}

	public boolean update(float lpos_x, float lpos_y,Vector3D_F32 point) {
		return set(lpos_x+point.x, lpos_y+point.y,1);
	}

	public short[][] getWindow(float lpos_x, float lpos_y) {

		int px,py, new_x,new_y;

		px = (int)Math.floor( (lpos_x * 1000.0f + center_x_mm) / cell_size_mm);
		py = (int)Math.floor( (lpos_y * 1000.0f + center_y_mm) / cell_size_mm);


		for (int y = 0; y < window_dimension; y++) {
			for (int x = 0; x < window_dimension; x++) {

				new_x = x + px - window_dimension / 2;
				new_y = y + py - window_dimension / 2;

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

		for (int y = 0; y < window_dimension; y++) {
			for (int x = 0; x < window_dimension; x++) {
				 if(window[x][y] <= 0)
					 continue;
				d = (float)Math.sqrt((x - window_dimension/2)*(x - window_dimension/2) + (y - window_dimension/2)*(y - window_dimension/2));
				if(d < distance)
					distance = d;
			}
		}
		return (distance * cell_size_mm + cell_size_mm/2) / 1000.0f;
	}

//	public float nearestDistance(float lpos_x, float lpos_y) {
//		float lpos_x_mm = lpos_x * 1000f;
//		float lpos_y_mm = lpos_y * 1000f;
//		float _x, _y; float distance = Float.MAX_VALUE, d;
//
//		for (int x = 0; x < map_dimension;x++) {
//			for (int y = 0; y < map_dimension; y++) {
//
//				if(map[x][y] < 1)
//					continue;
//
//				_x = x*cell_size_mm-center_x_mm+cell_size_mm/2f;
//				_y = y*cell_size_mm-center_y_mm+cell_size_mm/2f;
//
//				d = (float)Math.sqrt((lpos_x_mm-_x)*(lpos_x_mm-_x) + (lpos_y_mm-_y)*(lpos_y_mm-_y));
//				if(d < distance)
//					distance = d;
//			}
//		}
//		return distance/1000f;
//	}

//	public float[] getWindowPolar(int dir, float lpos_x, float lpos_y) {
//		int angle; float distance; float dx,dy;
//		Arrays.fill(polar, Float.MAX_VALUE);
//		for (int y = 0; y <map_dimension; y++) {
//			for (int x = 0; x < map_dimension; x++) {
//				if(map[x][y] == 0)
//					continue;
//				dx = lpos_x - (x * cell_size_mm - center_x_mm);
//			    dy = lpos_y - (y * cell_size_mm - center_y_mm);
//			    distance = (float)Math.sqrt(dx*dx + dy*dy);
//				angle = ((int)MSPMathUtils.fromRad((float)Math.atan2(dy,dx))-dir) % 360;
//				if(angle<0) angle += 360;
//                 if(polar[angle] > distance)
//                	    polar[angle] = distance;
//			}
//		}
//		return polar;
//	}

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

	public float getDiameter_mm() {
		return diameter_mm;
	}

	public float getCellSize_mm() {
		return cell_size_mm;
	}

	public void toDataModel(DataModel model, int threshold, boolean debug) {
		for (int y = 0; y <map_dimension; y++) {
			for (int x = 0; x < map_dimension; x++) {
				if(map[x][y] == 0)
					continue;

				if(map[x][y] > threshold) {
					model.grid.setBlock((x*cell_size_mm-center_x_mm)/1000f,(y*cell_size_mm-center_y_mm)/1000f, true);
					//	System.out.println("ADD: "+(j*grid.resolution/100f-center_x)+ ":"+ (i*grid.resolution/100f-center_y));
				}
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
					b.append("o");
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

}
