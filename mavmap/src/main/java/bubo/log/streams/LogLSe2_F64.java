/*
 * Copyright (c) 2013-2014, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Project BUBO.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bubo.log.streams;

import georegression.struct.se.Se2_F64;

/**
 * Se2 with a time stamp
 *
 * @author Peter Abeles
 */
public class LogLSe2_F64 extends Se2_F64 {
	public long time;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
