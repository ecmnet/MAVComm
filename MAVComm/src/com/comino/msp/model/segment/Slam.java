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
import com.comino.msp.utils.BlockPoint2D;
import com.comino.msp.utils.BlockPoint3D;

public class Slam extends Segment {

	private static final long serialVersionUID = -77272456745165428L;

	private static final int  MAX_LONG_SIZE = 27;

	private int      dimension 		= 30;
	private int      resolution_cm 	= 25;
	private int      max_length;

	private BitSet   data  = null;
	private int      cx,cy;
	private int      vx,vy;

	public Slam() {
		this.cx = dimension / 2;
		this.cy = dimension / 2;
		this.max_length = dimension * dimension;
		this.data  = new BitSet(max_length);
	}


	public Slam(float extension_m, float resolution_m) {
		this.dimension = (int)(extension_m/resolution_m)*2;
		this.resolution_cm = (int)(resolution_m*100f);
		this.cx = dimension / 2;
		this.cy = dimension / 2;
		this.max_length = dimension * dimension;
		this.data  = new BitSet(max_length);
	}

	public void set(Slam a) {
		data = new BitSet(max_length);
		data.xor(a.data);
	}

	public Slam clone() {
		Slam at = new Slam();
		at.cx = cx;
		at.cy = cy;
		at.set(this);
		return at;
	}

	public void clear() {
		data.clear(0, data.length()-1);
	}

	public long[] toArray() {
		return Arrays.copyOf(data.toLongArray(), MAX_LONG_SIZE);
	}

	public void fromArray(long[] array) {
		if(array!=null)
			data = BitSet.valueOf(array);
	}

	public void setVehicle(double vx, double vy) {
		this.vx = (int) (vx * 100f/resolution_cm)+cx;
		this.vy = (int) (vy * 100f/resolution_cm)+cy;
	}

	public boolean  setBlock(double xpos, double ypos) {
		setBlock(xpos,ypos,true);
		return true;
	}

	public boolean  setBlock(double xpos, double ypos, boolean set) {
		if(set)
			data.set(calculateBlock(xpos, ypos));
		else
			data.clear(calculateBlock(xpos, ypos));
		return true;
	}

	public boolean isBlocked(double xpos, double ypos) {
		return data.get(calculateBlock(xpos, ypos));
	}

	public boolean hasBlocked() {
		return !data.isEmpty();
	}

	public List<BlockPoint2D> getBlocks() {

		List<BlockPoint2D> list = new ArrayList<BlockPoint2D>();
		for(int r= 0; r < dimension; r++) {
			for(int c=0; c < dimension; c++) {
				if(data.get(r * dimension + c)) {
					list.add(new BlockPoint2D((c-cx)*resolution_cm/100f, (r-cy)*resolution_cm/100f));
				}
			}
		}
		return list;
	}

	public float getResolution() {
		return resolution_cm / 100f;
	}

	public float getVehicleX() {
		return (float)vx;
	}

	public float getVehicleY() {
		return (float)vy;
	}


	private int calculateBlock(double xpos, double ypos) {
		int block  =  (int) (xpos * 100f/resolution_cm) + cx
				+  ((int)(ypos * 100f/resolution_cm) + cy) * dimension;

		if(block < 0 || block > max_length-1)
			System.err.println(block+">"+max_length);

		return block;
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		System.out.println("GridSize: +/-"+(dimension*resolution_cm/200f)+
				" ("+dimension +"/"+resolution_cm+"/"+(dimension*dimension/8)+")");
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
				if(data.get(r * dimension + c))
					b.append("X");
				else
					b.append(".");
			}
			b.append("\n");
		}
		b.append("\n");
		return b.toString();
	}

	public static void main(String[] args) {
		Slam s = new Slam(5,0.25f);

		s.setBlock(1.0, 0.0);
		s.setBlock(0.0, 1.0);
		s.setBlock(1.0, 1.0);

		s.setVehicle(2.0, 1.3);

		List<BlockPoint2D> list = s.getBlocks();
		for(BlockPoint2D p : list)
			System.out.println(p);

		System.out.println();

		System.out.println(s);

	}

}
