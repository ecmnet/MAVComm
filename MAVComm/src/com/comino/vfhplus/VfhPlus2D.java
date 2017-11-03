package com.comino.vfhplus;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.comino.msp.slam.map.LocalMap2D;
import com.comino.msp.utils.MSPMathUtils;

import georegression.struct.point.Vector3D_F32;

// Code transferred from https://github.com/morpheus1820/ros-nodes/blob/master/vfh/src/vfh_algorithm.cc

public class VfhPlus2D {


	float 			robot_radius					= 400;      // millimeters
	int 				center_x;                 				// cells
	int 				center_y;                 				// cells
	int 				hist_size;                				// sectors (over 360deg)

	float 			cell_width					= 50;       // millimeters
	int 				window_diameter          	= 60;		// cells
	int 				sector_angle					= 5;        // degrees
	float 			safety_dist_0ms				= 100;      // millimeters
	float 			safety_dist_1ms        	    = 300;		// millimeters
	int 				current_max_speed;        				// mm/sec
	int 				max_speed					= 400;      // mm/sec
	int 				max_speed_narrow_opening    	= 50; 	    // mm/sec
	int 				max_speed_wide_opening		= 300;  		// mm/sec
	int 				max_acceleration				= 100;      // mm/sec/sec
	int 				min_turnrate             	= 0;		    // deg/sec -- not actually used internally

	int 				num_cell_sector_tables		= 20;		// if safety_dist_0ms == safety_dist_1ms put that to 1

	// scale turnrate linearly between these two

	int 				max_turnrate_0ms				= 80;       	// deg/sec
	int 				max_turnrate_1ms				= 40;       	// deg/sec

	double 			min_turn_radius_safety_factor 	= 1;

	// Obstacle avoiding factors: higher -> closer the robot will get to obstacles before avoiding (stopped, at 1m/s)
	float 			binary_hist_low_0ms			= 4000000.0f;
	float			binary_hist_high_0ms			= 4000000.0f;
	float 			binary_hist_low_1ms			= 2000000.0f;
	float			binary_hist_high_1ms			= 2000000.0f;

	private float 			u1 = 6.0f, u2 = 1.0f;

	private float 			desired_angle, dist_to_goal;
	private float 			goal_distance_tolerance;
	private float 			picked_angle;
	private float 			last_picked_angle;
	private float			current_dir;

	private int				last_chosen_speed;
	private int 				chosen_speed;

	private float			chosen_turn_rate;

	private int   			max_speed_for_picked_angle;

	private float 			blocked_circle_radius;  					// radius of dis-allowed circles, either side of the robot, which
	// we can't enter due to our minimum turning radius
	private double dist_eps;
	private double ang_eps;

	private float[] hist;
	private float[] last_binary_hist;

	private float[][]				cell_direction;
	private float[][]    			cell_base_mag;
	private float[][]				cell_mag;
	private float[][]				cell_dist;      					// millimetres
	private float[][]				cell_enlarge;

	private List<Integer>[][][]      cell_sector;

	private int[]					min_turning_radius;

	private List<Float>				candidate_angle;
	private List<Short> 			    candidate_speed;

	private long						last_update_time;

	public void update_VFH( LocalMap2D map, Vector3D_F32 current, Vector3D_F32 target, float speed_ms, float goal_distance_tol_m) {
		current_dir = MSPMathUtils.fromRad((float)Math.atan2(current.y, current.x));
		float goal_angle = MSPMathUtils.fromRad((float)Math.atan2(target.y - current.y, target.x - current.x)) - current_dir;
		if(goal_angle<0)
			goal_angle += 360;
		float goal_dist  = (float)(Math.sqrt((target.y - current.y)*(target.y - current.y) + (target.x - current.x)*(target.x - current.x)) * 1000f);
//		update_VFH(map.getWindowPolar((int)current_dir, current.x, current.y),(int)(speed_ms*1000f), goal_angle,goal_dist, goal_distance_tol_m * 1000f);
	}


