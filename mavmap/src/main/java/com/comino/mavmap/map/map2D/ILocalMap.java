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

package com.comino.mavmap.map.map2D;



import com.comino.mavcom.model.DataModel;
import com.comino.mavcom.struct.Polar3D_F32;
import com.comino.mavmap.map.map2D.filter.ILocalMapFilter;
import com.comino.mavodometry.estimators.IMAVMapper;

import boofcv.struct.image.GrayU16;

public interface ILocalMap extends IMAVMapper {


	public void nearestObstacle(Polar3D_F32 result);

	public float getMaxDistanceInDirection(float direction);

	public void processWindow(float lpos_x, float lpos_y);

	public int getWindowValue(int x, int y);

	public int getWindowDimension();

	public int getMapDimension();

	public int getCellSize_mm();

	public GrayU16 getMap();

	public void applyMapFilter(ILocalMapFilter filter);

	public short[][] get();

	public void reset();

	public void init();

	public void setDataModel(DataModel model);

	public void toDataModel(boolean debug);

	public void setIsLoaded( boolean loaded);

	public boolean isLoaded();

	public String windowToString();


}
