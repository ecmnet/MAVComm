package com.comino.msp.slam.map;

import com.comino.msp.slam.map.store.LocaMap2DStorage;

import boofcv.alg.filter.blur.GBlurImageOps;
import boofcv.gui.ListDisplayPanel;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayF32;

public class MapTest {

	static ListDisplayPanel panel = new ListDisplayPanel();

	private static void addMap(GrayF32 map, String name) {
		GrayF32 display = map.clone();
		 for(int i=0; i<display.data.length;i++)
			 display.data[i] = (display.data[i]*5000+(short)8192);
		 panel.addImage(ConvertBufferedImage.convertTo(display, null, true),name);
	}

	// Map enhancement tests

	public static void main(String[] args) {
		LocaMap2DStorage store = new LocaMap2DStorage("test");
		ILocalMap map = store.read();

		GrayF32 blurred = map.getMap().createSameShape();
		GBlurImageOps.gaussian(map.getMap(), blurred, -1, 2, null);

		addMap(map.getMap(),"Original");
		addMap(blurred,"Blurred");

		ShowImages.showWindow(panel,"Image Blur Examples",true);

	}

}