	public void update_VFH(float[] ranges, int current_speed,float goal_direction, float goal_distance, float goal_distance_tol) {
		//    int &chosen_speed,
		//    int &chosen_turnrate )

		desired_angle = goal_direction;
		dist_to_goal  = goal_distance;
		goal_distance_tolerance = goal_distance_tol;

		int current_pos_speed;

		if ( current_speed < 0 )
			current_pos_speed = 0;
		else
			current_pos_speed = current_speed;

		if ( current_pos_speed < last_chosen_speed )
			current_pos_speed = last_chosen_speed;

		float diffSeconds = (System.currentTimeMillis() - last_update_time ) / 1000.0f;
		last_update_time = System.currentTimeMillis();

		if ( !calculate_Cells_Mag(ranges, current_pos_speed ) ) {
			// Something's inside our safety distance: brake hard and
			// turn on the spot
			Arrays.fill(hist, 1);
			picked_angle = last_picked_angle;
			max_speed_for_picked_angle = 0;

		} else {

			build_Primary_Polar_Histogram(current_pos_speed);
			build_Binary_Polar_Histogram(current_pos_speed);
			build_Masked_Polar_Histogram(current_pos_speed);

			// Sets Picked_Angle, Last_Picked_Angle, and Max_Speed_For_Picked_Angle.
			select_Direction();
		}

		// OK, so now we've chosen a direction.  Time to choose a speed.
		int speed_incr;

		if ( (diffSeconds > 0.3) || (diffSeconds < 0) ) {
			// Either this is the first time we've been updated, or something's a bit screwy and
			// update hasn't been called for a while.  Don't want a sudden burst of acceleration,
			// so better to just pick a small value this time, calculate properly next time.
			speed_incr = 10;
		} else {
			speed_incr = (int) (max_acceleration * diffSeconds);
		}

		if ( cant_Turn_To_Goal() ) {
			//		      printf("The goal's too close -- we can't turn tightly enough to get to it, so slow down...");
			speed_incr = -speed_incr;
		}

		// Accelerate (if we're not already at Max_Speed_For_Picked_Angle).
		chosen_speed = Math.min( last_chosen_speed + speed_incr, max_speed_for_picked_angle );
		set_Motion(current_speed);

		last_chosen_speed = chosen_speed;
	}

	public float getTurnrate() {
		return chosen_turn_rate;
	}

	public float getSpeed() {
		return chosen_speed;
	}

	public float getAngleRel() {
		return picked_angle;
	}

	public float getAngle() {
		return (picked_angle+current_dir) % 360.0f;
	}

	private void set_Motion(int actual_speed )
	{
		// This happens if all directions blocked, so just spin in place
		if (chosen_speed <= 0) {
			//printf("stop\n");
			chosen_turn_rate = getMaxTurnrate( actual_speed );
			chosen_speed = 0;
		} else {
			//	    printf("Picked %f\n", Picked_Angle);
			if ((picked_angle > 270) && (picked_angle < 360)) {
				chosen_turn_rate = -1 * getMaxTurnrate( actual_speed );
			} else if ((picked_angle < 270) && (picked_angle > 180)) {
				chosen_turn_rate = getMaxTurnrate( actual_speed );
			} else {
				chosen_turn_rate = (int)Math.rint(((float)(picked_angle - 90) / 75.0) * getMaxTurnrate( actual_speed ));
				//	    	turnrate = (int)rint(((float)(Picked_Angle) / 75.0) * GetMaxTurnrate( actual_speed ));
				//	      printf("GetMaxTurnrate( actual_speed ): %d, Picked_Angle: %f, turnrate %d\n",
				//	    		  GetMaxTurnrate( actual_speed ),
				//	    		  Picked_Angle,
				//	    		  turnrate);

				if (chosen_turn_rate > getMaxTurnrate( actual_speed )) {
					chosen_turn_rate = getMaxTurnrate( actual_speed );
				} else if (chosen_turn_rate < (-1 * getMaxTurnrate( actual_speed ))) {
					chosen_turn_rate = -1 * getMaxTurnrate( actual_speed );
				}

				//	      if (abs(turnrate) > (0.9 * GetMaxTurnrate( actual_speed ))) {
				//	        speed = 0;
				//	      }
			}
		}

		//  speed and turnrate have been set for the calling function -- return.
	}

