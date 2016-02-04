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

