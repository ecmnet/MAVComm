/****************************************************************************
 *
 *   Copyright (c) 2017 Eike Mansfeld ecm@gmx.de. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 ****************************************************************************/

package com.comino.mavmap.mapper.impl;


import org.mavlink.messages.MSP_CMD;
import org.mavlink.messages.lquac.msg_msp_command;

import com.comino.mavcom.config.MSPConfig;
import com.comino.mavcom.control.IMAVMSPController;
import com.comino.mavcom.mavlink.IMAVLinkListener;
import com.comino.mavcom.model.DataModel;
import com.comino.mavcom.utils.MSP3DUtils;
import com.comino.mavmap.map.map2D.ILocalMap;
import com.comino.mavodometry.estimators.IMAVDetector;
import com.comino.mavodometry.librealsense.r200.vio.odometry.MAVDepthVisualOdometry;
import com.comino.mavodometry.video.IVisualStreamHandler;

import boofcv.struct.image.GrayU16;
import boofcv.struct.image.GrayU8;
import georegression.geometry.ConvertRotation3D_F64;
import georegression.struct.EulerType;
import georegression.struct.point.Point3D_F64;
import georegression.struct.se.Se3_F64;
import georegression.transform.se.SePointOps_F64;

public class FwDirectDepthDetector implements IMAVDetector {

	private static final float MIN_ALTITUDE                = 0.3f;
	private static final float COLLISION_WARNING_DISTANCE  = 1f;

	private static final int   YBAND_LOW     = 60;
	private static final int   YBAND_HIGH    = 180;

	private double     	max_distance     	= 5.0f;
	private double     	current_min_distance= 0.0f;
	private double     	min_altitude     	= 0;

	private DataModel   model        		= null;
	private ILocalMap 	map 				= null;

	private Point3D_F64	point				= null;
	private Point3D_F64 point_min      		= new Point3D_F64();
	private Point3D_F64 point_ned       	= new Point3D_F64();

	private Se3_F64 	  current 			= new Se3_F64();


	public <T> FwDirectDepthDetector(IMAVMSPController control, MSPConfig config,  ILocalMap map, IVisualStreamHandler<T> streamer) {

		this.model	= control.getCurrentModel();
		this.map 	= map;

		this.max_distance = config.getFloatProperty("map_max_distance", "3.00f");
		System.out.println("[col] Max planning distance set to "+max_distance);
		this.min_altitude = config.getFloatProperty("map_min_altitude", String.valueOf(MIN_ALTITUDE));
		System.out.println("[col] Min.altitude set to "+min_altitude);


		if(streamer !=null)
			streamer.registerOverlayListener(ctx -> {
				if(current_min_distance<Double.MAX_VALUE)
					ctx.drawString(String.format("Min.distance %.1fm",current_min_distance), 10, 50);
			});
	}

	@Override
	public void process(MAVDepthVisualOdometry<GrayU8,GrayU16> odometry, GrayU16 depth, GrayU8 gray) {

		getModelToState(model,current);

		// Do not update map if loaded from storage or vehicle is lower than MIN_ALTITUDE
		//		if((model.hud.ar < min_altitude || map.isLoaded()))
		//			return;

		model.grid.tms = model.sys.getSynchronizedPX4Time_us();

		current_min_distance = Double.MAX_VALUE;
		for(int x = 10;x < gray.getWidth()-10;x = x + 2) {

			point_min.set(0,0,Double.MAX_VALUE);
			for(int y = YBAND_LOW;y <= YBAND_HIGH;y = y + 5) {
				try {
					point = odometry.getPoint3DFromPixel(x,y);
					if(point != null && point.z < point_min.z)
						point_min.set(point);
				} catch(Exception e) {
					continue;
				}
			}

			if(point_min.z<Double.MAX_VALUE && !point_min.isIdentical(0,0,0)) {
				SePointOps_F64.transform(current,point_min,point_ned);
				MSP3DUtils.toNED(point_ned);

				if( //model.state.l_z < (- min_altitude )  &&
						point_min.z < max_distance ) {
					map.update(model.state.l_x, model.state.l_y,point_ned);
					if(point_min.z<current_min_distance)
						current_min_distance = point_min.z;
				}
			}
		}

	}


	public void reset(float x, float y, float z) {
		// reset map if local position was set to 0
		if(x==0 && y==0)
			map.reset();
	}

	private Se3_F64 getModelToState(DataModel m, Se3_F64 state) {
		ConvertRotation3D_F64.eulerToMatrix(EulerType.ZXY,
				m.attitude.r,
				m.attitude.p,
				m.attitude.y,
				state.getRotation());

		state.getTranslation().y = m.state.l_z;
		state.getTranslation().x = m.state.l_y;
		state.getTranslation().z = m.state.l_x;

		return state;
	}
}
