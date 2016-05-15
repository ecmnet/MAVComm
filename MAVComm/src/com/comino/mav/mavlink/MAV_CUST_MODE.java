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
