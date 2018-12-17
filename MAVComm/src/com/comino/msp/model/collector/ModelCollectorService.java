/****************************************************************************
 *
 *   Copyright (c) 2017 Eike Mansfeld ecm@gmx.de. All rights reserved.
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

	private DataModel				    			current     = null;
	private ArrayList<DataModel> 		           modelList   = null;
	private Future<?>          						service     = null;

	private int     mode = 0;

	private  int  totalTime_sec = 30;


	public ModelCollectorService(DataModel current) {
		this.modelList     = new ArrayList<DataModel>();
		this.current = current;

	}


	public List<DataModel> getModelList() {
		return modelList;
	}


	public int getCollectorInterval_ms() {
		return MODELCOLLECTOR_INTERVAL_US/1000;
	}

	public boolean start() {

		current.tms = 0;

		if(mode==PRE_COLLECTING) {
			mode = COLLECTING;
			return true;
		}
		if(mode==STOPPED) {
			modelList.clear();
			mode = COLLECTING;


			Thread t = new Thread(new Collector(0));
			t.setName("Collector service");
			t.start();
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
		current.tms = 0;
		modelList.clear();
	}

	public void setTotalTimeSec(int totalTime) {
		this.totalTime_sec = totalTime;
	}

	public int getTotalTimeSec() {
		return totalTime_sec;
	}

	public int calculateX0Index(double factor) {
		int current_x0_pt = (int)(
				( modelList.size()
						- totalTime_sec *  1000f
						/ getCollectorInterval_ms())
				* factor);

		if(current_x0_pt<0)
			current_x0_pt = 0;

		return current_x0_pt;
	}

	public int calculateX1Index(double factor) {

		int current_x1_pt = calculateX0Index(factor) +
				(int)(totalTime_sec *  1000f
				/ getCollectorInterval_ms());

		if(current_x1_pt>modelList.size()-1)
		current_x1_pt = modelList.size()-1;

		return (int)(current_x1_pt);
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
				synchronized(this) {
					current.tms = System.nanoTime() / 1000 - tms;
					DataModel model = current.clone();
					current.msg.clear();

					modelList.add(model);
					if(modelList.size()>MAX_SIZE)
						modelList.remove(0);

					count++;
				}
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
