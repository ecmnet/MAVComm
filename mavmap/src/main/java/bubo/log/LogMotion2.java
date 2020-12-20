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

package bubo.log;

import georegression.struct.se.Se2_F64;
import org.ddogleg.struct.FastQueue;

import java.util.List;

/**
 * Logs rigid body motion in 2D
 *
 * @author Peter Abeles
 */
public class LogMotion2 {

	protected FastQueue<Element> history = new FastQueue<Element>(Element.class,true);

	public void reset() {
		history.reset();
	}

	public void add( double time , Se2_F64 motion ) {
		history.grow().set(motion,time);
	}

	public List<Element> getHistory() {
		return history.toList();
	}

	public static class Element
	{
		public Se2_F64 motion = new Se2_F64();
		public double time;

		public void set(Se2_F64 motion, double time) {
			this.motion.set(motion);
			this.time = time;
		}
	}
}