	private void select_Direction() {
		int start, i, left;
		float angle, new_angle;

		List<pair> border = new ArrayList<pair>();
		pair   new_border = new pair();

		candidate_angle.clear();
		candidate_speed.clear();

		start = -1;

		for(i=0;i<hist_size/2;i++) {
			if (hist[i] == 1) {
				start = i;
				break;
			}
		}

		if (start == -1) {
			picked_angle = desired_angle;
			last_picked_angle = picked_angle;
			max_speed_for_picked_angle = current_max_speed;
			//		      printf("No obstacles detected in front of us: full speed towards goal: %f, %f, %d\n",
			//		    		  Picked_Angle, Last_Picked_Angle, Max_Speed_For_Picked_Angle);
			return;
		}
		left = 1;
		for(i=start;i<=(start+hist_size);i++) {
			if ((hist[i % hist_size] == 0) && (left==1)) {
				new_border.s = (i % hist_size) * sector_angle;
				left = 0;
			}

			if ((hist[i % hist_size] == 1) && (left==0)) {
				new_border.e = ((i % hist_size) - 1) * sector_angle;
				if (new_border.e < 0) {
					new_border.e += 360;
				}
				border.add(new_border.clone());
				left = 1;
			}
		}

		for(pair p : border) {
			angle = delta_Angle(p.s, p.e);
			// ignore very narrow openings
			if (Math.abs(angle) < 10)
				continue;

			if (Math.abs(angle) < 80) {
				// narrow opening: aim for the centre
				new_angle = p.s + (p.e - p.s) / 2.0f;
				candidate_angle.add(new_angle);
				candidate_speed.add((short)Math.min(current_max_speed,max_speed_narrow_opening));
			} else {
				// wide opening: consider the centre, and 40deg from each border
				new_angle = p.s + (p.e - p.s) / 2.0f;
				candidate_angle.add(new_angle);
				candidate_speed.add((short)current_max_speed);

				new_angle = (float)((p.s + 40) % 360);
				candidate_angle.add(new_angle);
				candidate_speed.add((short)Math.min(current_max_speed,max_speed_wide_opening));

				new_angle = (float)(p.e - 40);
				if (new_angle < 0)
					new_angle += 360;
				candidate_angle.add(new_angle);
				candidate_speed.add((short)Math.min(current_max_speed,max_speed_wide_opening));

				if ((delta_Angle(desired_angle, candidate_angle.get(candidate_angle.size()-2)) < 0) &&
						(delta_Angle(desired_angle, candidate_angle.get(candidate_angle.size()-1)) > 0)) {
					candidate_angle.add(desired_angle);
					candidate_speed.add((short)Math.min(current_max_speed,max_speed_wide_opening));
				}
			}
		}

		select_Candidate_Angle();
	}

	private void select_Candidate_Angle()
	{
		int i;
		float weight, min_weight;

		if (candidate_angle.size() == 0) {
			// We're hemmed in by obstacles -- nowhere to go,
			// so brake hard and turn on the spot.

			picked_angle = last_picked_angle;
			max_speed_for_picked_angle = 0;
			return;
		}

		picked_angle = 90;
		min_weight = 10000000;

		for(i=0;i<candidate_angle.size();i++)   {
			//printf("CANDIDATE: %f\n", Candidate_Angle[i]);
			weight = u1 * Math.abs(delta_Angle(desired_angle, candidate_angle.get(i))) +
					 u2 * Math.abs(delta_Angle(last_picked_angle, candidate_angle.get(i)));
			if (weight < min_weight) {
				min_weight = weight;
				picked_angle = candidate_angle.get(i);
				max_speed_for_picked_angle = candidate_speed.get(i);
			}
		}
		last_picked_angle = picked_angle;
	}

