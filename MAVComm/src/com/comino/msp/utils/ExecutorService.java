package com.comino.msp.utils;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorService {
	
	
	private static final int MAXTHREADS = 35;	
	 
	private static ScheduledThreadPoolExecutor schedThPoolExec =  
			new ScheduledThreadPoolExecutor(MAXTHREADS);
	
	{
	
		schedThPoolExec.prestartAllCoreThreads();
		schedThPoolExec.setKeepAliveTime(0, TimeUnit.SECONDS);
		
	}
	
	public static ScheduledThreadPoolExecutor get() {
		return schedThPoolExec;
	}
	
	public static int getActiveCount() {
		return (int)schedThPoolExec.getActiveCount();
	}
	
	
	public static void shutdown() {
		schedThPoolExec.shutdownNow();
	}
	

	

}

