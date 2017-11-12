package com.comino.msp.slam.map;

import com.comino.msp.slam.map.impl.LocalMap2DArray;
import com.comino.msp.slam.map.impl.LocalMap2DGrayU8;
import com.comino.msp.slam.map.store.LocaMap2DStorage;

import boofcv.abst.denoise.FactoryImageDenoise;
import boofcv.abst.denoise.WaveletDenoiseFilter;
import boofcv.alg.filter.blur.GBlurImageOps;
import boofcv.gui.ListDisplayPanel;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.GrayU8;

public class MapTest {

	static ListDisplayPanel panel = new ListDisplayPanel();

	private static void addMap(GrayU8 map, String name) {
		GrayU8 display = map.clone();
		 for(int i=0; i<display.data.length;i++)
			 display.data[i] = (byte)(display.data[i]*64);
		 panel.addImage(ConvertBufferedImage.convertTo(display, null, true),name);
	}

	// Map enhancement tests

	public static void main(String[] args) {
		LocalMap2DArray map    = new LocalMap2DArray();
		LocaMap2DStorage store = new LocaMap2DStorage(map,"test");
		if(!store.read())
		  System.exit(-1);

		GrayU8 a = map.getMap().createSameShape();
		GrayU8 b = map.getMap().createSameShape();
		long tms = System.currentTimeMillis();

		GBlurImageOps.gaussian(map.getMap(), a, 20, 2, null);


		System.out.println("Test: "+(System.currentTimeMillis() - tms));

		addMap(map.getMap(),"Map");
		addMap(a,"opA");
		addMap(b,"opB");

		ShowImages.showWindow(panel,"Image Blur Examples",true);

	}

}