	private void build_Masked_Polar_Histogram(int speed) {

		int x, y;
		float center_x_right, center_x_left, dist_r, dist_l;
		float angle_ahead, phi_left, phi_right, angle;

		// center_x_[left|right] is the centre of the circles on either side that
		// are blocked due to the robot's dynamics.  Units are in cells, in the robot's
		// local coordinate system (+y is forward).
		center_x_right = center_x + (min_turning_radius[speed] / (float)cell_width);
		center_x_left = center_x -  (min_turning_radius[speed] / (float)cell_width);

		angle_ahead = 90;
		phi_left  = 180;
		phi_right = 0;

		blocked_circle_radius = min_turning_radius[speed] + robot_radius + get_Safety_Dist(speed);

		//
		// This loop fixes phi_left and phi_right so that they go through the inside-most
		// occupied cells inside the left/right circles.  These circles are centred at the
		// left/right centres of rotation, and are of radius Blocked_Circle_Radius.
		//
		// We have to go between phi_left and phi_right, due to our minimum turning radius.
		//

		//
		// Only loop through the cells in front of us.
		//
		for(y=0;y<(int)Math.ceil(window_diameter/2.0);y++)  {
			for(x=0;x<window_diameter;x++) {
				if (cell_mag[x][y] == 0)
					continue;
				if ((delta_Angle(cell_direction[x][y], angle_ahead) > 0) &&
						(delta_Angle(cell_direction[x][y], phi_right) <= 0))  {
					// The cell is between phi_right and angle_ahead
					dist_r = (float)Math.hypot(center_x_right - x, center_y - y) * cell_width;
					if (dist_r < blocked_circle_radius)
						phi_right = cell_direction[x][y];

				} else if ((delta_Angle(cell_direction[x][y], angle_ahead) <= 0) &&
						(delta_Angle(cell_direction[x][y], phi_left) > 0)) {
					// The cell is between phi_left and angle_ahead
					dist_l = (float)Math.hypot(center_x_left - x, center_y - y) * cell_width;
					if (dist_l < blocked_circle_radius)
						phi_left = cell_direction[x][y];
				}
			}
		}

		//
		// Mask out everything outside phi_left and phi_right
		//
		for(x=0;x<hist_size;x++)
		{
			angle = x * sector_angle;
			if ((hist[x] == 0) && (((delta_Angle((float)angle, phi_right) <= 0) &&
					(delta_Angle((float)angle, angle_ahead) >= 0)) ||
					((delta_Angle((float)angle, phi_left) >= 0) &&
							(delta_Angle((float)angle, angle_ahead) <= 0))))
				hist[x] = 0;
			else
				hist[x] = 1;
		}

	}

	private void build_Binary_Polar_Histogram( int speed ) {
		int x;

		for(x=0;x<hist_size;x++) {
			if (hist[x] > get_Binary_Hist_High(speed)) {
				hist[x] = 1.0f;
			} else if (hist[x] < get_Binary_Hist_Low(speed)) {
				hist[x] = 0.0f;
			} else {
				hist[x] = last_binary_hist[x];
			}
		}

		for(x=0;x<hist_size;x++) {
			last_binary_hist[x] = hist[x];
		}
	}

	private void build_Primary_Polar_Histogram( int speed ) {
		int x, y, i;

		// index into the vector of Cell_Sector tables
		int speed_index = get_Speed_Index( speed );

		for(y=0;y<=(int)Math.ceil(window_diameter/2.0);y++) {
			for(x=0;x<window_diameter;x++) {
				for(i=0;i<cell_sector[speed_index][x][y].size();i++) {
					hist[cell_sector[speed_index][x][y].get(i)]+=cell_mag[x][y];
				}
			}
		}
		return;
	}

	private boolean calculate_Cells_Mag( float[] ranges, int speed ) {

		int x, y;

		float r = robot_radius +  (float) get_Safety_Dist(speed);

		Arrays.fill(hist, 0);

		for(x=0;x<window_diameter;x++) {
			for(y=0;y<(int)Math.ceil(window_diameter/2.0);y++) {
				if ((cell_dist[x][y] + cell_width / 2.0) > ranges[(int)Math.rint(cell_direction[x][y] * 2.0)]) {
					if ( cell_dist[x][y] < r && !(x==center_x && y==center_y) )
						// Damn, something got inside our safety_distance...
						// Short-circuit this process.
						return false;
					cell_mag[x][y] = cell_base_mag[x][y];
				} else
					cell_mag[x][y] = 0.0f;
			}
		}
		return true;
	}

