package com.comino.mavmap.map.map3D.store;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.comino.mavcom.config.MSPConfig;
import com.comino.mavmap.map.map3D.impl.octree.LocalMap3D;
import com.comino.mavutils.MSPMathUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import georegression.struct.point.Point3D_I32;

public class LocaMap3DStorage {

	private final static String EXT  = ".m3D";

	private float  lat;
	private float  lon;

	private String     filename;
	private LocalMap3D map;

	private long    tms;
	private Gson    gson;

	private String base_path;

	/* TODO: Search for map within MAP size and determine global center
	 * then move map according to position
	 */

	public LocaMap3DStorage(LocalMap3D map, double lat, double lon) {

		try {
			this.base_path = MSPConfig.getInstance().getBasePath()+"/";
		} catch(Exception e) {
			this.base_path = System.getProperty("user.home")+"/";
		}

		this.lat = (float)Math.floor(lat * 1000000d) / 1000000f;
		this.lon = (float)Math.floor(lon * 1000000d) / 1000000f;
		this.map = map;

		this.filename = generateFileName()+EXT;
		this.gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
	}

	public LocaMap3DStorage(LocalMap3D map, String filename) {
		try {
			this.base_path = MSPConfig.getInstance().getBasePath()+"/";
		} catch(Exception e) {
			this.base_path = System.getProperty("user.home")+"/";
		}
		this.filename = filename + EXT;
		this.map      = map;


		this.gson = new Gson();

	}

	public void write() {

		this.tms = System.currentTimeMillis();

		ArrayList<Long> data = new ArrayList<Long>();
		map.getMapItems().forEachRemaining((p) -> { data.add(map.getMapInfo().encodeMapPoint(p, p.probability)); });

		File f = new File(base_path+filename);
		System.out.println("Map stored to "+f.getPath());
		if(f.exists()) f.delete();
		try {
			f.createNewFile();
			FileOutputStream fs = new FileOutputStream(f);
			fs.write(gson.toJson(data).getBytes());
			fs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		data.clear();
	}

	public boolean locateAndRead() {
		return locateAndRead(this.lat, this.lon);
	}

	public boolean locateAndRead(float lat, float lon) {
		float[] origin; String found; float distance_origin, distance = Float.MAX_VALUE;
		float[] req_translation = new float[2];

		MSPMathUtils.map_projection_init(this.lat, this.lon);


		found = null;
		for( String f : getMapFileNames()) {

			if(f.contains("test") || f.contains("7fc000007fc00000")) {
				found = f;
				break;
			}

			origin = getOriginFromFileName(f);
			distance_origin = MSPMathUtils.map_projection_distance(lat, lon, origin[0], origin[1], req_translation);
			if(distance_origin<distance && distance_origin < (map.getMapInfo().getDimension().x / map.getMapInfo().getBlocksPerM()) ) {
				found = f; distance = distance_origin;
			}
		}

		if(found==null)
			return false;


		return read(found);
	}

	public boolean read() {
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

	private boolean read(String fn) {
		
		Point3D_I32 mappo = new Point3D_I32(); double prob =0;
		File f = new File(base_path+fn);
		if(f.exists()) {
			try {
				FileInputStream fs = new FileInputStream(f);
				Long[] data = gson.fromJson(new BufferedReader(new InputStreamReader(fs)), Long[].class);
				System.out.println("Map '"+f.getAbsolutePath()+"' found in store with "+data.length+ " entries");
				map.clear();
				for(int i=0;i<data.length;i++) {
					prob = map.getMapInfo().decodeMapPoint(data[i], mappo);
					if(prob> 0.5)
					  map.setMapPoint(mappo, 1, System.currentTimeMillis());
				}
				return true;
			} catch (Exception e) {
				System.err.println(fn+" reading error ");
				e.printStackTrace();
				return false;
			}
		}
		System.err.println(fn+" not found");
		return false;
	}


}
