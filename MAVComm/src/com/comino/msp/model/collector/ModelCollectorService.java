/*
 * Copyright (c) 2016 by E.Mansfeld
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.comino.msp.model.collector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import com.comino.msp.model.DataModel;
import com.comino.msp.utils.ExecutorService;

public class ModelCollectorService {

	public static  final int STOPPED		 	= 0;
	public static  final int PRE_COLLECTING 	= 1;
	public static  final int COLLECTING     	= 2;
	public static  final int POST_COLLECTING    = 3;


	private static final int MAX_SIZE = 120000;
	private static final int MODELCOLLECTOR_INTERVAL_US = 50000;

	private DataModel				    current     = null;
	private ArrayList<DataModel> 		modelList   = null;
	private Future<?>          			service     = null;

	private List<IInjectValueListener>   debugListener = null;

	private int     mode = 0;

	private float ned_offset_x =0;
	private float ned_offset_y =0;


	public ModelCollectorService(DataModel current) {
		this.modelList     = new ArrayList<DataModel>();
		this.current = current;
		this.debugListener = new ArrayList<IInjectValueListener>();
	}

	public void addInjectionListener(IInjectValueListener listener) {
		debugListener.add(listener);
	}


	public List<DataModel> getModelList() {
		return modelList;
	}


	public int getCollectorInterval_ms() {
		return MODELCOLLECTOR_INTERVAL_US/1000;
	}

	public boolean start() {

		if(mode==PRE_COLLECTING) {
			mode = COLLECTING;
			return true;
		}
		if(mode==STOPPED) {
			modelList.clear();
			mode = COLLECTING;

			ned_offset_x = current.state.x;
			ned_offset_y = current.state.y;

			new Thread(new Collector(0)).start();
		}
		return mode != STOPPED;
		//service = ExecutorService.get().scheduleAtFixedRate(new Collector(), 0, MODELCOLLECTOR_INTERVAL_US, TimeUnit.MICROSECONDS);
	}


	public boolean stop() {
		mode = STOPPED;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {

		}
		return false;
	}

	public void stop(int delay_sec) {
		mode = POST_COLLECTING;
		ExecutorService.get().schedule(new Runnable() {
			@Override
			public void run() {
				mode = STOPPED;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			}
		}, delay_sec, TimeUnit.SECONDS);
	}

	public void setModelList(List<DataModel> list) {
		mode = STOPPED;
		modelList.clear();
		modelList.addAll(list);
	}

	public void clearModelList() {
		mode = STOPPED;
		modelList.clear();
	}

	public void setCurrentTo(float time_ms) {
		int index = (int)(time_ms * 1000f / MODELCOLLECTOR_INTERVAL_US);
		if(index > 0 && index < modelList.size())
			current.set(modelList.get(index));
	}

	public long getTotalRecordingTimeMS() {
		if(modelList.size()> 0)
			return (modelList.get(modelList.size()-1).tms) / 1000;
		else
			return 0;
	}

	public void start(int pre_sec) {
		if(mode==STOPPED) {
			modelList.clear();

			ned_offset_x = current.state.x;
			ned_offset_y = current.state.y;

			mode = PRE_COLLECTING;
			new Thread(new Collector(pre_sec)).start();
		}
	}


	public boolean isCollecting() {
		return mode != STOPPED ;
	}


	public int getMode() {
		return mode;
	}


	private class Collector implements Runnable {

		int pre_delay_count=0; int count = 0;

		public Collector(int pre_delay_sec) {
			if(pre_delay_sec>0) {
				mode = PRE_COLLECTING;
				this.pre_delay_count = pre_delay_sec * 1000000 / MODELCOLLECTOR_INTERVAL_US;
			}
		}

		@Override
		public void run() {
			long tms = System.nanoTime() / 1000;
			while(mode!=STOPPED) {
				current.tms = System.nanoTime() / 1000 - tms;
				DataModel model = current.clone();

				for(IInjectValueListener listener :  debugListener) {
					listener.addValue(model);
				}

				model.state.hx = ned_offset_x;
				model.state.hy = ned_offset_y;

				modelList.add(model);
				if(modelList.size()>MAX_SIZE)
					modelList.remove(0);

				count++;
				LockSupport.parkNanos(MODELCOLLECTOR_INTERVAL_US*1000);
				if(mode==PRE_COLLECTING) {
					int _delcount = count - pre_delay_count;
					if(_delcount > 0) {
						for(int i = 0; i < _delcount; i++ )
							modelList.remove(0);
					}

				}
			}
		}

	}

}
