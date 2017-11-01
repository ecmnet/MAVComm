package com.comino.msp.slam.mapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.comino.msp.utils.MSPMathUtils;

import georegression.struct.point.Vector3D_F32;


public class LocalVFH2D {

	private static final float	DENSITY_A	= 10.0f;
	private static final float	DENSITY_B	= 0.25f;

	private static final int		ALPHA		= 2;
	private static final int  	SMOOTHING	= 5;
	private static final float  	THRESHOLD	= 0.5f;

	private static final float   THRESH_HIGH = 1000f;
	private static final float   THRESH_LOW  = 100f;


	private float 			u1 = 6.0f, u2 = 1.0f;

	private float[]  		hist;
	private float[]  		hist_smoothed;
	private float[]  		hist_last;

	private List<Float>		candidate_angle;
	private List<pair>       borders;

	private float				selected_tdir      = -1;
	private float 				last_selected_tdir = -1;

	public LocalVFH2D() {

		this.hist 			= new float[360 / ALPHA];
		this.hist_last 		= new float[360 / ALPHA];
		this.hist_smoothed 	= new float[360 / ALPHA];

		this.candidate_angle = new ArrayList<Float>();
		this.borders = new ArrayList<pair>();
	}


	public void update(LocalMap2D map, Vector3D_F32 current) {
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

		build_Binary_Polar_Histogram(hist_smoothed,0);
	}

	private void build_Binary_Polar_Histogram(float h[], int speed ) {
		for(int x=0; x<h.length;x++) {
			if (h[x] > THRESH_HIGH) {
				h[x] = 1.0f;
			} else if (h[x] < THRESH_LOW) {
				h[x] = 0.0f;
			} else {
				h[x] = hist_last[x];
			}
		}
		for(int x=0;x<h.length;x++)
			hist_last[x] = h[x];
	}

	public float getDirection(float tdir_rad, int smax) {
		return MSPMathUtils.toRad(getAbsoluteDirection(hist_smoothed,(int)MSPMathUtils.fromRad(tdir_rad), smax));
	}

	private float getAbsoluteDirection(float[] h, int tdir, int smax) {

		int start, left;
		float angle, new_angle, weight, min_weight;

		pair   new_border = new pair();

		borders.clear();
		candidate_angle.clear();

		start = -1; left = 1;

		for(int i=0;i<hist.length;i++) {
			if (h[i] > THRESHOLD) {
				start = i; 	break;
			}
		}

		if(start == -1) 	{
			last_selected_tdir = tdir;
			return tdir;
		}

		for(int i = start;i<start+hist.length+1;i++) {

			if ((h[i % hist.length] <= THRESHOLD) && (left==1)) {
				new_border.s = (i % hist.length) * ALPHA;
				left = 0;
			}
			if ((h[i % hist.length] > THRESHOLD) && (left==0)) {
				new_border.e = ((i % hist.length)-1) * ALPHA;
				if (new_border.e < 0 || new_border.s > new_border.e ) {
					new_border.e += 360;
				}

				borders.add(new_border.clone());
				left = 1;
			}
		}

		for(pair p : borders) {
			angle = delta_angle(p.s, p.e);
		//	System.out.print(p+" ["+angle+"]");

			// ignore narrow gaps
			if (Math.abs(angle) < 8)
				continue;

			if (Math.abs(angle) < 80) {
				// narrow opening: aim for the centre
				new_angle = p.s + (p.e - p.s) / 2.0f;
				candidate_angle.add(new_angle);

			} else {

				// wide opening: consider the centre, and 40deg from each border
				new_angle = p.s + (p.e - p.s) / 2.0f;
				candidate_angle.add(new_angle);
				new_angle = (float)((p.s + smax) % 360);
				candidate_angle.add(new_angle);
				new_angle = (float)(p.e - smax);
				if (new_angle < 0) 	new_angle += 360;
				candidate_angle.add(new_angle);

				// See if candidate dir is in this opening
				if(candidate_angle.get(candidate_angle.size()-1)>360) {
					tdir +=360;
				}
				if ((delta_angle(tdir, candidate_angle.get(candidate_angle.size()-2)) < 0) &&
					(delta_angle(tdir, candidate_angle.get(candidate_angle.size()-1)) > 0)) {
					candidate_angle.add((float)tdir);
				}


			}
		}

		if (candidate_angle.size() == 0) {
			// We're hemmed in by obstacles -- nowhere to go,
			// so brake hard and turn on the spot.
			System.err.println(this);
			return -1;
		}

		min_weight = 10000000;
		for(float selected : candidate_angle) {
			weight = u1 * Math.abs(delta_angle(tdir, selected)) +
					u2 * Math.abs(delta_angle(last_selected_tdir, selected));
			if(weight < min_weight) {
				min_weight = weight;
				selected_tdir = selected % 360;
				if(selected_tdir < 0)
					selected_tdir += 360;
			}
		}
		if(hist_smoothed[(int)selected_tdir/ALPHA]>THRESHOLD) {
			for(float selected : candidate_angle)
				System.err.print(" "+selected);
			System.err.println(" => "+selected_tdir);
			System.err.println(this);
		}
//		else
//		  System.out.println(this);
		last_selected_tdir = selected_tdir;
		return selected_tdir;
	}

	private float delta_angle(float a1, float a2) {
		return ( a2 - a1 ) % 360;
	}


	public String toString() {
		StringBuilder b = new StringBuilder();
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

	public static void main(String[] args) {
		int target = 0;

		LocalVFH2D poh = new LocalVFH2D();

		//		for(int i=40;i<120;i++)
		//			poh.hist_smoothed[i/ALPHA]  = 10f;
		//		for(int i=140;i<170;i++)
		//			poh.hist_smoothed[i/ALPHA]  = 10f;
		for(int i=240;i<260;i++)
			poh.hist_smoothed[i/ALPHA]  = 10f;

		System.out.println();


		target = (int)MSPMathUtils.fromRad(poh.getDirection(MSPMathUtils.toRad(0),  18));
		System.out.println(poh.toString());
		//		target = (int)MSPMathUtils.fromRad(poh.getDirection(MSPMathUtils.toRad(45), 18));
		//		System.out.println(poh.toString());
		//		target = (int)MSPMathUtils.fromRad(poh.getDirection(MSPMathUtils.toRad(130), 18));
		//		System.out.println(poh.toString());
		//		target = (int)MSPMathUtils.fromRad(poh.getDirection(MSPMathUtils.toRad(200), 18));
		//		System.out.println(poh.toString());
		//		target = (int)MSPMathUtils.fromRad(poh.getDirection(MSPMathUtils.toRad(258), 18));
		//		System.out.println(poh.toString());
		//		target = (int)MSPMathUtils.fromRad(poh.getDirection(MSPMathUtils.toRad(340), 18));
		//		System.out.println(poh.toString());

		System.out.println(target);

	}


}
