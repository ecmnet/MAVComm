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

import com.comino.mavmap.map.map3D.Map3DSpacialInfo;
import com.comino.mavmap.struct.Point3D_I;

import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Vector3D_F32;

public class RayCastRingBuffer {

	protected final RingBufferBase flag_buffer;
	protected final RingBufferBase occupancy_buffer;

	private static final byte occupied_flag   = (1 << 0);
	private static final byte free_flag       = (1 << 1);
	private static final byte free_ray_flag   = (1 << 2);
	private static final byte updated_flag    = (1 << 3);
	private static final byte insertion_flags = (occupied_flag | free_flag | free_ray_flag);

	private static double  hit  = 0.85;
	private static double  miss = -0.4;

	private final  int     N;

	private final Point3D_I updated_min = new Point3D_I();
	private final Point3D_I updated_max = new Point3D_I();

	private final Map3DSpacialInfo info;

	private static byte datatype_hit  = (byte)(hit  * 127 );
	private static byte datatype_miss = (byte)(miss * 127 );

	public RayCastRingBuffer(Map3DSpacialInfo info) {

		this.info = info;

		this.flag_buffer      = new  RingBufferBase(info);
		this.occupancy_buffer = new  RingBufferBase(info);

		this.N = flag_buffer.N;

		System.out.println("CircularBuffer size is "+flag_buffer.buffer.length/1024+" kByte");

		flag_buffer.setEmptyElement(updated_flag);
		clearUpdatedMinMax();

	}

	public boolean isOccupied(Point3D_I idx) {
		return occupancy_buffer.at(idx) >= datatype_hit;
	}

	public boolean isFree(Point3D_I idx) {
		return occupancy_buffer.at(idx) <= datatype_miss;
	}

	public boolean isUpdated(Point3D_I idx) {
		return (flag_buffer.at(idx) & updated_flag) == updated_flag;
	}

	public boolean clearUpdated(Point3D_I idx) {
		byte f = (byte)(flag_buffer.at(idx) & updated_flag);
		flag_buffer.at(idx, (byte)(f & ~updated_flag));
		return f == updated_flag;
	}

	public void getUpdatedMinMax(Point3D_I min, Point3D_I max) {
		min.set(updated_min);
		max.set(updated_max);
	}

	public void clearUpdatedMinMax() {
		updated_max.set(occupancy_buffer.getOffset());
		updated_min.set(updated_max.x + N  - 1,updated_max.y + N  - 1, updated_max.z + N  - 1 );
	}

	public void setOffset(Point3D_I off) {
		occupancy_buffer.setOffset(off);
		flag_buffer.setOffset(off);
	}

	public void moveVolume(Point3D_I direction) {
		occupancy_buffer.moveVolume(direction);
		flag_buffer.moveVolume(direction);

		Point3D_I offset = occupancy_buffer.getOffset();

		Point3D_I volume_min = new Point3D_I(offset);
		Point3D_I volume_max = new Point3D_I(offset.x + (N-1), offset.x + (N-1), offset.x + (N-1));



		for(int i=0; i<3; ++i) {

			Point3D_I min_point = new Point3D_I(volume_min);
			Point3D_I max_point = new Point3D_I(volume_max);

			if(direction.get(i) > 0) {

				min_point.set(i,volume_max.get(i));

				updated_min.min(min_point);
				updated_max.max(min_point);

				updated_min.min(max_point);
				updated_max.max(max_point);

			} else {

				min_point.set(i,volume_min.get(i));

				updated_min.min(min_point);
				updated_max.max(min_point);

				updated_min.min(max_point);
				updated_max.max(max_point);

			}
		}
	}

	public void addHit(Point3D_I idx) {
		occupancy_buffer.at_add(idx, datatype_hit);
	}

	public void  addMiss(Point3D_I idx) {
		occupancy_buffer.at_add(idx, datatype_miss);
	}

	public void closestPointInVolume(Vector3D_F32 point, Vector3D_F32 origin, Vector3D_F32 res) {
		
		float min_t = Float.MAX_VALUE;
		Point3D_I offset = occupancy_buffer.getOffset();

		Vector3D_F32 diff = new Vector3D_F32(point.x - origin.x, point.y - origin.y, point.z - origin.z); 
		
		for (int i = 0; i < 3; i++) {

			if (Math.abs(diff.getIdx(i)) > 0) {

				float t1 = ((offset.get(i) + 0.5f) * info.getCellSize() - origin.getIdx(i)) / diff.getIdx(i);
				if (t1 > 0 && t1 < min_t) min_t = t1;

				float t2 = ((offset.get(i) + N - 0.5f) * info.getCellSize() - origin.getIdx(i)) / diff.getIdx(i);
				if (t2 > 0 && t2 < min_t) min_t = t2;
			}
		}
		
		res.x = (origin.x + min_t * diff.x);
		res.y = (origin.y + min_t * diff.y);
		res.z = (origin.z + min_t * diff.z);
	}

	// TEST
	public void insertOccupied(Vector3D_F32 point) {
		
		Point3D_I idx = new Point3D_I();
		occupancy_buffer.getIdx(point, idx);
		if (occupancy_buffer.insideVolume(idx)) {
			flag_buffer.at_or(idx,occupied_flag);
			addHit(idx);
		}
	}
	
public void insertFree(Vector3D_F32 point) {
		
		Point3D_I idx = new Point3D_I();
		occupancy_buffer.getIdx(point, idx);
		if (occupancy_buffer.insideVolume(idx)) {
			flag_buffer.at_or(idx,free_flag);
			addMiss(idx);
		}
	}
	
	public RingBufferBase get() {
		return occupancy_buffer;
	}

	
	// END TEST

	public void insertFree(Vector3D_F32 point, Vector3D_F32 origin) {

		Vector3D_F32 dir = new Vector3D_F32(origin.x - point.x,origin.y - point.y,origin.z - point.z) ;

		float length = dir.norm(); dir.scale(1/length);

		Vector3D_F32 intermediate_point = new Vector3D_F32();
		Point3D_I intermediate_idx = new Point3D_I();

		for (float i = 0; i <= length; i += info.getCellSize()) {
			intermediate_point.x = point.x + dir.x * i;
			intermediate_point.y = point.y + dir.y * i;
			intermediate_point.z = point.z + dir.z * i;
			occupancy_buffer.getIdx(intermediate_point, intermediate_idx);
			System.out.println(intermediate_idx);
			flag_buffer.at_or(intermediate_idx,free_flag);
		}
	}

	public void insertFree(Point3D_I point_idx, Point3D_I origin_idx) {

		Vector3D_F32 point   = new Vector3D_F32();
		Vector3D_F32 origin  = new Vector3D_F32();

		occupancy_buffer.getPoint(point_idx, point);
		occupancy_buffer.getPoint(origin_idx, origin);

		Point3D_F32 dir = new Point3D_F32(origin.x - point.x,origin.y - point.y,origin.z - point.z) ;

		float length = dir.norm(); dir.scale(1/length);

		Vector3D_F32 intermediate_point = new Vector3D_F32();
		Point3D_I intermediate_idx = new Point3D_I();

		for (float i = 0; i <= length; i += info.getCellSize()) {
			intermediate_point.x = point.x + dir.x * i;
			intermediate_point.y = point.y + dir.y * i;
			intermediate_point.z = point.z + dir.z * i;
			occupancy_buffer.getIdx(intermediate_point, intermediate_idx);
			flag_buffer.at_or(intermediate_idx,free_flag);
		}
	}

	public void insertFreeBresenham3D(Point3D_I point_idx, Point3D_I origin_idx) {

	}
}
