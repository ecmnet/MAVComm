package com.comino.msp.slam.map.store;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.comino.main.MSPConfig;
import com.comino.msp.slam.map.LocalMap2D;
import com.comino.msp.utils.MSPArrayUtils;
import com.comino.msp.utils.MSPMathUtils;
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

	private String base_path;

	/* TODO:
	 * Search for map within MAP size and determine global center
	 * then move map according to position
	 */

	public LocaMap2DStorage(LocalMap2D map,float lat, float lon) {

		this.base_path = MSPConfig.getInstance().getBasePath()+"/";

		this.lat = (float)Math.floor(lat * 1000000d) / 1000000f;
		this.lon = (float)Math.floor(lon * 1000000d) / 1000000f;
		this.map = map;

		this.filename = generateFileName()+EXT;

		InstanceCreator<LocalMap2D> creator = new InstanceCreator<LocalMap2D>() {
			public LocalMap2D createInstance(Type type) { return map; }
		};

		this.gson = new GsonBuilder().registerTypeAdapter(LocalMap2D.class, creator).create();
	}

	public void write() {

		this.tms = System.currentTimeMillis();

		File f = new File(base_path+filename);
		System.out.println("Map stored to "+f.getPath());
		if(f.exists()) f.delete();
		try {
			f.createNewFile();
			FileOutputStream fs = new FileOutputStream(f);
			fs.write(gson.toJson(map).getBytes());
			fs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public LocalMap2D locateAndRead() {
		return locateAndRead(this.lat, this.lon);
	}

	public LocalMap2D locateAndRead(float lat, float lon) {
		float[] origin; String found; float distance_origin, distance = Float.MAX_VALUE;
		float[] req_translation = new float[2]; int tx,ty;

		MSPMathUtils.map_projection_init(this.lat, this.lon);

		found = null;
		for( String f : getMapFileNames()) {

			if(f.contains("test")) {
				found = f;
				break;
			}
			origin = getOriginFromFileName(f);
			distance_origin = MSPMathUtils.map_projection_distance(lat, lon, origin[0], origin[1], req_translation);
			if(distance_origin<distance && distance_origin < map.getDiameter_mm()/2000f) {
				found = f; distance = distance_origin;
			}
		}

		if(found==null)
			return null;

		read(found);
		// perform map translation (rotation later)

		tx =   (int)Math.floor(req_translation[0]*1000f/map.getCellSize_mm());
		ty = - (int)Math.floor(req_translation[1]*1000f/map.getCellSize_mm());


		MSPArrayUtils.translate(map.get(), tx, ty);

		System.out.println("Map Translation: ["+req_translation[0]+","+req_translation[1]+"] => "+distance);

		return map;
	}

	public LocalMap2D read() {
		return read(filename);
	}

	public String generateFileName() {
		return Integer.toHexString(Float.floatToIntBits(lat)) + Integer.toHexString(Float.floatToIntBits(lon));
	}

	private float[] getOriginFromFileName(String filename) {
		float[] r = new float[2];
		r[0] =  Float.intBitsToFloat(Integer.decode("0x"+filename.substring(0, 8)));
		r[1] =  Float.intBitsToFloat(Integer.decode("0x"+filename.substring(8, 16)));
		return r;
	}

	public String toString() {
		return tms+": ["+lat+","+lon+"]";
	}

	private List<String> getMapFileNames() {
		ArrayList<String> result = new ArrayList<String>();
		File folder = new File(base_path);
		for(File f : folder.listFiles()) {
			if(f.isFile() && f.getName().contains(EXT))
				result.add(f.getName());
		}
		return result;
	}

	private LocalMap2D read(String fn) {

		File f = new File(base_path+fn);
		if(f.exists()) {
			System.out.println("Map '"+f.getAbsolutePath()+"' found in store");
			try {
				FileInputStream fs = new FileInputStream(f);
				return gson.fromJson(new BufferedReader(new InputStreamReader(fs)), LocalMap2D.class);
			} catch (IOException e) {
				System.err.println(fn+" reading error "+e.getMessage());
				return null;
			}
		}
		System.err.println(fn+" not found");
		return null;
	}

	public static void main(String[] args) {

		LocalMap2D map = new LocalMap2D(20f,0.05f,1.5f);
		LocaMap2DStorage store = new LocaMap2DStorage(map, 40.563734f,11.2363635f);

		store.write();

		String fn = store.generateFileName();
		System.out.println(fn);
		System.out.println(store);

		if(store.locateAndRead()!=null)
			System.out.println(store);
		else
			System.err.println("ER");

	}

}
