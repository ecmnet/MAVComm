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


package com.comino.msp.utils;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorService {

	public static final int LOW  = 0;
	public static final int HIGH = 1;


	private static ScheduledThreadPoolExecutor low_pool  = null;
	private static ScheduledThreadPoolExecutor high_pool = null;

	public static void create() {
		low_pool  = new ScheduledThreadPoolExecutor(5);
		low_pool.setThreadFactory(new DaemonThreadFactory(Thread.NORM_PRIORITY-2,"LowPool"));
		low_pool.allowCoreThreadTimeOut(false);
		low_pool.setRemoveOnCancelPolicy(true);
		low_pool.prestartAllCoreThreads();

		high_pool  = new ScheduledThreadPoolExecutor(3);
		high_pool.setThreadFactory(new DaemonThreadFactory(Thread.NORM_PRIORITY+2,"HighPool"));
		high_pool.allowCoreThreadTimeOut(false);
		high_pool.setRemoveOnCancelPolicy(true);
		high_pool.prestartAllCoreThreads();
		System.out.println("("+low_pool.getCorePoolSize()+","+high_pool.getCorePoolSize()+") threads started");

	}

	public static ScheduledThreadPoolExecutor get() {
		return low_pool;
	}

	public static int getActiveCount() {
		return (int)(low_pool.getActiveCount()+high_pool.getActiveCount());
	}

	public static Future<?> submit(Runnable r) {
		return low_pool.schedule(r, 0, TimeUnit.MILLISECONDS);
	}

	public static Future<?> submit(Runnable r, int priority) {
		switch(priority) {
		case LOW:
			return low_pool.schedule(r, 0, TimeUnit.MILLISECONDS);
		case HIGH:
			return high_pool.schedule(r, 0, TimeUnit.MILLISECONDS);
		default:
			return null;
		}
	}

	public static Future<?> submit(Runnable r, int priority, int period_ms) {
		switch(priority) {
		case LOW:
			return low_pool.scheduleAtFixedRate(r, 0, period_ms, TimeUnit.MILLISECONDS);
		case HIGH:
			return high_pool.scheduleAtFixedRate(r, 0, period_ms, TimeUnit.MILLISECONDS);
		default:
			return null;
		}
	}

	public static void shutdown() {
		low_pool.shutdownNow();
		high_pool.shutdownNow();
	}

	public static class DaemonThreadFactory implements ThreadFactory {

		private int priority;
		private String poolName;

		private final AtomicInteger threadNumber = new AtomicInteger(1);

		public DaemonThreadFactory(int priority, String poolName) {
			this.priority = priority;
			this.poolName = poolName;
		}

		@Override
		public Thread newThread(Runnable paramRunnable) {
			Thread t = new Thread(paramRunnable, poolName+" "+threadNumber.getAndIncrement());
			t.setPriority(priority);
			t.setDaemon(true);
			return t;
		}
	}

}

