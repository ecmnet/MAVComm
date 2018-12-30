/****************************************************************************
 *
 *   Copyright (c) 2017-2019 Eike Mansfeld ecm@gmx.de. All rights reserved.
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


package com.comino.msp.model.segment;

import com.comino.msp.model.segment.generic.Segment;

public class Status extends Segment {

	// TODO: CleanUp and Refactoring: remove MODES an replace by NAV STATE


	private static final long serialVersionUID = -8811124897034255443L;


	// States

	public static final int MSP_CONNECTED						= 0;
	public static final int MSP_READY							= 1;
	public static final int MSP_ACTIVE							= 2;
	public static final int MSP_RC_ATTACHED						= 3;
	public static final int MSP_JOY_ATTACHED			        = 4;
	public static final int MSP_OFFBOARD_UPDATER_STARTED     	= 5;
	public static final int MSP_SITL                         	= 6;
	public static final int MSP_PROXY                        	= 7;
	public static final int MSP_ARMED							= 8;
	public static final int MSP_LANDED							= 9;
	public static final int MSP_INAIR							= 10;
	public static final int MSP_GPOS_VALID		            	= 11;
	public static final int MSP_LPOS_VALID         		    	= 12;


	// Low level sensors

	public static final  int MSP_IMU_AVAILABILITY     			= 0;
	public static final  int MSP_LIDAR_AVAILABILITY   			= 1;
	public static final  int MSP_SONAR_AVAILABILITY   			= 2;
	public static final  int MSP_GPS_AVAILABILITY     			= 3;
	public static final  int MSP_PIX4FLOW_AVAILABILITY    		= 4;


	// High level services

	public static final  int MSP_MSP_AVAILABILITY    		    = 5;
	public static final  int MSP_OPCV_AVAILABILITY    			= 6;
	public static final  int MSP_SYSM_AVAILABILITY    			= 7;
	public static final  int MSP_SLAM_AVAILABILITY    			= 8;
	public static final  int MSP_BASE_AVAILABILITY    			= 9;
	public static final  int MSP_RTK_AVAILABILITY    		    =10;

	// Navigation states

	public static final  int NAVIGATION_STATE_MANUAL 				= 0;		// Manual mode
	public static final  int NAVIGATION_STATE_ALTCTL 				= 1;		// Altitude control mode
	public static final  int NAVIGATION_STATE_POSCTL 				= 2;		// Position control mode
	public static final  int NAVIGATION_STATE_AUTO_MISSION 		= 3;		// Auto mission mode
	public static final  int NAVIGATION_STATE_AUTO_LOITER 		= 4;		// Auto loiter mode
	public static final  int NAVIGATION_STATE_AUTO_RTL 			= 5;		// Auto return to launch mode
	public static final  int NAVIGATION_STATE_AUTO_RCRECOVER 		= 6;		// RC recover mode
	public static final  int NAVIGATION_STATE_AUTO_RTGS 			= 7;		// Auto return to groundstation on data link loss
	public static final  int NAVIGATION_STATE_AUTO_LANDENGFAIL 	= 8;	 	// Auto land on engine failure
	public static final  int NAVIGATION_STATE_AUTO_LANDGPSFAIL 	= 9;		// Auto land on gps failure (e.g. open loop loiter down)
	public static final  int NAVIGATION_STATE_ACRO 				= 10	;	// Acro mode
	public static final  int NAVIGATION_STATE_UNUSED 				= 11	;	// Free slot
	public static final  int NAVIGATION_STATE_DESCEND 			= 12	;	// Descend mode (no position control)
	public static final  int NAVIGATION_STATE_TERMINATION 		= 13	;	// Termination mode
	public static final  int NAVIGATION_STATE_OFFBOARD 			= 14;
	public static final  int NAVIGATION_STATE_STAB 				= 15;	// Stabilized mode
	public static final  int NAVIGATION_STATE_RATTITUDE 			= 16	;	// Rattitude (aka "flip") mode
	public static final  int NAVIGATION_STATE_AUTO_TAKEOFF 		= 17;	// Takeoff
	public static final  int NAVIGATION_STATE_AUTO_LAND 			= 18	;	// Land
 	public static final  int NAVIGATION_STATE_AUTO_FOLLOW_TARGET 	= 19;	// Auto Follow
	public static final  int NAVIGATION_STATE_AUTO_PRECLAND 		= 20	; 	// Precision land with landing target


	private static final String[] sensor_names = { "IMU","LIDAR","SONAR","GPS","FLOW","MSP","CV","SYSM","SLAM","BASE","RTK",

	};

	public  int     autopilot    	= 0;
	public  int     px4_status   	= 0;
	public  int     nav_state       = 0;

	public  int     sensors      	= 0;
	public  int     status       	= 0;

	public int      error1      	= 0;
	public float    load_p       	= Float.NaN;
	public float   	drops_p       	= Float.NaN;
	public byte   	imu_temp	    = 0;
	public byte    	msp_temp    	= 0;

	public float	load_m	     	= Float.NaN;	  	//MSP
	public float    t_armed_ms    	= Float.NaN;
	public float    t_boot_ms     	= Float.NaN;
	public float    wifi_quality 	= Float.NaN;

	public long     t_offset_ns =	 0;
	public String   version    		= "";
	public String   build    		= "";


	public void set(Status s) {
		sensors  = s.sensors;
		status   = s.status;
		error1   = s.error1;
		load_p   = s.load_p;
		load_m   = s.load_m;
		drops_p  = s.drops_p;
		imu_temp = s.imu_temp;
		px4_status = s.px4_status;
		nav_state  = s.nav_state;
		autopilot  = s.autopilot;

		t_armed_ms = s.t_armed_ms;
		t_boot_ms  = s.t_boot_ms;
		wifi_quality  = s. wifi_quality;

		msp_temp = s.msp_temp;

		t_offset_ns = s.t_offset_ns;
	}


	public Status clone() {
		Status f = new Status();
		f.set(this);
		return f;
	}

	public long getSynchronizedPX4Time_us() {
		return (System.currentTimeMillis()*1000l) - (t_offset_ns/1000l);
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

	public boolean isSensorChanged(Status old, int ...box) {
		return old.isSensorAvailable(box) ^ isSensorAvailable(box);
	}

	public boolean isSensorChanged(Status old, int mask, boolean edge) {
		if(edge)
			return ((old.sensors & mask) != (sensors & mask) && ((sensors & mask) == mask));
		return ((old.sensors & mask) != (sensors & mask) && ((sensors & mask) == 0));
	}

	public void  setStatus(int box, boolean val) {
		if(val)
			status = (int) (status | (1<<box));
		else
			status = (int) (status & ~(1<<box));
	}

	public void setStatus(int box, long val) {
		setStatus(box, (val & (1<<box))!=0);
	}

	public boolean isStatus(int ...box) {
		for(int b : box)
			if((status & (1<<b))==0)
				return false;
		return true;
	}

	public void  setAutopilotMode(int box, boolean val) {
		if(val)
			autopilot = (int) (autopilot | (1<<box));
		else
			autopilot = (int) (autopilot & ~(1<<box));
	}

	public boolean isAutopilotMode(int ...box) {
		for(int b : box)
			if((autopilot & (1<<b))==0)
				return false;
		return true;
	}

	public boolean isStatusChanged(Status old, int mask) {
		return ((old.status & mask) != (status & mask));
	}

	public boolean isStatusChanged(Status old, int mask, boolean edge) {
		if(edge)
			return ((old.status & mask) != (status & mask) && ((status & mask) == mask));
		return ((old.status & mask) != (status & mask) && ((status & mask) == 0));
	}

	public boolean isAutopilotModeChanged(Status old,int mask) {
		return ((old.autopilot & mask) != (autopilot & mask));
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
		imu_temp	      = 0;
		drops_p   	  = 0;
		error1        = 0;
		t_armed_ms    = 0;
		t_boot_ms     = 0;
		px4_status    = 0;
		nav_state     = 0;
		status        = 0;
		autopilot     = 0;
		wifi_quality  = 0;
		msp_temp   	  = 0;
	}


	public int getStatus() {
		return status;
	}


	public boolean isEqual(Status m) {
		return (status == m.status)	&& (autopilot == m.autopilot) && (nav_state == m.nav_state) && (sensors == m.sensors);
	}


	public String toString() {
		return "Status="+Integer.toBinaryString(status)+" Autopilot="+Integer.toBinaryString(autopilot);
	}


}