	public void init() {

		float plus_dir=0, neg_dir=0, plus_sector=0, neg_sector=0;
		boolean plus_dir_bw, neg_dir_bw, dir_around_sector;
		float neg_sector_to_neg_dir=0, neg_sector_to_plus_dir=0;
		float plus_sector_to_neg_dir=0, plus_sector_to_plus_dir=0;
		int max_speed_this_table;
		float r;

		center_x = (int)Math.floor(window_diameter / 2.0);
		center_y = center_x;
		hist_size = (int)Math.rint(360.0 / sector_angle);

		allocate(hist_size);

		Arrays.fill(hist, 0);
		Arrays.fill(last_binary_hist, 1);

		for(int x=0;x<window_diameter;x++) {
			for(int y=0;y<window_diameter;y++) {

				cell_mag[x][y] = 0;
				cell_dist[x][y] = (float)Math.sqrt(Math.pow((center_x - x), 2) + Math.pow((center_y - y), 2)) * cell_width;
				cell_base_mag[x][y] = (float)(Math.pow((3000.0 - cell_dist[x][y]), 4) / 100000000.0);

				// set up cell_direction with the angle in degrees to each cell

				if (x < center_x) {
					if (y < center_y) {
						cell_direction[x][y] = (float)Math.atan((float)(center_y - y) / (float)(center_x - x));
						cell_direction[x][y] *= (360.0f / 6.28f);
						cell_direction[x][y] = 180.0f - cell_direction[x][y];
					} else if (y == center_y) {
						cell_direction[x][y] = 180.0f;
					} else if (y > center_y) {
						cell_direction[x][y] = (float)Math.atan((float)(y - center_y) / (float)(center_x - x));
						cell_direction[x][y] *= (360.0f / 6.28f);
						cell_direction[x][y] = 180.0f + cell_direction[x][y];
					}
				} else if (x == center_x) {
					if (y < center_y) {
						cell_direction[x][y] = 90.0f;
					} else if (y == center_y) {
						cell_direction[x][y] = -1.0f;
					} else if (y > center_y) {
						cell_direction[x][y] = 270.0f;
					}
				} else if (x > center_x) {
					if (y < center_y) {
						cell_direction[x][y] = (float)Math.atan((float)(center_y - y) / (float)(x - center_x));
						cell_direction[x][y] *= (360.0f / 6.28f);
					} else if (y == center_y) {
						cell_direction[x][y] = 0.0f;
					} else if (y > center_y) {
						cell_direction[x][y] = (float)Math.atan((float)(y - center_y) / (float)(x - center_x));
						cell_direction[x][y] *= (360.0f / 6.28f);
						cell_direction[x][y] = 360.0f - cell_direction[x][y];
					}
				}

				for (int cell_sector_tablenum = 0; cell_sector_tablenum < num_cell_sector_tables; cell_sector_tablenum++ ) {

					max_speed_this_table = (int) (((float)(cell_sector_tablenum+1)/(float)num_cell_sector_tables) *(float)max_speed);

					// Set Cell_Enlarge to the _angle_ by which a an obstacle must be
					// enlarged for this cell, at this speed
					if (cell_dist[x][y] > 0) {
						r = robot_radius + get_Safety_Dist(max_speed_this_table);
						// Cell_Enlarge[x][y] = (float)atan( r / Cell_Dist[x][y] ) * (180/M_PI);
						cell_enlarge[x][y] = (float)(Math.asin( r / cell_dist[x][y] ) * (180/Math.PI));
					}
					else
						cell_enlarge[x][y] = 0;

					cell_sector[cell_sector_tablenum][x][y].clear();
					plus_dir = cell_direction[x][y] + cell_enlarge[x][y];
					neg_dir  = cell_direction[x][y] - cell_enlarge[x][y];

					for(int i=0;i<(360 / sector_angle);i++) {

						// Set plus_sector and neg_sector to the angles to the two adjacent sectors
						plus_sector = (i + 1) * (float)sector_angle;
						neg_sector = i * (float)sector_angle;

						if ((neg_sector - neg_dir) > 180) {
							neg_sector_to_neg_dir = neg_dir - (neg_sector - 360);
						} else {
							if ((neg_dir - neg_sector) > 180) {
								neg_sector_to_neg_dir = neg_sector - (neg_dir + 360);
							} else {
								neg_sector_to_neg_dir = neg_dir - neg_sector;
							}
						}

						if ((plus_sector - neg_dir) > 180) {
							plus_sector_to_neg_dir = neg_dir - (plus_sector - 360);
						} else {
							if ((neg_dir - plus_sector) > 180) {
								plus_sector_to_neg_dir = plus_sector - (neg_dir + 360);
							} else {
								plus_sector_to_neg_dir = neg_dir - plus_sector;
							}
						}

						if ((plus_sector - plus_dir) > 180) {
							plus_sector_to_plus_dir = plus_dir - (plus_sector - 360);
						} else {
							if ((plus_dir - plus_sector) > 180) {
								plus_sector_to_plus_dir = plus_sector - (plus_dir + 360);
							} else {
								plus_sector_to_plus_dir = plus_dir - plus_sector;
							}
						}

						if ((neg_sector - plus_dir) > 180) {
							neg_sector_to_plus_dir = plus_dir - (neg_sector - 360);
						} else {
							if ((plus_dir - neg_sector) > 180) {
								neg_sector_to_plus_dir = neg_sector - (plus_dir + 360);
							} else {
								neg_sector_to_plus_dir = plus_dir - neg_sector;
							}
						}

						plus_dir_bw = false;
						neg_dir_bw = false;
						dir_around_sector =false;

						if ((neg_sector_to_neg_dir >= 0) && (plus_sector_to_neg_dir <= 0)) {
							neg_dir_bw = true;
						}

						if ((neg_sector_to_plus_dir >= 0) && (plus_sector_to_plus_dir <= 0)) {
							plus_dir_bw =true;
						}

						if ((neg_sector_to_neg_dir <= 0) && (neg_sector_to_plus_dir >= 0)) {
							dir_around_sector = true;
						}

						if ((plus_sector_to_neg_dir <= 0) && (plus_sector_to_plus_dir >= 0)) {
							plus_dir_bw = true;
						}

						if ((plus_dir_bw) || (neg_dir_bw) || (dir_around_sector)) {
							cell_sector[cell_sector_tablenum][x][y].add(i);
						}
					}
				}
			}
		}
//		last_update_time = System.currentTimeMillis();
	}

