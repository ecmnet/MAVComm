package com.comino.msp.model.segment;

import java.util.Arrays;

import com.comino.msp.model.segment.generic.Segment;

public class Slam extends Segment {

	private static final long serialVersionUID = -77272456745165428L;

	public byte[] data = null;

	public void set(Slam a) {
		if(a.data!=null)
		  Arrays.copyOf(a.data, a.data.length);
	}

	public Slam clone() {
		Slam at = new Slam();
		at.set(this);
		return at;
	}

	public void clear() {
		if(this.data!=null)
		  Arrays.fill(data, (byte)0);
	}

	public void  setBlock(float xpos, float ypos, float zpos) {

	}

	public boolean isBlocked(float xpos, float ypos, float zpos) {

		return false;
	}

}
