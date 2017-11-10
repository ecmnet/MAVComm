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

package com.comino.msp.slam.vfh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.comino.msp.slam.map.ILocalMap;
import com.comino.msp.slam.map.impl.LocalMap2D;
import com.comino.msp.utils.MSPMathUtils;

import georegression.struct.point.Vector3D_F32;

// VFH and VFH+ articles:
//- <A HREF="http://www-personal.umich.edu/~johannb/Papers/paper17.pdf"> VFH </A>
//- <A HREF="http://www-personal.umich.edu/~johannb/Papers/paper17.pdf"> VFH+ </A>


public class LocalVFH2D {

	private static final int     SMAX        		= 80;

	private static final int		ALPHA				= 2;
	private static final float  	HIST_THRESHOLD	    = 1f;

	private static final int MAX_ACCELERATION		= 100;

	private static final int MAX_SPEED   	    		= 800;
	private static final int MAX_SPEED_WIDE			= 500;
	private static final int MAX_SPEED_NARROW    	= 300;

	private ILocalMap 		map = null;


	private float 			u1 = 6.0f, u2 = 1f;

	private float[][]    	magnitude;
	private float[][]    	distance;

	private float[]  		hist;

	private List<result>		results;
	private List<pair>       borders;

	private float			desired_tdir			= 0;
	private float			selected_tdir       	= 0;
	private float 			last_selected_tdir  	= 0;

	private float			selected_speed	  	= 0;
	private float         	last_selected_speed 	= 0;

	private int   			max_speed_for_selected_angle;

	private long				last_update_time		=0;

	private int 			   safety_dist_0ms		= 100;
	private int 			   safety_dist_1ms    	= 200;

	private int				robot_radius        = 0;
	private int              threshold           = 0;

	public LocalVFH2D(ILocalMap map, float robot_radius_m, int threshold) {

		this.map = map;

		this.robot_radius = (int)(robot_radius_m*1000.0f);
		this.threshold = threshold;

		this.results = new ArrayList<result>();
		this.borders = new ArrayList<pair>();

		this.hist 				= new float[360 / ALPHA];

		int window_dim = map.getWindowDimension();

		this.magnitude = new float[window_dim][window_dim];
		this.distance  = new float[window_dim][window_dim];

		for (int y = 0; y < window_dim; y++) {
			for (int x = 0; x < window_dim; x++) {
				distance[x][y]  = (float)Math.sqrt((x - window_dim/2)*(x - window_dim/2)  +
						(y - window_dim/2)*(y - window_dim/2)) * map.getCellSize_mm();
				magnitude[x][y] = (float)(Math.pow(((window_dim*map.getCellSize_mm()) - distance[x][y] ), 4) / 10000000.0);
			}
		}

	}

	public float getSelectedDirection() {
		return MSPMathUtils.toRad(selected_tdir);
	}

	public float getSelectedSpeed() {
		return selected_speed / 1000f;
	}

	public int getAlpha() {
		return ALPHA;
	}

	public float[] getHist() {
		return hist;
	}

	public void setInitialSpeed(float current_speed) {
		selected_speed = Math.min(current_speed * 1000.0f, max_speed_for_selected_angle );
		last_selected_speed = selected_speed;
	}

	public void update_histogram(Vector3D_F32 current, float current_speed) {
		int beta = 0; int sigma;

		float min_distance = this.robot_radius;

		Arrays.fill(hist, 0);

	    map.processWindow(current.x, current.y);
	    int window_center = map.getWindowDimension()/2;

		// Build density polar histogram
		for (int y = 0; y < map.getWindowDimension(); y++) {
			for (int x = 0; x < map.getWindowDimension(); x++) {

				if(map.getWindowValue(x, y) <= threshold)
					continue;

				beta = (int)(MSPMathUtils.fromRad((float)Math.atan2((y - window_center), (x - window_center)))+180) % 360;
				if(beta < 0) beta += 360;
				sigma = Math.abs((int)MSPMathUtils.fromRad((float)Math.asin(min_distance/distance[x][y])));

				if(sigma > 0) {
					for(int i= beta-sigma+1; i<beta+sigma; i++) {
						if(i < 0)
							hist[(i+360)/ALPHA] = magnitude[x][y];
						else
							hist[(i%360)/ALPHA] = magnitude[x][y];
					}
				}
				else
					hist[beta/ALPHA] = magnitude[x][y];
			}
		}
	}


