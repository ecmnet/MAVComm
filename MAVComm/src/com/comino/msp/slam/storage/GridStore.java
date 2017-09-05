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


package com.comino.msp.slam.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.comino.msp.model.DataModel;

public class GridStore {

	private static final String DIRECTORY_NAME = "/grid_store";

	private static GridStore gridStore 	= null;

	private DataModel model				= null;
	private String    path              = null;


	public static GridStore getInstance(DataModel model) {
		if(gridStore==null)
			gridStore = new GridStore(model);
		return gridStore;
	}

	public static GridStore getInstance() {
		return gridStore;
	}

	public GridStore(DataModel model) {
		this.model = model;

		try {
			String dirName = System.getProperty("user.home") + DIRECTORY_NAME;
			File dir = new File(dirName);
			if(!dir.exists())
				dir.mkdir();
			path = dir.getAbsolutePath();
			System.out.println("GridStore directory set to "+path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void store_current_grid() {
		GridContainer gc = new GridContainer();
		gc.grid        = model.grid.clone();
		gc.home        = model.base.clone();
		gc.created_tms = System.currentTimeMillis();

		// TODO: generate file and store grid as json-encoded file
	}

	private String generateFilename(GridContainer gc) {
		// Idea: file name pattern:  [LAT]_[LON]_[TMS].grid
		return null;
	}

	private List<String> getListOfSurroundingGridsNames(float max_distance_m) {
		// Idea: scans all grids within a certain range by filename for access and returns a list of filenames
		List<String> gridNames = new ArrayList<String>();

		return gridNames;
	}

	public static void main(String[] args) {
		DataModel m = new DataModel();
		GridStore store = GridStore.getInstance(m);

	}

}
