package com.comino.msp.slam.map2D.filter.impl;

import com.comino.msp.slam.map2D.filter.ILocalMapFilter;
import com.comino.msp.utils.MSPArrayUtils;

import boofcv.abst.denoise.FactoryImageDenoise;
import boofcv.abst.denoise.WaveletDenoiseFilter;
import boofcv.abst.filter.blur.BlurFilter;
import boofcv.factory.filter.blur.FactoryBlurFilter;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.GrayU16;

public class DenoiseMapFilter implements ILocalMapFilter {

	private final GrayF32 map_in;
	private final GrayF32 map_out;
	private final BlurFilter<GrayF32> blurrer;


	public DenoiseMapFilter(int width, int height) {
		this.map_in = new GrayF32(width,height);
		this.map_out = new GrayF32(width,height);
		this.blurrer = FactoryBlurFilter.gaussian(GrayF32.class,2.5,2);
	}

	@Override
	public void apply(short[][] input) {
		long tms = System.currentTimeMillis();
		MSPArrayUtils.convertToGrayF32(input, map_in);
		blurrer.process(map_in, map_out);
		MSPArrayUtils.convertFromGrayF32(input, map_out);
		System.out.println("Filter application in "+(System.currentTimeMillis()-tms)+"ms");

	}


}
