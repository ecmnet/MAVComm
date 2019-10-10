package com.comino.mavmap.map.map2D.filter.impl;

import com.comino.mavmap.map.map2D.filter.ILocalMapFilter;

public class MedianMapFilter implements ILocalMapFilter {

	private final int 	radius;

	private final int   threshold;
	private final int   boxWidth;

	private final int[] histogram;
	private final int[] offset;



	private int  stride;


	public MedianMapFilter(int radius) {
		this.radius = radius;
		this.histogram = new int[ 256 ];

		int w = 2*radius+1;
		this.offset = new int[ w*w ];
		this.threshold = (w*w)/2+1;
		this.boxWidth = radius*2+1;

	}

	@Override
	public void apply(short[][] map) {

		System.out.print("Try to apply filter...");

		this.stride = map[0].length;

		int index = 0;
		for( int i = -radius; i <= radius; i++ ) {
			for( int j = -radius; j <= radius; j++ ) {
				offset[index++] = i*stride + j;
			}
		}

		for( int y = radius; y < stride-radius; y++ ) {
			int seed = y*stride+radius;

			for( int i =0; i < histogram.length; i++ ) {
				histogram[i] = 0;
			}

			// compute the median value for the first x component and initialize the system
			for( int i = 0; i < offset.length; i++ ) {
				int val = getVal(map,seed+offset[i]) & 0xFF;
				histogram[val]++;
			}

			int count = 0;
			short median;
			for( median = 0; median < histogram.length; median++ ) {
				count += histogram[median];
				if( count >= threshold )
					break;
			}

			setVal(map,y*stride+radius,median);

			for( int i = 0; i < offset.length; i += boxWidth ) {
				int val = getVal(map,seed+offset[i]) & 0xFF;
				histogram[val]--;
			}

			for( int x = radius+1; x < stride-radius; x++ ) {
				seed = y*stride+x;

				// add the right most pixels to the histogram
				for( int i = boxWidth-1; i < offset.length; i += boxWidth ) {
					int val = getVal(map,seed+offset[i]) & 0xFF;
					histogram[val]++;
				}

				// find the median
				count = 0;
				for( median = 0; median < histogram.length; median++ ) {
					count += histogram[median];
					if( count >= threshold )
						break;
				}

				setVal(map,y*stride+x,median);

				// remove the left most pixels from the histogram
				for( int i = 0; i < offset.length; i += boxWidth ) {
					int val = getVal(map,seed+offset[i]) & 0xFFFF;
					histogram[val]--;
				}

			}
		}

		System.out.println("applied");
	}

	private short getVal(short[][] map, int pos) {
		return (short)(map[pos % stride][pos / stride] / 8 );
	}

	private void setVal(short[][] map, int pos, short val) {
		map[pos % stride][pos / stride] = val;
	}

}
