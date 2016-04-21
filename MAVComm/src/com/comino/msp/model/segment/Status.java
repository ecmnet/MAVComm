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

public class Status extends Segment {


	private static final long serialVersionUID = -8811124897034255443L;


	// Status

	public static final int MSP_CONNECTED					= 0;
	public static final int MSP_READY						= 1;
	public static final int MSP_ACTIVE						= 2;
	public static final int MSP_RC_ATTACHED					= 3;

	public static final int MSP_ARMED						= 8;
	public static final int MSP_MODE_MANUAL			        = 9;
	public static final int MSP_MODE_STABILIZED			    = 10;
	public static final int MSP_MODE_ALTITUDE			    = 11;
	public static final int MSP_MODE_POSITION			    = 12;
	public static final int MSP_MODE_LOITER					= 13;
	public static final int MSP_MODE_MISSION				= 14;
	public static final int MSP_MODE_OFFBOARD				= 15;

	public static final int MSP_LANDED						= 24;



	// Low level sensors

	public static final  int MSP_IMU_AVAILABILITY     		= 0;
	public static final  int MSP_LIDAR_AVAILABILITY   		= 1;
	public static final  int MSP_SONAR_AVAILABILITY   		= 2;
	public static final  int MSP_GPS_AVAILABILITY     		= 3;
	public static final  int MSP_PIX4FLOW_AVAILABILITY    	= 4;

	// High level services

	public static final  int MSP_OPCV_AVAILABILITY    		= 5;
	public static final  int MSP_SYSM_AVAILABILITY    		= 6;
	public static final  int MSP_SLAM_AVAILABILITY    		= 7;
	public static final  int MSP_BASE_AVAILABILITY    		= 8;



	private static final String[] sensor_names = {

		"IMU","LIDAR","SONAR","GPS","FLOW","OPCV","SYSM","SLAM","BASE",

	};


	private int     sensors   = 0;
	private int     status    = 0;

	public int      error1    = 0;
	public float    load_p    = 0;
	public float   drops_p    = 0;
	public int    imu_temp	  = 0;

	public int		basemode  = 0;


	public float	load_m	  = 0;


	public void set(Status s) {
		sensors  = s.sensors;
		status   = s.status;
		error1   = s.error1;
		load_p   = s.load_p;
		load_m   = s.load_m;
		drops_p  = s.drops_p;
		imu_temp = s.imu_temp;
		basemode = s.basemode;
	}


	public Status clone() {
		Status f = new Status();
		f.set(this);
		return f;
	}

	public void  setSensor(int box, boolean val) {
		if(val)
			sensors = (int) (sensors | (1<<box));
		else
			sensors = (int) (sensors & ~(1<<box));
	}

	public boolean isSensorAvailable(int ...box) {
		for(int b : box)
		  if((sensors & (1<<b))==0)
            return false;
		return true;
	}

	public void  setStatus(int box, boolean val) {
		if(val)
			status = (int) (status | (1<<box));
		else
			status = (int) (status & ~(1<<box));
	}

	public boolean isStatus(int ...box) {
		for(int b : box)
		  if((status & (1<<b))==0)
            return false;
		return true;
	}


	public String getSensorString() {
		String text=" ";
		for(int i=0; i < sensor_names.length;i++) {
			if(((sensors >> i) & 1) == 1)
				text = text + sensor_names[i] +" ";
		}
		return text;
	}

	public void clear() {
		load_m	  	  = 0;
		load_p	  	  = 0;
		imu_temp	  = 0;
		drops_p   	  = 0;
		error1        = 0;

	}


	public boolean isEqual(Status m) {
		return (status == m.status) && ( basemode == m.basemode );
	}


	public String toString() {
		return "Connected="+isStatus(Status.MSP_CONNECTED)+" Sensors="+getSensorString();
	}


}
