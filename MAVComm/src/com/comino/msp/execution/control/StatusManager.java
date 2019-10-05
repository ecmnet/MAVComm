/****************************************************************************
 *
 *   Copyright (c) 2017,2019 Eike Mansfeld ecm@gmx.de. All rights reserved.
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

package com.comino.msp.execution.control;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import com.comino.msp.execution.control.listener.IMSPStatusChangedListener;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.Status;
import com.comino.msp.utils.ExecutorService;

public class StatusManager implements Runnable {

	private static final long TIMEOUT_IMU         = 5000000;
	private static final long TIMEOUT_VISION      = 2000000;
	private static final long TIMEOUT_CONNECTED   = 1000000;
	private static final long TIMEOUT_RC_ATTACHED = 5000000;
	private static final long TIMEOUT_GPOS        = 2000000;
	private static final long TIMEOUT_LPOS        = 2000000;
	private static final long TIMEOUT_GPS         = 2000000;
	private static final long TIMEOUT_SLAM        = 5000000;
	private static final long TIMEOUT_FLOW        = 2000000;

	public static final byte  TYPE_ALL             = 0;
	public static final byte  TYPE_PX4_STATUS      = 1;
	public static final byte  TYPE_PX4_NAVSTATE    = 2;
	public static final byte  TYPE_RESERVED        = 3;
	public static final byte  TYPE_MSP_AUTOPILOT   = 4;
	public static final byte  TYPE_MSP_SERVICES    = 5;

	public static final byte  EDGE_BOTH            = 0;
	public static final byte  EDGE_RISING          = 1;
	public static final byte  EDGE_FALLING         = 2;


	public static final int   MASK_ALL       = 0xFFFFFFFF;

	public DataModel model                   = null;

	private  Status status_current 	 = null;
	private  Status status_old 		 = null;

	private List<StatusListenerEntry>  list  = null;

	private boolean isRunning                = false;

	private long t_armed_start			     = 0;
	private Future<?> task                   = null;


	public StatusManager(DataModel model) {
		this.model = model;
		this.status_current = new Status();
		this.status_old     = new Status();
		this.list  = new ArrayList<StatusListenerEntry>();
	}

	public void start() {
		if(isRunning)
			return;
		isRunning = true;
		status_old.set(model.sys);
		task = ExecutorService.submit(this, ExecutorService.LOW, 50);
	}

	public void stop() {
		isRunning = false;
		task.cancel(false);
	}


	private void addListener(byte type, int mask, int timeout_ms, int edge, IMSPStatusChangedListener listener) {
		StatusListenerEntry entry = new StatusListenerEntry();
		entry.listener    = listener;
		entry.type        = type;
		entry.mask        = mask;
		entry.timeout_ms  = timeout_ms;
		entry.state       = edge;
		list.add(entry);
	}

	public void addListener(byte type, int box, int edge, IMSPStatusChangedListener listener) {
		// For NAV State use mask as comparison value
		if(type == TYPE_PX4_NAVSTATE)
			addListener(type, box, 0, edge, listener);
		else
			addListener(type, 1 << box, 0, edge, listener);
	}

	public void addListener(byte type, int box, IMSPStatusChangedListener listener) {
		if(type == TYPE_PX4_NAVSTATE)
			addListener(type, box, 0, EDGE_BOTH, listener);
		else
			addListener(type, 1 << box, 0, EDGE_BOTH, listener);
	}

	public void addListener(int box, IMSPStatusChangedListener listener) {
		addListener(TYPE_PX4_STATUS, 1 << box, 0, EDGE_BOTH, listener);
	}

	public void addListener(IMSPStatusChangedListener listener) {
		addListener(TYPE_PX4_STATUS, MASK_ALL, 0, EDGE_BOTH, listener);
	}

	public void removeAll() {
		list.clear();
	}

	public void reset() {
		status_old.status &= 0x1;
	}

	@Override
	public void run() {


		status_current.set(model.sys);

		if (status_current.isStatus(Status.MSP_ARMED))
			model.sys.t_armed_ms = System.currentTimeMillis() - t_armed_start;

		if(status_old.isEqual(status_current))
			return;


		if(status_current.isStatusChanged(status_old, 1<<Status.MSP_ARMED) && status_current.isStatus(Status.MSP_ARMED))
			t_armed_start = System.currentTimeMillis();

//		if(status_old.nav_state!=status_current.nav_state)
//			System.out.println(status_old.nav_state+" -> "+status_current.nav_state);

		try {

			for (StatusListenerEntry entry : list) {

				switch(entry.type) {
				case TYPE_PX4_STATUS:
					switch(entry.state) {
					case EDGE_BOTH:
						if(status_current.isStatusChanged(status_old, entry.mask)) {
							update_callback(entry.listener, status_current);
							entry.last_triggered = System.currentTimeMillis();
						}
						break;
					case EDGE_RISING:
						if(status_current.isStatusChanged(status_old, entry.mask, true)) {
							update_callback(entry.listener, status_current);
							entry.last_triggered = System.currentTimeMillis();
						}
						break;
					case EDGE_FALLING:
						if(status_current.isStatusChanged(status_old, entry.mask, false)) {
							update_callback(entry.listener, status_current);
							entry.last_triggered = System.currentTimeMillis();
						}
						break;
					}
					break;
				case TYPE_PX4_NAVSTATE:

					switch(entry.state) {
					case EDGE_BOTH:
						if((status_current.nav_state != entry.mask && status_old.nav_state == entry.mask ) ||
								(status_current.nav_state == entry.mask && status_old.nav_state != entry.mask )) {
							update_callback(entry.listener, status_current);
							entry.last_triggered = System.currentTimeMillis();
						}
						break;
					case EDGE_RISING:
						if(status_current.nav_state == entry.mask && status_old.nav_state!=entry.mask) {
							update_callback(entry.listener, status_current);
							entry.last_triggered = System.currentTimeMillis();
						}
						break;
					case EDGE_FALLING:

						if(status_current.nav_state != entry.mask && status_old.nav_state==entry.mask) {
							update_callback(entry.listener, status_current);
							entry.last_triggered = System.currentTimeMillis();
						}
						break;
					}

					break;
				case TYPE_RESERVED:

					// TODO: Implement MSP_STATUS

					break;
				case TYPE_MSP_SERVICES:

					switch(entry.state) {
					case EDGE_BOTH:
						if(status_current.isSensorChanged(status_old, entry.mask)) {
							update_callback(entry.listener, status_current);
							entry.last_triggered = System.currentTimeMillis();
						}
						break;
					case EDGE_RISING:
						if(status_current.isSensorChanged(status_old, entry.mask, true)) {
							update_callback(entry.listener, status_current);
							entry.last_triggered = System.currentTimeMillis();
						}
						break;
					case EDGE_FALLING:

						if(status_current.isSensorChanged(status_old, entry.mask, false)) {
							update_callback(entry.listener, status_current);
							entry.last_triggered = System.currentTimeMillis();
						}
						break;
					}
					break;

				case TYPE_MSP_AUTOPILOT:
					if(status_current.isAutopilotModeChanged(status_old, entry.mask)) {
						update_callback(entry.listener, status_current);
						entry.last_triggered = System.currentTimeMillis();
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		status_old.set(status_current);

		checkTimeouts();
	}

	private void update_callback(final IMSPStatusChangedListener listener, final Status current ) {

		ExecutorService.get().execute(() -> {

			listener.update(current);

		});

	}

	private void checkTimeouts() {

		if (checkTimeOut(model.attitude.tms, TIMEOUT_IMU)) {
			model.sys.setSensor(Status.MSP_IMU_AVAILABILITY, false);
		}

		if (checkTimeOut(model.state.tms, TIMEOUT_LPOS)) {
			model.sys.setStatus(Status.MSP_LPOS_VALID, false);
		}

		if (checkTimeOut(model.state.gpos_tms, TIMEOUT_GPOS)) {
			model.sys.setStatus(Status.MSP_GPOS_VALID, false);
		}

		if (checkTimeOut(model.raw.tms, TIMEOUT_FLOW)) {
			model.sys.setSensor(Status.MSP_PIX4FLOW_AVAILABILITY, false);
			model.raw.fq = 0;
		}

		if (checkTimeOut(model.vision.tms, TIMEOUT_VISION)) {
			model.sys.setSensor(Status.MSP_OPCV_AVAILABILITY, false);
			model.vision.clear();
		}

		if (checkTimeOut(model.gps.tms, TIMEOUT_GPS)) {
			model.sys.setSensor(Status.MSP_GPS_AVAILABILITY, false);
			model.gps.clear();
		}

		if (checkTimeOut(model.slam.tms, TIMEOUT_SLAM)) {
			model.sys.setSensor(Status.MSP_SLAM_AVAILABILITY, false);
			model.slam.clear();
		}

		if(!model.sys.isStatus(Status.MSP_SITL)) {
			if (checkTimeOut(model.rc.tms, TIMEOUT_RC_ATTACHED)) {
				model.sys.setStatus(Status.MSP_RC_ATTACHED, (false));
				model.rc.rssi = 0;
			}
		}

		if (checkTimeOut(model.sys.tms, TIMEOUT_CONNECTED) && model.sys.isStatus(Status.MSP_CONNECTED)) {
			model.sys.setStatus(Status.MSP_CONNECTED, false);
			//	model.sys.setStatus(Status.MSP_ACTIVE, false);
			model.sys.tms = model.sys.getSynchronizedPX4Time_us();
		}
	}

	private boolean checkTimeOut(long tms, long timeout) {
		return model.sys.getSynchronizedPX4Time_us() > (tms + timeout);
	}

	private class StatusListenerEntry {

		public IMSPStatusChangedListener	listener = null;
		public int							mask     = MASK_ALL;
		public byte                         type     = TYPE_PX4_STATUS;
		public long                   last_triggered = 0;
		public int                        timeout_ms = 0;
		public int                          state    = EDGE_BOTH;
	}
}
