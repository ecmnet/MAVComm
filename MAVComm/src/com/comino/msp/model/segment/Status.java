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


package com.comino.msp.model.segment;

import com.comino.msp.model.segment.generic.Segment;

public class Status extends Segment {


	private static final long serialVersionUID = -8811124897034255443L;


	// Status

	public static final int MSP_CONNECTED					= 0;
	public static final int MSP_READY						= 1;
	public static final int MSP_ACTIVE						= 2;
	public static final int MSP_RC_ATTACHED					= 3;
	public static final int MSP_JOY_ATTACHED			    = 4;
	public static final int MSP_OFFBOARD_UPDATER_STARTED    = 5;

	public static final int MSP_ARMED						= 8;
	public static final int MSP_MODE_MANUAL			        = 9;
	public static final int MSP_MODE_STABILIZED			    = 10;
	public static final int MSP_MODE_ALTITUDE			    = 11;
	public static final int MSP_MODE_POSITION			    = 12;
	public static final int MSP_MODE_LOITER					= 13;
	public static final int MSP_MODE_MISSION				= 14;
	public static final int MSP_MODE_OFFBOARD				= 15;
	public static final int MSP_MODE_LANDING				= 16;
	public static final int MSP_MODE_TAKEOFF				= 17;

	public static final int MSP_MODE_RTL					= 19;

	public static final int MSP_GPOS_AVAILABILITY           = 20;
	public static final int MSP_LPOS_AVAILABILITY           = 21;

	public static final int MSP_LANDED						= 24;
	public static final int MSP_INAIR						= 25;
	public static final int MSP_FREEFALL					= 26;


	// Low level sensors

	public static final  int MSP_IMU_AVAILABILITY     		= 0;
	public static final  int MSP_LIDAR_AVAILABILITY   		= 1;
	public static final  int MSP_SONAR_AVAILABILITY   		= 2;
	public static final  int MSP_GPS_AVAILABILITY     		= 3;
	public static final  int MSP_PIX4FLOW_AVAILABILITY    	= 4;


	// High level services

	public static final  int MSP_MSP_AVAILABILITY    		= 5;
	public static final  int MSP_OPCV_AVAILABILITY    		= 6;
	public static final  int MSP_SYSM_AVAILABILITY    		= 7;
	public static final  int MSP_SLAM_AVAILABILITY    		= 8;
	public static final  int MSP_BASE_AVAILABILITY    		= 9;



	private static final String[] sensor_names = {

		"IMU","LIDAR","SONAR","GPS","FLOW","MSP","CV","SYSM","SLAM","BASE",

	};


	private int     sensors    = 0;
	private int     status     = 0;

	public int      error1     = 0;
	public float    load_p     = Float.NaN;
	public float   drops_p     = Float.NaN;
	public float   imu_temp	   = Float.NaN;

	public int		basemode   = 0;
	public int		custommode = 0;

	public int     px4_status  = 0;


	public float	load_m	   = Float.NaN;	  	//MSP
	public float    t_armed_ms = Float.NaN;
	public float    t_boot_ms  = Float.NaN;

	public long     t_offset_ms = 0;
	public String   version    = "";


	public void set(Status s) {
		sensors  = s.sensors;
		status   = s.status;
		error1   = s.error1;
		load_p   = s.load_p;
		load_m   = s.load_m;
		drops_p  = s.drops_p;
		imu_temp = s.imu_temp;
		basemode = s.basemode;
		custommode = s.custommode;
		px4_status = s.px4_status;

		t_armed_ms = s.t_armed_ms;
		t_boot_ms  = s.t_boot_ms;

		t_offset_ms = s.t_offset_ms;
	}


	public Status clone() {
		Status f = new Status();
		f.set(this);
		return f;
	}

	public long getCurrentSyncTimeMS() {
		return System.currentTimeMillis() + t_offset_ms;
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

	public boolean isStatusChanged(Status old, int ...box) {
		return !old.isStatus(box) && isStatus(box);
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
		t_armed_ms    = 0;
		t_boot_ms     = 0;
		px4_status    = 0;

	}


	public int getStatus() {
		return status;
	}


	public boolean isEqual(Status m) {
		return (status == m.status) && ( basemode == m.basemode );
	}


	public String toString() {
		return "Connected="+isStatus(Status.MSP_CONNECTED)+" Sensors="+getSensorString();
	}


}
