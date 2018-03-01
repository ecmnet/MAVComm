package com.comino.dev;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.comino.msp.slam.map2D.ILocalMap;
import com.comino.msp.utils.MSPArrayUtils;
import com.comino.msp.utils.MSPMathUtils;

import boofcv.gui.ListDisplayPanel;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayU8;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector4D_F64;

public class CoreSLAM {

	private static final float SIGMA_XY				= 0.75f;
	private static final float SIGMA_THETA			= 0.3f;

	private static final int QUALITY 				= 50;
	private static final int HOLE_SIZE 				= 600;
	private static final int MONTE_CARLO_ITERATIONS 	= 1500;

	private ILocalMap		target_map  		= null;
	private int             diameter_mm		= 0;
	private int             cell_size_mm		= 0;

	private int             count			= 0;
	private float           avg_update_ms    = 0;

	private int[][]			map 				= null;

	private Vector4D_F64 	currentpos 		= new Vector4D_F64();
	private Vector4D_F64 	bestpos    		= new Vector4D_F64();
	private Vector4D_F64 	lastbestpos 		= new Vector4D_F64();
	private Vector4D_F64 	initialpos 		= new Vector4D_F64();

	private int 				bestdist 		= 0;

	private Ziggurat randomizer    = null;

	public CoreSLAM(ILocalMap target_map) {

		System.out.println("CoreSLAM initialized with "+ target_map.getMapDimension()+" x "+ target_map.getMapDimension()+" cells");

		this.target_map    = target_map;
		this.cell_size_mm  = target_map.getCellSize_mm();
		this.diameter_mm   = target_map.getMapDimension() *  cell_size_mm;
		this.map = new int[target_map.getMapDimension()][ target_map.getMapDimension()];
		this.randomizer = new Ziggurat();

		initMap();
	}

	public CoreSLAM(ILocalMap target_map, float diameter_m, float cell_size_m) {

		this.target_map    = target_map;
		this.cell_size_mm  = (int)(cell_size_m * 1000f);
		this.diameter_mm   = (int)(diameter_m  * 1000f);
		this.map = new int[diameter_mm/cell_size_mm][diameter_mm/cell_size_mm];
		this.randomizer = new Ziggurat();

		System.out.println("CoreSLAM initialized with "+(int)(diameter_mm/cell_size_mm)+" x "+(int)(diameter_mm/cell_size_mm)+" cells");

		initMap();
	}

	public Vector4D_F64 update(List<Point3D_F64> p_list, Vector4D_F64 current_pos) {
		long tms = System.currentTimeMillis();
		initialpos.set(current_pos);
		Vector4D_F64 guessed_pos = monte_carlo_search(p_list, current_pos, SIGMA_XY , SIGMA_THETA, MONTE_CARLO_ITERATIONS);
		update_map(p_list, guessed_pos, QUALITY, HOLE_SIZE);
		transfer_to_target_map(p_list,guessed_pos);
		avg_update_ms = (float)((avg_update_ms * count + (System.currentTimeMillis() - tms)) / ++count);
		return guessed_pos;
	}

	public Vector4D_F64 getGuessedPosition() {
		return bestpos;
	}

	public Vector4D_F64 getGuessedPosition(List<Point3D_F64> p_list, Vector4D_F64 current_pos) {
		initialpos.set(current_pos);
		return monte_carlo_search(p_list, current_pos, SIGMA_XY , SIGMA_THETA, MONTE_CARLO_ITERATIONS);
	}

	public void transfer_to_target_map(List<Point3D_F64> p_list, Vector4D_F64 pos) {
		for(Point3D_F64 p : p_list) {
			target_map.update(p, pos);
		}
	}

	public float getAverageUpdateTime_ms() {
		return avg_update_ms;
	}

	private void update_map(List<Point3D_F64> p_list, Vector4D_F64 pos, int quality, int hole_width) {
		int xp,yp; int radius;

		// current pos in units for distance/radius caluclation
		int xc = (int)Math.floor((pos.x*1000f+diameter_mm/2)/cell_size_mm);
		int yc = (int)Math.floor((pos.y*1000f+diameter_mm/2)/cell_size_mm);

		for(Point3D_F64 p : p_list) {

			xp = (int)Math.floor(((p.x +pos.x )*1000f+diameter_mm/2)/cell_size_mm) ;
			yp = (int)Math.floor(((p.y +pos.y )*1000f+diameter_mm/2)/cell_size_mm) ;

			radius = hole_width / 2 / sqrt((xp-xc)*(xp-xc)+(yp-yc)*(yp-yc));

			draw_hole_into_map(xp,yp, radius, quality, 1);
		}
	}

	private Vector4D_F64 monte_carlo_search(List<Point3D_F64> p_list, Vector4D_F64 startpos, float sxy, float st, int stop) {

		float sigma_theta = st;
		float sigma_xy    = sxy;

		int currentdist = 0;;
		int lastbestdist = 0;
		int counter = 0;

		currentpos.set(startpos);  bestpos.set(startpos); lastbestpos.set(startpos);
		currentdist = distance_to_features(p_list, currentpos);
		bestdist = lastbestdist = currentdist;
	// System.out.printf("Initial %g %g %d (count = %d)\n", bestpos.x, bestpos.y, currentdist, counter);

		do {

			currentpos.set(lastbestpos);
			currentpos.x = randomizer.normal((float)currentpos.x, sigma_xy);
			currentpos.y = randomizer.normal((float)currentpos.y, sigma_xy);
		//	currentpos.w = randomizer.normal((float)currentpos.w, sigma_theta);

			currentdist = distance_to_features(p_list, currentpos);
//			System.out.printf("Search = %g %g %.1f° => %d (count = %d)\n", currentpos.x, currentpos.y,
//			           MSPMathUtils.fromRad((float)currentpos.w), currentdist, counter);

			if (currentdist < bestdist) {
				bestdist = currentdist;
				bestpos.set(currentpos);

			} else {
				counter++;
			}

			if (counter > (stop / 3)) {
				if (bestdist < lastbestdist) {
					lastbestpos.set(bestpos);
					lastbestdist = bestdist;
					counter = 0;
					sigma_xy    *= 0.5f;
					sigma_theta *= 0.5f;
				}
			}
		} while (counter < stop);

		return bestpos;
	}

