/**
* This file is ported to Java from Ewok C++ implementation
*
* Copyright 2017 Vladyslav Usenko, Technical University of Munich.
* Developed by Vladyslav Usenko <vlad dot usenko at tum dot de>,
* for more information see <http://vision.in.tum.de/research/robotvision/replanning>.
* If you use this code, please cite the respective publications as
* listed on the above website.
*
* Ewok is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Ewok is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with Ewok. If not, see <http://www.gnu.org/licenses/>.
*/

package com.comino.mavmap.map.map3D.impl.circular;

import java.util.Arrays;

import com.comino.mavmap.map.map3D.Map3DSpacialInfo;
import com.comino.mavmap.struct.Point3D_I;

import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Vector3D_F32;

public class RingBufferBase {

	protected final Point3D_I         offset = new Point3D_I();
	protected final Point3D_I         idx    = new Point3D_I();

	protected final Map3DSpacialInfo  info;

	protected final int     N;   
	protected final int     N_2;

	protected final int     MASK;
	protected final int NEG_MASK;

	protected byte   empty_element = 0;
	
	protected final byte[] buffer;


	public RingBufferBase(Map3DSpacialInfo info) {
		this.info     = info;
		this.N        = (1 << (int)Math.ceil(Math.log(info.getDimension().x) / Math.log(2)));
		this.N_2      = N / 2;
		this.MASK     = N - 1;
		this.NEG_MASK = ~MASK;
		this.buffer   = new byte[N * N * N];
		System.out.println("N is "+N);
	}
	
	public void setEmptyElement(byte e) {
		this.empty_element = e;
		Arrays.fill(buffer, e);
	}

	public void setOffset(Point3D_I p) {
		this.offset.setTo(p);
	}

	public Point3D_I getOffset() {
		return this.offset;
	}

	public void moveVolume(Point3D_I direction) {
		for (int axis = 0; axis < 3; axis++) {
			if (direction.get(axis) != 0) {
				int slice;
				if (direction.get(axis) > 0) {
					offset.add(axis,1);
					slice = offset.get(axis) + N - 1;
				} else {
					offset.add(axis,-1);
					slice = offset.get(axis);
				}

				switch (axis) {
				case 0:setXSlice(slice, empty_element);
				break;
				case 1:setYSlice(slice, empty_element);
				break;
				case 2:setZSlice(slice, empty_element);
				break;

				}
			}
		}
	}

	private void setXSlice(int slice_idx, byte data) {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				idx.setTo(slice_idx, i, j);
				this.at(idx,data);
			}
		}
	}

	private void setYSlice(int slice_idx, byte data) {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				idx.setTo(i, slice_idx, j);
				this.at(idx,data);
			}
		}
	}

	private void setZSlice(int slice_idx, byte data) {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				idx.setTo(i, j, slice_idx);
				this.at(idx, data);
			}
		}
	}

	public  void getIdx(Vector3D_F32 point, Point3D_I idx)  {
		idx.setX((int)(point.x / info.getCellSize()));
		idx.setY((int)(point.y / info.getCellSize()));
		idx.setZ((int)(point.z / info.getCellSize()));
	}

	public void getPoint(Point3D_I idx, Vector3D_F32 point)  {
		point.setX((idx.x+0.5f)*info.getCellSize());
		point.setY((idx.y+0.5f)*info.getCellSize());
		point.setZ((idx.z+0.5f)*info.getCellSize());
	}

	public void at(Point3D_I coord, byte data) {	
		idx.setTo(coord.x & MASK, coord.y & MASK, coord.z & MASK);
		buffer[ N * N * idx.x + N * idx.y + idx.z] = data;	
	}
	
	public void at_add(Point3D_I coord, byte data) {	
		idx.setTo(coord.x & MASK, coord.y & MASK, coord.z & MASK);
		int i =  N * N * idx.x + N * idx.y + idx.z;
		int d = buffer[i] + data;
		if(d < -127) d = -127; if(d > 127) d = 127;
		buffer[i] += (byte)d;	
	}
	
	public void at_and(Point3D_I coord, byte data) {	
		idx.setTo(coord.x & MASK, coord.y & MASK, coord.z & MASK);
		buffer[ N * N * idx.x + N * idx.y + idx.z] &= data;	
	}
	
	public void at_or(Point3D_I coord, byte data) {	
		idx.setTo(coord.x & MASK, coord.y & MASK, coord.z & MASK);
		buffer[ N * N * idx.x + N * idx.y + idx.z] |= data;	
	}

	public byte at(Point3D_I coord) {	
		idx.setTo(coord.x & MASK, coord.y & MASK, coord.z & MASK);
		return buffer[ N * N * idx.x + N * idx.y + idx.z];	
	}

	public void getVolumeCenter(Point3D_I center) {
		center.setTo(N_2, N_2, N_2);
		center.add(offset);
	}
	
	public boolean insideVolume(Point3D_I coord) {
	      boolean res = true;
	      res &=  (((coord.x - offset.x) & NEG_MASK) == 0);
	      res &=  (((coord.y - offset.y) & NEG_MASK) == 0);
	      res &=  (((coord.z - offset.z) & NEG_MASK) == 0);      
	      return res;
	  }
}
