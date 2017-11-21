package com.comino.msp.slam.map;

import java.util.ArrayList;

import com.comino.msp.slam.core.CoreSLAM;
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
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F32;
import georegression.struct.point.Vector3D_F64;
import georegression.struct.point.Vector4D_F64;

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


		CoreSLAM slam = new CoreSLAM(map);

		ArrayList<Point3D_F64> list = new ArrayList<Point3D_F64>();

		Vector4D_F64 pos = new Vector4D_F64();
		Point3D_F64 p = new Point3D_F64();

		p.set(1,1,0);
		list.add(p.copy());
		p.set(2,0,0);
		list.add(p.copy());

		slam.update(list, pos);

		p.set(1.4,1,0);
		list.add(p.copy());
		p.set(2.1,0.0,0);
		list.add(p.copy());

		pos.set(0.1f,0,0,0);

		long tms = System.currentTimeMillis();
		slam.update(list, pos);

		System.out.println("Test: "+(System.currentTimeMillis() - tms));


		GrayU8 a = map.getMap().createSameShape();
		GrayU8 b = map.getMap().createSameShape();


		GBlurImageOps.gaussian(map.getMap(), a, 20, 2, null);




		addMap(map.getMap(),"Map");
		addMap(a,"opA");
		addMap(b,"opB");

		ShowImages.showWindow(panel,"Image Blur Examples",true);

	}

}
