package com.comino.msp.execution.autopilot;

import org.mavlink.messages.MAV_DISTANCE_SENSOR;
import org.mavlink.messages.lquac.msg_obstacle_distance;

import com.comino.main.MSPConfig;
import com.comino.mav.control.IMAVController;
import com.comino.msp.slam.colprev.CollisionPreventionConverter;

import georegression.struct.point.Vector3D_F32;

public class CollisonPreventionPilot extends AutoPilotBase {

	private static final int              CYCLE_MS	= 50;

	private CollisionPreventionConverter  collprev  = null;;

	protected CollisonPreventionPilot(IMAVController control, MSPConfig config) {
		super(control,config);
		this.collprev = new CollisionPreventionConverter(map,CERTAINITY_THRESHOLD);
		start();
	}

	public void run() {

		float[]               distances;
		Vector3D_F32          current    = new Vector3D_F32();

		msg_obstacle_distance msg       = new msg_obstacle_distance(1,2);
		msg.increment = 5;
		msg.max_distance = (int)(WINDOWSIZE * 100);
		msg.min_distance = 1;
		msg.sensor_type = MAV_DISTANCE_SENSOR.MAV_DISTANCE_SENSOR_RADAR;

		System.out.println("CollisionPreventionPilot started");

		while(isRunning) {

			try { Thread.sleep(CYCLE_MS); } catch(Exception s) { }

			current.set(model.state.l_x, model.state.l_y,model.state.l_z);
			distances = collprev.update(current);

			for(int i=0;i<distances.length;i++) {
				if(distances[i]<10000)
					msg.distances[i] = (int)distances[i] / 10;
				else
					msg.distances[i] = msg.max_distance + 1;
			}

			msg.time_usec = model.sys.getSynchronizedPX4Time_us();

			control.sendMAVLinkMessage(msg);


		}

	}

}
