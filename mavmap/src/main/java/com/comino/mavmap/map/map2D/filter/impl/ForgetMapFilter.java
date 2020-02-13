package com.comino.mavmap.map.map2D.filter.impl;



import com.comino.mavmap.map.map2D.filter.ILocalMapFilter;

public class ForgetMapFilter implements ILocalMapFilter {

    long tms = 0;

	public ForgetMapFilter() {

	}

	@Override
	public void apply(short[][] input) {
		if((System.currentTimeMillis() - tms) < 500)
			return;
		for(int x = 0; x < input.length;x++)
			for(int y = 0; y < input[x].length;y++) {
			 	input[x][y] =-1;
				if(input[x][y] < 0) input[x][y] = 0;
			}
		tms = System.currentTimeMillis();
	}


}