	private int distance_to_features(List<Point3D_F64> p_list, Vector4D_F64 cp) {
		int x,y; int sum = 0; int count = 0;

		if(p_list.isEmpty())
			return Short.MAX_VALUE;

		double c = Math.cos(cp.w);
		double s = Math.sin(cp.w);

		for(Point3D_F64 p : p_list) {
			x = (int)Math.floor(((cp.x + p.x * c - p.y * s )*1000.0+diameter_mm/2.0)/cell_size_mm);
			y = (int)Math.floor(((cp.y + p.x * s + p.y * c )*1000.0+diameter_mm/2.0)/cell_size_mm);

			if (x >= 0 && x < map.length && y >= 0 && y < map.length) {
				sum +=map[x][y]; count++;
			}
		}
		if(count > 0)
			return sum  / count;

		return Short.MAX_VALUE;
	}

	private void draw_hole_into_map(int xm, int ym, int radius, int quality, int op) {

		if (xm< 0 || xm >= map.length || ym < 0 || ym >= map.length)
			return;

		set_map_point(xm,ym,radius,quality);

		int i=0; int y_old=0; int dr=0;
		int r = radius, x = -r, y = 0, err = 2-2*r;
		do {
			for(i=x;i<=0;i++) {
				if(y!=y_old) {
					dr = ( quality - sqrt(y*y+i*i) * quality / radius ) * op;
					dr = dr < 0 ? 0 : dr;
					if(dr!=0) {
						set_map_point(xm-i,ym+y,radius,dr);
						set_map_point(xm-y,ym-i,radius,dr);
						set_map_point(xm+i,ym-y,radius,dr);
						set_map_point(xm+y,ym+i,radius,dr);
					}
				}
			}
			y_old = y;
			r = err;
			if (r < y) err += ++y*2+1;            /* e_xy+e_y < 0 */
			if (r > x || err > y) err += ++x*2+1; /* e_xy+e_x > 0 or no 2nd y-step */
		} while (x <= 0);

	}

	private void set_map_point(int x,int y, int radius, int dr) {

		if(x >=0 && y>=0 && x < map.length && y < map.length) {
			map[x][y] -=dr;
		}
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

	public void initMap() {
		for (int[] row : map)
			Arrays.fill(row, 0);
	}

	public String toString() {
		return String.format("SLAM correction from ( % .2f, % .2f - % .1f°) to ( % .2f, % .2f - % .1f° ) %d %.2fms",
				initialpos.x,initialpos.y,MSPMathUtils.fromRad((float)initialpos.w),
				bestpos.x,bestpos.y, MSPMathUtils.fromRad((float)bestpos.w), bestdist, avg_update_ms );
	}

	public String mapToString() {
		StringBuilder b = new StringBuilder();
		for(int y=0; y<map.length; y++) {
			for(int x=0; x<map.length; x++) {
				if(map[x][y] != Short.MAX_VALUE)
					b.append(" "+map[x][y]+" ");
				else
					b.append("   .   ");
			}
			b.append("\n\n");
		}
		b.append("\n");
		return b.toString();
	}

	public static void main(String[] args) {

		LocalMap2DArray_old map    = new LocalMap2DArray_old(10f,0.01f,1f,1);

		CoreSLAM slam = new CoreSLAM(map);

		Vector4D_F64 pos = new Vector4D_F64(1,-1f,0,0);
		Point3D_F64 p = new Point3D_F64();

		ArrayList<Point3D_F64> list = new ArrayList<Point3D_F64>();

		pos.set(1,-1f,0,0);      // gemessene position
		p.set(+0.3f,+0.3f,0);  // relative distance zum obstacle
		list.add(p.copy());
		p.set(-0.1,-0.3f,0);  // relative distance zum obstacle
		list.add(p.copy());
		p.set(-0.4,+1f,0);  // relative distance zum obstacle
		list.add(p.copy());
		slam.update(list, pos);
		//System.out.println(slam.mapToString());
		System.out.println(slam);

		pos.set(1.3f,-1.5,0,MSPMathUtils.toRad(7.98f));
		slam.update(list, pos);
		System.out.println(slam);

		for(int i=0;i<1000;i++) {

			pos.set(0.8f+Math.random()/2,-1.2+Math.random()/2,0,MSPMathUtils.toRad((float)Math.random()*20-10f));

			list.clear();
			p.set(+0.3f,+0.3f,0);  // relative distance zum obstacle
			list.add(p.copy());
			p.set(-0.1,-0.3f,0);  // relative distance zum obstacle
			list.add(p.copy());
			p.set(-0.4,+1f+Math.random()/10,0);  // relative distance zum obstacle
			list.add(p.copy());
			p.set(-0.0+Math.random()/10,+1f+Math.random()/10,0);  // relative distance zum obstacle
			list.add(p.copy());

			slam.update(list, pos);
			System.out.println(slam);
		}
		System.out.println(slam.getAverageUpdateTime_ms()+"ms");


	}

}
