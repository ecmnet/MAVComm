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
import com.comino.msp.execution.autopilot.offboard.OffboardManager;
import com.comino.msp.execution.autopilot.tracker.WayPointTracker;
import com.comino.msp.execution.control.StatusManager;
import com.comino.msp.log.MSPLogger;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.LogMessage;
import com.comino.msp.model.segment.Status;
import com.comino.msp.slam.map.ILocalMap;
import com.comino.msp.slam.map.impl.LocalMap2DArray;
import com.comino.msp.slam.map.store.LocaMap2DStorage;
import com.comino.msp.slam.vfh.LocalVFH2D;
import com.comino.msp.utils.ExecutorService;
import com.comino.msp.utils.MSP3DUtils;

import georegression.struct.point.Vector3D_F32;
import georegression.struct.point.Vector4D_F32;

public class Autopilot2D implements Runnable {

	private static final int   CYCLE_MS					= 100;

	private static final int   CERTAINITY_THRESHOLD      = 10;
	private static final float ROBOT_RADIUS         		= 0.3f;
	private static final float WINDOWSIZE       			= 2.0f;

	private static final float MIN_DISTANCE_HYSTERESIS   = 0.2f;


	private static final float OBSTACLE_MINDISTANCE_0MS  = 0.5f;
	private static final float OBSTACLE_MINDISTANCE_1MS  = 1.5f;

	private static final float OBSTACLE_FAILDISTANCE     = ROBOT_RADIUS / 2;

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
	private float             	nearestTarget 	= 0;


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

		this.offboard = new OffboardManager(control);
		this.tracker  = new WayPointTracker(control);
		this.control  = control;
		this.model    = control.getCurrentModel();
		this.logger   = MSPLogger.getInstance();

		this.mapForget = config.getBoolProperty("autopilot_forget_map", "false");
		System.out.println("Autopilot2D: Map forget enabled: "+mapForget);

		this.map      = new LocalMap2DArray(model.grid.getExtension(),model.grid.getResolution(),WINDOWSIZE,CERTAINITY_THRESHOLD);
		this.lvfh     = new LocalVFH2D(map,ROBOT_RADIUS, CERTAINITY_THRESHOLD);

