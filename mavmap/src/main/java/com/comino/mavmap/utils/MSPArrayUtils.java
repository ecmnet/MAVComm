package com.comino.mavmap.utils;

import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.GrayU16;
import boofcv.struct.image.GrayU8;

public class MSPArrayUtils {

	public static void translate(short[][] map, int px, int py) {

		if(px==0 && py==0)
			return;

		if(px > 0 && py > 0) {
			for(int x = map.length-px-1;x>0;x--)
				for(int y=map.length-py-1;y>0;y--)
					map[x+px][y+py] = map[x][y];

			for(int x = map.length-1;x>map.length-px;x--)
				for(int y = map.length-1;y>map.length-py;y--)
					map[x][y] = 0;
		}

		if(px > 0 && py <= 0) {
			for(int x = map.length-px-1;x>0;x--)
				for(int y= -py;y < map.length+py;y++)
					map[x+px][y+py] = map[x][y];

			for(int x = map.length-1;x>map.length-px;x--)
				for(int y = 0;x<py;x++)
					map[x][y] = 0;
		}

		if(px <= 0 && py <= 0) {
			for(int x= -px; x < map.length+px;x++)
				for(int y= -py;y < map.length+py;y++)
					map[x+px][y+py] = map[x][y];

			for(int x = map.length+px-1;x<map.length;x++)
				for(int y = map.length+py-1;x<map.length;x++)
					map[x][y] = 0;
		}

		if(px <= 0 && py > 0) {
			for(int x= -px; x < map.length+px;x++)
				for(int y=map.length-py-1;y > 0;y--)
					map[x+px][y+py] = map[x][y];

			for(int x = 0;x<px;x++)
				for(int y=map.length-py-1;y>0;y--)
					map[x][y] = 0;
		}
	}

	public static GrayU8 convertToGrayU8(short[][] map, GrayU8 m ) {
		if(m==null)
		  m = new GrayU8(map[0].length, map[0].length);

		for(int y = 0;y < map[0].length; y++)
			for(int x = 0;x < map[0].length; x++)
//				if(map[x][y]>0)
//					m.data[x +  map[0].length * y ] = (byte)255;
				m.data[x +  map[0].length * y ] = (byte)(map[x][y]*10);
		return m;
	}

	public static GrayU8 convertToGrayU8(int[][] map,  GrayU8 m ) {
		if(m==null)
			  m = new GrayU8(map[0].length, map[0].length);
		for(int y = 0;y < map[0].length; y++)
			for(int x = 0;x < map[0].length; x++)
				m.data[x +  map[0].length * y ] = (byte)((map[x][y]) >> 16 * 10);
		return m;
	}

	public static GrayU16 convertToGrayU16(short[][] map,  GrayU16 m ) {
		if(m==null)
			  m = new GrayU16(map[0].length, map[0].length);
		for(int y = 0;y < map[0].length; y++)
			for(int x = 0;x < map[0].length; x++)
				m.data[x +  map[0].length * y ] = (short)((map[x][y]));
		return m;
	}

	public static void convertFromGrayU16(short[][] map,  GrayU16 m ) {
		for(int y = 0;y < map[0].length; y++)
			for(int x = 0;x < map[0].length; x++)
				(map[x][y]) = m.data[x +  map[0].length * y ];
	}

	public static GrayF32 convertToGrayF32(short[][] map,  GrayF32 m ) {
		if(m==null)
			  m = new GrayF32(map[0].length, map[0].length);
		for(int y = 0;y < map[0].length; y++)
			for(int x = 0;x < map[0].length; x++)
				m.data[x +  map[0].length * y ] = (short)((map[x][y]));
		return m;
	}

	public static void convertFromGrayF32(short[][] map,  GrayF32 m ) {
		for(int y = 0;y < map[0].length; y++)
			for(int x = 0;x < map[0].length; x++)
				(map[x][y]) = (short)m.data[x +  map[0].length * y ];
	}



}
