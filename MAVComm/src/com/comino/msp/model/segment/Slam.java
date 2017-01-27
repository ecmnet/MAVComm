/****************************************************************************
 *
 *   Copyright (c) 2017 Eike Mansfeld ecm@gmx.de. All rights reserved.
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
import java.util.BitSet;
import java.util.List;

import com.comino.msp.model.segment.generic.Segment;
import com.comino.msp.utils.BlockPoint3D;

public class Slam extends Segment {

	private static final long serialVersionUID = -77272456745165428L;

	public static final int LENGTH   = 12;
	public static final int MAXBLOCKS = LENGTH*LENGTH*LENGTH;

	public BitSet data  = new BitSet(MAXBLOCKS);
	private float  cx,cy,cz;
	public float   res;
	public int     flags = 0;

	public Slam() {
		this(0,0,0,0.2f);
	}

	public Slam(float res) {
		this(0,0,0,res);
	}

	public Slam(float cx, float cy, float cz, float res) {
		this.cx = cx - LENGTH/2f*res;
		this.cy = cy - LENGTH/2f*res;
		this.cz = cz - LENGTH/2f*res;
		this.res = res;
	}

	public void set(Slam a) {
		data = new BitSet(MAXBLOCKS);
		data.xor(a.data);
		flags = a.flags;
	}

	public Slam clone() {
		Slam at = new Slam(res);
		at.cx = cx;
		at.cy = cy;
		at.cz = cz;
		at.set(this);
		return at;
	}

	public void clear() {
		data.clear(0, MAXBLOCKS-1);
		flags = 0;
	}

	public long[] toArray() {
		return Arrays.copyOf(data.toLongArray(), MAXBLOCKS/64);
	}

	public void fromArray(long[] array) {
		data = BitSet.valueOf(array);
	}

	public void moveTo(float cx, float cy, float cz) {
		scale(cx,cy,cz, res);
		System.out.println("New origin is: "+cx+","+cy+","+cz);
	}

	public void scale(float cx, float cy, float cz, float res) {
		List<BlockPoint3D> list = getBlocks();
		this.cx = cx - LENGTH/2f*res;
		this.cy = cy - LENGTH/2f*res;
		this.cz = cz - LENGTH/2f*res;
		this.res = res;
		if(list!=null) {
			data.clear();
			for( BlockPoint3D p : list)
				setBlock(p.x, p.y, p.z);
		}
	}

	public boolean  setBlock(float xpos, float ypos, float zpos) {
		setBlock(xpos,ypos,zpos,true);
		return true;
	}

	public boolean  setBlock(float xpos, float ypos, float zpos, boolean set) {
		if( Math.abs(Math.round(xpos)) >= LENGTH/2 * res ||
	    	Math.abs(Math.round(ypos)) >= LENGTH/2 * res ||
			Math.abs(Math.round(zpos)) >= LENGTH/2 * res)
			return false;
		if(set)
			data.set(calculateBlock(xpos, ypos, zpos));
		else
			data.clear(calculateBlock(xpos, ypos, zpos));
		return true;
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

	public boolean hasBlocked() {
		return !data.isEmpty();
	}

	public List<BlockPoint3D> getBlocks() {

		if(!hasBlocked())
			return null;

		List<BlockPoint3D> list = new ArrayList<BlockPoint3D>();
		for(int i=0;i<MAXBLOCKS;i++) {
			if(data.get(i)) {
				list.add(new BlockPoint3D(
						(i %    LENGTH              ) * res + cx -res/2f,
						( i /   LENGTH  % LENGTH    ) * res + cy +res/2f,
						( i / ( LENGTH * LENGTH )   ) * res + cz +res/2f, res)
						);
			}
		}
		return list;
	}


	public int calculateBlock(float xpos, float ypos, float zpos) {
		int block =  Math.round((xpos  - cx) / res)
			      +  Math.round((ypos  - cy) / res) * LENGTH
				  +  Math.round((zpos  - cz) / res) * LENGTH * LENGTH;
		return block;
	}

	public static void main(String[] args) {
		Slam s = new Slam(0,0,0,0.2f);

		s.setBlock(0.4f,-0.4f,-0.8f);
		s.setBlock(0.2f,-0.2f,0);
		s.setBlock(0.0f,0.0f,0.2f);

		List<BlockPoint3D> list = s.getBlocks();
		for( BlockPoint3D p : list) {
			System.out.println(s.calculateBlock(p.x, p.y, p.z)+":"+p);
		}
		System.out.println();
		//    s.moveTo(0.4f, -0.4f,-0.4f);

		List<BlockPoint3D> list2 = s.getBlocks();
		for( BlockPoint3D p : list2) {
			System.out.println(s.calculateBlock(p.x, p.y, p.z)+":"+p);
		}

		System.out.println();
		//    s.moveTo(0,0,0);

		Slam u = s.clone();

		List<BlockPoint3D> list3 = u.getBlocks();
		for( BlockPoint3D p : list3) {
			System.out.println(s.calculateBlock(p.x, p.y, p.z)+":"+p);
		}

	}

}
