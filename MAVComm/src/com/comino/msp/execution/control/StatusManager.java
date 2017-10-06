package com.comino.msp.execution.control;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.comino.msp.execution.control.listener.IMSPStatusChangedListener;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.Status;
import com.comino.msp.utils.ExecutorService;

public class StatusManager implements Runnable {

	public static final byte  TYPE_ALL             = 0;
	public static final byte  TYPE_PX4_STATUS      = 1;
	public static final byte  TYPE_MSP_STATUS      = 2;
	public static final byte  TYPE_MSP_AUTOPILOT   = 3;

	public static final byte  EDGE_BOTH            = 0;
	public static final byte  EDGE_RISING          = 1;
	public static final byte  EDGE_FALLING         = 2;


	public static final int   MASK_ALL       = 0xFFFFFFFF;

	public DataModel model                   = null;

	private Status status_current 			 = null;
	private Status status_old 				 = null;
	private List<StatusListenerEntry>  list  = null;

	private long t_armed_start			     = 0;


	public StatusManager(DataModel model) {
		this.model = model;
		this.status_current = new Status();
		this.status_old     = new Status();
		this.list  = new ArrayList<StatusListenerEntry>();
		Thread t = new Thread(this);
		t.setPriority(Thread.MIN_PRIORITY);
		t.start();
		System.out.println("StatusManager started");

	}


	public void addListener(byte type, int mask, int edge, IMSPStatusChangedListener listener) {
		StatusListenerEntry entry = new StatusListenerEntry();
		entry.listener    = listener;
		entry.type        = type;
		entry.mask        = mask;
		entry.state       = edge;
		list.add(entry);
	}

	public void addListener(byte type, int box, IMSPStatusChangedListener listener) {
		addListener(type, 1 << box, EDGE_BOTH, listener);
	}

	public void addListener(int box, IMSPStatusChangedListener listener) {
		addListener(TYPE_PX4_STATUS, 1 << box, EDGE_BOTH, listener);
	}

	public void addListener(IMSPStatusChangedListener listener) {
		addListener(TYPE_PX4_STATUS, MASK_ALL, EDGE_BOTH, listener);
	}

	public void removeAll() {
		list.clear();
	}

	@Override
	public void run() {

		while(true) {

			try { Thread.sleep(50); } catch(Exception e) { }

			status_current.set(model.sys);

			if (status_current.isStatus(Status.MSP_ARMED))
				model.sys.t_armed_ms = System.currentTimeMillis() - t_armed_start;

			if(status_old.isEqual(status_current)) {
				continue;
			}

			if(status_current.isStatusChanged(status_old, 1<<Status.MSP_ARMED) && status_current.isStatus(Status.MSP_ARMED))
				t_armed_start = System.currentTimeMillis();


			try {
				for (StatusListenerEntry entry : list) {

					switch(entry.type) {
					case TYPE_PX4_STATUS:
						//	System.err.println(status_current.getStatus()+" "+entry.listener.getClass().getName()+":"+status_current.isStatusChanged(status_old, entry.mask));
						switch(entry.state) {
						case EDGE_BOTH:
							if(status_current.isStatusChanged(status_old, entry.mask)) {
								entry.listener.update(status_old, status_current);
							}
							break;
						case EDGE_RISING:
							if(status_current.isStatusChanged(status_old, entry.mask, true)) {
								entry.listener.update(status_old, status_current);
							}
							break;
						case EDGE_FALLING:
							if(status_current.isStatusChanged(status_old, entry.mask, false)) {
								entry.listener.update(status_old, status_current);
							}
							break;
						}
						break;
					case TYPE_MSP_STATUS:

						// TODO:

						break;
					case TYPE_MSP_AUTOPILOT:
						if(status_current.isAutopilotModeChanged(status_old, entry.mask)) {
							entry.listener.update(status_old, status_current);
						}
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			status_old.set(status_current);
		}

	}

	private class StatusListenerEntry {

		public IMSPStatusChangedListener	listener = null;
		public int							mask    = MASK_ALL;
		public byte                         type     = TYPE_PX4_STATUS;
		public int                          state    = EDGE_BOTH;
	}
}
