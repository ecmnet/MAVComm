package com.comino.mavmap.map.map3D.ringbuffer;

import org.ddogleg.struct.FastQueue;

import com.comino.mavmap.struct.Point3D_I;

import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Point3D_I32;
import georegression.struct.point.Vector3D_F32;

// Source and papers http://vision.in.tum.de/research/robotvision/replanning

public class RaycastRingBuffer {

	private static final byte	OCCUPIED_FLAG		= (1 << 0);
	private static final byte	FREE_FLAG		    = (1 << 1);
	private static final byte	FREE_RAY_FLAG		= (1 << 2);
	private static final byte	UPDATED_FLAG		    = (1 << 3);



	private byte 				  insertion_flags 		= 0;

	private RingBufferBase<Byte>		flag_buffer			= null;
	private RingBufferBase<Short>	occupancy_buffer		= null;
	private int 						size					= 0;

	private Point3D_I					updated_min      = new Point3D_I();
	private Point3D_I					updated_max      = new Point3D_I();

	private static final double min_val = -2;
	private static final double max_val = 3.5;

	private static final double chit = 0.85;
	private static final double cmiss = -0.4;

	private static final short  hit  = (short)(chit  * (Short.MAX_VALUE - Short.MIN_VALUE) / (max_val - min_val));
	private static final short  miss = (short)(cmiss * (Short.MAX_VALUE - Short.MIN_VALUE) / (max_val - min_val));


	public RaycastRingBuffer(float resolution, int size) {
		this.size                = size;
		this.flag_buffer 		 = new RingBufferBase<Byte>(resolution,size,(byte)0);
		this.occupancy_buffer  	 = new RingBufferBase<Short>(resolution,size,(short)0);
	}

	public boolean isOccupied(Point3D_I32 index) {
		return occupancy_buffer.get(index) > hit;
	}

	public boolean isFree(Point3D_I32 index) {
		return occupancy_buffer.get(index) < miss;
	}

	public boolean isUpdated(Point3D_I32 index) {
		return (flag_buffer.get(index) & UPDATED_FLAG) == UPDATED_FLAG;
	}

	public void clearUpdated(Point3D_I32 index) {
		flag_buffer.set((byte)(flag_buffer.get(index) & UPDATED_FLAG));
	}

	public void setOffset(Point3D_I32 offset) {
		occupancy_buffer.setOffset(offset);
		flag_buffer.setOffset(offset);
	}

	public void moveVolume(Point3D_I direction) {

		Point3D_I min_point = new Point3D_I();
		Point3D_I max_point = new Point3D_I();

		occupancy_buffer.moveVolume(direction);
		flag_buffer.moveVolume(direction);

		Point3D_I volume_min = new Point3D_I();
		occupancy_buffer.getOffset(volume_min);

		Point3D_I volume_max = volume_min.copy().add(size);

		for(int i=0; i<3; ++i) {

			min_point.set(volume_min);
			max_point.set(volume_max);

			if(direction.get(i) > 0)
				min_point.set(i,volume_max.get(i));
			 else
				max_point.set(i,volume_min.get(i));

			for(int j=0;j<3;j++) {
				updated_min.set(j, Math.min(updated_min.get(j), min_point.get(j)));
				updated_max.set(j, Math.max(updated_max.get(j), min_point.get(j)));

				updated_min.set(j, Math.min(updated_min.get(j), max_point.get(j)));
				updated_max.set(j, Math.max(updated_max.get(j), max_point.get(j)));
			}

		}
	}

	public void insertPointCloud(FastQueue<Point3D_F32> cloud, Vector3D_F32 origin) {

	}


}
