package com.comino.msp.execution.autopilot;

import org.mavlink.messages.MAV_CMD;
import org.mavlink.messages.MAV_MODE_FLAG;
import org.mavlink.messages.MAV_SEVERITY;
import org.mavlink.messages.MSP_AUTOCONTROL_ACTION;
import org.mavlink.messages.MSP_AUTOCONTROL_MODE;
import org.mavlink.messages.lquac.msg_msp_micro_slam;

import com.comino.main.MSPConfig;
import com.comino.mav.control.IMAVController;
import com.comino.mav.mavlink.MAV_CUST_MODE;
import com.comino.msp.execution.control.StatusManager;
import com.comino.msp.execution.offboard.IOffboardTargetAction;
import com.comino.msp.execution.offboard.OffboardManager;
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
import com.comino.msp.utils.MSPMathUtils;

import georegression.struct.point.Vector3D_F32;
import georegression.struct.point.Vector4D_F32;


public abstract class AutoPilotBase implements Runnable {

	protected static final int   CERTAINITY_THRESHOLD      	= 3;
	protected static final float WINDOWSIZE       			= 4.0f;

	private static AutoPilotBase  autopilot    = null;

	protected DataModel               model    = null;
	protected MSPLogger               logger   = null;
	protected IMAVController          control  = null;
	protected ILocalMap				  map      = null;
	protected OffboardManager         offboard = null;

	protected boolean			    mapForget  = false;
	protected boolean               flowCheck  = false;

	protected boolean              isRunning   = false;

	protected ILocalMapFilter mapFilter = null;



