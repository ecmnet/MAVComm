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

/*
 * Source: https://github.com/agarie/vector-field-histogram
 */

package com.comino.vfh.vfh2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.comino.msp.utils.MSPMathUtils;
import com.comino.vfh.VfhGrid;
import com.comino.vfh.VfhHist;

public class PolarHistogram2D {

	private final static int DIV_N = 0;
	private final static int DIV_L = 1;
	private final static int DIV_R = 2;

	private VfhHist hist;
	public VfhHist hist_smoothed;

	private float resolution;
	private float density_a;
	private float density_b;

	private float threshold;
	private int alpha;

	private int diversion = DIV_N;

	private List<Valley> valleys = new ArrayList<Valley>();

	public PolarHistogram2D(int alpha, float threshold, float density_a, float density_b, float resolution) {

		this.alpha    = alpha;
		hist          = new VfhHist(alpha);
		hist_smoothed = new VfhHist(alpha);

		this.density_a = density_a;
		this.density_b = density_b;
		this.threshold = threshold;

		this.resolution = resolution;

		Arrays.fill(hist.densities, 0);

	}

	public void histUpdate(VfhGrid grid) {
		int dim = grid.dimension; float density=0;

		Arrays.fill(hist.densities, (int)0);
		for (int i = 0; i < dim; ++i) {
			for (int j = 0; j < dim; ++j) {

				if(grid.cells[i * dim + j] < 1)
					continue;

				/* Calculate the angular position (beta) of this cell. */
				int beta = (int)MSPMathUtils.fromRad((float)Math.atan2((double)(i - dim/2), (double)(j - dim/2)));

				/* Calculate the obstacle density of this cell. */
				density = grid.cells[i * dim + j] * grid.cells[i * dim + j] *
						(density_a - density_b * (float)Math.sqrt((i - dim/2)*(i - dim/2) + (j - dim/2)*(j - dim/2)) * resolution);

				/* Add density to respective point in the histogram. */
				int pos = (int)(wrap(beta+180,360)) / hist.alpha;
				hist.densities[pos] += density;

			}
		}
	}

	public VfhHist histSmooth(int l) {
		float h;
		if(l > 0 ) {
			for(int k =0; k < hist.sectors; k++) {
				h = 0;
				for(int i = -l; i<= l; i++ )
					h  = h + hist.densities[wrap(k+i+l,hist.sectors)] * (l - Math.abs(i) + 1);
				hist_smoothed.densities[wrap(k+l,hist.sectors)] = h / (2f * l + 1);
			}
			return hist_smoothed;
		}
		return hist;
	}

	public VfhHist getSmoothed() {
		return hist_smoothed;
	}


	public float getDirection(float target_direction_rad, int smax)  {
		int tdir = (int)MSPMathUtils.fromRad(target_direction_rad);
		int d    = Integer.MAX_VALUE;

		Valley tv = null;
		for(Valley v : getValleys(diversion, tdir)) {
			if(v.distance(smax,tdir) < d ) {
				tv = v; d  = v.distance(smax,tdir);
			}
		}

		int kdir = tv.get(smax, tdir);

		if(Math.abs(kdir -tdir)==0)
			  diversion = DIV_N;
		else {
			if(kdir > tdir)
				diversion = DIV_L;
			else
				diversion = DIV_N;
		}

		return MSPMathUtils.toRad(tv.get(smax, tdir));
	}

	private List<Valley> getValleys(int div,int tdir) {
		valleys.clear(); Valley v = null; int from;int to;

		switch(div) {
		default:
		     from = 0;
		     to   = hist_smoothed.sectors;
		     break;
		case DIV_R:
		     from = 0;
		     to   = tdir/alpha;
		     break;
		case DIV_L:
		     from = tdir/alpha;
		     to   = hist_smoothed.sectors;
		     break;
		}

		for(int i=from;i<to;i++) {
			if(hist_smoothed.densities[i]<threshold) {
				if(v==null) {
					v = new Valley(); v.s = i;
				} else {
					v.e = i;
				}
			} else {
				if(v!=null) {
					valleys.add(v); v = null;
				}
			}
		}
		if(v!=null) {
			if(v.e == hist_smoothed.sectors-1 && valleys.size()>0) {
				v.e = v.e + valleys.get(0).e; valleys.remove(0);
			}
			valleys.add(v);
			v = null;
		}
		return valleys;
	}


