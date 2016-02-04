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

package com.comino.mav.mavlink;

public class MAV_CUST_MODE {


	public static int PX4_CUSTOM_MAIN_MODE_MANUAL 		= 1;
	public static int PX4_CUSTOM_MAIN_MODE_ALTCTL 		= 2;
	public static int PX4_CUSTOM_MAIN_MODE_POSCTL 		= 3;
	public static int PX4_CUSTOM_MAIN_MODE_AUTO 		= 4;
	public static int PX4_CUSTOM_MAIN_MODE_ACRO 		= 5;
	public static int PX4_CUSTOM_MAIN_MODE_OFFBOARD 	= 6;
	public static int PX4_CUSTOM_MAIN_MODE_STABILIZED 	= 7;

	public static int PX4_CUSTOM_SUB_MODE_AUTO_READY    = 1;
	public static int PX4_CUSTOM_SUB_MODE_AUTO_TAKEOFF 	= 2;
	public static int PX4_CUSTOM_SUB_MODE_AUTO_LOITER 	= 3;
	public static int PX4_CUSTOM_SUB_MODE_AUTO_MISSION 	= 4;
	public static int PX4_CUSTOM_SUB_MODE_AUTO_RTL 		= 5;
	public static int PX4_CUSTOM_SUB_MODE_AUTO_LAND 	= 6;
	public static int PX4_CUSTOM_SUB_MODE_AUTO_RTGS 	= 7;


//	public static boolean is(long mode, int flag) {
//		return (mode  & 1 << (flag+15)) >0;
//	}

	public static boolean is(long mode, int flag) {
	return (mode >> 16) == flag;
    }


}
