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
import georegression.struct.se.SpecialEuclidean;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * A sequence of coordinate system transforms from one {@link bubo.desc.RobotComponent} to another.
 * Also contains helper functions for computing the path from RobotComponents.
 * </p>
 *
 * @author Peter Abeles
 */
public class RefFramePath {

	// used to detect bad graphs with cycles
	public static final int MAX_ITERATIONS = 100000;

	// the path
	private List<Node> path;

	public RefFramePath(List<Node> path) {
		this.path = path;
	}

	/**
	 * Find the coordinate frame path between these two nodes.
	 *
	 * @param from Where the path will begin.
	 * @param to   Where the path will end.
	 * @return The path.
	 */
	public static RefFramePath findPath(RobotComponent from, RobotComponent to) {
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

		return new RefFramePath(listFrom);
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

	public List<Node> getPath() {
		return path;
	}

	public void setPath(List<Node> path) {
		this.path = path;
	}

	@SuppressWarnings({"unchecked"})
	public void computeTransform(SpecialEuclidean result) {

		if (path.size() == 0)
			return;

		SpecialEuclidean inv = (SpecialEuclidean) result.createInstance();

		RefFramePath.Node n = path.get(0);
		SpecialEuclidean nodeTran = ((Extrinsic2D) n.comp.getExtrinsic()).getTransformToParent();

		if (n.forward) {
			result.set(nodeTran);
		} else {
			nodeTran.invert(result);
		}

		for (int i = 1; i < path.size(); i++) {
			n = path.get(i);
			nodeTran = ((Extrinsic2D) n.comp.getExtrinsic()).getTransformToParent();

			if (n.forward) {
				result.concat(nodeTran, result);
			} else {
				nodeTran.invert(inv);
				result.concat(inv, result);
			}
		}
	}

	public static class Node {
		public RobotComponent comp;
		public boolean forward;

		public Node(RobotComponent comp, boolean forward) {
			this.comp = comp;
			this.forward = forward;
		}
	}
}
