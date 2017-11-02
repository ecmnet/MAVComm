package com.comino.msp.slam.mapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.comino.msp.utils.MSPMathUtils;

import georegression.struct.point.Vector3D_F32;


public class LocalVFH2D {

	private static final float	DENSITY_A		= 1000.0f;
	private static final float	DENSITY_B		= 2.5f;

	private static final int     SMAX        	= 40;

	private static final int		ALPHA			= 2;
	private static final int  	SMOOTHING		= 5;
	private static final float  	THRESHOLD		= 1f;

	private static final int MAX_ACCELERATION	= 100;

	private static final int MAX_SPEED   	    = 1000;
	private static final int MAX_SPEED_WIDE		= 500;
	private static final int MAX_SPEED_NARROW    = 300;


	private float 			u1 = 6.0f, u2 = 2f;

	private float[]  		hist;
	private float[]  		hist_smoothed;

	private List<result>		results;
	private List<pair>       borders;

	private float			desired_tdir			= 0;
	private float			selected_tdir       	= 0;
	private float 			last_selected_tdir  	= 0;

	private float			selected_speed	  	= 0;
	private float         	last_selected_speed 	= 0;

	private int   			max_speed_for_selected_angle;

	private long				last_update_time=0;

	public LocalVFH2D(float window_size_m, float cell_size_m) {

		this.hist 			= new float[360 / ALPHA];
		this.hist_smoothed 	= new float[360 / ALPHA];

		this.results = new ArrayList<result>();
		this.borders = new ArrayList<pair>();

	}

	public float getSelectedDirection() {
		return MSPMathUtils.toRad(selected_tdir);
	}

	public float getSelectedSpeed() {
		return selected_speed / 1000f;
	}

	public void update_map(LocalMap2D map, Vector3D_F32 current, float current_speed) {
		int beta = 0; float density=0;

		Arrays.fill(hist, 0); Arrays.fill(hist_smoothed, 0);

		short[][] window =  map.getWindow(current.x, current.y);


		// Build density histogram
		for (int y = 0; y < window.length; y++) {
			for (int x = 0; x < window.length; x++) {

				if(window[x][y] <= 0)
					continue;

				beta = (int)(MSPMathUtils.fromRad((float)Math.atan2((y - window.length/2), (x - window.length/2)))+180) % 360;
				if(beta < 0) beta += 360;
				density = window[x][y] * window[x][y]
						* ( DENSITY_A - DENSITY_B
								* (float)Math.sqrt((x - window.length/2)*(x - window.length/2) + (y - window.length/2)*(y - window.length/2)) )
						* map.getCellSize_mm();
				hist[beta/ALPHA] += density;
			}
		}

		// Smoothing
		float h;
		for(int k =0; k < hist.length; k++) {
			h = 0;
			for(int i = -SMOOTHING; i<= SMOOTHING; i++ )
				h  = h + hist[(k+i+360/ALPHA) % (360/ALPHA)] * (SMOOTHING - Math.abs(i) + 1);
			hist_smoothed[k] = h / (2f * SMOOTHING + 1);
		}

	}

	public void select(float tdir_rad, float current_speed) {

		float diffSeconds = (System.currentTimeMillis() - last_update_time ) / 1000.0f;
		last_update_time = System.currentTimeMillis();

		if(getAbsoluteDirection(hist_smoothed,(int)MSPMathUtils.fromRad(tdir_rad), SMAX) < 0) {
			selected_speed = 0;
			last_selected_speed = 0;
			return;
		}

		int speed_incr;

		if ( (diffSeconds > 0.3) || (diffSeconds < 0) ) {
			// Either this is the first time we've been updated, or something's a bit screwy and
			// update hasn't been called for a while.  Don't want a sudden burst of acceleration,
			// so better to just pick a small value this time, calculate properly next time.
			speed_incr = 10;
		} else {
			speed_incr = (int) (MAX_ACCELERATION * diffSeconds);
		}

		if ( cantTurnToTarget() ) {
			speed_incr = -speed_incr;
		}

		selected_speed = Math.min( last_selected_speed + speed_incr, max_speed_for_selected_angle );
		last_selected_speed = selected_speed;
	}

	private boolean cantTurnToTarget() {
		return false;
	}