	public void select(float tdir_rad, float current_speed, float distance_to_goal_mm) {

		float diffSeconds = (System.currentTimeMillis() - last_update_time ) / 1000.0f;
		last_update_time = System.currentTimeMillis();

		if(getAbsoluteDirection(hist,(int)MSPMathUtils.fromRad(tdir_rad), SMAX) < 0) {
			System.err.println("NO PATH");
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

		if ( cantTurnToTarget(distance_to_goal_mm, tdir_rad, current_speed)) {
			speed_incr = - 3 * speed_incr;
		} else {
			if(distance_to_goal_mm < 750 && last_selected_speed > 200)
				speed_incr = - 3 * speed_incr;
		}

		selected_speed = Math.min( last_selected_speed + speed_incr, max_speed_for_selected_angle );
		if(selected_speed < 0) selected_speed = 0;
		last_selected_speed = selected_speed;
	}

	private boolean cantTurnToTarget(float distance_to_target_mm, float tdir_rad, float speed) {

		float blocked_circle_radius = (robot_radius + get_Safety_Dist(speed))/1000f;

		float dist_between_centres;

		float goal_x = (float)(distance_to_target_mm/1000f * Math.cos(tdir_rad));
		float goal_y = (float)(distance_to_target_mm/1000f * Math.sin(tdir_rad));

		// right circle
		dist_between_centres = (float)Math.hypot( goal_x - blocked_circle_radius, goal_y );
		if ( dist_between_centres+0.3 < blocked_circle_radius )
			return true;

		// left circle
		dist_between_centres = (float)Math.hypot( -goal_x - blocked_circle_radius, goal_y );
		if ( dist_between_centres+0.3f < blocked_circle_radius )
			return true;

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
			if (h[i] > HIST_THRESHOLD) {
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

			if ((h[i % hist.length] <= HIST_THRESHOLD) && (left==1)) {
				new_border.s = (i % hist.length) * ALPHA;
				left = 0;
			}
			if ((h[i % hist.length] > HIST_THRESHOLD) && (left==0)) {
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

			// ignore narrow gaps
			if (Math.abs(angle) <10)
				continue;

			if (Math.abs(angle) < smax) {

				new_result.speed = MAX_SPEED_NARROW;
				// narrow opening: aim for the centre
				new_result.angle = p.s + (p.e - p.s) / 2.0f;
				results.add(new_result.clone());

			} else {
				new_result.speed = MAX_SPEED_WIDE;
				// wide opening: consider the centre, and 40deg from each border
				new_result.angle = p.s + (p.e - p.s) / 2.0f;
				results.add(new_result.clone());
				new_result.angle = (float)((p.s + smax/2));
				results.add(new_result.clone());
				new_result.angle = (float)(p.e - smax/2);
				if (new_result.angle < 0) new_result.angle += 360;
				results.add(new_result.clone());

				// See if candidate dir is in this opening
				if(p.e>360)	tdir +=360;
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

			// Cost function;
			weight = u1 * Math.abs(delta_angle_180(tdir,selected.angle))
			       + u2 * Math.abs(delta_angle_180(last_selected_tdir,selected.angle));

			if(weight < min_weight) {
				min_weight = weight;
				selected_tdir = selected.angle % 360;
				if(selected_tdir < 0)
					selected_tdir += 360;
				max_speed_for_selected_angle = selected.speed;
			}
			//			System.out.print("["+tdir+","+selected.angle+":"+(Math.abs(delta_angle(tdir, selected.angle)))+"=>"+weight+"] ");
		}
		//	System.out.println(" ==> "+selected_tdir);

		last_selected_tdir  = selected_tdir;
		return selected_tdir;
	}


	private float get_Safety_Dist( float speed ) {
		float val = safety_dist_0ms + (speed*( safety_dist_1ms-safety_dist_0ms ));
		if ( val < 0 )
			val = 0;
		return val;
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
				if(hist[i]<HIST_THRESHOLD)
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
			p.angle  = angle;
			p.speed  = speed;
			return p;
		}

		public String toString() {
			return "{"+angle+","+speed+"}";
		}
	}

	public static void main(String[] args) {

		LocalMap2D map = new LocalMap2D(20f,0.05f,1.5f,1);

		LocalVFH2D poh = new LocalVFH2D(map, 0.3f, 1);

		for(int i=40;i<120;i++)
			poh.hist[i/ALPHA]  = 10f;
		for(int i=140;i<160;i++)
			poh.hist[i/ALPHA]  = 10f;
		for(int i=250;i<270;i++)
			poh.hist[i/ALPHA]  = 10f;

		System.out.println();


		poh.select(MSPMathUtils.toRad(180),0.5f,1);
		System.out.println(poh.toString());

		System.out.println("Result: "+ (int)MSPMathUtils.fromRad(poh.getSelectedDirection())+"° Speed: "+poh.getSelectedSpeed());

	}
}