	private int wrap(int s, int max) {
		if(s < 0)
			return s + max;
		if(s >= max)
			return s - max;
		return s;
	}

	public void print() {
		print(hist,-1);
	}

	public void print(VfhHist h, int vi) {
		StringBuilder b = new StringBuilder();
		b.append(diversion);
		for(int i=0;i<h.sectors;i++) {
			if(vi > -1 && i==vi/alpha) {
				b.append("o");
			} else {
				if(h.densities[i]<threshold)
					b.append(".");
				else
					b.append("X");
			}
		}
		System.out.println(b.toString());
	}

	public void printDensities(VfhHist h, int vi) {
		System.out.println();
		for(int i=0;i<h.sectors;i++) {
			System.err.println(h.densities[i]);
		}
		System.out.println();
	}

	public class Valley  {

		public int s= 0;
		public int e=360;

		public int get(int smax, int tdir) {

			if(Math.abs(e-s) > 180/alpha && hist_smoothed.densities[tdir/alpha] < threshold) {
				return tdir;
			}

			if(Math.abs(e-s) > smax) {

				int a = modular_dist(s,tdir/alpha,360/alpha);
				int b = modular_dist(e,tdir/alpha,360/alpha);

				if(a<b)
					e = s + smax;
				else
					s = e - smax;
			}

			return modulo((int)(alpha*(s+e)/2),360);

		}

		public int getDiv(int smax,int tdir) {
			if((get(smax,tdir) - tdir)<0)
				return DIV_L;
			if((get(smax,tdir) - tdir)>0)
				return DIV_R;
			return DIV_N;

		}

		public int distance(int smax, int tdir) {

			int a = modular_dist(s,tdir/alpha,360/alpha);
			int b = modular_dist(e,tdir/alpha,360/alpha);

			return a < b ? a : b;
		}

		public String toString(int smax, int tdir) {
			return "From: "+s*alpha+" To: "+e*alpha;
		}

		private int modulo(int x, int m) {
			if (m < 0) m = -m;
			int  r = x % m;
			return r < 0 ? r + m : r;
		}

		public int modular_dist(int a, int b, int m) {
			int dist_a = modulo(a - b , m);
			int dist_b = modulo(b - a , m);
			return dist_a < dist_b ? dist_a : dist_b;
		}
	}

	public static void main(String[] args) {
		int target = 0;

		PolarHistogram2D poh = new PolarHistogram2D(2,0.5f,10,0.24f,0.05f);

		for(int i=40;i<120;i++)
			poh.hist_smoothed.densities[i/poh.alpha]  = 10f;
		for(int i=140;i<170;i++)
			poh.hist_smoothed.densities[i/poh.alpha]  = 10f;
		for(int i=240;i<260;i++)
			poh.hist_smoothed.densities[i/poh.alpha]  = 10f;

		System.out.println();


		target = (int)MSPMathUtils.fromRad(poh.getDirection(MSPMathUtils.toRad(0),  18));
		poh.print(poh.hist_smoothed,target);
		target = (int)MSPMathUtils.fromRad(poh.getDirection(MSPMathUtils.toRad(45), 18));
		poh.print(poh.hist_smoothed,target);
		target = (int)MSPMathUtils.fromRad(poh.getDirection(MSPMathUtils.toRad(130), 18));
		poh.print(poh.hist_smoothed,target);
		target = (int)MSPMathUtils.fromRad(poh.getDirection(MSPMathUtils.toRad(200), 18));
		poh.print(poh.hist_smoothed,target);
		target = (int)MSPMathUtils.fromRad(poh.getDirection(MSPMathUtils.toRad(258), 18));
		poh.print(poh.hist_smoothed,target);
		target = (int)MSPMathUtils.fromRad(poh.getDirection(MSPMathUtils.toRad(355), 18));
		poh.print(poh.hist_smoothed,target);

		System.out.println(target);

	}

}