	private void setCurrentMaxSpeed( int max_speed )
	{
		current_max_speed = Math.min( max_speed, this.max_speed );
		min_turning_radius = new int[current_max_speed+1];

		// small chunks of forward movements and turns-in-place used to
		// estimate turning radius, coz I'm too lazy to screw around with limits -> 0.
		double dx, dtheta;

		//
		// Calculate the turning radius, indexed by speed.
		// Probably don't need it to be precise (changing in 1mm increments).
		//
		// WARNING: This assumes that the max_turnrate that has been set for VFH is
		//          accurate.
		//
		for(int x=0;x<=current_max_speed;x++) {
			dx = (double) x / 1e6; // dx in m/millisec
			dtheta = ((Math.PI/180)*(double)(getMaxTurnrate(x))) / 1000.0; // dTheta in radians/millisec
			min_turning_radius[x] = (int) ( ((dx / Math.tan( dtheta ))*1000.0) * min_turn_radius_safety_factor ); // in mm
		}
	}

	private void allocate(int cell_sector_size) {

		candidate_angle 		= new ArrayList<Float>();
		candidate_speed 		= new ArrayList<Short>();

		cell_direction 		= new float[window_diameter][window_diameter];
		cell_base_mag  		= new float[window_diameter][window_diameter];
		cell_mag  	   		= new float[window_diameter][window_diameter];
		cell_dist  	   		= new float[window_diameter][window_diameter];
		cell_enlarge   		= new float[window_diameter][window_diameter];

		cell_sector    		= new List[num_cell_sector_tables][window_diameter][window_diameter];

		for(int t=0;t<num_cell_sector_tables;t++)
			for(int x=0;x<window_diameter;x++)
				for(int y=0;y<window_diameter;y++)
					cell_sector[t][x][y] = new ArrayList<Integer>(0);

		hist 				= new float[hist_size];
		last_binary_hist 	= new float[hist_size];

		setCurrentMaxSpeed(max_speed);

		System.out.println("VFHPlus2D initialized ("+
				(cell_sector.length*cell_sector[0].length * cell_sector[0][0].length*cell_sector[0][0][0].size()/1024)
				+"k)");

	}

