

package com.comino.mavmap.map.map3D;

import bubo.maps.d3.grid.CellProbability_F64;
import georegression.struct.GeoTuple3D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Point3D_I32;


public class Map3DSpacialInfo {

	// size of a grid cell in global units
	private float  cellSize;
	private int    blocks_per_m;
	private final Point3D_I32 dimension;
	private final Point3D_I32 center;
	private final Point3D_F64 bl;

	private float cellSize2;
	
	private int dimensionxy;
	private int dimensionxyz;

	/**
	 * @param cellSize   Size of a map cell
	 * @param bottomLeft Bottom left coordinate of the map
	 */
	public Map3DSpacialInfo(double cellSize, Point3D_F64 bottomLeft) {
		this.dimension    = new Point3D_I32();
		this.bl           = bottomLeft;	
		this.cellSize     = (float)cellSize;
		this.cellSize2    = (float)cellSize / 2;
		this.blocks_per_m = (int)(1/cellSize + 0.5f);
		this.dimension.setTo((int)((bl.x) / cellSize  ) + 1, (int)((bl.y) / cellSize  ) + 1, (int)((bl.z) / cellSize  ) + 1);
		this.dimensionxy  = dimension.x * dimension.y;
		this.dimensionxyz = dimensionxy * dimension.z;
		this.center       = new Point3D_I32(dimension.x/2,dimension.y/2,0);
		System.out.println("Map Dimension is "+dimension+" BlockSize "+blocks_per_m);
		
	}

	/**
	 * @param cellSize Size of a map cell
	 * @param bl_x     Bottom left of the map.  x-coordinate
	 * @param bl_y     Bottom left of the map.  y-coordinate
	 */
	public Map3DSpacialInfo(double cellSize, double bl_x, double bl_y, double bl_z) {
		this(cellSize,new Point3D_F64(bl_x, bl_y, bl_z));
	}
	
	public void adjustResolution(double cellsize) {
		double adjustment = cellsize / this.cellSize;

		this.bl.x          = this.bl.x * adjustment;
		this.bl.y          = this.bl.y * adjustment;
		this.bl.z          = this.bl.z * adjustment;
		
		this.cellSize     = (float)cellsize;
		this.cellSize2    = (float)cellsize / 2;
		this.blocks_per_m = (int)(1/cellSize + 0.5f);
		
		this.dimension.setTo((int)((bl.x) / cellSize  ) + 1, (int)((bl.y) / cellSize  ) + 1, (int)((bl.z) / cellSize  ) + 1);
		this.dimensionxy  = dimension.x * dimension.y;
		this.dimensionxyz = dimensionxy * dimension.z;
		this.center.setTo(dimension.x/2,dimension.y/2,0);
		System.out.println("Map Dimension adjusted "+dimension+" BlockSize "+blocks_per_m);
		
	}

	/**
	 * Convert from global coordinates into map cell coordinates.
	 */
	public void globalToMap(GeoTuple3D_F64<?> global, Point3D_I32 map) {
		map.x =   (int)((global.x - cellSize2)   * blocks_per_m + 0.5) + center.x;
		map.y =   (int)((global.y - cellSize2)   * blocks_per_m + 0.5) + center.y;
		map.z =   (int)((- cellSize2 - global.z) * blocks_per_m + 0.5) + center.z;
	}
	
	public void globalToMap(GeoTuple3D_F64<?> global, GeoTuple3D_F64<?> origin, Point3D_I32 map) {
		map.x =   (int)((global.x - origin.x - cellSize2)   * blocks_per_m + 0.5) + center.x;
		map.y =   (int)((global.y - origin.y - cellSize2)   * blocks_per_m + 0.5) + center.y;
		map.z =   (int)((- cellSize2 - global.z - origin.z) * blocks_per_m + 0.5) + center.z;
	}


	/**
	 * Convert from map cell coordinates into global coordinates
	 */
	public void mapToGlobal(Point3D_I32 map, GeoTuple3D_F64<?> global) {
		global.x =   (map.x - center.x ) * cellSize + cellSize2 ;
		global.y =   (map.y - center.y ) * cellSize + cellSize2 ;
		global.z = - (map.z - center.z ) * cellSize + cellSize2 ;
	}
	
	/**
	 * Convert from map cell coordinates into global coordinates
	 */
	public void mapToGlobal(Point3D_I32 map,  GeoTuple3D_F64<?> origin, GeoTuple3D_F64<?> global) {
		global.x =   (map.x - center.x ) * cellSize + cellSize2 + origin.x;
		global.y =   (map.y - center.y ) * cellSize + cellSize2 + origin.y;
		global.z = - (map.z - center.z ) * cellSize + cellSize2 + origin.z;
	}
	
	/**
	 * Rounds to map grid
	 */
	
	public void roundToMap(GeoTuple3D_F64<?> global) {
		global.x =  (int)((global.x - cellSize2) * blocks_per_m +0.5)   * cellSize + cellSize2 ;
		global.y =  (int)((global.y - cellSize2) * blocks_per_m +0.5)   * cellSize + cellSize2 ;
		global.z = -((int)((- cellSize2 - global.z) * blocks_per_m+0.5) * cellSize + cellSize2 );
	}

	
	public double decodeMapPoint(long mpi, Point3D_I32 p) {

		p.x = (int)( mpi % dimension.x);
		p.y = (int)( mpi / dimension.x % dimension.y);
		p.z = (int)( mpi / dimensionxy % dimension.z);
		
		return (double)((long)(mpi / dimensionxyz) / 100.0);
	}
	
	public void decodeMapPoint(long mpi, CellProbability_F64 p) {

		p.x = (int)( mpi % dimension.x);
		p.y = (int)( mpi / dimension.x % dimension.y);
		p.z = (int)( mpi / dimensionxy % dimension.z);
		
		p.probability = (double)((long)(mpi / dimensionxyz) / 100.0);
		p.tms = System.currentTimeMillis();
	
	}
	
	public double decodeMapPoint(long mpi) {
		
		return (double)((long)(mpi / dimensionxyz) / 100.0);
	}
	

	public long encodeMapPoint(Point3D_I32 p) {
		return encodeMapPoint(p,0);
	}
	
	/**
	 *  Encode integer based map point 
	 */
	
	public long encodeMapPoint(Point3D_I32 p, double probability ) {
		return (long)(p.x) + (long)(p.y) * dimension.x + (long)(p.z) * dimensionxy + (long)(100.0 * probability) * dimensionxyz;
	}
	
	
	public Point3D_I32 getDimension() {
		return dimension;
	}
	
	public int getDimensionXY() {
		return dimensionxy;
	}
	
	public float getCellSize() {
		return cellSize;
	}
	
	public double getBlocksPerM() {
		return (double)blocks_per_m;
	}

	public Point3D_F64 getBl() {
		return bl;
	}
	
}
