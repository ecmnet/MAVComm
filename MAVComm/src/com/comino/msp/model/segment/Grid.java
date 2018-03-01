/****************************************************************************
 *
 *   Copyright (c) 2017-2018 Eike Mansfeld ecm@gmx.de. All rights reserved.
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

package com.comino.msp.model.segment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.comino.msp.model.segment.generic.Segment;

import georegression.struct.point.Point3D_F32;

public class Grid extends Segment {

	public static final float GRID_EXTENSION_M  = 20.0f;
	public static final float GRID_RESOLUTION_M = 0.05f;

	private static final long serialVersionUID = -77272456745165428L;

	// TODO: add blockcount => is 0 then refresh on MAVGCL side

	private int      dimension 		= 0;
	private int      resolution_cm 	= 0;
	private int      extension_cm    = 0;
	private int      max_length;
	private int      blocks_per_m    = 0;

	private static List<Integer>  transfer  = new ArrayList<Integer>();
	private static Map<Integer,Point3D_F32>   data  = new ConcurrentHashMap<Integer, Point3D_F32>(0);

	//	private  Map<Integer,BlockPoint2D> data = null;

	public int      count;
	public byte    status;

	private int      cx,cy, cz;
	private int      vx,vy, vz;

	public Grid() {
		this(GRID_EXTENSION_M, GRID_RESOLUTION_M);
	}


	public Grid(float extension_m, float resolution_m) {
		//		this.data  = new ConcurrentHashMap<Integer, BlockPoint2D>(0);
		this.count    = 0;
		this.status   = 0;
		this.extension_cm = (int)(extension_m) * 100 * 2;
		this.dimension = (int)(extension_m/resolution_m)*2;
		this.resolution_cm = (int)(resolution_m*100f);
		this.blocks_per_m = 100/resolution_cm;
		this.cx = dimension / 2;
		this.cy = dimension / 2;
		this.cz = dimension / 2;
		this.max_length = dimension * dimension * dimension;
	}

	public void set(Grid a) {

	}

	public Grid clone() {

		return this;
	}

	public void clear() {
		data.clear();
	}

	// Transfer via block only. positive values => set block; negative => remove block

	public boolean toArray(long[] array) {
		if(transfer==null || array == null)
			return false;
		count = data.size();
		Arrays.fill(array, 0);
		if(transfer.isEmpty() || array == null)
			return false;
		synchronized(this) {
			for(int i=0; i< array.length && transfer.size() > 0;i++) {
				array[i] = transfer.remove(0);
			}
		}
		return true;
	}

	public boolean hasTransfers() {
		return transfer!=null && !transfer.isEmpty();
	}

	@SuppressWarnings("unlikely-arg-type")
	public void fromArray(long[] array) {
		for(int i=0; i< array.length;i++) {
			if(data.containsKey(array[i]))
				return;
			if(array[i]>0) {
				data.put((int)array[i],new Point3D_F32(
						((int)(array[i] % dimension)-cx)*resolution_cm/100f,
						((int)((array[i] / dimension) % dimension)-cy)*resolution_cm/100f,
						((int)(array[i] / (dimension* dimension))-cz)*resolution_cm/100f
						));
			}
			if(array[i]<0)
				data.remove(-(int)array[i]);
		}
	}

	public void invalidateTransfer() {
		transfer.clear();
		data.forEach((i,e) -> {
			transfer.add(i);
		});
		count = -1;
	}

	public void translate(float dx, float dy, float dz) {

		List<Point3D_F32> tmp = copy();
		if(tmp != null) {
			data.clear();

			if(tmp.size()>0) {
				tmp.forEach((p) -> {
					setBlock(p.x+dx,p.y+dy, p.z+dz);
				});
				invalidateTransfer();
			}
		}
		setIndicator(getIndicatorX()+dx, getIndicatorY()+dy, getIndicatorZ()+dz);
	}

	public void setProperties(float extension_m, float resolution_m) {

		if(extension_m == 0 || resolution_m == 0)
			return;

		if((int)(extension_m/resolution_m)*2 == this.dimension
				&& (int)(resolution_m*100f) == this.resolution_cm)
			return;

		this.dimension = (int)(extension_m/resolution_m)*2;
		this.resolution_cm = (int)(resolution_m*100f);
		this.cx = dimension / 2;
		this.cy = dimension / 2;
		this.max_length = dimension * dimension;

		List<Point3D_F32> tmp = copy();
		if(tmp != null) {
			data.clear();

			if(tmp.size()>0) {
				tmp.forEach((p) -> {
					setBlock(p.x,p.y,p.z);
				});
				invalidateTransfer();
			}
		}
	}

	public void setIndicator(double vx, double vy, double vz) {
		this.vx = (int)Math.round((vx) * blocks_per_m)+cx;
		this.vy = (int)Math.round((vy) * blocks_per_m)+cy;
		this.vz = (int)Math.round((vz) * blocks_per_m)+cz;
	}

	public boolean setBlock(double xpos, double ypos, double zpos) {
		return setBlock(xpos,ypos,zpos,true);
	}

	public boolean  setBlock(double xpos, double ypos, double zpos, boolean set) {
		int block = calculateBlock(xpos, ypos, zpos);
		if(block< 0 || block > max_length)
			return false;

		if(set) {
			if(!data.containsKey(block)) {
				Point3D_F32 p = new Point3D_F32((float)Math.round((float)xpos * blocks_per_m)/blocks_per_m,
	                                            (float)Math.round((float)ypos * blocks_per_m)/blocks_per_m,
	                                            (float)Math.round((float)zpos * blocks_per_m)/blocks_per_m);
				data.put(block,p);
				transfer.add(block);
			}
		}
		else {
			if(data.containsKey(block)) {
				transfer.add(-block);
				data.remove(block);
			}
		}

		count = data.size();
		return true;
	}

	public boolean  setBlock(int block, boolean set) {
		if(block< 0 || block > max_length)
			return false;

		if(set) {
			if(!data.containsKey(block)) {
				data.put(block,new Point3D_F32(
						((int)(block % dimension)-cx)*resolution_cm/100f,
						((int)((block / dimension) % dimension)-cy)*resolution_cm/100f,
						((int)(block / (dimension * dimension))-cz)*resolution_cm/100f
						));
				transfer.add(block);
			}
		}
		else {
			if(data.containsKey(block)) {
				transfer.add(-block);
				data.remove(block);
			}
		}
		count = data.size();

		return true;
	}

	public boolean isBlocked(double xpos, double ypos, double zpos) {
		return data.containsKey(calculateBlock(xpos, ypos, zpos));
	}

	public boolean hasBlocked() {
		return !data.isEmpty();
	}

	public Map<Integer, Point3D_F32> getData() {
		return data;
	}

	public float getResolution() {
		return resolution_cm / 100f;
	}

	public float getExtension() {
		return extension_cm / 100f;
	}

	public float getIndicatorX() {
		return (float)(vx-cx)*resolution_cm/100f;
	}

	public float getIndicatorY() {
		return (float)(vy-cy)*resolution_cm/100f;
	}

	public float getIndicatorZ() {
		return (float)(vz-cz)*resolution_cm/100f;
	}


	private int calculateBlock(double xpos, double ypos, double zpos) {
		int blockx  =  (int)Math.round(xpos * blocks_per_m) + cx;
		if(blockx > dimension-1)
			blockx = dimension -1;
		if(blockx < 0)
			blockx = 0;
		int blocky = (int)Math.round(ypos * blocks_per_m ) + cy;
		if(blocky > dimension-1)
			blocky = dimension -1;
		if(blocky < 0)
			blocky = 0;
		int blockz = (int)Math.round(zpos * blocks_per_m ) + cy;
		if(blockz > dimension-1)
			blockz = dimension -1;
		if(blockz < 0)
			blockz = 0;
		return blockx + blocky * dimension + blockz * dimension * dimension;
	}



	public String toString() {
		StringBuilder b = new StringBuilder();
		for(int r= 0; r < dimension; r++) {
			for(int c=0; c < dimension; c++) {
				if(r==cy && c==cx) {
					b.append("o");
					continue;
				}
				if(r==vy && c==vx) {
					b.append("+");
					continue;
				}
				if(isBlocked((c-cx)*resolution_cm/100f,(r-cy)*resolution_cm/100f,0 )) {
					b.append("X");
					//					System.out.println((c-cx)*resolution_cm/100f);
				}
				else
					b.append(".");
			}
			b.append("\n");
		}
		b.append("\n");
		return b.toString();
	}

	private List<Point3D_F32> copy() {
		List<Point3D_F32> tmp = new ArrayList<Point3D_F32>();

		if(!data.isEmpty()) {
			data.forEach((i,p) -> {
				tmp.add(new Point3D_F32(p.x,p.y, p.z));
			});
			return tmp;
		}
		return null;
	}


	public static void main(String[] args) {

		long[] transfer = new long[50];

		Grid s = new Grid(10,0.05f);

		s.setBlock(1.77, 0.0, 1);
		s.setBlock(0.0, 1.0, 1);
		s.setBlock(1.0, 1.0, 1);

		s.getData().entrySet().forEach((e) -> {
			System.out.println(e.getKey()+":"+e.getValue());

		});



		System.out.println();

		System.out.println(s);

		Grid t = new Grid(2,0.10f);

		s.toArray(transfer);


		t.fromArray(transfer);

		System.out.println(t);

		t.translate(1, 1, 1);

		System.out.println(t);

		System.out.println(s);

	}

}
