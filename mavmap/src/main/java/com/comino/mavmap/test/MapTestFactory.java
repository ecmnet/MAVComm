package com.comino.mavmap.test;

import org.mavlink.messages.MAV_SEVERITY;

import com.comino.mavcom.model.DataModel;
import com.comino.mavmap.map.map3D.impl.octree.LocalMap3D;

import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Point3D_I32;

public class MapTestFactory {

	/**
	 * Builds a virtual wall in front of the vehicle's current orientation
	 * 
	 * @param map
	 * @param model
	 * @param distance_m
	 */

	public static void buildWall(LocalMap3D map, DataModel model, float distance_m, float rel_altitude) {

		if(map==null)
			return;


		Point3D_F64   veh          = new Point3D_F64(model.state.l_x,model.state.l_y,model.state.l_z);
		Point3D_F64   pos          = new Point3D_F64();
		Point3D_F64   wall         = new Point3D_F64();

		pos.setZ(model.state.l_z);
		pos.x = model.state.l_x + Math.cos(model.attitude.y) * distance_m;
		pos.y = model.state.l_y + Math.sin(model.attitude.y) * distance_m;

		for(int k=-5; k<6; k++) {
			wall.x = pos.x + Math.sin(-model.attitude.y) * 0.05f * k;
			wall.y = pos.y + Math.cos(-model.attitude.y) * 0.05f * k;
			int level = (int)((model.hud.ar + rel_altitude) * map.getMapInfo().getBlocksPerM() + 0.5f) * 2;
			for(int i=0;i<level;i++) {
				wall.setZ(- i * map.getMapInfo().getCellSize()/2 );
				map.update(veh,wall);
			}
		}

	}

	private static void buildTree(LocalMap3D map, Point3D_F64 pos, float height, float volume) {
		
		Point3D_I32 p = new Point3D_I32();
		
		
		int level = (int)((height) * map.getMapInfo().getBlocksPerM());
		for(int i=0;i<level;i++) {
			pos.setZ(- i * map.getMapInfo().getCellSize());
			map.getMapInfo().globalToMap(pos, p);
			map.setMapPoint(p, 1);
		}
	}



}
