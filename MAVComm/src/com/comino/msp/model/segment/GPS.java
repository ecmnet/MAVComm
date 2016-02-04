/*
 * Copyright (c) 2016 by E.Mansfeld
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.comino.msp.model.segment;

import com.comino.msp.model.segment.generic.Segment;

// ok


public class GPS extends Segment {

	private static final long serialVersionUID = 3343922430238721067L;


	public static final int GPS_SAT_FIX   = 0;
	public static final int GPS_SAT_VALID = 1;
	public static final int GPS_REF_VALID = 2;

	public int		flags		= 0;

	public byte   	numsat 		= 0;
	public double 	latitude 	= 0;
	public double 	longitude 	= 0;
	public short  	heading 	= 0;
	public short  	altitude 	= 0;
	public float  	error2d 	= 0;
	public float  	speed 		= 0;

	public double	ref_lat     = 0;
	public double   ref_lon     = 0;
	public float    ref_altitude= 0;

	public float	rel_x		= 0;
	public float    rel_y 		= 0;
	public float    rel_z		= 0;
	public float    rel_vx		= 0;
	public float    rel_vy		= 0;
	public float    rel_vz		= 0;


	public GPS() {

	}

	public GPS(double _lat, double _lon, int _heading ) {
		latitude 	= _lat;
		longitude 	= _lon;
		heading 	= (short)_heading;
		setFlag(GPS_SAT_VALID, true);
		numsat 		= 99;
	}

	public void set(int _fix, int _numsat, double _lat, double _lon, int _altitude, int _heading, float _error2d, float _speed) {

	    setFlag(GPS_SAT_FIX, _fix > 0);
	    setFlag(GPS_SAT_VALID, numsat > 6);

		numsat 		= (byte)_numsat;
		latitude 	= _lat;
		longitude 	= _lon;
		heading 	= (short)_heading;
		altitude 	= (short)_altitude;
		error2d 	= _error2d;
		speed 		= _speed;
	}

	public void set(GPS gps) {
		flags 		= gps.flags;
		numsat		= gps.numsat;
		latitude	= gps.latitude;
		longitude	= gps.longitude;
		altitude	= gps.altitude;
		heading		= gps.heading;
		error2d		= gps.error2d;
		speed		= gps.speed;

		ref_lat     = gps.ref_lat;
		ref_lon     = gps.ref_lon;
		ref_altitude= gps.ref_altitude;

		rel_x		= gps.rel_x;
		rel_y 		= gps.rel_y;
		rel_z		= gps.rel_z;
		rel_vx		= gps.rel_vx;
		rel_vy		= gps.rel_vy;
		rel_vz		= gps.rel_vz;
	}

	public void setReference(double _lat, double _lon, float _altitude) {
		ref_lon 		= _lon;
		ref_lat			= _lat;
		ref_altitude	= _altitude;
		setFlag(GPS_REF_VALID,true);
	}


	public GPS clone() {
		GPS g = new GPS();

		g.flags       	= flags;
		g.numsat    	= numsat;
		g.heading   	= heading;
		g.latitude  	= latitude;
		g.longitude 	= longitude;
		g.error2d 		= error2d;
		g.altitude		= altitude;
		g.speed     	= speed;

		g.ref_lat     	= ref_lat;
		g.ref_lon     	= ref_lon;
		g.ref_altitude	= ref_altitude;

		g.rel_x			= rel_x;
		g.rel_y 		= rel_y;
		g.rel_z			= rel_z;
		g.rel_vx		= rel_vx;
		g.rel_vy		= rel_vy;
		g.rel_vz		= rel_vz;

		return g;
	}


	public void clear() {

		flags 		= 0;
		numsat 		= 0;
		latitude 	= 0;
		longitude 	= 0;
		heading 	= 0;
		altitude 	= 0;
		error2d 	= 0;
		speed 		= 0;

		ref_lat     = 0;
		ref_lon     = 0;
		ref_altitude= 0;

		rel_x		= 0;
		rel_y 		= 0;
		rel_z		= 0;
		rel_vx		= 0;
		rel_vy		= 0;
		rel_vz		= 0;
	}

	public void  setFlag(int box, boolean val) {
		if(val)
			flags = (short) (flags | (1<<box));
		else
			flags = (short) (flags & ~(1<<box));
	}

	public boolean isFlagSet(int ...box) {
		for(int b : box)
			if((flags & (1<<b))==0)
				return false;
		return true;
	}


}

///*
//Copyright 2013 Brad Quick
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.
//*/
//
//#include "navigation.h"
//#include "gps.h"
//#include "lib_fp.h"
//#include "bradwii.h"
//
//#if (GPS_TYPE!=NO_GPS)
//
//// convert NAVIGATION_MAX_TILT user setting to a fixedpointnum constant
//#define MAX_TILT FIXEDPOINTCONSTANT(NAVIGATION_MAX_TILT)
//#define MAXYAWANGLEERROR FIXEDPOINTCONSTANT(5.0)
//
//extern globalstruct global;
//extern usersettingsstruct usersettings;
//
//fixedpointnum navigation_getdistanceandbearing(fixedpointnum lat1,fixedpointnum lon1,fixedpointnum lat2,fixedpointnum lon2,fixedpointnum *bearing)
//   { // returns fixedpointnum distance in meters and bearing in fixedpointnum degrees from point 1 to point 2
//   fixedpointnum latdiff=lat2-lat1;
//   fixedpointnum londiff=lib_fp_multiply(lon2-lon1,lib_fp_cosine(lat1>>LATLONGEXTRASHIFT));
//
//   *bearing = FIXEDPOINT90 + lib_fp_atan2(-latdiff, londiff);
//   if (*bearing >FIXEDPOINT180) *bearing -= FIXEDPOINT360;
//
//   // distance is 111319 meters per degree. This factor needs to be shifted 16 to make it a fixedpointnum.
//   // Since lat and lon are already shifted by 6,
//   // we will shift by 10 more total.  Shift lat and long by 8 and the constant by 2 to get the extra 10.
//   // The squaring will overflow our fixedpointnum at distances greater than 1000 meters or so, so test for size and shift accordingly
//   if (lib_fp_abs(latdiff)+lib_fp_abs(londiff)>40000L)
//      { // for big distances, don't shift lat and long.  Instead shift the constant by 10.
//      // this will get us to 32 kilometers at which point our fixedpoingnum can't hold a larger distance.
//      return(lib_fp_multiply(lib_fp_sqrt(lib_fp_multiply(latdiff,latdiff)+lib_fp_multiply(londiff,londiff)),113990656L));
//      }
//   else
//      {
//      latdiff=latdiff<<8;
//      londiff=londiff<<8;
//      return(lib_fp_multiply(lib_fp_sqrt(lib_fp_multiply(latdiff,latdiff)+lib_fp_multiply(londiff,londiff)),445276L));
//      }
//   }
//
//fixedpointnum target_latitude;
//fixedpointnum target_longitude;
//fixedpointnum navigation_starttodestbearing;
//fixedpointnum navigation_last_crosstrack_distance;
//fixedpointnum navigation_last_ontrack_distance;
//fixedpointnum navigation_crosstrack_integrated_error;
//fixedpointnum navigation_ontrack_integrated_error;
//fixedpointnum navigation_crosstrack_velocity;
//fixedpointnum navigation_ontrack_velocity;
//fixedpointnum navigation_time_sliver; // accumulated time between gps readings
//fixedpointnum navigation_desiredeulerattitude[3];
//
////                ontrack
////               distance
//// destination *==========-----------------------------* start
////               \ A     |
////                \      |
////                 \     |
////                  \    | crosstrack
////                   \   | distance
////                    \  |
////                     \ |
////                      \|
////                       * current location
////
////  angle A is the difference between our start to destination bearing and our current bearing to the destination
////  crosstrack distance=current distance to destination * sine(A)
////  ontrack distance = current distance to destination *cosine(A)
//void navigation_sethometocurrentlocation()
//   {
//   global.gps_home_latitude=global.gps_current_latitude;
//   global.gps_home_longitude=global.gps_current_longitude;
//   }
//
//void navigation_set_destination(fixedpointnum latitude,fixedpointnum longitude)
//   { // sets a new destination to navigate towards.  Assumes we are navigating from our current location.
//   target_latitude=latitude;
//   target_longitude=longitude;
//   navigation_last_crosstrack_distance=0;
//   navigation_crosstrack_integrated_error=0;
//   navigation_ontrack_integrated_error=0;
//   navigation_crosstrack_velocity=0;
//   navigation_ontrack_velocity=0;
//
//   // remember the bearing from the current location to the waypoint.  We will need this to calculate cross track.
//   // If we are already at the waypoint (position hold) any arbitrary angle will work.
//   navigation_last_ontrack_distance=navigation_getdistanceandbearing(global.gps_current_latitude,global.gps_current_longitude,latitude,longitude,&navigation_starttodestbearing);
//   navigation_time_sliver=0;
//
//   navigation_desiredeulerattitude[ROLLINDEX]=0;
//   navigation_desiredeulerattitude[PITCHINDEX]=0;
//
//   // for now, we will just stay rotated to the yaw angle we started at
//   navigation_desiredeulerattitude[YAWINDEX]=global.currentestimatedeulerattitude[YAWINDEX];
//   }
//
//// limit for windup
//#define NAVIGATIONINTEGRATEDERRORLIMIT FIXEDPOINTCONSTANT(1000)
//
//void navigation_setangleerror(unsigned char gotnewgpsreading,fixedpointnum *angleerror)
//   { // calculate the angle errors between our current attitude and the one we wish to have
//   // and adjust the angle errors that were passed to us.  They have already been set by pilot input.
//   // For now, we just override any pilot input.
//
//   // keep track of the time between good gps readings.
//   navigation_time_sliver+=global.timesliver;
//
//   if (gotnewgpsreading)
//      {
//      // unshift our timesliver since we are about to use it. Since we are accumulating time, it may get too large to use while shifted.
//      navigation_time_sliver=navigation_time_sliver>>TIMESLIVEREXTRASHIFT;
//
//      // get the new distance and bearing from our current location to our target position
//      global.navigation_distance=navigation_getdistanceandbearing(global.gps_current_latitude,global.gps_current_longitude,target_latitude,target_longitude,&global.navigation_bearing);
//
//       if((global.navigation_distance>>FIXEDPOINTSHIFT)<2 && global.navigationmode==NAVIGATIONMODERETURNTOHOME) {
//    	   global.activecheckboxitems &= ~CHECKBOXMASKRETURNTOHOME;
//    	   return;
//       }
//
//      // split the distance into it's ontrack and crosstrack components
//      // see the diagram above
//      fixedpointnum angledifference=global.navigation_bearing-navigation_starttodestbearing;
//      fixedpointnum crosstrack_distance=lib_fp_multiply(global.navigation_distance,lib_fp_sine(angledifference));
//      fixedpointnum ontrack_distance=lib_fp_multiply(global.navigation_distance,lib_fp_cosine(angledifference));
//
//      // accumulate integrated error for both ontrack and crosstrack
//      navigation_crosstrack_integrated_error+=lib_fp_multiply(crosstrack_distance,navigation_time_sliver);
//      navigation_ontrack_integrated_error+=lib_fp_multiply(ontrack_distance,navigation_time_sliver);
//      lib_fp_constrain(&navigation_crosstrack_integrated_error,-NAVIGATIONINTEGRATEDERRORLIMIT,NAVIGATIONINTEGRATEDERRORLIMIT);
//      lib_fp_constrain(&navigation_ontrack_integrated_error,-NAVIGATIONINTEGRATEDERRORLIMIT,NAVIGATIONINTEGRATEDERRORLIMIT);
//
//      // calculate the ontrack and crosstrack velocities toward our target.
//      // We want to put the navigation velocity (change in distance to target over time) into a low pass filter but
//      // we don't want to divide by the time interval to get velocity (divide is expensive) to then turn around and
//      // multiply by the same time interval. So the following is the same as the lib_fp_lowpassfilter code
//      // except we eliminate the multiply.
//      // note: if we use a different time period than FIXEDPOINTONEOVERONE, we need to multiply the distances by the new time period.
//      fixedpointnum fraction=lib_fp_multiply(navigation_time_sliver,FIXEDPOINTONEOVERONE);
//      navigation_crosstrack_velocity=(navigation_last_crosstrack_distance-crosstrack_distance+lib_fp_multiply((FIXEDPOINTONE)-fraction,navigation_crosstrack_velocity));
//      navigation_ontrack_velocity=(navigation_last_ontrack_distance-ontrack_distance+lib_fp_multiply((FIXEDPOINTONE)-fraction,navigation_ontrack_velocity));
//      navigation_last_crosstrack_distance=crosstrack_distance;
//      navigation_last_ontrack_distance=ontrack_distance;
//
//      // calculate the desired tilt in each direction independently using navigation PID
//      fixedpointnum crosstracktiltangle=lib_fp_multiply(usersettings.pid_pgain[NAVIGATIONINDEX],crosstrack_distance)
//                                    +lib_fp_multiply(usersettings.pid_igain[NAVIGATIONINDEX],navigation_crosstrack_integrated_error)
//                                    -lib_fp_multiply(usersettings.pid_dgain[NAVIGATIONINDEX],navigation_crosstrack_velocity);
//
//      fixedpointnum ontracktiltangle   =lib_fp_multiply(usersettings.pid_pgain[NAVIGATIONINDEX],ontrack_distance)
//                                    +lib_fp_multiply(usersettings.pid_igain[NAVIGATIONINDEX],navigation_ontrack_integrated_error)
//                                    -lib_fp_multiply(usersettings.pid_dgain[NAVIGATIONINDEX],navigation_ontrack_velocity);
//
//      // don't tilt more than MAX_TILT
//      lib_fp_constrain(&crosstracktiltangle,-MAX_TILT,MAX_TILT);
//      lib_fp_constrain(&ontracktiltangle,-MAX_TILT,MAX_TILT);
//
//      // Translate the ontrack and cross track tilts into pitch and roll tilts.
//      // Set angledifference equal to the difference between the aircraft's heading (the way it's currently pointing)
//      // and the angle between waypoints and rotate our tilts by that much.
//      angledifference=global.currentestimatedeulerattitude[YAWINDEX]-navigation_starttodestbearing;
//
//      fixedpointnum sineofangle=lib_fp_sine(angledifference);
//      fixedpointnum cosineofangle=lib_fp_cosine(angledifference);
//
//      navigation_desiredeulerattitude[ROLLINDEX]=lib_fp_multiply(crosstracktiltangle,cosineofangle)-lib_fp_multiply(ontracktiltangle,sineofangle);
//      navigation_desiredeulerattitude[PITCHINDEX]=lib_fp_multiply(crosstracktiltangle,sineofangle)+lib_fp_multiply(ontracktiltangle,cosineofangle);
//
//      // for now, don't rotate the aircraft in the direction of travel. Add this later.
//
//      navigation_time_sliver=0;
//      }
//
//   // set the angle error as the difference between where we want to be and where we currently are angle wise.
//   angleerror[ROLLINDEX]=navigation_desiredeulerattitude[ROLLINDEX]-global.currentestimatedeulerattitude[ROLLINDEX];
//   angleerror[PITCHINDEX]=navigation_desiredeulerattitude[PITCHINDEX]-global.currentestimatedeulerattitude[PITCHINDEX];
//
//   // don't set the yaw.  Let the pilot do yaw
////   angleerror[YAWINDEX]=navigation_desiredeulerattitude[YAWINDEX]-global.currentestimatedeulerattitude[YAWINDEX];
//
//   // don't let the yaw angle error get too large for any one cycle in order to control the maximum yaw rate.
////   lib_fp_constrain180(&angleerror[YAWINDEX]);
////   lib_fp_constrain(&angleerror[YAWINDEX],-MAXYAWANGLEERROR,MAXYAWANGLEERROR);
//   }
//
//#endif

