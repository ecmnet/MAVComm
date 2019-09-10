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

package com.comino.msp.execution.autopilot.tracker;

import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.comino.mav.control.IMAVController;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.Status;
import com.comino.msp.utils.ExecutorService;
import com.comino.msp.utils.MSP3DUtils;

import georegression.struct.point.Vector4D_F32;

public class WayPointTracker implements Runnable {

	//TODO: re-implement with speed_position mode

	private static final int MIN_FREEZE_WP   = 3;

	private final TreeMap<Long,Vector4D_F32>  list;
	private final TreeMap<Long,Vector4D_F32>  freezed;
	private final DataModel                   model;

	private static final int MAX_WAYPOINTS   = 50;
	private static final int RATE_MS         = 200;

	private boolean          enabled 		= false;

	private long tms = 0;
	private ScheduledFuture<?> future = null;

	public WayPointTracker(IMAVController control) {
		this.list    = new TreeMap<Long,Vector4D_F32>();
		this.freezed = new TreeMap<Long,Vector4D_F32>();
		this.model = control.getCurrentModel();
		System.out.println("WaypointTracker initialized (max. duration: "+MAX_WAYPOINTS*RATE_MS / 1000 +" seconds)");

		control.getStatusManager().addListener(Status.MSP_LANDED, (n) -> {
			if(n.isStatus(Status.MSP_LANDED))
				stop();
			else
				start();
		});

		control.getStatusManager().addListener(Status.MSP_CONNECTED, (n) -> {
			if(!n.isStatus(Status.MSP_LANDED)) {
				start();
			}
		});
	}

	public Entry<Long, Vector4D_F32> getWaypoint(long ago_ms) {
		return list.floorEntry(list.lastKey()-ago_ms);
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
			list.clear(); freezed.clear();
			enabled = true;
			future = ExecutorService.get().scheduleAtFixedRate(this, 50, RATE_MS, TimeUnit.MILLISECONDS);
		}
	}

	public void stop() {
		if(future!=null)
		  future.cancel(false);
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

		float distance = Float.MAX_VALUE;

		tms = model.sys.getSynchronizedPX4Time_us()/1000;
		Vector4D_F32 waypoint = new Vector4D_F32(model.state.l_x, model.state.l_y, model.state.l_z, model.attitude.y);

		if(!list.isEmpty())
			distance = MSP3DUtils.distance3D(waypoint, list.lastEntry().getValue());

		if((distance > 0.1f || list.isEmpty()) && freezed.isEmpty() && model.hud.ar > 0.4f) {
			if(list.size()>=MAX_WAYPOINTS)
				list.pollFirstEntry();
			list.put(tms, waypoint);
		}
	}

	public String toString() {
		return list.toString();
	}


}
