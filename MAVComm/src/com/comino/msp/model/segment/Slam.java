package com.comino.msp.model.segment;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import com.comino.msp.model.segment.generic.Segment;
import com.comino.msp.utils.BlockPoint3D;

public class Slam extends Segment {

	private static final long serialVersionUID = -77272456745165428L;

	public static final int LENGTH   = 12;
	public static final int MAXBLOCKS = LENGTH*LENGTH*LENGTH;

	BitSet data  = new BitSet(MAXBLOCKS);
	public float  cx=0;
	public float  cy=0;
	public float  cz=0;
	public float res=0.2f;
	public int   flags = 0;

	public void set(Slam a) {
		data = (BitSet)a.data.clone();
		flags = a.flags;
	}

	public Slam clone() {
		Slam at = new Slam();
		at.set(this);
		return at;
	}

	public void clear() {
		data.clear(0, MAXBLOCKS-1);
		flags = 0;
	}

	public long[] toArray() {
		return data.toLongArray();
	}

	public void fromArray(long[] array) {
		data = BitSet.valueOf(array);
	}

	public void  setBlock(float xpos, float ypos, float zpos) {
		data.set(calculateBlock(xpos, ypos, zpos));
	}

	public boolean isBlocked(float xpos, float ypos, float zpos) {
		return data.get(calculateBlock(xpos, ypos, zpos));
	}

	public boolean isBlocked(float xpos, float ypos) {
		boolean blocked = false;
		for(int i=0; i< 8; i++)
			blocked = blocked | isBlocked(xpos,ypos,i*res);
		return blocked;
	}

	public List<BlockPoint3D> getBlocks() {
		List<BlockPoint3D> list = new ArrayList<BlockPoint3D>();
		for(int i=0;i<MAXBLOCKS;i++) {
			if(data.get(i)) {
				list.add(new BlockPoint3D(
				  i % LENGTH * res + cx,i / LENGTH % LENGTH * res + cy, i / (LENGTH * LENGTH) * res,res + cz)
				);
			}
		}
		return list;
	}

	private int calculateBlock(float xpos, float ypos, float zpos) {
		int block = (int)((xpos - cx) / res)
				  + (int)((ypos - cy) / res) * LENGTH
				  + (int)((zpos - cz) / res) * LENGTH * LENGTH;
		return block;
	}

	public static void main(String[] args) {
		Slam s = new Slam();
		s.setBlock(0.5f,0.5f,0.1f);
		s.setBlock(1.5f,0.5f,0.4f);
		List<BlockPoint3D> list = s.getBlocks();
		for( BlockPoint3D p : list) {
			System.out.println(p.x+","+p.y+","+p.z);
		}

	}

}