	public static AutoPilotBase getInstance(Class<?> clazz, IMAVController control,MSPConfig config) {
		if(autopilot == null)
			try {
				autopilot =(AutoPilotBase)clazz.getDeclaredConstructor(IMAVController.class,MSPConfig.class).newInstance(control,config);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return autopilot;
	}

	public static AutoPilotBase getInstance() {
		return autopilot;
	}

	public AutoPilotBase(IMAVController control, MSPConfig config) {

		String instanceName = this.getClass().getSimpleName();

		System.out.println(instanceName+" instantiated");

		this.control  = control;
		this.model    = control.getCurrentModel();
		this.logger   = MSPLogger.getInstance();
		this.offboard = new OffboardManager(control);

		if(control.isSimulation())
			this.map      = new LocalMap2DArray(model,WINDOWSIZE,CERTAINITY_THRESHOLD);
		else
			this.map      = new LocalMap2DRaycast(model,WINDOWSIZE,CERTAINITY_THRESHOLD);

		this.mapForget = config.getBoolProperty("autopilot_forget_map", "false");
		System.out.println(instanceName+": Map forget enabled: "+mapForget);
		if(mapForget)
			registerMapFilter(new DenoiseMapFilter(800,800));

		this.flowCheck = config.getBoolProperty("autopilot_flow_check", "true") & !model.sys.isStatus(Status.MSP_SITL);
		System.out.println(instanceName+": FlowCheck enabled: "+flowCheck);

		// Auto-Takeoff: Switch to Offboard and enable ObstacleAvoidance as soon as takeoff completed
		//
		// TODO: Landing during takeoff switches to offboard mode here => should directly land instead
		//       Update: 		works in SITL, but not on vehicle (timing?)
		control.getStatusManager().addListener(StatusManager.TYPE_PX4_NAVSTATE, Status.NAVIGATION_STATE_AUTO_TAKEOFF, StatusManager.EDGE_FALLING, (o,n) -> {
			if(n.nav_state == Status.NAVIGATION_STATE_AUTO_LAND)
				return;
			offboard.setCurrentSetPointAsTarget();
			offboard.start(OffboardManager.MODE_LOITER);
			control.sendMAVLinkCmd(MAV_CMD.MAV_CMD_DO_SET_MODE,
					MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED,
					MAV_CUST_MODE.PX4_CUSTOM_MAIN_MODE_OFFBOARD, 0 );

			this.takeoffCompleted();

			control.writeLogMessage(new LogMessage("[msp] Auto-takeoff completed.", MAV_SEVERITY.MAV_SEVERITY_NOTICE));


		});

		// Stop offboard updater as soon as landed
		control.getStatusManager().addListener(StatusManager.TYPE_PX4_STATUS, Status.MSP_LANDED, StatusManager.EDGE_RISING, (o,n) -> {
			offboard.stop();
		});

	}

	protected void takeoffCompleted() {

	}


	protected void start() {
		isRunning = true;
		Thread worker = new Thread(this);
		worker.setPriority(Thread.MIN_PRIORITY);
		worker.setName("AutoPilot");
		worker.start();
	}

	protected void stop() {
		isRunning = false;
	}



	@Override
	public abstract void run();

	/*******************************************************************************/
	// Command dispatching


	public void setMode(boolean enable, int mode, float param) {

		model.sys.setAutopilotMode(mode, enable);

		switch(mode) {
		case MSP_AUTOCONTROL_MODE.ABORT:
			abort();
			break;
		case MSP_AUTOCONTROL_ACTION.SAVE_MAP2D:
			saveMap2D();
			break;
		case MSP_AUTOCONTROL_ACTION.LOAD_MAP2D:
			loadMap2D();
			break;
		case MSP_AUTOCONTROL_ACTION.DEBUG_MODE1:
			setXObstacleForSITL();
			break;
		case MSP_AUTOCONTROL_ACTION.DEBUG_MODE2:
			setYObstacleForSITL();
			break;
		case MSP_AUTOCONTROL_ACTION.OFFBOARD_UPDATER:
			offboardPosHold(enable);
			break;
		case MSP_AUTOCONTROL_ACTION.APPLY_MAP_FILTER:
			//	applyMapFilter();
			break;
		}
	}


	/*******************************************************************************/
	// Standard setpoint setting


	public void setSpeed(boolean enable, float p, float r, float h, float y) {

		if(enable)
			offboard.setTarget(p,r,h,y,OffboardManager.MODE_BODY_SPEED);
		else
			offboard.setCurrentAsTarget();
	}


	public void setTarget(float x, float y, float z, float yaw) {
		Vector4D_F32 target = new Vector4D_F32(x,y,z,yaw);
		offboard.setTarget(target);
		offboard.start(OffboardManager.MODE_SPEED_POSITION);

	}

	public void moveto(float x, float y, float z, float yaw) {
		final Vector4D_F32 target = new Vector4D_F32(x,y,z,yaw);
		if(flowCheck && !model.sys.isSensorAvailable(Status.MSP_PIX4FLOW_AVAILABILITY)) {
			logger.writeLocalMsg("[msp] Autopilot: Aborting. No Flow available.",MAV_SEVERITY.MAV_SEVERITY_WARNING);
			return;
		}
		offboard.registerActionListener( (m,d) -> {
			offboard.finalize();
			logger.writeLocalMsg("[msp] Autopilot: Target reached.",MAV_SEVERITY.MAV_SEVERITY_INFO);
		});
		offboard.setTarget(target);
		offboard.start(OffboardManager.MODE_SPEED_POSITION);


	}

	/*******************************************************************************/
	// Standard actions

	public void registerMapFilter(ILocalMapFilter filter) {
		System.out.println("registering MapFilter "+filter.getClass().getSimpleName());
		this.mapFilter = filter;
	}


	public void offboardPosHold(boolean enable) {
		if(enable) {
			offboard.start(OffboardManager.MODE_LOITER);
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
			offboard.start(OffboardManager.MODE_LOITER);
		}
	}



	protected void clearAutopilotActions() {
		model.sys.autopilot &= 0b11000000000000000111111111111111;
		offboard.removeActionListener();
		control.sendMAVLinkMessage(new msg_msp_micro_slam(2,1));
	}


	/*******************************************************************************/
	// Map management

	public void resetMap() {
		logger.writeLocalMsg("[msp] reset local map",MAV_SEVERITY.MAV_SEVERITY_NOTICE);
		map.reset();
		map.toDataModel(false);
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

	public ILocalMap getMap2D() {
		return map;
	}


	/*******************************************************************************/
	// SITL testing

	public void setCircleObstacleForSITL() {
		if(map==null)
			return;
		map.reset();
		Vector3D_F32   pos          = new Vector3D_F32();
		System.err.println("SITL -> set example obstacle map");
		pos.x = 0.5f + model.state.l_x;
		pos.y = 0.4f + model.state.l_y;
		pos.z = 1.0f + model.state.l_z;
		map.update(pos); map.update(pos); map.update(pos);
		pos.y = 0.45f + model.state.l_y;
		map.update(pos); map.update(pos); map.update(pos);
		pos.y = 0.50f + model.state.l_y;
		map.update(pos); map.update(pos); map.update(pos);
		pos.y = 0.55f + model.state.l_y;
		map.update(pos); map.update(pos); map.update(pos);
		pos.y = 0.60f + model.state.l_y;
		map.update(pos); map.update(pos); map.update(pos);
		pos.y = 0.65f + model.state.l_y;
		map.update(pos); map.update(pos); map.update(pos);
		pos.y = 0.70f + model.state.l_y;
		map.update(pos); map.update(pos); map.update(pos);
	}

	public void setXObstacleForSITL() {
		if(map==null)
			return;
		map.reset();
		Vector3D_F32   pos          = new Vector3D_F32();
		System.err.println("SITL -> set example obstacle map");

		pos.y = 2.25f + model.state.l_y;
		pos.z =  model.state.l_z;
		for(int i = 0; i < 40;i++) {
			pos.x = -1.25f + i *0.05f + model.state.l_x;
			map.update(pos); map.update(pos); map.update(pos);
		}

		pos.y = 3.75f + model.state.l_y;
		pos.z =  model.state.l_z;
		for(int i = 0; i < 30;i++) {
			pos.x = -1.25f + i *0.05f + model.state.l_x;
			map.update(pos); map.update(pos); map.update(pos);
		}

		for(int i = 0; i < 30;i++) {
			pos.x = 1.25f + i *0.05f + model.state.l_x;
			map.update(pos); map.update(pos); map.update(pos);
		}

		pos.x = 2.0f + model.state.l_x;
		for(int i = 0; i < 25;i++) {
			pos.y = -1 + i *0.05f + model.state.l_y;
			map.update(pos); map.update(pos); map.update(pos);
		}



	}

	public void setYObstacleForSITL() {
		float x,y;
		if(map==null)
			return;
		map.reset();
		Vector3D_F32   pos          = new Vector3D_F32();
		System.err.println("SITL -> set example obstacle map");
		pos.z = 1.0f + model.state.l_z;

		pos.y = 4f + model.state.l_y;
		pos.x = -0.15f + model.state.l_x;
		map.update(pos); map.update(pos); map.update(pos);
		pos.x = -0.10f + model.state.l_x;
		map.update(pos); map.update(pos); map.update(pos);
		pos.x = -0.05f + model.state.l_x;
		map.update(pos); map.update(pos); map.update(pos);
		pos.x =  0.00f + model.state.l_x;
		map.update(pos); map.update(pos); map.update(pos);
		pos.x = 0.05f + model.state.l_x;
		map.update(pos); map.update(pos); map.update(pos);
		pos.x = 0.10f + model.state.l_x;
		map.update(pos); map.update(pos); map.update(pos);
		pos.x = 0.15f + model.state.l_x;
		map.update(pos); map.update(pos); map.update(pos);

		float dotx, doty ; float[] r = new float[2];

		for(int j=0; j< 30; j++) {

			dotx = (float)((Math.random()*15-5f));
			doty = (float)((Math.random()*15-5f));

			MSPMathUtils.rotateRad(r, dotx, doty, (float)Math.random() * 6.28f);

			for(int i=0; i< 40; i++) {

				x =  (float)Math.random()*.8f - 0.4f + r[0];
				y =  (float)Math.random()*.8f - 0.4f + r[1];

				pos.x = x;
				pos.y = y ;
				map.update(pos); map.update(pos); map.update(pos);

			}
		}

	}


}