	private float delta_Angle(float a1, float a2) {
		float diff = a2 - a1;
		if (diff > 180) {
			diff -= 360;
		} else if (diff < -180) {
			diff += 360;
		}
		return(diff);
	}

	//	private int bisect_Angle(int angle1, int angle2) {
	//		float a; int angle;
	//
	//		a = delta_Angle((float)angle1, (float)angle2);
	//		angle = (int)Math.rint(angle1 + (a / 2.0));
	//		if (angle < 0) {
	//			angle += 360;
	//		} else if (angle >= 360) {
	//			angle -= 360;
	//		}
	//		return(angle);
	//	}

	private boolean cant_Turn_To_Goal()
	{
		// Calculate this by seeing if the goal is inside the blocked circles
		// (circles we can't enter because we're going too fast).  Radii set
		// by Build_Masked_Polar_Histogram.

		// Coords of goal in local coord system:
		float goal_x = (float)(dist_to_goal * Math.cos( MSPMathUtils.toRad(desired_angle)));
		float goal_y = (float)(dist_to_goal * Math.sin( MSPMathUtils.toRad(desired_angle)));

		// AlexB: Is this useful?
		//	     if ( goal_y < 0 )
		//	     {
		//	         printf("Goal behind\n");
		//	         return true;
		//	     }

		// This is the distance between the centre of the goal and
		// the centre of the blocked circle
		float dist_between_centres;

		// right circle
		dist_between_centres = (float)Math.hypot( goal_x - blocked_circle_radius, goal_y );
		if ( dist_between_centres+goal_distance_tolerance < blocked_circle_radius )
			return true;

		// left circle
		dist_between_centres = (float)Math.hypot( -goal_x - blocked_circle_radius, goal_y );
		if ( dist_between_centres+goal_distance_tolerance < blocked_circle_radius )
			return true;

		return false;
	}


	private float get_Binary_Hist_Low( int speed ) {
		return ( binary_hist_low_0ms - (speed*( binary_hist_low_0ms-binary_hist_low_1ms )/1000.0f) );
	}

	private float get_Binary_Hist_High( int speed ) {
		return ( binary_hist_high_0ms - (speed*( binary_hist_high_0ms-binary_hist_high_1ms )/1000.0f) );
	}

	private int get_Safety_Dist( int speed ) {
		int val = (int) ( safety_dist_0ms + (int)(speed*( safety_dist_1ms-safety_dist_0ms )/1000.0) );
		if ( val < 0 )
			val = 0;
		return val;
	}

	private int getMaxTurnrate( int speed ) {
		int val = ( max_turnrate_0ms - (int)(speed*( max_turnrate_0ms-max_turnrate_1ms )/1000.0) );
		if ( val < 0 )
			val = 0;
		return val;
	}

	private int get_Speed_Index( int speed ) {
		int val = (int) Math.floor(((float)speed/(float)current_max_speed)*num_cell_sector_tables);
		if ( val >= num_cell_sector_tables )
			val = num_cell_sector_tables-1;
		//	     printf("Speed_Index at %dmm/s: %d\n",speed,val);
		return val;
	}

	private class pair {
		int s,e;

		public pair clone() {
			pair p = new pair();
			p.s = s;
			p.e = e;
			return p;
		}
	}

	public void print_Cells_Sector()
	{
		int x, y;
		int i=0;

		System.out.format("\nCell Sectors for table 0:\n");
		System.out.format("***************************\n");

		for(y=0;y<window_diameter;y++) {
			for(x=0;x<window_diameter;x++) {
				for(i=0;i<cell_sector[0][x][y].size();i++) {
					if (i < (cell_sector[0][x][y].size() -1 )) {
						System.out.format("%d,", cell_sector[0][x][y].get(i));
					} else {
						System.out.format("%d\t\t", cell_sector[0][x][y].get(i));
					}
				}
			}
			System.out.format("\n");
		}
	}

	public void print_Hist()
	{
		int x;
		System.out.format("Histogram:\n");
		System.out.format("****************\n");

		for(x=0;x<=(hist_size/2);x++) {
			System.out.format("%d,%1.1f\n", (x * sector_angle), hist[x]);
		}
		System.out.format("\n\n");
	}
}
