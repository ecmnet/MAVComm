package com.comino.msp.main.control.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.Status;
import com.comino.msp.utils.ExecutorService;

public class StatusManager implements Runnable {

	public static final byte  TYPE_CHANGE    = 0;
	public static final byte  TYPE_TIMEOUT   = 1;

	public static final int   MASK_ALL       = 0xFFFF;

	public DataModel model                   = null;

	private Status status_current 			 = null;
	private Status status_old 				 = null;
	private List<StatusListenerEntry>  list  = null;


	public StatusManager(DataModel model) {
		this.model = model;
		this.status_current = new Status();
		this.status_old     = new Status();
		this.list  = new ArrayList<StatusListenerEntry>();
		ExecutorService.get().scheduleAtFixedRate(this, 2000, 10, TimeUnit.MILLISECONDS);
		System.out.println("StatusManager started");
	}


	private void addListener(byte type, int mask, int timeout_ms, IMSPStatusChangedListener listener) {
		StatusListenerEntry entry = new StatusListenerEntry();
		entry.listener    = listener;
		entry.type        = type;
		entry.mask        = mask;
		entry.timeout_ms  = timeout_ms;
		list.add(entry);
	}

	public void addListener(int mask, int timeout_ms, IMSPStatusChangedListener listener) {
		addListener(TYPE_TIMEOUT, mask, timeout_ms, listener);
	}

	public void addListener(int mask, IMSPStatusChangedListener listener) {
		addListener(TYPE_CHANGE, mask, 0, listener);
	}

	public void addListener(IMSPStatusChangedListener listener) {
		addListener(TYPE_CHANGE, MASK_ALL, 0, listener);
	}

	public void removeAll() {
		list.clear();
	}

	@Override
	public void run() {

		if(model.sys.isEqual(status_old))
			return;

		status_current.set(model.sys);
		try {
			for (StatusListenerEntry entry : list) {
				switch(entry.type) {
				case TYPE_CHANGE:
					if(entry.mask == MASK_ALL) {
						entry.listener.update(status_old, status_current);
					}
					entry.last_triggered = System.currentTimeMillis();
				break;
				case TYPE_TIMEOUT:

				break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		status_old.set(status_current);

	}

	private class StatusListenerEntry {

		public IMSPStatusChangedListener	listener = null;
		public int							mask     = MASK_ALL;
		public byte                         type     = TYPE_CHANGE;
		public long                   last_triggered = 0;
		public int                        timeout_ms = 0;
	}
}
