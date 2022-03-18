/****************************************************************************
 *
 *   Copyright (c) 2021 Eike Mansfeld ecm@gmx.de. All rights reserved.
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

package com.comino.mavmap.map.map3D.impl.octree;

import java.util.Iterator;

import com.comino.mavmap.map.map3D.Map3DSpacialInfo;
import com.comino.mavmap.utils.UtilPoint3D_I32;

import bubo.maps.d3.grid.CellProbability_F64;
import bubo.maps.d3.grid.OccupancyGrid3D_F64;
import bubo.maps.d3.grid.impl.OctreeGridMap_F64;
import georegression.geometry.UtilPoint3D_F64;
import georegression.struct.GeoTuple3D_F32;
import georegression.struct.GeoTuple3D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Point3D_I32;
import georegression.struct.point.Vector3D_F64;
import georegression.struct.se.Se3_F64;
import georegression.transform.se.InterpolateLinearSe3_F64;


/**
 * 3D map implementation based on octree representation
 * @author ecmnet
 */
public class LocalMap3D {

	private static final int MAX_SIZE = 100000;


	private OccupancyGrid3D_F64    map;
	private final Map3DSpacialInfo       info;

	private InterpolateLinearSe3_F64     interp = new InterpolateLinearSe3_F64();
	private Se3_F64                      tmp    = new Se3_F64();
	private Se3_F64                      ptm    = new Se3_F64();
	private Point3D_I32                  mapp   = new Point3D_I32();
	private Point3D_I32                  mapo   = new Point3D_I32();

	private Point3D_F64              indicator  = new Point3D_F64();

	private boolean                      forget = false;          

	public LocalMap3D() {
		this(new Map3DSpacialInfo(0.10f,20.0f,20.0f,5.0f),true);
	}

	public LocalMap3D(Map3DSpacialInfo info,boolean forget) {
		// TODO: Make it customizable and transfer to MACGCL
		this.info = info;
		this.map  = new OctreeGridMap_F64(info.getDimension().x,info.getDimension().y,info.getDimension().z); 
		this.forget = forget;
	}


	public void update(GeoTuple3D_F32<?> p ) {
		tmp.reset();
		ptm.reset(); ptm.T.setTo(p.x, p.y, p.z);
		update(tmp,ptm,1);	
	}

	public void update(GeoTuple3D_F64<?> p, GeoTuple3D_F64<?> o ) {
		tmp.reset(); tmp.T.setTo(p.x,p.y,p.z);
		ptm.reset(); ptm.T.setTo(o.x,o.y,o.z);
		update(tmp,ptm,1);	
	}
	
	public void update(GeoTuple3D_F64<?> p, GeoTuple3D_F64<?> o, double prob) {
		tmp.reset(); tmp.T.setTo(p.x,p.y,p.z);
		ptm.reset(); ptm.T.setTo(o.x,o.y,o.z);
		update(tmp,ptm,prob);	
	}

	/**
	 * Updates map with an observation using raycasting from observation to observer
	 * @param pos_ned 			observer pos in world coordinates
	 * @param observation_ned   observation pos in world coordinates
	 */
	public void update(Se3_F64 pos_ned, Se3_F64 observation_ned, double probability) {

		if(map.size()>MAX_SIZE)
			return;

		info.globalToMap(observation_ned.T, mapo);
		if(!map.isInBounds(mapo.x, mapo.y, mapo.z)) 
			return;
		
		

		mapp.setTo(mapo);

		interp.setTransforms(observation_ned, pos_ned);

		double steps = UtilPoint3D_F64.distance(pos_ned.T.x, pos_ned.T.y, pos_ned.T.z, 
				observation_ned.T.x, observation_ned.T.y, observation_ned.T.z) / info.getCellSize()+1;

		for(int i=1; i <= steps; i++ ) {
			interp.interpolate(i / steps , tmp);
			info.globalToMap(tmp.T, mapp);
			if(map.get(mapp.x, mapp.y, mapp.z) > 0.5) {
				map.set(mapp.x, mapp.y, mapp.z, 0.5);
			}
		} 
		map.set(mapo.x, mapo.y, mapo.z, probability);
	}

