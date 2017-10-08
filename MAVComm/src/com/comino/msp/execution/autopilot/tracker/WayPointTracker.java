package com.comino.msp.execution.autopilot.tracker;

import java.util.Map.Entry;
import java.util.TreeMap;

import com.comino.mav.control.IMAVController;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.Status;
import com.comino.msp.utils.MSP3DUtils;

import georegression.struct.point.Vector4D_F32;

public class WayPointTracker implements Runnable {

	private static final int MIN_FREEZE_WP     = 3;

	private static final int MAX_WAYPOINTS     = 100;
	private static final int RATE_MS           = 100;

	private final TreeMap<Long,Vector4D_F32>  list;
	private final TreeMap<Long,Vector4D_F32>  freezed;
	private final DataModel                   model;

	private boolean                       enabled = false;

	public WayPointTracker(IMAVController control) {
		this.list    = new TreeMap<Long,Vector4D_F32>();
		this.freezed = new TreeMap<Long,Vector4D_F32>();
		this.model = control.getCurrentModel();
		System.out.println("WaypointTracker initialized with length "+MAX_WAYPOINTS*RATE_MS / 1000 +" seconds");

		control.getStatusManager().addListener(Status.MSP_LANDED, (o,n) -> {
			if(n.isStatus(Status.MSP_LANDED))
				stop();
			else
				start();
		});
	}

	public Entry<Long, Vector4D_F32> getWaypoint(long ago_ms) {
		return list.floorEntry(model.sys.getSynchronizedPX4Time_us()/1000-ago_ms);
	}

	public Entry<Long, Vector4D_F32> pollLastWaypoint() {
		if(list.size()>0)
			return list.pollLastEntry();
		return null;
	}

	public boolean freeze() {
		freezed.clear();
		if(list.size()>MIN_FREEZE_WP) {
			freezed.putAll(list);
			return true;
		}
		return false;
	}

	public void unfreeze() {
		list.clear();
		freezed.clear();
	}


	public Entry<Long, Vector4D_F32> pollLastFreezedWaypoint() {
		if(freezed.size()>0)
			return freezed.pollLastEntry();
		return null;
	}

	public TreeMap<Long, Vector4D_F32> getFreezed() {
		return freezed;
	}

	public void start() {
		if(!enabled) {
			enabled = true;
			new Thread(this).start();
		}
	}

	public void stop() {
		enabled = false;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public TreeMap<Long,Vector4D_F32> getList() {
		return list;
	}

	@Override
	public void run() {

		list.clear(); freezed.clear(); long tms = 0; long sleep_tms = 0; float distance = Float.MAX_VALUE;

		while(enabled) {
			tms = model.sys.getSynchronizedPX4Time_us()/1000;
			Vector4D_F32 waypoint = new Vector4D_F32(model.state.l_x, model.state.l_y, model.state.l_z, model.attitude.y);

			if(!list.isEmpty())
				distance = MSP3DUtils.distance3D(waypoint, list.lastEntry().getValue());

			if((distance > 0.1f || list.isEmpty()) && freezed.isEmpty()) {
				if(list.size()>=MAX_WAYPOINTS)
					list.pollFirstEntry();
				list.put(tms, waypoint);
			}

			sleep_tms = RATE_MS - (model.sys.getSynchronizedPX4Time_us()/1000 - tms );
			tms = model.sys.getSynchronizedPX4Time_us();

			if(sleep_tms> 0 && enabled)
				try { Thread.sleep(sleep_tms); 	} catch (InterruptedException e) { }
		}
	}

	public String toString() {
		return list.toString();
	}


}
