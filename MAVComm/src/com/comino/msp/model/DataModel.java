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


package com.comino.msp.model;

import java.io.Serializable;
import java.lang.reflect.Field;

import com.comino.msp.model.segment.Attitude;
import com.comino.msp.model.segment.Battery;
import com.comino.msp.model.segment.Debug;
import com.comino.msp.model.segment.EstStatus;
import com.comino.msp.model.segment.GPS;
import com.comino.msp.model.segment.Hud;
import com.comino.msp.model.segment.Imu;
import com.comino.msp.model.segment.LogMessage;
import com.comino.msp.model.segment.Raw;
import com.comino.msp.model.segment.Rc;
import com.comino.msp.model.segment.Servo;
import com.comino.msp.model.segment.Slam;
import com.comino.msp.model.segment.State;
import com.comino.msp.model.segment.Status;
import com.comino.msp.model.segment.Telemetry;
import com.comino.msp.model.segment.Vibration;
import com.comino.msp.model.segment.Vision;
import com.comino.msp.model.segment.generic.Segment;


// Consolidated device data model after IMU


public class DataModel extends Segment implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3439530621929819600L;

	public   Attitude    attitude = null;
	public   Battery     battery  = null;
	public   Hud   			 hud  = null;
	public 	 Imu			 imu  = null;
	public   State		   state  = null;
	public   State	 target_state = null;
	public   State     home_state = null;
	public   Telemetry telemetry  = null;
	public	 GPS	         gps  = null;
	public	 GPS	        base  = null;
	public	 Raw			 raw  = null;
	public   Status          sys  = null;
	public	 Servo			servo = null;
	public	 Rc				  rc  = null;
	public   Vibration  vibration = null;
	public   Debug		    debug = null;
	public   LogMessage       msg = null;
	public   Vision        vision = null;
	public   EstStatus       est  = null;
	public   Slam           slam  = null;


	public DataModel()  {
		this.attitude       = new Attitude();
		this.battery   		= new Battery();
		this.hud  			= new Hud();
		this.imu       		= new Imu();
		this.state     		= new State();
		this.target_state 	= new State();
		this.home_state 	= new State();
		this.telemetry 		= new Telemetry();
		this.gps       		= new GPS();
		this.base           = new GPS();
		this.raw            = new Raw();
		this.sys       		= new Status();
		this.servo			= new Servo();
		this.rc             = new Rc();
		this.vibration      = new Vibration();
		this.debug          = new Debug();
		this.msg            = new LogMessage();
		this.vision         = new Vision();
		this.est            = new EstStatus();
		this.slam           = new Slam();
	}

	public DataModel(DataModel m) {
		this.copy(m);
	}

	public void copy(DataModel m) {
		this.attitude       = m.attitude.clone();
		this.battery   		= m.battery.clone();
		this.hud 			= m.hud.clone();
		this.imu       		= m.imu.clone();
		this.state     		= m.state.clone();
		this.target_state   = m.target_state.clone();
		this.home_state     = m.home_state.clone();
		this.telemetry 		= m.telemetry.clone();
		this.gps       		= m.gps.clone();
		this.base           = m.base.clone();
		this.raw            = m.raw.clone();
		this.sys       		= m.sys.clone();
		this.servo			= m.servo.clone();
		this.rc             = m.rc.clone();
		this.vibration      = m.vibration.clone();
		this.debug          = m.debug.clone();
		this.tms            = m.tms;
		this.msg            = m.msg.clone();
		this.vision         = m.vision.clone();
		this.est            = m.est.clone();
	}


	public void set(DataModel m) {
		this.attitude.set(m.attitude);
		this.battery.set(m.battery);
		this.hud.set(m.hud);
		this.imu.set(m.imu);
		this.state.set(m.state);
		this.target_state.set(m.target_state);
		this.home_state.set(m.home_state);
		this.telemetry.set(m.telemetry);
		this.gps.set(m.gps);
		this.base.set(m.base);
		this.raw.set(raw);
		this.sys.set(m.sys);
		this.servo.set(m.servo);
		this.rc.set(m.rc);
		this.vibration.set(m.vibration);
		this.debug.set(m.debug);
		this.tms = m.tms;
		this.msg.set(m.msg);
		this.vision.set(m.vision);
		this.est.set(m.est);
		this.slam.set(m.slam);
	}

	public DataModel clone() {
		return new DataModel(this);
	}

	public void clear() {
		this.attitude.clear();
		this.battery.clear();
		this.hud.clear();
		this.imu.clear();
		this.sys.clear();
		this.state.clear();
		this.target_state.clear();
		this.home_state.clear();
		this.telemetry.clear();
		this.gps.clear();
		this.base.clear();
		this.servo.clear();
		this.rc.clear();
		this.vibration.clear();
		this.debug.clear();
		this.tms = 0;
		this.msg.clear();
		this.vision.clear();
		this.est.clear();
		this.slam.clear();
	}

	public float getValue(String classkey) {
		try {
			String[] key = classkey.split("\\.");
			Field mclass_field = this.getClass().getField(key[0]);
			Object mclass = mclass_field.get(this);
			Field mfield_field = mclass.getClass().getField(key[1]);
			return new Double(mfield_field.getDouble(mclass)).floatValue();
		} catch(Exception e) {
			return Float.NaN;
		}
	}

	public static void main(String[] args) {
		DataModel m = new DataModel();
		m.battery.a0 = 5;
		try {
			System.out.println(m.getValue("battery.a0"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
