package com.comino.msp.model.collector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.locks.LockSupport;

import com.comino.msp.model.DataModel;

public class ModelCollectorService {

	private static final int MODELCOLLECTOR_INTERVAL_US = 50000;

	private DataModel				    current     = null;
	private ArrayList<DataModel> 		modelList   = null;
	private Future<?>          			service     = null;

	private boolean isRunning = false;

	public ModelCollectorService(DataModel current) {
		this.modelList = new ArrayList<DataModel>();

		this.current = current;
	}


	public List<DataModel> getModelList() {
		return modelList;
	}



	public boolean start() {
		if(!isRunning) {
			modelList.clear();
			isRunning = true;
			new Thread(new Collector()).start();
		}
		return isRunning;
		//service = ExecutorService.get().scheduleAtFixedRate(new Collector(), 0, MODELCOLLECTOR_INTERVAL_US, TimeUnit.MICROSECONDS);
	}


	public boolean stop() {
		isRunning = false;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {

		}
		return false;
	}


	public boolean isCollecting() {
		return isRunning;
	}


	private class Collector implements Runnable {

		private long  				last_model_tms_us = 0;

		@Override
		public void run() {

			while(isRunning) {

			//	if(current.attitude.tms > last_model_tms_us) {
					last_model_tms_us = current.attitude.tms;
					modelList.add(current.clone());
			//	}
				LockSupport.parkNanos(MODELCOLLECTOR_INTERVAL_US*1000);
			}

		}

	}

}