		// Auto-Takeoff: Switch to Offboard and enable ObstacleAvoidance as soon as takeoff completed
		//
		// TODO: Landing during takeoff switches to offboard mode here => should directly land instead
		//       Update: works in SITL, but not on vehicle (timing?)
		control.getStatusManager().addListener(StatusManager.TYPE_PX4_STATUS, Status.MSP_MODE_TAKEOFF, StatusManager.EDGE_FALLING, (o,n) -> {
            if(n.isStatus(Status.MSP_MODE_LANDING))
            	  return;
			offboard.setCurrentAsTarget();
			offboard.start(OffboardManager.MODE_POSITION);
			if(!model.sys.isStatus(Status.MSP_RC_ATTACHED)) {
				model.sys.setAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_AVOIDANCE, true);
				control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
						MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
						MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_OFFBOARD, 0 );
				control.writeLogMessage(new LogMessage("[msp] Auto-takeoff completed.", MAV_SEVERITY.MAV_SEVERITY_NOTICE));
			}
		//	loadMap2D();
		});

		control.getStatusManager().addListener(StatusManager.TYPE_MSP_AUTOPILOT, MSP_AUTOCONTROL_MODE.STEP_MODE,(o,n) -> {
			offboard.setStepMode(n.isAutopilotMode(MSP_AUTOCONTROL_MODE.STEP_MODE));
		});

		control.getStatusManager().addListener(StatusManager.TYPE_PX4_STATUS, Status.MSP_MODE_RTL, StatusManager.EDGE_RISING, (o,n) -> {
			offboard.stop();
		});

		// Stop offboard updater as soon as landed
		control.getStatusManager().addListener(StatusManager.TYPE_PX4_STATUS, Status.MSP_LANDED, StatusManager.EDGE_RISING, (o,n) -> {
			offboard.stop();
		});

		new Thread(this).start();

	}

	@Override
	public void run() {

		Vector3D_F32 current = new Vector3D_F32(); boolean tooClose = false; float min_distance;

		while(true) {
			try { Thread.sleep(CYCLE_MS); } catch(Exception s) { }

			current.set(model.state.l_x, model.state.l_y,model.state.l_z);
			map.toDataModel(model, false);
			lvfh.update_histogram(current, model.hud.s);

			nearestTarget = map.nearestDistance(model.state.l_y, model.state.l_x);
			if(nearestTarget < OBSTACLE_FAILDISTANCE && !tooClose ) {
				logger.writeLocalMsg("[msp] Collision warning.",MAV_SEVERITY.MAV_SEVERITY_CRITICAL);
				tooClose = true;
			}

			if(nearestTarget > OBSTACLE_FAILDISTANCE+MIN_DISTANCE_HYSTERESIS)
				tooClose = false;

			min_distance = getAvoidanceDistance(model.hud.s);

			if(nearestTarget < min_distance && !isAvoiding) {
					isAvoiding = true;
					if(model.sys.isAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_AVOIDANCE))
						obstacleAvoidance();
					if(model.sys.isAutopilotMode(MSP_AUTOCONTROL_MODE.JUMPBACK))
						jumpback(0.3f);
			}

			if(nearestTarget > (min_distance + MIN_DISTANCE_HYSTERESIS) && isAvoiding) {
				offboard.removeExternalControlListener();
				isAvoiding = false;
			}
			if(mapForget)
			  map.forget();
		}
	}

	public void reset() {
		logger.writeLocalMsg("[vis] reset local map",MAV_SEVERITY.MAV_SEVERITY_NOTICE);
		map.reset();
	}

	public void saveMap2D() {
		LocaMap2DStorage store = new LocaMap2DStorage(map,model.state.g_lat, model.state.g_lon);
		store.write();
		logger.writeLocalMsg("[msp] Map for this home position stored.",MAV_SEVERITY.MAV_SEVERITY_INFO);
	}

	public void loadMap2D() {
		LocaMap2DStorage store = new LocaMap2DStorage(map, model.state.g_lat, model.state.g_lon);
		if(store.locateAndRead())
			logger.writeLocalMsg("[msp] Map for this home position loaded.",MAV_SEVERITY.MAV_SEVERITY_INFO);
		else
			logger.writeLocalMsg("[msp] No Map for this home position found.",MAV_SEVERITY.MAV_SEVERITY_WARNING);
	}

	public void registerTargetListener(IAutoPilotGetTarget cb) {
		this.targetListener = cb;
	}

	public ILocalMap getMap2D() {
		return map;
	}

	public void reset(boolean grid) {
		clearAutopilotActions();
		if(grid)
			map.reset();
	}

	public void setTarget(float x, float y, float z, float yaw) {
		isAvoiding = false;
		Vector3D_F32 target = new Vector3D_F32(x,y,z);
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

	public void executeStep() {
		offboard.triggerStep();
	}

	public void offboardPosHold(boolean enable) {
		if(enable) {
			offboard.setCurrentAsTarget();
			offboard.start(OffboardManager.MODE_POSITION);
			if(!model.sys.isStatus(Status.MSP_LANDED)) {
				control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
						MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
						MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_OFFBOARD, 0 );
			}
		} else {
			if(model.sys.isStatus(Status.MSP_MODE_OFFBOARD)) {
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

		final Vector3D_F32 target = new Vector3D_F32(0,0,model.state.l_z);
		logger.writeLocalMsg("[msp] Autopilot: Return to land.",MAV_SEVERITY.MAV_SEVERITY_INFO);

		model.sys.setAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_AVOIDANCE, true);
		isAvoiding = false;
		autopilot.registerTargetListener((n)->{
			n.set(target);
		});

		offboard.registerActionListener((m,d) -> {
			offboard.finalize();
			logger.writeLocalMsg("[msp] Autopilot: Home reached.",MAV_SEVERITY.MAV_SEVERITY_INFO);
			ExecutorService.get().schedule(()-> {
				model.sys.setAutopilotMode(MSP_AUTOCONTROL_ACTION.RTL, false);
				control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_NAV_LAND, 0, 2, 0.05f );
			}, delay_ms, TimeUnit.MILLISECONDS);
			control.sendMAVLinkMessage(new msg_msp_micro_slam(2,1));
		});

		offboard.setTarget(target);
		offboard.start(OffboardManager.MODE_SPEED_POSITION);
	}

	public void jumpback(float distance) {

		isAvoiding = false;
		if(!tracker.freeze())
			return;

		final Vector4D_F32 current = MSP3DUtils.getCurrentVector4D(model);

		logger.writeLocalMsg("[msp] JumpBack executed",MAV_SEVERITY.MAV_SEVERITY_WARNING);
		offboard.start(OffboardManager.MODE_POSITION);
		if(!model.sys.isStatus(Status.MSP_LANDED)) {
			control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
					MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
					MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_OFFBOARD, 0 );
		}
		offboard.setTarget(tracker.pollLastFreezedWaypoint().getValue());

		offboard.registerActionListener((m,d) -> {
			Entry<Long, Vector4D_F32> e = tracker.pollLastFreezedWaypoint();
			if(e!=null && MSP3DUtils.distance3D(e.getValue(), current) < distance)
				offboard.setTarget(e.getValue());
			else {
				abort();
				tracker.unfreeze();
			}
		});
	}

	public void obstacleAvoidance() {

		float angle=0;  float projected_distance = 2.5f;

		final Vector3D_F32 current    = new Vector3D_F32();
		final Vector3D_F32 delta      = new Vector3D_F32();
		final Vector3D_F32 projected  = new Vector3D_F32();

		float[] ctl = new float[2];

		final msg_msp_micro_slam slam = new msg_msp_micro_slam(2,1);

		current.set(model.state.l_x,model.state.l_y,model.state.l_z);

		lvfh.init(model.hud.s);

		// Determine projected position via CB
		if(targetListener!=null) {
			targetListener.getTarget(projected);
		} else {
			// If no target by CB available => use last direction and project project
			projected.set(current);
			if(tracker.getList().size()>0)
			 angle = MSP3DUtils.angleXY(current, MSP3DUtils.convertTo3D(tracker.pollLastWaypoint().getValue()))+(float)Math.PI;

			delta.set((float)Math.sin(2*Math.PI-angle-(float)Math.PI/2f)*projected_distance,
				     (float)Math.cos(2*Math.PI-angle-(float)Math.PI/2f)*projected_distance, 0);
			projected.plusIP(delta);
		}

		offboard.setTarget(projected);

		// determine velocity setpoint via callback
		offboard.registerExternalControlListener((speed, target_dir, distance) -> {

			current.set(model.state.l_x,model.state.l_y,model.state.l_z);

			if(!model.sys.isAutopilotMode(MSP_AUTOCONTROL_MODE.OBSTACLE_AVOIDANCE)) {
				offboard.setTarget(current);
			}

			try {
				lvfh.select(MSP3DUtils.angleXY(projected, current)+(float)Math.PI, speed, distance*1000f);
				ctl[IOffboardExternalControl.ANGLE] = (float)(2* Math.PI) - lvfh.getSelectedDirection() - (float)Math.PI/2f;
				ctl[IOffboardExternalControl.SPEED] = lvfh.getSelectedSpeed();

				slam.px = projected.getY();
				slam.py = projected.getX();
				slam.md = nearestTarget;
				slam.pd = target_dir;
				slam.pv = speed*5;
				control.sendMAVLinkMessage(slam);

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
		final Vector3D_F32 target     = new Vector3D_F32(x,y,model.state.l_z);

		isAvoiding = false;
		autopilot.registerTargetListener((n)->{
			n.set(target);
		});

		offboard.registerActionListener((m,d) -> {
			offboard.finalize();
			logger.writeLocalMsg("[msp] Autopilot: Target reached.",MAV_SEVERITY.MAV_SEVERITY_INFO);
			control.sendMAVLinkMessage(new msg_msp_micro_slam(2,1));
		});

		offboard.setTarget(target);
		offboard.start(OffboardManager.MODE_SPEED_POSITION);

	}

	private float getAvoidanceDistance( float speed ) {
		float val = OBSTACLE_MINDISTANCE_0MS + (speed*( OBSTACLE_MINDISTANCE_1MS-OBSTACLE_MINDISTANCE_0MS ));
		if ( val < 0 )
			val = 0;
		return val;
	}


	///************ Experimental modes

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

}
