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

package com.comino.msp.model.segment;

import com.comino.msp.model.segment.generic.Segment;

public class LogMessage extends Segment {

	private static final long serialVersionUID = 3345013931542810501L;


	public String     msg = null;
	public int   severity = 0;

	public LogMessage() {
		this.tms = System.currentTimeMillis();
	}

	public LogMessage(String msg, int severity) {
		this.msg = msg;
		this.severity = severity;
		this.tms = System.currentTimeMillis();
	}

}
