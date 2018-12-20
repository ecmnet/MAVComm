/****************************************************************************
 *
 *   Copyright (c) 2017,2018 Eike Mansfeld ecm@gmx.de. All rights reserved.
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

package com.comino.msp.execution.autopilot;

import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.mavlink.messages.MAV_CMD;
import org.mavlink.messages.MAV_MODE_FLAG;
import org.mavlink.messages.MAV_SEVERITY;
import org.mavlink.messages.MSP_AUTOCONTROL_ACTION;
import org.mavlink.messages.MSP_AUTOCONTROL_MODE;
import org.mavlink.messages.lquac.msg_msp_micro_slam;

import com.comino.main.MSPConfig;
import com.comino.mav.control.IMAVController;
import com.comino.mav.mavlink.MAV_CUST_MODE;
import com.comino.msp.execution.autopilot.offboard.IOffboardExternalControl;
import com.comino.msp.execution.autopilot.offboard.IOffboardTargetAction;
import com.comino.msp.execution.autopilot.offboard.OffboardManager;
import com.comino.msp.execution.autopilot.tracker.WayPointTracker;
import com.comino.msp.execution.control.StatusManager;
import com.comino.msp.log.MSPLogger;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.LogMessage;
import com.comino.msp.model.segment.Status;
import com.comino.msp.slam.map2D.ILocalMap;
import com.comino.msp.slam.map2D.filter.ILocalMapFilter;
import com.comino.msp.slam.map2D.filter.impl.DenoiseMapFilter;
import com.comino.msp.slam.map2D.impl.LocalMap2DArray;
import com.comino.msp.slam.map2D.impl.LocalMap2DRaycast;
import com.comino.msp.slam.map2D.store.LocaMap2DStorage;
import com.comino.msp.slam.vfh.LocalVFH2D;
import com.comino.msp.utils.ExecutorService;
import com.comino.msp.utils.MSP3DUtils;

import georegression.struct.point.Vector3D_F32;
import georegression.struct.point.Vector4D_F32;

public class Autopilot2D implements Runnable {

	private static final int   CYCLE_MS						= 100;

	private static final int   CERTAINITY_THRESHOLD      	= 3;
	private static final float ROBOT_RADIUS         	 	= 0.25f;
	private static final float WINDOWSIZE       			= 2.0f;


	private static final float OBSTACLE_MINDISTANCE_0MS  	= 0.5f;
	private static final float OBSTACLE_MINDISTANCE_1MS  	= 1.5f;

	private static final float OBSTACLE_FAILDISTANCE     	= OBSTACLE_MINDISTANCE_1MS;
	private static final float OBSTACLE_FAILDISTANCE_2     	= OBSTACLE_MINDISTANCE_1MS / 2f;

	private static Autopilot2D      autopilot = null;

	private OffboardManager         offboard = null;
	private DataModel               model    = null;
	private MSPLogger               logger   = null;
	private IMAVController          control  = null;
	private WayPointTracker         tracker  = null;

	private ILocalMap				map     = null;
	private LocalVFH2D              lvfh     = null;

	private IAutoPilotGetTarget targetListener = null;

	private boolean            	isAvoiding  		= false;
	private boolean				mapForget   		= false;
	private boolean             flowCheck           = false;
	private float             	nearestTarget 	    = 0;

	private ILocalMapFilter filter = null;


	public static Autopilot2D getInstance(IMAVController control,MSPConfig config) {
		if(autopilot == null)
			autopilot = new Autopilot2D(control,config);
		return autopilot;
	}

	public static Autopilot2D getInstance() {
		return autopilot;
	}

	private Autopilot2D(IMAVController control, MSPConfig config) {

		System.out.println("Autopilot2D instantiated");

		filter = new DenoiseMapFilter(800,800);


		this.offboard = new OffboardManager(control);
		this.tracker  = new WayPointTracker(control);
		this.control  = control;
		this.model    = control.getCurrentModel();
		this.logger   = MSPLogger.getInstance();

		this.mapForget = config.getBoolProperty("autopilot_forget_map", "false");
		System.out.println("Autopilot2D: Map forget enabled: "+mapForget);
		this.flowCheck = config.getBoolProperty("autopilot_flow_check", "true") & !model.sys.isStatus(Status.MSP_SITL);
		System.out.println("Autopilot2D: FlowCheck enabled: "+flowCheck);

		if(control.isSimulation())
			this.map      = new LocalMap2DArray(model,WINDOWSIZE,CERTAINITY_THRESHOLD);
		else
			this.map      = new LocalMap2DRaycast(model,WINDOWSIZE,CERTAINITY_THRESHOLD);

		this.lvfh     = new LocalVFH2D(map,ROBOT_RADIUS, CERTAINITY_THRESHOLD);

		// Auto-Takeoff: Switch to Offboard and enable ObstacleAvoidance as soon as takeoff completed
		//
		// TODO: Landing during takeoff switches to offboard mode here => should directly land instead
		//       Update: 		works in SITL, but not on vehicle (timing?)
		control.getStatusManager().addListener(StatusManager.TYPE_PX4_NAVSTATE, Status.NAVIGATION_STATE_AUTO_TAKEOFF, StatusManager.EDGE_FALLING, (o,n) -> {
			if(n.nav_state == Status.NAVIGATION_STATE_AUTO_LAND)
				return;
			offboard.setCurrentSetPointAsTarget();
			offboard.start(OffboardManager.MODE_LOITER);
			if(!model.sys.isStatus(Status.MSP_RC_ATTACHED)) {
				model.sys.setAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_AVOIDANCE, true);
				control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
						MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
						MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_OFFBOARD, 0 );
				control.writeLogMessage(new LogMessage("[msp] Auto-takeoff completed.", MAV_SEVERITY.MAV_SEVERITY_NOTICE));
			}

		});


		//		control.getStatusManager().addListener(StatusManager.TYPE_PX4_NAVSTATE, Status.NAVIGATION_STATE_AUTO_RTL, StatusManager.EDGE_RISING, (o,n) -> {
		//			offboard.stop();
		//		});

		// Stop offboard updater as soon as landed
		control.getStatusManager().addListener(StatusManager.TYPE_PX4_STATUS, Status.MSP_LANDED, StatusManager.EDGE_RISING, (o,n) -> {
			offboard.stop();
		});

		Thread worker = new Thread(this);
		worker.setPriority(Thread.MIN_PRIORITY);
		worker.setName("AutoPilot");
		worker.start();

	}

	@Override
	public void run() {

		Vector3D_F32 current = new Vector3D_F32(); boolean tooClose = false; float min_distance;
		msg_msp_micro_slam slam = new msg_msp_micro_slam(2,1);

		while(true) {
			try { Thread.sleep(CYCLE_MS); } catch(Exception s) { }

			//System.out.println(model.sys.getSensorString());

			current.set(model.state.l_x, model.state.l_y,model.state.l_z);
			lvfh.update_histogram(current, model.hud.s);

			nearestTarget = map.nearestDistance(model.state.l_x, model.state.l_y);
			if(nearestTarget < OBSTACLE_FAILDISTANCE_2  && !tooClose ) {
				logger.writeLocalMsg("[msp] Collision warning.",MAV_SEVERITY.MAV_SEVERITY_CRITICAL);
				tooClose = true;
			}

			if(nearestTarget > OBSTACLE_FAILDISTANCE+ROBOT_RADIUS)
				tooClose = false;

			min_distance = getAvoidanceDistance(model.hud.s);

			if(nearestTarget < min_distance && !isAvoiding) {
				isAvoiding = true;
				if(model.sys.isAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_AVOIDANCE))
					obstacleAvoidance();
				if(model.sys.isAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_STOP))
					stop_at_position();
			}

			if(nearestTarget > (min_distance + ROBOT_RADIUS) && isAvoiding) {
				offboard.removeExternalControlListener();
				if(model.sys.isAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_AVOIDANCE))
					isAvoiding = false;
			}
			if(mapForget)
				map.applyMapFilter(filter);



			// Publish SLAM data to GC
			slam.px = model.slam.px;
			slam.py = model.slam.py;
			slam.pz = model.slam.pz;
			slam.pd = model.slam.pd;
			slam.pv = model.slam.pv;
			slam.md = model.slam.di;

			if(nearestTarget < OBSTACLE_FAILDISTANCE) {

				slam.ox = (float)map.getNearestObstaclePosition().x;
				slam.oy = (float)map.getNearestObstaclePosition().y;
				slam.oz = (float)map.getNearestObstaclePosition().z;

			} else {

				slam.ox = 0; slam.oy = 0; slam.oz = 0;
			}

			slam.tms = model.sys.getSynchronizedPX4Time_us();
			control.sendMAVLinkMessage(slam);
		}
	}

	public void reset() {
		logger.writeLocalMsg("[msp] reset local map",MAV_SEVERITY.MAV_SEVERITY_NOTICE);
		reset(true);
	}

	public void saveMap2D() {
		LocaMap2DStorage store = new LocaMap2DStorage(map,model.state.g_lat, model.state.g_lon);
		store.write();
		logger.writeLocalMsg("[msp] Map for this home position stored.",MAV_SEVERITY.MAV_SEVERITY_INFO);
	}

	public void loadMap2D() {
		LocaMap2DStorage store = new LocaMap2DStorage(map, model.state.g_lat, model.state.g_lon);
		if(store.locateAndRead()) {
			logger.writeLocalMsg("[msp] Map for this home position loaded.",MAV_SEVERITY.MAV_SEVERITY_INFO);
			map.setDataModel(control.getCurrentModel()); map.toDataModel(false); map.setIsLoaded(true);
		}
		else
			logger.writeLocalMsg("[msp] No Map for this home position found.",MAV_SEVERITY.MAV_SEVERITY_WARNING);
	}

	public void registerTargetListener(IAutoPilotGetTarget cb) {
		isAvoiding = false;
		this.targetListener = cb;
	}

	public ILocalMap getMap2D() {
		return map;
	}

	public void reset(boolean grid) {
		if(grid) {
			map.reset();
			map.toDataModel(false);
		}
	}

	public void setTarget(float x, float y, float z, float yaw) {
		isAvoiding = false;
		Vector4D_F32 target = new Vector4D_F32(x,y,z,yaw);
		offboard.setTarget(target);
		offboard.start(OffboardManager.MODE_POSITION);
	}

	private void clearAutopilotActions() {
		isAvoiding = false;
		targetListener = null;
		model.sys.autopilot &= 0b11000000000000000111111111111111;
		offboard.removeActionListener();
		control.sendMAVLinkMessage(new msg_msp_micro_slam(2,1));
	}

	public void offboardPosHold(boolean enable) {
		if(enable) {
			offboard.setCurrentAsTarget();
			offboard.start(OffboardManager.MODE_POSITION);
			if(!model.sys.isStatus(Status.MSP_LANDED) && !model.sys.isStatus(Status.MSP_RC_ATTACHED)) {
				control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
						MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
						MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_OFFBOARD, 0 );
			}
		} else {
			if(model.sys.nav_state==Status.NAVIGATION_STATE_OFFBOARD) {
				model.sys.autopilot = 0;
				control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
						MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
						MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_POSCTL, 0 );
			}
			offboard.stop();
		}
	}

	public void abort() {
		clearAutopilotActions();
		model.sys.autopilot &= 0b11000000000000000000000000000001;
		if(model.sys.isStatus(Status.MSP_RC_ATTACHED)) {
			control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
					MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
					MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_POSCTL, 0 );
			offboard.stop();
		} else {
			offboard.setCurrentAsTarget();
			offboard.start(OffboardManager.MODE_POSITION);
		}
	}

	public void returnToLand(int delay_ms) {

		final Vector4D_F32 target = new Vector4D_F32(0,0,model.state.l_z,0);
		logger.writeLocalMsg("[msp] Autopilot: Return to land.",MAV_SEVERITY.MAV_SEVERITY_INFO);

		model.sys.setAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_AVOIDANCE, true);
		isAvoiding = false;
		autopilot.registerTargetListener((n)->{
			n.set(target);
		});

		offboard.registerActionListener((m,d) -> {
			offboard.finalize();
			logger.writeLocalMsg("[msp] Autopilot: Home reached.",MAV_SEVERITY.MAV_SEVERITY_INFO);
			control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_NAV_LAND, 5, 0, 0, 0.05f );
			control.sendMAVLinkMessage(new msg_msp_micro_slam(2,1));
		});

		offboard.setTarget(target);
		offboard.start(OffboardManager.MODE_SPEED_POSITION);
	}

	public void stop_at_position() {
		offboard.setCurrentAsTarget();
		offboard.start(OffboardManager.MODE_POSITION);
	}


	public void obstacleAvoidance() {


		final Vector4D_F32 current    = new Vector4D_F32();
		final Vector4D_F32 projected  = new Vector4D_F32();

		float[] ctl = new float[2];

		final msg_msp_micro_slam slam = new msg_msp_micro_slam(2,1);

		current.set(model.state.l_x,model.state.l_y,model.state.l_z,Float.NaN);

		lvfh.init(model.hud.s);

		// Determine projected position via CB
		if(targetListener!=null) {
			targetListener.getTarget(projected);
		} else
			return;

		offboard.setTarget(projected);

		// determine velocity setpoint via callback
		offboard.registerExternalControlListener((speed, target_dir, distance) -> {

			current.set(model.state.l_x,model.state.l_y,model.state.l_z,Float.NaN);

			if(!model.sys.isAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_AVOIDANCE)) {
				offboard.setTarget(current);
			}

			try {
				lvfh.select(MSP3DUtils.angleXY(projected, current)+(float)Math.PI, speed, distance*1000f);
				ctl[IOffboardExternalControl.ANGLE] = (float)(2* Math.PI) - lvfh.getSelectedDirection() - (float)Math.PI/2f;
				ctl[IOffboardExternalControl.SPEED] = lvfh.getSelectedSpeed();

			} catch(Exception e) {
				logger.writeLocalMsg("Obstacle Avoidance: No path found", MAV_SEVERITY.MAV_SEVERITY_WARNING);
				offboard.setTarget(current);
			}
			return ctl;
		});

		offboard.start(OffboardManager.MODE_SPEED_POSITION);
		if(!model.sys.isStatus(Status.MSP_LANDED)) {
			control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
					MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
					MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_OFFBOARD, 0 );
		}
		logger.writeLocalMsg("[msp] ObstacleAvoidance executed",MAV_SEVERITY.MAV_SEVERITY_WARNING);
	}

	public void return_along_path(boolean enable) {

		isAvoiding = false;
		model.sys.setAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_AVOIDANCE, false);

		if(!tracker.freeze())
			return;

		logger.writeLocalMsg("[msp] Return along the path",MAV_SEVERITY.MAV_SEVERITY_INFO);
		offboard.setTarget(tracker.pollLastFreezedWaypoint().getValue());
		offboard.start(OffboardManager.MODE_POSITION);
		offboard.registerActionListener((m,d) -> {
			Entry<Long, Vector4D_F32> e = tracker.pollLastFreezedWaypoint();
			if(e!=null && e.getValue().z < -0.3f && !isAvoiding)  {
				offboard.setTarget(e.getValue());
			}
			else {
				tracker.unfreeze();
				logger.writeLocalMsg("[msp] Return finalized",MAV_SEVERITY.MAV_SEVERITY_INFO);
				clearAutopilotActions() ;
			}
		});
	}

	public void moveto(float x, float y, float z, float yaw) {
		final Vector4D_F32 target     = new Vector4D_F32(x,y,model.state.l_z,Float.NaN);

		if(flowCheck && !model.sys.isSensorAvailable(Status.MSP_PIX4FLOW_AVAILABILITY)) {
			logger.writeLocalMsg("[msp] Autopilot: Aborting. No Flow available.",MAV_SEVERITY.MAV_SEVERITY_WARNING);
			return;
		}

		execute(target, (m,d) -> {
			offboard.finalize();
			logger.writeLocalMsg("[msp] Autopilot: Target reached.",MAV_SEVERITY.MAV_SEVERITY_INFO);
			control.sendMAVLinkMessage(new msg_msp_micro_slam(2,1));
		});
	}

	private float getAvoidanceDistance( float speed ) {
		float val = OBSTACLE_MINDISTANCE_0MS + (speed*( OBSTACLE_MINDISTANCE_1MS-OBSTACLE_MINDISTANCE_0MS ));
		if ( val < 0 )
			val = 0;
		return val;
	}

	public void execute(Vector4D_F32 target,IOffboardTargetAction action) {
		this.registerTargetListener((n)->{ n.set(target); });
		offboard.registerActionListener(action);
		offboard.setTarget(target);
		offboard.start(OffboardManager.MODE_SPEED_POSITION);
	}


	///************ Experimental modes

	public void square(float distance) {
		final Vector4D_F32 target  = new Vector4D_F32(model.state.l_x,model.state.l_y,model.state.l_z,Float.NaN);
		target.x += distance;
		autopilot.execute(target, (m,d) -> {
			target.y += distance;
			autopilot.execute(target, (m1,d1) -> {
				target.x -= distance;
				autopilot.execute(target, (m2,d2) -> {
					target.y -= distance;
					autopilot.execute(target, (m3,d3) -> {
						offboard.finalize();
						target.y -= distance;
						logger.writeLocalMsg("[msp] Autopilot: Home reached.",MAV_SEVERITY.MAV_SEVERITY_INFO);
						control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_NAV_LAND, 5, 0, 0, 0.05f );
					});
				});
			});
		});
	}

	// Circle mode
	private Vector3D_F32 circleCenter = new Vector3D_F32();
	private Vector3D_F32 circleTarget = new Vector3D_F32();
	private float inc;
	private Vector3D_F32 circleDelta = new Vector3D_F32();

	public void enableCircleMode(boolean enable, float radius) {
		if(enable) {
			inc = model.attitude.y;
			circleCenter.set(model.state.l_x,model.state.l_y,model.state.l_z);
			circleTarget.set(circleCenter);
			circleDelta.set((float)Math.sin(inc), (float)Math.cos(inc), 0);
			circleTarget.plusIP(circleDelta);

			offboard.registerActionListener((m,d) -> {
				inc = inc+0.2f;
				circleTarget.set(circleCenter);
				circleDelta.set((float)Math.sin(inc)*radius, (float)Math.cos(inc)*radius, 0);
				circleTarget.plusIP(circleDelta);
				offboard.setTarget(circleTarget,0);
			});
			offboard.setTarget(circleTarget);
			offboard.start(OffboardManager.MODE_POSITION);
			logger.writeLocalMsg("[msp] Circlemode activated",MAV_SEVERITY.MAV_SEVERITY_INFO);
		}
		else {
			offboard.setTarget(circleCenter);
			offboard.registerActionListener((m,d) -> {
				logger.writeLocalMsg("[msp] Circlemode stopped",MAV_SEVERITY.MAV_SEVERITY_INFO);
			});
		}
	}

	public void enableDebugMode1(boolean enable, float delta) {
		if(enable) {
			circleCenter.set(model.state.l_x,model.state.l_y,model.state.l_z);
			circleTarget.set(circleCenter);
			circleDelta.set(delta,0,0);
			circleTarget.plusIP(circleDelta);

			offboard.registerActionListener((m,d) -> {
				circleTarget.plusIP(circleDelta);
				offboard.setTarget(circleTarget);
			});
			offboard.setTarget(circleTarget);
			offboard.start(OffboardManager.MODE_POSITION);
			logger.writeLocalMsg("[msp] DebugMode1 activated",MAV_SEVERITY.MAV_SEVERITY_INFO);
		}
		else {
			offboard.registerActionListener((m,d) -> {
				logger.writeLocalMsg("[msp] DebugMode1 stopped",MAV_SEVERITY.MAV_SEVERITY_INFO);
			});
		}
	}

	public void enableDebugMode2(boolean enable, float delta) {
		if(enable) {
			circleCenter.set(model.state.l_x,model.state.l_y,model.state.l_z);
			circleTarget.set(circleCenter);
			circleDelta.set(0,delta,0);
			circleTarget.plusIP(circleDelta);

			offboard.registerActionListener((m,d) -> {
				circleTarget.plusIP(circleDelta);
				offboard.setTarget(circleTarget);
			});
			offboard.setTarget(circleTarget);
			offboard.start(OffboardManager.MODE_POSITION);
			logger.writeLocalMsg("[msp] DebugMode2 activated",MAV_SEVERITY.MAV_SEVERITY_INFO);
		}
		else {
			offboard.registerActionListener((m,d) -> {
				logger.writeLocalMsg("[msp] DebugMod2 stopped",MAV_SEVERITY.MAV_SEVERITY_INFO);
			});
		}
	}

	public void applyMapFilter() {
		map.applyMapFilter(filter);
	}

}
