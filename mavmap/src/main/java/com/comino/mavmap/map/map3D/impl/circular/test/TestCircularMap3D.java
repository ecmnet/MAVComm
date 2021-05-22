package com.comino.mavmap.map.map3D.impl.circular.test;

import com.comino.mavmap.map.map3D.Map3DSpacialInfo;
import com.comino.mavmap.map.map3D.impl.circular.RayCastRingBuffer;
import com.comino.mavmap.struct.Point3D_I;

import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Vector3D_F32;

public class TestCircularMap3D {
	
	public TestCircularMap3D() {
		
		Point3D_I idx = new Point3D_I();
		
		Map3DSpacialInfo info = new Map3DSpacialInfo(0.1f,5.0f,5.0f,5.0f);
		RayCastRingBuffer cb = new RayCastRingBuffer(info);
		
		Vector3D_F32 p  = new Vector3D_F32(1,0,0);
		Vector3D_F32 p2 = new Vector3D_F32(0,1,0);
		Vector3D_F32 p3 = new Vector3D_F32(13f,13f,13f);
		
		Vector3D_F32 ori = new Vector3D_F32(2.5f,2.5f,2.5f);
		Vector3D_F32 res = new Vector3D_F32();
		
		cb.insertOccupied(p);
		cb.insertFree(p2);
		
		cb.get().getIdx(p, idx);
		
		System.out.println("\nOccupied:");
		System.out.println(cb.isOccupied(idx));
		System.out.println(cb.isFree(idx));
		
		System.out.println("\nFree:");
		cb.get().getIdx(p2, idx);
		System.out.println(cb.isOccupied(idx));
		System.out.println(cb.isFree(idx));
		
		System.out.println("\nin Volume:");
		cb.get().getIdx(p3, idx);
		System.out.println(cb.get().insideVolume(idx));
		cb.closestPointInVolume(p3, ori, res);
		System.out.println(res);
		
		System.out.println("\nin Volume:");
		cb.get().getIdx(res, idx);
		System.out.println(cb.get().insideVolume(idx));
		
		
		
	
	}
	

	public static void main(String[] args) {
		new TestCircularMap3D();

	}

}
