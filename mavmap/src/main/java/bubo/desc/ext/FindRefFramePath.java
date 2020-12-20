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

package bubo.desc.ext;

import bubo.desc.ExtrinsicParameters;
import bubo.desc.RobotComponent;
import georegression.transform.InvertibleTransformSequence;

import java.util.ArrayList;
import java.util.List;


/**
 * Computes the transform between two
 *
 * @author Peter Abeles
 */
public class FindRefFramePath {
	// used to detect bad graphs with cycles
	public static final int MAX_ITERATIONS = 100000;

	/**
	 * Find the coordinate frame path between these two nodes.
	 *
	 * @param from Where the path will begin.
	 * @param to   Where the path will end.
	 * @return The path.
	 */
	public static InvertibleTransformSequence findPath(RobotComponent from, RobotComponent to) {
		List<Node> listFrom = findPathToRoot(from, true);
		List<Node> listTo = findPathToRoot(to, false);

		// find the nodes at the end which are common and remove them
		if (listFrom.size() > 0 && listTo.size() > 0) {
			// quick sanity check
			// the last elements should be the final node
			if (listFrom.get(listFrom.size() - 1).comp != listTo.get(listTo.size() - 1).comp)
				throw new RuntimeException("Bad graph.  Last node is not the same in both lists.");

			while (listFrom.size() > 0 && listTo.size() > 0) {
				Node f = listFrom.remove(listFrom.size() - 1);
				Node t = listTo.remove(listTo.size() - 1);

				if (f.comp != t.comp) {
					listFrom.add(f);
					listTo.add(t);
					break;
				}
			}
		}

		for (int i = listTo.size() - 1; i >= 0; i--) {
			listFrom.add(listTo.get(i));
		}

		// convert it into a reference frame path
		InvertibleTransformSequence ret = new InvertibleTransformSequence();
		for (Node n : listFrom) {
			Extrinsic2D e = (Extrinsic2D) n.comp.getExtrinsic();
			ret.addTransform(n.forward, e.getTransformToParent());
		}

		return ret;
	}

	/**
	 * Traverses the graph until it finds the last node and return the sequence of nodes.
	 *
	 * @param start   First node in the search.
	 * @param forward is the transform in the forward or backwards direction.
	 * @return Path.
	 */
	public static List<Node> findPathToRoot(RobotComponent start, boolean forward) {
		List<Node> toRoot = new ArrayList<Node>();

		RobotComponent n = start;
		int iter = 0;

		while (n != null) {
			ExtrinsicParameters p = n.getExtrinsic();
			if (p == null)
				throw new RuntimeException("No extrinsic parameters in node " + n.getName());
			toRoot.add(new Node(n, forward));
			n = p.getReference();
			if (++iter > MAX_ITERATIONS)
				throw new RuntimeException("Max iterations exceeded.  Cycle in the graph?");
		}

		return toRoot;
	}

	protected static class Node {
		boolean forward;
		RobotComponent comp;

		private Node(RobotComponent comp, boolean forward) {
			this.forward = forward;
			this.comp = comp;
		}

		private Node() {
		}
	}
}
