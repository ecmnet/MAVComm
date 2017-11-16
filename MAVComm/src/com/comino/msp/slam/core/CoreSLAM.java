package com.comino.msp.slam.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.comino.msp.slam.map.ILocalMap;
import com.comino.msp.slam.map.impl.LocalMap2DArray;

import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F32;

public class CoreSLAM {

	private static final int QUALITY 				= 50;
	private static final int HOLE_SIZE 				= 70;
	private static final int MONTE_CARLO_ITERATIONS 	= 1000;

	private ILocalMap		target_map  		= null;
	private int             diameter_mm		= 0;
	private int             cell_size_mm		= 0;

	private short[][]		map 				= null;

	private Vector3D_F32 	currentpos 		= new Vector3D_F32();
	private Vector3D_F32 	bestpos    		= new Vector3D_F32();
	private Vector3D_F32 	lastbestpos 		= new Vector3D_F32();
	private int 				bestdist 		= 0;

	private Ziggurat randomizer    = null;

	public CoreSLAM(ILocalMap target_map) {

		this.target_map    = target_map;
		this.cell_size_mm  = target_map.getCellSize_mm();
		this.diameter_mm   = target_map.getMapDimension() *  cell_size_mm;
		this.map = new short[target_map.getMapDimension()][ target_map.getMapDimension()];
		this.randomizer = new Ziggurat();

		initMap();
	}


	public void update(List<Point3D_F64> p_list, Vector3D_F32 current_pos) {
            Vector3D_F32 guessed_pos = monte_carlo_search(p_list, current_pos, 0.1f , MONTE_CARLO_ITERATIONS);
            System.out.println("Current: "+current_pos+" guessed: "+guessed_pos);
            update_map(p_list,guessed_pos, QUALITY, HOLE_SIZE);

            // Transfer might not be every time
            transfer_to_target_map_and_forget(-2);
	}

	private void transfer_to_target_map_and_forget(int threshold) {
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map.length; x++) {
				if(map[x][y]>threshold)
					continue;
				target_map.get()[x][y] = 10;
	//			map[x][y] += QUALITY/10;
			}
		}
	}

	private void update_map(List<Point3D_F64> p_list, Vector3D_F32 current_pos, int quality, int hole_width) {
		int xp,yp; int radius;

		// current pos in units for distance/radius caluclation
		int xc = (int)Math.floor((current_pos.x*1000f+diameter_mm/2)/cell_size_mm);
		int yc = (int)Math.floor((current_pos.y*1000f+diameter_mm/2)/cell_size_mm);

		// Loop through all features
		for(Point3D_F64 p : p_list) {

			xp = (int)Math.floor((p.x*1000f+diameter_mm/2)/cell_size_mm);
			yp = (int)Math.floor((p.y*1000f+diameter_mm/2)/cell_size_mm);

			radius = hole_width / 2 / sqrt((xp-xc)*(xp-xc)+(yp-yc)*(yp-yc));

			draw_hole_into_map(xp,yp, radius, quality);
		}
	}

	private Vector3D_F32 monte_carlo_search(List<Point3D_F64> p_list, Vector3D_F32 startpos, float sigma_xy, int stop) {

        int currentdist = 0;;
	    int lastbestdist = 0;
	    int counter = 0;

	    if (stop < 0) {
	        stop = -stop;
	    }
	    currentpos.set(startpos);  bestpos.set(startpos); lastbestpos.set(startpos);
	    currentdist = distance_to_features(p_list, currentpos);
	    bestdist = lastbestdist = currentdist;

	    do {

	    	currentpos.set(lastbestpos);
		currentpos.x = randomizer.normal(currentpos.x, sigma_xy);
		currentpos.y = randomizer.normal(currentpos.y, sigma_xy);

		currentdist = distance_to_features(p_list, currentpos);
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
	            }
	        }
	    } while (counter < stop);

	    return bestpos;
	}

	private int distance_to_features(List<Point3D_F64> p_list, Vector3D_F32 cp) {
		int x,y; int sum = 0;

		if(p_list.isEmpty())
			return -Short.MAX_VALUE;

		for(Point3D_F64 p : p_list) {
			x = (int)Math.floor(((p.x-cp.x)*1000f+diameter_mm/2)/cell_size_mm);
			y = (int)Math.floor(((p.y-cp.y)*1000f+diameter_mm/2)/cell_size_mm);
			if (x >= 0 && x < target_map.getMapDimension() && y >= 0 && y < target_map.getMapDimension()) {
				sum += map[x][y];
			}
		}
		return sum  / p_list.size();
	}

	private void draw_hole_into_map(int xm, int ym, int radius, int quality) {

		if (xm< 0 || xm >= map.length || ym < 0 || ym >= map.length)
			return;

		map[xm][ym] -= quality;

		int i=0; int y_old=0; int dr=0;
		int r = radius, x = -r, y = 0, err = 2-2*r;
		do {
			for(i=x;i<=0;i++) {
				if(y!=y_old) {
					dr = quality - sqrt(y*y+i*i) * quality / radius;
					set_map_point(xm-i,ym+y,dr);
					set_map_point(xm-y,ym-i,dr);
					set_map_point(xm+i,ym-y,dr);
					set_map_point(xm+y,ym+i,dr);
				}
			}
			y_old = y;
			r = err;
			if (r < y) err += ++y*2+1;            /* e_xy+e_y < 0 */
			if (r > x || err > y) err += ++x*2+1; /* e_xy+e_x > 0 or no 2nd y-step */
		} while (x <= 0);

	}

	private void set_map_point(int x,int y, int dr) {
		if(x >=0 && y>=0 && x < map.length && y < map.length)
			map[x][y] -= dr;
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

	private void initMap() {
		for (short[] row : map)
			Arrays.fill(row, (short)0);
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		for(int y=0; y<map.length; y++) {
			for(int x=0; x<map.length; x++) {
				if(map[x][y]<0)
					b.append(" "+map[x][y]+" ");
				else
					b.append("  .  ");
			}
			b.append("\n\n");
		}
		b.append("\n");
		return b.toString();
	}

	public static void main(String[] args) {

		LocalMap2DArray map    = new LocalMap2DArray(2.11f,0.1f,1f,1);

		CoreSLAM slam = new CoreSLAM(map);


		Vector3D_F32 pos = new Vector3D_F32(0f,0f,0);
		Point3D_F64 p = new Point3D_F64();

		ArrayList<Point3D_F64> list = new ArrayList<Point3D_F64>();
		p.set(+0.3f,+0.3f,0);
		list.add(p.copy());
		p.set(+0.7f,+0.7f,0);
		list.add(p.copy());

		slam.update(list, pos);

		pos.set(0.1f,0.1f,0);
		list.clear();
		p.set(+0.4f,+0.4f,0);
		list.add(p.copy());
		p.set(+0.6f,+0.8f,0);
		list.add(p.copy());
		slam.update(list, pos);

	//	System.out.println(slam);





	}

}
