package com.comino.msp.slam.map.store;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import com.comino.msp.slam.map.LocalMap2D;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;

public class LocaMap2DStorage {

	private final static String EXT  = ".m2D";

	private float  lat;
	private float  lon;

	private String  filename;
	private LocalMap2D map;

	private long    tms;
	private Gson    gson;

	public LocaMap2DStorage(LocalMap2D map,float lat, float lon) {

		this.lat = lat;
		this.lon = lon;
		this.map = map;

		this.filename = System.getProperty("user.home")+"/"+generateFileName()+EXT;
		System.out.println(this.filename);

		InstanceCreator<LocalMap2D> creator = new InstanceCreator<LocalMap2D>() {
			  public LocalMap2D createInstance(Type type) { return map; }
		};

		this.gson = new GsonBuilder().registerTypeAdapter(LocalMap2D.class, creator).create();
	}

	public void write() {

		this.tms = System.currentTimeMillis();

		File f = new File(filename);
		if(f.exists()) f.delete();
		try {
			f.createNewFile();
			FileOutputStream fs = new FileOutputStream(f);
			fs.write(gson.toJson(map).getBytes());
			fs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean read() {

		File f = new File(filename);
		if(f.exists()) {
			try {
				FileInputStream fs = new FileInputStream(filename);
				gson.fromJson(new BufferedReader(new InputStreamReader(fs)), LocalMap2D.class);
				return true;
			} catch (IOException e) {
				return false;
			}
		}
		return false;
	}

	public String generateFileName() {
		return Integer.toHexString(Float.floatToIntBits(lat)) + Integer.toHexString(Float.floatToIntBits(lon));
	}

	public void setOriginFromFileName(String filename) {
		this.lat =  Float.intBitsToFloat(Integer.decode("0x"+filename.substring(0, 8)));
		this.lon =  Float.intBitsToFloat(Integer.decode("0x"+filename.substring(8, 16)));
	}

	public String toString() {
		return tms+": ["+lat+","+lon+"]";
	}

	public static void main(String[] args) {

		LocalMap2D map = new LocalMap2D(20f,0.05f,1.5f);
		LocaMap2DStorage store = new LocaMap2DStorage(map, 40.563734f,11.2363635f);

		store.write();

		String fn = store.generateFileName();
		System.out.println(fn);
		store.setOriginFromFileName(fn);
		System.out.println(store);

		if(store.read())
			System.out.println("Ok");
		else
			System.err.println("ER");

	}

}
