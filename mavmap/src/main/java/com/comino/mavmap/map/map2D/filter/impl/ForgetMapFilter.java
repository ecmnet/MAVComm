package com.comino.mavmap.map.map2D.filter.impl;


import com.comino.mavmap.map.map2D.filter.ILocalMapFilter;

public class ForgetMapFilter implements ILocalMapFilter {


	public ForgetMapFilter() {

	}

	@Override
	public void apply(short[][] input) {

		for(int x = 0; x < input.length;x++)
			for(int y = 0; y < input[x].length;y++) {
				if(input[x][y] > 20) {
			 	  input[x][y] = (short)(input[x][y] - 20) ;
				}
			}
	}


}
