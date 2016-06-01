/****************************************************************************
 *
 *   Copyright (c) 2016 Eike Mansfeld ecm@gmx.de. All rights reserved.
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


package com.comino.mav.mavlink;

public class MAV_CUST_MODE {

	public static int PX4_CUSTOM_MAIN_MODE_MANUAL 		= 1;
	public static int PX4_CUSTOM_MAIN_MODE_ALTCTL 		= 2;
	public static int PX4_CUSTOM_MAIN_MODE_POSCTL 		= 3;
	public static int PX4_CUSTOM_MAIN_MODE_AUTO 		= 4;
	public static int PX4_CUSTOM_MAIN_MODE_ACRO 		= 5;
	public static int PX4_CUSTOM_MAIN_MODE_OFFBOARD 	= 6;
	public static int PX4_CUSTOM_MAIN_MODE_STABILIZED 	= 7;
	public static int PX4_CUSTOM_MAIN_MODE_RATTITUDE    = 8;

	public static int PX4_CUSTOM_SUB_MODE_AUTO_READY    = 1;
	public static int PX4_CUSTOM_SUB_MODE_AUTO_TAKEOFF 	= 2;
	public static int PX4_CUSTOM_SUB_MODE_AUTO_LOITER 	= 3;
	public static int PX4_CUSTOM_SUB_MODE_AUTO_MISSION 	= 4;
	public static int PX4_CUSTOM_SUB_MODE_AUTO_RTL 		= 5;
	public static int PX4_CUSTOM_SUB_MODE_AUTO_LAND 	= 6;
	public static int PX4_CUSTOM_SUB_MODE_AUTO_RTGS 	= 7;
	public static int PX4_CUSTOM_SUB_MODE_AUTO_FOLLOW_ME= 8;

	private static String[] mode_string = {
			"Manual","Acro","Stabilized","Rattitude","Altitude Control","Position Control",
			"Offboard Control","Ready","Takeoff","Hold","Mission","Return To Land","Landing",
			"Return, Link Loss","Follow Me","Unknown"
	};



	public static boolean is(long custom, int mode) {
		custom = custom & 0xFFFFFFFF;
			return ((custom >> 16) & 0xFF) == mode;
	}

	public static boolean is(long custom, int mode_auto, int mode) {
		custom = custom & 0xFFFFFFFF;
		if(((custom >> 16) & 0xFF) == mode_auto) {
			return ((custom >> 24) & 0xFF) == mode;

		} else
			return false;
	}

	public static String getName(long custom) {
		if(is(custom,PX4_CUSTOM_MAIN_MODE_MANUAL))
			return mode_string[0];
		if(is(custom,PX4_CUSTOM_MAIN_MODE_ACRO))
			return mode_string[1];
		if(is(custom,PX4_CUSTOM_MAIN_MODE_STABILIZED))
			return mode_string[2];
		if(is(custom,PX4_CUSTOM_MAIN_MODE_RATTITUDE))
			return mode_string[3];
		if(is(custom,PX4_CUSTOM_MAIN_MODE_ALTCTL))
			return mode_string[4];
		if(is(custom,PX4_CUSTOM_MAIN_MODE_POSCTL))
			return mode_string[5];
		if(is(custom,PX4_CUSTOM_MAIN_MODE_OFFBOARD))
			return mode_string[6];
		if(is(custom,PX4_CUSTOM_MAIN_MODE_AUTO,PX4_CUSTOM_SUB_MODE_AUTO_READY))
			return mode_string[7];
		if(is(custom,PX4_CUSTOM_MAIN_MODE_AUTO,PX4_CUSTOM_SUB_MODE_AUTO_TAKEOFF))
			return mode_string[8];
		if(is(custom,PX4_CUSTOM_MAIN_MODE_AUTO,PX4_CUSTOM_SUB_MODE_AUTO_LOITER))
			return mode_string[9];
		if(is(custom,PX4_CUSTOM_MAIN_MODE_AUTO,PX4_CUSTOM_SUB_MODE_AUTO_MISSION))
			return mode_string[10];
		if(is(custom,PX4_CUSTOM_MAIN_MODE_AUTO,PX4_CUSTOM_SUB_MODE_AUTO_RTL))
			return mode_string[11];
		if(is(custom,PX4_CUSTOM_MAIN_MODE_AUTO,PX4_CUSTOM_SUB_MODE_AUTO_LAND))
			return mode_string[12];
		if(is(custom,PX4_CUSTOM_MAIN_MODE_AUTO,PX4_CUSTOM_SUB_MODE_AUTO_RTGS))
			return mode_string[13];
		if(is(custom,PX4_CUSTOM_MAIN_MODE_AUTO,PX4_CUSTOM_SUB_MODE_AUTO_FOLLOW_ME))
			return mode_string[14];
		return mode_string[15];
	}


}
