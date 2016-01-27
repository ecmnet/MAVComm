package com.comino.mav.control.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.comino.mav.control.IMAVController;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.collector.ModelCollectorService;
import com.comino.msp.model.segment.Status;
import com.comino.msp.utils.ExecutorService;

public class MAVSimController extends MAVController implements IMAVController {
	
	DataModel model = null;
	

	public MAVSimController() {
		System.out.println("Simulation Controller loaded");
		model = new DataModel();
		collector = new ModelCollectorService(model);
		
		ExecutorService.get().scheduleAtFixedRate(new Simulation(), 0, 20, TimeUnit.MILLISECONDS);
	}

	@Override
	public boolean connect() {
		return true;
	}

	

	@Override
	public DataModel getCurrentModel() {
		return model;
	}

	@Override
	public List<DataModel> getModelList() {
		return collector.getModelList();
	}

	
	private class Simulation implements Runnable {

		@Override
		public void run() {
			
			model.sys.setStatus(Status.MSP_CONNECTED, true);
			model.sys.setStatus(Status.MSP_READY, true);
			
			model.sys.setSensor(Status.MSP_IMU_AVAILABILITY, true);
			model.sys.setSensor(Status.MSP_LIDAR_AVAILABILITY, true);
			model.sys.setSensor(Status.MSP_PIX4FLOW_AVAILABILITY, true);
			
		}
		
	}
	



}
