package com.comino.vfhplus;

import com.comino.msp.slam.map.LocalMap2D;

import georegression.struct.point.Vector3D_F32;

public class TestVfhPlus2D {

	private VfhPlus2D 	vfhp = null;
	private LocalMap2D 	map = null;

	private Vector3D_F32 current_pos = new Vector3D_F32(0,0,0);
	private Vector3D_F32 target_pos  = new Vector3D_F32(0,5,0);
	private Vector3D_F32 point       = new Vector3D_F32(0,0.7f,0);

	public TestVfhPlus2D() {
		vfhp = new VfhPlus2D();
		vfhp.init();

	//	map = new LocalMap2D(8.0f, 0.05f);
		map.setLocalPosition(current_pos);

		for(int i=-0;i<80;i++) {
		  point.x = i*0.05f;
		  map.update(point);
		}

//		vfhp.update_VFH(map.getWindowPolar(0,0,0), 300 ,180 ,6000, 10);
		System.out.println(vfhp.getAngle()+"°  "+vfhp.getSpeed()+"mm/s  "+vfhp.getTurnrate());
		vfhp.print_Hist();

//		try { Thread.sleep(500); } catch(Exception e) { }
//		current_pos.y+=0.1f;
//		vfhp.Update_VFH(map, current_pos, target_pos, 0.1f, 0.30f);
//		System.out.println(vfhp.getAngle()+"°  "+vfhp.getSpeed()+"mm/s  "+vfhp.getTurnrate());
//		try { Thread.sleep(500); } catch(Exception e) { }
//		current_pos.y+=0.1f;
//		vfhp.Update_VFH(map, current_pos, target_pos, 0.1f, 0.30f);
//		System.out.println(vfhp.getAngle()+"°  "+vfhp.getSpeed()+"mm/s  "+vfhp.getTurnrate());
//		try { Thread.sleep(500); } catch(Exception e) { }
//		current_pos.y+=0.1f;
//		vfhp.Update_VFH(map, current_pos, target_pos, 0.1f, 0.30f);
//		System.out.println(vfhp.getAngle()+"°  "+vfhp.getSpeed()+"mm/s  "+vfhp.getTurnrate());



	}




	public static void main(String[] args) {
		new TestVfhPlus2D();
	}

}