	private float getAbsoluteDirection(float[] h, int tdir, int smax) {

		this.desired_tdir = tdir;

		int start, left; float angle, weight, min_weight;

		pair   new_border = new pair();
		result new_result = new result();

		borders.clear(); results.clear();

		start = -1; left = 1;

		for(int i=0;i<hist.length;i++) {
			if (h[i] > THRESHOLD) {
				start = i; 	break;
			}
		}

		if(start == -1) 	{
			max_speed_for_selected_angle = MAX_SPEED;
			selected_tdir = tdir;
			last_selected_tdir = tdir;
			return tdir;
		}

		if(start > 0) {
			new_border.s = 0;
			new_border.e = start * ALPHA;
			borders.add(new_border.clone());
		}

		for(int i = start;i<start+hist.length+1;i++) {

			if ((h[i % hist.length] <= THRESHOLD) && (left==1)) {
				new_border.s = (i % hist.length) * ALPHA;
				left = 0;
			}
			if ((h[i % hist.length] > THRESHOLD) && (left==0)) {
				new_border.e = ((i % hist.length) -1) * ALPHA;
				if (new_border.e < 0 )
					new_border.e += 360;

				if(new_border.s > new_border.e) {
					new_border.e += 360;
				}
				borders.add(new_border.clone());
				left = 1;
			}
		}

		for(pair p : borders) {
			angle = delta_angle(p.s,p.e);

	//		System.out.println(p);

			// ignore narrow gaps
			if (Math.abs(angle) <10)
				continue;

			if (Math.abs(angle) < 80) {
				new_result.speed = MAX_SPEED_NARROW;
				// narrow opening: aim for the centre
				new_result.angle = p.s + (p.e - p.s) / 2.0f;
				results.add(new_result.clone());

			} else {
				new_result.speed = MAX_SPEED_WIDE;
				// wide opening: consider the centre, and 40deg from each border
				new_result.angle = p.s + (p.e - p.s) / 2.0f;
				results.add(new_result.clone());
				new_result.angle = (float)((p.s + smax));
				results.add(new_result.clone());
				new_result.angle = (float)(p.e - smax);
				if (new_result.angle < 0) new_result.angle += 360;
				results.add(new_result.clone());

				// See if candidate dir is in this opening
				if(p.e>360)	tdir +=360;

//				System.out.println("A:"+delta_angle(tdir, results.get(results.size()-2).angle));
//				System.out.println("B:"+delta_angle(tdir, results.get(results.size()-1).angle));

				if ((delta_angle(tdir, results.get(results.size()-2).angle) < 0) &&
						(delta_angle(tdir, results.get(results.size()-1).angle) > 0)) {
					new_result.speed = MAX_SPEED;
					new_result.angle = tdir;
					results.add(new_result.clone());
				}
			}
		}

		if (results.size() == 0) {
			// We're hemmed in by obstacles -- nowhere to go,
			// so brake hard and turn on the spot.
			System.err.println(this);
			max_speed_for_selected_angle = 0;
			return -1;
		}

		min_weight = Float.MAX_VALUE;

		for(result selected : results) {

			weight = u1 * Math.abs(delta_angle_180(tdir,selected.angle))
					+ u2 * Math.abs(delta_angle_180(last_selected_tdir,selected.angle));
			//		System.out.print("["+tdir+","+selected.angle+":"+(Math.abs(delta_angle(tdir, selected.angle)))+"=>"+weight+"] ");
			if(weight < min_weight) {
				min_weight = weight;
				selected_tdir = selected.angle % 360;
				if(selected_tdir < 0)
					selected_tdir += 360;
				max_speed_for_selected_angle = selected.speed;
			}
		}
		//	System.out.println(" ==> "+selected_tdir);

		last_selected_tdir  = selected_tdir;
		return selected_tdir;
	}

	private float delta_angle(float a1, float a2) {
		return  ( a2 - a1 ) % 360 ;
	}

	private float delta_angle_180(float a1, float a2) {
		float diff = a2 - a1;
		if (diff > 180) diff -= 360;
		else if (diff < -180) diff += 360;
		return(diff);
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("["+desired_tdir+","+selected_tdir+"] ");
		for(int i=0;i<hist.length;i++) {
			if(i==(int)selected_tdir/ALPHA) {
				b.append("o");
			} else {
				if(hist_smoothed[i]<THRESHOLD)
					b.append(".");
				else
					b.append("X");
			}
		}
		return b.toString();
	}

	public void print(float[] h) {
		for(int x=0;x<h.length;x++)
			System.out.println((x*ALPHA)+"°: "+h[x]);
	}

	private class pair {
		int s,e;

		public pair clone() {
			pair p = new pair();
			p.s = s; p.e = e;
			return p;
		}

		public String toString() {
			return "("+s+","+e+")";
		}
	}

	private class result {
		float angle; int speed;

		public result clone() {
			result p = new result();
			p.angle = angle; p.speed = speed;
			return p;
		}

		public String toString() {
			return "{"+angle+","+speed+"}";
		}
	}

	public int getAlpha() {
		return ALPHA;
	}

	public float[] getHist() {
		return hist_smoothed;
	}

	public static void main(String[] args) {

		LocalVFH2D poh = new LocalVFH2D(3f,0.05f);

		for(int i=40;i<120;i++)
			poh.hist_smoothed[i/ALPHA]  = 10f;
		for(int i=140;i<160;i++)
			poh.hist_smoothed[i/ALPHA]  = 10f;
		for(int i=250;i<270;i++)
			poh.hist_smoothed[i/ALPHA]  = 10f;

		System.out.println();


		poh.select(MSPMathUtils.toRad(180),0.5f);
		System.out.println(poh.toString());


		System.out.println("Result: "+ (int)MSPMathUtils.fromRad(poh.getSelectedDirection())+"° Speed: "+poh.getSelectedSpeed());

	}


}
