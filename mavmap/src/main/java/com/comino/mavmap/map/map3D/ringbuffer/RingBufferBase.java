package com.comino.mavmap.map.map3D.ringbuffer;

import java.util.Arrays;

import georegression.struct.point.Point3D_I32;
import georegression.struct.point.Vector3D_F32;


// Source and papers http://vision.in.tum.de/research/robotvision/replanning

public class RingBufferBase<T> {

	private   int				size				= 0;
	private   int 				center			= 0;
	private   int				mask				= 0;
	private   int 				neg_mask			= 0;

	private   int				last_index      = 0;

	protected T					empty_element   = null;
	protected Point3D_I32 		offset			= new Point3D_I32();
	protected T[]			    buffer			= null;
	protected float				resolution		= 0.05f;

	@SuppressWarnings("unchecked")
	public RingBufferBase(float resolution, int size, T empty_element)   {
		this.size    	= size;
		this.center  	= size / 2;
		this.mask    	= (size - 1 );
		this.neg_mask  	= ~this.mask;
		this.resolution 	= resolution;
		this.buffer 		=  (T[])new Object[size*size*size];
		this.offset.set(-center, -center, -center);
		this.empty_element = empty_element;
		Arrays.fill(buffer, empty_element);
	}

	public int size() {
		return buffer.length;
	}

	public void setEmptyElement(T e) {
		this.empty_element = e;
	}

	public void setOffset(Point3D_I32 offset) {
		this.offset.set(offset);
	}

	public void set(Point3D_I32 index, T e) {
		last_index = size*size * (index.x & mask) + size * (index.y & mask) + (index.z & mask);
		buffer[last_index] = e;
	}

	public T get(Point3D_I32 index) {
		last_index = size*size * (index.x & mask) + size * (index.y & mask) + (index.z & mask);
		return buffer[last_index];
	}

	public T get() {
		return buffer[last_index];
	}

	public void set(T e) {
		buffer[last_index] = e;
	}

	public Point3D_I32 getOffset(Point3D_I32 offset) {
		offset.set(this.offset);
		return offset;
	}

	public T get(Vector3D_F32 point) {
		return get(getIdx(point));
	}

	public boolean insideVolume(Point3D_I32 index) {
		boolean res = true;
		res &= ((index.x - offset.x ) & neg_mask) == 0;
		res &= ((index.y - offset.y ) & neg_mask) == 0;
		res &= ((index.z - offset.z ) & neg_mask) == 0;
		return res;
	}

	public Point3D_I32 getVolumeCenter() {
		return new Point3D_I32(center+offset.x,center+offset.y,center+offset.z);
	}

	public Point3D_I32 getIdx(Vector3D_F32 point) {
		return getIdx(point,new Point3D_I32());
	}

	public Point3D_I32 getIdx(Vector3D_F32 point , Point3D_I32 index) {
		index.x = (int)(point.x / resolution);
		index.y = (int)(point.y / resolution);
		index.z = (int)(point.z / resolution);
		return index;
	}

	public Vector3D_F32 getPoint(Point3D_I32 idx, Vector3D_F32 point)  {
		point.x = (idx.x +0.5f)*resolution;
		point.y = (idx.y +0.5f)*resolution;
		point.z = (idx.z +0.5f)*resolution;
		return point;
	}

	public void moveVolume(Point3D_I32 direction) {

		if (direction.x==0 && direction.y==0 && direction.z==0)
			return;

		int slice;
		for (int axis = 0; axis < 3; axis++) {
				switch (axis) {
				case 0:
					if(direction.x > 0) {
						offset.x++; slice = offset.x + size - 1; }
					else {
						offset.x--; slice = offset.x;
					}
					setXSlice(slice, empty_element);
					break;
				case 1:
					if(direction.x > 0) {
						offset.y++; slice = offset.y + size - 1; }
					else {
						offset.y--; slice = offset.y;
					}
					setYSlice(slice, empty_element);
					break;
				case 2:
					if(direction.z > 0) {
						offset.z++; slice = offset.z + size - 1; }
					else {
						offset.z--; slice = offset.z;
					}
					setZSlice(slice, empty_element);
					break;
				}
		}
	}

	private void setXSlice(int slice_idx, T e) {
		Point3D_I32 idx = new Point3D_I32();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				idx.set(slice_idx, i, j);
				this.set(idx,e);
			}
		}
	}

	private void setYSlice(int slice_idx, T e) {
		Point3D_I32 idx = new Point3D_I32();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				idx.set(i, slice_idx, j);
				this.set(idx,e);
			}
		}
	}

	private void setZSlice(int slice_idx, T e) {
		Point3D_I32 idx = new Point3D_I32();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				idx.set(i, j, slice_idx);
				this.set(idx,e);
			}
		}
	}


	public static void main(String[] args) {

		Point3D_I32 index = new Point3D_I32();
		Vector3D_F32    p = new Vector3D_F32();

		RingBufferBase<Integer> test = new RingBufferBase<Integer>(0.1f,128, 20);
		p.set(6.4f, 5.2f,4.5f);

		test.getIdx(p, index);
		System.out.println(index);
		test.set(index, 64);

		System.out.println(test.get());

		p.set(6.4f, 5.2f,4.23f);

		System.out.println(test.get(p));



	}

}