	public void setMapPoint(int h, double probability) {
		info.decodeMapPoint(h, mapo);
		setMapPoint(mapo,probability);
	}

	/**
	 * Sets a map point. 
	 * @param p
	 * @param probability
	 */

	public void setMapPoint(Point3D_I32 p, double probability) {
		if(probability < 0.5f) {
			if(map.get(p.x, p.y, p.z) != 0.5f)
				map.set(p.x, p.y, p.z, probability);
		}
		else if(probability > 0.5f)
			map.set(p.x, p.y, p.z, probability);
	}

	/**
	 * @return	Iterator of all known positions
	 */
	public Iterator<CellProbability_F64> getMapItems() {
		return map.iterator();
	}

	/**
	 * Find closest occupied cell in the map
	 */

	public boolean findClosestPoint(Point3D_I32 p, Point3D_I32 closest) {

		if(closest == null)
			return false;

		float minDist = Float.MAX_VALUE;
		Iterator<CellProbability_F64> i = map.iterator();
		while(i.hasNext()) {
			CellProbability_F64 pr = i.next();
			if(pr.probability <= 0.5)
				continue;
			float d = UtilPoint3D_I32.distanceSq(pr, p);
			if( d < minDist) {
				minDist = d;
				closest.setTo(pr);
			}	
		}
		return minDist < Float.MAX_VALUE;
	}

	/**
	 * Returns the indicator position; NaN if not visible
	 * @return
	 */

	public Point3D_F64 getIndicator() {
		return indicator;
	}

	/**
	 *  Sets the indicator to a position in 3D space. Set NaN to hide it.
	 */

	public void setIndicator(float x, float y, float z) {
		indicator.setTo(x,y,z);
	}

	/**
	 * Returns an Interator over known cells that were changed shortly
	 * @param since
	 * @return Iterator
	 */

	public Iterator<CellProbability_F64> getLatestMapItems(long since) {
		return map.iterator(since);
	}


	/**
	 * Returns an Interator over known cells that are within a given Z range
	 * @param Z range in m
	 * @return Iterator
	 */
	public Iterator<CellProbability_F64> getMapLevelItems(float from, float to) {
		Comparable<Integer> zfilter = new ZFilter(from,to);
		return map.iterator(zfilter);
	}

	/**
	 * @return	Map information
	 */
	public Map3DSpacialInfo getMapInfo() {
		return info;
	}

	/**
	 * Clear Map and remove indicator
	 */
	public void clear() {
		this.map.clear();
		setIndicator(Float.NaN, Float.NaN, Float.NaN);
	}

	/**
	 * 
	 * @return size of map nodes
	 */
	public int size() {
		return map.size();
	}


	private class ZFilter implements Comparable<Integer> {

		int from;
		int to;

		public ZFilter(float from,float to) {

			this.from = (int)(from * info.getBlocksPerM());
			this.to   = (int)(to   * info.getBlocksPerM());
		}

		@Override
		public int compareTo(Integer z) {
			if( from < z &&  to > z) return 0; else return 1;

		}

	}

	public static void main(String[] args) {

		LocalMap3D map = new LocalMap3D();

		Map3DSpacialInfo info = map.getMapInfo();

		Point3D_I32 mp   = new Point3D_I32();
		Point3D_I32 mpt  = new Point3D_I32();


		Vector3D_F64 p  = new Vector3D_F64(0.055,-0.055,1);
		Vector3D_F64 pt = new Vector3D_F64();
		Vector3D_F64 ph = new Vector3D_F64();

		System.out.println(p);

		info.globalToMap(p,mp);
		System.out.println(mp);

		long h = info.encodeMapPoint(mp,0.75);
		System.out.println(h);
		System.out.println(info.decodeMapPoint(h, mpt));
		System.out.println(mpt);

		info.mapToGlobal(mp, pt);
		info.mapToGlobal(mpt, ph);

		System.out.println(pt);
		System.out.println(ph);



	}

}
