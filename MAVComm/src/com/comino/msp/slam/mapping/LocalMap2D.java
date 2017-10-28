package com.comino.msp.slam.mapping;

import java.util.Arrays;

import com.comino.msp.model.DataModel;

import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F32;

public class LocalMap2D implements IMSPMap {

	private short[][] 		map;
	private int   			diameter_mm;
	private int 				cell_size_mm;
	private float			center_x_mm;
	private float			center_y_mm;

	private int 				dimension;

	public LocalMap2D(float diameter_m, float cell_size_m) {
		this(diameter_m, cell_size_m, diameter_m/2f, diameter_m/2f);
	}

	public LocalMap2D(float diameter_m, float cell_size_m, float center_x_m, float center_y_m) {
		diameter_mm  = (int)(diameter_m * 1000f);
		cell_size_mm = (int)(cell_size_m * 1000f);

		dimension  = (int)Math.floor(diameter_m / cell_size_m + 1f);
		map = new short[dimension][dimension];

		reset();

		this.center_x_mm = center_x_m * 1000f;
		this.center_y_mm = center_y_m * 1000f;
	}

	public void reset() {
		for (short[] row : map)
			Arrays.fill(row, (short)-1);
	}

	public boolean update(Vector3D_F32 point) {
		return set(point.x, point.y,2);
	}

	public boolean update(float lpos_x, float lpos_y,Vector3D_F32 point) {
		return set(lpos_x+point.x, lpos_y+point.y,2);
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
		System.out.print(x+":"+y+"=>"+map[x][y]);
		if(x >=0 && x < map[0].length && y >=0 && y < map[0].length) {
			map[x][y] = (short)value;
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
		for (int x = 0; x <dimension; x++) {
			for (int y = 0; y < dimension; y++) {
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

}
